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
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;

/**
 * Copyright © Tyler Suehr
 *
 * Creates a {@link Bitmap} that contains a letter used in the English alphabet
 * or digit, if there is no letter or digit available, a default image is shown instead.
 *
 * @author Tyler Suehr
 * @version 1.0
 */
class LetterTileProvider {
    private static final int NUM_OF_TILE_COLORS = 8;

    private final TextPaint paint = new TextPaint();
    private final Rect bounds = new Rect();
    private final Canvas canvas = new Canvas();
    private final char[] firstChar = new char[1];

    private final String[] colors;
    private final Bitmap defaultBitmap;

    private final int width;
    private final int height;


    LetterTileProvider(Context c) {
        final Resources res = c.getResources();

        // Setup the paint
        this.paint.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        this.paint.setColor(Color.WHITE);
        this.paint.setTextAlign(Paint.Align.CENTER);
        this.paint.setAntiAlias(true);

        this.colors = res.getStringArray(R.array.letter_tile_colors);
        this.defaultBitmap = drawableToBitmap(ContextCompat.getDrawable(c, R.drawable.ic_person_white_24dp));
        this.width = res.getDimensionPixelSize(R.dimen.letter_tile_size);
        this.height = res.getDimensionPixelSize(R.dimen.letter_tile_size);
    }

    LetterTileProvider(Context c, String[] possibleColors) {
        final Resources res = c.getResources();

        // Setup the paint
        this.paint.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        this.paint.setColor(Color.WHITE);
        this.paint.setTextAlign(Paint.Align.CENTER);
        this.paint.setAntiAlias(true);

        this.colors = possibleColors;
        this.defaultBitmap = drawableToBitmap(ContextCompat.getDrawable(c, R.drawable.ic_person_white_24dp));
        this.width = res.getDimensionPixelSize(R.dimen.letter_tile_size);
        this.height = res.getDimensionPixelSize(R.dimen.letter_tile_size);
    }

    /**
     * Convenience method to create a circular tile letter Bitmap.
     * @param displayName Any kind of string
     * @return {@link Bitmap}
     */
    Bitmap getCircularLetterTile(String displayName) {
        return getCircularBitmap(getLetterTile(displayName));
    }

    /**
     * Creates a Bitmap with the letter (first character of the given name),
     * in the center of the Bitmap.
     * @param displayName Any kind of string
     * @return {@link Bitmap}
     */
    Bitmap getLetterTile(String displayName) {
        // Don't allow empty strings
        if (displayName == null || displayName.length() == 0) { return null; }

        final char firstChar = displayName.charAt(0);

        // Create a Bitmap with the width & height specified from resources
        final Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        // Setup our canvas for drawing
        final Canvas c = canvas;
        c.setBitmap(bitmap);
        c.drawColor(pickColor(displayName));

        // We want to use the default Bitmap if our character is not a letter or digit
        if (Character.isLetterOrDigit(firstChar)) {
            this.firstChar[0] = Character.toUpperCase(firstChar);

            // Set the paint text size as half the bitmap's height
            this.paint.setTextSize(height >> 1);

            // Measure the bounds of our first character
            this.paint.getTextBounds(this.firstChar, 0, 1, bounds);

            // Draw the character on the Canvas
            c.drawText(this.firstChar, 0, 1,
                    width / 2,
                    height / 2 + (bounds.bottom - bounds.top) / 2,
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
     * Randomly chooses one of the defined colors using the given key.
     * @param key Any kind of string
     * @return {@link android.support.annotation.ColorInt}
     */
    private int pickColor(String key) {
        // String.hashCode() is not supposed to change across Java versions, so this should
        // guarantee the same key always maps to the same color regardless
        final int whichColor = Math.abs(key.hashCode()) % NUM_OF_TILE_COLORS;
        return Color.parseColor(colors[whichColor]);
    }

    /**
     * Creates a circular Bitmap by drawing the given Bitmap in a circular mannor.
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
     * Converts a Drawable into a Bitmap object.
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