package com.example.powerlogger.dto;


import android.os.Parcel;
import android.os.Parcelable;

import com.example.powerlogger.model.Exercise;

import java.util.UUID;

public class LogDTO implements Parcelable {
    public String id;
    public int minutes;
    public int kcal;
    public String notes;
    public String createdAt;
    public ExerciseDTO exercise;


    public LogDTO(String id, int minutes, int kcal, String notes, String createdAt) {
        this.id = id;
        this.minutes = minutes;
        this.kcal = kcal;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    public LogDTO(LogDTO logToCopy) {
        this.id = logToCopy.id;
        this.notes = logToCopy.notes;
        this.exercise = logToCopy.exercise;
        this.createdAt = logToCopy.createdAt;
        this.kcal = logToCopy.kcal;
        this.minutes = logToCopy.minutes;
    }

    protected LogDTO(Parcel in) {
        this.id = in.readString();
        minutes = in.readInt();
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

    public String getCreatedAt() {
        return createdAt;
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
        dest.writeInt(minutes);
        dest.writeInt(kcal);
        dest.writeString(notes);
        dest.writeParcelable(exercise, flags);
        dest.writeString(createdAt);
    }
}
