package com.tylersuehr.chips;

import android.content.Context;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * @author Tyler Suehr
 * @version 1.0
 */
abstract class ChipsFactory {
    static ChipView styledChipView(Context c, ChipOptions options) {
        ChipView chipView = new ChipView(c);
        chipView.setChipOptions(options);
        return chipView;
    }

    static ChipDetailsView styledChipDetailsView(Context c, ChipOptions options) {
        ChipDetailsView chipView = new ChipDetailsView(c);
        chipView.setChipOptions(options);
        return chipView;
    }
}