package com.tylersuehr.library.data;

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
        chips.add(new TestChip("0", "Chip"));
        chips.add(new TestChip("1", "Chip"));
        chips.add(new TestChip("2", "Chip"));
        chips.add(new TestChip("3", "Chip"));
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
        final Chip chip = new TestChip("0", "takeFilteredChip");

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
        final Chip chip = new TestChip("0", "takeFilteredChip");

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
        final Chip chip = new TestChip("0", "takeNonFilteredChip");
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
        final Chip chip = new TestChip("0", "replaceNonFilteredChip");
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
    public void takeFilteredChipByPosition() throws Exception {
        // Create the initial chip list
        List<Chip> chips = new ArrayList<>();
        chips.add(new TestChip("0", "Chip 1"));
        chips.add(new TestChip("1", "Chip 2"));
        chips.add(new TestChip("2", "Chip 3"));

        // Create the data
        final ListChipDataSource ls = new ListChipDataSource();
        ls.setFilterableChips(chips);

        ls.takeChip(1);

        // Since chip was filterable, make sure it's in selected list only
        final Chip chip = chips.get(1);
        assertTrue(ls.selectedChips.contains(chip));
        assertFalse(ls.filteredChips.contains(chip));
        assertFalse(ls.originalChips.contains(chip));
    }

    @Test
    public void replaceFilteredChipByPosition() throws Exception {
        // Create the initial chip list
        List<Chip> chips = new ArrayList<>();
        chips.add(new TestChip("0", "Chip 1"));
        chips.add(new TestChip("1", "Chip 2"));
        chips.add(new TestChip("2", "Chip 3"));

        // Create the data
        final ListChipDataSource ls = new ListChipDataSource();
        ls.setFilterableChips(chips);

        ls.takeChip(1);
        ls.replaceChip(0);

        // Since chip was filterable, make sure it's in filtered list only
        final Chip chip = chips.get(1);
        assertFalse(ls.selectedChips.contains(chip));
        assertTrue(ls.filteredChips.contains(chip));
        assertTrue(ls.originalChips.contains(chip));
    }

    @Test
    public void getFilteredChip() throws Exception {
        final Chip chip = new TestChip("Chip", "Chip");
        final List<Chip> chips = new ArrayList<>();
        chips.add(chip);

        final ListChipDataSource ls = new ListChipDataSource();
        ls.setFilterableChips(chips);

        ls.takeChip(chip);
        ls.replaceChip(chip);

        final Chip found = ls.getFilteredChip(0);
        assertNotNull(found);
        assertEquals(chip, found);
    }

    @Test
    public void getSelectedChip() throws Exception {
        final Chip chip = new TestChip("Chip", "Chip");
        final ListChipDataSource ls = new ListChipDataSource();

        ls.takeChip(chip);

        final Chip found = ls.getSelectedChip(0);
        assertNotNull(found);
        assertEquals(chip, found);
    }

    @Test
    public void checkNotInDataSource() throws Exception {
        final Chip chip = new TestChip("Chip 1", "Chippy");
        final ListChipDataSource ls = new ListChipDataSource();

        assertFalse(ls.existsInDataSource(chip));
    }

    @Test
    public void checkInDataSource() throws Exception {
        final Chip chip = new TestChip("Chip 1", "Chip");

        final ListChipDataSource ls = new ListChipDataSource();
        ls.takeChip(chip);

        assertTrue(ls.existsInDataSource(chip));
    }

    @Test
    public void triggerChipAddedCallback() throws Exception {
        final OnChipSelectedObserver observer = new OnChipSelectedObserver() {
            @Override
            public void onChipAdded(Chip addedChip) {
                System.out.println("triggerChipAddedCallback() -> SUCCESS!");
            }

            @Override
            public void onChipRemoved(Chip removedChip) {
                fail("Chip should have been added, not removed!");
            }
        };

        final ListChipDataSource ls = new ListChipDataSource();
        ls.setFilterableChips(mockChips());
        ls.setOnChipSelectedObserver(observer);

        ls.takeChip(1);
    }

    @Test
    public void triggerChipRemovedCallback() throws Exception {
        final OnChipSelectedObserver observer = new OnChipSelectedObserver() {
            @Override
            public void onChipAdded(Chip addedChip) {
                fail("Chip should have been removed, not added!");
            }

            @Override
            public void onChipRemoved(Chip removedChip) {
                System.out.println("triggerChipRemovedCallback() -> SUCCESS!");
            }
        };

        final ListChipDataSource ls = new ListChipDataSource();
        ls.setFilterableChips(mockChips());
        ls.takeChip(1);

        ls.setOnChipSelectedObserver(observer);
        ls.replaceChip(0);
    }

    @Test
    public void setOnChipSelectedListener() throws Exception {
        final OnChipSelectedObserver observer = new OnChipSelectedObserver() {
            @Override
            public void onChipAdded(Chip addedChip) {

            }

            @Override
            public void onChipRemoved(Chip removedChip) {

            }
        };
        final ListChipDataSource ls = new ListChipDataSource();
        ls.setOnChipSelectedObserver(observer);

        assertTrue(ls.chipSelectedObserver == observer);
    }

    @Test
    public void registerObserver() throws Exception {
        final OnChipChangedObserver result = new OnChipChangedObserver() {
            @Override
            public void onChipDataSourceChanged() {

            }
        };
        final ListChipDataSource cm = new ListChipDataSource();

        cm.addOnChipChangedObserver(result);
        assertTrue(cm.observers.contains(result));
    }

    @Test
    public void unregisterObserver() throws Exception {
        final OnChipChangedObserver result = new OnChipChangedObserver() {
            @Override
            public void onChipDataSourceChanged() {

            }
        };
        final ListChipDataSource cm = new ListChipDataSource();

        cm.addOnChipChangedObserver(result);
        assertTrue(cm.observers.contains(result));

        cm.removeOnChipChangedObserver(result);
        assertFalse(cm.observers.contains(result));
    }

    @Test
    public void unregisterAllObservers() throws Exception {
        final OnChipChangedObserver result = new OnChipChangedObserver() {
            @Override
            public void onChipDataSourceChanged() {

            }
        };
        final OnChipChangedObserver result2 = new OnChipChangedObserver() {
            @Override
            public void onChipDataSourceChanged() {

            }
        };
        final ListChipDataSource cm = new ListChipDataSource();

        cm.addOnChipChangedObserver(result);
        cm.addOnChipChangedObserver(result2);

        cm.removeAllOnChipChangedObservers();
        assertTrue(cm.observers.isEmpty());
    }

    private static List<TestChip> mockChips() {
        List<TestChip> chips = new ArrayList<>();
        chips.add(new TestChip("Chip 1", ""));
        chips.add(new TestChip("Chip 2", ""));
        chips.add(new TestChip("Chip 3", ""));
        chips.add(new TestChip("Chip 4", ""));
        chips.add(new TestChip("Chip 5", ""));
        chips.add(new TestChip("Chip 6", ""));
        return chips;
    }
}