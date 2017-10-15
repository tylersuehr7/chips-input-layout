package com.tylersuehr.chips.data;
import android.support.annotation.VisibleForTesting;
import java.util.LinkedList;
import java.util.List;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * This implementation of {@link ChipDataSource} provides the basic functionality for
 * observers ONLY. It manages the observers in a {@link java.util.LinkedList}, and
 * includes convenience methods for notifying them too.
 *
 * Note: when notifying observers, it's a good idea to notify change observers first
 * because that will update the internal components before any other observers.
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public abstract class ObservableChipDataSource implements ChipDataSource {
    /* Aggregation of observers to watch changes to chip selection */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    List<ChipSelectionObserver> selectionObservers;

    /* Aggregation of observers to watch changes to data source */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    List<ChipChangedObserver> changeObservers;


    @Override
    public final void addChipSelectionObserver(ChipSelectionObserver observer) {
        if (observer == null) {
            throw new NullPointerException("ChipSelectionObserver cannot be null!");
        }
        if (selectionObservers == null) {
            this.selectionObservers = new LinkedList<>();
        }
        this.selectionObservers.add(observer);
    }

    @Override
    public final void removeChipSelectionObserver(ChipSelectionObserver observer) {
        if (observer == null) {
            throw new NullPointerException("ChipSelectionObserver cannot be null!");
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

    @Override
    public final void addChipChangedObserver(ChipChangedObserver observer) {
        if (observer == null) {
            throw new NullPointerException("ChipChangedObserver cannot be null!");
        }
        if (changeObservers == null) {
            this.changeObservers = new LinkedList<>();
        }
        this.changeObservers.add(observer);
    }

    @Override
    public final void removeChipChangedObserver(ChipChangedObserver observer) {
        if (observer == null) {
            throw new NullPointerException("ChipChangedObserver cannot be null!");
        }
        if (changeObservers != null) {
            this.changeObservers.remove(observer);
        }
    }

    @Override
    public final void removeAllChipChangedObservers() {
        if (changeObservers != null) {
            this.changeObservers.clear();
            this.changeObservers = null;
        }
    }

    @Override
    public final void cloneObservers(ChipDataSource to) {
        if (selectionObservers != null) {
            for (ChipSelectionObserver ob : selectionObservers) {
                to.addChipSelectionObserver(ob);
            }
        }
        if (changeObservers != null) {
            for (ChipChangedObserver ob : changeObservers) {
                to.addChipChangedObserver(ob);
            }
        }
    }

    /**
     * Notifies {@link #changeObservers} that a change to the data source happened.
     */
    protected final void notifyDataSourceChanged() {
        if (changeObservers != null) {
            synchronized (this) {
                for (ChipChangedObserver ob : changeObservers) {
                    ob.onChipDataSourceChanged();
                }
            }
        }
    }

    /**
     * Notifies {@link #selectionObservers} that a chip was selected in the data source.
     * @param chip {@link Chip} selected
     */
    protected final void notifyChipSelected(Chip chip) {
        if (selectionObservers != null) {
            synchronized (this) {
                for (ChipSelectionObserver ob : selectionObservers) {
                    ob.onChipSelected(chip);
                }
            }
        }
    }

    /**
     * Notifies {@link #selectionObservers} that a chip was unselected in the data source.
     * @param chip {@link Chip} unselected
     */
    protected final void notifyChipUnselected(Chip chip) {
        if (selectionObservers != null) {
            synchronized (this) {
                for (ChipSelectionObserver ob : selectionObservers) {
                    ob.onChipDeselected(chip);
                }
            }
        }
    }
}