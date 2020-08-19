package com.eduardv.powerlogger.dto.logs;


import android.os.Parcel;
import android.os.Parcelable;

import com.eduardv.powerlogger.dto.ExerciseDTO;

import static com.eduardv.powerlogger.dto.logs.IntensityType.MINUTES;

public class LogDTO implements Parcelable {
    private String id;
    private float intensity;
    private IntensityType intensityType = MINUTES;
    private int kcal;
    private String notes;
    private String createdAt;
    private ExerciseDTO exercise;

    public LogDTO(LogDTO logToCopy) {
        this.id = logToCopy.id;
        this.notes = logToCopy.notes;
        this.exercise = logToCopy.exercise;
        this.createdAt = logToCopy.createdAt;
        this.kcal = logToCopy.kcal;
        this.intensity = logToCopy.intensity;
        this.intensityType = logToCopy.intensityType;
    }

    public LogDTO(String id, int intensity, IntensityType intensityType, int kcal, String notes, String createdAt, ExerciseDTO exercise) {
        this.id = id;
        this.intensity = intensity;
        this.intensityType = intensityType;
        this.kcal = kcal;
        this.notes = notes;
        this.createdAt = createdAt;
        this.exercise = exercise;
    }

    protected LogDTO(Parcel in) {
        this.id = in.readString();
        intensity = in.readFloat();
        intensityType = IntensityType.valueOf(in.readString());
        kcal = in.readInt();
        notes = in.readString();
        this.exercise = in.readParcelable(ExerciseDTO.class.getClassLoader());
        createdAt = in.readString();
    }

    public static final Creator<LogDTO> CREATOR = new Creator<LogDTO>() {
        @Override
        public LogDTO createFromParcel(Parcel in) {
            return new LogDTO(in);
        }

        @Override
        public LogDTO[] newArray(int size) {
            return new LogDTO[size];
        }
    };

    public ExerciseDTO getExercise() {
        return exercise;
    }

    public void setExercise(ExerciseDTO exercise) {
        this.exercise = exercise;
    }

    public LogDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getIntensity() {
        return intensity;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public IntensityType getIntensityType() {
        return intensityType;
    }

    public void setIntensityType(IntensityType intensityType) {
        this.intensityType = intensityType;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeFloat(intensity);
        dest.writeString(intensityType.toString());
        dest.writeInt(kcal);
        dest.writeString(notes);
        dest.writeParcelable(exercise, flags);
        dest.writeString(createdAt);
    }
}
