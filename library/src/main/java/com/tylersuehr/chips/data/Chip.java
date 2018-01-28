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
 * This represents a chip (specified in Google Material Design Guide) as an object.
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public abstract class Chip {
    /* Allows to compare text from any language(s) */
    private static Collator sCollator;
    /* Allows to compare Chip objects to each other */
    private static Comparator<Chip> sComparator;
    /* Any king of extra data that might be used */
    private Object mTag;
    /* Specifies if this Chip can be filtered or not */
    private boolean mFilterable;


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
     * Lazy loads a comparator to compare chips to each other.
     * @return {@link Comparator}
     */
    public static Comparator<Chip> getComparator() {
        if (sComparator == null) {
            sComparator = new Comparator<Chip>() {
                @Override
                public int compare(Chip c1, Chip c2) {
                    if (sCollator == null) {
                        sCollator = Collator.getInstance(Locale.getDefault());
                    }
                    return sCollator.compare(c1.getTitle(), c2.getTitle());
                }
            };
        }
        return sComparator;
    }

    public Object getTag() {
        return mTag;
    }

    public void setTag(Object tag) {
        this.mTag = tag;
    }

    public void setFilterable(boolean value) {
        this.mFilterable = value;
    }

    public boolean isFilterable() {
        return mFilterable;
    }
}