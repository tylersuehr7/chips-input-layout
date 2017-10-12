package com.tylersuehr.library.data;

import android.support.test.runner.AndroidJUnit4;

import com.tylersuehr.library.Mocker;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * Unit tests the functionality of {@link ListChipDataSource}.
 *
 * @author Tyler Suehr
 * @version 1.0
 */
@RunWith(AndroidJUnit4.class)
public class ListChipDataSourceTest {
    @Test
    public void getOriginalChips() throws Exception {
        final ListChipDataSource ls = new ListChipDataSource();
        assertNotNull(ls.getOriginalChips());
    }

    @Test
    public void getFilteredChips() throws Exception {
        final ListChipDataSource ls = new ListChipDataSource();
        assertNotNull(ls.getFilteredChips());
    }

    @Test
    public void getSelectedChips() throws Exception {
        final ListChipDataSource ls = new ListChipDataSource();
        assertNotNull(ls.getSelectedChips());
    }

    @Test
    public void setFilterableChips() throws Exception {
        final List<Mocker.TestChip> chips = Mocker.mockChips(7);

        final ListChipDataSource ls = new ListChipDataSource();
        ls.setFilterableChips(chips);

        assertNotNull(ls.selectedChips);
        for (Chip chip : chips) {
            assertTrue(ls.originalChips.contains(chip));
            assertTrue(ls.filteredChips.contains(chip));
        }
    }

    @Test
    public void getFilteredChip() throws Exception {
        final Chip chip = Mocker.mock();

        final ListChipDataSource ls = new ListChipDataSource();
        ls.addFilteredChip(chip);

        final Chip found = ls.getFilteredChip(0);
        assertEquals(chip, found);
    }

    @Test
    public void getSelectedChip() throws Exception {
        final Chip chip = Mocker.mock();

        final ListChipDataSource ls = new ListChipDataSource();
        ls.addSelectedChip(chip);

        final Chip found = ls.getSelectedChip(0);
        assertEquals(chip, found);
    }

    @Test
    public void clearFilterableChips() throws Exception {
        final ListChipDataSource ls = new ListChipDataSource();
        ls.addFilteredChip(Mocker.mock());
        ls.addFilteredChip(Mocker.mock());
        ls.addFilteredChip(Mocker.mock());
        ls.clearFilterableChips();

        assertTrue(ls.filteredChips.isEmpty());
    }

    @Test
    public void clearSelectedChips() throws Exception {
        final ListChipDataSource ls = new ListChipDataSource();
        ls.addSelectedChip(Mocker.mock());
        ls.addSelectedChip(Mocker.mock());
        ls.addSelectedChip(Mocker.mock());
        ls.clearSelectedChips();

        assertTrue(ls.selectedChips.isEmpty());
    }
}