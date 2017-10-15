package com.tylersuehr.chips.data;

import com.tylersuehr.chips.Mocker;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public class ObservableChipDataSourceTest {
    @Test
    public void addChipSelectionObserver() throws Exception {
        final ChipSelectionObserver ob = Mocker.mockSelectionObserver();

        final ObservableChipDataSource cs = new TestDataSource();
        cs.addChipSelectionObserver(ob);

        assertTrue(cs.selectionObservers.contains(ob));
    }

    @Test
    public void removeChipSelectionObserver() throws Exception {
        final ChipSelectionObserver ob = Mocker.mockSelectionObserver();

        final ObservableChipDataSource cs = new TestDataSource();
        cs.addChipSelectionObserver(ob);
        cs.removeChipSelectionObserver(ob);

        assertFalse(cs.selectionObservers.contains(ob));
    }

    @Test
    public void removeAllChipSelectionObservers() throws Exception {
        final ObservableChipDataSource cs = new TestDataSource();
        cs.addChipSelectionObserver(Mocker.mockSelectionObserver());
        cs.addChipSelectionObserver(Mocker.mockSelectionObserver());
        cs.addChipSelectionObserver(Mocker.mockSelectionObserver());
        cs.removeAllChipSelectionObservers();

        assertNull(cs.selectionObservers);
    }

    @Test
    public void addChipChangedObserver() throws Exception {
        final ChipChangedObserver ob = Mocker.mockChangedObserver();

        final ObservableChipDataSource cs = new TestDataSource();
        cs.addChipChangedObserver(ob);

        assertTrue(cs.changeObservers.contains(ob));
    }

    @Test
    public void removeChipChangedObserver() throws Exception {
        final ChipChangedObserver ob = Mocker.mockChangedObserver();

        final ObservableChipDataSource cs = new TestDataSource();
        cs.addChipChangedObserver(ob);
        cs.removeChipChangedObserver(ob);

        assertFalse(cs.changeObservers.contains(ob));
    }

    @Test
    public void removeAllChipChangedObservers() throws Exception {
        final ObservableChipDataSource cs = new TestDataSource();
        cs.addChipChangedObserver(Mocker.mockChangedObserver());
        cs.addChipChangedObserver(Mocker.mockChangedObserver());
        cs.addChipChangedObserver(Mocker.mockChangedObserver());
        cs.removeAllChipChangedObservers();

        assertNull(cs.changeObservers);
    }

    @Test
    public void cloneObservers() throws Exception {
        final ChipChangedObserver ob1 = Mocker.mockChangedObserver();
        final ChipSelectionObserver ob2 = Mocker.mockSelectionObserver();

        final ObservableChipDataSource cs = new TestDataSource();
        cs.addChipChangedObserver(ob1);
        cs.addChipSelectionObserver(ob2);

        final ObservableChipDataSource cs2 = new TestDataSource();
        cs.cloneObservers(cs2);

        assertTrue(cs.changeObservers.contains(ob1));
        assertTrue(cs2.selectionObservers.contains(ob2));
    }


    /**
     * Empty implementation of {@link ObservableChipDataSource} for testing.
     */
    private static final class TestDataSource extends ObservableChipDataSource {
        @Override
        public List<Chip> getSelectedChips() {
            return null;
        }

        @Override
        public List<Chip> getFilteredChips() {
            return null;
        }

        @Override
        public List<Chip> getOriginalChips() {
            return null;
        }

        @Override
        public Chip getFilteredChip(int position) {
            return null;
        }

        @Override
        public Chip getSelectedChip(int position) {
            return null;
        }

        @Override
        public void addFilteredChip(Chip chip) {

        }

        @Override
        public void addSelectedChip(Chip chip) {

        }

        @Override
        public void setFilterableChips(List<? extends Chip> chips) {

        }

        @Override
        public void takeChip(Chip chip) {

        }

        @Override
        public void takeChip(int position) {

        }

        @Override
        public void replaceChip(Chip chip) {

        }

        @Override
        public void replaceChip(int position) {

        }

        @Override
        public void clearFilteredChips() {

        }

        @Override
        public void clearSelectedChips() {

        }

        @Override
        public boolean existsInFiltered(Chip chip) {
            return false;
        }

        @Override
        public boolean existsInSelected(Chip chip) {
            return false;
        }

        @Override
        public boolean existsInDataSource(Chip chip) {
            return false;
        }
    }
}