package com.joenastan.sleepingwars.events.CustomEvents;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.joenastan.sleepingwars.game.SleepingRoom;
import com.joenastan.sleepingwars.game.TeamGroupMaker;
import com.joenastan.sleepingwars.utility.CustomDerivedEntity.PlayerBedwarsEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BedwarsPlayerDeathEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final PlayerBedwarsEntity victim;
    private final SleepingRoom room;
    private final PlayerBedwarsEntity killer;

    public BedwarsPlayerDeathEvent(@Nonnull SleepingRoom room, @Nonnull PlayerBedwarsEntity victim,
                                   @Nullable PlayerBedwarsEntity killer) {
        this.room = room;
        this.victim = victim;
        this.killer = killer;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public PlayerBedwarsEntity getVictim() {
        return victim;
    }

    public SleepingRoom getRoom() {
        return room;
    }

    public PlayerBedwarsEntity getKiller() {
        return killer;
    }

}
