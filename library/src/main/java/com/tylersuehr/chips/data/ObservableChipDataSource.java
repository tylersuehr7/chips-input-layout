package com.tylersuehr.chips.data;
import android.support.annotation.VisibleForTesting;
import java.util.LinkedList;
import java.util.List;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * Implementation of {@link ChipDataSource} to provide the basic functionality for
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
    List<SelectionObserver> mSelectionObservers;

    /* Aggregation of observers to watch changes to data source */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    List<ChangeObserver> mChangeObservers;


    @Override
    public final void addSelectionObserver(SelectionObserver observer) {
        if (observer == null) {
            throw new NullPointerException("Observer cannot be null!");
        }
        if (mSelectionObservers == null) {
            mSelectionObservers = new LinkedList<>();
        }
        mSelectionObservers.add(observer);
    }

    @Override
    public final void removeSelectionObserver(SelectionObserver observer) {
        if (observer == null) {
            throw new NullPointerException("Observer cannot be null!");
        }
        if (mSelectionObservers != null) {
            mSelectionObservers.remove(observer);
        }
    }

    @Override
    public final void removeAllSelectionObservers() {
        if (mSelectionObservers != null) {
            mSelectionObservers.clear();
            mSelectionObservers = null;
        }
    }

    @Override
    public final void addChangedObserver(ChangeObserver observer) {
        if (observer == null) {
            throw new NullPointerException("Observer cannot be null!");
        }
        if (mChangeObservers == null) {
            mChangeObservers = new LinkedList<>();
        }
        mChangeObservers.add(observer);
    }

    @Override
    public final void removeChangedObserver(ChangeObserver observer) {
        if (observer == null) {
            throw new NullPointerException("Observer cannot be null!");
        }
        if (mChangeObservers != null) {
            mChangeObservers.remove(observer);
        }
    }

    @Override
    public final void removeAllChangedObservers() {
        if (mChangeObservers != null) {
            mChangeObservers.clear();
            mChangeObservers = null;
        }
    }

    @Override
    public final void cloneObservers(ChipDataSource to) {
        if (mSelectionObservers != null) {
            for (SelectionObserver ob : mSelectionObservers) {
                to.addSelectionObserver(ob);
            }
        }
        if (mChangeObservers != null) {
            for (ChangeObserver ob : mChangeObservers) {
                to.addChangedObserver(ob);
            }
        }
    }

    /**
     * Notifies {@link #mChangeObservers} that a change to the data
     * source happened.
     */
    protected final void notifyDataSourceChanged() {
        if (mChangeObservers != null) {
            synchronized (this) {
                for (ChangeObserver ob : mChangeObservers) {
                    ob.onChipDataSourceChanged();
                }
            }
        }
    }

    /**
     * Notifies {@link #mSelectionObservers} that a chip was selected
     * in the data source.
     * @param chip {@link Chip} selected
     */
    protected final void notifyChipSelected(Chip chip) {
        if (mSelectionObservers != null) {
            synchronized (this) {
                for (SelectionObserver ob : mSelectionObservers) {
                    ob.onChipSelected(chip);
                }
            }
        }
    }

    /**
     * Notifies {@link #mSelectionObservers} that a chip was unselected
     * in the data source.
     * @param chip {@link Chip} unselected
     */
    protected final void notifyChipUnselected(Chip chip) {
        if (mSelectionObservers != null) {
            synchronized (this) {
                for (SelectionObserver ob : mSelectionObservers) {
                    ob.onChipDeselected(chip);
                }
            }
        }
    }
}