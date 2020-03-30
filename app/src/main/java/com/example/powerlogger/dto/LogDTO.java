package com.example.powerlogger.dto;


import com.example.powerlogger.ui.logger.Identifiable;

import java.time.LocalDate;
import java.util.UUID;

public class LogDTO implements Identifiable {
    public UUID id;
    public int minutes;
    public int kcal;
    public String notes;
    public LocalDate createdAt;
    public ExerciseDTO exercise;


    public LogDTO(UUID id, int minutes, int kcal, String notes, LocalDate createdAt) {
        this.id = id;
        this.minutes = minutes;
        this.kcal = kcal;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    public ExerciseDTO getExercise() {
        return exercise;
    }

    public void setExercise(ExerciseDTO exercise) {
        this.exercise = exercise;
    }

    public LogDTO() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getKcal() {
        return kcal;
    }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String getName() {
        return this.exercise.getName();
    }
}
