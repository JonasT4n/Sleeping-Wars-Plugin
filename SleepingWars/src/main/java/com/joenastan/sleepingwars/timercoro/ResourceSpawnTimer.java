package com.joenastan.sleepingwars.timercoro;

import com.joenastan.sleepingwars.game.ResourceSpawner;
import com.joenastan.sleepingwars.SleepingWarsPlugin;
import com.joenastan.sleepingwars.utility.Hologram.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class ResourceSpawnTimer extends StopwatchTimer {

    private final ResourceSpawner spawner;
    private boolean isLocked = false;
    private Hologram holoSign;

    public ResourceSpawnTimer(float interval, ResourceSpawner spawner) {
        super(interval);
        this.spawner = spawner;
    }

    @Override
    public void start() {
        if (isLocked)
            return;
        if (holoSign == null) {
            if (spawner.getCurrency() == Material.IRON_INGOT) {
                holoSign = new Hologram(spawner.getSpawnLocation(), ChatColor.GRAY + "Iron Spawner");
            } else if (spawner.getCurrency() == Material.GOLD_INGOT) {
                holoSign = new Hologram(spawner.getSpawnLocation(), ChatColor.GOLD + "Gold Spawner");
            } else if (spawner.getCurrency() == Material.DIAMOND) {
                holoSign = new Hologram(spawner.getSpawnLocation(), ChatColor.AQUA + "Diamond Spawner");
            } else if (spawner.getCurrency() == Material.EMERALD) {
                holoSign = new Hologram(spawner.getSpawnLocation(), ChatColor.GREEN + "Emerald Spawner");
            } else {
                holoSign = new Hologram(spawner.getSpawnLocation(), "Unknown Resource Spawner");
            }
            holoSign.addLine("Spawn in: ");
        }
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(SleepingWarsPlugin.getPlugin(),
                () -> {
                    if (counter <= 0f) {
                        runEvent();
                        return;
                    }
                    counter -= 0.05f;
                    holoSign.editLine(1, ChatColor.AQUA + String.format("Spawn in: %d",
                            (int)counter));
                }, 0L, 1L);
    }

    @Override
    protected void runEvent() {
        spawner.spawn();
        reset();
        start();
    }

    @Override
    public void stop() {
        super.stop();
        if (holoSign != null) {
            holoSign.clear();
            holoSign = null;
        }
    }

    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }
}
