package com.tylersuehr.library.data;
import android.support.annotation.Nullable;
import java.util.List;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public interface ChipDataSourceObserver {
    void onChipDataSourceChanged(@Nullable Chip affectedChip);
}