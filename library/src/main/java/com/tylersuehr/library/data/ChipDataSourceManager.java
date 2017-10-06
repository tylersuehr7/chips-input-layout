package com.tylersuehr.library.data;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * Defines a basic observer manager for {@link ChipDataSourceObserver}.
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public interface ChipDataSourceManager {
    void registerObserver(ChipDataSourceObserver observer);
    void unregisterObserver(ChipDataSourceObserver observer);
    void unregisterAllObservers();
}