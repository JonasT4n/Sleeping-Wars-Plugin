package com.joenastan.sleepingwar.plugin.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joenastan.sleepingwar.plugin.Game.ResourceSpawner;
import com.joenastan.sleepingwar.plugin.Game.ResourcesType;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.World.Environment;
import org.bukkit.plugin.Plugin;

import net.md_5.bungee.api.ChatColor;

public class GameSystemConfig extends AbstractFile {

    private Map<String, Location> queueLocations = new HashMap<String, Location>();
    private Map<String, Map<String, Location>> inTeamGameSpawners = new HashMap<String, Map<String, Location>>();
    private Map<String, WorldCreator> worlds = new HashMap<String, WorldCreator>();
    private Map<String, Map<String, String>> teamPrefix = new HashMap<String, Map<String, String>>();
    private Map<String, Map<String, Map<String, ResourceSpawner>>> resourceSpawners = new HashMap<String, Map<String, Map<String, ResourceSpawner>>>();

    public GameSystemConfig(Plugin main, String filename) {
        super(main, filename);
        load();
    }

    public Map<String, Location> getTeamSpawner(String worldKey) {
        if (inTeamGameSpawners.containsKey(worldKey))
            return inTeamGameSpawners.get(worldKey);
        return null;
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

    public Map<String, ResourceSpawner> getResourceSpawnersPack(String worldType, String teamName) {
        if (resourceSpawners.containsKey(worldType))
            if (resourceSpawners.get(worldType).containsKey(teamName))
                return resourceSpawners.get(worldType).get(teamName);

        return null;
    }

    public Map<String, String> getTeamPrefix(String world) {
        if (teamPrefix.containsKey(world))
            return teamPrefix.get(world);
        return null;
    }

    public List<String> getAllWorldName() {
        List<String> wnames = new ArrayList<String>();
        for (Map.Entry<String, WorldCreator> w : worlds.entrySet()) {
            wnames.add(w.getKey());
        }
        return wnames;
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

                Location thisWorldQueueLoc = new Location( w, 
                    filecon.getInt("worlds." + key + ".queueloc.x"), 
                    filecon.getInt("worlds." + key + ".queueloc.y"),
                    filecon.getInt("worlds." + key + ".queueloc.z")
                    );
                queueLocations.put(key, thisWorldQueueLoc);

                // Loop by Team Name
                teamPrefix.put(key, new HashMap<String, String>());
                inTeamGameSpawners.put(key, new HashMap<String, Location>());
                Map<String, Map<String, ResourceSpawner>> teamResourceSpawnersLocal = new HashMap<String, Map<String, ResourceSpawner>>();
                for (String tString : filecon.getConfigurationSection("worlds." + key + ".teams").getKeys(false)) {
                    teamPrefix.get(key).put(tString, filecon.getString("worlds." + key + ".teams." + tString));
                    Location thisTeamSpawn = new Location(w, filecon.getInt("worlds." + key + ".teams." + tString + ".spawner.x"), 
                        filecon.getInt("worlds." + key + ".teams." + tString + ".spawner.y"), 
                        filecon.getInt("worlds." + key + ".teams." + tString + ".spawner.z"));
                    inTeamGameSpawners.get(key).put(tString, thisTeamSpawn);

                    // Get all resource spawners from team
                    Map<String, ResourceSpawner> resourceSpawnersLocal = new HashMap<String, ResourceSpawner>();
                    for (String rsName : filecon.getConfigurationSection("worlds." + key + ".teams." + tString + ".resources").getKeys(false)) {
                        Location rspawnerLoc = new Location(w, filecon.getInt("worlds." + key + ".teams." + tString + ".resources.spawnloc.x"), 
                            filecon.getInt("worlds." + key + ".teams." + tString + ".resources.spawnloc.y"), 
                            filecon.getInt("worlds." + key + ".teams." + tString + ".resources.spawnloc.z"));
                        ResourceSpawner rspawner = new ResourceSpawner(rsName, rspawnerLoc, 
                            ResourcesType.values()[filecon.getInt("worlds." + key + ".teams." + tString + ".resources.type")]);
                        resourceSpawnersLocal.put(rsName, rspawner);
                    }
                    teamResourceSpawnersLocal.put(tString, resourceSpawnersLocal);
                }

                // Get all public resource spawners
                // Get all resource spawners from team
                Map<String, ResourceSpawner> publicResourceSpawnersLocal = new HashMap<String, ResourceSpawner>();
                for (String rsName : filecon.getConfigurationSection("worlds." + key + ".public-resources-spawner").getKeys(false)) {
                    Location rspawnerLoc = new Location(w, filecon.getInt("worlds." + key + ".public-resources-spawner." + rsName + ".spawnloc.x"), 
                        filecon.getInt("worlds." + key + ".public-resources-spawner." + rsName + ".spawnloc.y"), 
                        filecon.getInt("worlds." + key + ".public-resources-spawner." + rsName + ".spawnloc.z"));
                    ResourceSpawner rspawner = new ResourceSpawner(rsName, rspawnerLoc, 
                        ResourcesType.values()[filecon.getInt("worlds." + key + ".public-resources-spawner." + rsName + ".type")]);
                    publicResourceSpawnersLocal.put(key, rspawner);
                }
                teamResourceSpawnersLocal.put("PUBLIC", publicResourceSpawnersLocal);
                resourceSpawners.put(key, teamResourceSpawnersLocal);
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
        teamPrefix.get(worldName).put("Blue", ChatColor.BLUE + "");
        teamPrefix.get(worldName).put("Yellow", ChatColor.YELLOW + "");
        teamPrefix.get(worldName).put("Red", ChatColor.RED + "");
        teamPrefix.get(worldName).put("Green", ChatColor.GREEN + "");

        // Create Sections
        filecon.createSection("worlds." + worldName, new HashMap<>());
        filecon.createSection("worlds." + worldName + ".teams", new HashMap<>());
        filecon.createSection("worlds." + worldName + ".queueloc", new HashMap<>());
        filecon.createSection("worlds." + worldName + ".public-resources-spawner", new HashMap<>());

        // Default Team Spawner
        inTeamGameSpawners.put(worldName, new HashMap<String, Location>());
        for (String teamName : teamPrefix.get(worldName).keySet()) {
            inTeamGameSpawners.get(worldName).put(teamName, defaultSpawnLoc);
            resourceSpawners.get(worldName).put(teamName, new HashMap<String, ResourceSpawner>());
            filecon.createSection("worlds." + worldName + ".teams." + teamName + ".resources", new HashMap<>());
        }
        resourceSpawners.get(worldName).put("PUBLIC", new HashMap<String, ResourceSpawner>());
    }

    public void save() {
        // Set All World queue spawn data
        for (Map.Entry<String, Location> entry : queueLocations.entrySet()) {
            filecon.set("worlds." + entry.getKey() + ".queueloc.x", entry.getValue().getBlockX());
            filecon.set("worlds." + entry.getKey() + ".queueloc.y", entry.getValue().getBlockY());
            filecon.set("worlds." + entry.getKey() + ".queueloc.z", entry.getValue().getBlockZ());
        }

        // Save all made worlds
        for (Map.Entry<String, WorldCreator> entry : worlds.entrySet()) {
            filecon.set("worlds." + entry.getKey() + ".env", entry.getValue().environment().ordinal());
            filecon.set("worlds." + entry.getKey() + ".structure", entry.getValue().generateStructures());
            filecon.set("worlds." + entry.getKey() + ".hardcore", entry.getValue().hardcore());
        }

        // Save team config
        for (int j = 0; j < getAllWorldName().size(); j++) {
            String wname = getAllWorldName().get(j);

            // Player prefix
            for (Map.Entry<String, String> prefix : teamPrefix.get(wname).entrySet()) {
                filecon.set("worlds." + wname + ".teams." + prefix.getKey() + ".color", prefix.getValue());
                filecon.set("worlds." + wname + ".teams." + prefix.getKey() + ".spawner.x", 
                    inTeamGameSpawners.get(wname).get(prefix.getKey()).getBlockX());
                filecon.set("worlds." + wname + ".teams." + prefix.getKey() + ".spawner.y", 
                    inTeamGameSpawners.get(wname).get(prefix.getKey()).getBlockY());
                filecon.set("worlds." + wname + ".teams." + prefix.getKey() + ".spawner.z", 
                    inTeamGameSpawners.get(wname).get(prefix.getKey()).getBlockZ());
            }

            // Resource Spawners
            for (Map.Entry<String, Map<String, ResourceSpawner>> resSp : resourceSpawners.get(wname).entrySet()) {
                // Public resource spawners
                if (resSp.getKey().equals("PUBLIC")) {
                    for (Map.Entry<String, ResourceSpawner> publicSpawner : resSp.getValue().entrySet()) {
                        Location spawnLoc = publicSpawner.getValue().getSpawnLocation();
                        filecon.set("worlds." + wname + ".public-resources-spawner." + publicSpawner.getKey() + ".type", 
                            publicSpawner.getValue().getTypeResourceSpawner().ordinal());
                        filecon.set("worlds." + wname + ".public-resources-spawner." + publicSpawner.getKey() + ".spawnloc.x", 
                            spawnLoc.getBlockX());
                        filecon.set("worlds." + wname + ".public-resources-spawner." + publicSpawner.getKey() + ".spawnloc.y", 
                            spawnLoc.getBlockY());
                        filecon.set("worlds." + wname + ".public-resources-spawner." + publicSpawner.getKey() + ".spawnloc.z", 
                            spawnLoc.getBlockZ());
                    }
                }
                // Team owned resources spawner
                else 
                {
                    for (Map.Entry<String, ResourceSpawner> publicSpawner : resSp.getValue().entrySet()) {
                        Location spawnLoc = publicSpawner.getValue().getSpawnLocation();
                        filecon.set("worlds." + wname + ".teams." + resSp.getKey() + ".resources." + publicSpawner.getKey() + ".type", 
                            publicSpawner.getValue().getTypeResourceSpawner().ordinal());
                        filecon.set("worlds." + wname + ".teams." + resSp.getKey() + ".resources." + publicSpawner.getKey() + ".spawnloc.x", 
                            spawnLoc.getBlockX());
                        filecon.set("worlds." + wname + ".teams." + resSp.getKey() + ".resources." + publicSpawner.getKey() + ".spawnloc.y", 
                            spawnLoc.getBlockY());
                        filecon.set("worlds." + wname + ".teams." + resSp.getKey() + ".resources." + publicSpawner.getKey() + ".spawnloc.z", 
                            spawnLoc.getBlockZ());
                    }
                }
            }
        }

        Save();
    }

}
