package com.tylersuehr.chips.testing;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tylersuehr.chips.R;
import com.tylersuehr.library.ChipsInputLayout;
import com.tylersuehr.library.data.Chip;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * This activity is solely for testing purposes only, not for demonstration.
 *
 * You can physical use this activity if you want, but it's intended to be used by Espresso
 * for UI testing and asserting functionality in the {@link ChipsInputLayout}.
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public class ChipsInputTestActivity extends AppCompatActivity implements View.OnClickListener {
    private ChipsInputLayout chipsInputLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chips_input_test);

        // Setup chips input layout
        this.chipsInputLayout = (ChipsInputLayout)findViewById(R.id.chips_input);

        // Setup test action listeners
        findViewById(R.id.button_add_filtered_chip).setOnClickListener(this);
        findViewById(R.id.button_add_selected_chip).setOnClickListener(this);
        findViewById(R.id.button_clear_filtered_chips).setOnClickListener(this);
        findViewById(R.id.button_clear_selected_chips).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add_filtered_chip:
                addFilteredChip(new TestActivityChip("Added chip!"));
                break;
            case R.id.button_add_selected_chip:
                this.chipsInputLayout.addSelectedChip(new TestActivityChip("Added Chip!"));
                break;
            case R.id.button_clear_filtered_chips:
                this.chipsInputLayout.clearFilteredChips();
                break;
            case R.id.button_clear_selected_chips:
                this.chipsInputLayout.clearSelectedChips();
                break;
        }
    }

    private void addFilteredChip(Chip chip) {
        chip.setFilterable(true);
        this.chipsInputLayout.addFilteredChip(chip);
    }


    /**
     * Creates a mock list of {@link TestActivityChip} with a given size.
     * @param size Size of mock list
     * @return List of {@link TestActivityChip}
     */
    private static List<TestActivityChip> createChips(int size) {
        List<TestActivityChip> chips = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            if ((i&1) == 1) {
                chips.add(new TestActivityChip("Chip " + i, "Subtitle for chip :)"));
            } else {
                chips.add(new TestActivityChip("Chip " + i));
            }
        }
        return chips;
    }

    /**
     * Subclass of {@link Chip} for testing in this activity.
     */
    private static final class TestActivityChip extends Chip {
        private final String title;
        private final String subtitle;

        private TestActivityChip(String title) {
            this(title, null);
        }

        private TestActivityChip(String title, String subtitle) {
            this.title = title;
            this.subtitle = subtitle;
        }

        @Nullable
        @Override
        public Object getId() {
            return null;
        }

        @NonNull
        @Override
        public String getTitle() {
            return title;
        }

        @Nullable
        @Override
        public String getSubtitle() {
            return subtitle;
        }

        @Nullable
        @Override
        public Uri getAvatarUri() {
            return null;
        }

        @Nullable
        @Override
        public Drawable getAvatarDrawable() {
            return null;
        }
    }
}