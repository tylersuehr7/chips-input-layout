package com.tylersuehr.chipexample;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tylersuehr.chips.ChipImageRenderer;
import com.tylersuehr.chips.data.Chip;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * Example of custom chip image renderer that uses Glide
 * to load images.
 *
 * @author Tyler Suehr
 * @version 1.0
 */
class GlideRenderer implements ChipImageRenderer {
    @Override
    public void renderAvatar(ImageView imageView, Chip chip) {
        Glide.with(imageView.getContext())
                .load(chip.getAvatarUri())
                .into(imageView);
    }
}