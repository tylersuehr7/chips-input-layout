package com.tylersuehr.chips;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * A subclass of {@link NestedScrollView} that allows you to specify a maximum height
 * that the view cannot exceed.
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public class MaxHeightScrollView extends NestedScrollView {
    private int maxHeight;


    public MaxHeightScrollView(Context context) {
        this(context, null);
    }

    public MaxHeightScrollView(Context c, AttributeSet attrs) {
        super(c, attrs);

        TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.MaxHeightScrollView);
        this.maxHeight = a.getDimensionPixelSize(R.styleable.MaxHeightScrollView_android_maxHeight, Utils.dp(300));
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setMaxHeight(int height) {
        this.maxHeight = height;
        invalidate();
    }
}