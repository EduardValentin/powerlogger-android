package com.eduardv.powerlogger.ui.more;

import java.util.Optional;

public enum MoreItemType {
    PROFILE("My Profile"),
    UNITS("Units"),
    LOG_OUT("Log Out"),
    CREDITS("Credits");

    private final String name;

    MoreItemType(String displayName) {
        this.name = displayName;
    }

    public String getName() {
        return name;
    }

    public static Optional<MoreItemType> fromString(String text) {
        for (MoreItemType b : MoreItemType.values()) {
            if (b.getName().equalsIgnoreCase(text)) {
                return Optional.of(b);
            }
        }
        return Optional.empty();
    }
}
