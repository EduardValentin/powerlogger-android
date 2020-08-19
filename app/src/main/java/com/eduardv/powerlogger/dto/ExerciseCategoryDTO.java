package com.eduardv.powerlogger.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.eduardv.powerlogger.lib.SelectableItem;

public class ExerciseCategoryDTO implements SelectableItem, Parcelable {

    private String name;
    private float met;
    private String displayName;
    private Float intensityFactor;

    protected ExerciseCategoryDTO(Parcel in) {
        name = in.readString();
        displayName = in.readString();
        if (in.readByte() == 0) {
            intensityFactor = null;
        } else {
            intensityFactor = in.readFloat();
        }
        met = in.readFloat();
    }

    public static final Creator<ExerciseCategoryDTO> CREATOR = new Creator<ExerciseCategoryDTO>() {
        @Override
        public ExerciseCategoryDTO createFromParcel(Parcel in) {
            return new ExerciseCategoryDTO(in);
        }

        @Override
        public ExerciseCategoryDTO[] newArray(int size) {
            return new ExerciseCategoryDTO[size];
        }
    };

    @Override
    public String getName() {
        return displayName;
    }

    public void setName(String name) {
        this.displayName = name;
    }

    public String getIdentifierName() {
        return name;
    }

    public void setIdentifierName(String name) {
        this.name = name;
    }

    public float getMet() {
        return met;
    }

    public void setMet(float met) {
        this.met = met;
    }

    public Float getIntensityFactor() {
        return intensityFactor;
    }

    public void setIntensityFactor(Float intensityFactor) {
        this.intensityFactor = intensityFactor;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(displayName);

        if (intensityFactor == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeFloat(intensityFactor);
        }

        dest.writeFloat(met);
    }
}
