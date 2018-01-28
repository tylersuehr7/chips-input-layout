package com.tylersuehr.chips;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tylersuehr.chips.data.Chip;

import java.util.UUID;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * Subclass of {@link Chip} that's used when the user creates a custom chip.
 *
 * A custom chip can be created whenever the user inputs text into the chip
 * input layout, that doesn't match filterable information, and they press
 * enter on the software keyboard.
 *
 * @author Tyler Suehr
 * @version 1.0
 */
final class DefaultCustomChip extends Chip {
    private String id;
    private String title;


    DefaultCustomChip(String title, boolean filtered) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        setFilterable(filtered);
    }

    DefaultCustomChip(String title) {
        this(title, false);
    }

    @Nullable
    @Override
    public Object getId() {
        return id;
    }

    @NonNull
    @Override
    public String getTitle() {
        return title;
    }

    @Nullable
    @Override
    public String getSubtitle() {
        return null;
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