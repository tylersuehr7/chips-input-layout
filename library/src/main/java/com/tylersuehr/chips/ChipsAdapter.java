package com.tylersuehr.chips;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.RelativeLayout;
import com.tylersuehr.chips.data.Chip;
import com.tylersuehr.chips.data.ChipDataSource;
import com.tylersuehr.chips.data.ChipChangedObserver;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * Used by {@link ChipsInputLayout} to adapt the selected chips into views and display
 * the EditText to allow the user to type text in for chips.
 *
 * This adapter should also afford the following abilities/features:
 * (1) Allow user to create custom chips, if the options permit it.
 * (2) Allow user to remove any chip by pressing delete on an empty input.
 * (3) Allow the user to see chip details, if the options permit it.
 *
 * We should also observe changes to {@link ChipDataSource} to update the UI accordingly.
 *
 * @author Tyler Suehr
 * @version 1.0
 */
class ChipsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        ChipEditText.OnKeyboardListener, ChipChangedObserver {
    private static final int TYPE_INPUT = 0;
    private static final int TYPE_ITEM  = 1;

    private final ChipDataSource chipDataSource;
    private final ChipOptions chipOptions;
    private final ChipsInputLayout chipsInput;
    private final ChipEditText editText;


    ChipsAdapter(ChipsInputLayout chipsInput) {
        this.chipsInput = chipsInput;
        this.chipDataSource = chipsInput.getChipDataSource();
        this.chipOptions = chipsInput.getChipOptions();
        this.editText = chipsInput.getThemedChipsEditText();
        this.editText.setKeyboardListener(this);

        // Register an observer on the chip data source
        this.chipDataSource.addChipChangedObserver(this);
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
            cHolder.chipView.inflateWithChip(chipDataSource.getSelectedChip(position));

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
        return chipDataSource.getSelectedChip(position).hashCode();
    }


    /**
     * Called when the IME_ACTION_DONE option is pressed on a software or
     * physical keyboard.
     *
     * @param text Current text in the EditText
     */
    @Override
    public void onKeyboardActionDone(String text) {
        if (TextUtils.isEmpty(text) || !chipOptions.allowCustomChips) { return; }

        // Create a custom, non-filterable, chip and add to selected list
        Chip chip = new NonFilterableChip(text);

        // Clear the input before taking chip so we don't need to update UI twice
        this.editText.setText("");

        // This will trigger callback, which calls notifyDataSetChanged()
        this.chipDataSource.addSelectedChip(chip);
    }

    /**
     * Called when the backspace (KEYCODE_DEL) is pressed on a software or
     * physical keyboard.
     */
    @Override
    public void onKeyboardBackspace() {
        // Only remove the last chip if the input was empty
        if (chipDataSource.getSelectedChips().size() > 0 && editText.getText().length() == 0) {
            // Will trigger notifyDataSetChanged()
            chipDataSource.replaceChip(chipDataSource.getSelectedChips().size() - 1);
        }
    }

    @Override
    public void onChipDataSourceChanged() {
        notifyDataSetChanged();
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
                // Will trigger notifyDataSetChanged()
                chipDataSource.replaceChip(position);
            }
        });

        // Show detailed chip, if possible
        if (chipOptions.showDetailedChips) {
            chipView.setOnChipClicked(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Get chip position
                    int[] coord = new int[2];
                    view.getLocationInWindow(coord);

                    // Create a detailed chip view to show
                    final DetailedChipView detailedChipView = chipsInput
                            .getThemedDetailedChipView(chipDataSource.getSelectedChip(position));
                    setDetailedChipViewPosition(detailedChipView, coord);

                    // Remove the button when the chip is delete
                    detailedChipView.setOnDeleteClicked(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Will trigger notifyDataSetChanged()
                            chipDataSource.replaceChip(position);
                            detailedChipView.fadeOut();
                        }
                    });
                }
            });
        } else {
            chipView.setOnChipClicked(null);
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