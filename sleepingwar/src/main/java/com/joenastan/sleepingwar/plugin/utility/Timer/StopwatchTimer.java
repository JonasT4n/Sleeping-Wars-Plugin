package com.joenastan.sleepingwar.plugin.utility.Timer;

import com.joenastan.sleepingwar.plugin.SleepingWarsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class StopwatchTimer {

    protected final JavaPlugin plugin = SleepingWarsPlugin.getPlugin();
    protected float counter;
    private float duration;
    protected int taskID = -1;

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

                counter -= 0.5f;
            }
        }, 0L, 10L);
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

    public float getDuration() {
        return duration;
    }

    protected void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    protected void runEvent() {
        // Do Nothing
    }

}
