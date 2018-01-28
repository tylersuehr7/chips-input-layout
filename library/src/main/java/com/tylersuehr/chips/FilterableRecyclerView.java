package com.tylersuehr.chips;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Filter;
import android.widget.Filterable;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * Subclass of {@link RecyclerView} to enable calling to {@link Filter} object
 * to filter the appropriate data source, and then receive a callback once the
 * filtering is completed.
 *
 * Callbacks for filtering events are necessary because they are used to show/hide
 * this view respectively.
 *
 * @author Tyler Suehr
 * @version 1.0
 */
class FilterableRecyclerView extends RecyclerView implements ChipComponent {
    /* Used to find its location in window */
    private ChipsInputLayout mChipsInput;
    /* Used to trigger filtering and receive callbacks to show or hide this */
    private Filter mFilter;


    FilterableRecyclerView(Context c) {
        super(c);
        setBackgroundColor(Color.WHITE);
        setLayoutManager(new LinearLayoutManager(c));
        setVisibility(GONE);
    }

    @Override
    public void setChipOptions(ChipOptions options) {
        ViewCompat.setElevation(this, options.mFilterableListElevation);
        if (options.mFilterableListBackgroundColor != null) {
            getBackground().setColorFilter(options.mFilterableListBackgroundColor
                    .getDefaultColor(), PorterDuff.Mode.SRC_ATOP);
        }
    }

    <T extends RecyclerView.Adapter & Filterable>
    void setup(T adapter, ChipsInputLayout chipsInputLayout) {
        setAdapter(adapter);
        mFilter = adapter.getFilter();
        mChipsInput = chipsInputLayout;
    }

    /**
     * Applies the given filter pattern to the filter. If the filter yields no results,
     * then we hide this filterable recycler, or show it otherwise.
     *
     * @param filter Filter pattern
     */
    void filterChips(CharSequence filter) {
        if (filter != null) {
            mFilter.filter(filter, new Filter.FilterListener() {
                @Override
                public void onFilterComplete(int count) {
                    // Show if, and only if, there are results
                    if (count > 0) {
                        fadeIn();
                    } else {
                        fadeOut();
                    }
                }
            });
        }
    }

    /**
     * Uses alpha animation to fade in the current view if it's not visible.
     */
    void fadeIn() {
        if (getVisibility() == VISIBLE) { return; }

        // Get visible window (keyboard shown)
        Rect r = new Rect();
        final View rootView = getRootView();
        rootView.getWindowVisibleDisplayFrame(r);

        int[] coord = new int[2];
        mChipsInput.getLocationInWindow(coord);

        ViewGroup.MarginLayoutParams lp = (MarginLayoutParams)getLayoutParams();
        lp.topMargin = coord[1] + mChipsInput.getHeight();

        // Height of the keyboard
        lp.bottomMargin = rootView.getHeight() - r.bottom;
        setLayoutParams(lp);

        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(200);
        startAnimation(anim);

        setVisibility(VISIBLE);
    }

    /**
     * Uses alpha animation to fade out the current view if it's not gone.
     */
    void fadeOut() {
        if (getVisibility() == GONE) { return; }

        AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setDuration(200);
        startAnimation(anim);

        setVisibility(GONE);
    }
}