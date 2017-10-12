package com.tylersuehr.library.data;

import android.support.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * Subclass of {@link ChipDataSource} that manages chips using the {@link ArrayList} data structure.
 *
 * Note: this is the default {@link IChipDataSource} used by ChipsInputLayout.
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public class ListChipDataSource extends ChipDataSource {
    /* Stores all the original chips */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    List<Chip> originalChips;

    /* Stores all the filtered chips, that are not selected by the user */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    List<Chip> filteredChips;

    /* Stores all the selected chips, selected by the user */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    List<Chip> selectedChips;


    /* Construct with empty array lists */
    public ListChipDataSource() {
        this.originalChips = new ArrayList<>();
        this.filteredChips = new ArrayList<>();
        this.selectedChips = new ArrayList<>();
    }

    @Override
    public List<Chip> getOriginalChips() {
        return originalChips;
    }

    @Override
    public List<Chip> getFilteredChips() {
        return filteredChips;
    }

    @Override
    public List<Chip> getSelectedChips() {
        return selectedChips;
    }

    /**
     * Overridden to implement a better performance pattern.
     *
     * @param chips collection of {@link Chip}
     */
    @Override
    public void setFilterableChips(Collection<? extends Chip> chips) {
        if (chips == null) {
            throw new NullPointerException("Chips cannot be null!");
        }

        // Instantiate our chip lists with the size of the given list
        this.originalChips = new ArrayList<>(chips.size());
        this.filteredChips = new ArrayList<>(chips.size());
        this.selectedChips = new ArrayList<>(chips.size());

        // Only copy the data from our chips into the original and filtered lists
        // and set the filterable flag to true to ensure all chips are filterable
        for (Chip chip : chips) {
            chip.setFilterable(true);
            this.originalChips.add(chip);
            this.filteredChips.add(chip);
        }

        // Sort the lists
        Collections.sort(originalChips, Chip.getComparator());
        Collections.sort(filteredChips, Chip.getComparator());

        // Tell our observers
        notifyDataSourceChanged();
    }

    /**
     * Overridden to implement a better performance pattern.
     *
     * @param position Position of chip
     * @return {@link Chip}
     */
    @Override
    public Chip getFilteredChip(int position) {
        return filteredChips.get(position);
    }

    /**
     * Overridden to implement a better performance pattern.
     *
     * @param position Position of chip
     * @return {@link Chip}
     */
    @Override
    public Chip getSelectedChip(int position) {
        return selectedChips.get(position);
    }

    /**
     * Overridden to implement a better performance pattern.
     */
    @Override
    public void clearFilterableChips() {
        this.filteredChips.clear();
        notifyDataSourceChanged();
    }

    /**
     * Overridden to implement a better performance pattern.
     */
    @Override
    public void clearSelectedChips() {
        // Clone the chips to appropriately handle telling observers
        List<Chip> clone = new ArrayList<>(selectedChips);
        this.selectedChips.clear();

        // Tell our observers!
        notifyDataSourceChanged();
        for (Chip chip : clone) {
            notifyChipUnselected(chip);
        }
    }
}
