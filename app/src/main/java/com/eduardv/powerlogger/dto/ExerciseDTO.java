package com.eduardv.powerlogger.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.eduardv.powerlogger.lib.SelectableItem;
import com.eduardv.powerlogger.model.ExerciseCategory;

import java.util.ArrayList;
import java.util.List;

public class ExerciseDTO implements SelectableItem, Parcelable {
    public String id;
    public String name;
    public ExerciseCategoryDTO category;
    public List<GroupDTO> groups = new ArrayList<>();

    public ExerciseDTO() {
    }

    protected ExerciseDTO(Parcel in) {
        id = in.readString();
        name = in.readString();
        category = in.readParcelable(ExerciseCategoryDTO.class.getClassLoader());
        groups = in.readArrayList(GroupDTO.class.getClassLoader());
    }

    public static final Creator<ExerciseDTO> CREATOR = new Creator<ExerciseDTO>() {
        @Override
        public ExerciseDTO createFromParcel(Parcel in) {
            return new ExerciseDTO(in);
        }

        @Override
        public ExerciseDTO[] newArray(int size) {
            return new ExerciseDTO[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GroupDTO> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupDTO> groups) {
        this.groups = groups;
    }

    public ExerciseCategoryDTO getCategory() {
        return category;
    }

    public void setCategory(ExerciseCategoryDTO category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeParcelable(category, flags);
        dest.writeList(groups);
    }

    public boolean equalIds(ExerciseDTO other) {
        return other.getId().equals(this.id);
    }

}

