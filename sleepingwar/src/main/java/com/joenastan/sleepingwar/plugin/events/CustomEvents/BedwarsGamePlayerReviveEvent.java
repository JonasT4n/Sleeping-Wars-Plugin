package com.joenastan.sleepingwar.plugin.events.CustomEvents;

import com.joenastan.sleepingwar.plugin.game.TeamGroupMaker;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BedwarsGamePlayerReviveEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private TeamGroupMaker team;

    public BedwarsGamePlayerReviveEvent(Player player, TeamGroupMaker team) {
        this.player = player;
        this.team = team;
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

    public TeamGroupMaker getTeam() {
        return team;
    }

}
