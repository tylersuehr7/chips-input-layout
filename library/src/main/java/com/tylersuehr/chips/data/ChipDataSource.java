package com.tylersuehr.chips.data;
import java.util.List;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * Defines the data source for our chips.
 *
 * This is how all the chips are managed overall. This is leveraged by components of
 * this library to access chip data, modify chip data, or observe changes to chip data.
 *
 * This is decoupled in order to allow other implementations of this using any type of
 * concrete {@link List} data structures. To simplify writing implementations of this,
 * I've provided {@link ObservableChipDataSource}, which handles all the logic for using
 * the observers. Simply inherit {@link ObservableChipDataSource}.
 *
 * The default implementation of this, used by the library, is {@link ListChipDataSource},
 * and it uses the {@link java.util.ArrayList} to store chips.
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

    void addChipSelectionObserver(ChipSelectionObserver observer);
    void removeChipSelectionObserver(ChipSelectionObserver observer);
    void removeAllChipSelectionObservers();

    void addChipChangedObserver(ChipChangedObserver observer);
    void removeChipChangedObserver(ChipChangedObserver observer);
    void removeAllChipChangedObservers();

    void cloneObservers(ChipDataSource to);
}