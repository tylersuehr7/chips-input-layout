package com.tylersuehr.chips.data;
import java.util.List;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * Defines the data source for all chips.
 *
 * This is how the chips are managed overall. This is leveraged by components
 * of this library to access chip data, modify chip data, or observe changes to
 * chip data.
 *
 * This is decoupled in order to allow other implementations of this using any type
 * of concrete {@link List} data structures.
 *
 * Custom implementation of this should subclass {@link ObservableChipDataSource},
 * it provides the base functionality for observing changes to chips and simplifies
 * some of the implementation.
 *
 * The default implementation of this used by this library is {@link ListChipDataSource},
 * and it uses the {@link java.util.ArrayList} to store chips.
 *
 * This requires three lists of chips:
 * (1) Selected chips: chips the user has explicitly selected.
 * (2) Filtered chips: chips that may have undergone some type of filtering.
 * (3) Original chips: original set list of filtered chips.
 *
 * Note: Selected chips will NEVER, or should never, appear in the other lists.
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public interface ChipDataSource {
    List<Chip> getSelectedChips();
    List<Chip> getFilteredChips();
    List<Chip> getOriginalChips();

    Chip getFilteredChip(int position);
    Chip getSelectedChip(int position);

    void setFilterableChips(List<? extends Chip> chips);

    void takeChip(Chip chip);
    void takeChip(int position);

    void replaceChip(Chip chip);
    void replaceChip(int position);

    void addFilteredChip(Chip chip);
    void addSelectedChip(Chip chip);

    void clearFilteredChips();
    void clearSelectedChips();

    boolean existsInFiltered(Chip chip);
    boolean existsInSelected(Chip chip);
    boolean existsInDataSource(Chip chip);

    void addSelectionObserver(SelectionObserver observer);
    void removeSelectionObserver(SelectionObserver observer);
    void removeAllSelectionObservers();

    void addChangedObserver(ChangeObserver observer);
    void removeChangedObserver(ChangeObserver observer);
    void removeAllChangedObservers();

    void cloneObservers(ChipDataSource to);


    /**
     * Defines an observer that wants to watch for any kind of overall
     * change to this data source.
     */
    interface ChangeObserver {
        void onChipDataSourceChanged();
    }

    /**
     * Defines an observer that wants to observe changes to individual
     * chip selection events.
     *
     * Unlike {@link ChangeObserver}, this observer cares more about 'what'
     * has changed or what event had caused a change to this data source.
     */
    interface SelectionObserver {
        void onChipSelected(Chip addedChip);
        void onChipDeselected(Chip removedChip);
    }
}