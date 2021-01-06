package com.joenastan.sleepingwars.events.CustomEvents;

import com.joenastan.sleepingwars.game.SleepingRoom;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

public class BedwarsStartEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final SleepingRoom room;

    public BedwarsStartEvent(@Nonnull SleepingRoom room) {
        this.room = room;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Nonnull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public SleepingRoom getRoom() {
        return room;
    }

}
