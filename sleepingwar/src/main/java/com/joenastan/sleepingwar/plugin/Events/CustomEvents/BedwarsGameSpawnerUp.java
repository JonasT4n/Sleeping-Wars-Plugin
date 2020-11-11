package com.joenastan.sleepingwar.plugin.Events.CustomEvents;

import com.joenastan.sleepingwar.plugin.Game.SleepingRoom;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BedwarsGameSpawnerUp extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private SleepingRoom room;

    public BedwarsGameSpawnerUp(Player player, SleepingRoom room) {
        this.player = player;
        this.room = room;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public SleepingRoom getRoom() {
        return room;
    }

}
