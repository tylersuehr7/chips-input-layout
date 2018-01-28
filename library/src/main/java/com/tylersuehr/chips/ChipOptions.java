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
        mTextColorHint = a.getColorStateList(R.styleable.ChipsInputLayout_android_textColorHint);
        mTextColor = a.getColorStateList(R.styleable.ChipsInputLayout_android_textColor);
        mHint = a.getString(R.styleable.ChipsInputLayout_android_hint);

        // Setup the properties for the ChipView
        mChipDeleteIconColor = a.getColorStateList(R.styleable.ChipsInputLayout_chipDeleteIconColor);
        mChipBackgroundColor = a.getColorStateList(R.styleable.ChipsInputLayout_chipBackgroundColor);
        mChipTextColor = a.getColorStateList(R.styleable.ChipsInputLayout_chipTextColor);
        mShowAvatar = a.getBoolean(R.styleable.ChipsInputLayout_chipHasAvatarIcon, true);
        mShowDetails = a.getBoolean(R.styleable.ChipsInputLayout_detailedChipsEnabled, true);
        mShowDelete = a.getBoolean(R.styleable.ChipsInputLayout_chipDeletable, true);
        mChipDeleteIcon = a.getDrawable(R.styleable.ChipsInputLayout_chipDeleteIcon);

        // Setup the properties for the DetailedChipView
        mDetailsChipDeleteIconColor = a.getColorStateList(R.styleable.ChipsInputLayout_detailedChipDeleteIconColor);
        mDetailsChipBackgroundColor = a.getColorStateList(R.styleable.ChipsInputLayout_chipBackgroundColor);
        mDetailsChipTextColor = a.getColorStateList(R.styleable.ChipsInputLayout_detailedChipTextColor);

        // Setup the properties for the FilterableRecyclerView
        mFilterableListBackgroundColor = a.getColorStateList(R.styleable.ChipsInputLayout_filterableListBackgroundColor);
        mFilterableListTextColor = a.getColorStateList(R.styleable.ChipsInputLayout_filterableListTextColor);
        mFilterableListElevation = a.getDimension(R.styleable.ChipsInputLayout_filterableListElevation, Utils.dp(2));

        // Setup the properties for the ChipsInput itself
        mAllowCustomChips = a.getBoolean(R.styleable.ChipsInputLayout_customChipsEnabled, true);
        mMaxRows = a.getInt(R.styleable.ChipsInputLayout_maxRows, 3);

        a.recycle();
    }
}