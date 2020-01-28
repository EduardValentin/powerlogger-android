package com.example.powerlogger.dto;

import androidx.annotation.NonNull;

public class LogDTO {
    private String id;
    private String name;
    private String category;
    private int minutes;
    private String groupId;
    private int kcal;
    private String notes;
    private String createdAt;

    public LogDTO(String name, String type, int minutes, String notes, String createdAt) {
        this.name = name;
        this.category = type;
        this.minutes = minutes;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @NonNull
    @Override
    public String toString() {
        return name + " | " +  category + " | " + " Intensity: " + minutes;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getKcal() {
        return kcal;
    }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return category;
    }

    public void setType(String type) {
        this.category = type;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }
}
