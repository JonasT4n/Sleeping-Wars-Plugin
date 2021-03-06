package com.joenastan.sleepingwars.enumtypes;

public enum LockedEntityType {
    NORMAL_LOCK("normal-lock"),
    RESOURCE_SPAWNER_LOCK("rs-lock");

    private final String lockString;

    LockedEntityType(String lockString) {
        this.lockString = lockString;
    }

    public static LockedEntityType fromString(String s) {
        if (s.equalsIgnoreCase(NORMAL_LOCK.toString())) {
            return NORMAL_LOCK;
        } else if (s.equalsIgnoreCase(RESOURCE_SPAWNER_LOCK.toString())) {
            return RESOURCE_SPAWNER_LOCK;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return lockString;
    }

}
