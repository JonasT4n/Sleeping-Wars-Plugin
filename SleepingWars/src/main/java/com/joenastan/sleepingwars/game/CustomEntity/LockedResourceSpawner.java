package com.joenastan.sleepingwars.game.CustomEntity;

import java.util.List;
import java.util.Map;

import com.joenastan.sleepingwars.enumtypes.LockedEntityType;
import com.joenastan.sleepingwars.game.ResourceSpawner;
import com.joenastan.sleepingwars.utility.CustomEntity.PlayerBedwarsEntity;

import org.bukkit.Location;
import org.bukkit.Material;

import javax.annotation.Nonnull;

public class LockedResourceSpawner extends LockedNormalEntity {

    private final List<ResourceSpawner> rSpawner;

    public LockedResourceSpawner(@Nonnull Location locLocked, Map<Material, Integer> requirements,
                                 List<ResourceSpawner> rSpawner) {
        super(locLocked, requirements);
        this.rSpawner = rSpawner;
        typeLock = LockedEntityType.RESOURCE_SPAWNER_LOCK;
    }

    @Override
    public boolean unlockEntity(PlayerBedwarsEntity keyEntity) {
        if (super.unlockEntity(keyEntity)) {
            for (ResourceSpawner rs : rSpawner) {
                rs.getLooper().setLocked(false);
                rs.getLooper().start();
            }
            return true;
        }
        return false;
    }
}
