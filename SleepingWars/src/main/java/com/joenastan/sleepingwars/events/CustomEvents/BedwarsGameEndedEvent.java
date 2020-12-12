package com.joenastan.sleepingwars.events.CustomEvents;

import com.joenastan.sleepingwars.game.SleepingRoom;
import com.joenastan.sleepingwars.game.TeamGroupMaker;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BedwarsGameEndedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private SleepingRoom room;
    private TeamGroupMaker winner;

    public BedwarsGameEndedEvent(SleepingRoom room, TeamGroupMaker winner) {
        this.room = room;
        this.winner = winner;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public SleepingRoom getRoom() {
        return room;
    }

    public TeamGroupMaker getWinnerTeam() {
        return winner;
    }

}
