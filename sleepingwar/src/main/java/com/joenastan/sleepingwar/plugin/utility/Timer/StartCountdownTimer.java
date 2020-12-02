package com.joenastan.sleepingwar.plugin.utility.Timer;

import com.joenastan.sleepingwar.plugin.game.SleepingRoom;

import org.bukkit.Bukkit;

import net.md_5.bungee.api.ChatColor;

public class StartCountdownTimer extends StopwatchTimer {

    private SleepingRoom room;
    
    public StartCountdownTimer(float duration, SleepingRoom room) {
        super(duration);
        this.room = room;
    }

    @Override
    public void start() {
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (counter <= 0f) {
                    runEvent();
                    reset();
                    return;
                }
                counter -= 1f;
                room.roomBroadcastTitle(ChatColor.RED + "Starting...", "In " + ChatColor.LIGHT_PURPLE + "", 0, 21, 0);
            }
        }, 0L, 20L);
    }
    
    @Override
    protected void runEvent() {
        room.gameStart();
    }

    public SleepingRoom getRoom() {
        return room;
    }

}
