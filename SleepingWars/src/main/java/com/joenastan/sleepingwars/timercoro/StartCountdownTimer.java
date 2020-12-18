package com.joenastan.sleepingwars.timercoro;

import com.joenastan.sleepingwars.game.SleepingRoom;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

public class StartCountdownTimer extends StopwatchTimer {

    private final SleepingRoom room;

    public StartCountdownTimer(float duration, SleepingRoom room) {
        super(duration);
        this.room = room;
    }

    @Override
    public void start() {
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            room.getWorld().playSound(room.getQueueLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 2f);
            if (counter <= 0f) {
                runEvent();
                reset();
                return;
            }
            counter -= 1f;
            room.roomBroadcastTitle(ChatColor.RED + "Starting...",
                    String.format("In: %d", (int) counter), 0, 21, 0);
        }, 0L, 20L);
    }

    @Override
    protected void runEvent() {
        room.setStarting(false);
        room.gameStart();
    }

    public SleepingRoom getRoom() {
        return room;
    }
}
