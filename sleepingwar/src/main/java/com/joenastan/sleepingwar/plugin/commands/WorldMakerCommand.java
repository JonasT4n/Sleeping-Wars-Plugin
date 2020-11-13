package com.joenastan.sleepingwar.plugin.commands;

import com.joenastan.sleepingwar.plugin.game.ResourceSpawner;
import com.joenastan.sleepingwar.plugin.game.ResourcesType;
import com.joenastan.sleepingwar.plugin.SleepingWarsPlugin;
import com.joenastan.sleepingwar.plugin.events.OnBuilderModeEvents;
import com.joenastan.sleepingwar.plugin.events.CustomEvents.BedwarsGameTimelineEvent;
import com.joenastan.sleepingwar.plugin.events.CustomEvents.TimelineEventType;
import com.joenastan.sleepingwar.plugin.utility.GameSystemConfig;
import com.joenastan.sleepingwar.plugin.utility.VoidGenerator;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class WorldMakerCommand implements Listener, CommandExecutor {

    private final GameSystemConfig systemConfig = SleepingWarsPlugin.getGameSystemConfig();
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
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 0) {
            // Classify commands, if there's none of them then ignored
            String initialSubCommand = args[0];
            // Only player in server can use this command
            if (sender instanceof Player) {
                Player player = ((Player) sender);
                // Sworld Help Command
                if (initialSubCommand.equalsIgnoreCase(sworldHelpCMD)) {
                    if (args.length < 2) 
                        helpMessage(sender);
                    else
                        helpMessage(sender, args[1]);
                }
                // Set World Spawn
                else if (initialSubCommand.equalsIgnoreCase(setSpawnCMD) && player.hasPermission("sleepywar.builder")) {
                    setSpawn(player, player.getWorld());
                }
                // Add event timeline
                else if (initialSubCommand.equalsIgnoreCase(addEventCMD) && player.hasPermission("sleepywar.builder")) {
                    addEventCommand(player, args);
                }
                // Delete event timeline
                else if (initialSubCommand.equalsIgnoreCase(removeEventCMD) && player.hasPermission("sleepywar.builder")) {
                    deleteEventCommand(player, args);
                }
                // Teleport and Edit to bedwars world
                else if (initialSubCommand.equalsIgnoreCase(editWorldCMD)) {
                    editWorld(player, args);
                }
                // Create World
                else if (initialSubCommand.equalsIgnoreCase(createCMD) && player.hasPermission("sleepywar.builder")) {
                    createWorld(sender, args);
                }
                // Create World
                else if (initialSubCommand.equalsIgnoreCase(setBedLocCMD) && player.hasPermission("sleepywar.builder")) {
                    setBedLocation(player, args);
                }
                // Set Queue Spawn
                else if (initialSubCommand.equalsIgnoreCase(queueSpawnCMD) && player.hasPermission("sleepywar.builder")) {
                    setQueueSpawn(player);
                }
                // Set Resource Spawner
                else if (initialSubCommand.equalsIgnoreCase(setResourceSpawnerCMD) && player.hasPermission("sleepywar.builder")) {
                    addResourceSpawner(player, args);
                }
                // Set Block anywhere
                else if (initialSubCommand.equalsIgnoreCase(setBlockCMD) && player.hasPermission("sleepywar.builder")) {
                    setBlockOnWorld(player, args);
                }
                // Open Builder GUI
                else if (initialSubCommand.equalsIgnoreCase(openBuilderCMD) && player.hasPermission("sleepywar.builder")) {
                    openBuilderGUI(player);
                }
                // Test resource spawn
                else if (initialSubCommand.equalsIgnoreCase(testResourceSpawnCMD) && player.hasPermission("sleepywar.builder")) {
                    testActivateResourceSpawner(player);
                }
                // Delete resource spawn
                else if (initialSubCommand.equalsIgnoreCase(deleteResSpawnCMD) && player.hasPermission("sleepywar.builder")) {
                    deleteResourceSpawner(player, args);
                }
                // Set Team Spawner
                else if (initialSubCommand.equalsIgnoreCase(setTeamSpawnCMD) && player.hasPermission("sleepywar.builder")) {
                    setTeamSpawn(player, args);
                }
                // Leave World
                else if (initialSubCommand.equalsIgnoreCase(leaveWorldCMD)) {
                    leaveWorldEdit(player);
                }
                // World resource spawner info
                else if (initialSubCommand.equalsIgnoreCase(resSpawnerInfo)) {
                    sendResourceSpawnerInfo(player);
                }
                // World general info
                else if (initialSubCommand.equalsIgnoreCase(worldInfoCMD)) {
                    sendWorldInfo(player);
                }
            }
        } else {
            helpMessage(sender);
        }
        return true;
    }

    // Page 1 Default help menu
    private void helpMessage(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "(Page 1/2) Sleeping World Maker Commands for World Building. " + ChatColor.AQUA + "List of sub-commands:\n" +
                ChatColor.GREEN + "create => Create bedwars world\n" +
                "edit => Teleport to bedwars world\n" +
                "setqspawn => Set room queue spawn\n"+
                "system => Check and edit game system in bedwars world\n" +
                "setrspawn => Set resource spawner\n" +
                "setspawn => Set world default spawn on your position\n" +
                "openb => Open extra kit for Bedwars");
    }

    private void helpMessage(CommandSender sender, String page) {
        if (page.equals("2")) {
            sender.sendMessage(ChatColor.GOLD + " (Page 2/2)Sleeping World Maker Commands for World Building. " + ChatColor.AQUA + "List of sub-commands:\n" +
                ChatColor.GREEN + "leave => Save and leave the bedwars world\n" +
                "setblock => Set bedrock on air, just for safety\n" +
                "setqspawn => Set room queue spawn\n"+
                "teamspawn => Set specific team spawner on your current location\n" +
                "delrspawn => Delete resource spawner\n" +
                "setspawn => Set world default spawn on your position\n" +
                "openb => Open extra kit for Bedwars");
        } else { // default is page 1
            sender.sendMessage(ChatColor.GOLD + "(Page 1/2) Sleeping World Maker Commands for World Building. " + ChatColor.AQUA + "List of sub-commands:\n" +
                ChatColor.GREEN + "create => Create bedwars world\n" +
                "edit => Teleport to bedwars world\n" +
                "setqspawn => Set room queue spawn\n"+
                "system => Check and edit game system in bedwars world\n" +
                "setrspawn => Set resource spawner on your current location\n" +
                "setspawn => Set world default spawn on your position\n" +
                "openb => Open extra kit for building a bedwars");
        }
    }

    private void addEventCommand(Player player, String[] args) {
        if (args.length < 4) {
            player.sendMessage(ChatColor.GOLD + "You can add one of these event by typing " + 
                    ChatColor.LIGHT_PURPLE + "/sworld addevent [options], List of options: \n" +
                    ChatColor.AQUA + "emerald-up <duration|trigger-in> <eventname|displayname>\n" +
                    "diamond-up <duration|trigger-in> <eventname|displayname>");
        } else {
            String worldName = player.getWorld().getName();
            if (systemConfig.getAllWorldName().contains(worldName)) {
                if (systemConfig.getTimelineEvents(worldName).size() < 10) {
                    TimelineEventType typeEvent = TimelineEventType.fromString(args[1]);
                    if (typeEvent != null && isNumber(args[2])) {
                        int dur = Integer.parseInt(args[2]);
                        BedwarsGameTimelineEvent nEvent = new BedwarsGameTimelineEvent(typeEvent, (float)dur, args[3]);
                        systemConfig.getTimelineEvents(worldName).add(nEvent);
                        player.sendMessage(ChatColor.GREEN + args[3] + " added to world event.");
                        return;
                    }

                    player.sendMessage(ChatColor.YELLOW + "Invalid Arguments.");
                } else {
                    player.sendMessage(ChatColor.YELLOW + "Amount of events reach it's limit, delete one of the event by using /sworld delevent <number>");
                }
            } else {
                player.sendMessage(ChatColor.RED + "You are not in Bedwars world building.");
            }
        }
    }

    private void deleteEventCommand(Player player, String[] args) {
        String worldName = player.getWorld().getName();
        if (args.length < 2) {
            if (systemConfig.getAllWorldName().contains(worldName)) {
                String listDescription = ChatColor.GRAY + "";
                List<BedwarsGameTimelineEvent> timelineEvents = systemConfig.getTimelineEvents(worldName);
                for (int i = 0; i < timelineEvents.size(); i++) {
                    BedwarsGameTimelineEvent thisEvent = timelineEvents.get(i);
                    if (i == timelineEvents.size() - 1)
                        listDescription += String.format("%d. %s (trigger in %.1f second(s))", i + 1, thisEvent.getEventType(), thisEvent.getTriggerSeconds());
                    else
                        listDescription += String.format("%d. %s (trigger in %.1f second(s))\n", i + 1, thisEvent.getEventType(), thisEvent.getTriggerSeconds());
                }
                player.sendMessage(ChatColor.GOLD + "Delete one of this timeline with /sworld delevent <name-of-event>\n" + listDescription);
            } else {
                player.sendMessage(ChatColor.YELLOW + "You are not in Bedwars world.");
            }
        } else {
            if (systemConfig.getAllWorldName().contains(worldName)) {
                BedwarsGameTimelineEvent ev = systemConfig.deleteTimelineEvent(worldName, args[1]);
                if (ev != null) {
                    player.sendMessage(ChatColor.GREEN + "Successfully deleted \'" + ev.getName() + "\' event.");
                } else {
                    player.sendMessage(ChatColor.RED + "Failed to delete, event may be not in the list.");
                }
            } else {
                player.sendMessage(ChatColor.YELLOW + "You are not in Bedwars world.");
            }
        }
    }

    private void setBedLocation(Player player, String[] args) {
        String worldName = player.getWorld().getName();
        if (systemConfig.getAllWorldName().contains(worldName)) {
            if (args.length < 2) {
                player.sendMessage(ChatColor.GREEN + "You need to insert team name. " + ChatColor.YELLOW + "/sworld setbed <teamname>");
            } else {
                if (systemConfig.getAllTeamName(worldName).contains(args[1])) {
                    OnBuilderModeEvents.getCustomBuilderEntity().get(player).setTeamChoice(args[1]);
                    player.sendMessage(ChatColor.GREEN + "Holding team \'" + args[1] + "\', now place any kinds of bed.");
                } else {
                    player.sendMessage(ChatColor.YELLOW + "Team not exists.");
                }
            }
        } else {
            player.sendMessage(ChatColor.RED + "You are not in Bedwars world building.");
        }
    }

    private void setQueueSpawn(Player player) {
        String worldName = player.getWorld().getName();
        if (systemConfig.getAllWorldName().contains(worldName)) {
            Location settledSpawn = systemConfig.getQueueLocations(worldName, player.getLocation());
            player.sendMessage(ChatColor.LIGHT_PURPLE + "Queue spawn settled on " + ChatColor.AQUA +
                    String.format("(X/Y/Z): %d/%d/%d", (int) settledSpawn.getX(), (int) settledSpawn.getY(), (int) settledSpawn.getZ()));
        } else {
            player.sendMessage(ChatColor.RED + "You are not in Bedwars world building.");
        }
    }

    private void editWorld(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.GREEN + "You need to insert a world name. " +
                    ChatColor.YELLOW + "/sworld edit <worldname>");
        } else {
            World w = Bukkit.getServer().getWorld(args[1]);
            if (w != null) {
                if (systemConfig.getAllWorldName().contains(w.getName())) {
                    player.sendMessage(ChatColor.LIGHT_PURPLE + "Teleporting to world " + args[1] + "!");
                    player.teleport(w.getSpawnLocation());
                } else {
                    player.sendMessage(ChatColor.RED + "Unrecognised world. this world is not registered by plugin.");
                }
            } else {
                player.sendMessage(ChatColor.RED + "World is not exists, create your Bedwars world using" + ChatColor.GOLD + "/sworld create");
            }
        }
    }

    private void leaveWorldEdit(Player player) {
        if (OnBuilderModeEvents.getCustomBuilderEntity().containsKey(player)) {
            // Teleport back to default world
            OnBuilderModeEvents.getCustomBuilderEntity().get(player).returnEntity();
        } else {
            player.sendMessage(ChatColor.YELLOW + "You are not in builder world.");
        }
    }

    private void openBuilderGUI(Player player) {
        Inventory extraBuilderGUI = Bukkit.getServer().createInventory(null, 18, "Bedwars Builder Pack");

        ItemStack upgradeMobEgg = new ItemStack(Material.VILLAGER_SPAWN_EGG);
        ItemMeta upgradeEggMeta = upgradeMobEgg.getItemMeta();
        upgradeEggMeta.setDisplayName("Bedwars Upgrade Villager");
        upgradeEggMeta.setLore(Arrays.asList("A Perma Upgrade Shop"));
        upgradeMobEgg.setItemMeta(upgradeEggMeta);

        ItemStack shopMobEgg = new ItemStack(Material.VILLAGER_SPAWN_EGG);
        ItemMeta shopEggMeta = shopMobEgg.getItemMeta();
        shopEggMeta.setDisplayName("Bedwars Shop Villager");
        shopEggMeta.setLore(Arrays.asList("All Bedwars Shop kit"));
        shopMobEgg.setItemMeta(shopEggMeta);

        extraBuilderGUI.setItem(0, upgradeMobEgg);
        extraBuilderGUI.setItem(1, shopMobEgg);

        player.openInventory(extraBuilderGUI);
    }

    private void setSpawn(Player player, World world) {
        Location playerLoc = player.getLocation();
        world.setSpawnLocation(playerLoc.getBlockX(), playerLoc.getBlockY(), playerLoc.getBlockZ());
    }

    private void setTeamSpawn(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.GREEN + "You need to insert team name. " +
                    ChatColor.YELLOW + "/sworld teamspawn <teamname>");
        } else {
            Location loc = player.getLocation();
            String wName = loc.getWorld().getName();
            if (systemConfig.getAllWorldName().contains(wName)) {
                if (systemConfig.getAllTeamName(wName).contains(args[1])) {
                    systemConfig.getTeamSpawner(wName).put(args[1], loc);
                    player.sendMessage(ChatColor.AQUA + "Spawn set for team \"" + args[1] + "\"");
                } else {
                    player.sendMessage(ChatColor.DARK_PURPLE + "Name of team not available in this world.");
                }
            }
        }
    }

    private void setBlockOnWorld(Player player, String[] args) {
        String worldName = player.getWorld().getName();
        if (args.length < 4) {
            player.sendMessage(ChatColor.YELLOW + "Invalid Argument, do /sworld setblock <X> <Y> <Z>");
        } else {
            if (systemConfig.getAllWorldName().contains(worldName)) {
                if (isNumber(args[1]) && isNumber(args[2]) && isNumber(args[3])) {
                    World w = Bukkit.getWorld(worldName);
                    int x = Integer.parseInt(args[1]);
                    int y = Integer.parseInt(args[2]);
                    int z = Integer.parseInt(args[3]);
                    w.getBlockAt(x, y, z).setType(Material.BEDROCK);
                } else {
                    player.sendMessage(ChatColor.YELLOW + "Invalid Argument, input coordinate must be number");
                }
            } else {
                player.sendMessage(ChatColor.YELLOW + "You are not in Bedwars world building.");
            }
        }
    }

    private void testActivateResourceSpawner(Player player) {
        String worldName = player.getWorld().getName();
        if (systemConfig.getAllWorldName().contains(worldName)) {
            Map<String, ResourceSpawner> spawners = systemConfig.getResourceSpawnersPack(worldName);

            if (spawners.isEmpty() == true) {
                player.sendMessage(ChatColor.YELLOW + "There are no spawner in this world.");
                return;
            }

            ResourceSpawner rsample = spawners.get(spawners.keySet().toArray()[0]);
            if (rsample.isRunning()) {
                for (Map.Entry<String, ResourceSpawner> rsp : spawners.entrySet()) {
                    rsp.getValue().isRunning(false);
                }
                player.sendMessage(ChatColor.BLUE + "World is stopping it's spawner.");
            } else {
                for (Map.Entry<String, ResourceSpawner> rsp : spawners.entrySet()) {
                    rsp.getValue().isRunning(true);
                }
                player.sendMessage(ChatColor.BLUE + "World is testing it's spawner.");
            }
        } else {
            player.sendMessage(ChatColor.RED + "World is not registered as Bedwars world.");
        }
    }

    private void deleteResourceSpawner(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.GREEN + "You need to insert an existing spawner name. " +
                    ChatColor.YELLOW + "/sworld delrspawn <codename>");
        } else {
            String availableWorldName = player.getWorld().getName();
            if (systemConfig.getAllWorldName().contains(availableWorldName)) {
                if (systemConfig.deleteResourceSpawner(availableWorldName, args[1])) {
                    player.sendMessage(ChatColor.GREEN + "Resource spawner successfully deleted.");
                    systemConfig.Save();
                } else {
                    player.sendMessage(ChatColor.RED + "Resource spawner may be not exists or failed.");
                }
            } else {
                player.sendMessage(ChatColor.RED + "World is not registered as Bedwars world.");
            }
        }
    }

    private void addResourceSpawner(Player player, String[] args) {
        if (args.length < 4) {
            player.sendMessage(ChatColor.DARK_PURPLE + "Invalid Input.");
        } else {
            String worldName = player.getWorld().getName();
            if (systemConfig.getAllWorldName().contains(worldName)) {
                Location loc = player.getLocation();
                String wname = player.getWorld().getName();
                ResourcesType rcst = ResourcesType.fromString(args[2]);
                if (rcst == null) {
                    player.sendMessage(ChatColor.RED + "Invalid resource type.");
                } else {
                    if (systemConfig.getAllTeamName(wname).contains(args[1])) {
                        ResourceSpawner newRSpawener = new ResourceSpawner(args[3], loc, rcst);
                        systemConfig.getResourceSpawnersPack(wname, args[1]).put(args[3], newRSpawener);
                        player.sendMessage(ChatColor.GREEN + "Added " + args[2] + " spawner with name \"" + args[3] + "\" on team " + args[1]);
                    } else if (args[1].equalsIgnoreCase("public")) {
                        ResourceSpawner newRSpawener = new ResourceSpawner(args[3], loc, rcst);
                        systemConfig.getResourceSpawnersPack(wname, "PUBLIC").put(args[3], newRSpawener);
                        player.sendMessage(ChatColor.GREEN + "Added public " + args[2] + " spawner with name \"" + args[3]);
                    } else {
                        player.sendMessage(ChatColor.YELLOW + "There is no team named \"" + args[1] + "\" in this world.");
                    }
                }
            } else {
                player.sendMessage(ChatColor.RED + "You are not in Bedwars World Building.");
            }
        }
    }

    private void sendResourceSpawnerInfo(Player player) {
        String worldName = player.getWorld().getName();
        if (systemConfig.getAllWorldName().contains(worldName)) {
            Map<String, ResourceSpawner> spawners = systemConfig.getResourceSpawnersPack(worldName);
            String description = String.format("%s~[Resource Spawners in %s]~\n", ChatColor.GREEN + "", worldName);
            description += String.format("%sThere are %s%d %sResource Spawner(s)\n", ChatColor.AQUA + "", ChatColor.LIGHT_PURPLE + "", 
                    systemConfig.getResourceSpawnersPack(worldName).size(), ChatColor.AQUA + "");
            for (Map.Entry<String, ResourceSpawner> rsp : spawners.entrySet()) {
                ResourceSpawner rs = rsp.getValue();
                if (rs.getTypeResourceSpawner() == ResourcesType.IRON) {
                    description += String.format("%s%s[%s, %.1fsec(s)/spawn]; ", ChatColor.GRAY + "", rs.getCodename(), 
                            ResourcesType.IRON.toString(), rs.getSecondsPerSpawn());
                } else if (rs.getTypeResourceSpawner() == ResourcesType.GOLD) {
                    description += String.format("%s%s[%s, %.1fsec(s)/spawn]; ", ChatColor.GOLD + "", rs.getCodename(), 
                            ResourcesType.GOLD.toString(), rs.getSecondsPerSpawn());
                } else if (rs.getTypeResourceSpawner() == ResourcesType.DIAMOND) {
                    description += String.format("%s%s[%s, %.1fsec(s)/spawn]; ", ChatColor.AQUA + "", rs.getCodename(), 
                            ResourcesType.DIAMOND.toString(), rs.getSecondsPerSpawn());
                } else { // EMERALD
                    description += String.format("%s%s[%s, %.1fsec(s)/spawn]; ", ChatColor.GREEN + "", rs.getCodename(), 
                            ResourcesType.EMERALD.toString(), rs.getSecondsPerSpawn());
                }
            }
            player.sendMessage(description);
        } else {
            player.sendMessage(ChatColor.RED + "You are not in Bedwars World Building.");
        }
    }
    
    private void sendWorldInfo(Player player) {
        String worldName = player.getWorld().getName();
        if (systemConfig.getAllWorldName().contains(worldName)) {
            Map<String, String> teamNames = systemConfig.getTeamPrefix(worldName);
            String description = String.format("%s~ [\'%s\' Info] ~%sThere are %s%d %steams:\n", ChatColor.GREEN + "", worldName, 
                    ChatColor.AQUA + "", ChatColor.LIGHT_PURPLE + "", teamNames.size(), ChatColor.AQUA + "");
            List<String> tns = systemConfig.getAllTeamName(worldName);
            for (int i = 0; i < tns.size(); i++) {
                String teamName = tns.get(i);
                if (i == tns.size() - 1) 
                    description += getColor(teamNames.get(teamName)) + teamName + ChatColor.WHITE + ";\n\n";
                else
                    description += getColor(teamNames.get(teamName)) + teamName + ChatColor.WHITE + ", ";
            }
            Location queueLoc = systemConfig.getQueueLocations(worldName);
            description += String.format("Queue Location on: X:%d Y:%d Z:%d\n", queueLoc.getBlockX(), queueLoc.getBlockY(), queueLoc.getBlockZ());
            description += String.format("%sEvents in timeline count: %s%d\n", ChatColor.AQUA + "", ChatColor.LIGHT_PURPLE + "", 
                    systemConfig.getTimelineEvents(worldName).size());
            description += String.format("%sThere are %s%d %sResource Spawner(s)", ChatColor.AQUA + "", ChatColor.LIGHT_PURPLE + "", 
                    systemConfig.getResourceSpawnersPack(worldName).size(), ChatColor.AQUA + "");
            player.sendMessage(description);
        } else {
            player.sendMessage(ChatColor.RED + "You are not in Bedwars World Building.");
        }
    }

    private void createWorld(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.GREEN + "You need to insert a world name. " +
                    ChatColor.YELLOW + "/sworld create <worldname>");
        } else {
            // Creating the world
            if (SleepingWarsPlugin.getPlugin().getServer().getWorld(args[1]) == null) {
                sender.sendMessage(ChatColor.BLUE + "Creating World...");
                WorldCreator creator = new WorldCreator(args[1])
                        .environment(Environment.NORMAL)
                        .type(WorldType.FLAT)
                        .hardcore(false)
                        .generateStructures(false);
                creator.generator(new VoidGenerator());
                World world = Bukkit.createWorld(creator);

                // Set world config
                world.setAutoSave(false);
                world.setKeepSpawnInMemory(false);
                world.setDifficulty(Difficulty.PEACEFUL);
                world.setAnimalSpawnLimit(0);
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
                world.setTime(0);

                // Add initial blocks of bedrocks on mid point
                for (int x = -2; x < 3; x++) {
                    for (int z = -2; z < 3; z++) {
                        world.getBlockAt(new Location(world, x, 45, z)).setType(Material.BEDROCK);
                    }
                }
                systemConfig.defaultSystemConfig(creator, world);
                sender.sendMessage(ChatColor.GREEN + "World \"" + args[1] + "\" has been Created.");
            } else {
                sender.sendMessage(ChatColor.BLUE + "World \"" + args[1] + "\" already exists. Aborted!!!");
            }
        }
    }

    private boolean isNumber(String numStr) {
        for (int i = 0; i < numStr.length(); i++) {
            switch (numStr.charAt(i)) {
                case '1':
                    break;
                case '2':
                    break;
                case '3':
                    break;
                case '4':
                    break;
                case '5':
                    break;
                case '6':
                    break;
                case '7':
                    break;
                case '8':
                    break;
                case '9':
                    break;
                case '0':
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    private String getColor(String prefix) {
        if (prefix.equalsIgnoreCase("blue")) {
            return ChatColor.BLUE + "";
        } else if (prefix.equalsIgnoreCase("green")) {
            return ChatColor.GREEN + "";
        } else if (prefix.equalsIgnoreCase("yellow")) {
            return ChatColor.YELLOW + "";
        } else if (prefix.equalsIgnoreCase("aqua")) {
            return ChatColor.AQUA + "";
        } else if (prefix.equalsIgnoreCase("red")) {
            return ChatColor.RED + "";
        } else if (prefix.equalsIgnoreCase("light-putple")) {
            return ChatColor.LIGHT_PURPLE + "";
        } else if (prefix.equalsIgnoreCase("gold")) {
            return ChatColor.GOLD + "";
        } else if (prefix.equalsIgnoreCase("gray")) {
            return ChatColor.GRAY + "";
        } else {
            return ChatColor.WHITE + "";
        }
    }

}
