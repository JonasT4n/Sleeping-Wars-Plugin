package com.joenastan.sleepingwar.plugin.Events.CustomEvents;

import com.joenastan.sleepingwar.plugin.Game.SleepingRoom;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BedwarsGameEnded extends Event {

    private static final HandlerList handlers = new HandlerList();

    private SleepingRoom room;

    public BedwarsGameEnded(SleepingRoom room) {
        this.room = room;
    }

	@Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public SleepingRoom getRoom() {
        return room;
    }
    
}
