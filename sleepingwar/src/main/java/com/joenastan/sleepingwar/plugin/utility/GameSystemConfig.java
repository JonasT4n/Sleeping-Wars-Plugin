package com.joenastan.sleepingwar.plugin.utility;

import com.joenastan.sleepingwar.plugin.enumtypes.BedwarsShopType;
import com.joenastan.sleepingwar.plugin.enumtypes.LockedEntityType;
import com.joenastan.sleepingwar.plugin.enumtypes.ResourcesType;
import com.joenastan.sleepingwar.plugin.enumtypes.TimelineEventType;
import com.joenastan.sleepingwar.plugin.events.CustomEvents.BedwarsGameTimelineEvent;
import com.joenastan.sleepingwar.plugin.game.CustomDerivedEntity.LockedEntities;
import com.joenastan.sleepingwar.plugin.game.CustomDerivedEntity.LockedResourceSpawner;
import com.joenastan.sleepingwar.plugin.game.ResourceSpawner;
import com.joenastan.sleepingwar.plugin.game.TeamGroupMaker;
import com.joenastan.sleepingwar.plugin.utility.Timer.AreaEffectTimer;
import com.joenastan.sleepingwar.plugin.utility.Timer.ResourceSpawnTimer;
import org.bukkit.*;
import org.bukkit.World.Environment;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;

public class GameSystemConfig extends AbstractFile {

    public GameSystemConfig(JavaPlugin main, String filename) {
        super(main, filename);
        load();
    }

    /**
     * All world names or map names that registered as bedwars world or map.
     *
     * @return List of world names or map names
     */
    public List<String> getWorldNames() {
        List<String> wnames = new ArrayList<String>();
        wnames.addAll(filecon.getConfigurationSection("worlds").getKeys(false));
        return wnames;
    }

    /**
     * Get all resource spawners that has been registered. All resource spawners including public and teams.
     *
     * @param inWorld Current game or builder world
     * @param mapName Original map name
     * @return List of resource spawners
     */
    public List<ResourceSpawner> getWorldResourceSpawners(World inWorld, String mapName) {
        String path = String.format("worlds.%s.resource-spawners", mapName);
        List<ResourceSpawner> rspawner = new ArrayList<ResourceSpawner>();
        if (!filecon.contains(path)) {
            filecon.createSection(path, new HashMap<>());
            return rspawner;
        }
        // Get all Resource spawners
        for (String t : filecon.getConfigurationSection(path).getKeys(false)) {
            for (String rs : filecon.getConfigurationSection(path + "." + t).getKeys(false)) {
                // Get all resource spawners real information
                String rsPath = path + "." + t + "." + rs;
                if (!filecon.contains(rsPath + ".type"))
                    filecon.set(rsPath + ".type", 0);
                if (!filecon.contains(rsPath + ".spawnloc.x"))
                    filecon.set(rsPath + ".spawnloc.x", inWorld.getSpawnLocation().getX());
                if (!filecon.contains(rsPath + ".spawnloc.y"))
                    filecon.set(rsPath + ".spawnloc.y", inWorld.getSpawnLocation().getY());
                if (!filecon.contains(rsPath + ".spawnloc.z"))
                    filecon.set(rsPath + ".spawnloc.z", inWorld.getSpawnLocation().getZ());
                if (!filecon.contains(rsPath + ".duration-spawn"))
                    filecon.set(rsPath + ".duration-spawn", 10f);
                ResourcesType typeSpawnResource = ResourcesType.values()[filecon.getInt(rsPath + ".type")];
                Location spawnLoc = new Location(inWorld, filecon.getDouble(rsPath + ".spawnloc.x"), filecon.getDouble(rsPath + ".spawnloc.y"),
                        filecon.getDouble(rsPath + ".spawnloc.z"));
                float spawnDur = (float) filecon.getDouble(rsPath + ".duration-spawn");
                // Create resource spawner instance
                ResourceSpawner rsp;
                if (spawnDur < 0f)
                    rsp = new ResourceSpawner(rs, spawnLoc, typeSpawnResource);
                else
                    rsp = new ResourceSpawner(rs, spawnLoc, typeSpawnResource, spawnDur);
                rspawner.add(rsp);
            }
        }
        return rspawner;
    }

    /**
     * Get all resource spawners that has been registered by specific class.
     *
     * @param inWorld  Current game or builder world
     * @param mapName  Original map name
     * @param teamName On team by name
     * @return List of resource spawners
     */
    public List<ResourceSpawner> getWorldResourceSpawners(World inWorld, String mapName, String teamName) {
        String tPath = String.format("worlds.%s.resource-spawners.%s", mapName, teamName);
        List<ResourceSpawner> rspawner = new ArrayList<ResourceSpawner>();
        List<String> team = getTeamNames(mapName);
        // Check team existence
        if (!filecon.contains(tPath) && team.contains(teamName)) {
            filecon.createSection(tPath, new HashMap<>());
            return rspawner;
        } else if (!team.contains(teamName)) {
            tPath = String.format("worlds.%s.resource-spawners.PUBLIC", mapName);
        }
        // Get all Resource spawners
        for (String rs : filecon.getConfigurationSection(tPath).getKeys(false)) {
            String rsPath = tPath + "." + rs;
            if (!filecon.contains(rsPath + ".type"))
                filecon.set(rsPath + ".type", 0);
            if (!filecon.contains(rsPath + ".spawnloc.x"))
                filecon.set(rsPath + ".spawnloc.x", inWorld.getSpawnLocation().getX());
            if (!filecon.contains(rsPath + ".spawnloc.y"))
                filecon.set(rsPath + ".spawnloc.y", inWorld.getSpawnLocation().getY());
            if (!filecon.contains(rsPath + ".spawnloc.z"))
                filecon.set(rsPath + ".spawnloc.z", inWorld.getSpawnLocation().getZ());
            if (!filecon.contains(rsPath + ".duration-spawn"))
                filecon.set(rsPath + ".duration-spawn", 10f);
            ResourcesType typeSpawnResource = ResourcesType.values()[filecon.getInt(rsPath + ".type")];
            Location spawnLoc = new Location(inWorld, filecon.getDouble(rsPath + ".spawnloc.x"), filecon.getDouble(rsPath + ".spawnloc.y"),
                    filecon.getDouble(rsPath + ".spawnloc.z"));
            float spawnDur = (float) filecon.getDouble(rsPath + ".duration-spawn");
            // Create resource spawner instance
            ResourceSpawner rsp;
            if (spawnDur < 0f)
                rsp = new ResourceSpawner(rs, spawnLoc, typeSpawnResource);
            else
                rsp = new ResourceSpawner(rs, spawnLoc, typeSpawnResource, spawnDur);
            rspawner.add(rsp);
        }
        return rspawner;
    }

    /**
     * All teams that registered in curent world or map.
     *
     * @param world Original map name
     * @return List of team names in map
     */
    public List<String> getTeamNames(String world) {
        String path = String.format("worlds.%s.teams", world);
        if (!filecon.contains(path)) {
            filecon.createSection(path, new HashMap<>());
            filecon.set(String.format("%s.Yellow", path), new HashMap<>());
            filecon.set(String.format("%s.Red", path), new HashMap<>());
            filecon.set(String.format("%s.Blue", path), new HashMap<>());
            filecon.set(String.format("%s.Green", path), new HashMap<>());
        }
        List<String> tnames = new ArrayList<String>();
        tnames.addAll(filecon.getConfigurationSection(path).getKeys(false));
        return tnames;
    }

    /**
     * Get team location spawner in game.
     *
     * @param inWorld  Current game or builder world
     * @param mapName  Original map name
     * @param teamName On team by name
     * @return Team location spawn
     */
    public Location getTeamSpawnLoc(World inWorld, String mapName, String teamName) {
        String path = String.format("worlds.%s.teams.%s.spawner", mapName, teamName);
        List<String> team = getTeamNames(mapName);
        if (!team.contains(teamName))
            return null;
        if (!filecon.contains(path))
            filecon.createSection(path, new HashMap<>());
        if (!filecon.contains(path + ".x"))
            filecon.set(path + ".x", inWorld.getSpawnLocation().getX());
        if (!filecon.contains(path + ".y"))
            filecon.set(path + ".y", inWorld.getSpawnLocation().getY());
        if (!filecon.contains(path + ".z"))
            filecon.set(path + ".z", inWorld.getSpawnLocation().getZ());

        Location teamSpawnerLoc = new Location(inWorld, filecon.getDouble(path + ".x"), filecon.getDouble(path + ".y"), filecon.getDouble(path + ".z"));
        return teamSpawnerLoc;
    }

    /**
     * Get team's bed location. User need to check if a bed on that location has been put.
     *
     * @param mapName  Original map name
     * @param teamName On team by name
     * @return Bed location not actual bed, if team is not exists then returns null
     */
    public Location getTeamBedLocation(World inWorld, String mapName, String teamName) {
        // Check if team is exists
        List<String> team = getTeamNames(mapName);
        if (!team.contains(teamName))
            return null;
        String path = String.format("worlds.%s.teams.%s.bed-location", mapName, teamName);
        if (!filecon.contains(path))
            filecon.createSection(path);
        if (!filecon.contains(path + ".x"))
            filecon.set(path + ".x", inWorld.getSpawnLocation().getBlockX());
        if (!filecon.contains(path + ".y"))
            filecon.set(path + ".y", inWorld.getSpawnLocation().getBlockY());
        if (!filecon.contains(path + ".z"))
            filecon.set(path + ".z", inWorld.getSpawnLocation().getBlockZ());
        Location bedLocation = new Location(inWorld, filecon.getInt(path + ".x"), filecon.getInt(path + ".y"),
                filecon.getInt(path + ".z"));
        return bedLocation;
    }

    /**
     * Get team color raw string config.
     *
     * @param mapName  Original map name
     * @param teamName On team by name
     * @return Raw string data, if team is not exists then null
     */
    public String getTeamColorPrefix(String mapName, String teamName) {
        List<String> team = getTeamNames(mapName);
        if (!team.contains(teamName))
            return null;
        String path = String.format("worlds.%s.teams.%s.color", mapName, teamName);
        if (!filecon.contains(path))
            filecon.set(path, "white");
        return filecon.getString(path);
    }

    /**
     * Get the minimum value location of team buffer area
     *
     * @param mapName  Original map name
     * @param teamName On team with name
     * @return Minimum buffer area location
     */
    public Location getTeamMinBuffArea(World inWorld, String mapName, String teamName) {
        // Check team exists
        List<String> team = getTeamNames(mapName);
        if (!team.contains(teamName))
            teamName = "PUBLIC";
        // Check path in config exists
        String path = String.format("worlds.%s.buffer-zone.%s", mapName, teamName);
        if (!filecon.contains(path))
            filecon.createSection(path);
        if (!filecon.contains(path + ".minx"))
            filecon.set(path + ".minx", inWorld.getSpawnLocation().getX() - 5d);
        if (!filecon.contains(path + ".miny"))
            filecon.set(path + ".miny", inWorld.getSpawnLocation().getY() - 5d);
        if (!filecon.contains(path + ".minz"))
            filecon.set(path + ".minz", inWorld.getSpawnLocation().getZ() - 5d);
        return new Location(inWorld, filecon.getDouble(path + ".minx"), filecon.getDouble(path + ".miny"), filecon.getDouble(path + ".minz"));
    }

    /**
     * Get the maximum value location of team buffer area
     *
     * @param mapName  Original map name
     * @param teamName On team with name
     * @return Maximum buffer area location
     */
    public Location getTeamMaxBuffArea(World inWorld, String mapName, String teamName) {
        // Check team exists
        List<String> team = getTeamNames(mapName);
        if (!team.contains(teamName))
            teamName = "PUBLIC";
        // Check path in config exists
        String path = String.format("worlds.%s.buffer-zone.%s", mapName, teamName);
        if (!filecon.contains(path))
            filecon.createSection(path);
        if (!filecon.contains(path + ".maxx"))
            filecon.set(path + ".maxx", inWorld.getSpawnLocation().getX() + 5d);
        if (!filecon.contains(path + ".maxy"))
            filecon.set(path + ".maxy", inWorld.getSpawnLocation().getY() + 5d);
        if (!filecon.contains(path + ".maxz"))
            filecon.set(path + ".maxz", inWorld.getSpawnLocation().getZ() + 5d);
        return new Location(inWorld, filecon.getDouble(path + ".maxx"), filecon.getDouble(path + ".maxy"), filecon.getDouble(path + ".maxz"));
    }

    /**
     * Immediately get the buffer zone coroutine with empty effects.
     *
     * @param inWorld  Current world standing
     * @param mapName  Original map name
     * @param teamName On team with name
     * @return Buffer zone coroutine, including the locations
     */
    public AreaEffectTimer getBufferZoneCoroutine(World inWorld, String mapName, TeamGroupMaker team) {
        // Check team exists
        List<String> teamNames = getTeamNames(mapName);
        if (!teamNames.contains(team.getName()))
            return null;
        AreaEffectTimer bufferZoneCoro = new AreaEffectTimer(5f, getTeamMinBuffArea(inWorld, mapName, team.getName()),
                getTeamMaxBuffArea(inWorld, mapName, team.getName()), team);
        return bufferZoneCoro;
    }

    /**
     * Immediately get the buffer zone coroutine with empty effects.
     *
     * @param inWorld Current world standing
     * @param mapName Original map name
     * @return List of public buffer zone coroutine, including the locations
     */
    public List<AreaEffectTimer> getPublicBZCoroutines(World inWorld, String mapName) {
        String path = String.format("worlds.%s.buffer-zone.PUBLIC", mapName);
        List<AreaEffectTimer> listBufferZonesCoro = new ArrayList<AreaEffectTimer>();
        if (!filecon.contains(path))
            filecon.createSection(path);
        for (String bz : filecon.getConfigurationSection(path).getKeys(false)) {
            Location minLoc = new Location(inWorld, filecon.getDouble(String.format("%s.%s.minx", path, bz)),
                    filecon.getDouble(String.format("%s.%s.miny", path, bz)),
                    filecon.getDouble(String.format("%s.%s.minz", path, bz)));
            Location maxLoc = new Location(inWorld, filecon.getDouble(String.format("%s.%s.maxx", path, bz)),
                    filecon.getDouble(String.format("%s.%s.maxy", path, bz)),
                    filecon.getDouble(String.format("%s.%s.maxz", path, bz)));
            AreaEffectTimer bufferZoneCoro = new AreaEffectTimer(5f, minLoc, maxLoc, null);
            listBufferZonesCoro.add(bufferZoneCoro);
        }
        return listBufferZonesCoro;
    }

    /**
     * Get codenames that owned by a team. if there's no existing team then it consider as public resource spawners.
     *
     * @param mapName  Original map name
     * @param teamName On team with name
     * @return A list of resource spawner codenames
     */
    public List<String> getRSCodenames(String mapName, String teamName) {
        // Check existing team
        List<String> team = getTeamNames(mapName);
        if (!team.contains(teamName))
            teamName = "PUBLIC";
        // Check existence in config file
        String path = String.format("worlds.%s.resource-spawners.%s", mapName, teamName);
        List<String> codenames = new ArrayList<String>();
        if (!filecon.contains(path)) {
            filecon.createSection(path);
            return codenames;
        }
        // Include all codenames in team
        codenames.addAll(filecon.getConfigurationSection(path).getKeys(false));
        return codenames;
    }

    /**
     * Specific map queue location.
     *
     * @param inWorld Current game or builder world
     * @param mapName Original map name
     * @return Queue Location
     */
    public Location getQueueLocations(World inWorld, String mapName) {
        String path = String.format("worlds.%s.queueloc", mapName);
        if (!filecon.contains(path))
            filecon.createSection(path, new HashMap<>());
        if (!filecon.contains(path + ".x"))
            filecon.set(path + ".x", inWorld.getSpawnLocation().getX());
        if (!filecon.contains(path + ".y"))
            filecon.set(path + ".y", inWorld.getSpawnLocation().getY());
        if (!filecon.contains(path + ".z"))
            filecon.set(path + ".z", inWorld.getSpawnLocation().getZ());
        Location queueLoc = new Location(inWorld, filecon.getDouble(path + ".x"), filecon.getDouble(path + ".y"),
                filecon.getDouble(path + ".z"));
        return queueLoc;
    }

    /**
     * Get bedwars timeline events
     *
     * @param mapName Original map name
     * @return List of events in timeline
     */
    public List<BedwarsGameTimelineEvent> getTimelineEvents(String mapName) {
        String path = String.format("worlds.%s.timeline", mapName);
        List<BedwarsGameTimelineEvent> timelineEvents = new ArrayList<BedwarsGameTimelineEvent>();
        if (!filecon.contains(path)) {
            filecon.createSection(path, new HashMap<>());
            return timelineEvents; // Empty timeline
        }
        // Get and create object event
        for (String eventNameString : filecon.getConfigurationSection(path).getKeys(false)) {
            // Set Defaults
            int order = 0;
            float secondsToTrigger = 100f;
            String eventmsg = "Event Trigger", eventName = eventNameString;
            TimelineEventType typevent = TimelineEventType.DIAMOND_UPGRADE;
            // Get Real event timeline information
            if (filecon.contains(path + ".order"))
                order = filecon.getInt(path + ".order");
            if (filecon.contains(path + ".message"))
                eventmsg = filecon.getString(path + ".message");
            if (filecon.contains(path + ".type"))
                typevent = TimelineEventType.fromString(filecon.getString(path + ".type"));
            if (filecon.contains(path + ".trigger-in-seconds"))
                secondsToTrigger = (float) filecon.getDouble(path + ".trigger-in-seconds");
            BedwarsGameTimelineEvent bevent = new BedwarsGameTimelineEvent(typevent, secondsToTrigger, eventName, order, eventmsg);
            timelineEvents.add(bevent);
        }
        // Selection sort
        for (int i = 0; i < timelineEvents.size() - 1; i++) {
            int lowestValueIndex = i;
            for (int j = i + 1; j < timelineEvents.size(); j++) {
                if (timelineEvents.get(lowestValueIndex).getTimelineOrder() > timelineEvents.get(j).getTimelineOrder())
                    lowestValueIndex = j;
            }
            if (i != lowestValueIndex) {
                BedwarsGameTimelineEvent temp = timelineEvents.get(i);
                timelineEvents.set(i, timelineEvents.get(lowestValueIndex));
                timelineEvents.set(lowestValueIndex, temp);
            }
        }
        return timelineEvents;
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
        Map<BedwarsShopType, List<Location>> shopMapLoc = new HashMap<BedwarsShopType, List<Location>>();
        if (!filecon.contains(path))
            filecon.createSection(path, new HashMap<>());
        for (String shopTypeString : filecon.getConfigurationSection(path).getKeys(false)) {
            BedwarsShopType shopType = BedwarsShopType.fromString(shopTypeString);
            Set<String> indexStringSet = filecon.getConfigurationSection(path + "." + shopTypeString).getKeys(false);
            if (shopType != null && !indexStringSet.isEmpty()) {
                List<Location> locationList = new ArrayList<Location>();
                for (String shopLocString : indexStringSet) {
                    String locPath = path + "." + shopTypeString + "." + shopLocString;
                    Location loc = new Location(inWorld, filecon.getDouble(locPath + ".x"),
                            filecon.getDouble(locPath + ".y"), filecon.getDouble(locPath + ".z"));
                    locationList.add(loc);
                }
                shopMapLoc.put(shopType, locationList);
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
    public List<LockedEntities> getLockedRequestEntity(World inWorld, String mapName) {
        String path = String.format("worlds.%s.locked-entity", mapName);
        if (!filecon.contains(path))
            filecon.createSection(path, new HashMap<>());
        List<LockedEntities> listOfLocked = new ArrayList<LockedEntities>();
        for (String cn : filecon.getConfigurationSection(path).getKeys(false)) {
            LockedEntityType typeLock = LockedEntityType.values()[filecon.getInt(path + ".type-lock")];
            if (typeLock == LockedEntityType.NORMAL_LOCK) {
                String tPath = path + "." + cn + ".request";
                Map<ResourcesType, Integer> requirements = new HashMap<ResourcesType, Integer>();
                if (!filecon.contains(tPath))
                    filecon.createSection(tPath);
                for (String req : filecon.getConfigurationSection(tPath).getKeys(false)) {
                    ResourcesType typeRequest = ResourcesType.fromString(req);
                    requirements.put(typeRequest, filecon.getInt(tPath + "." + req));
                }
                LockedEntities locked = new LockedEntities(new Location(inWorld,
                        filecon.getDouble(path + "." + cn + ".loc.x"),
                        filecon.getDouble(path + "." + cn + ".loc.y"),
                        filecon.getDouble(path + "." + cn + ".loc.z")), requirements);
                listOfLocked.add(locked);
            }
        }
        return listOfLocked;
    }

    /**
     * Get locked resource spawner entity reference.
     *
     * @param inWorld Current standing world
     * @param mapName Original map name
     * @param rsTimer Resource spawner coroutine reference
     * @return Locked resource spawner, if it's not exists then it returns null
     */
    public LockedResourceSpawner getLockedRSEntity(World inWorld, String mapName, ResourceSpawnTimer rsTimer) {
        String path = String.format("worlds.%s.locked-entity", mapName);
        if (!filecon.contains(path))
            filecon.createSection(path, new HashMap<>());
        for (String cn : filecon.getConfigurationSection(path).getKeys(false)) {
            LockedEntityType typeLock = LockedEntityType.values()[filecon.getInt(path + ".type-lock")];
            if (typeLock == LockedEntityType.RESOURCE_SPAWNER_LOCK && rsTimer.getCodeName().equals(filecon.getString(path + "." + cn + ".rs-lock"))) {
                String tPath = path + "." + cn + ".request";
                Map<ResourcesType, Integer> requirements = new HashMap<ResourcesType, Integer>();
                for (String req : filecon.getConfigurationSection(tPath).getKeys(false)) {
                    ResourcesType typeRequest = ResourcesType.fromString(req);
                    requirements.put(typeRequest, filecon.getInt(tPath + "." + req));
                }
                return new LockedResourceSpawner(new Location(inWorld,
                        filecon.getDouble(path + "." + cn + ".loc.x"),
                        filecon.getDouble(path + "." + cn + ".loc.y"),
                        filecon.getDouble(path + "." + cn + ".loc.z")), requirements, rsTimer);
            }
        }
        return null;
    }

    /**
     * Get all public resource spawners, codenames only.
     *
     * @param mapName Original map name
     * @return List of codenames
     */
    public List<String> getPublicRSCodenames(String mapName) {
        List<String> listRSCodenames = new ArrayList<String>();
        String path = String.format("worlds.%s.resource-spawners.PUBLIC", mapName);
        if (getWorldNames().contains(mapName)) {
            if (!filecon.contains(path))
                filecon.createSection(path, new HashMap<>());
            listRSCodenames.addAll(filecon.getConfigurationSection(path).getKeys(false));
        }
        return listRSCodenames;
    }

    /**
     * Get all locked entities codenames.
     *
     * @param mapName Original map name
     * @return List of locked entity codenames
     */
    public List<String> getLockedCodenames(String mapName) {
        String path = String.format("worlds.%s.locked-entity", mapName);
        if (!filecon.contains(path))
            filecon.createSection(path, new HashMap<>());
        List<String> codenames = new ArrayList<String>();
        codenames.addAll(filecon.getConfigurationSection(path).getKeys(false));
        return codenames;
    }

    /**
     * Insert a new team into bedwars team list.
     *
     * @param mapName     Original map name
     * @param teamName    Name of new team
     * @param colorPrefix Raw color for team prefix color
     * @return True if successfully added, if team already exists the it returns false
     */
    public boolean addTeam(String mapName, String teamName, String colorPrefix) {
        // Check existing team
        List<String> team = getTeamNames(mapName);
        if (team.contains(teamName))
            return false;
        // Proceed Insertion
        String path = String.format("worlds.%s.teams.%s", mapName, teamName);
        filecon.createSection(path, new HashMap<>());
        filecon.set(path + ".color", colorPrefix);
        String bufferZonePath = String.format("worlds.%s.buffer-zone", mapName);
        filecon.createSection(bufferZonePath + "." + teamName, new HashMap<>());
        filecon.createSection(bufferZonePath + "-effects." + teamName, new HashMap<>());
        String resouceSpawnerPath = String.format("worlds.%s.resource-spawners.%s", mapName, teamName);
        filecon.createSection(resouceSpawnerPath, new HashMap<>());
        return true;
    }

    /**
     * Add event into list
     *
     * @param mapName Original map name
     * @return True if successfully added, if world not exists then it returns false
     */
    public boolean addEventTimeline(String mapName, BedwarsGameTimelineEvent eventSample) {
        if (!getWorldNames().contains(mapName))
            return false;
        String path = String.format("worlds.%s.timeline", mapName);
        if (!filecon.contains(path))
            filecon.createSection(path);
        filecon.set(path + ".order", eventSample.getTimelineOrder());
        filecon.set(path + ".type", eventSample.getEventType().toString());
        filecon.set(path + ".trigger-in-seconds", eventSample.getTriggerSeconds());
        return true;
    }

    /**
     * Add a new resource spawner to the map.
     *
     * @param mapName  Original map name
     * @param teamName On team by name
     * @param spawner  Already created spawner
     */
    public void addResourceSpawner(String mapName, String teamName, ResourceSpawner spawner) {
        // Check available team
        List<String> team = getTeamNames(mapName);
        if (!team.contains(teamName))
            teamName = "PUBLIC";
        String path = String.format("worlds.%s.resource-spawners.%s", mapName, teamName);
        if (!filecon.contains(path))
            filecon.createSection(path, new HashMap<>());
        // Add into config file
        String rsPath = path + "." + spawner.getCodename();
        filecon.set(rsPath, new HashMap<>());
        filecon.set(rsPath + ".type", spawner.getTypeResourceSpawner().ordinal());
        filecon.set(rsPath + ".spawnloc.x", spawner.getSpawnLocation().getX());
        filecon.set(rsPath + ".spawnloc.y", spawner.getSpawnLocation().getY());
        filecon.set(rsPath + ".spawnloc.z", spawner.getSpawnLocation().getZ());
        filecon.set(rsPath + ".duration-spawn", spawner.getSecondsPerSpawn());
    }

    /**
     * Add a new shop spawner to the map.
     *
     * @param mapName        Original map name
     * @param type           Type of shop
     * @param playerLocation Player current location
     * @return true if successfuly added, else then false
     */
    public boolean addShopLocationSpawn(String mapName, BedwarsShopType type, Location playerLocation) {
        String path = String.format("worlds.%s.shop-location.%s", mapName, type.toString());
        if (!getWorldNames().contains(mapName))
            return false;
        if (!filecon.contains(path))
            filecon.createSection(path, new HashMap<>());
        // Add shop spawn location into config file
        String newPath = String.format("%s.shop%d", path, filecon.getConfigurationSection(path).getKeys(false).size());
        filecon.set(newPath + ".x", playerLocation.getX());
        filecon.set(newPath + ".y", playerLocation.getY());
        filecon.set(newPath + ".z", playerLocation.getZ());
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
        List<String> team = getTeamNames(mapName);
        if (!team.contains(teamName))
            teamName = "PUBLIC";
        // Add into config file
        String path = String.format("worlds.%s.buffer-zone-effects.%s", mapName, teamName);
        if (!filecon.contains(path))
            filecon.createSection(path);
        String tPath = path + "." + effectType.getName();
        if (!filecon.contains(tPath)) {
            filecon.createSection(tPath);
            filecon.set(tPath + ".seconds-hit", 5f);
            filecon.set(tPath + ".for-opposition", false);
            return true;
        }
        return false;
    }

    /**
     * Add requirement on locked entity.
     *
     * @param mapName  Original map name
     * @param codename Locked entity has their own codename
     * @param typeRes  Resource type which required
     * @return True if successfully set, if the locked entity not exists then it returns false
     */
    public boolean addRequestOnLockedEntity(String mapName, String codename, ResourcesType typeRes, int amount) {
        String path = String.format("worlds.%s.locked-entity.%s", mapName, codename);
        if (!filecon.contains(path))
            return false;
        if (!filecon.contains(path + ".request"))
            filecon.createSection(path + ".request");
        path = path + ".request." + typeRes.toString();
        filecon.set(path, amount);
        return true;
    }

    /**
     * Set team color prefix, you can use any of string but the plugin itself will decide if it is a color.
     *
     * @param mapName     Original map name
     * @param teamName    On team with name
     * @param colorPrefix Color choice
     * @return True if successfully set, if team not exists then it returns false
     */
    public boolean setTeamColorPrefix(String mapName, String teamName, String colorPrefix) {
        // Check available team
        List<String> team = getTeamNames(mapName);
        if (!team.contains(teamName))
            return false;
        // Set configuration
        String path = String.format("worlds.%s.teams.%s.color", mapName, teamName);
        filecon.set(path, colorPrefix);
        return true;
    }

    /**
     * Set or overwrite queue location, this will also check if the registered world exists.
     *
     * @param mapName  Original map name
     * @param queueLoc Location set
     * @return true if successfully set, else then false
     */
    public boolean setQueueLocation(String mapName, Location queueLoc) {
        if (!getWorldNames().contains(mapName))
            return false;
        String path = String.format("worlds.%s.queueloc", mapName);
        filecon.set(path + ".x", queueLoc.getX());
        filecon.set(path + ".y", queueLoc.getY());
        filecon.set(path + ".z", queueLoc.getZ());
        return true;
    }

    /**
     * Set or overwrite team spawn location.
     *
     * @param mapName  Original map name
     * @param teamName On team with name
     * @param setLoc   Location set
     * @return true if successfully set, else then false;
     */
    public boolean setTeamSpawnLoc(String mapName, String teamName, Location setLoc) {
        List<String> team = getTeamNames(mapName);
        if (!team.contains(teamName))
            return false;
        String path = String.format("worlds.%s.teams.%s.spawner", mapName, teamName);
        if (!filecon.contains(path))
            filecon.createSection(path);
        filecon.set(path + ".x", setLoc.getX());
        filecon.set(path + ".y", setLoc.getY());
        filecon.set(path + ".z", setLoc.getZ());
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
    public boolean setTeamBedLocation(String mapName, String teamName, Location bedLoc) {
        List<String> team = getTeamNames(mapName);
        if (!team.contains(teamName))
            return false;
        String path = String.format("worlds.%s.teams.%s.bed-location", mapName, teamName);
        if (!filecon.contains(path))
            filecon.createSection(path);
        filecon.set(path + ".x", bedLoc.getBlockX());
        filecon.set(path + ".y", bedLoc.getBlockY());
        filecon.set(path + ".z", bedLoc.getBlockZ());
        return true;
    }

    /**
     * Set team area buffer zone, this area gives effect of any potion effects.
     * This function automaticaly recalculate minimum and maximum value of location.
     * Each team can only have 1 buffer area.
     *
     * @param mapName  Original map name
     * @param teamName On team with name
     * @param minLoc   Location minimum value
     * @param maxLoc   Location maximum value
     */
    public void setTeamBufferArea(String mapName, String teamName, Location minLoc, Location maxLoc) {
        List<String> team = getTeamNames(mapName);
        if (!team.contains(teamName))
            teamName = "PUBLIC";
        double temp;
        // Minimal and Maximal value correction
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
        if (!filecon.contains(path))
            filecon.createSection(path);
        if (teamName.equals("PUBLIC")) {
            // Overwrite list of buffer area
            Set<String> setListBuffer = filecon.getConfigurationSection(path).getKeys(false);
            List<Vector> minVec = new ArrayList<Vector>(), maxVec = new ArrayList<Vector>();
            for (int i = 0; i < setListBuffer.size(); i++) {
                minVec.add(new Vector(filecon.getDouble(String.format("%s.bz%d.minx", path, i)),
                        filecon.getDouble(String.format("%s.bz%d.miny", path, i)),
                        filecon.getDouble(String.format("%s.bz%d.minz", path, i))));
                maxVec.add(new Vector(filecon.getDouble(String.format("%s.bz%d.maxx", path, i)),
                        filecon.getDouble(String.format("%s.bz%d.maxy", path, i)),
                        filecon.getDouble(String.format("%s.bz%d.maxz", path, i))));
            }
            minVec.add(new Vector(minLoc.getX(), minLoc.getY(), minLoc.getZ()));
            maxVec.add(new Vector(maxLoc.getX(), maxLoc.getY(), maxLoc.getZ()));
            overwritePublicBZList(path, minVec, maxVec);
        } else {
            filecon.set(path + ".minx", minLoc.getX());
            filecon.set(path + ".miny", minLoc.getY());
            filecon.set(path + ".minz", minLoc.getZ());
            filecon.set(path + ".maxx", maxLoc.getX());
            filecon.set(path + ".maxy", maxLoc.getY());
            filecon.set(path + ".maxz", maxLoc.getZ());
        }
    }

    /**
     * Set locked placeable entity, which player cannot interact with.
     *
     * @param mapName    Original map name
     * @param codename   Codename for this entity, must be unique
     * @param onLocation Location where did player set
     * @return True if successfully set, if map not exists then it's
     */
    public boolean setLockedRequestEntity(String mapName, String codename, Location onLocation) {
        if (!getWorldNames().contains(mapName))
            return false;
        // Check if locked entity already exists
        String path = String.format("worlds.%s.locked-entity.%s", mapName, codename);
        if (!filecon.contains(path))
            filecon.createSection(path);
        filecon.set(path + ".loc.x", onLocation.getX());
        filecon.set(path + ".loc.y", onLocation.getY());
        filecon.set(path + ".loc.z", onLocation.getZ());
        filecon.set(path + ".type-lock", LockedEntityType.NORMAL_LOCK.ordinal());
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
    public boolean setLockedRequestEntity(String mapName, String codename, Location onLocation, String codenameRS) {
        if (!getWorldNames().contains(mapName))
            return false;
        // Move resource spawner's classification into Locked section
        String tPath = String.format("worlds.%s.resource-spawners.PUBLIC.%s", mapName, codenameRS);
        if (!filecon.contains(tPath))
            return false;
        String ttPath = String.format("worlds.%s.resource-spawners.LOCKED.%s", mapName, codenameRS);
        if (!filecon.contains(ttPath))
            filecon.createSection(ttPath);
        filecon.set(ttPath + ".type", filecon.getInt(tPath + ".type"));
        filecon.set(ttPath + ".spawnloc.x", filecon.getDouble(tPath + ".spawnloc.x"));
        filecon.set(ttPath + ".spawnloc.y", filecon.getDouble(tPath + ".spawnloc.y"));
        filecon.set(ttPath + ".spawnloc.z", filecon.getDouble(tPath + ".spawnloc.z"));
        filecon.set(ttPath + ".duration-spawn", filecon.getDouble(tPath + ".duration-spawn"));
        filecon.set(tPath, null);
        // Check if locked entity already exists
        String path = String.format("worlds.%s.locked-entity.%s", mapName, codename);
        if (!filecon.contains(path))
            filecon.createSection(path);
        filecon.set(path + ".loc.x", onLocation.getX());
        filecon.set(path + ".loc.y", onLocation.getY());
        filecon.set(path + ".loc.z", onLocation.getZ());
        filecon.set(path + ".type-lock", LockedEntityType.RESOURCE_SPAWNER_LOCK.ordinal());
        filecon.set(path + ".rs-lock", codenameRS);
        return true;
    }

    /**
     * Set a resource spawner seconds per spawn.
     *
     * @param mapName  Original map name
     * @param teamName On team with name
     * @param codename Resource spawner has their own unique codename
     * @param duration Seconds per spawn duration
     * @return true if successfully changed, else then false
     */
    public boolean setRSDuration(String mapName, String teamName, String codename, float duration) {
        String path = String.format("worlds.%s.resource-spawners.%s.%s", mapName, teamName, codename);
        if (!filecon.contains(path))
            return false;
        filecon.set(path + ".duration-spawn", duration);
        return true;
    }

    /**
     * Delete a team from this map.
     *
     * @param mapName  Original map name
     * @param teamName On team with name
     * @return true if successfully deleted, if team not exists then false
     */
    public boolean deleteTeam(String mapName, String teamName) {
        // Check if team exists
        List<String> team = getTeamNames(mapName);
        if (!team.contains(teamName))
            return false;
        // Proceed Deletion
        String path = String.format("worlds.%s.teams.%s", mapName, teamName);
        filecon.set(path, null);
        String bufferZonePath = String.format("worlds.%s.buffer-zone", mapName);
        filecon.set(bufferZonePath + "." + teamName, null);
        filecon.set(bufferZonePath + "-effects." + teamName, null);
        String resouceSpawnerPath = String.format("worlds.%s.resource-spawners.%s", mapName, teamName);
        filecon.set(resouceSpawnerPath, null);
        return true;
    }

    /**
     * Delete an existing resource spawner in world or map. After you got the list, you need to sort it by order.
     *
     * @param mapName  Original map name
     * @param teamName On team by name
     * @param codeName Every resource spawner has their own unique codename
     * @return True if successfully deleted, else then false
     */
    public boolean deleteResourceSpawner(String mapName, String teamName, String codeName) {
        String path = String.format("worlds.%s.resource-spawners.%s", mapName, teamName);
        List<String> teams = getTeamNames(mapName);
        // Check if team is exists but resource spawner is not
        if (teams.contains(teamName) && !filecon.contains(path)) {
            filecon.createSection(path, new HashMap<>());
            return false;
        }
        // Consider a public resource spawner if the team not exists
        if (!filecon.contains(path)) {
            path = String.format("worlds.%s.resource-spawners.PUBLIC", mapName);
            // Check PUBLIC section exists
            if (!filecon.contains(path)) {
                // Consider create a public section if not exists
                filecon.createSection(path, new HashMap<>());
                return false;
            }
        }
        // Check team resource spawner by codename
        if (!filecon.contains(path + "." + codeName))
            return false;
        filecon.set(path + "." + codeName, null);
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
        if (!filecon.contains(path)) {
            filecon.createSection(path);
            return false;
        }
        Map<String, Vector> indexesInConfig = new HashMap<String, Vector>();
        // Get number in sub-string
        String pickedIndex = null;
        for (String ic : filecon.getConfigurationSection(path).getKeys(false)) {
            String numString = ic.substring(4);
            if (Integer.toString(index).equals(numString))
                pickedIndex = ic;
            Vector vectorLoc = new Vector(filecon.getDouble(path + "." + ic + ".x"), filecon.getDouble(path + "." + ic + ".y"),
                    filecon.getDouble(path + "." + ic + ".z"));
            indexesInConfig.put(ic, vectorLoc);
        }
        // Check if pickedIndex is not null
        if (pickedIndex == null)
            return false;
        filecon.set(path + "." + pickedIndex, null);
        indexesInConfig.remove(pickedIndex);
        // Overwrite the list in config file
        List<Vector> listVectors = new ArrayList<Vector>();
        listVectors.addAll(indexesInConfig.values());
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
        String path = String.format("worlds.%s.timeline.%s", mapName, eventName);
        if (!filecon.contains(path))
            return false;
        filecon.set(path, null);
        return true;
    }

    /**
     * Delete by index a public buffer zone.
     *
     * @param mapName Original map name
     * @param index   List index to be delete
     * @return True if successfully deleted, if world not exists or index exceeded then it returns false
     */
    public boolean deletePublicBufferZone(String mapName, int index) {
        if (!getWorldNames().contains(mapName))
            return false;
        // Overwrite list of buffer area
        String path = String.format("worlds.%s.buffer-zone.PUBLIC", mapName);
        Set<String> setListBuffer = filecon.getConfigurationSection(path).getKeys(false);
        if (index >= setListBuffer.size())
            return false;
        List<Vector> minVec = new ArrayList<Vector>(), maxVec = new ArrayList<Vector>();
        for (int i = 0; i < setListBuffer.size(); i++) {
            minVec.add(new Vector(filecon.getDouble(String.format("%s.bz%d.minx", path, i)),
                    filecon.getDouble(String.format("%s.bz%d.miny", path, i)),
                    filecon.getDouble(String.format("%s.bz%d.minz", path, i))));
            maxVec.add(new Vector(filecon.getDouble(String.format("%s.bz%d.maxx", path, i)),
                    filecon.getDouble(String.format("%s.bz%d.maxy", path, i)),
                    filecon.getDouble(String.format("%s.bz%d.maxz", path, i))));
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
        String path = String.format("worlds.%s.locked-entity.%s", mapName, codename);
        if (!filecon.contains(path))
            return false;
        // Move from locked Resource spawner to the public
        if (filecon.contains(path + ".rs-lock")) {
            String rsCodename = filecon.getString(path + ".rs-lock");
            String rsPath = String.format("worlds.%s.resource-spawners.LOCKED.%s", mapName, rsCodename);
            if (filecon.contains(rsPath)) {
                String rsPublicPath = String.format("worlds.%s.resource-spawners.PUBLIC.%s", mapName, rsCodename);
                if (!filecon.contains(rsPublicPath))
                    filecon.createSection(rsPublicPath);
                filecon.set(rsPublicPath + ".type", filecon.getInt(rsPath + ".type"));
                filecon.set(rsPublicPath + ".spawnloc.x", filecon.getDouble(rsPath + ".spawnloc.x"));
                filecon.set(rsPublicPath + ".spawnloc.y", filecon.getDouble(rsPath + ".spawnloc.y"));
                filecon.set(rsPublicPath + ".spawnloc.z", filecon.getDouble(rsPath + ".spawnloc.z"));
                filecon.set(rsPublicPath + ".duration-spawn", filecon.getDouble(rsPath + ".duration-spawn"));
            }
        }
        filecon.set(path, null);
        return true;
    }

    /**
     * Count overall resource spawners in this map
     *
     * @param mapName Original map name
     * @return amount of existing resource spawners
     */
    public int countOverallRS(String mapName) {
        String path = String.format("worlds.%s.resource-spawners", mapName);
        // Consider a public resource spawner if the team not exists
        if (!filecon.contains(path)) {
            filecon.createSection(path, new HashMap<>());
            return 0;
        }
        // Count all resource spawners
        int amountSet = 0;
        for (String rsp : filecon.getConfigurationSection(path).getKeys(false)) {
            amountSet += filecon.getConfigurationSection(path + "." + rsp).getKeys(false).size();
        }
        return amountSet;
    }

    /**
     * Count events in current world timeline.
     *
     * @param mapName Original map name
     * @return Amount of events in timeline
     */
    public int countEventsInTimeline(String mapName) {
        String path = String.format("worlds.%s.timeline", mapName);
        // Consider if the timeline section is not exists
        if (!filecon.contains(path)) {
            filecon.createSection(path, new HashMap<>());
            return 0;
        }
        return filecon.getConfigurationSection(path).getKeys(false).size();
    }

    /**
     * Load worlds and config
     */
    public void load() {
        if (!filecon.contains("worlds"))
            filecon.set("worlds", new HashMap<>());
        // Loop by World Name
        for (String key : filecon.getConfigurationSection("worlds").getKeys(false)) {
            // load world creator
            WorldCreator creator = new WorldCreator(key).environment(Environment.values()[filecon.getInt("worlds." + key + ".env")])
                    .hardcore(filecon.getBoolean("worlds." + key + ".hardcore"))
                    .generateStructures(filecon.getBoolean("worlds." + key + ".structure"));
            Bukkit.createWorld(creator);
        }
    }

    /**
     * Only used when the world is going to be created.
     *
     * @param creator World creator is needed to create a world
     */
    public void saveWorldConfig(WorldCreator creator) {
        World worldSample = Bukkit.createWorld(creator);
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
        // Create Sections
        // Generic world path
        String worldPathName = String.format("worlds.%s", worldName);
        // Name of world and World Creator Config
        filecon.createSection(worldPathName, new HashMap<>());
        filecon.set(worldPathName + ".env", creator.environment().ordinal());
        filecon.set(worldPathName + ".structure", creator.generateStructures());
        filecon.set(worldPathName + "hardcore", creator.hardcore());
        // Initial Teams in game
        filecon.createSection(worldPathName + ".teams", new HashMap<>());
        filecon.set(String.format("%s.teams.Yellow", worldPathName), new HashMap<>());
        filecon.set(String.format("%s.teams.Red", worldPathName), new HashMap<>());
        filecon.set(String.format("%s.teams.Blue", worldPathName), new HashMap<>());
        filecon.set(String.format("%s.teams.Green", worldPathName), new HashMap<>());
        // Queue Location of Game
        filecon.createSection(worldPathName + ".queueloc", new HashMap<>());
        filecon.set(worldPathName + "queueloc.x", defaultSpawnLoc.getX());
        filecon.set(worldPathName + "queueloc.y", defaultSpawnLoc.getX());
        filecon.set(worldPathName + "queueloc.z", defaultSpawnLoc.getX());
        // Empty Resource Spawners
        filecon.createSection(worldPathName + ".resource-spawners", new HashMap<>());
        filecon.createSection(worldPathName + ".resource-spawners.PUBLIC", new HashMap<>());
        // Empty Timeline Event
        filecon.createSection(worldPathName + ".timeline", new HashMap<>());
        // Empty Shop Location
        filecon.createSection(worldPathName + ".shop-location", new HashMap<>());
    }

    /**
     * List of shop only overwrite the new data.
     *
     * @param path    Data path in config file
     * @param listLoc List of locations
     */
    private void overwriteShopList(String path, List<Vector> listLoc) {
        filecon.set(path, new HashMap<>());
        for (int i = 0; i < listLoc.size(); i++) {
            Vector thisLocVector = listLoc.get(i);
            filecon.createSection(String.format("%s.shop%d", path, i), new HashMap<>());
            filecon.set(String.format("%s.shop%d.x", path, i), thisLocVector.getX());
            filecon.set(String.format("%s.shop%d.y", path, i), thisLocVector.getY());
            filecon.set(String.format("%s.shop%d.z", path, i), thisLocVector.getZ());
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
        filecon.set(path, new HashMap<>());
        for (int i = 0; i < listMinLoc.size(); i++) {
            Vector minLoc = listMinLoc.get(i), maxLoc = listMaxLoc.get(i);
            filecon.createSection(String.format("%s.bz%d", path, i), new HashMap<>());
            filecon.set(String.format("%s.bz%d.minx", path, i), minLoc.getX());
            filecon.set(String.format("%s.bz%d.miny", path, i), minLoc.getY());
            filecon.set(String.format("%s.bz%d.minz", path, i), minLoc.getZ());
            filecon.set(String.format("%s.bz%d.maxx", path, i), maxLoc.getX());
            filecon.set(String.format("%s.bz%d.maxy", path, i), maxLoc.getY());
            filecon.set(String.format("%s.bz%d.maxz", path, i), maxLoc.getZ());
        }
    }
}
