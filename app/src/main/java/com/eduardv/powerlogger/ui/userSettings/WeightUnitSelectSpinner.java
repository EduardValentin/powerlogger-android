package com.eduardv.powerlogger.ui.userSettings;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.eduardv.powerlogger.dto.units.WeightUnit;
import com.eduardv.powerlogger.lib.SingleSelectSpinner;

public class WeightUnitSelectSpinner extends SingleSelectSpinner<WeightUnit> {
    public WeightUnitSelectSpinner(@NonNull Context context) {
        super(context);
    }

    public WeightUnitSelectSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WeightUnitSelectSpinner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
