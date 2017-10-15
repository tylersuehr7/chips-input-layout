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
 * Subclass of {@link Chip} that contains the most minimal information needed to work
 * and is not filterable.
 *
 * Note: although this is publicly accessible, this is intended to be used by
 * this library as a custom chip when a user types a custom chip title into the
 * input only.
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public class NonFilterableChip extends Chip {
    private final String id;
    private final String title;


    public NonFilterableChip(String title) {
        setFilterable(false);
        this.id = UUID.randomUUID().toString();
        this.title = title;
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