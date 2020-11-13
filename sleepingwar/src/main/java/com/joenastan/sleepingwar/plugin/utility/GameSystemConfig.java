package com.joenastan.sleepingwar.plugin.utility;

import com.joenastan.sleepingwar.plugin.events.CustomEvents.BedwarsGameTimelineEvent;
import com.joenastan.sleepingwar.plugin.events.CustomEvents.TimelineEventType;
import com.joenastan.sleepingwar.plugin.game.ResourceSpawner;
import com.joenastan.sleepingwar.plugin.game.ResourcesType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameSystemConfig extends AbstractFile {

    private Map<String, Location> queueLocations = new HashMap<String, Location>();
    private Map<String, Map<String, Location>> inTeamGameSpawners = new HashMap<String, Map<String, Location>>();
    private Map<String, WorldCreator> worlds = new HashMap<String, WorldCreator>();
    private Map<String, Map<String, String>> teamPrefix = new HashMap<String, Map<String, String>>();
    private Map<String, Map<String, Map<String, ResourceSpawner>>> resourceSpawners = new HashMap<String, Map<String, Map<String, ResourceSpawner>>>();
    private Map<String, List<BedwarsGameTimelineEvent>> timelineEvent = new HashMap<String, List<BedwarsGameTimelineEvent>>();
    private Map<String, Map<String, Location>> teamBedLocations = new HashMap<String, Map<String, Location>>();

    public GameSystemConfig(Plugin main, String filename) {
        super(main, filename);
        load();
    }

    public Map<String, Location> getTeamSpawner(String worldKey) {
        if (inTeamGameSpawners.containsKey(worldKey))
            return inTeamGameSpawners.get(worldKey);
        return new HashMap<String, Location>();
    }

    public Location getQueueLocations(String worldName) {
        if (queueLocations.containsKey(worldName))
            return queueLocations.get(worldName);
        return null;
    }

    public Location getQueueLocations(String worldName, Location loc) {
        queueLocations.put(worldName, loc);
        return queueLocations.get(worldName);
    }

    public List<String> getAllTeamName(String worldName) {
        if (teamPrefix.containsKey(worldName)) {
            List<String> tNames = new ArrayList<String>();
            tNames.addAll(teamPrefix.get(worldName).keySet());
            return tNames;
        }
        return null;
    }

    public boolean deleteResourceSpawner(String world, String codename) {
        if (resourceSpawners.containsKey(world)) {
            for (Map.Entry<String, Map<String, ResourceSpawner>> collectionSpawner : resourceSpawners.get(world).entrySet()) {
                if (resourceSpawners.get(world).get(collectionSpawner.getKey()).containsKey(codename)) {
                    ResourceSpawner delSpawner = collectionSpawner.getValue().remove(codename);
                    if (delSpawner.isRunning()) {
                        delSpawner.isRunning(false);
                    }

                    if (collectionSpawner.getKey().equals("PUBLIC")) {
                        filecon.set("worlds." + world + ".public-resources-spawner." + codename, null);
                    } else {
                        filecon.set("worlds." + world + ".teams." + collectionSpawner.getKey() + ".resources", null);
                    }

                    return true;
                }
            }
        }
        return false;
    }

    public BedwarsGameTimelineEvent deleteTimelineEvent(String world, String eventName) {
        if (timelineEvent.containsKey(world)) {
            for (int i = 0; i < timelineEvent.get(world).size(); i++) {
                BedwarsGameTimelineEvent ev = timelineEvent.get(world).get(i);
                if (ev.getName().equalsIgnoreCase(eventName)) {
                    filecon.set("worlds." + world + ".timeline." + ev.getName(), null);
                    return timelineEvent.get(world).remove(i);
                }
            }
        }
        return null;
    }

    public Location getTeamBedLocation(String world, String teamName) {
        if (teamBedLocations.containsKey(world))
            if (teamBedLocations.get(world).containsKey(teamName))
                return teamBedLocations.get(world).get(teamName);
        return null;
    }

    public Map<String, Location> getBedLocationMap(String world) {
        if (teamBedLocations.containsKey(world))
            return teamBedLocations.get(world);
        return new HashMap<String, Location>();
    }

    public Map<String, ResourceSpawner> getResourceSpawnersPack(String world) {
        Map<String, ResourceSpawner> packs = new HashMap<String, ResourceSpawner>();
        if (resourceSpawners.containsKey(world)) {
            for (Map.Entry<String, Map<String, ResourceSpawner>> pack : resourceSpawners.get(world).entrySet()) {
                packs.putAll(pack.getValue());
            }
        }

        return packs;
    }

    public Map<String, ResourceSpawner> getResourceSpawnersPack(String world, String teamName) {
        if (resourceSpawners.containsKey(world))
            if (resourceSpawners.get(world).containsKey(teamName))
                return resourceSpawners.get(world).get(teamName);

        return new HashMap<String, ResourceSpawner>();
    }

    public Map<String, String> getTeamPrefix(String world) {
        if (teamPrefix.containsKey(world))
            return teamPrefix.get(world);
        return new HashMap<String, String>();
    }

    public List<String> getAllWorldName() {
        List<String> wnames = new ArrayList<String>();
        for (Map.Entry<String, WorldCreator> w : worlds.entrySet()) {
            wnames.add(w.getKey());
        }
        return wnames;
    }

    public List<BedwarsGameTimelineEvent> getTimelineEvents(String world) {
        if (timelineEvent.containsKey(world))
            return timelineEvent.get(world);
        return new ArrayList<BedwarsGameTimelineEvent>();
    }

    public void load() {
        if (!filecon.contains("worlds")) {
            filecon.set("worlds", worlds);
        } else {
            // Loop by World Name
            for (String key : filecon.getConfigurationSection("worlds").getKeys(false)) {
                WorldCreator creator = new WorldCreator(key).environment(Environment.values()[filecon.getInt("worlds." + key + ".env")])
                        .hardcore(filecon.getBoolean("worlds." + key + ".hardcore"))
                        .generateStructures(filecon.getBoolean("worlds." + key + ".structure"));
                World w = Bukkit.createWorld(creator);
                worlds.put(key, creator);

                Location thisWorldQueueLoc = new Location(w,
                        filecon.getInt("worlds." + key + ".queueloc.x"),
                        filecon.getInt("worlds." + key + ".queueloc.y"),
                        filecon.getInt("worlds." + key + ".queueloc.z"));
                queueLocations.put(key, thisWorldQueueLoc);

                // Loop by Team Name
                teamPrefix.put(key, new HashMap<String, String>());
                inTeamGameSpawners.put(key, new HashMap<String, Location>());
                teamBedLocations.put(key, new HashMap<String, Location>());
                Map<String, Map<String, ResourceSpawner>> teamResourceSpawnersLocal = new HashMap<String, Map<String, ResourceSpawner>>();
                if (filecon.getConfigurationSection("worlds." + key + ".teams") == null) {
                    filecon.createSection("worlds." + key + ".teams", new HashMap<>());
                    teamPrefix.put(key, new HashMap<String, String>());
                    teamPrefix.get(key).put("Blue", "blue");
                    teamPrefix.get(key).put("Yellow", "yellow");
                    teamPrefix.get(key).put("Red", "red");
                    teamPrefix.get(key).put("Green", "green");

                    // Default Team Spawner
                    inTeamGameSpawners.put(key, new HashMap<String, Location>());
                    for (String teamName : teamPrefix.get(key).keySet()) {
                        inTeamGameSpawners.get(key).put(teamName, w.getSpawnLocation());
                        resourceSpawners.get(key).put(teamName, new HashMap<String, ResourceSpawner>());
                        filecon.createSection("worlds." + key + ".teams." + teamName + ".resources", new HashMap<>());
                    }
                } else {
                    for (String tString : filecon.getConfigurationSection("worlds." + key + ".teams").getKeys(false)) {
                        String prefixColor;
                        if (filecon.getString("worlds." + key + ".teams." + tString + ".color") == null) {
                            prefixColor = "white";
                        } else {
                            prefixColor = filecon.getString("worlds." + key + ".teams." + tString + ".color");
                        }
                        teamPrefix.get(key).put(tString, prefixColor);
                        
                        // get team spawn location specifically
                        Location thisTeamSpawn;
                        if (filecon.getConfigurationSection("worlds." + key + ".teams." + tString + ".spawner") == null) {
                            filecon.createSection("worlds." + key + ".teams." + tString + ".spawner");
                            thisTeamSpawn = w.getSpawnLocation();
                        } else {
                            thisTeamSpawn = new Location(w, filecon.getInt("worlds." + key + ".teams." + tString + ".spawner.x"),
                                    filecon.getInt("worlds." + key + ".teams." + tString + ".spawner.y"),
                                    filecon.getInt("worlds." + key + ".teams." + tString + ".spawner.z"));
                        }
                        inTeamGameSpawners.get(key).put(tString, thisTeamSpawn);

                        // get bed location specifically
                        Location thisTeamBedLocation;
                        if (filecon.getConfigurationSection("worlds." + key + ".teams." + tString + ".bed-location") == null) {
                            filecon.createSection("worlds." + key + ".teams." + tString + ".bed-location");
                            thisTeamBedLocation = w.getSpawnLocation();
                        } else {
                            thisTeamBedLocation= new Location(w, 
                                    filecon.getInt("worlds." + key + ".teams." + tString + ".bed-location.x"), 
                                    filecon.getInt("worlds." + key + ".teams." + tString + ".bed-location.y"), 
                                    filecon.getInt("worlds." + key + ".teams." + tString + ".bed-location.z"));
                        }
                        teamBedLocations.get(key).put(tString, thisTeamBedLocation);
    
                        // Get all resource spawners from team
                        Map<String, ResourceSpawner> resourceSpawnersLocal = new HashMap<String, ResourceSpawner>();
                        if (filecon.getConfigurationSection("worlds." + key + ".teams." + tString + ".resources") == null) {
                            filecon.createSection("worlds." + key + ".teams." + tString + ".resources", new HashMap<>());
                        } else {
                            for (String rsName : filecon.getConfigurationSection("worlds." + key + ".teams." + tString + ".resources").getKeys(false)) {
                                Location rspawnerLoc = new Location(w, filecon.getInt("worlds." + key + ".teams." + tString + ".resources.spawnloc.x"),
                                        filecon.getInt("worlds." + key + ".teams." + tString + ".resources.spawnloc.y"),
                                        filecon.getInt("worlds." + key + ".teams." + tString + ".resources.spawnloc.z"));
                                ResourceSpawner rspawner = new ResourceSpawner(rsName, rspawnerLoc,
                                        ResourcesType.values()[filecon.getInt("worlds." + key + ".teams." + tString + ".resources.type")]);
                                resourceSpawnersLocal.put(rsName, rspawner);
                            }
                        }
                        teamResourceSpawnersLocal.put(tString, resourceSpawnersLocal);
                    }
                }
                
                // Get all public resource spawners
                // Get all resource spawners from team
                Map<String, ResourceSpawner> publicResourceSpawnersLocal = new HashMap<String, ResourceSpawner>();
                if (filecon.getConfigurationSection("worlds." + key + ".public-resources-spawner") == null) {
                    filecon.createSection("worlds." + key + ".public-resources-spawner", new HashMap<>());
                    resourceSpawners.get(key).put("PUBLIC", new HashMap<String, ResourceSpawner>());
                } else {
                    for (String rsName : filecon.getConfigurationSection("worlds." + key + ".public-resources-spawner").getKeys(false)) {
                        Location rspawnerLoc = new Location(w, filecon.getInt("worlds." + key + ".public-resources-spawner." + rsName + ".spawnloc.x"),
                                filecon.getInt("worlds." + key + ".public-resources-spawner." + rsName + ".spawnloc.y"),
                                filecon.getInt("worlds." + key + ".public-resources-spawner." + rsName + ".spawnloc.z"));
                        ResourceSpawner rspawner = new ResourceSpawner(rsName, rspawnerLoc,
                                ResourcesType.values()[filecon.getInt("worlds." + key + ".public-resources-spawner." + rsName + ".type")]);
                        publicResourceSpawnersLocal.put(rsName, rspawner);
                    }
                }
                teamResourceSpawnersLocal.put("PUBLIC", publicResourceSpawnersLocal);
                resourceSpawners.put(key, teamResourceSpawnersLocal);

                // Get all event list
                List<BedwarsGameTimelineEvent> configEvents = new ArrayList<BedwarsGameTimelineEvent>();
                if (filecon.getConfigurationSection("worlds." + key + ".timeline") == null) {
                    filecon.createSection("worlds." + key + ".timeline", new HashMap<>());
                } else {
                    for (String eventTimelineName : filecon.getConfigurationSection("worlds." + key + ".timeline").getKeys(false)) {
                        BedwarsGameTimelineEvent thisTimelineEvent = new BedwarsGameTimelineEvent(
                                TimelineEventType.fromString(filecon.getString("worlds." + key + ".timeline." + eventTimelineName + ".type")), 
                                (float)filecon.getInt("worlds." + key + ".timeline." + eventTimelineName + ".trigger-in-seconds"), eventTimelineName);
                        configEvents.add(thisTimelineEvent);
                    }
                }
                timelineEvent.put(key, configEvents);
            }
        }
    }

    public void defaultSystemConfig(WorldCreator creator, World worldSample) {
        String worldName = worldSample.getName();
        Location defaultSpawnLoc = worldSample.getSpawnLocation();
        worlds.put(worldName, creator);
        queueLocations.put(worldName, defaultSpawnLoc);
        resourceSpawners.put(worldName, new HashMap<String, Map<String, ResourceSpawner>>());

        // Default Color
        teamPrefix.put(worldName, new HashMap<String, String>());
        teamPrefix.get(worldName).put("Blue", "blue");
        teamPrefix.get(worldName).put("Yellow", "yellow");
        teamPrefix.get(worldName).put("Red", "red");
        teamPrefix.get(worldName).put("Green", "green");

        // Create Sections
        filecon.createSection("worlds." + worldName, new HashMap<>());
        filecon.createSection("worlds." + worldName + ".teams", new HashMap<>());
        filecon.createSection("worlds." + worldName + ".queueloc", new HashMap<>());
        filecon.createSection("worlds." + worldName + ".public-resources-spawner", new HashMap<>());
        filecon.createSection("worlds." + worldName + ".timeline", new HashMap<>());

        // Default Team Spawner
        inTeamGameSpawners.put(worldName, new HashMap<String, Location>());
        for (String teamName : teamPrefix.get(worldName).keySet()) {
            inTeamGameSpawners.get(worldName).put(teamName, defaultSpawnLoc);
            resourceSpawners.get(worldName).put(teamName, new HashMap<String, ResourceSpawner>());
            filecon.createSection("worlds." + worldName + ".teams." + teamName + ".resources", new HashMap<>());
            filecon.createSection("worlds." + worldName + ".teams." + teamName + ".bed-location", new HashMap<>());
        }
        resourceSpawners.get(worldName).put("PUBLIC", new HashMap<String, ResourceSpawner>());

    }

    @Override
    public void Save() {
        saveWorldCreator();
        saveWorldQueueSpawn();
        saveTimelineEvent();
        saveResourceSpawnersData();
        savePlayerPrefix();
        saveBedLocation();
        
        super.Save();
    }

    private void saveResourceSpawnersData() {
        for (Map.Entry<String, Map<String, Map<String, ResourceSpawner>>> entryRS : resourceSpawners.entrySet()) {
            // Resource Spawners
            for (Map.Entry<String, Map<String, ResourceSpawner>> resSp : resourceSpawners.get(entryRS.getKey()).entrySet()) {
                // Public resource spawners
                if (resSp.getKey().equals("PUBLIC")) {
                    for (Map.Entry<String, ResourceSpawner> publicSpawner : resSp.getValue().entrySet()) {
                        Location spawnLoc = publicSpawner.getValue().getSpawnLocation();
                        filecon.set("worlds." + entryRS.getKey() + ".public-resources-spawner." + publicSpawner.getKey() + ".type",
                                publicSpawner.getValue().getTypeResourceSpawner().ordinal());
                        filecon.set("worlds." + entryRS.getKey() + ".public-resources-spawner." + publicSpawner.getKey() + ".spawnloc.x",
                                spawnLoc.getBlockX());
                        filecon.set("worlds." + entryRS.getKey() + ".public-resources-spawner." + publicSpawner.getKey() + ".spawnloc.y",
                                spawnLoc.getBlockY());
                        filecon.set("worlds." + entryRS.getKey() + ".public-resources-spawner." + publicSpawner.getKey() + ".spawnloc.z",
                                spawnLoc.getBlockZ());
                    }
                }
                // Team owned resources spawner
                else {
                    for (Map.Entry<String, ResourceSpawner> publicSpawner : resSp.getValue().entrySet()) {
                        Location spawnLoc = publicSpawner.getValue().getSpawnLocation();
                        filecon.set("worlds." + entryRS.getKey() + ".teams." + resSp.getKey() + ".resources." + publicSpawner.getKey() + ".type",
                                publicSpawner.getValue().getTypeResourceSpawner().ordinal());
                        filecon.set("worlds." + entryRS.getKey() + ".teams." + resSp.getKey() + ".resources." + publicSpawner.getKey() + ".spawnloc.x",
                                spawnLoc.getBlockX());
                        filecon.set("worlds." + entryRS.getKey() + ".teams." + resSp.getKey() + ".resources." + publicSpawner.getKey() + ".spawnloc.y",
                                spawnLoc.getBlockY());
                        filecon.set("worlds." + entryRS.getKey() + ".teams." + resSp.getKey() + ".resources." + publicSpawner.getKey() + ".spawnloc.z",
                                spawnLoc.getBlockZ());
                    }
                }
            }
        }
    }

    private void saveWorldCreator() {
        // Save all made worlds
        for (Map.Entry<String, WorldCreator> entry : worlds.entrySet()) {
            filecon.set("worlds." + entry.getKey() + ".env", entry.getValue().environment().ordinal());
            filecon.set("worlds." + entry.getKey() + ".structure", entry.getValue().generateStructures());
            filecon.set("worlds." + entry.getKey() + ".hardcore", entry.getValue().hardcore());
        }
    }

    private void saveWorldQueueSpawn() {
        // Save All World queue spawn data
        for (Map.Entry<String, Location> entry : queueLocations.entrySet()) {
            filecon.set("worlds." + entry.getKey() + ".queueloc.x", entry.getValue().getBlockX());
            filecon.set("worlds." + entry.getKey() + ".queueloc.y", entry.getValue().getBlockY());
            filecon.set("worlds." + entry.getKey() + ".queueloc.z", entry.getValue().getBlockZ());
        }
    }

    private void savePlayerPrefix() {
        for (Map.Entry<String, Map<String, String>> wEntry : teamPrefix.entrySet()) {
            // Player prefix
            for (Map.Entry<String, String> prefix : teamPrefix.get(wEntry.getKey()).entrySet()) {
                filecon.set("worlds." + wEntry.getKey() + ".teams." + prefix.getKey() + ".color", prefix.getValue());
                filecon.set("worlds." + wEntry.getKey() + ".teams." + prefix.getKey() + ".spawner.x",
                        inTeamGameSpawners.get(wEntry.getKey()).get(prefix.getKey()).getBlockX());
                filecon.set("worlds." + wEntry.getKey() + ".teams." + prefix.getKey() + ".spawner.y",
                        inTeamGameSpawners.get(wEntry.getKey()).get(prefix.getKey()).getBlockY());
                filecon.set("worlds." + wEntry.getKey() + ".teams." + prefix.getKey() + ".spawner.z",
                        inTeamGameSpawners.get(wEntry.getKey()).get(prefix.getKey()).getBlockZ());
            }
        }
    }

    private void saveTimelineEvent() {
        // Saving Timeline
        for (Map.Entry<String, List<BedwarsGameTimelineEvent>> entryEventEntry : timelineEvent.entrySet()) {
            for (int i = 0; i < entryEventEntry.getValue().size(); i++) {
                BedwarsGameTimelineEvent ev = entryEventEntry.getValue().get(i);
                filecon.set("worlds." + entryEventEntry.getKey() + ".timeline." + ev.getName() + ".type", ev.getEventType().toString());
                filecon.set("worlds." + entryEventEntry.getKey() + ".timeline." + ev.getName() + ".trigger-in-seconds", ev.getTriggerSeconds());
            }
        }
    }

    private void saveBedLocation() {
        // Save all bed location
        for (Map.Entry<String, Map<String, Location>> bedLocEntry : teamBedLocations.entrySet()) {
            for (Map.Entry<String, Location> teamEntry : teamBedLocations.get(bedLocEntry.getKey()).entrySet()) {
                Location loc = teamEntry.getValue();
                filecon.set("worlds." + bedLocEntry.getKey() + ".teams." + teamEntry.getKey() + ".bed-location.x", loc.getBlockX());
                filecon.set("worlds." + bedLocEntry.getKey() + ".teams." + teamEntry.getKey() + ".bed-location.y", loc.getBlockY());
                filecon.set("worlds." + bedLocEntry.getKey() + ".teams." + teamEntry.getKey() + ".bed-location.z", loc.getBlockZ());
            }
        }
    }

}
