package com.tylersuehr.chips;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tylersuehr.chips.data.Chip;
import com.tylersuehr.chips.data.ChipChangedObserver;
import com.tylersuehr.chips.data.ChipSelectionObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public final class Mocker {
    public static List<TestChip> mockChips(int size) {
        List<TestChip> chips = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            if ((i&1) == 1) {
                chips.add(new TestChip("Chip " + i, "Chippy subtitle!"));
            } else {
                chips.add(new TestChip("Chip " + i));
            }
        }
        return chips;
    }

    public static TestChip mock() {
        return new TestChip("Chip Mock");
    }

    public static ChipChangedObserver mockChangedObserver() {
        return new ChipChangedObserver() {
            @Override
            public void onChipDataSourceChanged() {

            }
        };
    }

    public static ChipSelectionObserver mockSelectionObserver() {
        return new ChipSelectionObserver() {
            @Override
            public void onChipSelected(Chip addedChip) {

            }

            @Override
            public void onChipDeselected(Chip removedChip) {

            }
        };
    }


    /**
     * Simple subclass of {@link Chip} for testing.
     */
    public static final class TestChip extends Chip {
        private final String title;
        private final String subtitle;

        private TestChip(String title) {
            this(title, null);
        }

        private TestChip(String title, String subtitle) {
            this.title = title;
            this.subtitle = subtitle;
        }

        @Nullable
        @Override
        public Object getId() {
            return null;
        }

        @NonNull
        @Override
        public String getTitle() {
            return title;
        }

        @Nullable
        @Override
        public String getSubtitle() {
            return subtitle;
        }

        @Nullable
        @Override
        public Uri getAvatarUri() {
            return null;
        }

        @Nullable
        @Override
        public Drawable getAvatarDrawable() {
            return null;
        }
    }
}