package com.tylersuehr.library;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.tylersuehr.library.data.Chip;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * @author Tyler Suehr
 * @version 1.0
 */
class BaseChip extends Chip {
    private Object id;
    private Uri avatarUri;
    private Drawable avatarDrawable;
    private String title;
    private String subtitle;


    BaseChip(@NonNull Object id, @Nullable Uri avatarUri, @NonNull String label, @Nullable String subtitle) {
        this.id = id;
        this.avatarUri = avatarUri;
        this.title = label;
        this.subtitle = subtitle;
    }

    BaseChip(@NonNull Object id, @Nullable Drawable avatarDrawable, @NonNull String label, @Nullable String subtitle) {
        this.id = id;
        this.avatarDrawable = avatarDrawable;
        this.title = label;
        this.subtitle = subtitle;
    }

    BaseChip(@Nullable Uri avatarUri, @NonNull String label, @Nullable String subtitle) {
        this.avatarUri = avatarUri;
        this.title = label;
        this.subtitle = subtitle;
    }

    BaseChip(@Nullable Drawable avatarDrawable, @NonNull String label, @Nullable String subtitle) {
        this.avatarDrawable = avatarDrawable;
        this.title = label;
        this.subtitle = subtitle;
    }

    BaseChip(@NonNull Object id, @NonNull String label, @Nullable String subtitle) {
        this.id = id;
        this.title = label;
        this.subtitle = subtitle;
    }

    BaseChip(@NonNull String label, @Nullable String subtitle) {
        this.title = label;
        this.subtitle = subtitle;
    }

    @Override
    public Object getId() {
        return id;
    }

    @Override
    public Uri getAvatarUri() {
        return avatarUri;
    }

    @Override
    public Drawable getAvatarDrawable() {
        return avatarDrawable;
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
}