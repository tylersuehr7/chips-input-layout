package com.tylersuehr.chips;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * Subclass of {@link NestedScrollView} that allows a maximum height to be specified
 * such that this views height cannot exceed it.
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public class MaxHeightScrollView extends NestedScrollView {
    private int mMaxHeight;


    public MaxHeightScrollView(Context context) {
        this(context, null);
    }

    public MaxHeightScrollView(Context c, AttributeSet attrs) {
        super(c, attrs);
        TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.MaxHeightScrollView);
        this.mMaxHeight = a.getDimensionPixelSize(R.styleable.MaxHeightScrollView_android_maxHeight, Utils.dp(300));
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setMaxHeight(int height) {
        this.mMaxHeight = height;
        invalidate();
    }
}