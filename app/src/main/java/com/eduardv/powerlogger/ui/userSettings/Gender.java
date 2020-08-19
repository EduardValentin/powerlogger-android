package com.eduardv.powerlogger.ui.userSettings;

import com.eduardv.powerlogger.lib.SelectableItem;

public enum Gender implements SelectableItem {
    FEMALE("Female"),
    MALE("Male");

    private final String genderName;

    Gender(String gender) {
        genderName = gender;
    }

    @Override
    public String getName() {
        return genderName;
    }
}
