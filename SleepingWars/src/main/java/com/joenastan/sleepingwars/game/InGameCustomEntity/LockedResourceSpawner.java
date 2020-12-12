package com.joenastan.sleepingwars.game.InGameCustomEntity;

import java.util.Map;

import com.joenastan.sleepingwars.enumtypes.LockedEntityType;
import com.joenastan.sleepingwars.enumtypes.ResourcesType;
import com.joenastan.sleepingwars.utility.CustomDerivedEntity.PlayerBedwarsEntity;
import com.joenastan.sleepingwars.timercoro.ResourceSpawnTimer;

import org.bukkit.Location;

public class LockedResourceSpawner extends LockedEntities {

    private final ResourceSpawnTimer rsTimer;

    public LockedResourceSpawner(Location locLocked, Map<ResourcesType, Integer> requirements, ResourceSpawnTimer rsTimer) {
        super(locLocked, requirements);
        this.rsTimer = rsTimer;
        typeLock = LockedEntityType.RESOURCE_SPAWNER_LOCK;
    }

    @Override
    public boolean unlockEntity(PlayerBedwarsEntity keyEntity) {
        if (isUnlocked())
            return true;
        if (super.unlockEntity(keyEntity)) {
            rsTimer.setLocked(false);
            rsTimer.start();
            return true;
        }
        return false;
    }

}
