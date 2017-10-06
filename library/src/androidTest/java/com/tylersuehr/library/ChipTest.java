package com.tylersuehr.library;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tylersuehr.library.data.Chip;

import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public class ChipTest {
    @Test
    public void getId() throws Exception {
        final String result = UUID.randomUUID().toString();
        final Chip chip = new TestChipImpl(result, null, null, null, null, null);
        assertEquals(chip.getId(), result);
    }

    @Test
    public void getTitle() throws Exception {
        final String result = "My name is Jeff";
        final Chip chip = new TestChipImpl(null, result, null, null, null, null);
        assertEquals(chip.getTitle(), result);
    }

    @Test
    public void getSubtitle() throws Exception {
        final String result = "Hello there!";
        final Chip chip = new TestChipImpl(null, null, result, null, null, null);
        assertEquals(chip.getSubtitle(), result);
    }

    @Test
    public void getAvatarUri() throws Exception {
        final Uri uri = Uri.parse("http://www.google.com");
        final Chip chip = new TestChipImpl(null, null, null, uri, null, null);
        assertEquals(chip.getAvatarUri(), uri);
    }

    @Test
    public void getAvatarDrawable() throws Exception {
        final Drawable dr = new ColorDrawable(Color.BLACK);
        final Chip chip = new TestChipImpl(null, null, null, null, dr, null);
        assertEquals(chip.getAvatarDrawable(), dr);
    }

    @Test
    public void getTag() throws Exception {
        final List result = Collections.EMPTY_LIST;
        final Chip chip = new TestChipImpl(null, null, null, null, null, result);
        assertEquals(chip.getTag(), result);
    }



    private static final class TestChipImpl extends Chip {
        private final Object id;
        private final String title;
        private final String subtitle;
        private final Uri uri;
        private final Drawable drawable;


        private TestChipImpl(Object id, String title, String subtitle, Uri uri, Drawable drawable, Object tag) {
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
}