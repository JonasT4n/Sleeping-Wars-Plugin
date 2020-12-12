package com.joenastan.sleepingwars.events.CustomEvents;

import javax.annotation.Nullable;

import com.joenastan.sleepingwars.game.SleepingRoom;
import com.joenastan.sleepingwars.game.TeamGroupMaker;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BedwarsGamePlayerDeathEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final SleepingRoom room;
    private final TeamGroupMaker team;
    private final Player killer;

    public BedwarsGamePlayerDeathEvent(Player player, SleepingRoom room, TeamGroupMaker team, @Nullable Player killer) {
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
