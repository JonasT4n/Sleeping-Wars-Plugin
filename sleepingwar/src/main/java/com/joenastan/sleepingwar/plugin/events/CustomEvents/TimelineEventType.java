package com.joenastan.sleepingwar.plugin.events.CustomEvents;

public enum TimelineEventType {
    DIAMOND_UPGRADE("diamond-up"),
    EMERALD_UPGRADE("emerald-up");

    private String eventName;

    private TimelineEventType(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public String toString() {
        return eventName;
    }

    public static TimelineEventType fromString(String e) {
        if (e.equalsIgnoreCase(EMERALD_UPGRADE.toString())) {
            return EMERALD_UPGRADE;
        } else if (e.equalsIgnoreCase(DIAMOND_UPGRADE.toString())) {
            return DIAMOND_UPGRADE;
        } else {
            return null;
        }
    }
}
