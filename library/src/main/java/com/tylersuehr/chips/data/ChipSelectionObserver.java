package com.tylersuehr.chips.data;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * Defines an observer that wants to observe changes to individual chips.
 *
 * Unlike {@link ChipChangedObserver}, this observer cares more about 'what' has changed
 * or what event had caused a change to the data source.
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public interface ChipSelectionObserver {
    void onChipSelected(Chip addedChip);
    void onChipDeselected(Chip removedChip);
}