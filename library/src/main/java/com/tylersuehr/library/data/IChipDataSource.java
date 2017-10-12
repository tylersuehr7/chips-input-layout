package com.tylersuehr.library.data;
import java.util.Collection;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * Defines the abstract data source for our chips.
 *
 * Note: Selected chips will NEVER, or should never, appear in the other lists.
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public interface IChipDataSource {
    /**
     * Gets a collection of the originally filterable chips.
     * @return Collection of {@link Chip}
     */
    Collection<Chip> getOriginalChips();

    /**
     * Gets a collection of the filtered chips.
     * @return Collection of {@link Chip}
     */
    Collection<Chip> getFilteredChips();

    /**
     * Gets a collection of the selected chips.
     * @return Collection of {@link Chip}
     */
    Collection<Chip> getSelectedChips();


    /**
     * Gets a chip from the filtered chips, using the given index.
     * @param position Position of chip in filtered chips
     * @return {@link Chip}
     */
    Chip getFilteredChip(int position);

    /**
     * Gets a chip from the selected chips, using the given index.
     * @param position Position of chip in selected chips
     * @return {@link Chip}
     */
    Chip getSelectedChip(int position);


    /**
     * Sets a collection of chips to be filterable... which guarantees
     * that they will be in the filtered and original chips.
     * @param chips Collection of {@link Chip}
     */
    void setFilterableChips(Collection<? extends Chip> chips);

    /**
     * Takes a chip from the filtered and original chips and puts it
     * into the selected chips.
     * @param chip {@link Chip}
     */
    void takeFilteredChip(Chip chip);

    /**
     * Takes a chip from the filtered and original chips and puts it
     * into the selected chips, using the given index.
     * @param position Position of chip in filtered chips
     */
    void takeFilteredChip(int position);

    /**
     * Takes a chip from the selected chips and puts it back into the
     * filtered and original chips.
     * @param chip {@link Chip}
     */
    void replaceFilteredChip(Chip chip);

    /**
     * Takes a chip from the selected chips and puts it back into the
     * filtered and original chips, using the given index.
     * @param position Position of chip in selected chips
     */
    void replaceFilteredChip(int position);


    /**
     * Adds a given chip to the filtered chips, ensuring it's filterable.
     * @param chip {@link Chip}
     */
    void addFilteredChip(Chip chip);

    /**
     * Adds a given chip to the selected chips.
     * @param chip {@link Chip}
     */
    void addSelectedChip(Chip chip);


    /**
     * Removes all chips from the filtered chips.
     */
    void clearFilterableChips();

    /**
     * Removes all chips from the selected chips.
     */
    void clearSelectedChips();


    /**
     * Checks if a given chip exists within the filtered chips.
     * @param chip {@link Chip}
     * @return True if chip exists in filtered chips
     */
    boolean existsInFiltered(Chip chip);

    /**
     * Checks if a given chip exists within the selected chips.
     * @param chip {@link Chip}
     * @return True if chip exists in selected chips
     */
    boolean existsInSelected(Chip chip);

    /**
     * Checks if a given chip exists within the entire data source.
     * @param chip {@link Chip}
     * @return True if chip exists somewhere in data source
     */
    boolean existsInDataSource(Chip chip);


    /**
     * Adds a given chip change observer to observe chip data source change events.
     * @param observer {@link ChipChangedObserver}
     */
    void addChipChangedObserver(ChipChangedObserver observer);

    /**
     * Removes a given chip change observer from observing chip data source change events.
     * @param observer {@link ChipChangedObserver}
     */
    void removeChipChangedObserver(ChipChangedObserver observer);

    /**
     * Removes all chip change observers from observing chip data source change events.
     */
    void removeAllChipChangedObservers();


    /**
     * Adds a given chip selection observer to observe chip selection events.
     * @param observer {@link ChipSelectionObserver}
     */
    void addChipSelectionObserver(ChipSelectionObserver observer);

    /**
     * Removes a given chip selection observer from observing chip selection events.
     * @param observer {@link ChipSelectionObserver}
     */
    void removeChipSelectionObserver(ChipSelectionObserver observer);

    /**
     * Removes all chip selection observers from observing chip selection events.
     */
    void removeAllChipSelectionObservers();
}