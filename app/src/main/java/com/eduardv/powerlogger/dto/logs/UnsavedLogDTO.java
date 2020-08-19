package com.eduardv.powerlogger.dto.logs;

import android.os.Parcel;
import android.os.Parcelable;

import com.eduardv.powerlogger.dto.ExerciseDTO;

public class UnsavedLogDTO implements Parcelable {
    public int minutes;
    public int kcal;
    public String notes;
    public String createdAt;
    public ExerciseDTO exercise;

    protected UnsavedLogDTO(Parcel in) {
        minutes = in.readInt();
        kcal = in.readInt();
        notes = in.readString();
        createdAt = in.readString();
        exercise = in.readParcelable(ExerciseDTO.class.getClassLoader());
    }

    public UnsavedLogDTO() {
    }

    public static final Creator<UnsavedLogDTO> CREATOR = new Creator<UnsavedLogDTO>() {
        @Override
        public UnsavedLogDTO createFromParcel(Parcel in) {
            return new UnsavedLogDTO(in);
        }

        @Override
        public UnsavedLogDTO[] newArray(int size) {
            return new UnsavedLogDTO[size];
        }
    };

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

    public ExerciseDTO getExercise() {
        return exercise;
    }

    public void setExercise(ExerciseDTO exercise) {
        this.exercise = exercise;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(minutes);
        dest.writeInt(kcal);
        dest.writeString(notes);
        dest.writeString(createdAt);
        dest.writeParcelable(exercise, flags);
    }
}
