package com.tylersuehr.chips;

import android.widget.ImageView;

import com.tylersuehr.chips.data.Chip;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * Implementation of {@link ChipImageRenderer} that affords the default
 * way of rendering avatar images.
 *
 * The default renderer does this:
 * (1) Try to load the avatar uri, or
 * (2) Try to load the avatar drawable, or
 * (3) Load a circular tile with a letter.
 *
 * @author Tyler Suehr
 * @version 1.0
 */
class DefaultImageRenderer implements ChipImageRenderer {
    @Override
    public void renderAvatar(ImageView imageView, Chip chip) {
        if (chip.getAvatarUri() != null) {
            imageView.setImageURI(chip.getAvatarUri());
        } else if (chip.getAvatarDrawable() != null) {
            imageView.setImageDrawable(chip.getAvatarDrawable());
        } else {
            imageView.setImageBitmap(LetterTileProvider
                    .getInstance(imageView.getContext())
                    .getLetterTile(chip.getTitle()));
        }
    }
}