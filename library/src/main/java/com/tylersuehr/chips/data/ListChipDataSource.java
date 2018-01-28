package com.tylersuehr.chips.data;
import android.support.annotation.VisibleForTesting;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * Subclass of {@link ObservableChipDataSource} that stores chips using
 * an {@link ArrayList}.
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public class ListChipDataSource extends ObservableChipDataSource {
    /* Aggregation of all the original chips */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    List<Chip> mOriginal;

    /* Aggregation of all filtered chips, not selected by the user */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    List<Chip> mFiltered;

    /* Aggregation of all selected chips, selected by the user */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    List<Chip> mSelected;


    /* Construct with all empty lists */
    public ListChipDataSource() {
        mOriginal = new ArrayList<>();
        mFiltered = new ArrayList<>();
        mSelected = new ArrayList<>();
    }

    @Override
    public List<Chip> getSelectedChips() {
        return mSelected;
    }

    @Override
    public List<Chip> getFilteredChips() {
        return mFiltered;
    }

    @Override
    public List<Chip> getOriginalChips() {
        return mOriginal;
    }

    @Override
    public Chip getFilteredChip(int position) {
        return mFiltered.get(position);
    }

    @Override
    public Chip getSelectedChip(int position) {
        return mSelected.get(position);
    }

    @Override
    public void setFilterableChips(List<? extends Chip> chips) {
        if (chips == null) {
            throw new NullPointerException("Chips cannot be null!");
        }

        // Instantiate our chip lists with the size of the given list
        mSelected = new ArrayList<>();
        mOriginal = new ArrayList<>(chips.size());
        mFiltered = new ArrayList<>(chips.size());

        // Only copy the data from our chips into the original and filtered lists
        for (Chip chip : chips) {
            chip.setFilterable(true);
            mOriginal.add(chip);
            mFiltered.add(chip);
        }

        // Sort the lists
        Collections.sort(mOriginal, Chip.getComparator());
        Collections.sort(mFiltered, Chip.getComparator());

        // Tell our observers!
        notifyDataSourceChanged();
    }

    @Override
    public void addFilteredChip(Chip chip) {
        if (chip == null) {
            throw new NullPointerException("Chip cannot be null!");
        }
        chip.setFilterable(true);
        mOriginal.add(chip);
        mFiltered.add(chip);

        // Sort the filterable chips
        Collections.sort(mOriginal, Chip.getComparator());
        Collections.sort(mFiltered, Chip.getComparator());

        notifyDataSourceChanged();
    }

    @Override
    public void addSelectedChip(Chip chip) {
        if (chip == null) {
            throw new NullPointerException("Chip cannot be null!");
        }
        mSelected.add(chip);
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
            if (mFiltered.contains(chip)) {
                mOriginal.remove(chip);
                mFiltered.remove(chip);
                mSelected.add(chip);
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
        final Chip foundChip = mFiltered.get(position);
        if (foundChip == null) {
            throw new NullPointerException("Chip cannot be null; " +
                    "not found in filtered chip list!");
        }

        // Check if chip is filterable
        if (foundChip.isFilterable()) {
            // Since the child isn't null, we know it's in the filtered list
            mOriginal.remove(foundChip);
            mFiltered.remove(foundChip);
            mSelected.add(foundChip);
        } else {
            // Just add it to the selected list only
            mSelected.add(foundChip);
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
        if (mSelected.contains(chip)) {
            mSelected.remove(chip);

            // Check if the chip is filterable
            if (chip.isFilterable()) {
                mFiltered.add(chip);
                mOriginal.add(chip);

                // Sort the filterable chips
                Collections.sort(mFiltered, Chip.getComparator());
                Collections.sort(mOriginal, Chip.getComparator());
            }

            notifyDataSourceChanged();
            notifyChipUnselected(chip);
        } else {
            throw new IllegalArgumentException("Chip is not in selected chip list!");
        }
    }

    @Override
    public void replaceChip(int position) {
        final Chip foundChip = mSelected.get(position);
        if (foundChip == null) {
            throw new NullPointerException("Chip cannot be null; not " +
                    "found in selected chip list!");
        }

        // Since not null, we know the chip is selected
        mSelected.remove(foundChip);

        // Check if the chip is filterable
        if (foundChip.isFilterable()) {
            mFiltered.add(foundChip);
            mOriginal.add(foundChip);

            // Sort the filterable chips
            Collections.sort(mFiltered, Chip.getComparator());
            Collections.sort(mOriginal, Chip.getComparator());
        }

        notifyDataSourceChanged();
        notifyChipUnselected(foundChip);
    }

    @Override
    public void clearFilteredChips() {
        mOriginal.clear();
        mFiltered.clear();
        notifyDataSourceChanged();
    }

    @Override
    public void clearSelectedChips() {
        // Since we want to tell observers that chips have been unselected,
        // we need to store a clone of the selected list of chips
        final List<Chip> clone = new ArrayList<>(mSelected);
        mSelected.clear();

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
        return mFiltered.contains(chip);
    }

    @Override
    public boolean existsInSelected(Chip chip) {
        if (chip == null) {
            throw new NullPointerException("Chip cannot be null!");
        }
        return mSelected.contains(chip);
    }

    @Override
    public boolean existsInDataSource(Chip chip) {
        if (chip == null) {
            throw new NullPointerException("Chip cannot be null!");
        }
        return (mOriginal.contains(chip)
                || mFiltered.contains(chip)
                || mSelected.contains(chip));
    }
}