package com.joenastan.sleepingwar.plugin.commands;

import com.joenastan.sleepingwar.plugin.enumtypes.BedwarsShopType;
import com.joenastan.sleepingwar.plugin.game.GameManager;
import com.joenastan.sleepingwar.plugin.game.ResourceSpawner;
import com.joenastan.sleepingwar.plugin.enumtypes.ResourcesType;
import com.joenastan.sleepingwar.plugin.SleepingWarsPlugin;
import com.joenastan.sleepingwar.plugin.events.OnBuilderModeEvents;
import com.joenastan.sleepingwar.plugin.events.CustomEvents.BedwarsGameTimelineEvent;
import com.joenastan.sleepingwar.plugin.enumtypes.TimelineEventType;
import com.joenastan.sleepingwar.plugin.utility.GameSystemConfig;
import com.joenastan.sleepingwar.plugin.utility.UsefulStaticFunctions;
import com.joenastan.sleepingwar.plugin.utility.VoidGenerator;
import com.joenastan.sleepingwar.plugin.utility.CustomDerivedEntity.PlayerBedwarsBuilderEntity;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

// import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldMakerCommand implements Listener, CommandExecutor {

    // Constants
    private final GameSystemConfig systemConfig = SleepingWarsPlugin.getGameSystemConfig();
    private final GameManager gameManager = SleepingWarsPlugin.getGameManager();
    private Map<Player, PlayerBedwarsBuilderEntity> customBuilderEntity = OnBuilderModeEvents.getCustomBuilderEntity();

    // Command names
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

    // Temporary(s)
    private Map<String, List<ResourceSpawner>> allResourceSpawners = new HashMap<String, List<ResourceSpawner>>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 0) {
            // Classify commands, if there's none of them then ignored
            String initialSubCommand = args[0];
            // Only player in server can use this command
            if (sender instanceof Player) {
                Player player = ((Player) sender);
                // Check if player currently playing Bedwars
                if (gameManager.getRoom(player.getWorld().getName()) != null) {
                    player.sendMessage(ChatColor.RED + "You can't edit world while playing Bedwars");
                    return true;
                }
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
                // Add team command
                else if (initialSubCommand.equalsIgnoreCase(addTeamCMD) && player.hasPermission("sleepywar.builder")) {
                    addNewTeam(player, args);
                }
                // Delete team command
                else if (initialSubCommand.equalsIgnoreCase(deleteTeamCMD) && player.hasPermission("sleepywar.builder")) {
                    deleteTeam(player, args);
                }
                // Set team color prefix
                else if (initialSubCommand.equalsIgnoreCase(setTeamColorCMD) && player.hasPermission("sleepywar.builder")) {
                    setColorPrefix(player, args);
                }
                // Create World
                else if (initialSubCommand.equalsIgnoreCase(createCMD) && player.hasPermission("sleepywar.builder")) {
                    createWorld(sender, args);
                }
                // Set bed location
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
                // Set shop location
                else if (initialSubCommand.equalsIgnoreCase(setShopLocationCMD) && player.hasPermission("sleepywar.builder")) {
                    setShopLocation(player, args);
                }
                // Delete shop location
                else if (initialSubCommand.equalsIgnoreCase(deleteShopLocCMD) && player.hasPermission("sleepywar.builder")) {
                    deleteShopLocation(player, args);
                }
                // Set entity react requirement
                else if (initialSubCommand.equalsIgnoreCase(setLockedEntityCMD) && player.hasPermission("sleepywar.builder")) {
                    setLockedEntity(player, args);
                }
                // Add request to unlock an entity
                else if (initialSubCommand.equalsIgnoreCase(addRequestCMD) && player.hasPermission("sleepywar.builder")) {
                    addRequestLockedEntity(player, args);
                }
                // Set Area Buffer
                else if (initialSubCommand.equalsIgnoreCase(setTeamAreaCMD) && player.hasPermission("sleepywar.builder")) {
                    setAreaBufferHandler(player, args);
                }
                // Test resource spawn
                else if (initialSubCommand.equalsIgnoreCase(testResourceSpawnCMD) && player.hasPermission("sleepywar.builder")) {
                    testActivateResourceSpawner(player);
                }
                // Test shop spawn
                else if (initialSubCommand.equalsIgnoreCase(spawnShopCMD) && player.hasPermission("sleepywar.builder")) {
                    spawnShop(player);
                }
                // Delete resource spawn
                else if (initialSubCommand.equalsIgnoreCase(deleteResSpawnCMD) && player.hasPermission("sleepywar.builder")) {
                    deleteResourceSpawner(player, args);
                }
                // Set duration spawn on specific codename resource spawner
                else if (initialSubCommand.equalsIgnoreCase(setResSpawnerDurCMD) && player.hasPermission("sleepywar.builder")) {
                    setResourceSpawnerDuration(player, args);
                }
                // Set Team Spawner
                else if (initialSubCommand.equalsIgnoreCase(setTeamSpawnCMD) && player.hasPermission("sleepywar.builder")) {
                    setTeamSpawn(player, args);
                }
                // Save configuration, this automatically run when there's nobody in world
                else if (initialSubCommand.equalsIgnoreCase(saveCMD) && player.hasPermission("sleepywar.builder")) {
                    systemConfig.Save();
                }
                // Teleport and Edit to bedwars world
                else if (initialSubCommand.equalsIgnoreCase(editWorldCMD)) {
                    editWorld(player, args);
                }
                // Leave World
                else if (initialSubCommand.equalsIgnoreCase(leaveWorldCMD)) {
                    leaveWorldEdit(player);
                }
                // World resource spawner info
                else if (initialSubCommand.equalsIgnoreCase(resSpawnerInfoCMD)) {
                    sendResourceSpawnerInfo(player);
                }
                // Game timeline event information
                else if (initialSubCommand.equalsIgnoreCase(timelineInfoCMD)) {
                    sendTimelineInfo(player);
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
                "help => This help menu\n" +
                "edit => Teleport to bedwars world\n" +
                "setqspawn => Set room queue spawn\n"+
                "system => Check and edit game system in bedwars world\n" +
                "setrspawn => Set resource spawner on your current location\n" +
                "setspawn => Set world default spawn on your position\n");
                //"openb => Open extra kit for building a bedwars");
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
            if (systemConfig.getWorldNames().contains(worldName)) {
                if (systemConfig.getTimelineEvents(worldName).size() < 10) {
                    TimelineEventType typeEvent = TimelineEventType.fromString(args[1]);
                    if (typeEvent != null && isPositiveNumber(args[2])) {
                        int dur = Integer.parseInt(args[2]);
                        BedwarsGameTimelineEvent nEvent = new BedwarsGameTimelineEvent(typeEvent, (float)dur, args[3], 0, typeEvent.toString() + " Event");
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
            if (systemConfig.getWorldNames().contains(worldName)) {
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
            if (systemConfig.getWorldNames().contains(worldName)) {
                if (systemConfig.deleteTimelineEvent(worldName, args[1])) {
                    player.sendMessage(ChatColor.GREEN + "Successfully deleted \'" + args[1] + "\' event.");
                } else {
                    player.sendMessage(ChatColor.RED + "Failed to delete, event may be not in the list.");
                }
            } else {
                player.sendMessage(ChatColor.YELLOW + "You are not in Bedwars world.");
            }
        }
    }

    private void addNewTeam(Player player, String[] args) {
        // TODO: Add Team command
    }

    private void deleteTeam(Player player, String[] args) {
        // TODO: Delete Team Command
    }

    private void setColorPrefix(Player player, String[] args) {
        // TODO: Set Team color prefix
    }

    private void setLockedEntity(Player player, String[] args) {
        String inWorldName = player.getWorld().getName();
        PlayerBedwarsBuilderEntity playerBE = customBuilderEntity.get(player);
        if (systemConfig.getWorldNames().contains(inWorldName) && playerBE != null) {
            if (args.length < 2) {
                player.sendMessage(ChatColor.YELLOW + "Invalid Argument, do /sworld " + setLockedEntityCMD + " <codename> [PUBLIC-rs-codename]");
            } else {
                playerBE.setTeamChoice("PUBLIC");
                playerBE.addCodenameHolder(args[1]);
                if (args.length >= 3)
                    playerBE.addCodenameHolder(args[2]);
                player.sendMessage(ChatColor.GREEN + "Place any type of door, it will automatically locked when inside the gameplay.");
            }
        } else {
            player.sendMessage(ChatColor.YELLOW + "You are not in Bedwars world building.");
        }
    }

    private void addRequestLockedEntity(Player player, String[] args) {
        String inWorldName = player.getWorld().getName();
        if (systemConfig.getWorldNames().contains(inWorldName)) {
            if (args.length < 4) {
                player.sendMessage(ChatColor.YELLOW + "Invalid Input, /sworld " + addRequestCMD + " <codename> <resource-type> <amount>");
            } else {
                if (systemConfig.getLockedCodenames(inWorldName).contains(args[1])) {
                    ResourcesType typeReq = ResourcesType.fromString(args[2]);
                    if (typeReq != null && isPositiveNumber(args[3])) {
                        if (systemConfig.addRequestOnLockedEntity(inWorldName, args[1], typeReq, Integer.parseInt(args[3]))) {
                            player.sendMessage(String.format("%sSuccessfully added required to unlock %s in game.", ChatColor.AQUA + "", args[1]));
                            return;
                        }
                    }
                }
                player.sendMessage(ChatColor.RED + "Locked entity codename or type request may not exists.");
            }
        } else {
            player.sendMessage(ChatColor.YELLOW + "You are not in Bedwars world building.");
        }
    }

    private void setBedLocation(Player player, String[] args) {
        String worldName = player.getWorld().getName();
        PlayerBedwarsBuilderEntity playerBE = customBuilderEntity.get(player);
        if (systemConfig.getWorldNames().contains(worldName) && playerBE != null) {
            if (args.length < 2) {
                player.sendMessage(ChatColor.GREEN + "You need to insert team name. " + ChatColor.YELLOW + "/sworld setbed <teamname>\n");
                player.sendMessage(ChatColor.GREEN + "All beds that has been set:");
                String description = "";
                for (String tn : systemConfig.getTeamNames(worldName)) {
                    Location bedLoc = systemConfig.getTeamBedLocation(player.getWorld(), worldName, tn);
                    String teamColorPrefix = systemConfig.getTeamColorPrefix(worldName, tn);
                    if (UsefulStaticFunctions.isMaterialBed(bedLoc.getBlock().getType()))
                        description += String.format("%s: %s%b; ", UsefulStaticFunctions.getColorString(teamColorPrefix) + tn, ChatColor.GREEN + "", true);
                    else
                        description += String.format("%s: %s%b; ", UsefulStaticFunctions.getColorString(teamColorPrefix) + tn, ChatColor.RED + "", false);
                }
                player.sendMessage(description);
            } else {
                if (systemConfig.getTeamNames(worldName).contains(args[1])) {
                    customBuilderEntity.get(player).setTeamChoice(args[1]);
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
        Location playerPos = player.getLocation();
        if (systemConfig.setQueueLocation(worldName, playerPos)) {
            player.sendMessage(ChatColor.LIGHT_PURPLE + "Queue spawn settled on " + ChatColor.AQUA +
                    String.format("(X/Y/Z): %d/%d/%d", (int)playerPos.getX(), (int)playerPos.getY(), (int)playerPos.getZ()));
        } else {
            player.sendMessage(ChatColor.RED + "You are not in Bedwars world building.");
        }
    }

    private void editWorld(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.GREEN + "You need to insert a world name. " + ChatColor.YELLOW + "/sworld edit <worldname>");
        } else {
            World w = Bukkit.getServer().getWorld(args[1]);
            if (w != null) {
                if (systemConfig.getWorldNames().contains(w.getName())) {
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
        World bedwarsWorld = player.getWorld();
        if (customBuilderEntity.containsKey(player)) {
            // Teleport back to default world
            customBuilderEntity.get(player).returnEntity();
            if (bedwarsWorld.getPlayerCount() == 0 && allResourceSpawners.containsKey(bedwarsWorld.getName())) {
                // Remove temporary stuff in builder world
                List<ResourceSpawner> listRS = allResourceSpawners.remove(bedwarsWorld.getName());
                for (ResourceSpawner rs : listRS)
                    if (rs.isRunning())
                        rs.isRunning(false);
            }
        } else {
            player.sendMessage(ChatColor.YELLOW + "You are not in builder world.");
        }
    }

    private void setShopLocation(Player player, String[] args) {
        String inWorldName = player.getWorld().getName();
        if (systemConfig.getWorldNames().contains(inWorldName)) {
            if (args.length < 2) {
                player.sendMessage(ChatColor.GREEN + "You need to insert type of shop. " + ChatColor.YELLOW + "/sworld setshop <type>");
            } else {
                BedwarsShopType pickedShopType = BedwarsShopType.fromString(args[1]);
                if (pickedShopType != null) {
                    if (systemConfig.addShopLocationSpawn(inWorldName, pickedShopType, player.getLocation())) {
                        player.sendMessage(ChatColor.GREEN + pickedShopType.toString() + " settled on your location where you are standing.");
                        return;
                    }
                } 
                player.sendMessage(ChatColor.RED + "Invalid Input argument");
            }
        } else {
            player.sendMessage(ChatColor.YELLOW + "You are not in Bedwars world building.");
        }
    }

    private void deleteShopLocation(Player player, String[] args) {
        String inWorldName = player.getWorld().getName();
        if (systemConfig.getWorldNames().contains(inWorldName)) {
            Map<BedwarsShopType, List<Location>> shopLocMap = systemConfig.getShops(player.getWorld(), inWorldName);
            if (args.length < 3) {
                player.sendMessage(ChatColor.GREEN + "You need to insert the index number. " + ChatColor.YELLOW + "/sworld delshop <index>\n");
                String description = ChatColor.LIGHT_PURPLE + "List of shop spawn:\n";
                for (Map.Entry<BedwarsShopType, List<Location>> shopLocTypeEntry : shopLocMap.entrySet()) {
                    // Set types by colors
                    if (shopLocTypeEntry.getKey() == BedwarsShopType.ITEMS_SHOP)
                        description += ChatColor.YELLOW + ": ";
                    else if (shopLocTypeEntry.getKey() == BedwarsShopType.PERMA_SHOP)
                        description += ChatColor.AQUA + ": ";
                    else
                        description += ChatColor.WHITE + ": ";
                    // Get all shop location informations
                    for (int i = 0; i < shopLocTypeEntry.getValue().size(); i++) {
                        Location loc = shopLocTypeEntry.getValue().get(i);
                        description += String.format("[%d. X:%d Y:%d Z:%d]; ", i + 1, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
                    }
                }
                player.sendMessage(description);
            } else {
                BedwarsShopType typeSelect = BedwarsShopType.fromString(args[1]);
                if (typeSelect != null && isPositiveNumber(args[2])) {
                    if (systemConfig.deleteShopLocation(inWorldName, typeSelect, Integer.parseInt(args[2]) - 1)) {
                        player.sendMessage(ChatColor.LIGHT_PURPLE + "Shop spawn successfully deleted");
                        return;
                    }
                }
                player.sendMessage(ChatColor.RED + "Argument may be invalid, index exceeded, or failed");        
            }
        } else {
            player.sendMessage(ChatColor.YELLOW + "You are not in Bedwars world building.");
        }
    }

    private void setSpawn(Player player, World world) {
        String inWorldName = world.getName();
        if (systemConfig.getWorldNames().contains(inWorldName)) {
            Location playerLoc = player.getLocation();
            world.setSpawnLocation(playerLoc.getBlockX(), playerLoc.getBlockY(), playerLoc.getBlockZ());
            player.sendMessage(ChatColor.GREEN + "Default world spawn settled");
        } else {
            player.sendMessage(ChatColor.YELLOW + "You are not in Bedwars world building.");
        }
    }

    private void setTeamSpawn(Player player, String[] args) {
        String inWorldName = player.getWorld().getName();
        if (systemConfig.getWorldNames().contains(inWorldName)) {
            if (args.length < 2) {
                player.sendMessage(ChatColor.GREEN + "You need to insert team name. " + ChatColor.YELLOW + "/sworld teamspawn <teamname>");
            } else {
                Location loc = player.getLocation();
                if (systemConfig.setTeamSpawnLoc(inWorldName, args[1], loc)) {
                    player.sendMessage(String.format("%sSpawn set for team \"%s\" at (X/Y/Z): %d/%d/%d", ChatColor.AQUA + "", 
                            args[1], loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
                    return;
                }
                player.sendMessage(ChatColor.DARK_PURPLE + "Invalid input or team may not be exists");
            }
        } else {
            player.sendMessage(ChatColor.YELLOW + "You are not in Bedwars world building.");
        }
    }

    private void setBlockOnWorld(Player player, String[] args) {
        String worldName = player.getWorld().getName();
        if (systemConfig.getWorldNames().contains(worldName)) {
            if (args.length < 4) {
                player.sendMessage(ChatColor.YELLOW + "Invalid Argument, do /sworld setblock <X> <Y> <Z>");
            } else {
                if (isPositiveNumber(args[1]) && isPositiveNumber(args[2]) && isPositiveNumber(args[3])) {
                    World w = Bukkit.getWorld(worldName);
                    int x = Integer.parseInt(args[1]);
                    int y = Integer.parseInt(args[2]);
                    int z = Integer.parseInt(args[3]);
                    w.getBlockAt(x, y, z).setType(Material.BEDROCK);
                } else {
                    player.sendMessage(ChatColor.YELLOW + "Invalid Argument, input coordinate must be number");
                }
            }
        } else {
            player.sendMessage(ChatColor.YELLOW + "You are not in Bedwars world building.");
        }
    }

    private void setAreaBufferHandler(Player player, String[] args) {
        String inWorldName = player.getWorld().getName();
        PlayerBedwarsBuilderEntity playerBE = customBuilderEntity.get(player);
        if (systemConfig.getWorldNames().contains(inWorldName) && playerBE != null) {
            if (args.length < 2) {
                player.sendMessage(ChatColor.YELLOW + "Invalid Argument, do /sworld areabuff <teamname|PUBLIC>");
            } else {
                if (!systemConfig.getTeamNames(inWorldName).contains(args[1]))
                    args[1] = "PUBLIC";
                playerBE.setTeamChoice(args[1]);
                playerBE.setRequiredAmountLoc(2);
                player.sendMessage(ChatColor.GREEN + "Place 2 block to create a box of area buffer.");
            }
        } else {
            player.sendMessage(ChatColor.YELLOW + "You are not in Bedwars world building.");
        }
    }

    private void testActivateResourceSpawner(Player player) {
        String worldName = player.getWorld().getName();
        if (systemConfig.getWorldNames().contains(worldName)) {
            // Check if there's already contains the tester
            if (allResourceSpawners.containsKey(worldName)) {
                // Deactivate and delete a list
                List<ResourceSpawner> listRS = allResourceSpawners.remove(worldName);
                for (ResourceSpawner rSpawner : listRS)
                    rSpawner.isRunning(false);
                listRS.clear();
                player.sendMessage(ChatColor.BLUE + "World is stopping it's spawner.");
            } else {
                // Check if no resource spawners in world
                if (systemConfig.countOverallRS(worldName) == 0) {
                    player.sendMessage(ChatColor.YELLOW + "There are no resource spawner exists.");
                    return;
                }
                // Put list and activate it
                List<ResourceSpawner> spawners = systemConfig.getWorldResourceSpawners(player.getWorld(), worldName);
                for (ResourceSpawner rSpawner : spawners)
                    rSpawner.isRunning(true);
                allResourceSpawners.put(worldName, spawners);
                player.sendMessage(ChatColor.BLUE + "World is testing it's spawner.");
            }
        } else {
            player.sendMessage(ChatColor.RED + "World is not registered as Bedwars world.");
        }
    }

    private void spawnShop(Player player) {
        String inWorldName = player.getWorld().getName();
        if (systemConfig.getWorldNames().contains(inWorldName)) {
            Map<BedwarsShopType, List<Location>> shopLoc = systemConfig.getShops(player.getWorld(), inWorldName);
            // Check if it is empty
            if (shopLoc.isEmpty()) {
                player.sendMessage(ChatColor.YELLOW + "There are no shop spawner exists.");
                return;
            }
            PotionEffect slowEffect = new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 9999);
            for (Map.Entry<BedwarsShopType, List<Location>> shopLocEntry : shopLoc.entrySet()) {
                for (Location loc : shopLocEntry.getValue()) {
                    Entity ent = loc.getWorld().spawnEntity(loc, EntityType.VILLAGER);
                    if (ent instanceof LivingEntity) {
                        LivingEntity entLive = (LivingEntity)ent;
                        entLive.addPotionEffect(slowEffect);
                    }
                    if (shopLocEntry.getKey() == BedwarsShopType.PERMA_SHOP) {
                        ent.setCustomName("Bedwars Upgrade Villager");
                    } else {
                        ent.setCustomName("Bedwars Shop Villager");
                    }
                }
            }
            player.sendMessage(ChatColor.AQUA + "All shop spawned.");
        } else {
            player.sendMessage(ChatColor.YELLOW + "You are not in Bedwars world building.");
        }
    }

    private void deleteResourceSpawner(Player player, String[] args) {
        String availableWorldName = player.getWorld().getName();
        if (systemConfig.getWorldNames().contains(availableWorldName)) {
            if (args.length < 3) {
                player.sendMessage(ChatColor.GREEN + "You need to insert an existing spawner name. " + 
                        ChatColor.YELLOW + "/sworld delrspawn <teamname|PUBLIC> <codename>");
            } else {
                if (systemConfig.deleteResourceSpawner(availableWorldName, args[1], args[2])) {
                    player.sendMessage(ChatColor.GREEN + "Resource spawner successfully deleted.");
                    return;
                }
                player.sendMessage(ChatColor.RED + "Resource spawner may be not exists or failed.");
            }
        } else {
            player.sendMessage(ChatColor.YELLOW + "You are not in Bedwars world building.");
        }
    }

    private void setResourceSpawnerDuration(Player player, String[] args) {
        String inWorldName = player.getWorld().getName();
        if (systemConfig.getWorldNames().contains(inWorldName)) {
            if (args.length < 4) {
                player.sendMessage(ChatColor.GREEN + "You need to insert an existing spawner name. " + 
                        ChatColor.YELLOW + "/sworld setrdur <teamname|PUBLIC> <codename> <duration-per-spawn>");
            } else {
                if (isPositiveNumber(args[3])) {
                    int dur = Integer.parseInt(args[3]);
                    // If team name not exists then consider as public
                    if (!systemConfig.getTeamNames(inWorldName).contains(args[1]))
                        args[1] = "PUBLIC";
                    if (systemConfig.setRSDuration(inWorldName, args[1], args[2], dur)) {
                        player.sendMessage(ChatColor.GREEN + args[2] + " resource spawner duration set: " + dur + " sec(s)");
                        return;
                    }
                    player.sendMessage(ChatColor.RED + "Invalid Input or Resource spawner may not exists.");
                } else {
                    player.sendMessage(ChatColor.YELLOW + "Duration must be number.");
                }
            }
        } else {
            player.sendMessage(ChatColor.YELLOW + "You are not in Bedwars world building.");
        }
    }

    private void addResourceSpawner(Player player, String[] args) {
        String worldName = player.getWorld().getName();
        if (systemConfig.getWorldNames().contains(worldName)) {
            if (args.length < 4) {
                player.sendMessage(ChatColor.DARK_PURPLE + "Invalid Input.");
            } else {
                Location loc = player.getLocation();
                String wname = player.getWorld().getName();
                ResourcesType rcst = ResourcesType.fromString(args[2]);
                if (rcst == null) {
                    player.sendMessage(ChatColor.RED + "Invalid resource type.");
                } else {
                    if (systemConfig.getTeamNames(wname).contains(args[1])) {
                        ResourceSpawner newRSpawener = new ResourceSpawner(args[3], loc, rcst);
                        systemConfig.addResourceSpawner(wname, args[1], newRSpawener);
                        player.sendMessage(ChatColor.GREEN + "Added " + rcst.toString() + " spawner with name \"" + args[3] + "\" for team " + args[1]);
                    } else {
                        ResourceSpawner newRSpawener = new ResourceSpawner(args[3], loc, rcst);
                        systemConfig.addResourceSpawner(wname, "PUBLIC", newRSpawener);
                        player.sendMessage(ChatColor.GREEN + "Added public " + rcst.toString() + " spawner with name \"" + args[3]);
                    }
                }
            }
        } else {
            player.sendMessage(ChatColor.YELLOW + "You are not in Bedwars world building.");
        }
        
    }

    private void sendResourceSpawnerInfo(Player player) {
        String worldName = player.getWorld().getName();
        if (systemConfig.getWorldNames().contains(worldName)) {
            int amountRS = systemConfig.countOverallRS(worldName);
            player.sendMessage(String.format("%s~ Resource Spawners in %s ~", ChatColor.GREEN + "", worldName));
            player.sendMessage(String.format("%s%d %sResource Spawner(s): [codename; second/spawn]", ChatColor.LIGHT_PURPLE + "", 
                    amountRS, ChatColor.AQUA + ""));
            if (amountRS == 0) {
                player.sendMessage(ChatColor.ITALIC + "EMPTY...");
                return;
            }
            String description = "";
            for (ResourceSpawner rSpawner : systemConfig.getWorldResourceSpawners(player.getWorld(), worldName)) {
                if (rSpawner.getTypeResourceSpawner() == ResourcesType.IRON) {
                    description += String.format("%s[%s; %.1f] ", ChatColor.GRAY + "", rSpawner.getCodename(), rSpawner.getSecondsPerSpawn());
                } else if (rSpawner.getTypeResourceSpawner() == ResourcesType.GOLD) {
                    description += String.format("%s[%s; %.1f] ", ChatColor.GOLD + "", rSpawner.getCodename(), rSpawner.getSecondsPerSpawn());
                } else if (rSpawner.getTypeResourceSpawner() == ResourcesType.DIAMOND) {
                    description += String.format("%s[%s; %.1f] ", ChatColor.AQUA + "", rSpawner.getCodename(), rSpawner.getSecondsPerSpawn());
                } else { // EMERALD
                    description += String.format("%s[%s; %.1f] ", ChatColor.GREEN + "", rSpawner.getCodename(), rSpawner.getSecondsPerSpawn());
                }
            }
            player.sendMessage(description);
        } else {
            player.sendMessage(ChatColor.RED + "You are not in Bedwars World Building.");
        }
    }

    private void sendTimelineInfo(Player player) {
        String inWorldName = player.getWorld().getName();
        if (systemConfig.getWorldNames().contains(inWorldName)) {
            int amountEvents = systemConfig.countEventsInTimeline(inWorldName);
            player.sendMessage(String.format("%s~ Timeline Events in %s ~", ChatColor.GREEN + "", inWorldName));
            player.sendMessage(String.format("%s%d %sEvent(s) in Timeline: [name; trigger-in; type]", ChatColor.LIGHT_PURPLE + "", amountEvents, ChatColor.AQUA + ""));
            if (amountEvents == 0) {
                player.sendMessage(ChatColor.ITALIC + "EMPTY...");
                return;
            }
            String description = "";
            for (BedwarsGameTimelineEvent ev : systemConfig.getTimelineEvents(inWorldName)) {
                if (ev.getEventType() == TimelineEventType.DIAMOND_UPGRADE) {
                    description += String.format("%s[%s; %d; %s] ", ChatColor.AQUA + "", ev.getName(), ev.getTriggerSeconds(), ev.getEventType().toString());
                } else if (ev.getEventType() == TimelineEventType.EMERALD_UPGRADE) {
                    description += String.format("%s[%s; %d; %s] ", ChatColor.GREEN + "", ev.getName(), ev.getTriggerSeconds(), ev.getEventType().toString());
                } else if (ev.getEventType() == TimelineEventType.BUFFER_ZONE_ACTIVE) {
                    description += String.format("%s[%s; %d; %s] ", ChatColor.GOLD + "", ev.getName(), ev.getTriggerSeconds(), ev.getEventType().toString());
                } else if (ev.getEventType() == TimelineEventType.DESTROY_ALL_BED) {
                    description += String.format("%s[%s; %d; %s] ", ChatColor.YELLOW + "", ev.getName(), ev.getTriggerSeconds(), ev.getEventType().toString());
                } else { // World border Shrinking
                    description += String.format("%s[%s; %d; %s] ", ChatColor.RED + "", ev.getName(), ev.getTriggerSeconds(), ev.getEventType().toString());
                }
            }
            player.sendMessage(description);
        } else {
            player.sendMessage(ChatColor.RED + "You are not in Bedwars World Building.");
        }
    }
    
    private void sendWorldInfo(Player player) {
        String worldName = player.getWorld().getName();
        if (systemConfig.getWorldNames().contains(worldName)) {
            List<String> teamNames = systemConfig.getTeamNames(worldName);
            player.sendMessage(String.format("%s~ [\'%s\' Info] ~", ChatColor.GREEN + "", worldName));
            player.sendMessage(String.format("%sThere are %s%d %steams:", ChatColor.AQUA + "", ChatColor.LIGHT_PURPLE + "", teamNames.size(), 
                    ChatColor.AQUA + ""));
            String description = "";
            for (String tn : teamNames) {
                String colorPref = systemConfig.getTeamColorPrefix(worldName, tn);
                if (colorPref != null)
                    description += UsefulStaticFunctions.getColorString(colorPref) + tn + ChatColor.WHITE + "; ";
                else
                    description += UsefulStaticFunctions.getColorString("white") + tn + ChatColor.WHITE + "; ";
            }
            player.sendMessage(description);
            Location queueLoc = systemConfig.getQueueLocations(player.getWorld(), worldName);
            player.sendMessage(String.format("Queue Location on (X/Y/Z): %d/%d/%d; ", queueLoc.getBlockX(), queueLoc.getBlockY(), queueLoc.getBlockZ()));
            player.sendMessage(String.format("%sEvents in timeline count: %s%d; ", ChatColor.AQUA + "", ChatColor.LIGHT_PURPLE + "", 
                    systemConfig.countEventsInTimeline(worldName)));
            player.sendMessage(String.format("%sThere are %s%d %sResource Spawner(s)", ChatColor.AQUA + "", ChatColor.LIGHT_PURPLE + "", 
                    systemConfig.countOverallRS(worldName), ChatColor.AQUA + ""));
        } else {
            player.sendMessage(ChatColor.RED + "You are not in Bedwars World Building.");
        }
    }

    private void createWorld(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.GREEN + "You need to insert a world name. " + ChatColor.YELLOW + "/sworld create <worldname>");
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
                systemConfig.saveWorldConfig(creator);
                sender.sendMessage(ChatColor.GREEN + "World \"" + args[1] + "\" has been Created.");
            } else {
                sender.sendMessage(ChatColor.BLUE + "World \"" + args[1] + "\" already exists. Aborted!!!");
            }
        }
    }

    private boolean isPositiveNumber(String numStr) {
        int dotCount = 0;
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
                case '.':
                    dotCount++;
                    if (dotCount > 1) 
                        return false;
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
