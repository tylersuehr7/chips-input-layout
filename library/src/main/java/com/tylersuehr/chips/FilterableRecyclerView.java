package com.tylersuehr.chips;
import android.content.Context;
import android.graphics.Rect;
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
 * This subclass of {@link RecyclerView} will allow us to call to the {@link Filter} to
 * filter the appropriate data source, and then receive a callback once the filtering is
 * complete.
 *
 * We want to callbacks for filtering events because we can handle showing/hiding this
 * view respectively.
 *
 * @author Tyler Suehr
 * @version 1.0
 */
class FilterableRecyclerView extends RecyclerView {
    /* Used to determine location in window */
    private ChipsInputLayout chipsInput;

    /* Used to trigger filtering and receive callbacks to show or hide this */
    private Filter chipsFilter;


    FilterableRecyclerView(Context c) {
        super(c);
        setLayoutManager(new LinearLayoutManager(c));
        setVisibility(GONE);
    }

    <T extends RecyclerView.Adapter & Filterable> void setAdapter(ChipsInputLayout chipsInput, T adapter) {
        setAdapter(adapter);
        this.chipsInput = chipsInput;
        this.chipsFilter = adapter.getFilter();
    }

    /**
     * Applies the given filter pattern to the filter. If the filter yields no results,
     * then we hide this filterable recycler, or show it otherwise.
     *
     * @param filter Filter pattern
     */
    void filterChips(CharSequence filter) {
        if (filter != null) {
            this.chipsFilter.filter(filter, new Filter.FilterListener() {
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
        this.chipsInput.getLocationInWindow(coord);
        ViewGroup.MarginLayoutParams lp = (MarginLayoutParams)getLayoutParams();
        lp.topMargin = coord[1] + chipsInput.getHeight();

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