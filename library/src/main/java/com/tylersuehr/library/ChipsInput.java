package com.tylersuehr.library;
import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.tylersuehr.library.data.Chip;
import com.tylersuehr.library.data.ChipDataSource;
import com.tylersuehr.library.data.ListChipDataSource;

import java.util.List;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public class ChipsInput extends MaxHeightScrollView {
    private ChipDataSource chipDataSource;
    private ChipOptions chipOptions;

    /* Allows user to type text into the ChipsInput */
    private EditText chipsEditText;

    /* Displays selected chips and chips EditText */
    private RecyclerView chipsRecycler;

    /* Adapts the user's selected chips into the chips RecyclerView */
    private ChipsAdapter chipsAdapter;

    /* Displays filtered chips */
    private FilterableRecyclerView filterableRecyclerView;


    public ChipsInput(Context context) {
        this(context, null);
    }

    public ChipsInput(Context c, AttributeSet attrs) {
        super(c, attrs);
        this.chipOptions = new ChipOptions(c, attrs);
        this.chipDataSource = new ListChipDataSource();

        // Inflate the view
        inflate(c, R.layout.chips_input_view, this);

        // Setup the chips recycler view
        this.chipsAdapter = new ChipsAdapter(this);
        this.chipsRecycler = findViewById(R.id.chips_recycler);
        this.chipsRecycler.setLayoutManager(ChipsLayoutManager.newBuilder(c)
                .setOrientation(ChipsLayoutManager.HORIZONTAL).build());
        this.chipsRecycler.setNestedScrollingEnabled(false);
        this.chipsRecycler.setAdapter(chipsAdapter);

        setFilterableChipList(Mocker.mockChips());

        // Set the window callbacks to hide when the detailed chip view is visible
        Activity activity = Utils.scanForActivity(c);
        if (activity == null) {
            throw new ClassCastException("android.view.Context cannot be cast to android.app.Activity");
        }
        Window.Callback localCallback = activity.getWindow().getCallback();
        activity.getWindow().setCallback(new WindowCallback(localCallback, activity));
    }

    ChipDataSource getChipDataSource() {
        return chipDataSource;
    }

    ChipOptions getChipOptions() {
        return chipOptions;
    }

    RecyclerView getChipsRecyclerView() {
        return chipsRecycler;
    }

    /**
     * Lazy loads the input for the user to enter chip titles.
     * @return {@link EditText}
     */
    EditText getChipsInput() {
        if (chipsEditText == null) {
            this.chipsEditText = new AppCompatEditText(getContext());
            this.chipsEditText.setLayoutParams(new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));

            this.chipsEditText.setPadding(Utils.dp(8), 0, 0, 0);

            // Setup the chips options for the input
            this.chipsEditText.setBackgroundResource(android.R.color.transparent);
            if (chipOptions.textColorHint != null) {
                this.chipsEditText.setHintTextColor(chipOptions.textColorHint);
            }
            if (chipOptions.textColor != null) {
                this.chipsEditText.setTextColor(chipOptions.textColor);
            }
            this.chipsEditText.setHint(chipOptions.hint);

            // Prevent fullscreen on landscape
            this.chipsEditText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
            this.chipsEditText.setPrivateImeOptions("nm");

            // No suggestions
            this.chipsEditText.setInputType(InputType.TYPE_TEXT_VARIATION_FILTER|InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

            // Listen to text changes
            this.chipsEditText.addTextChangedListener(new ChipInputTextChangedHandler());
        }
        return chipsEditText;
    }

    public void setFilterableChipList(List<? extends Chip> chips) {
        this.chipDataSource.setFilterableChips(chips);

        // Setup the filterable recycler when new filterable data has been set
        this.filterableRecyclerView = new FilterableRecyclerView(getContext());
        if (chipOptions.filterableListBackgroundColor != null) {
            this.filterableRecyclerView.getBackground().setColorFilter(
                    chipOptions.filterableListBackgroundColor.getDefaultColor(), PorterDuff.Mode.SRC_ATOP);
        }
        this.filterableRecyclerView.build(this);
    }

    /**
     * Creates a new {@link ChipView} with its theme set from properties defined
     * in {@link #chipOptions}.
     * @return {@link ChipView}
     */
    ChipView getThemedChipView() {
        int padding = Utils.dp(4);

        ChipView chipView = new ChipView.Builder(getContext())
                .labelColor(chipOptions.chipLabelColor)
                .hasAvatarIcon(chipOptions.hasAvatarIcon)
                .deletable(chipOptions.chipDeletable)
                .deleteIcon(chipOptions.chipDeleteIcon)
                .deleteIconColor(chipOptions.chipDeleteIconColor)
                .backgroundColor(chipOptions.chipBackgroundColor)
                .build();

        chipView.setPadding(padding, padding, padding, padding);
        return chipView;
    }

    /**
     * Creates a new {@link DetailedChipView} with its theme set from properties
     * defined in {@link #chipOptions}.
     * @param chip {@link Chip}
     * @return {@link DetailedChipView}
     */
    DetailedChipView getThemedDetailedChipView(Chip chip) {
        return new DetailedChipView.Builder(getContext())
                .chip(chip)
                .textColor(chipOptions.detailedChipTextColor)
                .backgroundColor(chipOptions.detailedChipBackgroundColor)
                .deleteIconColor(chipOptions.detailedChipDeleteIconColor)
                .build();
    }


    private final class ChipInputTextChangedHandler implements TextWatcher {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (filterableRecyclerView != null) {
                // Hide the filterable recycler if there is no filter.
                // Filter the recycler if there is a filter
                if (TextUtils.isEmpty(s)) {
                    filterableRecyclerView.fadeOut();
                } else {
                    filterableRecyclerView.filterList(s);
                }
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void afterTextChanged(Editable s) {}
    }


    /**
     * This is what we use to hide the DetailedChipView when it's shown when the user
     * presses outside of the bounds of its visible space.
     *
     * This also hides the software keyboard when anything other than the bounds of our
     * chips EditText visible space is pressed.
     *
     * Importantly, we also need to wrap the existing window callback so we don't mess
     * up its functionality or prevent other things from working.
     */
    private static final class WindowCallback implements Window.Callback {
        private final Window.Callback localCallback;
        private final Activity activity;


        private WindowCallback(Window.Callback localCallback, Activity activity) {
            this.localCallback = localCallback;
            this.activity = activity;
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                View v = activity.getCurrentFocus();
                if (v instanceof DetailedChipView) {
                    Rect outRect = new Rect();
                    v.getGlobalVisibleRect(outRect);

                    // If the touch was outside the visible bounds of the detailed chip view,
                    // then we want to fade it out
                    if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                        ((DetailedChipView)v).fadeOut();
                    }
                } else if (v instanceof EditText) {
                    Rect outRect = new Rect();
                    v.getGlobalVisibleRect(outRect);

                    // If the touch was outside the visible bounds of the edit text, then we
                    // want to close the software keyboard
                    // TODO: make functional maybe
//                    if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())
//                            && !((EditText)v).isFilterableListVisible()) {
//                        InputMethodManager im = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//                        im.hideSoftInputFromInputMethod(v.getWindowToken(), 0);
//                    }
                }
            }
            return localCallback.dispatchTouchEvent(event);
        }

        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
            return localCallback.dispatchKeyEvent(event);
        }

        @Override
        public boolean dispatchKeyShortcutEvent(KeyEvent event) {
            return localCallback.dispatchKeyShortcutEvent(event);
        }

        @Override
        public boolean dispatchTrackballEvent(MotionEvent event) {
            return localCallback.dispatchTrackballEvent(event);
        }

        @Override
        public boolean dispatchGenericMotionEvent(MotionEvent event) {
            return localCallback.dispatchGenericMotionEvent(event);
        }

        @Override
        public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
            return localCallback.dispatchPopulateAccessibilityEvent(event);
        }

        @Nullable
        @Override
        public View onCreatePanelView(int featureId) {
            return localCallback.onCreatePanelView(featureId);
        }

        @Override
        public boolean onCreatePanelMenu(int featureId, Menu menu) {
            return localCallback.onCreatePanelMenu(featureId, menu);
        }

        @Override
        public boolean onPreparePanel(int featureId, View view, Menu menu) {
            return localCallback.onPreparePanel(featureId, view, menu);
        }

        @Override
        public boolean onMenuOpened(int featureId, Menu menu) {
            return localCallback.onMenuOpened(featureId, menu);
        }

        @Override
        public boolean onMenuItemSelected(int featureId, MenuItem item) {
            return localCallback.onMenuItemSelected(featureId, item);
        }

        @Override
        public void onWindowAttributesChanged(WindowManager.LayoutParams attrs) {
            this.localCallback.onWindowAttributesChanged(attrs);
        }

        @Override
        public void onContentChanged() {
            this.localCallback.onContentChanged();
        }

        @Override
        public void onWindowFocusChanged(boolean hasFocus) {
            this.localCallback.onWindowFocusChanged(hasFocus);
        }

        @Override
        public void onAttachedToWindow() {
            this.localCallback.onAttachedToWindow();
        }

        @Override
        public void onDetachedFromWindow() {
            this.localCallback.onDetachedFromWindow();
        }

        @Override
        public void onPanelClosed(int featureId, Menu menu) {
            this.localCallback.onPanelClosed(featureId, menu);
        }

        @Override
        public boolean onSearchRequested() {
            return localCallback.onSearchRequested();
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public boolean onSearchRequested(SearchEvent searchEvent) {
            return localCallback.onSearchRequested(searchEvent);
        }

        @Nullable
        @Override
        public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
            return localCallback.onWindowStartingActionMode(callback);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Nullable
        @Override
        public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int type) {
            return localCallback.onWindowStartingActionMode(callback, type);
        }

        @Override
        public void onActionModeStarted(ActionMode mode) {
            this.localCallback.onActionModeStarted(mode);
        }

        @Override
        public void onActionModeFinished(ActionMode mode) {
            this.localCallback.onActionModeFinished(mode);
        }
    }
}