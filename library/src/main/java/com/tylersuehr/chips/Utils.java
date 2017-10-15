package com.tylersuehr.chips;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.Px;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * Utility class that provides common methods needed by many different components
 * or for convenience.
 *
 * @author Tyler Suehr
 * @version 1.0
 */
final class Utils {
    static int dp(@Px int px) {
        final float density = Resources.getSystem().getDisplayMetrics().density;
        return (int)(px * density);
    }

    static float sp(@Px int px) {
        final float sDensity = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (px * sDensity);
    }

    static int alpha(int color, int alpha) {
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
    }

    static boolean isColorDark(int color) {
        double darkness = 1 - (0.2126 * Color.red(color) + 0.7152 * Color.green(color) + 0.0722 * Color.blue(color)) / 255;
        return darkness >= 0.5;
    }

    static int getWindowWidth(Context c) {
        final Resources res = c.getResources();
        return res.getDisplayMetrics().widthPixels;
    }

    static int getNavBarHeight(Context c) {
        int result = 0;
        boolean hasMenuKey = ViewConfiguration.get(c).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);

        if (!hasMenuKey && !hasBackKey) {
            // The device has a navigation bar
            Resources res = c.getResources();

            int orientation = res.getConfiguration().orientation;
            int resourceId;
            if (isTablet(c)) {
                resourceId = res.getIdentifier(orientation == Configuration.ORIENTATION_PORTRAIT ? "navigation_bar_height" : "navigation_bar_height_landscape", "dimen", "android");
            } else {
                resourceId = res.getIdentifier(orientation == Configuration.ORIENTATION_PORTRAIT ? "navigation_bar_height" : "navigation_bar_width", "dimen", "android");
            }
            if (resourceId > 0) {
                return res.getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }

    static Activity scanForActivity(Context c) {
        if (c == null) {
            return null;
        } else if (c instanceof Activity) {
            return (Activity)c;
        } else if (c instanceof ContextWrapper) {
            return scanForActivity(((ContextWrapper)c).getBaseContext());
        }
        return null;
    }

    private static boolean isTablet(Context c) {
        final Resources res = c.getResources();
        return (res.getConfiguration().screenLayout&Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}