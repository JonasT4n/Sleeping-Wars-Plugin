package com.joenastan.sleepingwar.plugin.game;

public enum CustomEntityStatus {
    DEFAULT("Default"),
    LOCKED("Locked");

    private String statusName;

    private CustomEntityStatus(String statusName) {
        this.statusName = statusName;
    }

    @Override
    public String toString() {
        return statusName;
    }

    public static CustomEntityStatus fromString(String s) {
        if (s.equalsIgnoreCase(LOCKED.toString())) {
            return LOCKED;
        } else {
            return DEFAULT;
        }
    }
}
