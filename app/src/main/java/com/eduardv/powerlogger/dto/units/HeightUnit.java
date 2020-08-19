package com.eduardv.powerlogger.dto.units;

import com.eduardv.powerlogger.lib.SelectableItem;

public enum HeightUnit implements SelectableItem {
    INCH("Inches", "in"),
    FEET("Feet", "ft"),
    CENTIMETERS("Centimeters", "cm");

    private String name;
    private String shortname;

    HeightUnit(String name, String shortname) {
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
