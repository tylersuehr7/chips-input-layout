package com.tylersuehr.chipexample.testing;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.tylersuehr.chipexample.R;
import com.tylersuehr.chips.ChipsInputLayout;
import com.tylersuehr.chips.data.Chip;
import com.tylersuehr.chips.data.ChipSelectionObserver;

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
public class ChipsInputTestActivity extends AppCompatActivity
        implements View.OnClickListener, ChipSelectionObserver {
    private ChipsInputLayout chipsInputLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chips_input_test);

        // Setup chips input layout
        this.chipsInputLayout = (ChipsInputLayout)findViewById(R.id.chips_input);
        this.chipsInputLayout.addChipSelectionObserver(this);

        // Setup test action listeners
        findViewById(R.id.button_add_filtered_chip).setOnClickListener(this);
        findViewById(R.id.button_add_selected_chip).setOnClickListener(this);
        findViewById(R.id.button_clear_filtered_chips).setOnClickListener(this);
        findViewById(R.id.button_clear_selected_chips).setOnClickListener(this);
        findViewById(R.id.button_show_selection).setOnClickListener(this);

        findViewById(R.id.button_text_color).setOnClickListener(this);
        findViewById(R.id.button_text_color_hint).setOnClickListener(this);
        findViewById(R.id.button_hint).setOnClickListener(this);

        findViewById(R.id.button_chip_delete_icon_color).setOnClickListener(this);
        findViewById(R.id.button_chip_bg_color).setOnClickListener(this);
        findViewById(R.id.button_chip_label_color).setOnClickListener(this);
        findViewById(R.id.button_delete_icon).setOnClickListener(this);
        findViewById(R.id.button_toggle_avatar).setOnClickListener(this);
        findViewById(R.id.button_toggle_detailed_chips).setOnClickListener(this);
        findViewById(R.id.button_toggle_deletable_chips).setOnClickListener(this);

        findViewById(R.id.button_detailed_icon_color).setOnClickListener(this);
        findViewById(R.id.button_detailed_bg_color).setOnClickListener(this);
        findViewById(R.id.button_detailed_text_color).setOnClickListener(this);

        findViewById(R.id.button_filterable_bg_color).setOnClickListener(this);
        findViewById(R.id.button_filterable_text_color).setOnClickListener(this);
        findViewById(R.id.button_filterable_elevation).setOnClickListener(this);

        findViewById(R.id.button_toggle_custom_chips).setOnClickListener(this);
        findViewById(R.id.button_max_rows).setOnClickListener(this);
        findViewById(R.id.button_typeface).setOnClickListener(this);

        findViewById(R.id.button_validate).setOnClickListener(this);
    }

    @Override
    public void onChipSelected(Chip addedChip) {
        Toast.makeText(this, "Chip added!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChipDeselected(Chip removedChip) {
        Toast.makeText(this, "Chip removed!", Toast.LENGTH_SHORT).show();
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
            case R.id.button_show_selection:
                showSelection();
                break;

            case R.id.button_text_color:
                this.chipsInputLayout.setInputTextColor(new ColorStateList(
                        new int[][] {
                                new int[] { android.R.attr.state_enabled },
                                new int[] { -android.R.attr.state_enabled }
                        },
                        new int[] { Color.RED, Color.GREEN }
                ));
                break;
            case R.id.button_text_color_hint:
                this.chipsInputLayout.setInputHintTextColor(new ColorStateList(
                        new int[][] {
                                new int[] { android.R.attr.state_enabled },
                                new int[] { -android.R.attr.state_enabled }
                        },
                        new int[] { Color.BLUE, Color.CYAN }
                ));
                break;
            case R.id.button_hint:
                this.chipsInputLayout.setInputHint("New hint!...");
                break;

            case R.id.button_chip_delete_icon_color:
                this.chipsInputLayout.setChipDeleteIconColor(new ColorStateList(
                        new int[][] {
                                new int[] { android.R.attr.state_pressed },
                                new int[] { -android.R.attr.state_pressed }
                        },
                        new int[] { Color.BLUE, Color.RED }
                ));
                break;
            case R.id.button_chip_bg_color:
                this.chipsInputLayout.setChipBackgroundColor(new ColorStateList(
                        new int[][] {
                                new int[] { android.R.attr.state_pressed },
                                new int[] { -android.R.attr.state_pressed }
                        },
                        new int[] { Color.BLUE, Color.RED }
                ));
                break;
            case R.id.button_chip_label_color:
                this.chipsInputLayout.setChipTitleTextColor(new ColorStateList(
                        new int[][] {
                                new int[] { android.R.attr.state_pressed },
                                new int[] { -android.R.attr.state_pressed }
                        },
                        new int[] { Color.BLUE, Color.RED }
                ));
                break;
            case R.id.button_delete_icon:
                this.chipsInputLayout.setChipDeleteIcon(R.drawable.ic_clear_black_24dp);
                break;
            case R.id.button_toggle_avatar:
                this.chipsInputLayout.setShowChipAvatarEnabled(false);
                break;
            case R.id.button_toggle_detailed_chips:
                this.chipsInputLayout.setShowDetailedChipsEnabled(false);
                break;
            case R.id.button_toggle_deletable_chips:
                this.chipsInputLayout.setChipsDeletable(false);
                break;

            case R.id.button_detailed_icon_color:
                this.chipsInputLayout.setDetailedChipDeleteIconColor(new ColorStateList(
                        new int[][] {
                                new int[] { android.R.attr.state_enabled },
                                new int[] { -android.R.attr.state_enabled }
                        },
                        new int[] { Color.BLUE, Color.RED }
                ));
                break;
            case R.id.button_detailed_bg_color:
                this.chipsInputLayout.setDetailedChipBackgroundColor(new ColorStateList(
                        new int[][] {
                                new int[] { android.R.attr.state_enabled },
                                new int[] { -android.R.attr.state_enabled }
                        },
                        new int[] { Color.BLUE, Color.RED }
                ));
                break;
            case R.id.button_detailed_text_color:
                this.chipsInputLayout.setDetailedChipTextColor(new ColorStateList(
                        new int[][] {
                                new int[] { android.R.attr.state_enabled },
                                new int[] { -android.R.attr.state_enabled }
                        },
                        new int[] { Color.RED, Color.BLUE  }
                ));
                break;

            case R.id.button_filterable_bg_color:
                this.chipsInputLayout.setFilterableListBackgroundColor(new ColorStateList(
                        new int[][] {
                                new int[] { android.R.attr.state_enabled },
                                new int[] { -android.R.attr.state_enabled }
                        },
                        new int[] { Color.BLUE, Color.RED }
                ));
                break;
            case R.id.button_filterable_text_color:
                this.chipsInputLayout.setFilterableListTextColor(new ColorStateList(
                        new int[][] {
                                new int[] { android.R.attr.state_enabled },
                                new int[] { -android.R.attr.state_enabled }
                        },
                        new int[] { Color.RED, Color.BLUE }
                ));
                break;
            case R.id.button_filterable_elevation:
                this.chipsInputLayout.setFilterableListElevation(0f);
                break;

            case R.id.button_toggle_custom_chips:
                this.chipsInputLayout.setCustomChipsEnabled(false);
                break;
            case R.id.button_max_rows:
                this.chipsInputLayout.setMaxRows(6);
                break;
            case R.id.button_typeface:
                this.chipsInputLayout.setTypeface(Typeface.create("cursive", Typeface.BOLD));
                break;

            case R.id.button_validate:
                validateChips();
                break;
        }
    }

    private void addFilteredChip(Chip chip) {
        chip.setFilterable(true);
        this.chipsInputLayout.addFilteredChip(chip);
    }

    private void showSelection() {
        List<TestActivityChip> chips = (List<TestActivityChip>)chipsInputLayout.getSelectedChips();
        System.out.println("Number of chips selected: " + chips.size());
        for (Chip chip : chips) {
            System.out.println("Chips: " + chip.getTitle());
        }
    }

    private void validateChips() {
        Toast.makeText(this, (chipsInputLayout.validateSelectedChips() ?
                "Chips are valid!" : "Chips are NOT valid!"), Toast.LENGTH_SHORT).show();
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