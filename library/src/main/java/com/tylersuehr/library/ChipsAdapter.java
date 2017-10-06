package com.tylersuehr.library;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tylersuehr.library.data.Chip;
import com.tylersuehr.library.data.ChipDataSource;
import com.tylersuehr.library.data.ChipDataSourceObserver;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * Used by {@link ChipsInput} to adapt the selected chips into views and display
 * the EditText to allow the user to type text in for chips.
 *
 * @author Tyler Suehr
 * @version 1.0
 */
class ChipsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        View.OnKeyListener, TextView.OnEditorActionListener, ChipDataSourceObserver {
    private static final int TYPE_INPUT = 0;
    private static final int TYPE_ITEM  = 1;

    private final ChipDataSource chipDataSource;
    private final ChipOptions chipOptions;
    private final ChipsInput chipsInput;
    private final EditText editText;


    ChipsAdapter(ChipsInput chipsInput) {
        this.chipsInput = chipsInput;
        this.chipDataSource = chipsInput.getChipDataSource();
        this.chipOptions = chipsInput.getChipOptions();
        this.editText = chipsInput.getChipsInput();

        this.editText.setOnKeyListener(this);
        this.editText.setOnEditorActionListener(this);

        // Register an observer on the chip data source
        this.chipDataSource.registerObserver(this);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewType == TYPE_INPUT
                ? new InputHolder(editText)
                : new ChipHolder(chipsInput.getThemedChipView());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == chipDataSource.getSelectedChips().size()) { // For EditText
            if (chipDataSource.getSelectedChips().size() == 0) {
                this.editText.setHint(chipOptions.hint);
            }

            // Auto fit the EditText
            autoFitEditText();
        } else if (getItemCount() > 1) { // For chips
            ChipHolder cHolder = (ChipHolder)holder;
            cHolder.chipView.inflateWithChip(chipDataSource.getSelectedChips().get(position));

            // TODO POSSIBLE OPTIMIZATION
            // Add custom listeners for click and delete on ChipView and then implement them on
            // the ChipViewHolder class. Get the selected position using the ChipViewHolder's
            // adapter position.
            handleClickOnEditText(cHolder.chipView, position);
        }
    }

    @Override
    public int getItemCount() {
        return chipDataSource.getSelectedChips().size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == chipDataSource.getSelectedChips().size() ? TYPE_INPUT : TYPE_ITEM;
    }

    @Override
    public long getItemId(int position) {
        return chipDataSource.getSelectedChips().get(position).hashCode();
    }

    /**
     * Only used to detect when the 'enter' button is pressed on the keyboard.
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE && !TextUtils.isEmpty(v.getText())) {
            // Create a custom, non-filterable, chip and add to selected list
            Chip chip = new BaseChip(v.getText().toString(), null);
            chip.setFilterable(false);

            // Empty input before taking chip so we don't have to update UI twice
            v.setText("");

            this.chipDataSource.takeChip(chip);

//            v.setText("");
//            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    /**
     * Only used to detect backspace events on the keyboard.
     */
    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (keyEvent.getAction() == KeyEvent.ACTION_DOWN
                && keyEvent.getKeyCode() == KeyEvent.KEYCODE_DEL) { // Backspace key
            // Just remove the last chip, if possible
            if (chipDataSource.getSelectedChips().size() > 0 && editText.getText().toString().length() == 0) {
                removeChip(chipDataSource.getSelectedChips().size() - 1);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onChipDataSourceChanged(@Nullable Chip affectedChip) {
        notifyDataSetChanged();
    }

    /**
     * Removes the chip from the data source or directly if it was a custom chip.
     * @param position Position of chip to remove
     */
    private void removeChip(int position) {
        final Chip chip = chipDataSource.getSelectedChips().get(position);
        this.chipDataSource.replaceChip(chip);
    }

    private void autoFitEditText() {
        // Minimum width of EditText = 50dp
        ViewGroup.LayoutParams lp = editText.getLayoutParams();
        lp.width = Utils.dp(50);
        this.editText.setLayoutParams(lp);

        // Listen to changes in the tree
        this.editText.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Get right of recycler and left of edit text
                int right = chipsInput.getChipsRecyclerView().getRight();
                int left = editText.getLeft();

                // EditText shall fill the space
                ViewGroup.LayoutParams lp = editText.getLayoutParams();
                lp.width = right - left - Utils.dp(8);
                editText.setLayoutParams(lp);

                // Request focus
                editText.requestFocus();

                // Remove the tree listener
                editText.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private void handleClickOnEditText(final ChipView chipView, final int position) {
        // Delete chip
        chipView.setOnDeleteClicked(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeChip(position);
            }
        });

        // Show detailed chip, if possible
        if (chipOptions.showDetailedChip) {
            chipView.setOnChipClicked(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Get chip position
                    int[] coord = new int[2];
                    view.getLocationInWindow(coord);

                    // Create a detailed chip view to show
                    final DetailedChipView detailedChipView = chipsInput.getThemedDetailedChipView(
                            chipDataSource.getSelectedChips().get(position));
                    setDetailedChipViewPosition(detailedChipView, coord);

                    // Remove the button when the chip is delete
                    detailedChipView.setOnDeleteClicked(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            removeChip(position);
                            detailedChipView.fadeOut();
                        }
                    });
                }
            });
        }
    }

    private void setDetailedChipViewPosition(final DetailedChipView detailedChipView, int[] coord) {
        // Window width
        final ViewGroup rootView = (ViewGroup)chipsInput.getChipsRecyclerView().getRootView();
        int windowWidth = Utils.getWindowWidth(rootView.getContext());

        // Chip size
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                Utils.dp(300),
                Utils.dp(100)
        );
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        // Determine the detailed chip's alignment inside the window
        if (coord[0] <= 0) { // Left align
            lp.leftMargin = 0;
            lp.topMargin = coord[1] - Utils.dp(13);
            detailedChipView.alignLeft();
        } else if (coord[0] + Utils.dp(300) > windowWidth + Utils.dp(13)) { // Right align
            lp.leftMargin = windowWidth - Utils.dp(300);
            lp.topMargin = coord[1] - Utils.dp(13);
            detailedChipView.alignRight();
        } else { // Same position as chip
            lp.leftMargin = coord[0] - Utils.dp(13);
            lp.topMargin = coord[1] - Utils.dp(13);
        }

        // Show the detailed chip view
        rootView.addView(detailedChipView, lp);
        detailedChipView.fadeIn();
    }

    private static class InputHolder extends RecyclerView.ViewHolder {
        InputHolder(EditText editText) {
            super(editText);
        }
    }

    private static class ChipHolder extends RecyclerView.ViewHolder {
        ChipView chipView;

        ChipHolder(ChipView chipView) {
            super(chipView);
            this.chipView = chipView;
        }
    }
}