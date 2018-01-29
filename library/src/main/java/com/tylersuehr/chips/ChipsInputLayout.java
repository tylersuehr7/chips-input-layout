package com.tylersuehr.chips;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.tylersuehr.chips.data.Chip;
import com.tylersuehr.chips.data.ChipDataSource;
import com.tylersuehr.chips.data.ListChipDataSource;

import java.util.List;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * Chips are a design concept specified in the Google Material Design Guide.
 *
 * The purpose of this view, and this library, is for displaying chips in a flowing
 * layout, and allowing the user to input to filter any filterable chips or to create
 * custom chips.
 *
 * Notes: by default, the chip data source is {@link ListChipDataSource}.
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public class ChipsInputLayout extends MaxHeightScrollView
        implements FilterableChipsAdapter.OnFilteredChipClickListener {
    /* Stores mutable properties for our library */
    private final ChipOptions mOptions;
    /* Stores the source of all the chips */
    private ChipDataSource mDataSource;
    /* Stores reference to the user input */
    private ChipsEditText mChipsInput;

    /* Displays selected chips and chips EditText */
    private final RecyclerView mChipsRecycler;
    private final ChipsAdapter mChipsAdapter;

    /* Displays filtered chips */
    private FilterableRecyclerView mFilteredRecycler;
    private FilterableChipsAdapter mFilteredAdapter;

    /* Used to validate selected chips */
    private ChipValidator mValidator;


    public ChipsInputLayout(Context context) {
        this(context, null);
    }

    public ChipsInputLayout(Context c, AttributeSet attrs) {
        super(c, attrs);
        mOptions = new ChipOptions(c, attrs);
        mDataSource = new ListChipDataSource();

        // Inflate the view
        inflate(c, R.layout.chips_input_view, this);

        // Setup the chips recycler view
        mChipsAdapter = new ChipsAdapter(
                mDataSource, loadChipsInput(), mOptions);
        mChipsRecycler = findViewById(R.id.chips_recycler);
        mChipsRecycler.addItemDecoration(new DefaultChipDecor(c));
        mChipsRecycler.setLayoutManager(ChipsLayoutManager.newBuilder(c).build());
        mChipsRecycler.setNestedScrollingEnabled(false);
        mChipsRecycler.setAdapter(mChipsAdapter);

        // Set the max height from options
        setMaxHeight(Utils.dp(40) * mOptions.mMaxRows);
    }

    @Override
    public void onFilteredChipClick(Chip chip) {
        // Hide the filterable recycler
        mFilteredRecycler.fadeOut();

        // Clear the input and refresh the chips recycler
        mChipsInput.setText("");
        mChipsAdapter.notifyDataSetChanged();

        // Close the software keyboard
        hideKeyboard();
    }

    /**
     * Sets and stores a list of chips that are filterable and updates the
     * UI to enable the filterable RecyclerView accordingly.
     *
     * Note: this should only be called if you want the user to be able to
     * filter pre-existing (filterable chip list) chips.
     *
     * @param chips List of {@link Chip}
     */
    public void setFilterableChipList(List<? extends Chip> chips) {
        mDataSource.setFilterableChips(chips);

        // Setup the filterable recycler when new
        // filterable data has been set
        loadFilterableRecycler();
    }

    /**
     * Sets and stores a list of chips that are selected and updates the UI
     * to display them accordingly.
     *
     * @param chips List of {@link Chip}
     */
    public void setSelectedChipList(List<? extends Chip> chips) {
        // Set the selected chips in the data source
        mDataSource.getSelectedChips().clear();
        mDataSource.getSelectedChips().addAll(chips);

        // Update the chips UI display
        mChipsAdapter.notifyDataSetChanged();
    }

    /**
     * Adds a new chip to the filterable chips, which will update the UI
     * accordingly because of the change observers.
     *
     * @param chip {@link Chip}
     */
    public void addFilteredChip(Chip chip) {
        // Ensure that the chip is actually filterable
        chip.setFilterable(true);

        // Ensure that the chip is not already in the data source
        if (mDataSource.existsInDataSource(chip)) {
            throw new IllegalArgumentException("Chip already exists in the data source!");
        }

        // Using the method on data source will update UI
        mDataSource.addFilteredChip(chip);

        // Create the filterable recycler at this point, if needed
        loadFilterableRecycler();
    }

    /**
     * Adds a new chip to the selected chips, which will update the UI
     * accordingly because of the change observers.
     *
     * @param chip {@link Chip}
     */
    public void addSelectedChip(Chip chip) {
        // Ensure that the chip is not already in the data source
        if (mDataSource.existsInDataSource(chip)) {
            throw new IllegalArgumentException("Chip already exists in the data source!");
        }

        // Using the method on data source will update UI
        mDataSource.addSelectedChip(chip);
    }

    /**
     * Clears all the filterable chips, which will update the UI accordingly
     * because of the change observers.
     */
    public void clearFilteredChips() {
        mDataSource.clearFilteredChips();
    }

    /**
     * Clears all the selected chips, which will update the UI accordingly
     * because of the change observers.
     */
    public void clearSelectedChips() {
        mDataSource.clearSelectedChips();
    }

    /**
     * Gets all the currently selected chips.
     *
     * @return List of {@link Chip}
     */
    public List<? extends Chip> getSelectedChips() {
        return mDataSource.getSelectedChips();
    }

    /**
     * Gets all the currently filtered chips.
     * @see #getOriginalFilterableChips() if you want the original list of chips
     *
     * @return List of {@link Chip}
     */
    public List<? extends Chip> getFilteredChips() {
        return mDataSource.getFilteredChips();
    }

    /**
     * Gets all the originally set filterable chips.
     *
     * @return List of {@link Chip}
     */
    public List<? extends Chip> getOriginalFilterableChips() {
        return mDataSource.getOriginalChips();
    }

    /**
     * Gets a selected chip using the given index.
     *
     * @param position Position of chip
     * @return {@link Chip}
     */
    public Chip getSelectedChipByPosition(int position) {
        return mDataSource.getSelectedChip(position);
    }

    /**
     * Gets a selected chip using the given ID, if possible.
     *
     * @param id ID of the selected chip
     * @return {@link Chip}
     */
    public Chip getSelectedChipById(Object id) {
        for (Chip chip : mDataSource.getSelectedChips()) {
            if (chip.getId() != null && chip.getId().equals(id)) {
                return chip;
            }
        }
        return null;
    }

    /**
     * Gets a selected chip with exactly the given title or like the given title.
     *
     * @param title Title to search for
     * @param exactlyEqual True if chip title should exactly match title
     * @return {@link Chip}
     */
    public Chip getSelectedChipByTitle(String title, boolean exactlyEqual) {
        for (Chip chip : mDataSource.getSelectedChips()) {
            if ((exactlyEqual && chip.getTitle().equals(title)) ||
                    (!exactlyEqual && chip.getTitle().toLowerCase().contains(title.toLowerCase()))) {
                return chip;
            }
        }
        return null;
    }

    /**
     * Gets a selected chip with exactly the given subtitle or like the given subtitle.
     *
     * @param subtitle Subtitle to search for
     * @param exactlyEqual True if chip subtitle should exactly match subtitle
     * @return {@link Chip}
     */
    public Chip getSelectedChipBySubtitle(String subtitle, boolean exactlyEqual) {
        for (Chip chip : mDataSource.getSelectedChips()) {
            if (chip.getSubtitle() == null) { continue; }
            if ((exactlyEqual && chip.getSubtitle().equals(subtitle)) ||
                    (!exactlyEqual && chip.getSubtitle().toLowerCase().contains(subtitle.toLowerCase()))) {
                return chip;
            }
        }
        return null;
    }

    /**
     * Gets a filtered chip using the given index.
     *
     * @param position Position of chip
     * @return {@link Chip}
     */
    public Chip getFilteredChipPosition(int position) {
        return mDataSource.getFilteredChip(position);
    }

    /**
     * Gets a filtered chip using the given ID, if possible.
     *
     * @param id Filtered chip's ID
     * @return {@link Chip}
     */
    public Chip getFilteredChipById(Object id) {
        for (Chip chip : mDataSource.getFilteredChips()) {
            if (chip.getId() != null && chip.getId().equals(id)) {
                return chip;
            }
        }
        return null;
    }

    /**
     * Gets a filtered chip with exactly the given title or like the given title.
     *
     * @param title Title to search for
     * @param exactlyEqual True if filtered chip title should exactly match title
     * @return {@link Chip}
     */
    public Chip getFilteredChipByTitle(String title, boolean exactlyEqual) {
        for (Chip chip : mDataSource.getFilteredChips()) {
            if ((exactlyEqual && chip.getTitle().equals(title)) ||
                    (!exactlyEqual && chip.getTitle().toLowerCase().contains(title.toLowerCase()))) {
                return chip;
            }
        }
        return null;
    }

    /**
     * Gets a filtered chip with exactly the given subtitle or like the given subtitle.
     *
     * @param subtitle Subtitle to search for
     * @param exactlyEqual True if filtered chip subtitle should exactly match subtitle
     * @return {@link Chip}
     */
    public Chip getFilteredChipBySubtitle(String subtitle, boolean exactlyEqual) {
        for (Chip chip : mDataSource.getFilteredChips()) {
            if (chip.getSubtitle() == null) { continue; }
            if ((exactlyEqual && chip.getSubtitle().equals(subtitle)) ||
                    (!exactlyEqual && chip.getSubtitle().toLowerCase().contains(subtitle.toLowerCase()))) {
                return chip;
            }
        }
        return null;
    }

    /**
     * Checks if the given chip exists in either the filterable or selected chips.
     *
     * @param chip {@link Chip}
     * @return True if chip exists in filterable or selected chips
     */
    public boolean doesChipExist(Chip chip) {
        return mDataSource.existsInDataSource(chip);
    }

    /**
     * Checks if the given chip exists in the filtered chips.
     *
     * @param chip {@link Chip}
     * @return True if chip exists in filtered chips
     */
    public boolean isChipFiltered(Chip chip) {
        return mDataSource.existsInFiltered(chip);
    }

    /**
     * Checks if the given chip exists in the selected chips.
     *
     * @param chip {@link Chip}
     * @return True if chip exists in selected chips
     */
    public boolean isChipSelected(Chip chip) {
        return mDataSource.existsInSelected(chip);
    }

    /**
     * Validates the given chip using {@link #mValidator}.
     *
     * @param chip {@link Chip}
     * @return True if chip is valid, or no chip mValidator is set
     */
    public boolean validateChip(Chip chip) {
        return mValidator == null || mValidator.validate(chip);
    }

    /**
     * Validates all the selected chips using {@link #mValidator}.
     *
     * @return True if all selected chips are valid, or no chip mValidator is set
     */
    public boolean validateSelectedChips() {
        if (mValidator != null) {
            for (Chip chip : mDataSource.getSelectedChips()) {
                if (!mValidator.validate(chip)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Sets the chip mValidator to valid chips.
     *
     * @param validator {@link ChipValidator}
     */
    public void setChipValidator(ChipValidator validator) {
        mValidator = validator;
    }

    /**
     * Adds an observer to watch selection events on the chip data source.
     *
     * @param observer {@link ChipDataSource.SelectionObserver}
     */
    public void addSelectionObserver(ChipDataSource.SelectionObserver observer) {
        mDataSource.addSelectionObserver(observer);
    }

    /**
     * Removes an observer from watching selection events on the chip data source.
     *
     * @param observer {@link ChipDataSource.SelectionObserver}
     */
    public void removeSelectionObserver(ChipDataSource.SelectionObserver observer) {
        mDataSource.removeSelectionObserver(observer);
    }

    /**
     * Adds an observer to watch for any change events on the chip data source.
     *
     * Note: please use this conservatively!
     *
     * @param observer {@link ChipDataSource.ChangeObserver}
     */
    public void addChangeObserver(ChipDataSource.ChangeObserver observer) {
        mDataSource.addChangedObserver(observer);
    }

    /**
     * Removes an observer from watching any change events on the chip data source.
     *
     * @param observer {@link ChipDataSource.ChangeObserver}
     */
    public void removeChangeObserver(ChipDataSource.ChangeObserver observer) {
        mDataSource.removeChangedObserver(observer);
    }

    /**
     * Changes the chip data source being used to manipulate chips, which will
     * update the UI accordingly.
     *
     * Note: will retain existing observers from old data source, but all chips
     * in the old data source will be cleared!
     *
     * @param dataSource {@link ChipDataSource}
     */
    public void changeChipDataSource(ChipDataSource dataSource) {
        mDataSource.cloneObservers(dataSource);
        mDataSource = dataSource;
        mChipsAdapter.notifyDataSetChanged();
    }

    /**
     * Gets an instance of {@link LetterTileProvider}.
     * @return {@link LetterTileProvider}
     */
    public LetterTileProvider getLetterTileProvider() {
        return LetterTileProvider.getInstance(getContext());
    }

    public void setInputTextColor(ColorStateList textColor) {
        mOptions.mTextColor = textColor;
        if (mChipsInput != null) { // Can be null because its lazy loaded
            mChipsInput.setTextColor(textColor);
        }
    }

    public void setInputHintTextColor(ColorStateList textColorHint) {
        mOptions.mTextColorHint = textColorHint;
        if (mChipsInput != null) { // Can be null because its lazy loaded
            mChipsInput.setHintTextColor(textColorHint);
        }
    }

    public void setInputHint(CharSequence hint) {
        mOptions.mHint = hint;
        if (mChipsInput != null) { // Can be null because its lazy loaded
            mChipsInput.setHint(hint);
        }
    }

    public void setChipDeleteIconColor(ColorStateList deleteIconColor) {
        mOptions.mChipDeleteIconColor = deleteIconColor;
    }

    public void setChipBackgroundColor(ColorStateList chipBackgroundColor) {
        mOptions.mChipBackgroundColor = chipBackgroundColor;
    }

    public void setChipTitleTextColor(ColorStateList chipTitleTextColor) {
        mOptions.mChipTextColor = chipTitleTextColor;
    }

    public void setChipDeleteIcon(Drawable chipDeleteIcon) {
        mOptions.mChipDeleteIcon = chipDeleteIcon;
    }

    public void setChipDeleteIcon(@DrawableRes int res) {
        mOptions.mChipDeleteIcon = ContextCompat.getDrawable(getContext(), res);
    }

    public void setShowChipAvatarEnabled(boolean hasAvatar) {
        mOptions.mShowAvatar = hasAvatar;
    }

    public void setShowDetailedChipsEnabled(boolean enabled) {
        mOptions.mShowDetails = enabled;
    }

    public void setChipsDeletable(boolean enabled) {
        mOptions.mShowDelete = enabled;
    }

    public void setChipDetailsDeleteIconColor(ColorStateList detailedChipIconColor) {
        mOptions.mDetailsChipDeleteIconColor = detailedChipIconColor;
    }

    public void setChipDetailsBackgroundColor(ColorStateList detailedChipBackgroundColor) {
        mOptions.mDetailsChipBackgroundColor = detailedChipBackgroundColor;
    }

    public void setChipDetailsTextColor(ColorStateList detailedChipTextColor) {
        mOptions.mDetailsChipTextColor = detailedChipTextColor;
    }

    public void setFilterListBackgroundColor(ColorStateList backgroundColor) {
        mOptions.mFilterableListBackgroundColor = backgroundColor;
    }

    public void setFilterListTextColor(ColorStateList textColor) {
        mOptions.mFilterableListTextColor = textColor;
    }

    public void setFilterListElevation(float elevation) {
        mOptions.mFilterableListElevation = elevation;
    }

    public void setCustomChipsEnabled(boolean enabled) {
        mOptions.mAllowCustomChips = enabled;
    }

    public void setMaxRows(int rows) {
        mOptions.mMaxRows = rows;
        setMaxHeight(Utils.dp(40) * mOptions.mMaxRows);
    }

    public void setTypeface(Typeface typeface) {
        mOptions.mTypeface = typeface;
        LetterTileProvider.getInstance(getContext()).setTypeface(typeface);
        if (mChipsInput != null) {
            mChipsInput.setTypeface(typeface);
        }
    }

    /**
     * Sets the image renderer used to load chip avatars.
     * @param renderer {@link ChipImageRenderer}
     */
    public void setImageRenderer(ChipImageRenderer renderer) {
        mOptions.mImageRenderer = renderer;
    }

    public ChipDataSource getChipDataSource() {
        return mDataSource;
    }

    ChipsEditText loadChipsInput() {
        if (mChipsInput == null) {
            mChipsInput = new ChipsEditText(getContext());
            mChipsInput.setChipOptions(mOptions);
            mChipsInput.addTextChangedListener(new ChipInputTextChangedHandler());
        }
        return mChipsInput;
    }

    private void loadFilterableRecycler() {
        if (mFilteredRecycler == null) {
            // Create and set the filterable chips adapter
            mFilteredAdapter = new FilterableChipsAdapter(mDataSource, mOptions, this);

            // Create a new filterable recycler view
            mFilteredRecycler = new FilterableRecyclerView(getContext());
            mFilteredRecycler.setChipOptions(mOptions);
            mFilteredRecycler.setup(mFilteredAdapter, this);

            // To show our filterable recycler view, we need to make sure
            // our ChipsInputLayout has already been displayed on the screen
            // so we can access its root view
            ViewGroup rootView = (ViewGroup)getRootView();
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    Utils.getWindowWidth(getContext()),
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            if (getResources().getConfiguration().orientation
                    == Configuration.ORIENTATION_PORTRAIT) {
                lp.bottomMargin = Utils.getNavBarHeight(getContext());
            }
            rootView.addView(mFilteredRecycler, lp);
        }
    }

    private void hideKeyboard() {
        ((InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(mChipsInput.getWindowToken(), 0);
    }


    /**
     * Defines a validator to validate chips.
     */
    public interface ChipValidator {
        boolean validate(Chip chip);
    }


    /**
     * Implementation of {@link TextWatcher} that handles two things for us:
     * (1) Hides the filterable recycler if the user removes all the text from input.
     * (2) Tells the filterable recycler to filter the chips when the user enters text.
     */
    private final class ChipInputTextChangedHandler implements TextWatcher {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mFilteredRecycler != null) {
                // Hide the filterable recycler if there is no filter.
                // Filter the recycler if there is a filter
                if (TextUtils.isEmpty(s)) {
                    mFilteredRecycler.fadeOut();
                } else {
                    mFilteredRecycler.filterChips(s);
                }
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void afterTextChanged(Editable s) {}
    }
}