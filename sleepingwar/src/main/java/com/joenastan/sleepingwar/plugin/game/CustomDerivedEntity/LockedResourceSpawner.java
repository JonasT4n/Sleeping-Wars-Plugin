package com.joenastan.sleepingwar.plugin.game.CustomDerivedEntity;

import java.util.Map;

import com.joenastan.sleepingwar.plugin.enumtypes.LockedEntityType;
import com.joenastan.sleepingwar.plugin.enumtypes.ResourcesType;
import com.joenastan.sleepingwar.plugin.utility.CustomDerivedEntity.PlayerBedwarsEntity;
import com.joenastan.sleepingwar.plugin.utility.Timer.ResourceSpawnTimer;

import org.bukkit.Location;

public class LockedResourceSpawner extends LockedEntities {

    private ResourceSpawnTimer rsTimer;

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
