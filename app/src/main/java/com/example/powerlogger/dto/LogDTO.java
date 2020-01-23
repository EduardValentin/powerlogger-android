package com.example.powerlogger.dto;

import androidx.annotation.NonNull;

public class LogDTO {
    private String name;
    private String type;
    private String intensity;
    private String groupId;
    private String kcal;

    public LogDTO(String name, String type, String intensity) {
        this.name = name;
        this.type = type;
        this.intensity = intensity;
    }

    @NonNull
    @Override
    public String toString() {
        return name + " | " +  type + " | " + " Intensity: " + intensity;
    }

    public String getGroupId() {
        return groupId;
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
}
