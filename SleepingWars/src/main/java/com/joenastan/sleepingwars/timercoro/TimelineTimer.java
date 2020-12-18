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
import org.bukkit.block.Block;


public class TimelineTimer extends StopwatchTimer {

    private final SleepingRoom room;
    private final BedwarsTimelineEvent event;
    private final List<ResourceSpawner> publicSpawners;

    public TimelineTimer(float duration, SleepingRoom room, BedwarsTimelineEvent event,
                         List<ResourceSpawner> publicSpawners) {
        super(duration);
        this.room = room;
        this.event = event;
        this.publicSpawners = publicSpawners;
    }

    @Override
    public void runEvent() {
        TimelineEventType eType = event.getEventType();
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
                if (PluginStaticFunc.isMaterialBed(bedLoc.getBlock().getType())) {
                    Block b = bedLoc.getBlock();
                    b.breakNaturally();
                }
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
