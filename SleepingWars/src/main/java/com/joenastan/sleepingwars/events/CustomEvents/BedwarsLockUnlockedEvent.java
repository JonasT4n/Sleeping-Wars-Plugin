package com.joenastan.sleepingwars.events.CustomEvents;

import com.joenastan.sleepingwars.game.CustomEntity.LockedNormalEntity;
import com.joenastan.sleepingwars.utility.CustomEntity.PlayerBedwarsEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

public class BedwarsLockUnlockedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final LockedNormalEntity entity;
    private final PlayerBedwarsEntity playerEnt;

    public BedwarsLockUnlockedEvent(LockedNormalEntity entity, PlayerBedwarsEntity playerEnt) {
        this.entity = entity;
        this.playerEnt = playerEnt;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Nonnull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public PlayerBedwarsEntity getPlayerEnt() {
        return playerEnt;
    }

    public LockedNormalEntity getUnlockedEntity() {
        return entity;
    }
}
