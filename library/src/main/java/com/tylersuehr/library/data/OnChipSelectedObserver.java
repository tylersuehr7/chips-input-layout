package com.tylersuehr.library.data;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * Defines an observer that wants to observe changes to individual chips.
 *
 * Unlike {@link OnChipChangedObserver}, this observer cares more about 'what' has changed
 * or what event had caused a change to the data source.
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public interface OnChipSelectedObserver {
    void onChipAdded(Chip addedChip);
    void onChipRemoved(Chip removedChip);
}