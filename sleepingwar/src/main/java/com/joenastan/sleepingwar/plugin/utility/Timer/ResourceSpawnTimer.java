package com.joenastan.sleepingwar.plugin.utility.Timer;

import com.joenastan.sleepingwar.plugin.game.ResourceSpawner;
import com.joenastan.sleepingwar.plugin.SleepingWarsPlugin;
import org.bukkit.Bukkit;

public class ResourceSpawnTimer extends StopwatchTimer {

    private ResourceSpawner spawner;

    public ResourceSpawnTimer(float duration, ResourceSpawner spawner) {
        super(duration);
        this.spawner = spawner;
    }

    @Override
    public void start() {
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(SleepingWarsPlugin.getPlugin(), 
                new Runnable() {
                    @Override
                    public void run() {
                        if (counter <= 0f) {
                            runEvent();
                            return;
                        }

                        counter -= 0.05f;
                    }
                }, 0L, 1L);
    }

    @Override
    protected void runEvent() {
        spawner.spawn();
        //System.out.println("[DEBUG] Repeating spawn " + spawner.getCodename());
        reset();
        start();
    }
}
