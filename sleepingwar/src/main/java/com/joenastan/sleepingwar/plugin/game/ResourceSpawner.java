package com.joenastan.sleepingwar.plugin.game;

import com.joenastan.sleepingwar.plugin.utility.Timer.ResourceSpawnTimer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

public class ResourceSpawner {

    private String ownedTeam = "UNKNOWN";
    private String codename;
    private int spawnAmount;
    private Location spawnLoc;
    private ResourcesType typeSpawnResource;
    private ResourceSpawnTimer looper;
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
                looper = new ResourceSpawnTimer(10f, this);
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
                looper = new ResourceSpawnTimer(2.5f, this);
                break;
        }
    }

    // Spawn the resource
    public void spawn() {
        World w = spawnLoc.getWorld();
        ItemStack res = new ItemStack(mat, spawnAmount);
        w.dropItemNaturally(spawnLoc, res);
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

    public ResourcesType getTypeResourceSpawner() {
        return typeSpawnResource;
    }

    public String getCodename() {
        return codename;
    }

    public boolean isRunning() {
        return isActive;
    }

    private void setRunning(boolean run) {
        if (run) {
            System.out.println("[DEBUG] Resource Spawner " + codename + " is now running");
            looper.start();
        } else {
            System.out.println("[DEBUG] Resource Spawner " + codename + " is now stopped");
            looper.stop();
        }
    }

    public boolean isRunning(boolean active) {
        if (isActive == active)
            return isActive;

        isActive = active;
        setRunning(isActive);
        return isActive;
    }
}