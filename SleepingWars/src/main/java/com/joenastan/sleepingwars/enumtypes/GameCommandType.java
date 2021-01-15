package com.joenastan.sleepingwars.enumtypes;

public enum GameCommandType {
    JOIN("join-game"),
    LEAVE("leave-game"),
    START("start-game");

    private final String cmdString;

    GameCommandType(String cmdString) {
        this.cmdString = cmdString;
    }

    @Override
    public String toString() {
        return cmdString;
    }

    public static GameCommandType fromString(String s) {
        if (s.equalsIgnoreCase(GameCommandType.JOIN.toString())) {
            return GameCommandType.JOIN;
        } else if (s.equalsIgnoreCase(GameCommandType.LEAVE.toString())) {
            return GameCommandType.LEAVE;
        } else if (s.equalsIgnoreCase(GameCommandType.START.toString())) {
            return GameCommandType.START;
        } else {
            return null;
        }
    }
}
