package com.tylersuehr.library;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * Contains all the mutable properties for our {@link ChipsInput}.
 *
 * @author Tyler Suehr
 * @version 1.0
 */
final class ChipOptions {
    /* Properties for the chips EditText */
    ColorStateList textColorHint;
    ColorStateList textColor;
    CharSequence hint;

    /* Properties for the ChipView */
    ColorStateList chipDeleteIconColor;
    ColorStateList chipBackgroundColor;
    ColorStateList chipLabelColor;
    boolean hasAvatarIcon;
    boolean showDetailedChip = true;
    boolean chipDeletable = true;
    Drawable chipDeleteIcon;

    /* Properties for the DetailedChipView */
    ColorStateList detailedChipDeleteIconColor;
    ColorStateList detailedChipBackgroundColor;
    ColorStateList detailedChipTextColor;

    /* Properties for the FilterableListView */
    ColorStateList filterableListBackgroundColor;
    ColorStateList filterableListTextColor;

    /* Properties for the ChipsInput itself */
    int maxRows;


    ChipOptions(Context c, AttributeSet attrs) {
        TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.ChipsInput);

        // Setup the properties for the Chips EditText
        this.textColorHint = a.getColorStateList(R.styleable.ChipsInput_android_textColorHint);
        this.textColor = a.getColorStateList(R.styleable.ChipsInput_android_textColor);
        this.hint = a.getString(R.styleable.ChipsInput_android_hint);

        // Setup the properties for the ChipView
        this.chipDeleteIconColor = a.getColorStateList(R.styleable.ChipsInput_chip_deleteIconColor);
        this.chipBackgroundColor = a.getColorStateList(R.styleable.ChipsInput_chip_backgroundColor);
        this.chipLabelColor = a.getColorStateList(R.styleable.ChipsInput_chip_labelColor);
        this.hasAvatarIcon = a.getBoolean(R.styleable.ChipsInput_chip_hasAvatarIcon, true);
        this.showDetailedChip = a.getBoolean(R.styleable.ChipsInput_showChipDetailed, true);
        this.chipDeletable = a.getBoolean(R.styleable.ChipsInput_chip_deletable, true);
        this.chipDeleteIcon = a.getDrawable(R.styleable.ChipsInput_chip_deleteIcon);

        // Setup the properties for the DetailedChipView
        this.detailedChipDeleteIconColor = a.getColorStateList(R.styleable.ChipsInput_chip_detailed_deleteIconColor);
        this.detailedChipBackgroundColor = a.getColorStateList(R.styleable.ChipsInput_chip_backgroundColor);
        this.detailedChipTextColor = a.getColorStateList(R.styleable.ChipsInput_chip_detailed_textColor);

        // Setup the properties for the FilterableListView
        this.filterableListBackgroundColor = a.getColorStateList(R.styleable.ChipsInput_filterable_list_backgroundColor);
        this.filterableListTextColor = a.getColorStateList(R.styleable.ChipsInput_filterable_list_textColor);

        // Setup the properties for the ChipsInput itself
        this.maxRows = a.getInt(R.styleable.ChipsInput_maxRows, 3);

        a.recycle();
    }
}