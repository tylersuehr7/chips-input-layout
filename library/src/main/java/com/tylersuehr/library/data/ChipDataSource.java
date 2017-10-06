package com.tylersuehr.library.data;
import java.util.List;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public interface ChipDataSource {
    List<Chip> getSelectedChips();
    List<Chip> getFilteredChips();
    List<Chip> getOriginalChips();

    void setFilterableChips(List<? extends Chip> chips);
    void takeChip(Chip chip);
    void replaceChip(Chip chip);

    void registerObserver(ChipDataSourceObserver observer);
    void unregisterObserver(ChipDataSourceObserver observer);
    void unregisterAllObservers();
}