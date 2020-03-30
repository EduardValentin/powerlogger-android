package com.example.powerlogger.model;

import java.util.HashMap;
import java.util.Map;

public enum ExerciseCategory {
    HIIT("HIIT Cardio"),
    SLOW_CARDIO("Slow cardio"),
    MEDIUM_CARDIO("Medium cardio"),
    STRENGTH("Strength training");

    private String name;
    private Map<String, ExerciseCategory> nameMapper = new HashMap<>();

    ExerciseCategory(String s) {
        name = s;
    }

    public String getName() {
        return name;
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
