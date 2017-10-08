package com.tylersuehr.library;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import com.tylersuehr.library.data.Chip;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * This is the view that is used for any kind of Chip that we will use.
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public class ChipView extends FrameLayout {
    private CircleImageView mAvatarImageView;
    private ImageButton mButtonDelete;
    private TextView mTitleView;

    private ColorStateList mLabelColor;
    private String mLabel;

    private boolean mHasAvatarIcon = false;
    private Drawable mAvatarIconDrawable;
    private Uri mAvatarIconUri;

    private boolean mDeletable = false;
    private Drawable mDeleteIcon;

    private ColorStateList mDeleteIconColor;
    private ColorStateList mBackgroundColor;

    private LetterTileProvider mLetterTileProvider;
    private Chip mChip;


    public ChipView(@NonNull Context context) {
        this(context, null);
    }

    public ChipView(@NonNull Context c, @Nullable AttributeSet attrs) {
        super(c, attrs);

        // Inflate the layout
        inflate(c, R.layout.chip_view, this);
        this.mAvatarImageView = findViewById(R.id.icon);
        this.mTitleView = findViewById(R.id.label);
        this.mButtonDelete = findViewById(R.id.delete_button);

        this.mLetterTileProvider = new LetterTileProvider(c);
    }

    /**
     * Sets properties from a chip.
     * @param chip {@link Chip}
     */
    public void inflateWithChip(Chip chip) {
        this.mChip = chip;
        this.mLabel = mChip.getTitle();
        this.mAvatarIconUri = mChip.getAvatarUri();
        this.mAvatarIconDrawable = mChip.getAvatarDrawable();
        inflateWithAttributes();
    }

    /**
     * Sets properties from the set XML attributes.
     */
    private void inflateWithAttributes() {
        setLabel(mLabel);
        if (mLabelColor != null) {
            setLabelColor(mLabelColor);
        }

        setHasAvatarIcon(mHasAvatarIcon);
        setDeletable(mDeletable);

        if (mBackgroundColor != null) {
            setChipBackgroundColor(mBackgroundColor);
        }
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        this.mLabel = label;
        this.mTitleView.setText(label);
    }

    public void setLabelColor(ColorStateList color) {
        this.mLabelColor = color;
        this.mTitleView.setTextColor(color);
    }

    public void setLabelColor(@ColorInt int color) {
        this.mLabelColor = ColorStateList.valueOf(color);
        this.mTitleView.setTextColor(color);
    }

    /**
     * Show or hide the avatar icon.
     * @param hasAvatarIcon True if avatar icon should be shown
     */
    public void setHasAvatarIcon(boolean hasAvatarIcon) {
        this.mHasAvatarIcon = hasAvatarIcon;
        if (hasAvatarIcon) { // Show the avatar icon
            this.mAvatarImageView.setVisibility(VISIBLE);

            // Adjust label's padding
            if (mButtonDelete.getVisibility() == VISIBLE) {
                this.mTitleView.setPadding(Utils.dp(8), 0, 0, 0);
            } else {
                this.mTitleView.setPadding(Utils.dp(8), 0, Utils.dp(12), 0);
            }

            // Set the avatar icon
            if (mAvatarIconUri != null) { // Use the URI
                this.mAvatarImageView.setImageURI(mAvatarIconUri);
            } else if (mAvatarIconDrawable != null) { // Use the Drawable
                this.mAvatarImageView.setImageDrawable(mAvatarIconDrawable);
            } else { // Use the tile provider
                this.mAvatarImageView.setImageBitmap(mLetterTileProvider.getLetterTile(getLabel()));
            }
        } else { // Hide the avatar icon
            this.mAvatarImageView.setVisibility(GONE);

            // Adjust label's padding
            if(mButtonDelete.getVisibility() == VISIBLE) {
                this.mTitleView.setPadding(Utils.dp(12), 0, 0, 0);
            } else {
                this.mTitleView.setPadding(Utils.dp(12), 0, Utils.dp(12), 0);
            }
        }
    }

    public void setAvatarIcon(Drawable avatarIcon) {
        this.mAvatarIconDrawable = avatarIcon;
        this.mHasAvatarIcon = true;
        inflateWithAttributes();
    }

    public void setAvatarIcon(Uri avatarUri) {
        this.mAvatarIconUri = avatarUri;
        this.mHasAvatarIcon = true;
        inflateWithAttributes();
    }

    /**
     * Show or hide the delete button.
     * @param deletable True if delete button should be shown
     */
    public void setDeletable(boolean deletable) {
        this.mDeletable = deletable;
        if (deletable) { // Show the delete button
            this.mButtonDelete.setVisibility(VISIBLE);

            // Adjust label's padding
            if (mAvatarImageView.getVisibility() == VISIBLE) {
                this.mTitleView.setPadding(Utils.dp(8), 0, 0, 0);
            } else {
                this.mTitleView.setPadding(Utils.dp(12), 0, 0, 0);
            }

            // Set the delete icon
            if (mDeleteIcon != null) {
                this.mButtonDelete.setImageDrawable(mDeleteIcon);
            }
            if (mDeleteIconColor != null) {
                this.mButtonDelete.getDrawable().mutate().setColorFilter(mDeleteIconColor.getDefaultColor(), PorterDuff.Mode.SRC_ATOP);
            }
        } else { // Hide the delete button
            this.mButtonDelete.setVisibility(GONE);

            // Adjust label's padding
            if (mAvatarImageView.getVisibility() == VISIBLE) {
                this.mTitleView.setPadding(Utils.dp(8), 0, Utils.dp(12), 0);
            } else {
                this.mTitleView.setPadding(Utils.dp(12), 0, Utils.dp(12), 0);
            }
        }
    }

    public void setDeleteIconColor(ColorStateList color) {
        this.mDeleteIconColor = color;
        this.mDeletable = true;
        inflateWithAttributes();
    }

    public void setDeleteIconColor(@ColorInt int color) {
        this.mDeleteIconColor = ColorStateList.valueOf(color);
        this.mDeletable = true;
        inflateWithAttributes();
    }

    public void setDeleteIcon(Drawable deleteIcon) {
        this.mDeleteIcon = deleteIcon;
        this.mDeletable = true;
        inflateWithAttributes();
    }

    public void setChipBackgroundColor(ColorStateList color) {
        this.mBackgroundColor = color;
        setChipBackgroundColor(color.getDefaultColor());
    }

    public void setChipBackgroundColor(@ColorInt int color) {
        this.mBackgroundColor = ColorStateList.valueOf(color);
        getChildAt(0).getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }

    /**
     * Set the chip object.
     * @param chip the chip
     */
    public void setChip(Chip chip) {
        this.mChip = chip;
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
        private String label;
        private ColorStateList labelColor;
        private Uri avatarIconUri;
        private Drawable avatarIconDrawable;
        private boolean hasAvatarIcon = false;
        private boolean deletable = false;
        private Drawable deleteIcon;
        private ColorStateList deleteIconColor;
        private ColorStateList backgroundColor;
        private Chip chip;


        Builder(Context context) {
            this.context = context;
        }

        Builder label(String label) {
            this.label = label;
            return this;
        }

        Builder labelColor(ColorStateList labelColor) {
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
            this.label = chip.getTitle();
            this.avatarIconDrawable = chip.getAvatarDrawable();
            this.avatarIconUri = chip.getAvatarUri();
            return this;
        }

        ChipView build() {
            return newInstance(this);
        }
    }

    private static ChipView newInstance(Builder builder) {
        ChipView chipView = new ChipView(builder.context);
        chipView.mLabel = builder.label;
        chipView.mLabelColor = builder.labelColor;
        chipView.mHasAvatarIcon = builder.hasAvatarIcon;
        chipView.mAvatarIconUri = builder.avatarIconUri;
        chipView.mAvatarIconDrawable = builder.avatarIconDrawable;
        chipView.mDeletable = builder.deletable;
        chipView.mDeleteIcon = builder.deleteIcon;
        chipView.mDeleteIconColor = builder.deleteIconColor;
        chipView.mBackgroundColor = builder.backgroundColor;
        chipView.mChip = builder.chip;
        chipView.inflateWithAttributes();
        return chipView;
    }
}