package com.tylersuehr.chipexample;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.tylersuehr.chips.data.Chip;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public class ContactChip extends Chip {
    private int id;
    private String name;
    private String phone;
    private String email;
    private String phoneType;

    private Uri avatarUri;
    private Drawable avatarDr;


    public ContactChip() {}

    @Nullable
    @Override
    public Integer getId() {
        return id;
    }

    @NonNull
    @Override
    public String getTitle() {
        return name;
    }

    @Nullable
    @Override
    public String getSubtitle() {
        if (phoneType != null && phone != null) {
            return phoneType + ": " + phone;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri getAvatarUri() {
        return avatarUri;
    }

    @Nullable
    @Override
    public Drawable getAvatarDrawable() {
        return avatarDr;
    }

    public void setAvatarUri(Uri uri) {
        this.avatarUri = uri;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}