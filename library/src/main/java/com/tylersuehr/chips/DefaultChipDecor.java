package com.tylersuehr.chips;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * Subclass of {@link RecyclerView.ItemDecoration} used to add margin
 * to chips on their left and bottom sides.
 *
 * @author Tyler Suehr
 * @version 1.0
 */
class DefaultChipDecor extends RecyclerView.ItemDecoration {
    private final int mMargin;


    DefaultChipDecor(final Context c) {
        mMargin = (int)(8f * c.getResources().getDisplayMetrics().density);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = mMargin;
        outRect.bottom = (mMargin >> 1);
    }
}