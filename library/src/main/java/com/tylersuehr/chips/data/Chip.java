package com.tylersuehr.chips.data;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * This is an abstraction that represents the most basic chip.
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public abstract class Chip {
    /* Allows us to compare text from any language(s) */
    private static Collator collator;

    /* Allows us to compare Chip objects to each other */
    private static Comparator<Chip> comparator;

    /* Any king of extra data we might want to use */
    private Object tag;

    /* Specifies if this Chip can be filtered or not */
    private boolean filterable;


    @Nullable
    public abstract Object getId();

    @NonNull
    public abstract String getTitle();

    @Nullable
    public abstract String getSubtitle();

    @Nullable
    public abstract Uri getAvatarUri();

    @Nullable
    public abstract Drawable getAvatarDrawable();

    /**
     * Lazy loads a static comparator to compare chips.
     * @return {@link Comparator}
     */
    public static Comparator<Chip> getComparator() {
        if (comparator == null) {
            comparator = new Comparator<Chip>() {
                @Override
                public int compare(Chip c1, Chip c2) {
                    if (collator == null) {
                        collator = Collator.getInstance(Locale.getDefault());
                    }
                    return collator.compare(c1.getTitle(), c2.getTitle());
                }
            };
        }
        return comparator;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public void setFilterable(boolean value) {
        this.filterable = value;
    }

    public boolean isFilterable() {
        return filterable;
    }
}