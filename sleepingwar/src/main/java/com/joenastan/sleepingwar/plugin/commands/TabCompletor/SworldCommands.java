package com.joenastan.sleepingwar.plugin.commands.TabCompletor;

import com.joenastan.sleepingwar.plugin.SleepingWarsPlugin;
import com.joenastan.sleepingwar.plugin.enumtypes.BedwarsShopType;
import com.joenastan.sleepingwar.plugin.enumtypes.ResourcesType;
import com.joenastan.sleepingwar.plugin.enumtypes.TimelineEventType;
import com.joenastan.sleepingwar.plugin.events.CustomEvents.BedwarsGameTimelineEvent;
import com.joenastan.sleepingwar.plugin.utility.GameSystemConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SworldCommands implements TabCompleter {

    private static final GameSystemConfig systemConf = SleepingWarsPlugin.getGameSystemConfig();

    private String addEventCMD = "addevent"; // Add event timeline
    private String setTeamAreaCMD = "areabuff"; // set team area potion effect
    private String setAreaOppositionCMD = "areaopp"; // set whether the area is for team or the opposition
    private String setAreaSingleShotCMD = "areasin"; // set whether the area only one time effect run
    private String addAreaBufferEffectCMD = "areapot"; // Add potion effects into buffer zone
    private String addTeamCMD = "addteam"; // Add another team
    private String addRequestCMD = "addlockreq"; // Add a request to unlock the locked entity
    private String createCMD = "create"; // command to create the world
    private String removeEventCMD = "delevent"; // delete event timeline
    private String deleteResSpawnCMD = "delrspawn"; // delete resource spawner by name
    private String deleteShopLocCMD = "delshop"; // delete shop spawner
    private String deleteTeamCMD = "delteam"; // delete an existing team
    private String deleteLockedEntityCMD = "dellock"; // delete locked entity
    private String editWorldCMD = "edit"; // command to go teleport into bedwars and set to builder mode
    private String sworldHelpCMD = "help"; // Help menu for world builder
    private String leaveWorldCMD = "leave"; // leave world back to where you were
    private String resSpawnerInfoCMD = "rsinfo"; // Look up resource spawner info
    private String saveCMD = "save"; // Save configuration
    private String setLockedEntityCMD = "setlock"; // Set requirement by entity to be unlock
    private String setEventMessageCMD = "setmevent"; // set event message when it will be trigger
    private String setEventOrderCMD = "setoevent"; // set event order, when will it be in order every event in timeline
    private String setBedLocCMD = "setbed"; // set bed location
    private String setBlockCMD = "setblock"; // set block on, for default bedrock will be spawned on location
    private String setShopLocationCMD = "setshop"; // set villager shop location
    private String queueSpawnCMD = "setqspawn"; // set queue spawn for hosting a bedwars
    private String setSpawnCMD = "setspawn"; // set world default spawn, this can be use in all kinds of world
    private String setTeamColorCMD = "setcolor"; // set team color prefix
    private String spawnShopCMD = "spawnshop"; // Spawn the shops safely in world
    private String setResSpawnerDurCMD = "setrdur"; // Set resource spawner duration per spawn specifically
    private String setResourceSpawnerCMD = "setrspawn"; // Set Resource Spawner with it's type
    private String setTeamSpawnCMD = "teamspawn"; // Set Team Spawn by name on that location
    private String teamInfoCMD = "teaminfo"; // Look up the team in world info
    private String testResourceSpawnCMD = "testres"; // Test respawning resource spawner
    private String timelineInfoCMD = "tlinfo"; // Look up timeline info
    private String worldInfoCMD = "worldinfo"; // Look up the world info

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender);
            if (args.length == 1) {
                List<String> sworldSubs = new ArrayList<String>();
                sworldSubs.add(addEventCMD);
                sworldSubs.add(setTeamAreaCMD);
                sworldSubs.add(setAreaOppositionCMD);
                sworldSubs.add(setAreaSingleShotCMD);
                sworldSubs.add(addAreaBufferEffectCMD);
                sworldSubs.add(addTeamCMD);
                sworldSubs.add(addRequestCMD);
                sworldSubs.add(removeEventCMD);
                sworldSubs.add(createCMD);
                sworldSubs.add(editWorldCMD);
                sworldSubs.add(deleteResSpawnCMD);
                sworldSubs.add(deleteShopLocCMD);
                sworldSubs.add(deleteTeamCMD);
                sworldSubs.add(deleteLockedEntityCMD);
                sworldSubs.add(sworldHelpCMD);
                sworldSubs.add(leaveWorldCMD);
                sworldSubs.add(resSpawnerInfoCMD);
                sworldSubs.add(saveCMD);
                sworldSubs.add(setLockedEntityCMD);
                sworldSubs.add(setEventMessageCMD);
                sworldSubs.add(setEventOrderCMD);
                sworldSubs.add(setBedLocCMD);
                sworldSubs.add(setShopLocationCMD);
                sworldSubs.add(queueSpawnCMD);
                sworldSubs.add(setTeamColorCMD);
                sworldSubs.add(setSpawnCMD);
                sworldSubs.add(setBlockCMD);
                sworldSubs.add(spawnShopCMD);
                sworldSubs.add(setResSpawnerDurCMD);
                sworldSubs.add(setResourceSpawnerCMD);
                sworldSubs.add(setTeamSpawnCMD);
                sworldSubs.add(teamInfoCMD);
                sworldSubs.add(testResourceSpawnCMD);
                sworldSubs.add(timelineInfoCMD);
                sworldSubs.add(worldInfoCMD);
                return sworldSubs;
            } else if (args.length == 2) {
                // Gives world name hint
                if (args[0].equalsIgnoreCase("edit")) {
                    return systemConf.getWorldNames();
                }
                // Gives coordinate X hint
                else if (args[0].equalsIgnoreCase(setBlockCMD)) {
                    List<String> setBlockHint = new ArrayList<String>();
                    setBlockHint.add("X");
                    return setBlockHint;
                }
                // Hint for world name
                else if (args[0].equalsIgnoreCase(createCMD)) {
                    List<String> createWorldHint = new ArrayList<String>();
                    createWorldHint.add("<world|map-name>");
                    return createWorldHint;
                }
                // Gives team names hint for setTeamSpawnCMD, teamInfoCMD, and setBedLocCMD
                else if (args[0].equalsIgnoreCase(setTeamSpawnCMD) || args[0].equalsIgnoreCase(teamInfoCMD) ||
                        args[0].equalsIgnoreCase(setBedLocCMD) || args[0].equalsIgnoreCase(setTeamColorCMD)) {
                    String worldName = player.getWorld().getName();
                    if (systemConf.getWorldNames().contains(worldName))
                        return systemConf.getTeamNames(worldName);
                }
                // Gives team names hint and public
                else if (args[0].equalsIgnoreCase(setResourceSpawnerCMD) || args[0].equalsIgnoreCase(deleteResSpawnCMD) ||
                        args[0].equalsIgnoreCase(setResSpawnerDurCMD) || args[0].equalsIgnoreCase(setTeamAreaCMD)) {
                    String worldName = player.getWorld().getName();
                    if (systemConf.getWorldNames().contains(worldName)) {
                        List<String> teamNameshint = new ArrayList<String>();
                        teamNameshint.addAll(systemConf.getTeamNames(worldName));
                        teamNameshint.add("PUBLIC");
                        return teamNameshint;
                    }
                }
                // Gives add event hint
                else if (args[0].equalsIgnoreCase(addEventCMD)) {
                    List<String> eventType = new ArrayList<String>();
                    eventType.add(TimelineEventType.EMERALD_UPGRADE.toString());
                    eventType.add(TimelineEventType.DIAMOND_UPGRADE.toString());
                    return eventType;
                }
                // Gives delete event hint
                else if (args[0].equalsIgnoreCase(addEventCMD)) {
                    String worldName = player.getWorld().getName();
                    if (systemConf.getWorldNames().contains(worldName)) {
                        List<String> names = new ArrayList<String>();
                        for (BedwarsGameTimelineEvent ev : systemConf.getTimelineEvents(worldName)) {
                            names.add(ev.getName());
                        }
                        return names;
                    }
                }
                // Gives hint of shop type
                else if (args[0].equalsIgnoreCase(setShopLocationCMD)) {
                    List<String> shopTypeNames = new ArrayList<String>();
                    shopTypeNames.add(BedwarsShopType.ITEMS_SHOP.toString());
                    shopTypeNames.add(BedwarsShopType.PERMA_SHOP.toString());
                    return shopTypeNames;
                }
                // Gives codename hint
                else if (args[0].equalsIgnoreCase(setLockedEntityCMD)) {
                    String inWorldName = player.getWorld().getName();
                    if (systemConf.getWorldNames().contains(inWorldName))
                        return Arrays.asList("<codename>");
                }
                // Gives locked entities codename hint
                else if (args[0].equalsIgnoreCase(addRequestCMD) || args[0].equalsIgnoreCase(deleteLockedEntityCMD)) {
                    String inWorldName = player.getWorld().getName();
                    if (systemConf.getWorldNames().contains(inWorldName))
                        return systemConf.getLockedCodenames(inWorldName);
                }
            } else if (args.length == 3) {
                // Gives coordinate Y hint
                if (args[0].equalsIgnoreCase(setBlockCMD)) {
                    List<String> setBlockHint = new ArrayList<String>();
                    setBlockHint.add("Y");
                    return setBlockHint;
                }
                // Gives duration hint
                else if (args[0].equalsIgnoreCase(addEventCMD)) {
                    List<String> eventType = new ArrayList<String>();
                    eventType.add("<duration-in-second>");
                    return eventType;
                }
                // Gives resource type hint
                else if (args[0].equalsIgnoreCase(setResourceSpawnerCMD) || args[0].equalsIgnoreCase(addRequestCMD)) {
                    List<String> resourceType = new ArrayList<String>();
                    resourceType.add(ResourcesType.IRON.toString());
                    resourceType.add(ResourcesType.GOLD.toString());
                    resourceType.add(ResourcesType.DIAMOND.toString());
                    resourceType.add(ResourcesType.EMERALD.toString());
                    return resourceType;
                }
                // Gives resource spawner codename hint
                else if (args[0].equalsIgnoreCase(deleteResSpawnCMD) || args[0].equalsIgnoreCase(setResSpawnerDurCMD)) {
                    String worldName = player.getWorld().getName();
                    if (systemConf.getWorldNames().contains(worldName)) {
                        // Give hint by teamname or else other public resource spawners
                        return systemConf.getRSCodenames(worldName, args[1]);
                    }
                }
                // Gives hint all public resource spawners
                else if (args[0].equalsIgnoreCase(setLockedEntityCMD)) {
                    String inWorldName = player.getWorld().getName();
                    if (systemConf.getWorldNames().contains(inWorldName))
                        return systemConf.getPublicRSCodenames(inWorldName);
                }
            } else if (args.length == 4) {
                // Gives coordinate Z hint
                if (args[0].equalsIgnoreCase(setBlockCMD)) {
                    List<String> setBlockHint = new ArrayList<String>();
                    setBlockHint.add("Z");
                    return setBlockHint;
                }
                // Gives set name hint
                else if (args[0].equalsIgnoreCase(addEventCMD)) {
                    List<String> eventType = new ArrayList<String>();
                    eventType.add("<event-name|display>");
                    return eventType;
                }
                // Gives duration hint
                else if (args[0].equalsIgnoreCase(setResSpawnerDurCMD)) {
                    List<String> eventType = new ArrayList<String>();
                    eventType.add("<duration-per-spawn>");
                    return eventType;
                }
                // Gives <amount> hint
                else if (args[0].equalsIgnoreCase(addRequestCMD)) {
                    String inWorldName = player.getWorld().getName();
                    if (systemConf.getWorldNames().contains(inWorldName))
                        return Arrays.asList("<amount>");
                }
            }
        }

        return null;
    }

}
