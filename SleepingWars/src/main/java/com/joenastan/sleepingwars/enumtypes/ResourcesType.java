package com.joenastan.sleepingwars.enumtypes;

public enum ResourcesType {
    IRON("iron"),
    GOLD("gold"),
    DIAMOND("diamond"),
    EMERALD("emerald");

    private final String resourceName;

    ResourcesType(String resourceName) {
        this.resourceName = resourceName;
    }

    public static ResourcesType fromString(String name) {
        if (name.equalsIgnoreCase("iron"))
            return IRON;
        else if (name.equalsIgnoreCase("gold"))
            return GOLD;
        else if (name.equalsIgnoreCase("diamond"))
            return DIAMOND;
        else if (name.equalsIgnoreCase("emerald"))
            return EMERALD;
        else
            return null;
    }

    @Override
    public String toString() {
        return resourceName;
    }
}