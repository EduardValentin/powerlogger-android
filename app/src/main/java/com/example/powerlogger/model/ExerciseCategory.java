package com.example.powerlogger.model;

import com.example.powerlogger.lib.SelectableItem;

import java.util.HashMap;
import java.util.Map;

public enum ExerciseCategory implements SelectableItem {
    HIIT("HIIT Cardio"),
    SLOW_CARDIO("Slow cardio"),
    MEDIUM_CARDIO("Medium cardio"),
    STRENGTH("Strength training");

    private String name;
    private Map<String, ExerciseCategory> nameMapper = new HashMap<>();

    ExerciseCategory(String s) {
        name = s;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static ExerciseCategory fromString(String text) {
        for (ExerciseCategory b : ExerciseCategory.values()) {
            if (b.getName().equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
