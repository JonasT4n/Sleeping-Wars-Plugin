package com.joenastan.sleepingwar.plugin.Events.CustomEvents;

import com.joenastan.sleepingwar.plugin.Game.SleepingRoom;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BedwarsGamePlayerJoin extends Event {
    
    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private SleepingRoom room;

    public BedwarsGamePlayerJoin(Player player, SleepingRoom room) {
        this.player = player;
        this.room = room;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public SleepingRoom getRoom() {
        return room;
    }

}
