package com.tylersuehr.library.data;
import java.util.List;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public interface ChipDataSourceObserver {
    void onChipAdded(final List<Chip> list, Chip addedChip);
    void onChipRemoved(final List<Chip> list, Chip removedChip);
}