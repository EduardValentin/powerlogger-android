package com.example.powerlogger.utils;

import android.widget.TextView;

import androidx.databinding.InverseMethod;

import java.time.LocalDate;

public class BasicConverters {
    public static LocalDate fromStringToDate(String date) {
        try {
            return LocalDate.parse(date);
        } catch (Exception ex) {
            return LocalDate.now();
        }
    }

    public static float fromStringToFloat(String nr) {
        try {
            return Float.parseFloat(nr);
        } catch (Exception ex) {
            return 0;
        }
    }

    @InverseMethod("fromStringToFloat")
    public static String fromFloatToString(float f) {
        if (f == 0) {
            return "";
        }
        return Float.toString(f);
    }

    @InverseMethod("fromStringToDate")
    public static String fromDateToString(LocalDate ld) {
        try {
            return ld.toString();
        } catch (Exception ex) {
            return "";
        }

    }

}
