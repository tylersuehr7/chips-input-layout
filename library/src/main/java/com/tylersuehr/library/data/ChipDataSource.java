package com.tylersuehr.library.data;
import java.util.List;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * Defines the data source for our chips.
 *
 * This is how all the chips are managed overall. This is used by most components
 * of this library to access chip data, modify chip data, or observe changes to it.
 *
 * This is decoupled in order to allow you to implement it using any type of concrete
 * List data structure, although a basic implementation, using ArrayList, is provided
 * for you already {@link ListChipDataSource}.
 *
 * This needs to use three lists of chips:
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

    void setFilterableChips(List<? extends Chip> chips);
    void takeChip(Chip chip);
    void replaceChip(Chip chip);

    void registerObserver(ChipDataSourceObserver observer);
    void unregisterObserver(ChipDataSourceObserver observer);
    void unregisterAllObservers();
}