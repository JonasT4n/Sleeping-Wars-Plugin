package com.joenastan.sleepingwars.timercoro;

import com.joenastan.sleepingwars.SleepingWarsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class StopwatchTimer {

    protected final JavaPlugin plugin = SleepingWarsPlugin.getPlugin();
    protected float counter;
    protected int taskID = -1;
    private float duration;

    public StopwatchTimer(float duration) {
        setDuration(duration);
    }

    public void start() {
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (counter <= 0f) {
                runEvent();
                reset();
                return;
            }
            counter -= 0.5f;
        }, 0L, 10L);
    }

    public void stop() {
        Bukkit.getScheduler().cancelTask(taskID);
    }

    public void reset() {
        if (taskID != -1)
            stop();
        counter = duration;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
        counter = duration;
    }

    protected void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    protected abstract void runEvent();
}
