package com.joenastan.sleepingwars.game.CustomEntity;

import java.util.List;
import java.util.Map;

import com.joenastan.sleepingwars.enumtypes.LockedEntityType;
import com.joenastan.sleepingwars.enumtypes.ResourcesType;
import com.joenastan.sleepingwars.game.ResourceSpawner;
import com.joenastan.sleepingwars.utility.CustomDerivedEntity.PlayerBedwarsEntity;

import org.bukkit.Location;

import javax.annotation.Nonnull;

public class LockedResourceSpawner extends LockedNormalEntity {

    private final List<ResourceSpawner> rSpawner;

    public LockedResourceSpawner(@Nonnull Location locLocked, Map<ResourcesType, Integer> requirements,
                                 List<ResourceSpawner> rSpawner) {
        super(locLocked, requirements);
        this.rSpawner = rSpawner;
        typeLock = LockedEntityType.RESOURCE_SPAWNER_LOCK;
    }

    @Override
    public boolean unlockEntity(PlayerBedwarsEntity keyEntity) {
        System.out.println("[DEBUG] Unlocking...");
        if (super.unlockEntity(keyEntity)) {
            for (ResourceSpawner rs : rSpawner) {
                rs.getCoroutine().setLocked(false);
                rs.getCoroutine().start();
                System.out.println("[DEBUG] Resource Spawner Unlocked! " + rs.getCodename());
            }
            return true;
        }
        return false;
    }
}
