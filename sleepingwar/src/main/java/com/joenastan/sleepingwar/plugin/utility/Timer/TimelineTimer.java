package com.joenastan.sleepingwar.plugin.utility.Timer;

import com.joenastan.sleepingwar.plugin.events.CustomEvents.BedwarsGameTimelineEvent;
import com.joenastan.sleepingwar.plugin.game.SleepingRoom;

import org.bukkit.Bukkit;

import net.md_5.bungee.api.ChatColor;

public class TimelineTimer extends StopwatchTimer {

    private SleepingRoom room;
    private BedwarsGameTimelineEvent event;

    public TimelineTimer(float duration, SleepingRoom room, BedwarsGameTimelineEvent event) {
        super(duration);
        this.room = room;
        this.event = event;
    }
    
    @Override
    public void runEvent() {
        room.roomBroadcast(event.getName() + " [Event triggered]");
        Bukkit.getPluginManager().callEvent(event);
        room.timelineUpdate();
        System.out.println("[DEBUG] Event Timeline Triggered: " + event.getName());
        room.getScoreboard().resetScores(ChatColor.GRAY + event.getName());
    }

    public String getBedwarsEventName() {
        return event.getName();
    }

    public float getCounter() {
        return counter;
    }

}
