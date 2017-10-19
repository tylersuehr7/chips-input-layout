package com.tylersuehr.chips;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import com.tylersuehr.chips.data.Chip;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * This is the view that displays the details of a chip (specified in Material Design Guide).
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public class DetailedChipView extends FrameLayout {
    private TextView mTitleView;
    private TextView mSubtitleView;
    private ImageButton mButtonDelete;

    private ConstraintLayout mContentLayout;
    private CircleImageView mAvatarImageView;

    private ColorStateList backgroundColor;


    public DetailedChipView(@NonNull Context context) {
        this(context, null);
    }

    public DetailedChipView(@NonNull Context c, @Nullable AttributeSet attrs) {
        super(c, attrs);

        // Inflate the view
        View v = inflate(c, R.layout.chip_view_detailed, this);
        this.mContentLayout = v.findViewById(R.id.container);
        this.mAvatarImageView = v.findViewById(R.id.avatar_icon);
        this.mTitleView = v.findViewById(R.id.title);
        this.mSubtitleView = v.findViewById(R.id.subtitle);
        this.mButtonDelete = v.findViewById(R.id.button_delete);

        setVisibility(GONE);
        hideOnTouchOutside();
    }

    /**
     * Fades in the view.
     */
    public void fadeIn() {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(200);
        startAnimation(anim);
        setVisibility(VISIBLE);

        // Force this view to hide when it loses focus
        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    fadeOut();
                    setOnFocusChangeListener(null);
                }
            }
        });

        requestFocus();
    }

    /**
     * Fades out the view.
     */
    public void fadeOut() {
        AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setDuration(200);
        startAnimation(anim);
        setVisibility(GONE);

        // Fix onClick issue
        clearFocus();
        setClickable(false);
    }

    public void setAvatarIcon(Drawable icon) {
        this.mAvatarImageView.setImageDrawable(icon);
    }

    public void setAvatarIcon(Bitmap icon) {
        this.mAvatarImageView.setImageBitmap(icon);
    }

    public void setAvatarIcon(Uri icon) {
        this.mAvatarImageView.setImageURI(icon);
    }

    public void setTitle(String name) {
        this.mTitleView.setText(name);
    }

    /**
     * Sets the subtitle display and shows/hides it accordingly.
     * @param info Info text
     */
    public void setSubtitle(@Nullable String info) {
        if(info != null) {
            this.mSubtitleView.setVisibility(VISIBLE);
            this.mSubtitleView.setText(info);
        } else {
            this.mSubtitleView.setVisibility(GONE);
        }
    }

    public void setTextColor(ColorStateList color) {
        this.mTitleView.setTextColor(color);
        this.mSubtitleView.setTextColor(Utils.alpha(color.getDefaultColor(), 150));
    }

    public void setBackGroundColor(ColorStateList color) {
        this.backgroundColor = color;
        this.mContentLayout.getBackground().setColorFilter(color.getDefaultColor(), PorterDuff.Mode.SRC_ATOP);
    }

    public int getBackgroundColor() {
        return backgroundColor == null ? ContextCompat.getColor(getContext(), R.color.colorAccent) : backgroundColor.getDefaultColor();
    }

    public void setDeleteIconColor(ColorStateList color) {
        this.mButtonDelete.getDrawable().mutate().setColorFilter(color.getDefaultColor(), PorterDuff.Mode.SRC_ATOP);
    }

    public void setOnDeleteClicked(OnClickListener onClickListener) {
        this.mButtonDelete.setOnClickListener(onClickListener);
    }

    public void alignLeft() {
        LayoutParams params = (LayoutParams)mContentLayout.getLayoutParams();
        params.leftMargin = 0;
        this.mContentLayout.setLayoutParams(params);
    }

    public void alignRight() {
        LayoutParams params = (LayoutParams)mContentLayout.getLayoutParams();
        params.rightMargin = 0;
        this.mContentLayout.setLayoutParams(params);
    }

    /**
     * Hide the view on touch outside of it by making it focusable.
     */
    private void hideOnTouchOutside() {
        setFocusable(true);
        setFocusableInTouchMode(true);
        setClickable(true);
    }


    /**
     * Builder for the detailed chip view.
     */
    static class Builder {
        private Context context;
        private Uri avatarUri;
        private Drawable avatarDrawable;
        private String title;
        private String subtitle;
        private ColorStateList textColor;
        private ColorStateList backgroundColor;
        private ColorStateList deleteIconColor;
        private Typeface typeface;


        Builder(Context context) {
            this.context = context;
        }

        Builder avatar(Uri avatarUri) {
            this.avatarUri = avatarUri;
            return this;
        }

        Builder avatar(Drawable avatarDrawable) {
            this.avatarDrawable = avatarDrawable;
            return this;
        }

        Builder title(String title) {
            this.title = title;
            return this;
        }

        Builder subtitle(String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        Builder chip(Chip chip) {
            this.avatarUri = chip.getAvatarUri();
            this.avatarDrawable = chip.getAvatarDrawable();
            this.title = chip.getTitle();
            this.subtitle = chip.getSubtitle();
            return this;
        }

        Builder textColor(ColorStateList textColor) {
            this.textColor = textColor;
            return this;
        }

        Builder backgroundColor(ColorStateList backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        Builder deleteIconColor(ColorStateList deleteIconColor) {
            this.deleteIconColor = deleteIconColor;
            return this;
        }

        Builder typeface(Typeface typeface) {
            this.typeface = typeface;
            return this;
        }

        DetailedChipView build() {
            return DetailedChipView.newInstance(this);
        }
    }

    private static DetailedChipView newInstance(Builder builder) {
        DetailedChipView detailedChipView = new DetailedChipView(builder.context);

        // Set the available avatar image
        if(builder.avatarUri != null) {
            detailedChipView.setAvatarIcon(builder.avatarUri);
        } else if(builder.avatarDrawable != null) {
            detailedChipView.setAvatarIcon(builder.avatarDrawable);
        } else {
            detailedChipView.setAvatarIcon(LetterTileProvider.getInstance(
                    builder.context).getLetterTile(builder.title));
        }

        // Set the background color, if available
        if(builder.backgroundColor != null) {
            detailedChipView.setBackGroundColor(builder.backgroundColor);
        }

        // Set the available text color
        if(builder.textColor != null) {
            detailedChipView.setTextColor(builder.textColor);
        } else if(Utils.isColorDark(detailedChipView.getBackgroundColor())) {
            detailedChipView.setTextColor(ColorStateList.valueOf(Color.WHITE));
        } else {
            detailedChipView.setTextColor(ColorStateList.valueOf(Color.BLACK));
        }

        // Set the available delete icon
        if(builder.deleteIconColor != null) {
            detailedChipView.setDeleteIconColor(builder.deleteIconColor);
        } else if(Utils.isColorDark(detailedChipView.getBackgroundColor())) {
            detailedChipView.setDeleteIconColor(ColorStateList.valueOf(Color.WHITE));
        } else {
            detailedChipView.setDeleteIconColor(ColorStateList.valueOf(Color.BLACK));
        }

        if (builder.typeface != null) {
            detailedChipView.mTitleView.setTypeface(builder.typeface);
            detailedChipView.mSubtitleView.setTypeface(builder.typeface);
        }

        detailedChipView.setTitle(builder.title);
        detailedChipView.setSubtitle(builder.subtitle);
        return detailedChipView;
    }
}