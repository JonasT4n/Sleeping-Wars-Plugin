package com.joenastan.sleepingwars.utility.DataFiles;

import com.joenastan.sleepingwars.SleepingWarsPlugin;
import com.joenastan.sleepingwars.enumtypes.*;
import com.joenastan.sleepingwars.events.CustomEvents.BedwarsTimelineEvent;
import com.joenastan.sleepingwars.game.ResourceSpawner;
import com.joenastan.sleepingwars.game.SleepingRoom;
import com.joenastan.sleepingwars.game.TeamGroupMaker;
import com.joenastan.sleepingwars.game.CustomEntity.LockedNormalEntity;
import com.joenastan.sleepingwars.game.CustomEntity.LockedResourceSpawner;
import com.joenastan.sleepingwars.tasks.DeleteWorldDelayed;
import com.joenastan.sleepingwars.timercoro.AreaEffectTimer;

import com.joenastan.sleepingwars.utility.PluginStaticFunc;
import com.joenastan.sleepingwars.utility.VoidGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.*;

public class GameSystemConfig extends AbstractFile {

    // Constant paths
    private static final String WORLD_BORDER_PATH = "worlds.%s.border";
    private static final String RESOURCE_SPAWNER_PATH = "worlds.%s.resource-spawners";
    private static final String TEAM_PATH = "worlds.%s.teams";
    private static final String FLAG_PATH = "worlds.%s.flag";
    private static final String TIMELINE_EVENT_PATH = "worlds.%s.timeline";
    private static final String DEFAULT_VALUE_PATH = "worlds.%s.default-values";
    private static final String LOCKED_ENTITY_PATH = "worlds.%s.locked-entity";

    // Other Attributes
    // TODO: remove temporary usage
    public static final HashSet<String> DEFAULT_KEYS = new HashSet<>();

    // Constructor
    public GameSystemConfig(JavaPlugin main, String filename) {
        super(main, filename);
        load();
    }

    static {
        // TODO: remove temporary usage
        DEFAULT_KEYS.add("iron_ingot-spawner-freq");
        DEFAULT_KEYS.add("gold_ingot-spawner-freq");
        DEFAULT_KEYS.add("diamond-spawner-freq");
        DEFAULT_KEYS.add("emerald-spawner-freq");
    }

    //#region Getter Methods
    /**
     * All world names or map names that registered as bedwars world or map.
     *
     * @return List of world names or map names
     */
    public List<String> getWorldNames() {
        ConfigurationSection cs = fileConfig.getConfigurationSection("worlds");
        List<String> worldList = new ArrayList<>();
        if (cs != null)
            worldList.addAll(cs.getKeys(false));
        return worldList;
    }

    /**
     * Get all resource spawners that has been registered.
     * All resource spawners including public, locked resource spawner, and team owned resource spawner.
     *
     * @param inWorld Current game or builder world
     * @param mapName Original map name
     * @return List of resource spawners
     */
    public List<ResourceSpawner> getWorldRS(World inWorld, String mapName) {
        String path = String.format(RESOURCE_SPAWNER_PATH, mapName);
        List<ResourceSpawner> rSpawner = new ArrayList<>();
        if (!fileConfig.contains(path)) {
            fileConfig.createSection(path, new HashMap<>());
            return rSpawner;
        }

        // Get all Resource spawners
        ConfigurationSection csPath = fileConfig.getConfigurationSection(path);
        if (csPath != null) {
            for (String t : csPath.getKeys(false)) {
                ConfigurationSection rsCS = fileConfig.getConfigurationSection(path + "." + t);
                if (rsCS != null) {
                    for (String rs : rsCS.getKeys(false)) {
                        // Get all resource spawners real information
                        String rsPath = String.format("%s.%s.%s", path, t, rs);
                        if (!fileConfig.contains(rsPath + ".type"))
                            fileConfig.set(rsPath + ".type", "iron_ingot");
                        if (!fileConfig.contains(rsPath + ".spawnloc.x"))
                            fileConfig.set(rsPath + ".spawnloc.x", inWorld.getSpawnLocation().getX());
                        if (!fileConfig.contains(rsPath + ".spawnloc.y"))
                            fileConfig.set(rsPath + ".spawnloc.y", inWorld.getSpawnLocation().getY());
                        if (!fileConfig.contains(rsPath + ".spawnloc.z"))
                            fileConfig.set(rsPath + ".spawnloc.z", inWorld.getSpawnLocation().getZ());
                        if (!fileConfig.contains(rsPath + ".duration-spawn"))
                            fileConfig.set(rsPath + ".duration-spawn", 10f);
                        String typeString = fileConfig.getString(rsPath + ".type");
                        if (typeString == null)
                            typeString = "";
                        Material typeSpawnResource = Material.getMaterial(typeString.toUpperCase());
                        Location spawnLoc = new Location(inWorld, fileConfig.getDouble(rsPath + ".spawnloc.x"),
                                fileConfig.getDouble(rsPath + ".spawnloc.y"),
                                fileConfig.getDouble(rsPath + ".spawnloc.z"));
                        float spawnDur = (float) fileConfig.getDouble(rsPath + ".duration-spawn");

                        // Check if it is a valid currency type
                        if (typeSpawnResource == null)
                            continue;
                        if (!PluginStaticFunc.isCurrencyInGame(typeSpawnResource))
                            continue;

                        // Create resource spawner instance
                        ResourceSpawner rsp;
                        if (spawnDur < 0f)
                            rsp = new ResourceSpawner(rs, spawnLoc, typeSpawnResource);
                        else
                            rsp = new ResourceSpawner(rs, spawnLoc, typeSpawnResource, spawnDur);
                        rSpawner.add(rsp);
                    }
                }
            }
        }

        // Get all locked resource spawner
        String lockedEntPath = String.format(LOCKED_ENTITY_PATH, mapName);
        ConfigurationSection entLockPath = fileConfig.getConfigurationSection(lockedEntPath);
        if (entLockPath != null) {
            for (String le : entLockPath.getKeys(false)) {
                LockedEntityType lockType = LockedEntityType.values()[fileConfig.getInt(lockedEntPath +
                        "." + le + ".type-lock")];
                if (lockType == LockedEntityType.RESOURCE_SPAWNER_LOCK) {
                    String rsLockPath = String.format("%s.%s.rs-lock", lockedEntPath, le);
                    ConfigurationSection rsCS = fileConfig.getConfigurationSection(rsLockPath);
                    if (rsCS != null) {
                        for (String lrs : rsCS.getKeys(false)) {
                            // Get all resource spawners real information
                            String rsPath = String.format("%s.%s", rsLockPath, le);
                            if (!fileConfig.contains(rsPath + ".type"))
                                fileConfig.set(rsPath + ".type", "iron_ingot");
                            if (!fileConfig.contains(rsPath + ".spawnloc.x"))
                                fileConfig.set(rsPath + ".spawnloc.x", inWorld.getSpawnLocation().getX());
                            if (!fileConfig.contains(rsPath + ".spawnloc.y"))
                                fileConfig.set(rsPath + ".spawnloc.y", inWorld.getSpawnLocation().getY());
                            if (!fileConfig.contains(rsPath + ".spawnloc.z"))
                                fileConfig.set(rsPath + ".spawnloc.z", inWorld.getSpawnLocation().getZ());
                            if (!fileConfig.contains(rsPath + ".duration-spawn"))
                                fileConfig.set(rsPath + ".duration-spawn", 10f);
                            String typeString = fileConfig.getString(rsPath + ".type");
                            if (typeString == null)
                                typeString = "";
                            Material typeSpawnResource = Material.getMaterial(typeString.toUpperCase());
                            Location spawnLoc = new Location(inWorld, fileConfig.getDouble(rsPath + ".spawnloc.x"),
                                    fileConfig.getDouble(rsPath + ".spawnloc.y"),
                                    fileConfig.getDouble(rsPath + ".spawnloc.z"));
                            float spawnDur = (float) fileConfig.getDouble(rsPath + ".duration-spawn");

                            // Check if it is a valid currency type
                            if (typeSpawnResource == null)
                                continue;
                            if (!PluginStaticFunc.isCurrencyInGame(typeSpawnResource))
                                continue;

                            // Create resource spawner instance
                            ResourceSpawner rsp;
                            if (spawnDur < 0f)
                                rsp = new ResourceSpawner(lrs, spawnLoc, typeSpawnResource);
                            else
                                rsp = new ResourceSpawner(lrs, spawnLoc, typeSpawnResource, spawnDur);
                            rSpawner.add(rsp);
                        }
                    }
                }
            }
        }
        return rSpawner;
    }

    /**
     * Get border data from configuration file.
     *
     * @param mapName Original map name
     */
    public int getBorderData(String mapName, boolean shrunkBorder) {
        String path = String.format(WORLD_BORDER_PATH, mapName);
        if (!fileConfig.contains(path))
            fileConfig.createSection(path);
        // Check if path exists, if not then create default
        if (shrunkBorder) {
            if (!fileConfig.contains(path + ".shrunk"))
                fileConfig.set(path + ".shrunk", 24);
            return fileConfig.getInt(path + ".shrunk");
        } else {
            if (!fileConfig.contains(path + ".normal"))
                fileConfig.set(path + ".normal", 1024);
            return fileConfig.getInt(path + ".normal");
        }
    }

    /**
     * Get in game flags.
     * Only usable after the room constructed.
     *
     * @param mapName Original map name
     * @param mapRef Referenced collection
     */
    public void getFlags(String mapName, @Nonnull Map<InGameFlags, Boolean> mapRef) {
        String path = String.format(FLAG_PATH, mapName);
        if (!fileConfig.contains(path))
            fileConfig.createSection(path);
        ConfigurationSection cs = fileConfig.getConfigurationSection(path);
        if (cs != null) {
            for (String f : cs.getKeys(false)) {
                InGameFlags fl = InGameFlags.fromString(f);
                if (fl != null)
                    mapRef.put(fl, fileConfig.getBoolean(path + "." + fl));
            }
        }
    }

    /**
     * Get all resource spawners that has been registered by specific class.
     *
     * @param inWorld  Current game or builder world
     * @param mapName  Original map name
     * @param teamName On team by name
     * @return List of resource spawners
     */
    public List<ResourceSpawner> getWorldRS(World inWorld, String mapName, String teamName) {
        String tPath = String.format(RESOURCE_SPAWNER_PATH + ".%s", mapName, teamName);
        List<ResourceSpawner> rSpawner = new ArrayList<>();

        // Check team existence
        List<String> team = getTeamNames(mapName, true);
        if (!fileConfig.contains(tPath) && team.contains(teamName)) {
            fileConfig.createSection(tPath, new HashMap<>());
            return rSpawner;
        } else if (!team.contains(teamName)) {
            tPath = String.format(RESOURCE_SPAWNER_PATH + ".PUBLIC", mapName);
        }

        // Get all Resource spawners
        ConfigurationSection tcs = fileConfig.getConfigurationSection(tPath);
        if (tcs != null) {
            for (String rs : tcs.getKeys(false)) {
                String rsPath = tPath + "." + rs;
                if (!fileConfig.contains(rsPath + ".type"))
                    fileConfig.set(rsPath + ".type", "iron_ingot");
                if (!fileConfig.contains(rsPath + ".spawnloc.x"))
                    fileConfig.set(rsPath + ".spawnloc.x", inWorld.getSpawnLocation().getX());
                if (!fileConfig.contains(rsPath + ".spawnloc.y"))
                    fileConfig.set(rsPath + ".spawnloc.y", inWorld.getSpawnLocation().getY());
                if (!fileConfig.contains(rsPath + ".spawnloc.z"))
                    fileConfig.set(rsPath + ".spawnloc.z", inWorld.getSpawnLocation().getZ());
                if (!fileConfig.contains(rsPath + ".duration-spawn"))
                    fileConfig.set(rsPath + ".duration-spawn", 10f);
                String typeString = fileConfig.getString(rsPath + ".type");
                if (typeString == null)
                    typeString = "";
                Material typeSpawnResource = Material.getMaterial(typeString.toUpperCase());
                Location spawnLoc = new Location(inWorld, fileConfig.getDouble(rsPath + ".spawnloc.x"),
                        fileConfig.getDouble(rsPath + ".spawnloc.y"),
                        fileConfig.getDouble(rsPath + ".spawnloc.z"));
                float spawnDur = (float) fileConfig.getDouble(rsPath + ".duration-spawn");

                // Check if it is a valid currency type
                if (typeSpawnResource == null)
                    continue;
                if (!PluginStaticFunc.isCurrencyInGame(typeSpawnResource))
                    continue;

                // Create resource spawner instance
                ResourceSpawner rsp;
                if (spawnDur < 0f)
                    rsp = new ResourceSpawner(rs, spawnLoc, typeSpawnResource);
                else
                    rsp = new ResourceSpawner(rs, spawnLoc, typeSpawnResource, spawnDur);
                rSpawner.add(rsp);
            }
        }
        return rSpawner;
    }

    /**
     * All teams that registered in current world or map.
     *
     * @param world Original map name
     * @return List of team names in map
     */
    public List<String> getTeamNames(String world, boolean withRemoved) {
        String path = String.format(TEAM_PATH, world);
        if (!fileConfig.contains(path)) {
            fileConfig.createSection(path, new HashMap<>());
            fileConfig.set(String.format("%s.Yellow", path), new HashMap<>());
            fileConfig.set(String.format("%s.Red", path), new HashMap<>());
            fileConfig.set(String.format("%s.Blue", path), new HashMap<>());
            fileConfig.set(String.format("%s.Green", path), new HashMap<>());
        }
        ConfigurationSection cs = fileConfig.getConfigurationSection(path);
        List<String> teamList = new ArrayList<>();
        if (cs != null)
            teamList.addAll(cs.getKeys(false));
        if (!withRemoved) {
            for (String teamName : teamList) {
                String pathRemove = String.format("%s.%s.is-removed", path, teamName);
                if (!fileConfig.contains(pathRemove))
                    fileConfig.set(pathRemove, false);
                if (fileConfig.getBoolean(pathRemove))
                    teamList.remove(teamName);
            }
        }
        return new ArrayList<>(teamList);
    }

    /**
     * Get team location spawner in game.
     *
     * @param inWorld  Current game or builder world
     * @param mapName  Original map name
     * @param teamName On team by name
     * @return Team location spawn
     */
    public Location getTeamSpawner(World inWorld, String mapName, String teamName) {
        String path = String.format(TEAM_PATH + ".%s.spawner", mapName, teamName);
        List<String> team = getTeamNames(mapName, true);
        if (!team.contains(teamName))
            return null;
        if (!fileConfig.contains(path))
            fileConfig.createSection(path, new HashMap<>());
        if (!fileConfig.contains(path + ".x"))
            fileConfig.set(path + ".x", inWorld.getSpawnLocation().getX());
        if (!fileConfig.contains(path + ".y"))
            fileConfig.set(path + ".y", inWorld.getSpawnLocation().getY());
        if (!fileConfig.contains(path + ".z"))
            fileConfig.set(path + ".z", inWorld.getSpawnLocation().getZ());
        return new Location(inWorld, fileConfig.getDouble(path + ".x"), fileConfig
                .getDouble(path + ".y"), fileConfig.getDouble(path + ".z"));
    }

    /**
     * Get max player in team configuration.
     *
     * @param mapName Original map name
     */
    public int getMaxPlayerInTeam(String mapName) {
        String path = String.format("worlds.%s.max-player-per-team", mapName);
        if (!fileConfig.contains(path))
            fileConfig.set(path, 4);
        return fileConfig.getInt(path);
    }

    /**
     * Get team's bed location. User need to check if a bed on that location has been put.
     *
     * @param mapName  Original map name
     * @param teamName On team by name
     * @return Bed location not actual bed, if team is not exists then returns null
     */
    public Location getBedLocation(World inWorld, String mapName, String teamName) {
        // Check if team is exists
        List<String> team = getTeamNames(mapName, true);
        if (!team.contains(teamName))
            return null;
        String path = String.format(TEAM_PATH + ".%s.bed-location", mapName, teamName);
        if (!fileConfig.contains(path))
            fileConfig.createSection(path);
        if (!fileConfig.contains(path + ".x"))
            fileConfig.set(path + ".x", inWorld.getSpawnLocation().getBlockX());
        if (!fileConfig.contains(path + ".y"))
            fileConfig.set(path + ".y", inWorld.getSpawnLocation().getBlockY());
        if (!fileConfig.contains(path + ".z"))
            fileConfig.set(path + ".z", inWorld.getSpawnLocation().getBlockZ());
        return new Location(inWorld, fileConfig.getInt(path + ".x"), fileConfig.getInt(path + ".y"),
                fileConfig.getInt(path + ".z"));
    }

    /**
     * Get team color raw string config.
     *
     * @param mapName  Original map name
     * @param teamName On team by name
     * @return Raw string data, if team is not exists then null
     */
    public String getRawColor(String mapName, String teamName) {
        List<String> team = getTeamNames(mapName, true);
        if (!team.contains(teamName))
            return null;
        String path = String.format(TEAM_PATH + ".%s.color", mapName, teamName);
        if (!fileConfig.contains(path))
            fileConfig.set(path, "white");
        return fileConfig.getString(path);
    }

    /**
     * Get the minimum value location of team buffer area
     *
     * @param mapName  Original map name
     * @param teamName On team with name
     * @return Minimum buffer area location
     */
    public Location getMinimumAreaBuff(World inWorld, String mapName, String teamName) {
        // Check team exists
        List<String> team = getTeamNames(mapName, true);
        if (!team.contains(teamName))
            teamName = "PUBLIC";
        // Check path in config exists
        String path = String.format("worlds.%s.buffer-zone.%s", mapName, teamName);
        if (!fileConfig.contains(path))
            fileConfig.createSection(path);
        if (!fileConfig.contains(path + ".minx"))
            fileConfig.set(path + ".minx", inWorld.getSpawnLocation().getX() - 5d);
        if (!fileConfig.contains(path + ".miny"))
            fileConfig.set(path + ".miny", inWorld.getSpawnLocation().getY() - 5d);
        if (!fileConfig.contains(path + ".minz"))
            fileConfig.set(path + ".minz", inWorld.getSpawnLocation().getZ() - 5d);
        return new Location(inWorld, fileConfig.getDouble(path + ".minx"), fileConfig.getDouble(path +
                ".miny"), fileConfig.getDouble(path + ".minz"));
    }

    /**
     * Get the maximum value location of team buffer area
     *
     * @param mapName  Original map name
     * @param teamName On team with name
     * @return Maximum buffer area location
     */
    public Location getMaximumAreaBuff(World inWorld, String mapName, String teamName) {
        // Check team exists
        List<String> team = getTeamNames(mapName, true);
        if (!team.contains(teamName))
            teamName = "PUBLIC";
        // Check path in config exists
        String path = String.format("worlds.%s.buffer-zone.%s", mapName, teamName);
        if (!fileConfig.contains(path))
            fileConfig.createSection(path);
        if (!fileConfig.contains(path + ".maxx"))
            fileConfig.set(path + ".maxx", inWorld.getSpawnLocation().getX() + 5d);
        if (!fileConfig.contains(path + ".maxy"))
            fileConfig.set(path + ".maxy", inWorld.getSpawnLocation().getY() + 5d);
        if (!fileConfig.contains(path + ".maxz"))
            fileConfig.set(path + ".maxz", inWorld.getSpawnLocation().getZ() + 5d);
        return new Location(inWorld, fileConfig.getDouble(path + ".maxx"), fileConfig.getDouble(
                path + ".maxy"), fileConfig.getDouble(path + ".maxz"));
    }

    /**
     * Immediately get the buffer zone coroutine with empty effects.
     * @param inWorld Current world standing
     * @param mapName Original map name
     * @param team On team with name
     * @return Buffer zone coroutine, including the locations
     */
    public AreaEffectTimer getAreaBuffRoutine(World inWorld, String mapName, TeamGroupMaker team) {
        // Check team exists
        if (!getTeamNames(mapName, true).contains(team.getName()))
            return null;
        return new AreaEffectTimer(5f, getMinimumAreaBuff(inWorld, mapName, team.getName()),
                getMaximumAreaBuff(inWorld, mapName, team.getName()), team);
    }

    /**
     * Immediately get the buffer zone coroutine with empty effects.
     *
     * @param inWorld Current world standing
     * @param mapName Original map name
     * @return List of public buffer zone coroutine, including the locations
     */
    public List<AreaEffectTimer> getAreaBuffPublic(World inWorld, String mapName) {
        String path = String.format("worlds.%s.buffer-zone.PUBLIC", mapName);
        List<AreaEffectTimer> listBufferZonesCor = new ArrayList<>();
        if (!fileConfig.contains(path))
            fileConfig.createSection(path);
        ConfigurationSection cs = fileConfig.getConfigurationSection(path);
        for (String bz : cs.getKeys(false)) {
            Location minLoc = new Location(inWorld, fileConfig.getDouble(String.format("%s.%s.minx", path, bz)),
                    fileConfig.getDouble(String.format("%s.%s.miny", path, bz)),
                    fileConfig.getDouble(String.format("%s.%s.minz", path, bz)));
            Location maxLoc = new Location(inWorld, fileConfig.getDouble(String.format("%s.%s.maxx", path, bz)),
                    fileConfig.getDouble(String.format("%s.%s.maxy", path, bz)),
                    fileConfig.getDouble(String.format("%s.%s.maxz", path, bz)));
            listBufferZonesCor.add(new AreaEffectTimer(5f, minLoc, maxLoc, null));
        }
        return listBufferZonesCor;
    }

    /**
     * Get codename that owned by a team.
     * If there's no existing team then it consider as public resource spawners.
     *
     * @param mapName  Original map name
     * @param teamName On team with name
     * @return A list of resource spawner codename
     */
    public List<String> getRSCodename(String mapName, String teamName) {
        // Check existing team
        List<String> team = getTeamNames(mapName, true);
        if (!team.contains(teamName))
            teamName = "PUBLIC";

        // Check existence in config file
        String path = String.format(RESOURCE_SPAWNER_PATH + ".%s", mapName, teamName);
        List<String> codenameList = new ArrayList<>();
        if (!fileConfig.contains(path)) {
            fileConfig.createSection(path);
            return codenameList;
        }

        // Include all codename in team
        codenameList.addAll(fileConfig.getConfigurationSection(path).getKeys(false));

        // Check if it is public, then get all locked resource spawner list
        if (teamName.equals("PUBLIC")) {
            // Get all locked resource spawner codename
            String lockedEntPath = String.format(LOCKED_ENTITY_PATH, mapName);
            for (String le : fileConfig.getConfigurationSection(lockedEntPath).getKeys(false)) {
                LockedEntityType lockType = LockedEntityType.values()[fileConfig.getInt(lockedEntPath +
                        "." + le + ".type-lock")];
                if (lockType == LockedEntityType.RESOURCE_SPAWNER_LOCK) {
                    String rsLockPath = String.format("%s.%s.rs-lock", lockedEntPath, le);
                    codenameList.addAll(fileConfig.getConfigurationSection(rsLockPath).getKeys(false));
                }
            }
        }
        return codenameList;
    }

    /**
     * Specific map queue location.
     *
     * @param inWorld Current game or builder world
     * @param mapName Original map name
     * @return Queue Location
     */
    public Location getQueuePos(World inWorld, String mapName) {
        String path = String.format("worlds.%s.queueloc", mapName);
        if (!fileConfig.contains(path))
            fileConfig.createSection(path, new HashMap<>());
        if (!fileConfig.contains(path + ".x"))
            fileConfig.set(path + ".x", inWorld.getSpawnLocation().getX());
        if (!fileConfig.contains(path + ".y"))
            fileConfig.set(path + ".y", inWorld.getSpawnLocation().getY());
        if (!fileConfig.contains(path + ".z"))
            fileConfig.set(path + ".z", inWorld.getSpawnLocation().getZ());
        return new Location(inWorld, fileConfig.getDouble(path + ".x"),
                fileConfig.getDouble(path + ".y"),
                fileConfig.getDouble(path + ".z"));
    }

    /**
     * Get bedwars timeline events
     *
     * @param mapName Original map name
     * @return List of events in timeline
     */
    public List<BedwarsTimelineEvent> getTimelineEvents(String mapName, @Nullable SleepingRoom room) {
        String path = String.format(TIMELINE_EVENT_PATH, mapName);
        List<BedwarsTimelineEvent> timelineEvents = new ArrayList<>();
        if (!fileConfig.contains(path)) {
            fileConfig.createSection(path, new HashMap<>());
            return timelineEvents; // Empty timeline
        }

        // Get and create object event
        for (String eventNameString : fileConfig.getConfigurationSection(path).getKeys(false)) {
            // Set Defaults
            String tPath = String.format("%s.%s", path, eventNameString);
            int order = fileConfig.getInt(tPath + ".order");
            float secondsToTrigger = (float) fileConfig.getDouble(tPath + ".trigger-in-seconds");
            String typeString = fileConfig.getString(tPath + ".type");
            if (typeString != null) {
                TimelineEventType typeEvent = TimelineEventType.fromString(typeString);
                String eventMsg = fileConfig.getString(tPath + ".message");
                BedwarsTimelineEvent bent = new BedwarsTimelineEvent(typeEvent, secondsToTrigger,
                        eventNameString, order, eventMsg, room);
                timelineEvents.add(bent);
            }
        }

        // Selection sort
        for (int i = 0; i < timelineEvents.size() - 1; i++) {
            int lowestValueIndex = i;
            for (int j = i + 1; j < timelineEvents.size(); j++) {
                if (timelineEvents.get(lowestValueIndex).getTimelineOrder() > timelineEvents.get(j).getTimelineOrder())
                    lowestValueIndex = j;
            }
            if (i != lowestValueIndex) {
                BedwarsTimelineEvent temp = timelineEvents.get(i);
                timelineEvents.set(i, timelineEvents.get(lowestValueIndex));
                timelineEvents.set(lowestValueIndex, temp);
            }
        }
        return timelineEvents;
    }

    /**
     * Get bedwars timeline events
     *
     * @param mapName Original map name
     * @return List of event names in timeline
     */
    public List<String> getTimelineEventNames(String mapName) {
        String path = String.format(TIMELINE_EVENT_PATH, mapName);
        if (!fileConfig.contains(path))
            fileConfig.createSection(path, new HashMap<>());
        ConfigurationSection cs = fileConfig.getConfigurationSection(path);
        List<String> eventNames = new ArrayList<>();
        if (cs != null)
            eventNames.addAll(cs.getKeys(false));
        return eventNames;
    }

    /**
     * Get shops information on map or registered world.
     *
     * @param inWorld Current world standing
     * @param mapName Original map name
     * @return Map of type with locations
     */
    public Map<BedwarsShopType, List<Location>> getShops(World inWorld, String mapName) {
        String path = String.format("worlds.%s.shop-location", mapName);
        Map<BedwarsShopType, List<Location>> shopMapLoc = new HashMap<>();
        if (!fileConfig.contains(path))
            fileConfig.createSection(path, new HashMap<>());
        ConfigurationSection csPath = fileConfig.getConfigurationSection(path);
        if (csPath != null) {
            for (String shopTypeString : csPath.getKeys(false)) {
                BedwarsShopType shopType = BedwarsShopType.fromString(shopTypeString);
                ConfigurationSection cs = fileConfig.getConfigurationSection(path + "." + shopTypeString);
                if (cs != null) {
                    Set<String> indexStringSet = cs.getKeys(false);
                    if (shopType != null && !indexStringSet.isEmpty()) {
                        List<Location> locationList = new ArrayList<>();
                        for (String shopLocString : indexStringSet) {
                            String locPath = path + "." + shopTypeString + "." + shopLocString;
                            Location loc = new Location(inWorld, fileConfig.getDouble(locPath + ".x"),
                                    fileConfig.getDouble(locPath + ".y"), fileConfig.getDouble(locPath + ".z"));
                            locationList.add(loc);
                        }
                        shopMapLoc.put(shopType, locationList);
                    }
                }
            }
        }
        return shopMapLoc;
    }

    /**
     * Get list of locked entities.
     *
     * @param inWorld Current world standing
     * @param mapName Original map name
     */
    public List<LockedNormalEntity> getLockedEntities(World inWorld, String mapName,
                                                      @Nonnull List<ResourceSpawner> refHolder) {
        String path = String.format("worlds.%s.locked-entity", mapName);
        if (!fileConfig.contains(path))
            fileConfig.createSection(path, new HashMap<>());
        List<LockedNormalEntity> listOfLocked = new ArrayList<>();
        ConfigurationSection csPath = fileConfig.getConfigurationSection(path);
        if (csPath != null) {
            for (String cn : csPath.getKeys(false)) {
                LockedEntityType typeLock = LockedEntityType.values()[fileConfig.getInt(path + "." + cn + ".type-lock")];

                // Get requirements to unlock
                String tPath = path + "." + cn + ".request";
                Map<Material, Integer> requirements = new HashMap<>();
                if (!fileConfig.contains(tPath))
                    fileConfig.createSection(tPath);
                ConfigurationSection tcs = fileConfig.getConfigurationSection(tPath);
                if (tcs != null) {
                    for (String req : tcs.getKeys(false)) {
                        Material typeRequest = Material.getMaterial(req.toUpperCase());
                        requirements.put(typeRequest, fileConfig.getInt(tPath + "." + req));
                    }
                }

                // Check lock type
                if (typeLock == LockedEntityType.NORMAL_LOCK) {
                    listOfLocked.add(new LockedNormalEntity(new Location(inWorld,
                            fileConfig.getDouble(path + "." + cn + ".loc.x"),
                            fileConfig.getDouble(path + "." + cn + ".loc.y"),
                            fileConfig.getDouble(path + "." + cn + ".loc.z")), requirements));
                } else if (typeLock == LockedEntityType.RESOURCE_SPAWNER_LOCK) {
                    String lockedRSPath = String.format("%s.%s.rs-lock", path, cn);
                    List<ResourceSpawner> rsList = new ArrayList<>();
                    ConfigurationSection rsCS = fileConfig.getConfigurationSection(lockedRSPath);
                    if (rsCS != null) {
                        for (String rs : rsCS.getKeys(false)) {
                            String rsPath = lockedRSPath + "." + rs;
                            if (!fileConfig.contains(rsPath + ".type"))
                                fileConfig.set(rsPath + ".type", "IRON_INGOT");
                            if (!fileConfig.contains(rsPath + ".spawnloc.x"))
                                fileConfig.set(rsPath + ".spawnloc.x", inWorld.getSpawnLocation().getX());
                            if (!fileConfig.contains(rsPath + ".spawnloc.y"))
                                fileConfig.set(rsPath + ".spawnloc.y", inWorld.getSpawnLocation().getY());
                            if (!fileConfig.contains(rsPath + ".spawnloc.z"))
                                fileConfig.set(rsPath + ".spawnloc.z", inWorld.getSpawnLocation().getZ());
                            if (!fileConfig.contains(rsPath + ".duration-spawn"))
                                fileConfig.set(rsPath + ".duration-spawn", 10f);
                            String typeInString = fileConfig.getString(rsPath + ".type");
                            if (typeInString == null)
                                typeInString = "";
                            Material typeSpawnResource = Material.getMaterial(typeInString.toUpperCase());

                            // Check if it is a valid currency type
                            if (typeSpawnResource == null)
                                continue;
                            if (!PluginStaticFunc.isCurrencyInGame(typeSpawnResource))
                                continue;

                            Location spawnLoc = new Location(inWorld, fileConfig.getDouble(rsPath + ".spawnloc.x"),
                                    fileConfig.getDouble(rsPath + ".spawnloc.y"),
                                    fileConfig.getDouble(rsPath + ".spawnloc.z"));
                            float spawnDur = (float) fileConfig.getDouble(rsPath + ".duration-spawn");
                            // Create resource spawner instance
                            ResourceSpawner rsp;
                            if (spawnDur <= 0f)
                                rsp = new ResourceSpawner(rs, spawnLoc, typeSpawnResource);
                            else
                                rsp = new ResourceSpawner(rs, spawnLoc, typeSpawnResource, spawnDur);
                            rsp.getLooper().setLocked(true);
                            rsList.add(rsp);
                        }
                    }
                    refHolder.addAll(rsList);
                    listOfLocked.add(new LockedResourceSpawner(new Location(inWorld,
                            fileConfig.getDouble(path + "." + cn + ".loc.x"),
                            fileConfig.getDouble(path + "." + cn + ".loc.y"),
                            fileConfig.getDouble(path + "." + cn + ".loc.z")),
                            requirements, rsList));
                }
            }
        }
        return listOfLocked;
    }

    /**
     * Get all public resource spawners, codename only.
     *
     * @param mapName Original map name
     * @return List of codename
     */
    public List<String> getPublicRSCodename(String mapName) {
        List<String> listRSCodename = new ArrayList<>();
        String path = String.format(RESOURCE_SPAWNER_PATH + ".PUBLIC", mapName);
        if (getWorldNames().contains(mapName)) {
            if (!fileConfig.contains(path))
                fileConfig.createSection(path, new HashMap<>());
            ConfigurationSection csPath = fileConfig.getConfigurationSection(path);
            if (csPath != null)
                listRSCodename.addAll(csPath.getKeys(false));

            // Get all locked resource spawner codename
            String lockedEntPath = String.format(LOCKED_ENTITY_PATH, mapName);
            ConfigurationSection lockedCS = fileConfig.getConfigurationSection(lockedEntPath);
            if (lockedCS != null) {
                for (String le : lockedCS.getKeys(false)) {
                    LockedEntityType lockType = LockedEntityType.values()[fileConfig.getInt(lockedEntPath +
                            "." + le + ".type-lock")];
                    if (lockType == LockedEntityType.RESOURCE_SPAWNER_LOCK) {
                        String rsLockPath = String.format("%s.%s.rs-lock", lockedEntPath, le);
                        ConfigurationSection rsLockCS = fileConfig.getConfigurationSection(rsLockPath);
                        if (rsLockCS != null)
                            listRSCodename.addAll(rsLockCS.getKeys(false));
                    }
                }
            }
        }
        return listRSCodename;
    }

    /**
     * Get all locked entities codename.
     *
     * @param mapName Original map name
     * @return List of locked entity codename
     */
    public List<String> getLockedCodename(String mapName) {
        String path = String.format(LOCKED_ENTITY_PATH, mapName);
        if (!fileConfig.contains(path))
            fileConfig.createSection(path, new HashMap<>());
        ConfigurationSection cs = fileConfig.getConfigurationSection(path);
        List<String> lockedCL = new ArrayList<>();
        if (cs != null)
            lockedCL.addAll(cs.getKeys(false));
        return lockedCL;
    }
    //#endregion

    //#region Setter Methods
    /**
     * Set team color prefix, you can use any of string but the plugin itself will decide if it is a color.
     *
     * @param mapName     Original map name
     * @param teamName    On team with name
     * @param colorPrefix Color choice
     */
    public void setRawColor(String mapName, String teamName, String colorPrefix) {
        // Check available team
        List<String> team = getTeamNames(mapName, true);
        if (!team.contains(teamName))
            return;

        // Set configuration
        String path = String.format(TEAM_PATH + ".%s.color", mapName, teamName);
        fileConfig.set(path, colorPrefix);
    }

    /**
     * Set or overwrite queue location, this will also check if the registered world exists.
     *
     * @param mapName  Original map name
     * @param queueLoc Location set
     * @return true if successfully set, else then false
     */
    public boolean setQueuePos(String mapName, Location queueLoc) {
        if (!getWorldNames().contains(mapName))
            return false;
        String path = String.format("worlds.%s.queueloc", mapName);
        fileConfig.set(path + ".x", queueLoc.getX());
        fileConfig.set(path + ".y", queueLoc.getY());
        fileConfig.set(path + ".z", queueLoc.getZ());
        return true;
    }

    /**
     * Set world border, save it into database.
     *
     * @param mapName Original map name
     * @param blockSize Size in block
     * @param shrunkBorder Set true to set how wide border after shrunk
     */
    public void setBorderData(String mapName, int blockSize, boolean shrunkBorder) {
        String path = String.format(WORLD_BORDER_PATH, mapName);
        if (!fileConfig.contains(path))
            fileConfig.createSection(path);
        if (shrunkBorder)
            fileConfig.set(path + ".shrunk", blockSize);
        else
            fileConfig.set(path + ".normal", blockSize);
    }

    /**
     * Set or overwrite team spawn location.
     *
     * @param mapName  Original map name
     * @param teamName On team with name
     * @param setLoc   Location set
     * @return true if successfully set, else then false;
     */
    public boolean setTeamSpawner(String mapName, String teamName, Location setLoc) {
        List<String> team = getTeamNames(mapName, true);
        if (!team.contains(teamName))
            return false;
        String path = String.format(TEAM_PATH + ".%s.spawner", mapName, teamName);
        if (!fileConfig.contains(path))
            fileConfig.createSection(path);
        fileConfig.set(path + ".x", setLoc.getX());
        fileConfig.set(path + ".y", setLoc.getY());
        fileConfig.set(path + ".z", setLoc.getZ());
        return true;
    }

    /**
     * Set team bed location.
     *
     * @param mapName  Original map name
     * @param teamName On team with name
     * @param bedLoc   Bed location that has just placed
     * @return true if the set is done, if team not exists then false
     */
    public boolean setBedLocation(String mapName, String teamName, Location bedLoc) {
        List<String> team = getTeamNames(mapName, true);
        if (!team.contains(teamName))
            return false;
        String path = String.format(TEAM_PATH + ".%s.bed-location", mapName, teamName);
        if (!fileConfig.contains(path))
            fileConfig.createSection(path);
        fileConfig.set(path + ".x", bedLoc.getBlockX());
        fileConfig.set(path + ".y", bedLoc.getBlockY());
        fileConfig.set(path + ".z", bedLoc.getBlockZ());
        return true;
    }

    /**
     * Set team area buffer zone, this area gives effect of any potion effects.
     * This function automatically recalculate minimum and maximum value of location.
     * Each team can only have 1 buffer area.
     *
     * @param mapName  Original map name
     * @param teamName On team with name
     * @param minLoc   Location minimum value
     * @param maxLoc   Location maximum value
     */
    public void setAreaBuff(String mapName, String teamName, Location minLoc, Location maxLoc) {
        List<String> team = getTeamNames(mapName, true);
        if (!team.contains(teamName))
            teamName = "PUBLIC";

        // Comparing Minimal and Maximal value correction
        double temp;
        if (minLoc.getX() > maxLoc.getX()) {
            temp = minLoc.getX();
            minLoc.setX(maxLoc.getX());
            maxLoc.setX(temp);
        }
        if (minLoc.getY() > maxLoc.getY()) {
            temp = minLoc.getY();
            minLoc.setY(maxLoc.getY());
            maxLoc.setY(temp);
        }
        if (minLoc.getZ() > maxLoc.getZ()) {
            temp = minLoc.getZ();
            minLoc.setZ(maxLoc.getZ());
            maxLoc.setZ(temp);
        }

        // Check if exists in config file
        String path = String.format("worlds.%s.buffer-zone.%s", mapName, teamName);
        if (!fileConfig.contains(path))
            fileConfig.createSection(path);
        if (teamName.equals("PUBLIC")) {
            // Overwrite list of buffer area
            Set<String> setListBuffer = fileConfig.getConfigurationSection(path).getKeys(false);
            List<Vector> minVec = new ArrayList<>(), maxVec = new ArrayList<>() ;
            for (int i = 0; i < setListBuffer.size(); i++) {
                minVec.add(new Vector(fileConfig.getDouble(String.format("%s.bz%d.minx", path, i)),
                        fileConfig.getDouble(String.format("%s.bz%d.miny", path, i)),
                        fileConfig.getDouble(String.format("%s.bz%d.minz", path, i))));
                maxVec.add(new Vector(fileConfig.getDouble(String.format("%s.bz%d.maxx", path, i)),
                        fileConfig.getDouble(String.format("%s.bz%d.maxy", path, i)),
                        fileConfig.getDouble(String.format("%s.bz%d.maxz", path,i))));
            }
            minVec.add(new Vector(minLoc.getX(), minLoc.getY(), minLoc.getZ()));
            maxVec.add(new Vector(maxLoc.getX(), maxLoc.getY(), maxLoc.getZ()));
            overwritePublicBZList(path, minVec, maxVec);
        } else {
            fileConfig.set(path + ".minx", minLoc.getX());
            fileConfig.set(path + ".miny", minLoc.getY());
            fileConfig.set(path + ".minz", minLoc.getZ());
            fileConfig.set(path + ".maxx", maxLoc.getX());
            fileConfig.set(path + ".maxy", maxLoc.getY());
            fileConfig.set(path + ".maxz", maxLoc.getZ());
        }
    }

    /**
     * Set game default values for current map.
     *
     * @param mapName Original map name
     * @param key Key to be set
     * @param x Set value
     */
    public boolean setDefaultValue(String mapName, String key, double x) {
        String path = String.format(DEFAULT_VALUE_PATH + ".%s", mapName, key);
        if (!DEFAULT_KEYS.contains(key))
            return false;
        fileConfig.set(path, x);
        return true;
    }

    /**
     * Set game default values for current map.
     *
     * @param mapName Original map name
     * @param key Key to be set
     * @param x Set value
     */
    public boolean setDefaultValue(String mapName, String key, int x) {
        String path = String.format(DEFAULT_VALUE_PATH + ".%s", mapName, key);
        if (!DEFAULT_KEYS.contains(key))
            return false;
        fileConfig.set(path, x);
        return true;
    }

    /**
     * Set game default values for current map.
     *
     * @param mapName Original map name
     * @param key Key to be set
     * @param x Set value
     */
    public boolean setDefaultValue(String mapName, String key, float x) {
        String path = String.format(DEFAULT_VALUE_PATH + ".%s", mapName, key);
        if (!DEFAULT_KEYS.contains(key))
            return false;
        fileConfig.set(path, x);
        return true;
    }

    /**
     * Set locked placeable entity, which player cannot interact with.
     *
     * @param mapName    Original map name
     * @param codename   Codename for this entity, must be unique
     * @param onLocation Location where did player set
     * @return True if successfully set, if map not exists then it's
     */
    public boolean setLockedRequest(String mapName, String codename, @Nonnull Location onLocation) {
        if (!getWorldNames().contains(mapName))
            return false;
        // Check if locked entity already exists
        String path = String.format(LOCKED_ENTITY_PATH + ".%s", mapName, codename);
        if (!fileConfig.contains(path))
            fileConfig.createSection(path);
        fileConfig.set(path + ".loc.x", onLocation.getX());
        fileConfig.set(path + ".loc.y", onLocation.getY());
        fileConfig.set(path + ".loc.z", onLocation.getZ());
        fileConfig.set(path + ".type-lock", LockedEntityType.NORMAL_LOCK.ordinal());
        return true;
    }

    /**
     * Set locked placeable entity, which player cannot interact with. Public resource spawner only.
     *
     * @param mapName    Original map name
     * @param codename   Codename for this entity, must be unique
     * @param onLocation Location where did player set
     * @param codenameRS Resource spawner codename in public
     * @return True if successfully set, if map not exists then it's
     */
    public boolean setLockedRequest(String mapName, String codename, @Nonnull String codenameRS,
                                    @Nullable Location onLocation) {
        if (!getWorldNames().contains(mapName))
            return false;

        // Move resource spawner's classification into Locked section
        String tPath = String.format(RESOURCE_SPAWNER_PATH + ".PUBLIC.%s", mapName, codenameRS);
        if (!fileConfig.contains(tPath))
            return false;

        // Check if locked entity already exists
        String path = String.format(LOCKED_ENTITY_PATH + ".%s", mapName, codename);
        if (!fileConfig.contains(path))
            fileConfig.createSection(path);
        if (onLocation != null) {
            fileConfig.set(path + ".loc.x", onLocation.getX());
            fileConfig.set(path + ".loc.y", onLocation.getY());
            fileConfig.set(path + ".loc.z", onLocation.getZ());
            fileConfig.set(path + ".type-lock", LockedEntityType.RESOURCE_SPAWNER_LOCK.ordinal());
        }
        String ttPath = path + ".rs-lock." + codenameRS;
        if (!fileConfig.contains(ttPath))
            fileConfig.createSection(ttPath);
        fileConfig.set(ttPath + ".type", fileConfig.getString(tPath + ".type"));
        fileConfig.set(ttPath + ".spawnloc.x", fileConfig.getDouble(tPath + ".spawnloc.x"));
        fileConfig.set(ttPath + ".spawnloc.y", fileConfig.getDouble(tPath + ".spawnloc.y"));
        fileConfig.set(ttPath + ".spawnloc.z", fileConfig.getDouble(tPath + ".spawnloc.z"));
        fileConfig.set(ttPath + ".duration-spawn", fileConfig.getDouble(tPath + ".duration-spawn"));
        fileConfig.set(tPath, null);
        return true;
    }

    /**
     * Set a resource spawner seconds per spawn.
     *
     * @param mapName  Original map name
     * @param teamName On team with name
     * @param codename Resource spawner has their own unique codename
     * @param interval Seconds per spawn duration
     * @return true if successfully changed, else then false
     */
    public boolean setRSInterval(String mapName, String teamName, String codename, float interval) {
        // If team name not exists then consider it as public
        if (!getTeamNames(mapName, true).contains(teamName))
            teamName = "PUBLIC";

        // Check if the resource spawner is exists
        String path = String.format(RESOURCE_SPAWNER_PATH + ".%s.%s", mapName, teamName, codename);
        if (!fileConfig.contains(path)) {
            // Get all locked resource spawners
            String lockedEntPath = String.format(LOCKED_ENTITY_PATH, mapName);
            ConfigurationSection lockCS = fileConfig.getConfigurationSection(lockedEntPath);
            if (lockCS != null) {
                for (String le : lockCS.getKeys(false)) {
                    LockedEntityType lockType = LockedEntityType.values()[fileConfig.getInt(lockedEntPath +
                            "." + le + ".type-lock")];
                    if (lockType == LockedEntityType.RESOURCE_SPAWNER_LOCK) {
                        String rsLockPath = String.format("%s.%s.rs-lock", lockedEntPath, le);
                        ConfigurationSection rsLockCS = fileConfig.getConfigurationSection(rsLockPath);
                        if (rsLockCS != null) {
                            for (String rs : rsLockCS.getKeys(false)) {
                                if (codename.equals(rs)) {
                                    fileConfig.set(String.format("%s.%s.duration-spawn", rsLockPath, rs), interval);
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
            return false;
        }

        fileConfig.set(path + ".duration-spawn", interval);
        return true;
    }

    /**
     * Set resource spawner resource type spawner.
     *
     * @param mapName Original map name
     * @param teamName Team owned resource spawner
     * @param codename Resource spawner codename
     * @param type Material spawn type
     * @return True if successfully set, if codename not exists then it returns false
     */
    public boolean setRSTypeSpawner(String mapName, String teamName, String codename, Material type) {
        if (!getTeamNames(mapName, true).contains(teamName))
            teamName = "PUBLIC";

        String path = String.format(RESOURCE_SPAWNER_PATH + ".%s.%s", mapName, teamName, codename);
        if (!fileConfig.contains(path)) {
            // Get all locked resource spawners
            String lockedEntPath = String.format(LOCKED_ENTITY_PATH, mapName);
            ConfigurationSection lockCS = fileConfig.getConfigurationSection(lockedEntPath);
            if (lockCS != null) {
                for (String le : lockCS.getKeys(false)) {
                    LockedEntityType lockType = LockedEntityType.values()[fileConfig.getInt(lockedEntPath + "." + le + ".type-lock")];
                    if (lockType == LockedEntityType.RESOURCE_SPAWNER_LOCK) {
                        String rsLockPath = String.format("%s.%s.rs-lock", lockedEntPath, le);
                        ConfigurationSection rsLockCS = fileConfig.getConfigurationSection(rsLockPath);
                        if (rsLockCS != null) {
                            for (String rs : rsLockCS.getKeys(false)) {
                                if (codename.equals(rs)) {
                                    fileConfig.set(String.format("%s.%s.type", rsLockPath, rs), type.toString().toLowerCase());
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
            return false;
        }

        fileConfig.set(path + ".type", type.toString().toLowerCase());
        return true;
    }

    /**
     * Set event order. Procedurally will be sorted by order when the event will be invoked.
     *
     * @param mapName Original map name
     * @param eventName Event name
     * @param order Order index
     * @return True if successfully set, if event not exists then it returns false
     */
    public boolean setEventOrder(String mapName, String eventName, int order) {
        String path = String.format(TIMELINE_EVENT_PATH + ".%s", mapName, eventName);
        if (!fileConfig.contains(path))
            return false;
        fileConfig.set(path + ".order", order);
        return true;
    }

    /**
     * Set max player for each team.
     *
     * @param mapName Original map name
     * @param count Player max count
     */
    public void setMaxPlayerInTeam(String mapName, int count) {
        String path = String.format("worlds.%s.max-player-per-team", mapName);
        fileConfig.set(path, count);
    }

    /**
     * Set available flag.
     *
     * @param mapName Original map name
     * @param flag Flag
     * @param val True or False
     */
    public void setFlag(String mapName, @Nonnull InGameFlags flag, boolean val) {
        String path = String.format(FLAG_PATH, mapName);
        if (!fileConfig.contains(path))
            fileConfig.createSection(path);
        fileConfig.set(path + "." + flag.toString(), val);
    }
    //#endregion

    //#region Creator and Adder Methods
    /**
     * Insert a new team into bedwars team list.
     *
     * @param mapName     Original map name
     * @param teamName    Name of new team
     * @param colorPrefix Raw color for team prefix color
     */
    public void addTeam(String mapName, String teamName, @Nullable String colorPrefix) {
        // Check existing team
        String path = String.format(TEAM_PATH + ".%s", mapName, teamName);
        List<String> team = getTeamNames(mapName, true);
        if (team.contains(teamName)) {
            fileConfig.set(path + ".is-removed", false);
            return;
        }

        // Proceed Insertion
        fileConfig.createSection(path, new HashMap<>());
        if (colorPrefix != null)
            fileConfig.set(path + ".color", colorPrefix);
        else
            fileConfig.set(path + ".color", "white");
        String bufferZonePath = String.format("worlds.%s.buffer-zone", mapName);
        fileConfig.createSection(bufferZonePath + "." + teamName, new HashMap<>());
        fileConfig.createSection(bufferZonePath + "-effects." + teamName, new HashMap<>());
        String resourceSpawnerPath = String.format(RESOURCE_SPAWNER_PATH + ".%s", mapName, teamName);
        fileConfig.createSection(resourceSpawnerPath, new HashMap<>());
    }

    /**
     * Add event into list
     *
     * @param mapName Original map name
     */
    public void addEventTimeline(String mapName, @Nonnull BedwarsTimelineEvent eventSample) {
        if (!getWorldNames().contains(mapName))
            return;
        String path = String.format(TIMELINE_EVENT_PATH + ".%s", mapName, eventSample.getName());
        if (!fileConfig.contains(path))
            fileConfig.createSection(path);
        fileConfig.set(path + ".order", eventSample.getTimelineOrder());
        fileConfig.set(path + ".type", eventSample.getEventType().toString());
        fileConfig.set(path + ".trigger-in-seconds", eventSample.getSecTrigger());
    }

    /**
     * Add a new resource spawner to the map.
     *
     * @param mapName  Original map name
     * @param teamName On team by name
     * @param spawner  Already created spawner
     */
    public void addResourceSpawner(String mapName, String teamName, @Nonnull ResourceSpawner spawner) {
        // Check available team
        List<String> team = getTeamNames(mapName, true);
        if (!team.contains(teamName))
            teamName = "PUBLIC";
        String path = String.format(RESOURCE_SPAWNER_PATH + ".%s", mapName, teamName);
        if (!fileConfig.contains(path))
            fileConfig.createSection(path, new HashMap<>());

        // Add into config file
        String rsPath = path + "." + spawner.getCodename();
        fileConfig.set(rsPath, new HashMap<>());
        fileConfig.set(rsPath + ".type", spawner.getCurrency().toString().toLowerCase());
        fileConfig.set(rsPath + ".spawnloc.x", spawner.getSpawnLocation().getX());
        fileConfig.set(rsPath + ".spawnloc.y", spawner.getSpawnLocation().getY());
        fileConfig.set(rsPath + ".spawnloc.z", spawner.getSpawnLocation().getZ());
        fileConfig.set(rsPath + ".duration-spawn", spawner.getSecondsPerSpawn());
    }

    /**
     * Add a new shop spawner to the map.
     *
     * @param mapName        Original map name
     * @param type           Type of shop
     * @param playerLocation Player current location
     * @return true if successfuly added, else then false
     */
    public boolean addShopLocationSpawn(String mapName, @Nonnull BedwarsShopType type, Location playerLocation) {
        String path = String.format("worlds.%s.shop-location.%s", mapName, type.toString());
        if (!getWorldNames().contains(mapName))
            return false;
        if (!fileConfig.contains(path))
            fileConfig.createSection(path, new HashMap<>());

        // Add shop spawn location into config file
        String newPath = String.format("%s.shop%d", path, fileConfig.getConfigurationSection(path)
                .getKeys(false).size());
        fileConfig.set(newPath + ".x", playerLocation.getX());
        fileConfig.set(newPath + ".y", playerLocation.getY());
        fileConfig.set(newPath + ".z", playerLocation.getZ());
        return true;
    }

    /**
     * Add a potion effect to buffer zone.
     *
     * @param mapName    Original map name
     * @param teamName   On team with name
     * @param effectType Potion effect included in buffer zone
     * @return True if successfully added, if the effect is already in list then it returns false
     */
    public boolean addTeamAreaPotionEffect(String mapName, String teamName, PotionEffectType effectType) {
        // Check available team
        List<String> team = getTeamNames(mapName, true);
        if (!team.contains(teamName))
            teamName = "PUBLIC";
        // Add into config file
        String path = String.format("worlds.%s.buffer-zone-effects.%s", mapName, teamName);
        if (!fileConfig.contains(path))
            fileConfig.createSection(path);
        String tPath = path + "." + effectType.getName();
        if (!fileConfig.contains(tPath)) {
            fileConfig.createSection(tPath);
            fileConfig.set(tPath + ".seconds-hit", 5f);
            fileConfig.set(tPath + ".for-opposition", false);
            return true;
        }
        return false;
    }

    /**
     * Add requirement on locked entity.
     *
     * @param mapName  Original map name
     * @param codename Locked entity has their own codename
     * @param currency  Currency type needed
     * @return True if successfully set, if the locked entity not exists then it returns false
     */
    public boolean addLockedRequest(String mapName, String codename, Material currency, int amount) {
        String path = String.format(LOCKED_ENTITY_PATH + ".%s", mapName, codename);
        if (!fileConfig.contains(path))
            return false;
        if (!fileConfig.contains(path + ".request"))
            fileConfig.createSection(path + ".request");
        path = path + ".request." + currency.toString().toLowerCase();
        if (amount == 0) {
            fileConfig.set(path, null);
        } else {
            fileConfig.set(path, amount);
        }
        return true;
    }
    //#endregion

    //#region Remove and Delete Methods
    /**
     * Delete a team from this map.
     *
     * @param mapName  Original map name
     * @param teamName On team with name
     * @return true if successfully deleted, if team not exists then false
     */
    public boolean deleteTeam(String mapName, String teamName, boolean permanentDelete) {
        // Check if team exists
        List<String> team = getTeamNames(mapName, true);
        if (!team.contains(teamName))
            return false;

        // Proceed Deletion
        String path = String.format(TEAM_PATH + ".%s", mapName, teamName);
        if (permanentDelete) {
            fileConfig.set(path, null);
            String bufferZonePath = String.format("worlds.%s.buffer-zone", mapName);
            fileConfig.set(bufferZonePath + "." + teamName, null);
            fileConfig.set(bufferZonePath + "-effects." + teamName, null);
            String resourceSpawnerPath = String.format(RESOURCE_SPAWNER_PATH + ".%s", mapName, teamName);
            fileConfig.set(resourceSpawnerPath, null);
        } else {
            fileConfig.set(path + ".is-removed", true);
        }
        return true;
    }

    /**
     * Delete an existing resource spawner in world or map.
     * After you got the list, you need to sort it by order.
     *
     * @param mapName  Original map name
     * @param teamName On team by name
     * @param codeName Every resource spawner has their own unique codename
     * @return True if successfully deleted, else then false
     */
    public boolean deleteResourceSpawner(String mapName, String teamName, String codeName) {
        String path = String.format(RESOURCE_SPAWNER_PATH + ".%s", mapName, teamName);
        List<String> teams = getTeamNames(mapName, true);

        // Check if team is exists but resource spawner is not
        if (teams.contains(teamName) && !fileConfig.contains(path)) {
            fileConfig.createSection(path, new HashMap<>());
            return false;
        }

        // Consider a public resource spawner if the team not exists
        if (!fileConfig.contains(path)) {
            path = String.format(RESOURCE_SPAWNER_PATH + ".PUBLIC", mapName);
            // Check PUBLIC section exists
            if (!fileConfig.contains(path)) {
                // Consider create a public section if not exists
                fileConfig.createSection(path, new HashMap<>());
                return false;
            }
        }

        // Check team resource spawner by codename
        if (!fileConfig.contains(path + "." + codeName))
            return false;
        fileConfig.set(path + "." + codeName, null);
        return true;
    }

    /**
     * Delete an existing shop spawn location. User need to include the index to choose a specific shop spawn location.
     *
     * @param mapName Original map name
     * @param type    Type of shop that will be deleted
     * @param index   List index
     * @return True if it is successfully deleted, else then false
     */
    public boolean deleteShopLocation(String mapName, BedwarsShopType type, int index) {
        String path = String.format("worlds.%s.shop-location.%s", mapName, type.toString());
        // Check if type exists, if not then create a section
        if (!fileConfig.contains(path)) {
            fileConfig.createSection(path);
            return false;
        }
        Map<String, Vector> indexesInConfig = new HashMap<>();
        // Get number in sub-string
        String pickedIndex = null;
        ConfigurationSection cs = fileConfig.getConfigurationSection(path);
        assert cs != null;
        for (String ic : cs.getKeys(false)) {
            String numString = ic.substring(4);
            if (Integer.toString(index).equals(numString))
                pickedIndex = ic;
            Vector vectorLoc = new Vector(fileConfig.getDouble(path + "." + ic + ".x"), fileConfig
                    .getDouble(path + "." + ic + ".y"), fileConfig.getDouble(path + "." + ic + ".z"));
            indexesInConfig.put(ic, vectorLoc);
        }
        // Check if picked index is not null
        if (pickedIndex == null)
            return false;
        fileConfig.set(path + "." + pickedIndex, null);
        indexesInConfig.remove(pickedIndex);
        // Overwrite the list in config file
        List<Vector> listVectors = new ArrayList<>(indexesInConfig.values());
        overwriteShopList(path, listVectors);
        return true;
    }

    /**
     * Delete an existing event on timeline event in game.
     *
     * @param mapName   Original map name
     * @param eventName Event display name that will be deleted
     * @return True if it is successfully deleted, else then false
     */
    public boolean deleteTimelineEvent(String mapName, String eventName) {
        String path = String.format(TIMELINE_EVENT_PATH + ".%s", mapName, eventName);
        if (!fileConfig.contains(path))
            return false;
        fileConfig.set(path, null);
        return true;
    }

    /**
     * Delete by index a public buffer zone.
     *
     * @param mapName Original map name
     * @param index   List index to be delete
     * @return True if successfully deleted, if world not exists or index exceeded then it returns false
     */
    public boolean deleteAreaBuffPublic(String mapName, int index) {
        if (!getWorldNames().contains(mapName))
            return false;
        // Overwrite list of buffer area
        String path = String.format("worlds.%s.buffer-zone.PUBLIC", mapName);
        ConfigurationSection cs = fileConfig.getConfigurationSection(path);
        Set<String> setListBuffer = new HashSet<>();
        if (cs != null)
            setListBuffer.addAll(cs.getKeys(false));
        if (index >= setListBuffer.size())
            return false;
        List<Vector> minVec = new ArrayList<>(), maxVec = new ArrayList<>();
        for (int i = 0; i < setListBuffer.size(); i++) {
            minVec.add(new Vector(fileConfig.getDouble(String.format("%s.bz%d.minx", path, i)),
                    fileConfig.getDouble(String.format("%s.bz%d.miny", path, i)),
                    fileConfig.getDouble(String.format("%s.bz%d.minz", path, i))));
            maxVec.add(new Vector(fileConfig.getDouble(String.format("%s.bz%d.maxx", path, i)),
                    fileConfig.getDouble(String.format("%s.bz%d.maxy", path, i)),
                    fileConfig.getDouble(String.format("%s.bz%d.maxz", path,i))));
        }
        overwritePublicBZList(path, minVec, maxVec);
        return true;
    }

    /**
     * Delete the locked entity.
     *
     * @param mapName  Original map name
     * @param codename Locked entity codename
     * @return True if successfully deleted, if codename does not exists then it returns false
     */
    public boolean deleteLockedKey(String mapName, String codename) {
        String path = String.format(LOCKED_ENTITY_PATH + ".%s", mapName, codename);
        if (!fileConfig.contains(path))
            return false;

        // Move from locked Resource spawner to the public
        if (fileConfig.contains(path + ".rs-lock")) {
            ConfigurationSection cs = fileConfig.getConfigurationSection(path + ".rs-lock");
            if (cs != null) {
                for (String rsName : cs.getKeys(false)) {
                    String rsPath = path + ".rs-lock." + rsName;
                    String rsPublicPath = String.format(RESOURCE_SPAWNER_PATH + ".PUBLIC.%s", mapName, rsName);
                    if (!fileConfig.contains(rsPublicPath))
                        fileConfig.createSection(rsPublicPath);
                    fileConfig.set(rsPublicPath + ".type", fileConfig.getString(rsPath + ".type"));
                    fileConfig.set(rsPublicPath + ".spawnloc.x", fileConfig.getDouble(rsPath + ".spawnloc.x"));
                    fileConfig.set(rsPublicPath + ".spawnloc.y", fileConfig.getDouble(rsPath + ".spawnloc.y"));
                    fileConfig.set(rsPublicPath + ".spawnloc.z", fileConfig.getDouble(rsPath + ".spawnloc.z"));
                    fileConfig.set(rsPublicPath + ".duration-spawn", fileConfig.getDouble(rsPath + ".duration-spawn"));
                }
            }
        }
        fileConfig.set(path, null);
        return true;
    }
    //#endregion

    //#region Utility and Other Methods
    /**
     * This will delete all room folder from last time server was shutdown.
     */
    public void roomDeletionHandle() {
        String path = "room-folders-will-be-deleted";
        if (fileConfig.contains(path)) {
            // Get all world folders
            File[] f = Bukkit.getWorldContainer().listFiles();
            Map<String, File> nameFile = new HashMap<>();
            if (f == null)
                return;
            for (File fi : f) {
                if (fi.isDirectory())
                    nameFile.put(fi.getName(), fi);
            }

            // Delete each room
            List<String> deleteRooms = fileConfig.getStringList(path);
            for (String r : deleteRooms)
                if (nameFile.containsKey(r))
                    Bukkit.getScheduler().scheduleSyncDelayedTask(SleepingWarsPlugin.getPlugin(),
                            new DeleteWorldDelayed(nameFile.get(r)), 100L);
        }
        fileConfig.set(path, null);
    }

    public void insertRoomNames(List<String> roomNameList) {
        String path = "room-folders-will-be-deleted";
        fileConfig.set(path, roomNameList);
    }

    /**
     * Count overall resource spawners in this map
     *
     * @param mapName Original map name
     * @return amount of existing resource spawners
     */
    public int countOverallRS(String mapName) {
        String path = String.format(RESOURCE_SPAWNER_PATH, mapName);

        // Consider a public resource spawner if the team not exists
        if (!fileConfig.contains(path)) {
            fileConfig.createSection(path, new HashMap<>());
            return 0;
        }

        // Count all resource spawners
        int amountSet = 0;
        for (String rsp : fileConfig.getConfigurationSection(path).getKeys(false))
            amountSet += fileConfig.getConfigurationSection(path + "." + rsp).getKeys(false).size();
        return amountSet;
    }

    /**
     * Count events in current world timeline.
     *
     * @param mapName Original map name
     * @return Amount of events in timeline
     */
    public int countEventsInTimeline(String mapName) {
        String path = String.format(TIMELINE_EVENT_PATH, mapName);

        // Consider if the timeline section is not exists
        if (!fileConfig.contains(path)) {
            fileConfig.createSection(path, new HashMap<>());
            return 0;
        }

        ConfigurationSection cs = fileConfig.getConfigurationSection(path);
        if (cs != null)
            return cs.getKeys(false).size();
        return 0;
    }

    @Override
    public void load() {
        if (!fileConfig.contains("worlds"))
            fileConfig.set("worlds", new HashMap<>());

        // Loop by World Name
        ConfigurationSection cs = fileConfig.getConfigurationSection("worlds");
        if (cs != null) {
            for (String key : cs.getKeys(false)) {
                // load world creator
                WorldCreator creator = new WorldCreator(key).environment(Environment.values()[fileConfig
                        .getInt("worlds." + key + ".env")])
                        .hardcore(fileConfig.getBoolean("worlds." + key + ".hardcore"))
                        .generateStructures(fileConfig.getBoolean("worlds." + key + ".structure"));
                creator.generator(new VoidGenerator());
                Bukkit.createWorld(creator);
            }
        }
    }

    /**
     * Only used when the world is going to be created.
     *
     * @param creator World creator is needed to create a world
     */
    public void saveWorldConfig(WorldCreator creator) {
        World worldSample = Bukkit.createWorld(creator);

        // Set world border
        WorldBorder border = worldSample.getWorldBorder();
        border.setCenter(new Location(worldSample, 0, 0, 0));
        border.setSize(1024d);

        // Set world config
        worldSample.setAutoSave(false);
        worldSample.setKeepSpawnInMemory(false);
        worldSample.setDifficulty(Difficulty.PEACEFUL);
        worldSample.setAnimalSpawnLimit(0);
        worldSample.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        worldSample.setTime(0);

        // Add initial blocks of bedrocks on mid point
        for (int x = -3; x < 4; x++) {
            for (int z = -3; z < 4; z++)
                worldSample.getBlockAt(new Location(worldSample, x, 45, z)).setType(Material.BEDROCK);
        }

        // Get world Attributes
        String worldName = worldSample.getName();
        Location defaultSpawnLoc = worldSample.getSpawnLocation();

        // Generic world path
        String worldPathName = String.format("worlds.%s", worldName);

        // Name of world and World Creator Config
        fileConfig.createSection(worldPathName, new HashMap<>());
        fileConfig.set(worldPathName + ".env", creator.environment().ordinal());
        fileConfig.set(worldPathName + ".structure", creator.generateStructures());
        fileConfig.set(worldPathName + ".hardcore", creator.hardcore());

        // Initial Teams in game
        fileConfig.createSection(worldPathName + ".teams", new HashMap<>());
        fileConfig.set(String.format("%s.teams.Yellow", worldPathName), new HashMap<>());
        fileConfig.set(String.format("%s.teams.Red", worldPathName), new HashMap<>());
        fileConfig.set(String.format("%s.teams.Blue", worldPathName), new HashMap<>());
        fileConfig.set(String.format("%s.teams.Green", worldPathName), new HashMap<>());

        // Set color prefixes
        fileConfig.set(String.format("%s.teams.Yellow.color", worldPathName), "yellow");
        fileConfig.set(String.format("%s.teams.Red.color", worldPathName), "red");
        fileConfig.set(String.format("%s.teams.Blue.color", worldPathName), "blue");
        fileConfig.set(String.format("%s.teams.Green.color", worldPathName), "green");

        // Queue Location of Game
        fileConfig.createSection(worldPathName + ".queueloc", new HashMap<>());
        fileConfig.set(worldPathName + ".queueloc.x", defaultSpawnLoc.getX());
        fileConfig.set(worldPathName + ".queueloc.y", defaultSpawnLoc.getX());
        fileConfig.set(worldPathName + ".queueloc.z", defaultSpawnLoc.getX());

        // Empty Resource Spawners
        fileConfig.createSection(worldPathName + ".resource-spawners", new HashMap<>());
        fileConfig.createSection(worldPathName + ".resource-spawners.PUBLIC", new HashMap<>());

        // Empty Timeline Event
        fileConfig.createSection(worldPathName + ".timeline", new HashMap<>());

        // Empty Shop Location
        fileConfig.createSection(worldPathName + ".shop-location", new HashMap<>());

        // Empty Flags
        fileConfig.createSection(worldPathName + ".flag", new HashMap<>());

        // Empty Default Values
        fileConfig.createSection(worldPathName + ".default-values", new HashMap<>());
        fileConfig.set(worldPathName + ".default-values.iron_ingot-spawner-freq", 1f);
        fileConfig.set(worldPathName + ".default-values.gold_ingot-spawner-freq", 4f);
        fileConfig.set(worldPathName + ".default-values.diamond-spawner-freq", 30f);
        fileConfig.set(worldPathName + ".default-values.emerald-spawner-freq", 60f);
    }

    /**
     * List of shop only overwrite the new data.
     *
     * @param path    Data path in config file
     * @param listLoc List of locations
     */
    private void overwriteShopList(String path, List<Vector> listLoc) {
        fileConfig.set(path, new HashMap<>());
        for (int i = 0; i < listLoc.size(); i++) {
            Vector thisLocVector = listLoc.get(i);
            fileConfig.createSection(String.format("%s.shop%d", path, i), new HashMap<>());
            fileConfig.set(String.format("%s.shop%d.x", path, i), thisLocVector.getX());
            fileConfig.set(String.format("%s.shop%d.y", path, i), thisLocVector.getY());
            fileConfig.set(String.format("%s.shop%d.z", path, i), thisLocVector.getZ());
        }
    }

    /**
     * List public buffer zone only overwrite the new data.
     * Note that list of minimum and maximum locations must be the same size, if not it will not be executed.
     *
     * @param path       Data path in config file
     * @param listMinLoc List of minimum location buffer zones
     * @param listMaxLoc List of maximum location buffer zones
     */
    private void overwritePublicBZList(String path, List<Vector> listMinLoc, List<Vector> listMaxLoc) {
        if (listMinLoc.size() != listMaxLoc.size())
            return;
        fileConfig.set(path, new HashMap<>());
        for (int i = 0; i < listMinLoc.size(); i++) {
            Vector minLoc = listMinLoc.get(i), maxLoc = listMaxLoc.get(i);
            fileConfig.createSection(String.format("%s.bz%d", path, i), new HashMap<>());
            fileConfig.set(String.format("%s.bz%d.minx", path, i), minLoc.getX());
            fileConfig.set(String.format("%s.bz%d.miny", path, i), minLoc.getY());
            fileConfig.set(String.format("%s.bz%d.minz", path, i), minLoc.getZ());
            fileConfig.set(String.format("%s.bz%d.maxx", path, i), maxLoc.getX());
            fileConfig.set(String.format("%s.bz%d.maxy", path, i), maxLoc.getY());
            fileConfig.set(String.format("%s.bz%d.maxz", path, i), maxLoc.getZ());
        }
    }
    //#endregion
}
