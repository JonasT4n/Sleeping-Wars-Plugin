package com.joenastan.sleepingwars.game;

import com.joenastan.sleepingwars.timercoro.ResourceSpawnTimer;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

public class ResourceSpawner {

    private final String codename;
    private final Location spawnLoc;
    private final ResourceSpawnTimer looper;

    private String ownedTeam = "UNKNOWN";
    private int spawnAmount;
    private Material currency;
    private boolean isActive;

    public ResourceSpawner(String codename, Location spawnLoc, Material currency) {
        this.spawnLoc = spawnLoc;
        this.codename = codename;
        isActive = false;
        spawnAmount = 1;
        // TODO: Set Default values from config
        this.currency = currency;
        switch (currency) {
            case GOLD_INGOT:
                looper = new ResourceSpawnTimer(4f, this);
                break;
            case DIAMOND:
                looper = new ResourceSpawnTimer(30f, this);
                break;
            case EMERALD:
                looper = new ResourceSpawnTimer(60f, this);
                break;
            default: // Default is IRON
                looper = new ResourceSpawnTimer(1f, this);
                break;
        }
    }

    public ResourceSpawner(String codename, Location spawnLoc, Material currency,
                           float durationPerSpawn) {
        this.spawnLoc = spawnLoc;
        this.codename = codename;
        isActive = false;
        spawnAmount = 1;
        this.currency = currency;

        // Create routine for spawner
        looper = new ResourceSpawnTimer(durationPerSpawn, this);
    }

    // Spawn the resource
    public void spawn() {
        World w = spawnLoc.getWorld();
        ItemStack res = new ItemStack(currency, spawnAmount);
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

    public boolean isRunning() {
        return isActive;
    }

    public void setOwner(String ownerTeam) {
        this.ownedTeam = ownerTeam;
    }

    public void setCurrency(Material mat) {
        this.currency = mat;
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

    public String getCodename() {
        return codename;
    }

    public Material getCurrency() {
        return currency;
    }

    public float getSecondsPerSpawn() {
        return looper.getDuration();
    }

    public String getOwner() {
        return ownedTeam;
    }

    public ResourceSpawnTimer getLooper() {
        return looper;
    }
}