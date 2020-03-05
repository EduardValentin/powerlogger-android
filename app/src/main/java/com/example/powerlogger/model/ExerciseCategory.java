package com.example.powerlogger.model;

public enum ExerciseCategory {
    HIIT("HIIT Cardio"),
    SLOW_CARDIO("Slow cardio"),
    MEDIUM_CARDIO("Medium cardio"),
    STRENGTH("Strength training");

    private String name;

    ExerciseCategory(String s) {
        name = s;
    }

    public String getName() {
        return name;
    }
}
