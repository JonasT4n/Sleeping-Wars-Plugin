package com.joenastan.sleepingwars.enumtypes;

public enum TimelineEventType {
    DIAMOND_UPGRADE("diamond-up"),
    EMERALD_UPGRADE("emerald-up"),
    DESTROY_ALL_BED("all-bed-destroy"),
    WORLD_SHRINKING("world-shrink"),
    BUFFER_ZONE_ACTIVE("buffer-zone-active");

    private final String eventName;

    private TimelineEventType(String eventName) {
        this.eventName = eventName;
    }

    public static TimelineEventType fromString(String e) {
        if (e.equalsIgnoreCase(EMERALD_UPGRADE.toString())) {
            return EMERALD_UPGRADE;
        } else if (e.equalsIgnoreCase(DIAMOND_UPGRADE.toString())) {
            return DIAMOND_UPGRADE;
        } else if (e.equalsIgnoreCase(DESTROY_ALL_BED.toString())) {
            return DESTROY_ALL_BED;
        } else if (e.equalsIgnoreCase(WORLD_SHRINKING.toString())) {
            return WORLD_SHRINKING;
        } else if (e.equalsIgnoreCase(BUFFER_ZONE_ACTIVE.toString())) {
            return BUFFER_ZONE_ACTIVE;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return eventName;
    }
}
