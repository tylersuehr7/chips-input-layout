package com.tylersuehr.library.data;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * @author Tyler Suehr
 * @version 1.0
 */
final class DataChipImpl extends Chip {
    private final Object id;
    private final String title;
    private final String subtitle;
    private final Uri uri;
    private final Drawable drawable;


    DataChipImpl(Object id, String title, String subtitle, Object tag) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.uri = null;
        this.drawable = null;
        setTag(tag);
    }

    DataChipImpl(Object id, String title, String subtitle, Uri uri, Drawable drawable, Object tag) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.uri = uri;
        this.drawable = drawable;
        setTag(tag);
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
        return subtitle;
    }

    @Nullable
    @Override
    public Uri getAvatarUri() {
        return uri;
    }

    @Nullable
    @Override
    public Drawable getAvatarDrawable() {
        return drawable;
    }
}