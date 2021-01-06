package com.joenastan.sleepingwars.events.CustomEvents;

import com.joenastan.sleepingwars.game.TeamGroupMaker;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

public class BedwarsBedDestroyEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player whoDestroy;
    private final TeamGroupMaker bedTeamDestroyed;
    private final TeamGroupMaker byTeam;

    public BedwarsBedDestroyEvent(Player whoDestroy, TeamGroupMaker bedTeamDestroyed, TeamGroupMaker byTeam) {
        this.whoDestroy = whoDestroy;
        this.bedTeamDestroyed = bedTeamDestroyed;
        this.byTeam = byTeam;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Nonnull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public Player byWho() {
        return whoDestroy;
    }

    public TeamGroupMaker getVictim() {
        return bedTeamDestroyed;
    }

    public TeamGroupMaker getTeamDestroyer() {
        return byTeam;
    }

}
