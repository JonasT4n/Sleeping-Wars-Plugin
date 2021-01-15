package com.joenastan.sleepingwars.enumtypes;

public enum InGameFlags {
    ARMOR_RESTRICTION("armor-restrict");

    private final String flagString;

    InGameFlags(String flagString) {
        this.flagString = flagString;
    }

    @Override
    public String toString() {
        return flagString;
    }

    public static InGameFlags fromString(String s) {
        if (s.equalsIgnoreCase(InGameFlags.ARMOR_RESTRICTION.toString())) {
            return InGameFlags.ARMOR_RESTRICTION;
        } else {
            return null;
        }
    }
}
