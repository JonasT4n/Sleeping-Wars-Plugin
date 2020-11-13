package com.joenastan.sleepingwar.plugin.commands.TabCompletor;

import com.joenastan.sleepingwar.plugin.game.ResourcesType;
import com.joenastan.sleepingwar.plugin.SleepingWarsPlugin;
import com.joenastan.sleepingwar.plugin.events.CustomEvents.BedwarsGameTimelineEvent;
import com.joenastan.sleepingwar.plugin.events.CustomEvents.TimelineEventType;
import com.joenastan.sleepingwar.plugin.utility.GameSystemConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SworldCommands implements TabCompleter {

    private static final GameSystemConfig systemConf = SleepingWarsPlugin.getGameSystemConfig();
    private String addEventCMD = "addevent"; // Add event timeline
    private String removeEventCMD = "delevent"; // delete event timeline
    private String createCMD = "create"; // command to create the world
    private String deleteResSpawnCMD = "delrspawn"; // delete resource spawner by name
    private String editWorldCMD = "edit"; // command to go teleport into bedwars and set to builder mode
    private String sworldHelpCMD = "help"; // Help menu for world builder
    private String leaveWorldCMD = "leave"; // leave world back to where you were
    private String openBuilderCMD = "openb"; // exclusive kit for bedwars
    private String resSpawnerInfo = "rsinfo"; // Look up resource spawner info
    private String setBedLocCMD = "setbed"; // set bed location
    private String setBlockCMD = "setblock"; // set block on, for default bedrock will be spawned on location
    private String queueSpawnCMD = "setqspawn"; // set queue spawn for hosting a bedwars
    private String setSpawnCMD = "setspawn"; // set world default spawn, this can be use in all kinds of world
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
                sworldSubs.add(removeEventCMD);
                sworldSubs.add(createCMD);
                sworldSubs.add(editWorldCMD);
                sworldSubs.add(deleteResSpawnCMD);
                sworldSubs.add(sworldHelpCMD);
                sworldSubs.add(leaveWorldCMD);
                sworldSubs.add(openBuilderCMD);
                sworldSubs.add(resSpawnerInfo);
                sworldSubs.add(setBedLocCMD);
                sworldSubs.add(queueSpawnCMD);
                sworldSubs.add(setSpawnCMD);
                sworldSubs.add(setBlockCMD);
                sworldSubs.add(setResourceSpawnerCMD);
                sworldSubs.add(openBuilderCMD);
                sworldSubs.add(setTeamSpawnCMD);
                sworldSubs.add(teamInfoCMD);
                sworldSubs.add(testResourceSpawnCMD);
                sworldSubs.add(timelineInfoCMD);
                sworldSubs.add(worldInfoCMD);
                return sworldSubs;
            } else if (args.length == 2) {
                // Gives world name hint
                if (args[0].equalsIgnoreCase("edit")) {
                    return systemConf.getAllWorldName();
                }
                // Gives coordinate X hint
                else if (args[0].equalsIgnoreCase(setBlockCMD)) {
                    List<String> setBlockHint = new ArrayList<String>();
                    setBlockHint.add("X");
                    return setBlockHint;
                }
                // Gives team names hint for setTeamSpawnCMD, teamInfoCMD, and setBedLocCMD
                else if (args[0].equalsIgnoreCase(setTeamSpawnCMD) || args[0].equalsIgnoreCase(teamInfoCMD) || args[0].equalsIgnoreCase(setBedLocCMD)) {
                    String worldName = player.getWorld().getName();
                    if (systemConf.getAllWorldName().contains(worldName)) {
                        return systemConf.getAllTeamName(worldName);
                    }
                }
                // Gives team names hint and public
                else if (args[0].equalsIgnoreCase(setResourceSpawnerCMD)) {
                    String worldName = player.getWorld().getName();
                    if (systemConf.getAllWorldName().contains(worldName)) {
                        List<String> teamNameshint = new ArrayList<String>();
                        teamNameshint.addAll(systemConf.getAllTeamName(worldName));
                        teamNameshint.add("public");
                        return teamNameshint;
                    }
                }
                // Gives hint of resource spawner codename
                else if (args[0].equalsIgnoreCase(deleteResSpawnCMD)) {
                    String worldName = player.getWorld().getName();
                    if (systemConf.getAllWorldName().contains(worldName)) {
                        List<String> names = new ArrayList<String>();
                        names.addAll(systemConf.getResourceSpawnersPack(worldName).keySet());
                        return names;
                    }
                }
                // Gives resource type hint
                else if (args[0].equalsIgnoreCase(setResourceSpawnerCMD)) {
                    List<String> resourceType = new ArrayList<String>();
                    resourceType.add(ResourcesType.IRON.toString());
                    resourceType.add(ResourcesType.GOLD.toString());
                    resourceType.add(ResourcesType.DIAMOND.toString());
                    resourceType.add(ResourcesType.EMERALD.toString());
                    return resourceType;
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
                    if (systemConf.getAllWorldName().contains(worldName)) {
                        List<String> names = new ArrayList<String>();
                        for (BedwarsGameTimelineEvent ev : systemConf.getTimelineEvents(worldName)) {
                            names.add(ev.getName());
                        }
                        return names;
                    }
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
            }
        }

        return null;
    }

}
