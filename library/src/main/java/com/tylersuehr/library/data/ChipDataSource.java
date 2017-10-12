package com.tylersuehr.library.data;
import android.support.annotation.VisibleForTesting;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.RandomAccess;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * This SHOULD be the subclass of any class that wants to implement {@link IChipDataSource}!!
 *
 * Implements most of {@link IChipDataSource} to make creating subclasses simpler.
 *
 * Although the available implemented methods for {@link IChipDataSource} are as efficient
 * as they can get while using the {@link Collection} interface, you should definitely make
 * sure that you override methods implemented here in any subclass you create if you can make
 * them more efficient for the data structure you're using!
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public abstract class ChipDataSource implements IChipDataSource {
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    List<ChipChangedObserver> changedObservers;
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    List<ChipSelectionObserver> selectionObservers;


    /**
     * Default implementation.
     *
     * This will attempt to get non-null references to the original and filtered
     * collections, remove any pre-existing chips that may be in them, and add all
     * the new filterable chips.
     *
     * Will notify observers.
     *
     * @param chips Collection of {@link Chip}
     */
    @Override
    public void setFilterableChips(Collection<? extends Chip> chips) {
        if (chips == null) {
            throw new NullPointerException("Chips cannot be null!");
        }

        final Collection<Chip> original = getOriginalChips();
        final Collection<Chip> filtered = getFilteredChips();
        if (original == null || filtered == null) {
            throw new NullPointerException("Original or filtered collections cannot be null!");
        }

        // Clear any pre-existing chips
        original.clear();
        filtered.clear();

        // Add all the new chips into the collections, but ensure we
        // set every chip to filterable to prevent any errors
        for (Chip chip : chips) {
            chip.setFilterable(true);
            original.add(chip);
            filtered.add(chip);
        }

        // Tell our observers
        notifyDataSourceChanged();
    }

    /**
     * Default implementation.
     *
     * Attempts to get a non-null reference to the filtered collection and then
     * tries to avoid using the iterator pattern, if possible, to retrieve a chip
     * at the given position.
     *
     * This will try to use {@link RandomAccess} method first, and will fall back
     * on the iterator pattern if it cannot do the previous.
     *
     * @param position Position of chip
     * @return {@link Chip}
     */
    @Override
    public Chip getFilteredChip(int position) {
        final Collection<Chip> chips = getFilteredChips();
        if (chips == null) {
            throw new NullPointerException("Filtered chips cannot be null!");
        }

        if (chips instanceof RandomAccess) { // We know RandomAccess will always be a List
            return ((List<Chip>)chips).get(position);
        } else {
            Iterator<Chip> iterator = chips.iterator();
            for (int i = 0; iterator.hasNext(); i++) {
                final Chip chip = iterator.next();
                if (i == position) {
                    return chip;
                }
            }
        }
        return null;
    }

    /**
     * Default implementation.
     *
     * Attempts to get a non-null reference to the selected collection and then
     * tries to avoid using the iterator pattern, if possible, to retrieve a chip
     * at the given position.
     *
     * This will try to use {@link RandomAccess} method first, and will fall back
     * on the iterator pattern if it cannot do the previous.
     *
     * @param position Position of chip
     * @return {@link Chip}
     */
    @Override
    public Chip getSelectedChip(int position) {
        final Collection<Chip> chips = getSelectedChips();
        if (chips == null) {
            throw new NullPointerException("Selected chips cannot be null!");
        }

        if (chips instanceof RandomAccess) { // We know RandomAccess will always be a List
            return ((List<Chip>)chips).get(position);
        } else {
            Iterator<Chip> iterator = chips.iterator();
            for (int i = 0; iterator.hasNext(); i++) {
                final Chip chip = iterator.next();
                if (i == position) {
                    return chip;
                }
            }
        }
        return null;
    }

    /**
     * Default implementation.
     *
     * Attempts to take a chip from the filterable collections and then put it
     * in the selected collection.
     *
     * Will notify observers.
     *
     * @param chip {@link Chip}
     */
    @Override
    public void takeFilteredChip(Chip chip) {
        if (chip == null) {
            throw new NullPointerException("Chip cannot be null!");
        }

        // Check if chip is filterable
        if (chip.isFilterable()) {
            // Check if chip is actually in the filtered list
            final Collection<Chip> filtered = getFilteredChips();
            if (filtered.contains(chip)) {
                final Collection<Chip> original = getOriginalChips();
                final Collection<Chip> selected = getSelectedChips();

                // Remove the chip from the filterable collections and put it
                // in the selected collection
                original.remove(chip);
                filtered.remove(chip);
                selected.add(chip);
            } else {
                throw new IllegalArgumentException("Chip is not in filtered chip list!");
            }
        } else {
            throw new IllegalArgumentException("Cannot take a non-filterable chip!");
        }

        // Tell our observers!
        notifyDataSourceChanged();
        notifyChipSelected(chip);
    }

    /**
     * Default implementation.
     *
     * Attempts to take a chip from the filterable collections and then put it
     * in the selected collection using the index of the filtered chip.
     *
     * Will notify observers.
     *
     * @param position Position of chip
     */
    @Override
    public void takeFilteredChip(int position) {
        final Chip foundChip = getFilteredChip(position);
        if (foundChip == null) {
            throw new NullPointerException("Chip cannot be null; not found in filtered chip list!");
        }

        // Since we took the chip from the filtered chip list, it's redundant
        // to check if it's filterable again
        final Collection<Chip> original = getOriginalChips();
        final Collection<Chip> filtered = getFilteredChips();
        final Collection<Chip> selected = getSelectedChips();

        // Remove the chip from the filterable collections and put it
        // in the selected collection
        original.remove(foundChip);
        filtered.remove(foundChip);
        selected.add(foundChip);

        // Tell our observers!
        notifyDataSourceChanged();
        notifyChipSelected(foundChip);
    }

    /**
     * Default implementation.
     *
     * Attempts to take a chip from the selected collection and place it back
     * into the filterable collections.
     *
     * Will notify observers.
     *
     * @param chip {@link Chip}
     */
    @Override
    public void replaceFilteredChip(Chip chip) {
        if (chip == null) {
            throw new NullPointerException("Chip cannot be null!");
        }

        // Check if chip is actually selected
        final Collection<Chip> selected = getSelectedChips();
        if (selected.contains(chip)) {
            // Remove the chip from the selected collection
            selected.remove(chip);

            // Check if the chip is filterable
            if (chip.isFilterable()) {
                final Collection<Chip> original = getOriginalChips();
                final Collection<Chip> filtered = getFilteredChips();

                // Add the chip back into the original and filtered collections
                original.add(chip);
                filtered.add(chip);
            }

            // Tell our observers!
            notifyDataSourceChanged();
            notifyChipUnselected(chip);
        } else {
            throw new IllegalArgumentException("Chip is not in selected chip list!");
        }
    }

    /**
     * Default implementation.
     *
     * Attempts to take a chip from the selected collection and place it back
     * into the filterable collections using the index of the selected chip.
     *
     * Will notify observers.
     *
     * @param position Position of chip
     */
    @Override
    public void replaceFilteredChip(int position) {
        final Chip foundChip = getSelectedChip(position);
        if (foundChip == null) {
            throw new NullPointerException("Chip cannot be null; not found in selected chip list!");
        }

        // Since not null, we know the chip is selected
        final Collection<Chip> selected = getSelectedChips();
        selected.remove(foundChip);

        // Check if the chip is filterable
        if (foundChip.isFilterable()) {
            final Collection<Chip> original = getOriginalChips();
            final Collection<Chip> filtered = getFilteredChips();

            // Add the chip back into the original and filtered collections
            original.add(foundChip);
            filtered.add(foundChip);
        }

        // Tell our observers!
        notifyDataSourceChanged();
        notifyChipUnselected(foundChip);
    }

    /**
     * Default implementation.
     *
     * Attempts to add a chip into the filterable collections, ensuring the
     * chip's filterable flag is true.
     *
     * Will notify observers.
     *
     * @param chip {@link Chip}
     */
    @Override
    public void addFilteredChip(Chip chip) {
        if (chip == null) {
            throw new NullPointerException("Chip cannot be null!");
        }

        // Ensure the chip is filterable
        chip.setFilterable(true);

        // Add to the original and filtered collections
        final Collection<Chip> original = getOriginalChips();
        final Collection<Chip> filtered = getFilteredChips();
        original.add(chip);
        filtered.add(chip);

        // Tell our observers!
        notifyDataSourceChanged();
    }

    /**
     * Default implementation.
     *
     * Attempts to add a chip into the selected collection. The chip will be
     * place into the filterable collections if {@link #replaceFilteredChip(Chip)}
     * is called on it, if its filterable flag is set to true.
     *
     * Will notify observers.
     *
     * @param chip {@link Chip}
     */
    @Override
    public void addSelectedChip(Chip chip) {
        if (chip == null) {
            throw new NullPointerException("Chip cannot be null!");
        }

        // Add to the selected collection
        final Collection<Chip> selected = getSelectedChips();
        selected.add(chip);

        // Tell our observers!
        notifyDataSourceChanged();
        notifyChipSelected(chip);
    }

    /**
     * Default implementation.
     *
     * Attempts to get a non-null reference to our filtered collection
     * and clear its data.
     *
     * Will notify observers.
     */
    @Override
    public void clearFilterableChips() {
        final Collection<Chip> chips = getFilteredChips();
        if (chips != null) {
            chips.clear();
            notifyDataSourceChanged();
        }
    }

    /**
     * Default implementation.
     *
     * Attempts to get a non-null reference to selected collection
     * and clear its data.
     *
     * Will notify observers.
     */
    @Override
    public void clearSelectedChips() {
        final Collection<Chip> chips = getSelectedChips();
        if (chips != null) {
            // Clone the chips before clearing the list so we can appropriate
            // callback to the observers
            final Collection<Chip> clone = new LinkedList<>(chips);
            chips.clear();

            // Make sure we notify changed observers first
            notifyDataSourceChanged();
            for (Chip chip : clone) {
                notifyChipUnselected(chip);
            }
        }
    }

    @Override
    public boolean existsInFiltered(Chip chip) {
        if (chip == null) {
            throw new NullPointerException("Chip cannot be null!");
        }
        final Collection<Chip> chips = getFilteredChips();
        return chips.contains(chip);
    }

    @Override
    public boolean existsInSelected(Chip chip) {
        if (chip == null) {
            throw new NullPointerException("Chip cannot be null!");
        }
        final Collection<Chip> chips = getSelectedChips();
        return chips.contains(chip);
    }

    @Override
    public boolean existsInDataSource(Chip chip) {
        if (chip == null) {
            throw new NullPointerException("Chip cannot be null!");
        }

        // Instead of checking the actual filtered collection, we'll check the original
        // collection because we know if the original contains it, so will the filtered

        final Collection<Chip> original = getOriginalChips();
        final Collection<Chip> selected = getSelectedChips();
        return original.contains(chip) || selected.contains(chip);
    }

    @Override
    public final void addChipChangedObserver(ChipChangedObserver observer) {
        if (observer == null) {
            throw new NullPointerException("Cannot add null observer!");
        }
        if (changedObservers == null) {
            this.changedObservers = new LinkedList<>();
        }
        this.changedObservers.add(observer);
    }

    @Override
    public final void removeChipChangedObserver(ChipChangedObserver observer) {
        if (observer == null) {
            throw new NullPointerException("Cannot remove null observer!");
        }
        if (changedObservers != null) {
            this.changedObservers.remove(observer);
        }
    }

    @Override
    public final void removeAllChipChangedObservers() {
        if (changedObservers != null) {
            this.changedObservers.clear();
            this.changedObservers = null;
        }
    }

    @Override
    public final void addChipSelectionObserver(ChipSelectionObserver observer) {
        if (observer == null) {
            throw new NullPointerException("Cannot add null observer!");
        }
        if (selectionObservers == null) {
            this.selectionObservers = new LinkedList<>();
        }
        this.selectionObservers.add(observer);
    }

    @Override
    public final void removeChipSelectionObserver(ChipSelectionObserver observer) {
        if (observer == null) {
            throw new NullPointerException("Cannot remove null observer!");
        }
        if (selectionObservers != null) {
            this.selectionObservers.remove(observer);
        }
    }

    @Override
    public final void removeAllChipSelectionObservers() {
        if (selectionObservers != null) {
            this.selectionObservers.clear();
            this.selectionObservers = null;
        }
    }

    /**
     * Notifies {@link #changedObservers} that a change to the data source has occurred.
     */
    protected final void notifyDataSourceChanged() {
        if (changedObservers != null) {
            synchronized (this) {
                for (ChipChangedObserver ob : changedObservers) {
                    ob.onChipDataSourceChanged();
                }
            }
        }
    }

    /**
     * Notifies {@link #selectionObservers} that a chip has been selected.
     * @param selected {@link Chip}
     */
    protected final void notifyChipSelected(Chip selected) {
        if (selectionObservers != null) {
            synchronized (this) {
                for (ChipSelectionObserver ob : selectionObservers) {
                    ob.onChipSelected(selected);
                }
            }
        }
    }

    /**
     * Notifies {@link #selectionObservers} that a chip has been unselected.
     * @param unselected {@link Chip}
     */
    protected final void notifyChipUnselected(Chip unselected) {
        if (selectionObservers != null) {
            synchronized (this) {
                for (ChipSelectionObserver ob : selectionObservers) {
                    ob.onChipDeselected(unselected);
                }
            }
        }
    }


    /**
     * A helper method to copy observers from one chip data source to another
     * without having to expose any of the observers themselves.
     *
     * @param from {@link ChipDataSource} to copy observers from
     * @param to {@link ChipDataSource} to copy observers to
     */
    public static void copyObservers(IChipDataSource from, IChipDataSource to) {
        if (!(from instanceof ChipDataSource && to instanceof ChipDataSource)) {
            throw new IllegalArgumentException("Can only copy observers from " +
                    "ChipDataSource or its descendants!");
        }
        final ChipDataSource c1 = (ChipDataSource)from;
        final ChipDataSource c2 = (ChipDataSource)to;
        if (c1.changedObservers != null) {
            c2.changedObservers = new LinkedList<>();
            Collections.copy(c2.changedObservers, c1.changedObservers);
        }
        if (c1.selectionObservers != null) {
            c2.selectionObservers = new LinkedList<>();
            Collections.copy(c2.selectionObservers, c1.selectionObservers);
        }
    }
}