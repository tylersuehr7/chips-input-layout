package com.tylersuehr.library;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import com.tylersuehr.library.data.Chip;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public class DetailedChipView extends FrameLayout {
    private static LetterTileProvider mLetterTileProvider;

    private ConstraintLayout mContentLayout;
    private CircleImageView mAvatarIconImageView;
    private TextView mNameTextView;
    private TextView mInfoTextView;
    private ImageButton mDeleteButton;

    private ColorStateList mBackgroundColor;


    public DetailedChipView(@NonNull Context context) {
        this(context, null);
    }

    public DetailedChipView(@NonNull Context c, @Nullable AttributeSet attrs) {
        super(c, attrs);

        // Setup the tile provider
        mLetterTileProvider = getTileProvider(c);

        // Inflate the view
        View v = inflate(c, R.layout.chip_view_detailed, this);
        this.mContentLayout = v.findViewById(R.id.container);
        this.mAvatarIconImageView = v.findViewById(R.id.avatar_icon);
        this.mNameTextView = v.findViewById(R.id.name);
        this.mInfoTextView = v.findViewById(R.id.info);
        this.mDeleteButton = v.findViewById(R.id.delete_button);

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
        this.mAvatarIconImageView.setImageDrawable(icon);
    }

    public void setAvatarIcon(Bitmap icon) {
        this.mAvatarIconImageView.setImageBitmap(icon);
    }

    public void setAvatarIcon(Uri icon) {
        this.mAvatarIconImageView.setImageURI(icon);
    }

    public void setName(String name) {
        this.mNameTextView.setText(name);
    }

    /**
     * Sets the info display and shows/hides it accordingly.
     * @param info Info text
     */
    public void setInfo(@Nullable String info) {
        if(info != null) {
            this.mInfoTextView.setVisibility(VISIBLE);
            this.mInfoTextView.setText(info);
        } else {
            this.mInfoTextView.setVisibility(GONE);
        }
    }

    public void setTextColor(ColorStateList color) {
        this.mNameTextView.setTextColor(color);
        this.mInfoTextView.setTextColor(Utils.alpha(color.getDefaultColor(), 150));
    }

    public void setBackGroundColor(ColorStateList color) {
        this.mBackgroundColor = color;
        this.mContentLayout.getBackground().setColorFilter(color.getDefaultColor(), PorterDuff.Mode.SRC_ATOP);
    }

    public int getBackgroundColor() {
        return mBackgroundColor == null ? ContextCompat.getColor(getContext(), R.color.colorAccent) : mBackgroundColor.getDefaultColor();
    }

    public void setDeleteIconColor(ColorStateList color) {
        this.mDeleteButton.getDrawable().mutate().setColorFilter(color.getDefaultColor(), PorterDuff.Mode.SRC_ATOP);
    }

    public void setOnDeleteClicked(OnClickListener onClickListener) {
        this.mDeleteButton.setOnClickListener(onClickListener);
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

    private static LetterTileProvider getTileProvider(Context c) {
        if (mLetterTileProvider == null) {
            mLetterTileProvider = new LetterTileProvider(c);
        }
        return mLetterTileProvider;
    }


    /**
     * Builder for the detailed chip view.
     */
    static class Builder {
        private Context context;
        private Uri avatarUri;
        private Drawable avatarDrawable;
        private String name;
        private String info;
        private ColorStateList textColor;
        private ColorStateList backgroundColor;
        private ColorStateList deleteIconColor;


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

        Builder name(String name) {
            this.name = name;
            return this;
        }

        Builder info(String info) {
            this.info = info;
            return this;
        }

        Builder chip(Chip chip) {
            this.avatarUri = chip.getAvatarUri();
            this.avatarDrawable = chip.getAvatarDrawable();
            this.name = chip.getTitle();
            this.info = chip.getSubtitle();
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
            detailedChipView.setAvatarIcon(mLetterTileProvider.getLetterTile(builder.name));
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

        detailedChipView.setName(builder.name);
        detailedChipView.setInfo(builder.info);
        return detailedChipView;
    }
}