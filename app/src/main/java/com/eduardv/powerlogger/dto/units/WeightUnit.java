package com.eduardv.powerlogger.dto.units;

import com.eduardv.powerlogger.lib.SelectableItem;

public enum WeightUnit implements SelectableItem {
    KILOGRAM("Kilogram", "kg"),
    POUND("Pound", "lbs");

    private String name;
    private String shortname;

    WeightUnit(String name, String shortname) {
        this.name = name;
        this.shortname = shortname;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getShortname() {
        return shortname;
    }
}
