package com.joenastan.sleepingwars.timercoro;

import java.util.List;

import com.joenastan.sleepingwars.enumtypes.TimelineEventType;
import com.joenastan.sleepingwars.events.CustomEvents.BedwarsTimelineEvent;
import com.joenastan.sleepingwars.game.ResourceSpawner;
import com.joenastan.sleepingwars.game.SleepingRoom;
import com.joenastan.sleepingwars.game.TeamGroupMaker;
import com.joenastan.sleepingwars.utility.PluginStaticFunc;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;


public class TimelineTimer extends StopwatchTimer {

    private final SleepingRoom room;
    // TODO: Make it one object for all events
    private final List<BedwarsTimelineEvent> events;
    private final List<ResourceSpawner> publicSpawners;

    private int upcomingEventIndex = 0;
    private int shrunkBorderSize = 24;
    private boolean gameEventIsOnGoing = false;

    public TimelineTimer(SleepingRoom room, List<BedwarsTimelineEvent> events,
                         List<ResourceSpawner> publicSpawners) {
        super(0f);
        this.room = room;
        this.events = events;
        this.publicSpawners = publicSpawners;
    }

    @Override
    public void start() {
        if (gameEventIsOnGoing)
            return;
        if (events.size() > 0) {
            upcomingEventIndex = 0;
            setDuration(events.get(upcomingEventIndex).getSecTrigger());
            gameEventIsOnGoing = true;
            taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                if (counter <= 0f) {
                    runEvent();
                    upcomingEventIndex++;
                    if (upcomingEventIndex >= events.size())
                        stop();
                    else
                        setDuration(events.get(upcomingEventIndex).getSecTrigger());
                    return;
                }
                counter -= 0.5f;
            }, 0L, 10L);
        }
    }

    @Override
    public void stop() {
        gameEventIsOnGoing = false;
        Bukkit.getScheduler().cancelTask(taskID);
    }

    @Override
    public void runEvent() {
        BedwarsTimelineEvent ev = events.get(upcomingEventIndex);
        TimelineEventType eType = ev.getEventType();
        if (eType == TimelineEventType.DIAMOND_UPGRADE || eType == TimelineEventType.EMERALD_UPGRADE) {
            for (ResourceSpawner rspEntry : publicSpawners) {
                // Reduce about 25% amount of time
                // TODO: Make it Generic
                if (eType == TimelineEventType.DIAMOND_UPGRADE &&
                        rspEntry.getMaterialSpawn() == Material.DIAMOND) {
                    rspEntry.setSpawnInterval(rspEntry.getSecondsPerSpawn() -
                            (rspEntry.getSecondsPerSpawn() * 25/100));
                } else if (eType == TimelineEventType.EMERALD_UPGRADE &&
                        rspEntry.getMaterialSpawn() ==
                        Material.EMERALD) {
                    rspEntry.setSpawnInterval(rspEntry.getSecondsPerSpawn() -
                            (rspEntry.getSecondsPerSpawn() * 25/100));
                }
            }
        } else if (eType == TimelineEventType.DESTROY_ALL_BED) {
            for (TeamGroupMaker team : room.getTeams()) {
                Location bedLoc = team.getTeamBedLocation();
                if (PluginStaticFunc.isBed(bedLoc.getBlock().getType())) {
                    Block b = bedLoc.getBlock();
                    b.breakNaturally();
                }
            }
        } else if (eType == TimelineEventType.WORLD_SHRINKING) {
            WorldBorder border = room.getWorld().getWorldBorder();
            border.setSize(shrunkBorderSize, 180L);
        }
        // Remove score name from sidebar scoreboard
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> room.getScoreboard()
                .resetScores(String.format("%s     Next Event in [0:00] ", ChatColor.ITALIC + "")), 11L);
        room.getScoreboard().resetScores(ChatColor.GRAY + ev.getName());
        Bukkit.getPluginManager().callEvent(ev);
    }

    public float getCounter() {
        return counter;
    }

    public boolean isEventExceeded() {
        return upcomingEventIndex >= events.size();
    }

    public void setShrunkBorderSize(int size) {
        shrunkBorderSize = size;
    }

    public BedwarsTimelineEvent getNextEvent() {
        return events.get(upcomingEventIndex);
    }
}
