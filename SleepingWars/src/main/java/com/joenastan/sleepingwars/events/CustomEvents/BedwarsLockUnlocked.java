package com.joenastan.sleepingwars.events.CustomEvents;

import com.joenastan.sleepingwars.game.CustomEntity.LockedNormalEntity;
import com.joenastan.sleepingwars.utility.CustomDerivedEntity.PlayerBedwarsEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BedwarsLockUnlocked extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final LockedNormalEntity entity;
    private final PlayerBedwarsEntity playerEnt;

    public BedwarsLockUnlocked(LockedNormalEntity entity, PlayerBedwarsEntity playerEnt) {
        this.entity = entity;
        this.playerEnt = playerEnt;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

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
