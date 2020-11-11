package com.joenastan.sleepingwar.plugin.commands.TabCompletor;

import com.joenastan.sleepingwar.plugin.game.ResourcesType;
import com.joenastan.sleepingwar.plugin.SleepingWarsPlugin;
import com.joenastan.sleepingwar.plugin.utility.GameSystemConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SworldCommands implements TabCompleter {

    private static final GameSystemConfig systemConf = SleepingWarsPlugin.getGameSystemConfig();
    private String createCMD = "create"; // command to create the world
    private String editWorldCMD = "edit"; // command to go teleport into bedwars and set to builder mode
    private String deleteResourceSpawnCMD = "delrspawn"; // delete resource spawner by name
    private String leaveWorldCMD = "leave"; // leave world back to where you were
    private String openBuilderCMD = "openb"; // exclusive kit for bedwars
    private String queueSpawnCMD = "setqspawn"; // set queue spawn for hosting a bedwars
    private String setSpawnCMD = "setspawn"; // set world default spawn, this can be use in all kinds of world
    private String setBlockCMD = "setblock"; // set block on, for default bedrock will be spawned on location
    private String setResourceSpawnerCMD = "setrspawn"; // Set Resource Spawner with it's type
    private String systemCMD = "system"; // check and edit game system, or you can edit in yml file in plugin folder
    private String setTeamSpawnCMD = "teamspawn"; // Set Team Spawn by name on that location
    private String testResourceSpawnCMD = "testres"; // Test respawning resource spawner

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender);
            if (args.length == 1) {
                List<String> sworldSubs = new ArrayList<String>();
                sworldSubs.add(createCMD);
                sworldSubs.add(editWorldCMD);
                sworldSubs.add(deleteResourceSpawnCMD);
                sworldSubs.add(leaveWorldCMD);
                sworldSubs.add(openBuilderCMD);
                sworldSubs.add(queueSpawnCMD);
                sworldSubs.add(setSpawnCMD);
                sworldSubs.add(setBlockCMD);
                sworldSubs.add(setResourceSpawnerCMD);
                sworldSubs.add(openBuilderCMD);
                sworldSubs.add(systemCMD);
                sworldSubs.add(setTeamSpawnCMD);
                sworldSubs.add(testResourceSpawnCMD);
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
                // Gives team names hint
                else if (args[0].equalsIgnoreCase(setTeamSpawnCMD)) {
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
                else if (args[0].equalsIgnoreCase(deleteResourceSpawnCMD)) {
                    String worldName = player.getWorld().getName();
                    if (systemConf.getAllWorldName().contains(worldName)) {
                        List<String> names = new ArrayList<String>();
                        names.addAll(systemConf.getResourceSpawnerPack(worldName).keySet());
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
            } else if (args.length == 4) {
                // Gives coordinate Z hint
                if (args[0].equalsIgnoreCase(setBlockCMD)) {
                    List<String> setBlockHint = new ArrayList<String>();
                    setBlockHint.add("Z");
                    return setBlockHint;
                }
                // Gives resource type hint
                else if (args[0].equalsIgnoreCase(setResourceSpawnerCMD)) {
                    return ResourcesType.getAcceptedStrings();
                }
            }
        }

        return null;
    }

}