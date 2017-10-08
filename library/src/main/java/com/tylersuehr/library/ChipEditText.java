package com.tylersuehr.library;
import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * Setting onKeyEventListener doesn't work on software keyboards (IME) :(
 *
 * This subclass of {@link AppCompatEditText} provides a solution for detecting both
 * the IME_ACTION_DONE and backspace key press on both software keyboards!
 *
 * TODO: Also try to simplify text watcher crap with this as well
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public class ChipEditText extends AppCompatEditText {
    private OnKeyboardListener keyboardListener;


    public ChipEditText(Context c) {
        super(c);
    }

    /**
     * Used to detect IME option press on any type of input method.
     */
    @Override
    public void onEditorAction(int actionCode) {
        if (keyboardListener != null && actionCode == EditorInfo.IME_ACTION_DONE) {
            this.keyboardListener.onKeyboardActionDone(getText().toString());
        }
        super.onEditorAction(actionCode);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new ChipsInputConnection(super.onCreateInputConnection(outAttrs));
    }

    void setCustomProperties(ChipOptions op) {
        setBackgroundResource(android.R.color.transparent);
        if (op.textColorHint != null) {
            setHintTextColor(op.textColorHint);
        }
        if (op.textColor != null) {
            setTextColor(op.textColor);
        }
        setHint(op.hint);
    }

    public void setKeyboardListener(OnKeyboardListener listener) {
        this.keyboardListener = listener;
    }

    public OnKeyboardListener getKeyboardListener() {
        return keyboardListener;
    }

    /**
     * Callbacks for simplified keyboard action events.
     */
    interface OnKeyboardListener {
        void onKeyboardBackspace();
        void onKeyboardActionDone(String text);
    }


    /**
     * Since we cannot detect software keyboard backspace (KEYCODE_DEL) events using
     * onKeyEventListener, we will use this wrapper for {@link InputConnection} to do
     * so for software keyboards.
     *
     * In the latest Android version, deleteSurroundingText(1, 0), will be called for
     * backspace. So, we just emulate a backspace key press if that method is called
     * by manually calling {@link #sendKeyEvent(KeyEvent)}.
     */
    private final class ChipsInputConnection extends InputConnectionWrapper {
        private ChipsInputConnection(InputConnection target) {
            super(target, true);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            if (keyboardListener != null) {
                if (event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_DEL) { // Backspace key
                    keyboardListener.onKeyboardBackspace();
                }
            }
            return super.sendKeyEvent(event);
        }

        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            if (beforeLength == 1 && afterLength == 0) { // Backspace key
                return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
                        && sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
            }
            return super.deleteSurroundingText(beforeLength, afterLength);
        }
    }
}