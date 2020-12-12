package com.joenastan.sleepingwars.timercoro;

import java.util.ArrayList;
import java.util.List;

import com.joenastan.sleepingwars.enumtypes.TimelineEventType;
import com.joenastan.sleepingwars.events.CustomEvents.BedwarsGameTimelineEvent;
import com.joenastan.sleepingwars.game.ResourceSpawner;
import com.joenastan.sleepingwars.game.SleepingRoom;
import com.joenastan.sleepingwars.game.TeamGroupMaker;
import com.joenastan.sleepingwars.utility.UsefulStaticFunctions;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Bed;

import java.util.List;

public class TimelineTimer extends StopwatchTimer {

    private final SleepingRoom room;
    private final BedwarsGameTimelineEvent event;
    private final List<ResourceSpawner> publicSpawners;
    private final List<Location> bedLocations = new ArrayList<>();

    public TimelineTimer(float duration, SleepingRoom room, BedwarsGameTimelineEvent event, List<ResourceSpawner> publicSpawners) {
        super(duration);
        this.room = room;
        this.event = event;
        this.publicSpawners = publicSpawners;
        for (TeamGroupMaker team : room.getTeams()) {
            Block c_bed = team.getTeamBedLocation().getBlock();
            if (UsefulStaticFunctions.isMaterialBed(c_bed.getType())) 
                bedLocations.add(team.getTeamBedLocation());
        }
    }

    @Override
    public void runEvent() {
        if (event.getEventType() == TimelineEventType.DIAMOND_UPGRADE || event.getEventType() == TimelineEventType.EMERALD_UPGRADE) {
            for (ResourceSpawner rspEntry : publicSpawners) {
                // Reduce about 25% amount of time
                // TODO: Make it Generic
                if (event.getEventType() == TimelineEventType.DIAMOND_UPGRADE && rspEntry.getMaterialSpawn() == Material.DIAMOND) {
                    rspEntry.setSpawnInterval(rspEntry.getSecondsPerSpawn() - (rspEntry.getSecondsPerSpawn() * 25/100));
                } else if (event.getEventType() == TimelineEventType.EMERALD_UPGRADE && rspEntry.getMaterialSpawn() == Material.EMERALD) {
                    rspEntry.setSpawnInterval(rspEntry.getSecondsPerSpawn() - (rspEntry.getSecondsPerSpawn() * 25/100));
                } else if (event.getEventType() == TimelineEventType.DESTROY_ALL_BED) {
                    for (int i = 0; i < bedLocations.size(); i++) {
                        Block c_bed = bedLocations.remove(0).getBlock();
                        if (UsefulStaticFunctions.isMaterialBed(c_bed.getType())) {
                            Bed c_bedOrigin = (Bed)c_bed.getBlockData();
                            Block c_headBed = c_bed.getRelative(c_bedOrigin.getFacing(), 1);
                            c_bed.setType(Material.AIR);
                            c_headBed.setType(Material.AIR);
                        }
                    }
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
