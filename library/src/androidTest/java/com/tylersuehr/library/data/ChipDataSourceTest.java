package com.tylersuehr.library.data;

import com.tylersuehr.library.Mocker;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * Unit tests the functionality of {@link ChipDataSource}.
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public class ChipDataSourceTest {
    @Test
    public void takeFilteredChip() throws Exception {
        final Chip chip = Mocker.mock();

        final ChipDataSource cs = new TCDS();
        cs.addFilteredChip(chip);
        cs.takeFilteredChip(chip);

        assertTrue(cs.getSelectedChips().contains(chip));
        assertFalse(cs.getFilteredChips().contains(chip));
        assertFalse(cs.getOriginalChips().contains(chip));
    }

    @Test
    public void takeFilteredChipByPosition() throws Exception {
        final Chip chip = Mocker.mock();

        final ChipDataSource cs = new TCDS();
        cs.addFilteredChip(chip);
        cs.takeFilteredChip(0);

        assertTrue(cs.getSelectedChips().contains(chip));
        assertFalse(cs.getFilteredChips().contains(chip));
        assertFalse(cs.getOriginalChips().contains(chip));
    }

    @Test
    public void replaceFilteredChip() throws Exception {
        final Chip chip = Mocker.mock();
        chip.setFilterable(true);

        final ChipDataSource cs = new TCDS();
        cs.addSelectedChip(chip);
        cs.replaceFilteredChip(chip);

        assertTrue(cs.getFilteredChips().contains(chip));
        assertTrue(cs.getOriginalChips().contains(chip));
        assertFalse(cs.getSelectedChips().contains(chip));
    }

    @Test
    public void replaceFilteredChipByPosition() throws Exception {
        final Chip chip = Mocker.mock();
        chip.setFilterable(true);

        final ChipDataSource cs = new TCDS();
        cs.addSelectedChip(chip);
        cs.replaceFilteredChip(0);

        assertTrue(cs.getFilteredChips().contains(chip));
        assertTrue(cs.getOriginalChips().contains(chip));
        assertFalse(cs.getSelectedChips().contains(chip));
    }

    @Test
    public void getFilteredChip() throws Exception {
        final Chip chip = Mocker.mock();

        final ChipDataSource cs = new TCDS();
        cs.addFilteredChip(chip);

        final Chip found = cs.getFilteredChip(0);
        assertNotNull(found);
        assertEquals(chip, found);
    }

    @Test
    public void getSelectedChip() throws Exception {
        final Chip chip = Mocker.mock();

        final ChipDataSource cs = new TCDS();
        cs.addSelectedChip(chip);

        final Chip found = cs.getSelectedChip(0);
        assertNotNull(found);
        assertEquals(chip, found);
    }

    @Test
    public void addFilteredChip() throws Exception {
        final Chip chip = Mocker.mock();

        final ChipDataSource cs = new TCDS();
        cs.addFilteredChip(chip);

        assertNotNull(cs.getFilteredChips());
        assertTrue(cs.getFilteredChips().contains(chip));
    }

    @Test
    public void addSelectedChip() throws Exception {
        final Chip chip = Mocker.mock();

        final ChipDataSource cs = new TCDS();
        cs.addSelectedChip(chip);

        assertNotNull(cs.getSelectedChips());
        assertTrue(cs.getSelectedChips().contains(chip));
    }

    @Test
    public void clearFilteredChips() throws Exception {
        final TCDS cs = new TCDS();
        cs.filtered.add(Mocker.mock());
        cs.filtered.add(Mocker.mock());
        cs.clearFilterableChips();

        assertNotNull(cs.getFilteredChips());
        assertTrue(cs.getFilteredChips().isEmpty());
    }

    @Test
    public void clearSelectedChips() throws Exception {
        final TCDS cs = new TCDS();
        cs.filtered.add(Mocker.mock());
        cs.filtered.add(Mocker.mock());
        cs.clearSelectedChips();

        assertNotNull(cs.getSelectedChips());
        assertTrue(cs.getSelectedChips().isEmpty());
    }

    @Test
    public void existsInFiltered() throws Exception {
        final Chip chip = Mocker.mock();

        final TCDS cs = new TCDS();
        cs.filtered.add(chip);

        assertTrue(cs.existsInFiltered(chip));
    }

    @Test
    public void existsInSelected() throws Exception {
        final Chip chip = Mocker.mock();

        final TCDS cs = new TCDS();
        cs.selected.add(chip);

        assertTrue(cs.existsInSelected(chip));
    }

    @Test
    public void existsInDataSource() throws Exception {
        final Chip chip = Mocker.mock();

        final TCDS cs = new TCDS();
        cs.original.add(chip);

        assertTrue(cs.existsInDataSource(chip));
    }

    @Test
    public void addChipChangedObserver() throws Exception {
        final ChipChangedObserver observer = Mocker.mockChangedObserver();

        final ChipDataSource cs = new TCDS();
        cs.addChipChangedObserver(observer);

        assertNotNull(cs.changedObservers);
        assertTrue(cs.changedObservers.contains(observer));
    }

    @Test
    public void removeChipChangedObserver() throws Exception {
        final ChipChangedObserver observer = Mocker.mockChangedObserver();

        final ChipDataSource cs = new TCDS();
        cs.addChipChangedObserver(observer);
        cs.removeChipChangedObserver(observer);

        assertNotNull(cs.changedObservers);
        assertFalse(cs.changedObservers.contains(observer));
    }

    @Test
    public void removeAllChipChangedObservers() throws Exception {
        final ChipChangedObserver ob1 = Mocker.mockChangedObserver();
        final ChipChangedObserver ob2 = Mocker.mockChangedObserver();

        final ChipDataSource cs = new TCDS();
        cs.addChipChangedObserver(ob1);
        cs.addChipChangedObserver(ob2);
        cs.removeAllChipChangedObservers();

        assertNull(cs.changedObservers);
    }

    @Test
    public void addChipSelectionObserver() throws Exception {
        final ChipSelectionObserver ob = Mocker.mockSelectionObserver();

        final ChipDataSource cs = new TCDS();
        cs.addChipSelectionObserver(ob);

        assertNotNull(cs.selectionObservers);
        assertTrue(cs.selectionObservers.contains(ob));
    }

    @Test
    public void removeChipSelectionObserver() throws Exception {
        final ChipSelectionObserver ob = Mocker.mockSelectionObserver();

        final ChipDataSource cs = new TCDS();
        cs.addChipSelectionObserver(ob);
        cs.removeChipSelectionObserver(ob);

        assertNotNull(cs.selectionObservers);
        assertFalse(cs.selectionObservers.contains(ob));
    }

    @Test
    public void removeAllChipSelectionObservers() throws Exception {
        final ChipSelectionObserver ob1 = Mocker.mockSelectionObserver();
        final ChipSelectionObserver ob2 = Mocker.mockSelectionObserver();

        final ChipDataSource cs = new TCDS();
        cs.addChipSelectionObserver(ob1);
        cs.addChipSelectionObserver(ob2);
        cs.removeAllChipSelectionObservers();

        assertNull(cs.selectionObservers);
    }


    /**
     * Subclass of {@link ChipDataSource} to provide implementations of abstract
     * methods needed for testing it.
     */
    private static final class TCDS extends ChipDataSource {
        final List<Chip> original = new ArrayList<>();
        final List<Chip> filtered = new ArrayList<>();
        final List<Chip> selected = new ArrayList<>();

        @Override
        public Collection<Chip> getOriginalChips() {
            return original;
        }

        @Override
        public Collection<Chip> getFilteredChips() {
            return filtered;
        }

        @Override
        public Collection<Chip> getSelectedChips() {
            return selected;
        }
    }
}