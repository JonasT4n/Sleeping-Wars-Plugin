package com.joenastan.sleepingwars.enumtypes;

public enum GameCommandType {
    JOIN("join-game"),
    LEAVE("leave-game");

    private final String cmdString;

    GameCommandType(String lockString) {
        this.cmdString = lockString;
    }

    @Override
    public String toString() {
        return cmdString;
    }
}
