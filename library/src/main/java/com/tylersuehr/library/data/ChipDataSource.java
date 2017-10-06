package com.tylersuehr.library.data;
import java.util.List;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public interface ChipDataSource {
    /**
     * Returns the chips that are selected by the user.
     * @return List of {@link Chip}
     */
    List<Chip> getSelectedChips();

    /**
     * Returns the chips that are not selected by the user.
     * @return List of {@link Chip}
     */
    List<Chip> getFilteredChips();

    /**
     * Returns the all the chips, selected and unselected.
     * @return List of {@link Chip}
     */
    List<Chip> getOriginalChips();

    /**
     * Creates new lists for selected, filtered, and original chips with the data from
     * the given list.
     * @param chips List of {@link Chip}
     */
    void setFilterableChips(List<? extends Chip> chips);

    /**
     * Attempts to take a chip from the filtered list and put it in the selected list.
     * If the chip isn't filterable, it will just be added to the selected list only.
     * @param chip {@link Chip}
     */
    void takeChip(Chip chip);

    /**
     * Attempts to remove a chip from the selected list and put it back into the filtered
     * list. If the chip isn't filterable, it will just be removed from the selected list
     * only.
     * @param chip {@link Chip}
     */
    void replaceChip(Chip chip);

    /**
     * Takes a chip from the filtered list, puts it in the selected list, and then sorts them.
     * @deprecated use {@link #takeChip(Chip)} instead!
     * @param chip {@link Chip}
     */
    @Deprecated
    void takeFilteredChip(Chip chip);

    /**
     * Returns a selected chip to the filtered list from the selected list and then sorts them.
     * @deprecated use {@link #replaceChip(Chip)} instead!
     * @param chip {@link Chip}
     */
    @Deprecated
    void replaceFilteredChip(Chip chip);
}