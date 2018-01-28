package com.tylersuehr.chips;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import com.tylersuehr.chips.data.Chip;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * This view displays the details of a chip (specified in Google Material Design Guide).
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public class ChipDetailsView extends FrameLayout implements ChipComponent {
    private ChipImageRenderer mImageRenderer;

    private TextView mTitleView;
    private TextView mLabelView;
    private ImageButton mButtonDelete;
    private CircleImageView mAvatarView;
    private ConstraintLayout mContentLayout;


    ChipDetailsView(@NonNull Context context) {
        super(context);
        View v = inflate(context, R.layout.chip_view_detailed, this);
        mContentLayout = v.findViewById(R.id.container);
        mAvatarView = v.findViewById(R.id.avatar);
        mTitleView = v.findViewById(R.id.title);
        mLabelView = v.findViewById(R.id.subtitle);
        mButtonDelete = v.findViewById(R.id.button_delete);

        setVisibility(GONE);

        // Hide the view on touch outside of it by making it focusable.
        setFocusable(true);
        setFocusableInTouchMode(true);
        setClickable(true);
    }

    @Override
    public void setChipOptions(ChipOptions options) {
        // Set background color, if possible
        if (options.mDetailsChipBackgroundColor != null) {
            mContentLayout.getBackground().setColorFilter(
                    options.mDetailsChipBackgroundColor.getDefaultColor(),
                    PorterDuff.Mode.SRC_ATOP);
        }

        // Set an available text color
        if (options.mDetailsChipTextColor != null) {
            mTitleView.setTextColor(options.mDetailsChipTextColor);
            mLabelView.setTextColor(options.mDetailsChipTextColor);
        } else if (Utils.isColorDark(getBackgroundColor())) {
            mTitleView.setTextColor(ColorStateList.valueOf(Color.WHITE));
            mLabelView.setTextColor(ColorStateList.valueOf(Color.WHITE));
        } else {
            mTitleView.setTextColor(ColorStateList.valueOf(Color.BLACK));
            mLabelView.setTextColor(ColorStateList.valueOf(Color.BLACK));
        }

        // Set an available delete icon
        if (options.mChipDeleteIcon != null) {
            mButtonDelete.setImageDrawable(options.mChipDeleteIcon);
        } else if (Utils.isColorDark(getBackgroundColor())) {
            mButtonDelete.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        } else {
            mButtonDelete.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
        }

        mTitleView.setTypeface(options.mTypeface);
        mLabelView.setTypeface(options.mTypeface);

        mImageRenderer = options.mImageRenderer;
    }

    /**
     * Displays the information stored in the given chip object.
     * @param chip {@link Chip}
     */
    public void inflateWithChip(Chip chip) {
        // Set the title and subtitle
        mTitleView.setText(chip.getTitle());
        if (chip.getSubtitle() == null) {
            mLabelView.setVisibility(GONE);
        } else {
            mLabelView.setText(chip.getSubtitle());
        }

        // Set an available avatar
        if (mImageRenderer == null) {
            throw new NullPointerException("Image renderer must be set!");
        }
        mImageRenderer.renderAvatar(mAvatarView, chip);
    }

    /**
     * Fades this view in using alpha animation.
     */
    public void fadeIn() {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(200);
        startAnimation(anim);
        setVisibility(VISIBLE);

        // Force this view to hide when it loses focus
        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    fadeOut();
                    setOnFocusChangeListener(null);
                }
            }
        });

        requestFocus();
    }

    /**
     * Fades this view out using alpha animation.
     */
    public void fadeOut() {
        AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setDuration(200);
        startAnimation(anim);
        setVisibility(GONE);

        // Fix onClick issue
        clearFocus();
        setClickable(false);
    }

    public void alignLeft() {
        LayoutParams params = (LayoutParams)mContentLayout.getLayoutParams();
        params.leftMargin = 0;
        mContentLayout.setLayoutParams(params);
    }

    public void alignRight() {
        LayoutParams params = (LayoutParams)mContentLayout.getLayoutParams();
        params.rightMargin = 0;
        mContentLayout.setLayoutParams(params);
    }

    public void setOnDeleteClicked(OnClickListener onClickListener) {
        mButtonDelete.setOnClickListener(onClickListener);
    }

    private int getBackgroundColor() {
        final Drawable dr = mContentLayout.getBackground();
        if (dr == null || !(dr instanceof ColorDrawable)) {
            return ContextCompat.getColor(getContext(),
                    R.color.chip_details_background);
        } else {
            return ((ColorDrawable)dr).getColor();
        }
    }
}