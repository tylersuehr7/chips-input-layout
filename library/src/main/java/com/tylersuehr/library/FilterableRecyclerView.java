package com.tylersuehr.library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.widget.Filter;
import android.widget.RelativeLayout;

import com.tylersuehr.library.data.Chip;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * @author Tyler Suehr
 * @version 1.0
 */
@SuppressLint("ViewConstructor")
class FilterableRecyclerView extends RecyclerView implements FilterableChipsAdapter.OnFilteredChipClickListener {
    private FilterableChipsAdapter adapter;
    private ChipsInput chipsInput;
    private boolean shown = false;


    FilterableRecyclerView(Context c, final ChipsInput chipsInput) {
        super(c);
        this.chipsInput = chipsInput;
        setLayoutManager(new LinearLayoutManager(c));
        setVisibility(GONE);

        this.adapter = new FilterableChipsAdapter(c,
                this,
                chipsInput.getChipOptions(),
                chipsInput.getChipDataSource(),
                chipsInput.getChipDataSourceManager());
        setAdapter(adapter);

        // Listen to changes in the tree
        this.chipsInput.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Position
                ViewGroup rootView = (ViewGroup) chipsInput.getRootView();

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

                shown = true;

                // Remove the listener
                chipsInput.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    /**
     * When a filtered chip is clicked, the data source will be updated, and therefore
     * should hide the filtered recycler view.
     */
    @Override
    public void onFilteredChipClick(Chip chip) {
        fadeOut();
    }

    void filterList(CharSequence filter) {
        this.adapter.getFilter().filter(filter, new Filter.FilterListener() {
            @Override
            public void onFilterComplete(int count) {
                // Show if, and only if, there are results
                if (adapter.getItemCount() > 0) {
                    fadeIn();
                } else {
                    fadeOut();
                }
            }
        });
    }

    /**
     * Uses alpha animation to fade in the current view if it's not visible.
     */
    void fadeIn() {
        if (!shown) { return; }
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
        if (!shown) { return; }
        if (getVisibility() == GONE) { return; }

        AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setDuration(200);
        startAnimation(anim);

        setVisibility(GONE);
    }
}