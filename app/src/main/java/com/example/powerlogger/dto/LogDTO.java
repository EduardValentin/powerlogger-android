package com.example.powerlogger.dto;

import androidx.annotation.NonNull;

public class LogDTO {
    private String id;
    private String name;
    private String type;
    private String intensity;
    private String groupId;
    private String kcal;
    private String notes;

    public LogDTO(String id, String name, String type, String intensity, String kcal, String notes) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.intensity = intensity;
        this.kcal = kcal;
        this.notes = notes;
    }

    @NonNull
    @Override
    public String toString() {
        return name + " | " +  type + " | " + " Intensity: " + intensity;
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

    public String getKcal() {
        return kcal;
    }

    public void setKcal(String kcal) {
        this.kcal = kcal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIntensity() {
        return intensity;
    }

    public void setIntensity(String intensity) {
        this.intensity = intensity;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
