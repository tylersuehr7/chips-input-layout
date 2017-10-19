package com.tylersuehr.chips;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import com.tylersuehr.chips.data.Chip;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * This is the view that displays the normal chip (specified in Material Design Guide).
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public class ChipView extends FrameLayout {
    private Chip chip;

    private CircleImageView mAvatarImageView;
    private ImageButton mButtonDelete;
    private TextView mTitleView;

    private ColorStateList titleTextColor;
    private String title;

    private boolean hasAvatarIcon = false;
    private Drawable avatarDrawable;
    private Uri avatarUri;

    private boolean deletable = false;
    private Drawable deleteIcon;
    private ColorStateList deleteIconColor;
    private ColorStateList backgroundColor;


    public ChipView(@NonNull Context context) {
        this(context, null);
    }

    public ChipView(@NonNull Context c, @Nullable AttributeSet attrs) {
        super(c, attrs);

        // Inflate the layout
        inflate(c, R.layout.chip_view, this);
        this.mAvatarImageView = findViewById(R.id.icon);
        this.mTitleView = findViewById(R.id.label);
        this.mButtonDelete = findViewById(R.id.button_delete);
    }

    /**
     * Sets properties from a chip.
     * @param chip {@link Chip}
     */
    public void inflateWithChip(Chip chip) {
        this.chip = chip;
        this.title = this.chip.getTitle();
        this.avatarUri = this.chip.getAvatarUri();
        this.avatarDrawable = this.chip.getAvatarDrawable();
        inflateFromFields();
    }

    /**
     * Updates the views from the stored field data.
     */
    private void inflateFromFields() {
        setTitle(title);
        if (titleTextColor != null) {
            setTitleTextColor(titleTextColor);
        }

        setHasAvatarIcon(hasAvatarIcon);
        setDeletable(deletable);

        if (backgroundColor != null) {
            setChipBackgroundColor(backgroundColor);
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String label) {
        this.title = label;
        this.mTitleView.setText(label);
    }

    public void setTitleTextColor(ColorStateList color) {
        this.titleTextColor = color;
        this.mTitleView.setTextColor(color);
    }

    public void setTitleTextColor(@ColorInt int color) {
        this.titleTextColor = ColorStateList.valueOf(color);
        this.mTitleView.setTextColor(color);
    }

    /**
     * Show or hide the avatar icon.
     * @param hasAvatarIcon True if avatar icon should be shown
     */
    public void setHasAvatarIcon(boolean hasAvatarIcon) {
        this.hasAvatarIcon = hasAvatarIcon;
        if (hasAvatarIcon) { // Show the avatar icon
            this.mAvatarImageView.setVisibility(VISIBLE);

            // Adjust title's padding
            if (mButtonDelete.getVisibility() == VISIBLE) {
                this.mTitleView.setPadding(Utils.dp(8), 0, 0, 0);
            } else {
                this.mTitleView.setPadding(Utils.dp(8), 0, Utils.dp(12), 0);
            }

            // Set the avatar icon
            if (avatarUri != null) { // Use the URI
                this.mAvatarImageView.setImageURI(avatarUri);
            } else if (avatarDrawable != null) { // Use the Drawable
                this.mAvatarImageView.setImageDrawable(avatarDrawable);
            } else { // Use the tile provider
                this.mAvatarImageView.setImageBitmap(LetterTileProvider.getInstance(
                        getContext()).getLetterTile(getTitle()));
            }
        } else { // Hide the avatar icon
            this.mAvatarImageView.setVisibility(GONE);

            // Adjust title's padding
            if(mButtonDelete.getVisibility() == VISIBLE) {
                this.mTitleView.setPadding(Utils.dp(12), 0, 0, 0);
            } else {
                this.mTitleView.setPadding(Utils.dp(12), 0, Utils.dp(12), 0);
            }
        }
    }

    public void setAvatarIcon(Drawable avatarIcon) {
        this.avatarDrawable = avatarIcon;
        this.hasAvatarIcon = true;
        inflateFromFields();
    }

    public void setAvatarIcon(Uri avatarUri) {
        this.avatarUri = avatarUri;
        this.hasAvatarIcon = true;
        inflateFromFields();
    }

    /**
     * Show or hide the delete button.
     * @param deletable True if delete button should be shown
     */
    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
        if (deletable) { // Show the delete button
            this.mButtonDelete.setVisibility(VISIBLE);

            // Adjust title's padding
            if (mAvatarImageView.getVisibility() == VISIBLE) {
                this.mTitleView.setPadding(Utils.dp(8), 0, 0, 0);
            } else {
                this.mTitleView.setPadding(Utils.dp(12), 0, 0, 0);
            }

            // Set the delete icon
            if (deleteIcon != null) {
                this.mButtonDelete.setImageDrawable(deleteIcon);
            }
            if (deleteIconColor != null) {
                this.mButtonDelete.getDrawable().mutate().setColorFilter(deleteIconColor.getDefaultColor(), PorterDuff.Mode.SRC_ATOP);
            }
        } else { // Hide the delete button
            this.mButtonDelete.setVisibility(GONE);

            // Adjust title's padding
            if (mAvatarImageView.getVisibility() == VISIBLE) {
                this.mTitleView.setPadding(Utils.dp(8), 0, Utils.dp(12), 0);
            } else {
                this.mTitleView.setPadding(Utils.dp(12), 0, Utils.dp(12), 0);
            }
        }
    }

    public void setDeleteIconColor(ColorStateList color) {
        this.deleteIconColor = color;
        this.deletable = true;
        inflateFromFields();
    }

    public void setDeleteIconColor(@ColorInt int color) {
        this.deleteIconColor = ColorStateList.valueOf(color);
        this.deletable = true;
        inflateFromFields();
    }

    public void setDeleteIcon(Drawable deleteIcon) {
        this.deleteIcon = deleteIcon;
        this.deletable = true;
        inflateFromFields();
    }

    public void setChipBackgroundColor(ColorStateList color) {
        this.backgroundColor = color;
        setChipBackgroundColor(color.getDefaultColor());
    }

    public void setChipBackgroundColor(@ColorInt int color) {
        this.backgroundColor = ColorStateList.valueOf(color);
        getChildAt(0).getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }

    /**
     * Set the chip object.
     * @param chip the chip
     */
    public void setChip(Chip chip) {
        this.chip = chip;
        inflateWithChip(chip);
    }

    /**
     * Sets an OnClickListener on the ChipView itself.
     * @param onClickListener {@link OnClickListener}
     */
    public void setOnChipClicked(OnClickListener onClickListener) {
        getChildAt(0).setOnClickListener(onClickListener);
    }

    /**
     * Sets an OnClickListener on the delete button.
     * @param onClickListener {@link OnClickListener}
     */
    public void setOnDeleteClicked(OnClickListener onClickListener) {
        this.mButtonDelete.setOnClickListener(onClickListener);
    }

    /**
     * Builder class
     */
    static class Builder {
        private Context context;
        private String title;
        private ColorStateList labelColor;
        private Uri avatarIconUri;
        private Drawable avatarIconDrawable;
        private boolean hasAvatarIcon = false;
        private boolean deletable = false;
        private Drawable deleteIcon;
        private ColorStateList deleteIconColor;
        private ColorStateList backgroundColor;
        private Typeface typeface;
        private Chip chip;


        Builder(Context context) {
            this.context = context;
        }

        Builder title(String title) {
            this.title = title;
            return this;
        }

        Builder titleTextColor(ColorStateList labelColor) {
            this.labelColor = labelColor;
            return this;
        }

        Builder hasAvatarIcon(boolean hasAvatarIcon) {
            this.hasAvatarIcon = hasAvatarIcon;
            return this;
        }

        Builder avatarIcon(Uri avatarUri) {
            this.avatarIconUri = avatarUri;
            return this;
        }

        Builder avatarIcon(Drawable avatarIcon) {
            this.avatarIconDrawable = avatarIcon;
            return this;
        }

        Builder deletable(boolean deletable) {
            this.deletable = deletable;
            return this;
        }

        Builder deleteIcon(Drawable deleteIcon) {
            this.deleteIcon = deleteIcon;
            return this;
        }

        Builder deleteIconColor(ColorStateList deleteIconColor) {
            this.deleteIconColor = deleteIconColor;
            return this;
        }

        Builder backgroundColor(ColorStateList backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        Builder chip(Chip chip) {
            this.chip = chip;
            this.title = chip.getTitle();
            this.avatarIconDrawable = chip.getAvatarDrawable();
            this.avatarIconUri = chip.getAvatarUri();
            return this;
        }

        Builder typeface(Typeface typeface) {
            this.typeface = typeface;
            return this;
        }

        ChipView build() {
            return newInstance(this);
        }
    }

    private static ChipView newInstance(Builder builder) {
        ChipView chipView = new ChipView(builder.context);
        chipView.title = builder.title;
        chipView.titleTextColor = builder.labelColor;
        chipView.hasAvatarIcon = builder.hasAvatarIcon;
        chipView.avatarUri = builder.avatarIconUri;
        chipView.avatarDrawable = builder.avatarIconDrawable;
        chipView.deletable = builder.deletable;
        chipView.deleteIcon = builder.deleteIcon;
        chipView.deleteIconColor = builder.deleteIconColor;
        chipView.backgroundColor = builder.backgroundColor;
        chipView.chip = builder.chip;
        if (builder.typeface != null) {
            chipView.mTitleView.setTypeface(builder.typeface);
        }
        chipView.inflateFromFields();
        return chipView;
    }
}