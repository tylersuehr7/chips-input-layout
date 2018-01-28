package com.tylersuehr.chips;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * Contains all the mutable properties for this library.
 *
 * @author Tyler Suehr
 * @version 1.0
 */
final class ChipOptions {
    /* Properties pertaining to ChipsEditText */
    ColorStateList mTextColorHint;
    ColorStateList mTextColor;
    CharSequence mHint;

    /* Properties pertaining to ChipView */
    Drawable mChipDeleteIcon;
    ColorStateList mChipDeleteIconColor;
    ColorStateList mChipBackgroundColor;
    ColorStateList mChipTextColor;
    boolean mShowAvatar;
    boolean mShowDetails;
    boolean mShowDelete;

    /* Properties pertaining to ChipDetailsView */
    ColorStateList mDetailsChipDeleteIconColor;
    ColorStateList mDetailsChipBackgroundColor;
    ColorStateList mDetailsChipTextColor;

    /* Properties pertaining to FilterableRecyclerView */
    ColorStateList mFilterableListBackgroundColor;
    ColorStateList mFilterableListTextColor;
    float mFilterableListElevation;

    /* Properties pertaining to the ChipsInputLayout itself */
    Typeface mTypeface = Typeface.DEFAULT;
    boolean mAllowCustomChips;
    int mMaxRows;


    ChipOptions(Context c, AttributeSet attrs) {
        TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.ChipsInputLayout);

        // Setup the properties for the ChipEditText
        this.mTextColorHint = a.getColorStateList(R.styleable.ChipsInputLayout_android_textColorHint);
        this.mTextColor = a.getColorStateList(R.styleable.ChipsInputLayout_android_textColor);
        this.mHint = a.getString(R.styleable.ChipsInputLayout_android_hint);

        // Setup the properties for the ChipView
        this.mChipDeleteIconColor = a.getColorStateList(R.styleable.ChipsInputLayout_chipDeleteIconColor);
        this.mChipBackgroundColor = a.getColorStateList(R.styleable.ChipsInputLayout_chipBackgroundColor);
        this.mChipTextColor = a.getColorStateList(R.styleable.ChipsInputLayout_chipTextColor);
        this.mShowAvatar = a.getBoolean(R.styleable.ChipsInputLayout_chipHasAvatarIcon, true);
        this.mShowDetails = a.getBoolean(R.styleable.ChipsInputLayout_detailedChipsEnabled, true);
        this.mShowDelete = a.getBoolean(R.styleable.ChipsInputLayout_chipDeletable, true);
        this.mChipDeleteIcon = a.getDrawable(R.styleable.ChipsInputLayout_chipDeleteIcon);

        // Setup the properties for the DetailedChipView
        this.mDetailsChipDeleteIconColor = a.getColorStateList(R.styleable.ChipsInputLayout_detailedChipDeleteIconColor);
        this.mDetailsChipBackgroundColor = a.getColorStateList(R.styleable.ChipsInputLayout_chipBackgroundColor);
        this.mDetailsChipTextColor = a.getColorStateList(R.styleable.ChipsInputLayout_detailedChipTextColor);

        // Setup the properties for the FilterableRecyclerView
        this.mFilterableListBackgroundColor = a.getColorStateList(R.styleable.ChipsInputLayout_filterableListBackgroundColor);
        this.mFilterableListTextColor = a.getColorStateList(R.styleable.ChipsInputLayout_filterableListTextColor);
        this.mFilterableListElevation = a.getDimension(R.styleable.ChipsInputLayout_filterableListElevation, Utils.dp(2));

        // Setup the properties for the ChipsInput itself
        this.mAllowCustomChips = a.getBoolean(R.styleable.ChipsInputLayout_customChipsEnabled, true);
        this.mMaxRows = a.getInt(R.styleable.ChipsInputLayout_maxRows, 3);

        a.recycle();
    }
}