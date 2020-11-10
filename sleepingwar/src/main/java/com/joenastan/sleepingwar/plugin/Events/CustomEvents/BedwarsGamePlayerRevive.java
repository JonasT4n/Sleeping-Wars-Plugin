package com.joenastan.sleepingwar.plugin.Events.CustomEvents;

import com.joenastan.sleepingwar.plugin.Game.TeamGroupMaker;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BedwarsGamePlayerRevive extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private TeamGroupMaker team;

    public BedwarsGamePlayerRevive(Player player, TeamGroupMaker team) {
        this.player = player;
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

    public TeamGroupMaker getTeam() {
        return team;
    }

}
