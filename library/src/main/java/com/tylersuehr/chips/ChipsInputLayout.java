package com.tylersuehr.chips;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.tylersuehr.chips.data.Chip;
import com.tylersuehr.chips.data.ChipChangedObserver;
import com.tylersuehr.chips.data.ChipDataSource;
import com.tylersuehr.chips.data.ListChipDataSource;
import com.tylersuehr.chips.data.ChipSelectionObserver;
import java.util.List;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * Chips are a design concept specified in the Google Material Design Guide.
 *
 * The purpose of this view, in this library, is for displaying chips in a flowing layout,
 * and allowing the user to input to filter any filterable chips or to create custom chips.
 *
 * Important notes:
 * The chips data source is initialized as a {@link ListChipDataSource} in the constructor
 * by default.
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public class ChipsInputLayout extends MaxHeightScrollView
        implements FilterableChipsAdapter.OnFilteredChipClickListener {

    /* Stores and manages all our chips */
    private ChipDataSource chipDataSource;

    /* Stores the mutable properties of our ChipsInput (XML attrs) */
    private ChipOptions chipOptions;

    /* Allows user to type text into the ChipsInput */
    private ChipEditText chipsEditText;

    /* Displays selected chips and chips EditText */
    private final RecyclerView chipsRecycler;
    private final ChipsAdapter chipsAdapter;

    /* Displays filtered chips */
    private FilterableRecyclerView filterableRecyclerView;
    private FilterableChipsAdapter filterableChipsAdapter;

    /* Used to validate selected chips */
    private ChipValidator validator;


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

        // Set the max height from options
        setMaxHeight(Utils.dp(40) * chipOptions.maxRows);
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
     * Sets and stores a list of chips that are filterable and updates the
     * UI to enable the filterable RecyclerView accordingly.
     *
     * Note: this should only be called if you want the user to be able to
     * filter pre-existing (filterable chip list) chips.
     *
     * @param chips List of {@link Chip}
     */
    public void setFilterableChipList(List<? extends Chip> chips) {
        this.chipDataSource.setFilterableChips(chips);

        // Setup the filterable recycler when new filterable data has been set
        createAndSetupFilterableRecyclerView();
    }

    /**
     * Sets and stores a list of chips that are selected and updates the UI
     * to display them accordingly.
     *
     * @param chips List of {@link Chip}
     */
    public void setSelectedChipList(List<? extends Chip> chips) {
        // Set the selected chips in the data source
        this.chipDataSource.getSelectedChips().clear();
        this.chipDataSource.getSelectedChips().addAll(chips);

        // Update the chips UI display
        this.chipsAdapter.notifyDataSetChanged();
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
        if (chipDataSource.existsInDataSource(chip)) {
            throw new IllegalArgumentException("Chip already exists in the data source!");
        }

        // Using the method on data source will update UI
        this.chipDataSource.addFilteredChip(chip);

        // Create the filterable recycler at this point, if needed
        if (filterableRecyclerView == null) {
            createAndSetupFilterableRecyclerView();
        }
    }

    /**
     * Adds a new chip to the selected chips, which will update the UI
     * accordingly because of the change observers.
     *
     * @param chip {@link Chip}
     */
    public void addSelectedChip(Chip chip) {
        // Ensure that the chip is not already in the data source
        if (chipDataSource.existsInDataSource(chip)) {
            throw new IllegalArgumentException("Chip already exists in the data source!");
        }

        // Using the method on data source will update UI
        this.chipDataSource.addSelectedChip(chip);
    }

    /**
     * Clears all the filterable chips, which will update the UI accordingly
     * because of the change observers.
     */
    public void clearFilteredChips() {
        this.chipDataSource.clearFilteredChips();
    }

    /**
     * Clears all the selected chips, which will update the UI accordingly
     * because of the change observers.
     */
    public void clearSelectedChips() {
        this.chipDataSource.clearSelectedChips();
    }

    /**
     * Gets all the currently selected chips.
     *
     * @return List of {@link Chip}
     */
    public List<? extends Chip> getSelectedChips() {
        return chipDataSource.getSelectedChips();
    }

    /**
     * Gets all the currently filtered chips.
     * @see #getOriginalFilterableChips() if you want the original list of chips
     *
     * @return List of {@link Chip}
     */
    public List<? extends Chip> getFilteredChips() {
        return chipDataSource.getFilteredChips();
    }

    /**
     * Gets all the originally set filterable chips.
     *
     * @return List of {@link Chip}
     */
    public List<? extends Chip> getOriginalFilterableChips() {
        return chipDataSource.getOriginalChips();
    }

    /**
     * Gets a selected chip using the given index.
     *
     * @param position Position of chip
     * @return {@link Chip}
     */
    public Chip getSelectedChipByPosition(int position) {
        return chipDataSource.getSelectedChip(position);
    }

    /**
     * Gets a selected chip using the given ID, if possible.
     *
     * @param id ID of the selected chip
     * @return {@link Chip}
     */
    public Chip getSelectedChipById(Object id) {
        for (Chip chip : chipDataSource.getSelectedChips()) {
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
        for (Chip chip : chipDataSource.getSelectedChips()) {
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
        for (Chip chip : chipDataSource.getSelectedChips()) {
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
        return chipDataSource.getFilteredChip(position);
    }

    /**
     * Gets a filtered chip using the given ID, if possible.
     *
     * @param id Filtered chip's ID
     * @return {@link Chip}
     */
    public Chip getFilteredChipById(Object id) {
        for (Chip chip : chipDataSource.getFilteredChips()) {
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
        for (Chip chip : chipDataSource.getFilteredChips()) {
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
        for (Chip chip : chipDataSource.getFilteredChips()) {
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
        return chipDataSource.existsInDataSource(chip);
    }

    /**
     * Checks if the given chip exists in the filtered chips.
     *
     * @param chip {@link Chip}
     * @return True if chip exists in filtered chips
     */
    public boolean isChipFiltered(Chip chip) {
        return chipDataSource.existsInFiltered(chip);
    }

    /**
     * Checks if the given chip exists in the selected chips.
     *
     * @param chip {@link Chip}
     * @return True if chip exists in selected chips
     */
    public boolean isChipSelected(Chip chip) {
        return chipDataSource.existsInSelected(chip);
    }

    /**
     * Validates the given chip using {@link #validator}.
     *
     * @param chip {@link Chip}
     * @return True if chip is valid, or no chip validator is set
     */
    public boolean validateChip(Chip chip) {
        return validator == null || validator.validate(chip);
    }

    /**
     * Validates all the selected chips using {@link #validator}.
     *
     * @return True if all selected chips are valid, or no chip validator is set
     */
    public boolean validateSelectedChips() {
        if (validator != null) {
            for (Chip chip : chipDataSource.getSelectedChips()) {
                if (!validator.validate(chip)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Sets the chip validator to valid chips.
     *
     * @param validator {@link ChipValidator}
     */
    public void setChipValidator(ChipValidator validator) {
        this.validator = validator;
    }

    /**
     * Adds an observer to watch selection events on the chip data source.
     *
     * @param observer {@link ChipSelectionObserver}
     */
    public void addChipSelectionObserver(ChipSelectionObserver observer) {
        this.chipDataSource.addChipSelectionObserver(observer);
    }

    /**
     * Removes an observer from watching selection events on the chip data source.
     *
     * @param observer {@link ChipSelectionObserver}
     */
    public void removeChipSelectionObserver(ChipSelectionObserver observer) {
        this.chipDataSource.removeChipSelectionObserver(observer);
    }

    /**
     * Adds an observer to watch for any change events on the chip data source.
     *
     * Note: please use this conservatively!
     *
     * @param observer {@link ChipChangedObserver}
     */
    public void addChipChangedObserver(ChipChangedObserver observer) {
        this.chipDataSource.addChipChangedObserver(observer);
    }

    /**
     * Removes an observer from watching any change events on the chip data source.
     *
     * @param observer {@link ChipChangedObserver}
     */
    public void removeChipChangedObserver(ChipChangedObserver observer) {
        this.chipDataSource.removeChipChangedObserver(observer);
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
        this.chipDataSource.cloneObservers(dataSource);
        this.chipDataSource = dataSource;
        this.chipsAdapter.notifyDataSetChanged();
    }

    /**
     * Gets an instance of {@link LetterTileProvider}.
     * @return {@link LetterTileProvider}
     */
    public LetterTileProvider getLetterTileProvider() {
        return LetterTileProvider.getInstance(getContext());
    }

    public void setInputTextColor(ColorStateList textColor) {
        this.chipOptions.textColor = textColor;
        if (chipsEditText != null) { // Can be null because its lazy loaded
            this.chipsEditText.setTextColor(textColor);
        }
    }

    public void setInputHintTextColor(ColorStateList textColorHint) {
        this.chipOptions.textColorHint = textColorHint;
        if (chipsEditText != null) { // Can be null because its lazy loaded
            this.chipsEditText.setHintTextColor(textColorHint);
        }
    }

    public void setInputHint(CharSequence hint) {
        this.chipOptions.hint = hint;
        if (chipsEditText != null) { // Can be null because its lazy loaded
            this.chipsEditText.setHint(hint);
        }
    }

    public void setChipDeleteIconColor(ColorStateList deleteIconColor) {
        this.chipOptions.chipDeleteIconColor = deleteIconColor;
    }

    public void setChipBackgroundColor(ColorStateList chipBackgroundColor) {
        this.chipOptions.chipBackgroundColor = chipBackgroundColor;
    }

    public void setChipTitleTextColor(ColorStateList chipTitleTextColor) {
        this.chipOptions.chipTextColor = chipTitleTextColor;
    }

    public void setChipDeleteIcon(Drawable chipDeleteIcon) {
        this.chipOptions.chipDeleteIcon = chipDeleteIcon;
    }

    public void setChipDeleteIcon(@DrawableRes int res) {
        this.chipOptions.chipDeleteIcon = ContextCompat.getDrawable(getContext(), res);
    }

    public void setShowChipAvatarEnabled(boolean hasAvatar) {
        this.chipOptions.hasAvatarIcon = hasAvatar;
    }

    public void setShowDetailedChipsEnabled(boolean enabled) {
        this.chipOptions.showDetailedChips = enabled;
    }

    public void setChipsDeletable(boolean enabled) {
        this.chipOptions.chipDeletable = enabled;
    }

    public void setDetailedChipDeleteIconColor(ColorStateList detailedChipIconColor) {
        this.chipOptions.detailedChipDeleteIconColor = detailedChipIconColor;
    }

    public void setDetailedChipBackgroundColor(ColorStateList detailedChipBackgroundColor) {
        this.chipOptions.detailedChipBackgroundColor = detailedChipBackgroundColor;
    }

    public void setDetailedChipTextColor(ColorStateList detailedChipTextColor) {
        this.chipOptions.detailedChipTextColor = detailedChipTextColor;
    }

    public void setFilterableListBackgroundColor(ColorStateList backgroundColor) {
        this.chipOptions.filterableListBackgroundColor = backgroundColor;
    }

    public void setFilterableListTextColor(ColorStateList textColor) {
        this.chipOptions.filterableListTextColor = textColor;
    }

    public void setFilterableListElevation(float elevation) {
        this.chipOptions.filterableListElevation = elevation;
    }

    public void setCustomChipsEnabled(boolean enabled) {
        this.chipOptions.allowCustomChips = enabled;
    }

    public void setMaxRows(int rows) {
        this.chipOptions.maxRows = rows;
        setMaxHeight(Utils.dp(40) * chipOptions.maxRows);
    }

    public void setTypeface(Typeface typeface) {
        this.chipOptions.typeface = typeface;
        LetterTileProvider.getInstance(getContext()).setTypeface(typeface);
        if (chipsEditText != null) {
            this.chipsEditText.setTypeface(typeface);
        }
    }

    /**
     * Gets the current chip data source being used.
     *
     * Note: This method should be used conservatively. Most components should access chips
     * through other methods on ChipsInputLayout instead of the chip data source directly.
     *
     * @return {@link ChipDataSource}
     */
    public ChipDataSource getChipDataSource() {
        return chipDataSource;
    }

    /**
     * Gets the current chip options.
     * Note: package-private for now because outside components should use accessors instead.
     *
     * @return {@link ChipOptions}
     */
    ChipOptions getChipOptions() {
        return chipOptions;
    }

    /**
     * Gets the current chips recycler view.
     * Note: package-private because no outside components should access this.
     *
     * @return {@link RecyclerView}
     */
    RecyclerView getChipsRecyclerView() {
        return chipsRecycler;
    }

    /**
     * Lazy loads the input for the user to enter chip titles.
     *
     *  @return {@link EditText}
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
            this.chipsEditText.setTypeface(chipOptions.typeface);

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
     *
     * @return {@link ChipView}
     */
    ChipView getThemedChipView() {
        int padding = Utils.dp(4);

        ChipView chipView = new ChipView.Builder(getContext())
                .titleTextColor(chipOptions.chipTextColor)
                .hasAvatarIcon(chipOptions.hasAvatarIcon)
                .deletable(chipOptions.chipDeletable)
                .deleteIcon(chipOptions.chipDeleteIcon)
                .deleteIconColor(chipOptions.chipDeleteIconColor)
                .backgroundColor(chipOptions.chipBackgroundColor)
                .typeface(chipOptions.typeface)
                .build();

        chipView.setPadding(padding, padding, padding, padding);
        return chipView;
    }

    /**
     * Creates a new {@link DetailedChipView} with its theme set from properties
     * defined in {@link #chipOptions}.
     *
     * @param chip {@link Chip}
     * @return {@link DetailedChipView}
     */
    DetailedChipView getThemedDetailedChipView(Chip chip) {
        return new DetailedChipView.Builder(getContext())
                .chip(chip)
                .textColor(chipOptions.detailedChipTextColor)
                .backgroundColor(chipOptions.detailedChipBackgroundColor)
                .deleteIconColor(chipOptions.detailedChipDeleteIconColor)
                .typeface(chipOptions.typeface)
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
        ViewCompat.setElevation(filterableRecyclerView, chipOptions.filterableListElevation);

        if (chipOptions.filterableListBackgroundColor != null) {
            this.filterableRecyclerView.getBackground().setColorFilter(
                    chipOptions.filterableListBackgroundColor.getDefaultColor(), PorterDuff.Mode.SRC_ATOP);
        }

        // Create and set the filterable chips adapter
        this.filterableChipsAdapter = new FilterableChipsAdapter(this, chipDataSource, chipOptions);
        this.filterableRecyclerView.setAdapter(this, filterableChipsAdapter);

        // To show our filterable recycler view, we need to make sure our ChipsInputLayout has
        // already been displayed on the screen so we can access its root view
        ViewGroup rootView = (ViewGroup)getRootView();
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                Utils.getWindowWidth(getContext()),
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            lp.bottomMargin = Utils.getNavBarHeight(getContext());
        }
        rootView.addView(filterableRecyclerView, lp);
    }

    /**
     * Hides the software keyboard from the chips edit text.
     */
    private void hideKeyboard() {
        ((InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(chipsEditText.getWindowToken(), 0);
    }


    /**
     * This is used to ensure that selected chips are... well... valid lol.
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