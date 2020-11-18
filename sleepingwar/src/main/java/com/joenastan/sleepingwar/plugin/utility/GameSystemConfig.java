package com.joenastan.sleepingwar.plugin.utility;

import com.joenastan.sleepingwar.plugin.events.CustomEvents.BedwarsGameTimelineEvent;
import com.joenastan.sleepingwar.plugin.events.CustomEvents.TimelineEventType;
import com.joenastan.sleepingwar.plugin.game.BedwarsShopType;
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
    private Map<String, Map<BedwarsShopType, List<Location>>> shopsLocation = new HashMap<String, Map<BedwarsShopType, List<Location>>>();

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

    public Map<BedwarsShopType, List<Location>> getShopLocations(String world) {
        if (shopsLocation.containsKey(world)) 
            return shopsLocation.get(world);
        return new HashMap<BedwarsShopType, List<Location>>();
    }

    public boolean deleteResourceSpawner(String world, String team, String codename) {
        if (resourceSpawners.containsKey(world)) {
            String resourceSpawnerPath = String.format("worlds.%s.resource-spawners", world);
            if (resourceSpawners.get(world).containsKey(team)) {
                resourceSpawnerPath += "." + team;
                if(resourceSpawners.get(world).get(team).keySet().contains(codename)) {
                    ResourceSpawner delSpawner = resourceSpawners.get(world).get(team).remove(codename);
                    if (delSpawner.isRunning())
                        delSpawner.isRunning(false);
                    filecon.set(resourceSpawnerPath + "." + codename, null);
                    Save();
                    return true;
                }
            } else {
                resourceSpawnerPath += ".PUBLIC";
                if (resourceSpawners.get(world).get("PUBLIC").keySet().contains(codename)) {
                    ResourceSpawner delSpawner = resourceSpawners.get(world).get("PUBLIC").remove(codename);
                    if (delSpawner.isRunning())
                        delSpawner.isRunning(false);
                    filecon.set(resourceSpawnerPath + "." + codename, null);
                    Save();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean deleteShopLocation(String world, BedwarsShopType type, int index) {
        if (shopsLocation.containsKey(world)) {
            if (shopsLocation.get(world).containsKey(type)) {
                if (index < shopsLocation.get(world).get(type).size()) {
                    shopsLocation.get(world).get(type).remove(index);
                    filecon.set("worlds." + world + ".shop-location", new HashMap<>());
                    saveShopLocation();
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

    public Map<String, ResourceSpawner> getAllResourceSpawnersPack(String world) {
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

    public void addResourceSpawner(String world, String team, ResourceSpawner spawner) {
        if (resourceSpawners.containsKey(world)) {
            if (getTeamPrefix(world).containsKey(team)) {
                if (!resourceSpawners.get(world).containsKey(team))
                    resourceSpawners.get(world).put(team, new HashMap<String, ResourceSpawner>());
                resourceSpawners.get(world).get(team).put(spawner.getCodename(), spawner);
            } else {
                if (!resourceSpawners.get(world).containsKey("PUBLIC"))
                    resourceSpawners.get(world).put(team, new HashMap<String, ResourceSpawner>());
                resourceSpawners.get(world).get("PUBLIC").put(spawner.getCodename(), spawner);
            }
        }
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
                // load world creator
                WorldCreator creator = new WorldCreator(key).environment(Environment.values()[filecon.getInt("worlds." + key + ".env")])
                        .hardcore(filecon.getBoolean("worlds." + key + ".hardcore"))
                        .generateStructures(filecon.getBoolean("worlds." + key + ".structure"));
                World w = Bukkit.createWorld(creator);
                worlds.put(key, creator);

                // load world queue location
                Location thisWorldQueueLoc = new Location(w, filecon.getDouble(String.format("worlds.%s.queueloc.x", key)),
                        filecon.getDouble(String.format("worlds.%s.queueloc.y", key)),
                        filecon.getDouble(String.format("worlds.%s.queueloc.z", key)));
                queueLocations.put(key, thisWorldQueueLoc);

                // Loop by Team Name
                teamPrefix.put(key, new HashMap<String, String>());
                inTeamGameSpawners.put(key, new HashMap<String, Location>());
                teamBedLocations.put(key, new HashMap<String, Location>());
                String teamPathName = String.format("worlds.%s.teams", key);
                if (filecon.getConfigurationSection(teamPathName) == null) {
                    filecon.createSection(teamPathName, new HashMap<>());
                    teamPrefix.put(key, new HashMap<String, String>());
                    teamPrefix.get(key).put("Blue", "blue");
                    teamPrefix.get(key).put("Yellow", "yellow");
                    teamPrefix.get(key).put("Red", "red");
                    teamPrefix.get(key).put("Green", "green");

                    // Default Team Spawner
                    inTeamGameSpawners.put(key, new HashMap<String, Location>());
                    for (String teamName : teamPrefix.get(key).keySet())
                        inTeamGameSpawners.get(key).put(teamName, w.getSpawnLocation());
                } else {
                    for (String tString : filecon.getConfigurationSection(teamPathName).getKeys(false)) {
                        String prefixColor;
                        if (filecon.getString(String.format("%s.%s.color", teamPathName, tString)) == null) {
                            prefixColor = "white";
                        } else {
                            prefixColor = filecon.getString(String.format("%s.%s.color", teamPathName, tString));
                        }
                        teamPrefix.get(key).put(tString, prefixColor);
                        
                        // get team spawn location specifically
                        Location thisTeamSpawn;
                        if (filecon.getConfigurationSection(String.format("%s.%s.spawner", teamPathName, tString)) == null) {
                            filecon.createSection(String.format("%s.%s.spawner", teamPathName, tString));
                            thisTeamSpawn = w.getSpawnLocation();
                        } else {
                            thisTeamSpawn = new Location(w, filecon.getDouble(String.format("%s.%s.spawner.x", teamPathName, tString)),
                                    filecon.getDouble(String.format("%s.%s.spawner.y", teamPathName, tString)),
                                    filecon.getDouble(String.format("%s.%s.spawner.z", teamPathName, tString)));
                        }
                        inTeamGameSpawners.get(key).put(tString, thisTeamSpawn);

                        // get bed location specifically
                        Location thisTeamBedLocation;
                        if (filecon.getConfigurationSection(String.format("%s.%s.bed-location", teamPathName, tString)) == null) {
                            filecon.createSection(String.format("%s.%s.bed-location", teamPathName, tString));
                            thisTeamBedLocation = w.getSpawnLocation();
                        } else {
                            thisTeamBedLocation= new Location(w, filecon.getDouble(String.format("%s.%s.bed-location.x", teamPathName, tString)), 
                                    filecon.getDouble(String.format("%s.%s.bed-location.y", teamPathName, tString)), 
                                    filecon.getDouble(String.format("%s.%s.bed-location.z", teamPathName, tString)));
                        }
                        teamBedLocations.get(key).put(tString, thisTeamBedLocation);
                    }
                }
                
                // Get all resource spawners
                Map<String, Map<String, ResourceSpawner>> catResSpawner = new HashMap<String, Map<String, ResourceSpawner>>();
                String resourceSpawnerPath = String.format("worlds.%s.resource-spawners", key);
                for (String teamName : teamPrefix.get(key).keySet()) {
                    if (filecon.getConfigurationSection(resourceSpawnerPath + "." + teamName) == null) {
                        filecon.createSection(resourceSpawnerPath + "." + teamName, new HashMap<>());
                        resourceSpawners.get(key).put(teamName, new HashMap<String, ResourceSpawner>());
                    }
                }
                resourceSpawners.put(key, catResSpawner);
                if (filecon.getConfigurationSection(resourceSpawnerPath + ".PUBLIC") == null) {
                    filecon.createSection(resourceSpawnerPath, new HashMap<>());
                    resourceSpawners.put(key, new HashMap<String, Map<String, ResourceSpawner>>());
                    filecon.createSection(resourceSpawnerPath + ".PUBLIC", new HashMap<>());
                    resourceSpawners.get(key).put("PUBLIC", new HashMap<String, ResourceSpawner>());
                } else {
                    for (String rsTeamName : filecon.getConfigurationSection(resourceSpawnerPath).getKeys(false)) {
                        String teamName = rsTeamName;
                        if (!teamPrefix.get(key).keySet().contains(rsTeamName))
                            teamName = "PUBLIC";

                        Map<String, ResourceSpawner> localResourceSpawner = new HashMap<String, ResourceSpawner>();
                        for (String rsName : filecon.getConfigurationSection(String.format("%s.%s", resourceSpawnerPath, teamName)).getKeys(false)) {
                            Location rspawnerLoc = new Location(w, filecon.getDouble(String.format("%s.%s.%s.spawnloc.x", resourceSpawnerPath, teamName, rsName)),
                                    filecon.getDouble(String.format("%s.%s.%s.spawnloc.y", resourceSpawnerPath, teamName, rsName)),
                                    filecon.getDouble(String.format("%s.%s.%s.spawnloc.z", resourceSpawnerPath, teamName, rsName)));
                            ResourceSpawner rspawner = new ResourceSpawner(rsName, rspawnerLoc,
                                    ResourcesType.values()[filecon.getInt(String.format("%s.%s.%s.type", resourceSpawnerPath, teamName, rsName))],
                                    (float)filecon.getDouble(String.format("%s.%s.%s.duration-spawn", resourceSpawnerPath, teamName, rsName)));
                            rspawner.setOwner(teamName);
                            localResourceSpawner.put(rsName, rspawner);
                        }

                        if (teamName.equals("PUBLIC")) {
                            if (!catResSpawner.containsKey("PUBLIC")) {
                                catResSpawner.put("PUBLIC", localResourceSpawner);
                            } else {
                                for (Map.Entry<String, ResourceSpawner> localResSpawnerEntry : localResourceSpawner.entrySet()) {
                                    catResSpawner.get("PUBLIC").put(localResSpawnerEntry.getKey(), localResSpawnerEntry.getValue());
                                }
                            }

                            // Unset the unknown resource spawner
                            if (!rsTeamName.equals("PUBLIC"))
                                filecon.set(resourceSpawnerPath + rsTeamName, null);
                        } else {
                            catResSpawner.put(teamName, localResourceSpawner);
                        }
                    }
                }

                // Get all event list
                List<BedwarsGameTimelineEvent> configEvents = new ArrayList<BedwarsGameTimelineEvent>();
                String timelinePathName = String.format("worlds.%s.timeline", key);
                if (filecon.getConfigurationSection(timelinePathName) == null) {
                    filecon.createSection(timelinePathName, new HashMap<>());
                } else {
                    for (String eventTimelineName : filecon.getConfigurationSection(timelinePathName).getKeys(false)) {
                        BedwarsGameTimelineEvent thisTimelineEvent = new BedwarsGameTimelineEvent(
                                TimelineEventType.fromString(filecon.getString(String.format("%s.%s.type", timelinePathName, eventTimelineName))), 
                                (float)filecon.getInt(String.format("%s.%s.trigger-in-seconds", timelinePathName, eventTimelineName)), eventTimelineName);
                        configEvents.add(thisTimelineEvent);
                    }
                }
                timelineEvent.put(key, configEvents);

                // Get all shop locations
                Map<BedwarsShopType, List<Location>> loadShopLocations = new HashMap<BedwarsShopType, List<Location>>();
                String shopLocPathName = String.format("worlds.%s.shop-location", key);
                if (filecon.getConfigurationSection(shopLocPathName) == null) {
                    filecon.createSection(shopLocPathName, new HashMap<>());
                    filecon.createSection(shopLocPathName + BedwarsShopType.ITEMS_SHOP.toString(), new HashMap<>());
                    filecon.createSection(shopLocPathName + BedwarsShopType.PERMA_SHOP.toString(), new HashMap<>());

                    loadShopLocations.put(BedwarsShopType.ITEMS_SHOP, new ArrayList<Location>());
                    loadShopLocations.put(BedwarsShopType.PERMA_SHOP, new ArrayList<Location>());
                } else {
                    for (String shopTypeString : filecon.getConfigurationSection(shopLocPathName).getKeys(false)) {
                        List<Location> listShopLocations = new ArrayList<Location>();
                        BedwarsShopType st = BedwarsShopType.fromString(shopTypeString);
                        if (st != null) {
                            for (String shopLoc : filecon.getConfigurationSection(String.format("%s.%s", shopLocPathName, st.toString())).getKeys(false)) {
                                Location loc = new Location(w, filecon.getDouble(String.format("%s.%s.%s.x", shopLocPathName, st.toString(), shopLoc)), 
                                        filecon.getDouble(String.format("%s.%s.%s.y", shopLocPathName, st.toString(), shopLoc)), 
                                        filecon.getDouble(String.format("%s.%s.%s.z", shopLocPathName, st.toString(), shopLoc)));
                                listShopLocations.add(loc);
                            }
                            loadShopLocations.put(st, listShopLocations);
                        }
                    }
                }
                shopsLocation.put(key, loadShopLocations);
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
        String worldPathName = String.format("worlds.%s", worldName);
        filecon.createSection(worldPathName, new HashMap<>());
        filecon.createSection(worldPathName + ".teams", new HashMap<>());
        filecon.createSection(worldPathName + ".queueloc", new HashMap<>());
        filecon.createSection(worldPathName + ".resource-spawners", new HashMap<>());
        filecon.createSection(worldPathName + ".timeline", new HashMap<>());

        filecon.createSection(worldPathName + ".shop-location", new HashMap<>());
        filecon.createSection(worldPathName + ".shop-location." + BedwarsShopType.ITEMS_SHOP.toString(), new HashMap<>());
        filecon.createSection(worldPathName + ".shop-location." + BedwarsShopType.PERMA_SHOP.toString(), new HashMap<>());

        // Default Team YML Initializer
        inTeamGameSpawners.put(worldName, new HashMap<String, Location>());
        for (String teamName : teamPrefix.get(worldName).keySet()) {
            inTeamGameSpawners.get(worldName).put(teamName, defaultSpawnLoc);
            resourceSpawners.get(worldName).put(teamName, new HashMap<String, ResourceSpawner>());
            filecon.createSection(worldPathName + ".teams." + teamName + ".bed-location", new HashMap<>());
        }
    }

    @Override
    public void Save() {
        saveWorldCreator();
        saveWorldQueueSpawn();
        saveTimelineEvent();
        saveResourceSpawnersData();
        savePlayerPrefix();
        saveBedLocation();
        saveShopLocation();
        
        super.Save();
    }

    private void saveResourceSpawnersData() {
        // Save all resource spawners
        for (Map.Entry<String, Map<String, Map<String, ResourceSpawner>>> entryRS : resourceSpawners.entrySet()) {
            for (Map.Entry<String, Map<String, ResourceSpawner>> resSp : resourceSpawners.get(entryRS.getKey()).entrySet()) {
                filecon.createSection(String.format("worlds.%s.resource-spawners.%s", entryRS.getKey(), resSp.getKey()));
                for (Map.Entry<String, ResourceSpawner> theResSpawnerEntry : resSp.getValue().entrySet()) {
                    Location spawnLoc = theResSpawnerEntry.getValue().getSpawnLocation();
                    String rsPathName = String.format("worlds.%s.resource-spawners.%s.%s", entryRS.getKey(), resSp.getKey(), theResSpawnerEntry.getKey());
                    filecon.set(rsPathName + ".type", theResSpawnerEntry.getValue().getTypeResourceSpawner().ordinal());
                    filecon.set(rsPathName + ".spawnloc.x", spawnLoc.getX());
                    filecon.set(rsPathName + ".spawnloc.y", spawnLoc.getY());
                    filecon.set(rsPathName + ".spawnloc.z", spawnLoc.getZ());
                    filecon.set(rsPathName + ".duration-spawn", theResSpawnerEntry.getValue().getSecondsPerSpawn());
                }
            }
        }
    }

    private void saveWorldCreator() {
        // Save all made worlds
        for (Map.Entry<String, WorldCreator> entry : worlds.entrySet()) {
            filecon.set(String.format("worlds.%s.env", entry.getKey()), entry.getValue().environment().ordinal());
            filecon.set(String.format("worlds.%s.structure", entry.getKey()), entry.getValue().generateStructures());
            filecon.set(String.format("worlds.%s.hardcore", entry.getKey()), entry.getValue().hardcore());
        }
    }

    private void saveWorldQueueSpawn() {
        // Save All World queue spawn data
        for (Map.Entry<String, Location> entry : queueLocations.entrySet()) {
            filecon.set(String.format("worlds.%s.queueloc.x", entry.getKey()), entry.getValue().getX());
            filecon.set(String.format("worlds.%s.queueloc.y", entry.getKey()), entry.getValue().getY());
            filecon.set(String.format("worlds.%s.queueloc.z", entry.getKey()), entry.getValue().getZ());
        }
    }

    private void savePlayerPrefix() {
        // Save Player prefix and it's team spawn location
        for (Map.Entry<String, Map<String, String>> wEntry : teamPrefix.entrySet()) {
            for (Map.Entry<String, String> prefix : teamPrefix.get(wEntry.getKey()).entrySet()) {
                String teamPathName = String.format("worlds.%s.teams.%s", wEntry.getKey(), prefix.getKey());
                filecon.set(String.format("%s.color", teamPathName), prefix.getValue());
                filecon.set(String.format("%s.spawner.x", teamPathName), inTeamGameSpawners.get(wEntry.getKey()).get(prefix.getKey()).getX());
                filecon.set(String.format("%s.spawner.y", teamPathName), inTeamGameSpawners.get(wEntry.getKey()).get(prefix.getKey()).getY());
                filecon.set(String.format("%s.spawner.z", teamPathName), inTeamGameSpawners.get(wEntry.getKey()).get(prefix.getKey()).getZ());
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
                String bedLocPathName = String.format("worlds.%s.teams.%s", bedLocEntry.getKey(), teamEntry.getKey());
                filecon.set(bedLocPathName + ".bed-location.x", loc.getX());
                filecon.set(bedLocPathName + ".bed-location.y", loc.getY());
                filecon.set(bedLocPathName + ".bed-location.z", loc.getZ());
            }
        }
    }

    private void saveShopLocation() {
        // Save all shop spawn locations
        for (Map.Entry<String, Map<BedwarsShopType, List<Location>>> shopEntry : shopsLocation.entrySet()) {
            for (Map.Entry<BedwarsShopType, List<Location>> shopTypeEntry : shopEntry.getValue().entrySet()) {
                for (int i = 0; i < shopTypeEntry.getValue().size(); i++) {
                    String shopPathName = String.format("worlds.%s.shop-location.%s.shop%d", shopEntry.getKey(), shopTypeEntry.getKey().toString(), i);
                    filecon.set(shopPathName + ".x", shopTypeEntry.getValue().get(i).getX());
                    filecon.set(shopPathName + ".y", shopTypeEntry.getValue().get(i).getY());
                    filecon.set(shopPathName + ".z", shopTypeEntry.getValue().get(i).getZ());
                }
            }
        }
    }

}
