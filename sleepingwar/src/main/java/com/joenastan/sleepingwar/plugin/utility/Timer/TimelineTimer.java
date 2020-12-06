package com.joenastan.sleepingwar.plugin.utility.Timer;

import com.joenastan.sleepingwar.plugin.enumtypes.TimelineEventType;
import com.joenastan.sleepingwar.plugin.events.CustomEvents.BedwarsGameTimelineEvent;
import com.joenastan.sleepingwar.plugin.game.ResourceSpawner;
import com.joenastan.sleepingwar.plugin.game.SleepingRoom;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.List;

public class TimelineTimer extends StopwatchTimer {

    private SleepingRoom room;
    private BedwarsGameTimelineEvent event;
    private List<ResourceSpawner> publicSpawners;

    public TimelineTimer(float duration, SleepingRoom room, BedwarsGameTimelineEvent event, List<ResourceSpawner> publicSpawners) {
        super(duration);
        this.room = room;
        this.event = event;
        this.publicSpawners = publicSpawners;
    }

    @Override
    public void runEvent() {
        if (event.getEventType() == TimelineEventType.DIAMOND_UPGRADE || event.getEventType() == TimelineEventType.EMERALD_UPGRADE) {
            for (ResourceSpawner rspEntry : publicSpawners) {
                // Reduce about 25% amount of time
                // TODO: Make it Generic
                if (event.getEventType() == TimelineEventType.DIAMOND_UPGRADE && rspEntry.getMaterialSpawn() == Material.DIAMOND)
                    rspEntry.setSpawnInterval(rspEntry.getSecondsPerSpawn() - (rspEntry.getSecondsPerSpawn() * 25 / 100));
                if (event.getEventType() == TimelineEventType.EMERALD_UPGRADE && rspEntry.getMaterialSpawn() == Material.EMERALD)
                    rspEntry.setSpawnInterval(rspEntry.getSecondsPerSpawn() - (rspEntry.getSecondsPerSpawn() * 25 / 100));
            }
        }
        // Update timeline
        room.gotoNextTimelineEvent();
        room.getScoreboard().resetScores(String.format("%s     Next Event in [0:00] ", ChatColor.ITALIC + ""));
        room.getScoreboard().resetScores(ChatColor.GRAY + event.getName());
        Bukkit.getPluginManager().callEvent(event);
    }

    public String getBedwarsEventName() {
        return event.getName();
    }

    public float getCounter() {
        return counter;
    }

}
