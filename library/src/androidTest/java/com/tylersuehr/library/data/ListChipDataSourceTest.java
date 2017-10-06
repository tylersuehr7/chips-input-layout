package com.tylersuehr.library.data;

import android.support.annotation.Nullable;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * Tests the concrete functionality of {@link ListChipDataSource}.
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public class ListChipDataSourceTest {
    @Test
    public void constructTest() throws Exception {
        final ListChipDataSource ls = new ListChipDataSource();
        assertTrue(ls.originalChips.isEmpty());
        assertTrue(ls.filteredChips.isEmpty());
        assertTrue(ls.selectedChips.isEmpty());
    }

    @Test
    public void setFilterableChips() throws Exception {
        final ListChipDataSource ls = new ListChipDataSource();

        List<Chip> chips = new ArrayList<>();
        chips.add(new DataChipImpl("0", "Chip", "Chip", null));
        chips.add(new DataChipImpl("1", "Chip", "Chip", null));
        chips.add(new DataChipImpl("2", "Chip", "Chip", null));
        chips.add(new DataChipImpl("3", "Chip", "Chip", null));
        ls.setFilterableChips(chips);

        assertFalse(ls.originalChips.isEmpty());
        assertFalse(ls.filteredChips.isEmpty());
        assertTrue(ls.selectedChips.isEmpty());
    }

    @Test
    public void selectedListTest() throws Exception {
        final ListChipDataSource ls = new ListChipDataSource();

        List<Chip> chips = new ArrayList<>();
        ls.selectedChips = chips;

        assertNotNull(ls.selectedChips);
        assertEquals(chips, ls.getSelectedChips());
    }

    @Test
    public void filteredListTest() throws Exception {
        final ListChipDataSource ls = new ListChipDataSource();

        List<Chip> chips = new ArrayList<>();
        ls.filteredChips = chips;

        assertNotNull(ls.filteredChips);
        assertEquals(chips, ls.getFilteredChips());
    }

    @Test
    public void originalListTest() throws Exception {
        final ListChipDataSource ls = new ListChipDataSource();

        List<Chip> chips = new ArrayList<>();
        ls.originalChips = chips;

        assertNotNull(ls.originalChips);
        assertEquals(chips, ls.getOriginalChips());
    }

    @Test
    public void takeFilteredChip() throws Exception {
        // Create a test chip to use
        final Chip chip = new DataChipImpl("0", "takeFilteredChip", "Chippy", null);

        // Create the data source
        final ListChipDataSource ls = new ListChipDataSource();

        // Set chips as filterable
        List<Chip> chips = new ArrayList<>();
        chips.add(chip);
        ls.setFilterableChips(chips);

        ls.takeChip(chip);

        // Since chip was filterable, make sure it's in selected list only
        assertTrue(ls.selectedChips.contains(chip));
        assertFalse(ls.filteredChips.contains(chip));
        assertFalse(ls.originalChips.contains(chip));
    }

    @Test
    public void replaceFilteredChip() throws Exception {
        // Create a test chip to use
        final Chip chip = new DataChipImpl("0", "takeFilteredChip", "Chippy", null);

        // Create the data source
        final ListChipDataSource ls = new ListChipDataSource();

        // Set chips as filterable
        List<Chip> chips = new ArrayList<>();
        chips.add(chip);
        ls.setFilterableChips(chips);

        ls.takeChip(chip);
        ls.replaceChip(chip);

        // Since chip was filterable, make sure it's in filtered list only
        assertFalse(ls.selectedChips.contains(chip));
        assertTrue(ls.filteredChips.contains(chip));
        assertTrue(ls.originalChips.contains(chip));
    }

    @Test
    public void takeNonFilteredChip() throws Exception {
        // Create a test chip to use
        final Chip chip = new DataChipImpl("0", "takeNonFilteredChip", "Chippy", null);
        chip.setFilterable(false);

        // Create the data source
        final ListChipDataSource ls = new ListChipDataSource();
        ls.takeChip(chip);

        // Since chip was not filterable, make sure it's in selected list only
        assertTrue(ls.selectedChips.contains(chip));
        assertFalse(ls.filteredChips.contains(chip));
        assertFalse(ls.originalChips.contains(chip));
    }

    @Test
    public void replaceNonFilteredChip() throws Exception {
        // Create a test chip to use
        final Chip chip = new DataChipImpl("0", "replaceNonFilteredChip", "Chippy", null);
        chip.setFilterable(false);

        // Create the data source
        final ListChipDataSource ls = new ListChipDataSource();
        ls.takeChip(chip);

        ls.replaceChip(chip);

        // Since chip was not filterable, make sure it's not in selected or filtered list
        assertFalse(ls.selectedChips.contains(chip));
        assertFalse(ls.filteredChips.contains(chip));
        assertFalse(ls.originalChips.contains(chip));
    }

    @Test
    public void registerObserver() throws Exception {
        final ChipDataSourceObserver result = new ChipDataSourceObserver() {
            @Override
            public void onChipDataSourceChanged(@Nullable Chip affectedChip) {

            }
        };
        final ListChipDataSource cm = new ListChipDataSource();

        cm.registerObserver(result);
        assertTrue(cm.observers.contains(result));
    }

    @Test
    public void unregisterObserver() throws Exception {
        final ChipDataSourceObserver result = new ChipDataSourceObserver() {
            @Override
            public void onChipDataSourceChanged(@Nullable Chip affectedChip) {

            }
        };
        final ListChipDataSource cm = new ListChipDataSource();

        cm.registerObserver(result);
        assertTrue(cm.observers.contains(result));

        cm.unregisterObserver(result);
        assertFalse(cm.observers.contains(result));
    }

    @Test
    public void unregisterAllObservers() throws Exception {
        final ChipDataSourceObserver result = new ChipDataSourceObserver() {
            @Override
            public void onChipDataSourceChanged(@Nullable Chip affectedChip) {

            }
        };
        final ChipDataSourceObserver result2 = new ChipDataSourceObserver() {
            @Override
            public void onChipDataSourceChanged(@Nullable Chip affectedChip) {

            }
        };
        final ListChipDataSource cm = new ListChipDataSource();

        cm.registerObserver(result);
        cm.registerObserver(result2);

        cm.unregisterAllObservers();
        assertTrue(cm.observers.isEmpty());
    }
}