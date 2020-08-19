package com.eduardv.powerlogger.dto;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.eduardv.powerlogger.lib.SelectableItem;

public class GroupDTO implements SelectableItem, Parcelable {
    private String id;
    private String name;

    public GroupDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public GroupDTO() {
    }

    protected GroupDTO(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    public static final Creator<GroupDTO> CREATOR = new Creator<GroupDTO>() {
        @Override
        public GroupDTO createFromParcel(Parcel in) {
            return new GroupDTO(in);
        }

        @Override
        public GroupDTO[] newArray(int size) {
            return new GroupDTO[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
    }

    public boolean equalIds(GroupDTO other) {
        return other.getId().equals(this.id);
    }
}
