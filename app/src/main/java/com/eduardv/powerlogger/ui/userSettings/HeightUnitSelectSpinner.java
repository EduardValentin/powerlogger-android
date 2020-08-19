package com.eduardv.powerlogger.ui.userSettings;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.eduardv.powerlogger.dto.units.HeightUnit;
import com.eduardv.powerlogger.lib.SingleSelectSpinner;

public class HeightUnitSelectSpinner extends SingleSelectSpinner<HeightUnit> {
    public HeightUnitSelectSpinner(@NonNull Context context) {
        super(context);
    }

    public HeightUnitSelectSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeightUnitSelectSpinner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
