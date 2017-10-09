package com.tylersuehr.library;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.tylersuehr.library.data.Chip;
import com.tylersuehr.library.data.ChipDataSource;
import com.tylersuehr.library.data.ListChipDataSource;
import java.util.List;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public class ChipsInputLayout extends MaxHeightScrollView
        implements FilterableChipsAdapter.OnFilteredChipClickListener {

    private static final String TAG = "CHIPS_INPUT";

    /* Stores and manages all our chips */
    private ChipDataSource chipDataSource;

    /* Stores the mutable properties of our ChipsInput (XML attrs) */
    private ChipOptions chipOptions;

    /* Allows user to type text into the ChipsInput */
    private ChipEditText chipsEditText;

    /* Displays selected chips and chips EditText */
    private RecyclerView chipsRecycler;
    private ChipsAdapter chipsAdapter;

    /* Displays filtered chips */
    private FilterableRecyclerView filterableRecyclerView;
    private FilterableChipsAdapter filterableChipsAdapter;


    public ChipsInputLayout(Context context) {
        this(context, null);
    }

    public ChipsInputLayout(Context c, AttributeSet attrs) {
        super(c, attrs);
        this.chipOptions = new ChipOptions(c, attrs);
        this.chipDataSource = new ListChipDataSource();

        // Inflate the view
        inflate(c, R.layout.chips_input_view, this);

        // Setup the chips recycler view
        this.chipsAdapter = new ChipsAdapter(this);
        this.chipsRecycler = findViewById(R.id.chips_recycler);
        this.chipsRecycler.setLayoutManager(ChipsLayoutManager.newBuilder(c)
                .setOrientation(ChipsLayoutManager.HORIZONTAL).build());
        this.chipsRecycler.setNestedScrollingEnabled(false);
        this.chipsRecycler.setAdapter(chipsAdapter);
    }

    @Override
    public void onFilteredChipClick(Chip chip) {
        // Hide the filterable recycler
        this.filterableRecyclerView.fadeOut();

        // Clear the input and refresh the chips recycler
        this.chipsEditText.setText("");
        this.chipsAdapter.notifyDataSetChanged();

        // Close the software keyboard
        hideKeyboard();
    }

    /**
     * Sets and stores a list of filterable chips on the data source.
     * @param chips List of {@link Chip}
     */
    public void setFilterableChipList(List<? extends Chip> chips) {
        this.chipDataSource.setFilterableChips(chips);

        // Setup the filterable recycler when new filterable data has been set
        createAndSetupFilterableRecyclerView();
    }

    ChipDataSource getChipDataSource() {
        return chipDataSource;
    }

    ChipOptions getChipOptions() {
        return chipOptions;
    }

    RecyclerView getChipsRecyclerView() {
        return chipsRecycler;
    }

    /**
     * Lazy loads the input for the user to enter chip titles.
     * @return {@link EditText}
     */
    ChipEditText getThemedChipsEditText() {
        if (chipsEditText == null) {
            this.chipsEditText = new ChipEditText(getContext());
            this.chipsEditText.setLayoutParams(new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));

            int padding = Utils.dp(8);
            this.chipsEditText.setPadding(padding, padding, padding, padding);

            // Setup the chips options for the input
            this.chipsEditText.setBackgroundResource(android.R.color.transparent);
            if (chipOptions.textColorHint != null) {
                this.chipsEditText.setHintTextColor(chipOptions.textColorHint);
            }
            if (chipOptions.textColor != null) {
                this.chipsEditText.setTextColor(chipOptions.textColor);
            }
            this.chipsEditText.setHint(chipOptions.hint);

            // Prevent fullscreen on landscape
            this.chipsEditText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI|EditorInfo.IME_ACTION_DONE);
            this.chipsEditText.setPrivateImeOptions("nm");

            // No suggestions
            this.chipsEditText.setInputType(InputType.TYPE_TEXT_VARIATION_FILTER|InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

            // Listen to text changes
            this.chipsEditText.addTextChangedListener(new ChipInputTextChangedHandler());
        }
        return chipsEditText;
    }

    /**
     * Creates a new {@link ChipView} with its theme set from properties defined
     * in {@link #chipOptions}.
     * @return {@link ChipView}
     */
    ChipView getThemedChipView() {
        int padding = Utils.dp(4);

        ChipView chipView = new ChipView.Builder(getContext())
                .titleTextColor(chipOptions.chipLabelColor)
                .hasAvatarIcon(chipOptions.hasAvatarIcon)
                .deletable(chipOptions.chipDeletable)
                .deleteIcon(chipOptions.chipDeleteIcon)
                .deleteIconColor(chipOptions.chipDeleteIconColor)
                .backgroundColor(chipOptions.chipBackgroundColor)
                .build();

        chipView.setPadding(padding, padding, padding, padding);
        return chipView;
    }

    /**
     * Creates a new {@link DetailedChipView} with its theme set from properties
     * defined in {@link #chipOptions}.
     * @param chip {@link Chip}
     * @return {@link DetailedChipView}
     */
    DetailedChipView getThemedDetailedChipView(Chip chip) {
        return new DetailedChipView.Builder(getContext())
                .chip(chip)
                .textColor(chipOptions.detailedChipTextColor)
                .backgroundColor(chipOptions.detailedChipBackgroundColor)
                .deleteIconColor(chipOptions.detailedChipDeleteIconColor)
                .build();
    }

    /**
     * Creates a new filterable recycler, sets up its properties from the chip options,
     * creates a new filterable adapter for the recycler, and adds it as a child view to
     * the root ViewGroup.
     */
    private void createAndSetupFilterableRecyclerView() {
        // Create a new filterable recycler view
        this.filterableRecyclerView = new FilterableRecyclerView(getContext());

        // Set the filterable properties from the options
        this.filterableRecyclerView.setBackgroundColor(Color.WHITE);
        ViewCompat.setElevation(filterableRecyclerView, 4f);

        if (chipOptions.filterableListBackgroundColor != null) {
            this.filterableRecyclerView.getBackground().setColorFilter(
                    chipOptions.filterableListBackgroundColor.getDefaultColor(), PorterDuff.Mode.SRC_ATOP);
        }

        // Create and set the filterable chips adapter
        this.filterableChipsAdapter = new FilterableChipsAdapter(getContext(), this, chipOptions, chipDataSource);
        this.filterableRecyclerView.setAdapter(this, filterableChipsAdapter);

        // To show our filterable recycler view, we need to make sure our ChipsInput has already
        // been displayed on the screen so we can access its root view
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Get the root view of the ChipsInput
                ViewGroup rootView = (ViewGroup)getRootView();

                // Create the layout params for our filterable recycler view
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                        Utils.getWindowWidth(getContext()),
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    lp.bottomMargin = Utils.getNavBarHeight(getContext());
                }

                // Add the filterable recycler to our root with the specified layout params
                rootView.addView(filterableRecyclerView, lp);

                // Remove the view tree listener
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    /**
     * Hides the software keyboard from the chips edit text.
     */
    private void hideKeyboard() {
        ((InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(chipsEditText.getWindowToken(), 0);
    }


    /**
     * Implementation of {@link TextWatcher} that handles two things for us:
     * (1) Hides the filterable recycler if the user removes all the text from input.
     * (2) Tells the filterable recycler to filter the chips when the user enters text.
     */
    private final class ChipInputTextChangedHandler implements TextWatcher {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (filterableRecyclerView != null) {
                // Hide the filterable recycler if there is no filter.
                // Filter the recycler if there is a filter
                if (TextUtils.isEmpty(s)) {
                    filterableRecyclerView.fadeOut();
                } else {
                    filterableRecyclerView.filterChips(s);
                }
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void afterTextChanged(Editable s) {}
    }
}