package com.eduardv.powerlogger.dto.user;

import com.eduardv.powerlogger.dto.units.HeightUnit;
import com.eduardv.powerlogger.dto.units.WeightUnit;
import com.eduardv.powerlogger.ui.userSettings.Gender;

import java.util.UUID;

public class UserSettingsDTO {
    public UUID id;
    private float weight;
    private float height;
    private String birthDate;
    private  HeightUnit heightUnit;
    private  WeightUnit weightUnit;
    private Gender gender;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public HeightUnit getHeightUnit() {
        return heightUnit;
    }

    public void setHeightUnit(HeightUnit heightUnit) {
        this.heightUnit = heightUnit;
    }

    public WeightUnit getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(WeightUnit weightUnit) {
        this.weightUnit = weightUnit;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}