package com.tylersuehr.library;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;

/**
 * Copyright © Tyler Suehr
 *
 * This is used to create a Bitmap containing a single letter with a background of
 * a randomly, but repeatable, color. If no digit or letter in the English alphabet
 * is available, a default image is shown instead.
 *
 * This tool affords the above with two methods:
 * (1) {@link #getLetterTile(String)}, and
 * (2) {@link #getCircularLetterTile(String)}
 *
 * @author Tyler Suehr
 * @version 1.1
 */
class LetterTileProvider {
    /* Default colors for the letter tiles */
    private static final String[] DEFAULT_COLORS = new String[] {
            "#f16364", "#f58559", "#f9a43e", "#e4c62e",
            "#67bf74", "#59a2be", "#2093cd", "#ad62a7"
    };

    private final TextPaint paint = new TextPaint();
    private final Rect bounds = new Rect();
    private final Canvas canvas = new Canvas();
    private final char[] firstChar = new char[1];

    private final String[] colors;
    private final Bitmap defaultBitmap;
    private final int tileSize;


    /* Constructors with all defaults */
    LetterTileProvider(Context c) {
        this(c, DEFAULT_COLORS);
    }

    /* Constructs with custom background colors */
    LetterTileProvider(Context c, String[] possibleColors) {
        this(c, possibleColors, c.getResources().getDimensionPixelSize(R.dimen.default_letter_tile_size));
    }

    /* Constructs with custom background colors and tile size */
    LetterTileProvider(Context c, @NonNull String[] possibleColors, int tileSize) {
        this(c, possibleColors, tileSize, R.drawable.ic_default_tile);
    }

    /* Constructs with all custom properties */
    LetterTileProvider(Context c, @NonNull String[] possibleColors, int tileSize, @DrawableRes int defaultDr) {
        // Setup the paint
        this.paint.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        this.paint.setColor(Color.WHITE);
        this.paint.setTextAlign(Paint.Align.CENTER);
        this.paint.setAntiAlias(true);

        // Setup the properties
        this.colors = possibleColors;
        this.tileSize = tileSize;
        this.defaultBitmap = drawableToBitmap(ContextCompat.getDrawable(c, defaultDr));
    }

    /**
     * Convenience method to make the custom Bitmap from {@link #getLetterTile(String)}
     * a circular Bitmap.
     *
     * @param displayName Any string value
     * @return {@link Bitmap}
     */
    Bitmap getCircularLetterTile(String displayName) {
        return getCircularBitmap(getLetterTile(displayName));
    }

    /**
     * Creates a custom Bitmap containing a letter, digit, or default image (if no letter
     * or digit can be resolved), positioned at the center, with a randomized background
     * color, picked from {@link #colors}, based on the hashed value of the given string.
     *
     * @param displayName Any string value
     * @return {@link Bitmap}
     */
    Bitmap getLetterTile(String displayName) {
        // Don't allow empty strings
        if (displayName == null || displayName.length() == 0) { return null; }

        final char firstChar = displayName.charAt(0);

        // Create a Bitmap with the width & height specified from resources
        final Bitmap bitmap = Bitmap.createBitmap(tileSize, tileSize, Bitmap.Config.ARGB_8888);

        // Setup our canvas for drawing
        final Canvas c = canvas;
        c.setBitmap(bitmap);
        c.drawColor(pickColor(displayName));

        // We want to use the default Bitmap if our character is not a letter or digit
        if (Character.isLetterOrDigit(firstChar)) {
            this.firstChar[0] = Character.toUpperCase(firstChar);

            // Set the paint text size as half the bitmap's height
            this.paint.setTextSize(tileSize >> 1);

            // Measure the bounds of our first character
            this.paint.getTextBounds(this.firstChar, 0, 1, bounds);

            // Draw the character on the Canvas
            c.drawText(this.firstChar, 0, 1,
                    tileSize / 2,
                    tileSize / 2 + (bounds.bottom - bounds.top) / 2,
                    paint);
        } else {
            // (32 - 24) / 2 = 4
            final float density = Resources.getSystem().getDisplayMetrics().density;
            final float defSize = (4f * density);
            c.drawBitmap(defaultBitmap, defSize, defSize, null);
        }

        return bitmap;
    }

    /**
     * Randomly picks one of the colors in {@link #colors} using an algorithm based
     * on the hashed value of the given key.
     *
     * This is consistent because String.hashCode() is guaranteed to not change across
     * Java versions, which implicates that the same key always maps to the same color.
     *
     * @param key Any string value
     * @return {@link android.support.annotation.ColorInt}
     */
    private int pickColor(String key) {
        final int whichColor = Math.abs(key.hashCode()) % colors.length;
        return Color.parseColor(colors[whichColor]);
    }

    /**
     * Creates a circular Bitmap by drawing a circle and then the given
     * Bitmap in the center.
     *
     * @param bitmap {@link Bitmap}
     * @return {@link Bitmap}
     */
    private Bitmap getCircularBitmap(Bitmap bitmap) {
        Bitmap output;

        final int smallestSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
        output = Bitmap.createBitmap(smallestSize, smallestSize, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        float r = smallestSize / 2;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0); // Draw the entire bitmap transparent
        paint.setColor(color);
        canvas.drawCircle(r, r, r, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * Creates a Bitmap object from a Drawable object.
     */
    private static Bitmap drawableToBitmap(Drawable dr) {
        // Attempt to retrieve any existing Bitmap, if possible
        if (dr instanceof BitmapDrawable) {
            BitmapDrawable bDr = (BitmapDrawable)dr;
            if (bDr.getBitmap() != null) {
                return bDr.getBitmap();
            }
        }

        // Create a valid blank Bitmap
        final Bitmap bitmap;
        if (dr.getIntrinsicWidth() <= 0 || dr.getIntrinsicHeight() <= 0) {
            // Single color bitmap will be create of 1x1 pixel
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(dr.getIntrinsicWidth(),
                    dr.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        // Use our Canvas to draw the Drawable onto the Bitmap
        Canvas canvas = new Canvas(bitmap);
        dr.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        dr.draw(canvas);
        return bitmap;
    }
}