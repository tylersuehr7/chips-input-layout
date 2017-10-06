package com.tylersuehr.library.data;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public interface ChipDataSourceManager {
    void registerObserver(ChipDataSourceObserver observer);
    void unregisterObserver(ChipDataSourceObserver observer);
    void unregisterAllObservers();
}