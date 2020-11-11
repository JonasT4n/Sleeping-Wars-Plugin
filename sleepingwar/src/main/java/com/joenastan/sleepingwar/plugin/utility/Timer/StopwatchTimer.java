package com.joenastan.sleepingwar.plugin.utility.Timer;

import com.joenastan.sleepingwar.plugin.SleepingWarsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class StopwatchTimer {

    private final JavaPlugin plugin = SleepingWarsPlugin.getPlugin();
    protected float counter;
    private float duration;
    private int taskID = -1;

    public StopwatchTimer(float duration) {
        this.duration = duration;
        counter = duration;
    }

    public void start() {
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (counter <= 0f) {
                    runEvent();
                    stop();
                    return;
                }

                counter -= 0.05f;
            }
        }, 0L, 1L);
    }

    public void stop() {
        Bukkit.getScheduler().cancelTask(taskID);
    }

    public void reset() {
        if (taskID != -1) {
            stop();
        }

        counter = duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    protected void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    protected void runEvent() {
        // Do Nothing
    }

}
