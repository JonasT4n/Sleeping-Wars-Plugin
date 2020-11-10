package com.joenastan.sleepingwar.plugin.Events.CustomEvents;

import com.joenastan.sleepingwar.plugin.Game.SleepingRoom;
import com.joenastan.sleepingwar.plugin.Game.TeamGroupMaker;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BedwarsGamePlayerDeath extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private SleepingRoom room;
    private TeamGroupMaker team;

    public BedwarsGamePlayerDeath(Player player, SleepingRoom room, TeamGroupMaker team) {
        this.player = player;
        this.room = room;
        this.team = team;
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

    public TeamGroupMaker getTeam() {
        return team;
    }
    
}
