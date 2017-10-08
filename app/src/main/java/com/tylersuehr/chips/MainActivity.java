package com.tylersuehr.chips;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.tylersuehr.library.ChipsInput;
import com.tylersuehr.library.data.Chip;

import java.util.List;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    private ChipsInput chipsInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.chipsInput = (ChipsInput)findViewById(R.id.chips_input);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_check:
                checkSelectedChips();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkSelectedChips() {
        final List<? extends Chip> chips = chipsInput.getSelectedChips();

        System.out.println("Number of chips selected: " + chips.size());
        for (Chip chip : chipsInput.getSelectedChips()) {
            System.out.println("Selected chip: " + chip.getTitle());
        }
    }
}