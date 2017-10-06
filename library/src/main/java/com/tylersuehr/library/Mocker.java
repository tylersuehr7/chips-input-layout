package com.tylersuehr.library;

import com.tylersuehr.library.data.Chip;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * TODO: REMOVE FOR REAL USAGE
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public final class Mocker {
    public static List<Chip> mockChips() {
        List<Chip> chips = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            chips.add(new BaseChip("Chip " + i, "Hell yeah, I'm a chip!"));
        }
        chips.add(new BaseChip("Wrestling", "Hell yeah, I'm a chip!"));
        chips.add(new BaseChip("Football", "Hell yeah, I'm a chip!"));
        chips.add(new BaseChip("Soccer", "Hell yeah, I'm a chip!"));
        chips.add(new BaseChip("Baseball", "Hell yeah, I'm a chip!"));
        chips.add(new BaseChip("Kick the Can", "Hell yeah, I'm a chip!"));
        chips.add(new BaseChip("Ping Pong", "Hell yeah, I'm a chip!"));
        return chips;
    }
}