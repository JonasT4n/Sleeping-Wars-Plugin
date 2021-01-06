package com.joenastan.sleepingwars.events.CustomEvents;

import com.joenastan.sleepingwars.game.SleepingRoom;
import com.joenastan.sleepingwars.game.TeamGroupMaker;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

public class BedwarsTNTExplodedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final SleepingRoom room;
    private final Player byPlayer;
    private final TeamGroupMaker ownedByTeam;

    public BedwarsTNTExplodedEvent(SleepingRoom room, Player byPlayer) {
        this.room = room;
        this.byPlayer = byPlayer;
        ownedByTeam = room.findTeam(byPlayer);
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

    public TeamGroupMaker byTeam() {
        return ownedByTeam;
    }

    public Player getPlayer() {
        return byPlayer;
    }
}
