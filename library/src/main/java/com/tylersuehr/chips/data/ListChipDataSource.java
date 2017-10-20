package com.tylersuehr.chips.data;
import android.support.annotation.ColorRes;
import android.support.annotation.VisibleForTesting;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * Subclass of {@link ObservableChipDataSource} that stores chips using an {@link ArrayList}.
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public class ListChipDataSource extends ObservableChipDataSource {
    /* Stores all the original chips */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    List<Chip> originalChips;

    /* Stores all the filtered chips, that are not selected by the user */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    List<Chip> filteredChips;

    /* Stores all the selected chips, selected by the user */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    List<Chip> selectedChips;


    /* Construct with all empty lists */
    public ListChipDataSource() {
        this.originalChips = new ArrayList<>();
        this.filteredChips = new ArrayList<>();
        this.selectedChips = new ArrayList<>();
    }

    @Override
    public List<Chip> getSelectedChips() {
        return selectedChips;
    }

    @Override
    public List<Chip> getFilteredChips() {
        return filteredChips;
    }

    @Override
    public List<Chip> getOriginalChips() {
        return originalChips;
    }

    @Override
    public Chip getFilteredChip(int position) {
        return filteredChips.get(position);
    }

    @Override
    public Chip getSelectedChip(int position) {
        return selectedChips.get(position);
    }

    @Override
    public void setFilterableChips(List<? extends Chip> chips) {
        if (chips == null) {
            throw new NullPointerException("Chips cannot be null!");
        }

        // Instantiate our chip lists with the size of the given list
        this.originalChips = new ArrayList<>(chips.size());
        this.filteredChips = new ArrayList<>(chips.size());
        this.selectedChips = new ArrayList<>(chips.size());

        // Only copy the data from our chips into the original and filtered lists
        for (Chip chip : chips) {
            chip.setFilterable(true);
            this.originalChips.add(chip);
            this.filteredChips.add(chip);
        }

        // Sort the lists
        Collections.sort(originalChips, Chip.getComparator());
        Collections.sort(filteredChips, Chip.getComparator());

        // Tell our observers!
        notifyDataSourceChanged();
    }

    @Override
    public void addFilteredChip(Chip chip) {
        if (chip == null) {
            throw new NullPointerException("Chip cannot be null!");
        }
        chip.setFilterable(true);
        this.originalChips.add(chip);
        this.filteredChips.add(chip);

        // Sort the filterable chips
        Collections.sort(originalChips, Chip.getComparator());
        Collections.sort(filteredChips, Chip.getComparator());

        notifyDataSourceChanged();
    }

    @Override
    public void addSelectedChip(Chip chip) {
        if (chip == null) {
            throw new NullPointerException("Chip cannot be null!");
        }
        this.selectedChips.add(chip);
        notifyDataSourceChanged();
        notifyChipSelected(chip);
    }

    @Override
    public void takeChip(Chip chip) {
        if (chip == null) {
            throw new NullPointerException("Chip cannot be null!");
        }

        // Check if chip is filterable
        if (chip.isFilterable()) {
            // Check if chip is actually in the filtered list
            if (filteredChips.contains(chip)) {
                this.originalChips.remove(chip);
                this.filteredChips.remove(chip);
                this.selectedChips.add(chip);
            } else {
                throw new IllegalArgumentException("Chip is not in filtered chip list!");
            }
        } else {
            throw new IllegalArgumentException("Cannot take a non-filterable chip!");
        }

        notifyDataSourceChanged();
        notifyChipSelected(chip);
    }

    @Override
    public void takeChip(int position) {
        final Chip foundChip = filteredChips.get(position);
        if (foundChip == null) {
            throw new NullPointerException("Chip cannot be null; not found in filtered chip list!");
        }

        // Check if chip is filterable
        if (foundChip.isFilterable()) {
            // Since the child isn't null, we know it's in the filtered list
            this.originalChips.remove(foundChip);
            this.filteredChips.remove(foundChip);
            this.selectedChips.add(foundChip);
        } else {
            // Just add it to the selected list only
            this.selectedChips.add(foundChip);
        }

        notifyDataSourceChanged();
        notifyChipSelected(foundChip);
    }

    @Override
    public void replaceChip(Chip chip) {
        if (chip == null) {
            throw new NullPointerException("Chip cannot be null!");
        }

        // Check if chip is actually selected
        if (selectedChips.contains(chip)) {
            this.selectedChips.remove(chip);

            // Check if the chip is filterable
            if (chip.isFilterable()) {
                this.filteredChips.add(chip);
                this.originalChips.add(chip);

                // Sort the filterable chips
                Collections.sort(filteredChips, Chip.getComparator());
                Collections.sort(originalChips, Chip.getComparator());
            }

            notifyDataSourceChanged();
            notifyChipUnselected(chip);
        } else {
            throw new IllegalArgumentException("Chip is not in selected chip list!");
        }
    }

    @Override
    public void replaceChip(int position) {
        final Chip foundChip = selectedChips.get(position);
        if (foundChip == null) {
            throw new NullPointerException("Chip cannot be null; not found in selected chip list!");
        }

        // Since not null, we know the chip is selected
        this.selectedChips.remove(foundChip);

        // Check if the chip is filterable
        if (foundChip.isFilterable()) {
            this.filteredChips.add(foundChip);
            this.originalChips.add(foundChip);

            // Sort the filterable chips
            Collections.sort(filteredChips, Chip.getComparator());
            Collections.sort(originalChips, Chip.getComparator());
        }

        notifyDataSourceChanged();
        notifyChipUnselected(foundChip);
    }

    @Override
    public void clearFilteredChips() {
        this.originalChips.clear();
        this.filteredChips.clear();
        notifyDataSourceChanged();
    }

    @Override
    public void clearSelectedChips() {
        // Since we want to tell observers that chips have been unselected,
        // we need to store a clone of the selected list of chips
        final List<Chip> clone = new ArrayList<>(selectedChips);
        this.selectedChips.clear();

        // Let's notify our change observers first (so internal components can
        // instantly get notified of the data source change
        notifyDataSourceChanged();

        // Now let's tell our selection observers!
        for (Chip chip : clone) {
            notifyChipUnselected(chip);
        }
    }

    @Override
    public boolean existsInFiltered(Chip chip) {
        if (chip == null) {
            throw new NullPointerException("Chip cannot be null!");
        }
        return filteredChips.contains(chip);
    }

    @Override
    public boolean existsInSelected(Chip chip) {
        if (chip == null) {
            throw new NullPointerException("Chip cannot be null!");
        }
        return selectedChips.contains(chip);
    }

    @Override
    public boolean existsInDataSource(Chip chip) {
        if (chip == null) {
            throw new NullPointerException("Chip cannot be null!");
        }
        return (originalChips.contains(chip)
                || filteredChips.contains(chip)
                || selectedChips.contains(chip));
    }
}