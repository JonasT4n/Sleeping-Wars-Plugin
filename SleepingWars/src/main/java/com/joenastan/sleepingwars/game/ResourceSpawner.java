package com.joenastan.sleepingwars.game;

import com.joenastan.sleepingwars.enumtypes.ResourcesType;
import com.joenastan.sleepingwars.timercoro.ResourceSpawnTimer;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

public class ResourceSpawner {

    private final String codename;
    private final Location spawnLoc;
    private final ResourcesType typeSpawnResource;
    private final ResourceSpawnTimer looper;

    private String ownedTeam = "UNKNOWN";
    private int spawnAmount;
    private Material mat;
    private boolean isActive;

    public ResourceSpawner(String codename, Location spawnLoc, ResourcesType typeSpawnResource) {
        this.spawnLoc = spawnLoc;
        this.typeSpawnResource = typeSpawnResource;
        this.codename = codename;
        isActive = false;
        spawnAmount = 1;

        // Defaults values
        switch (typeSpawnResource) {
            case GOLD:
                mat = Material.GOLD_INGOT;
                looper = new ResourceSpawnTimer(4f, this);
                break;
            case DIAMOND:
                mat = Material.DIAMOND;
                looper = new ResourceSpawnTimer(30f, this);
                break;
            case EMERALD:
                mat = Material.EMERALD;
                looper = new ResourceSpawnTimer(60f, this);
                break;
            default: // Default is IRON
                mat = Material.IRON_INGOT;
                looper = new ResourceSpawnTimer(1f, this);
                break;
        }
    }

    public ResourceSpawner(String codename, Location spawnLoc, ResourcesType typeSpawnResource,
                           float durationPerSpawn) {
        this.spawnLoc = spawnLoc;
        this.typeSpawnResource = typeSpawnResource;
        this.codename = codename;
        isActive = false;
        spawnAmount = 1;
        // Defaults values
        switch (typeSpawnResource) {
            case GOLD:
                mat = Material.GOLD_INGOT;
                break;
            case DIAMOND:
                mat = Material.DIAMOND;
                break;
            case EMERALD:
                mat = Material.EMERALD;
                break;
            default: // Default is IRON
                mat = Material.IRON_INGOT;
                break;
        }
        // Create routine for spawner
        looper = new ResourceSpawnTimer(durationPerSpawn, this);
    }

    // Spawn the resource
    public void spawn() {
        World w = spawnLoc.getWorld();
        ItemStack res = new ItemStack(mat, spawnAmount);
        w.dropItemNaturally(spawnLoc, res);
    }

    public void setRunning(boolean run) {
        if (isActive == run)
            return;
        isActive = run;
        if (run) {
            looper.start();
        } else {
            looper.stop();
        }
    }

    public String getOwner() {
        return ownedTeam;
    }

    public void setOwner(String ownerTeam) {
        this.ownedTeam = ownerTeam;
    }

    public Material getMaterialSpawn() {
        return mat;
    }

    public void setMaterialSpawn(Material mat) {
        this.mat = mat;
    }

    public void setSpawnInterval(float seconds) {
        looper.setDuration(seconds);
    }

    public void setSpawnAmount(int amount) {
        spawnAmount = amount;
    }

    public Location getSpawnLocation() {
        return spawnLoc;
    }

    public ResourcesType getTypeSpawner() {
        return typeSpawnResource;
    }

    public String getCodename() {
        return codename;
    }

    public boolean isRunning() {
        return isActive;
    }

    public float getSecondsPerSpawn() {
        return looper.getDuration();
    }

    public ResourceSpawnTimer getCoroutine() {
        return looper;
    }
}