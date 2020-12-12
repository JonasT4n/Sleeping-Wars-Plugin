package com.joenastan.sleepingwars.events.CustomEvents;

import com.joenastan.sleepingwars.game.SleepingRoom;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BedwarsGameStartEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private SleepingRoom room;

    public BedwarsGameStartEvent(SleepingRoom room) {
        this.room = room;
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

}
