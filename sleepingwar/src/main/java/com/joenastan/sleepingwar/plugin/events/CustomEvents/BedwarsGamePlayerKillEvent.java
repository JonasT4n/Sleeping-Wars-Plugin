package com.joenastan.sleepingwar.plugin.events.CustomEvents;

import com.joenastan.sleepingwar.plugin.game.SleepingRoom;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BedwarsGamePlayerKillEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    
    private SleepingRoom room;
    private Player killed;
    private Player killer;

    public BedwarsGamePlayerKillEvent(SleepingRoom room, Player killed, Player killer) {
        this.room = room;
        this.killed = killed;
        this.killer = killer;
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

    public Player getKiller() {
        return killer;
    }

    public Player getWho() {
        return killed;
    }

}
