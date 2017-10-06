package com.tylersuehr.library;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.widget.Filter;
import android.widget.RelativeLayout;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * @author Tyler Suehr
 * @version 1.0
 */
class FilterableRecyclerView extends RecyclerView {
    private ChipsInput chipsInput;
    private Filter chipsFilter;


    FilterableRecyclerView(Context c) {
        super(c);
        setLayoutManager(new LinearLayoutManager(c));
        setVisibility(GONE);
    }

    public void setChipsInputAndAdjustLayout(final ChipsInput chipsInput, Filter chipsFilter) {
        this.chipsFilter = chipsFilter;
        this.chipsInput = chipsInput;
        this.chipsInput.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Position
                ViewGroup rootView = (ViewGroup)chipsInput.getRootView();

                // Size
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                        Utils.getWindowWidth(getContext()),
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    lp.bottomMargin = Utils.getNavBarHeight(getContext());
                }

                // Add view
                rootView.addView(FilterableRecyclerView.this, lp);

                // Remove the listener
                chipsInput.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    /**
     * Applies the given filter pattern to the filter. If the filter yields no results,
     * then we hide this filterable recycler, or show it otherwise.
     *
     * @param filter Filter pattern
     */
    void filterList(CharSequence filter) {
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