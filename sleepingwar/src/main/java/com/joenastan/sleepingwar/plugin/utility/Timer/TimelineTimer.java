package com.joenastan.sleepingwar.plugin.utility.Timer;

import com.joenastan.sleepingwar.plugin.events.CustomEvents.BedwarsGameTimelineEvent;
import com.joenastan.sleepingwar.plugin.game.SleepingRoom;

import org.bukkit.Bukkit;

public class TimelineTimer extends StopwatchTimer {

    private SleepingRoom room;
    private BedwarsGameTimelineEvent event;

    public TimelineTimer(int duration, SleepingRoom room, BedwarsGameTimelineEvent event) {
        super(duration);
        this.room = room;
        this.event = event;
    }
    
    @Override
    public void runEvent() {
        Bukkit.getPluginManager().callEvent(event);
        room.timelineUpdate();
    }

    public String getBedwarsEventName() {
        return event.getName();
    }

    public float getCounter() {
        return counter;
    }

}
