package com.joenastan.sleepingwar.plugin.game;

public enum ResourcesType {
    IRON("iron"),
    GOLD("gold"),
    DIAMOND("diamond"),
    EMERALD("emerald");

    private String resourceName;

    private ResourcesType(String resourceName) {
        this.resourceName = resourceName;
    }

    @Override
    public String toString() {
        return resourceName;
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
}