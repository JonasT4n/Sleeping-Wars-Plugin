package com.joenastan.sleepingwar.plugin.events.CustomEvents;

import com.joenastan.sleepingwar.plugin.game.SleepingRoom;
import com.joenastan.sleepingwar.plugin.game.TeamGroupMaker;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BedwarsGamePlayerDeathEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private SleepingRoom room;
    private TeamGroupMaker team;
    private Player killer;

    public BedwarsGamePlayerDeathEvent(Player player, SleepingRoom room, TeamGroupMaker team, Player killer) {
        this.player = player;
        this.room = room;
        this.team = team;
        this.killer = killer;
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

    public TeamGroupMaker getTeam() {
        return team;
    }

    public Player getKiller() {
        return killer;
    }

}
