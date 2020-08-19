package com.eduardv.powerlogger.ui.userSettings;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.eduardv.powerlogger.lib.SingleSelectSpinner;

public class GenderSpinner extends SingleSelectSpinner<Gender> {
    public GenderSpinner(@NonNull Context context) {
        super(context);
    }

    public GenderSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GenderSpinner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
