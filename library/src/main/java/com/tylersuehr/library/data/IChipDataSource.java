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
    Collection<Chip> getOriginalChips();
    Collection<Chip> getFilteredChips();
    Collection<Chip> getSelectedChips();

    Chip getFilteredChip(int position);
    Chip getSelectedChip(int position);

    void setFilterableChips(Collection<? extends Chip> chips);
    void takeFilteredChip(Chip chip);
    void takeFilteredChip(int position);
    void replaceFilteredChip(Chip chip);
    void replaceFilteredChip(int position);

    void addFilteredChip(Chip chip);
    void addSelectedChip(Chip chip);

    void clearFilterableChips();
    void clearSelectedChips();

    boolean existsInFiltered(Chip chip);
    boolean existsInSelected(Chip chip);
    boolean existsInDataSource(Chip chip);

    void addChipChangedObserver(ChipChangedObserver observer);
    void removeChipChangedObserver(ChipChangedObserver observer);
    void removeAllChipChangedObservers();

    void addChipSelectionObserver(ChipSelectionObserver observer);
    void removeChipSelectionObserver(ChipSelectionObserver observer);
    void removeAllChipSelectionObservers();
}