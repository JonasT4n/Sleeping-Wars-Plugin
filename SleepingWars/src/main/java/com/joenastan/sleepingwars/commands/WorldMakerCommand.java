package com.joenastan.sleepingwars.commands;

import com.joenastan.sleepingwars.enumtypes.BedwarsShopType;
import com.joenastan.sleepingwars.game.GameManager;
import com.joenastan.sleepingwars.game.InventoryMenus.BedwarsShopMenu;
import com.joenastan.sleepingwars.game.InventoryMenus.BedwarsUpgradeMenu;
import com.joenastan.sleepingwars.game.ResourceSpawner;
import com.joenastan.sleepingwars.enumtypes.ResourcesType;
import com.joenastan.sleepingwars.SleepingWarsPlugin;
import com.joenastan.sleepingwars.events.OnBuilderModeEvents;
import com.joenastan.sleepingwars.events.CustomEvents.BedwarsTimelineEvent;
import com.joenastan.sleepingwars.enumtypes.TimelineEventType;
import com.joenastan.sleepingwars.utility.DataFiles.GameSystemConfig;
import com.joenastan.sleepingwars.utility.PluginStaticColor;
import com.joenastan.sleepingwars.utility.PluginStaticFunc;
import com.joenastan.sleepingwars.utility.VoidGenerator;
import com.joenastan.sleepingwars.utility.CustomDerivedEntity.PlayerBedwarsBuilderEntity;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldMakerCommand implements Listener, CommandExecutor {

    // Constants
    private final GameSystemConfig systemConfig = SleepingWarsPlugin.getGameSystemConfig();
    private final GameManager gameManager = SleepingWarsPlugin.getGameManager();
    private final Map<Player, PlayerBedwarsBuilderEntity> customBuilderEntity = OnBuilderModeEvents.getCustomBuilderEntity();

    // Command names
    public static final String ADD_EVENT_CMD = "addevent"; // Add event timeline
    public static final String SET_AREA_BUFFER_CMD = "areabuff"; // set team area potion effect
    public static final String SET_AREA_OPPOSITION_CMD = "areaopp"; // set whether the area is for team or the opposition
    public static final String SET_AREA_SINGLE_FX_CMD = "areasin"; // set whether the area only one time effect run
    public static final String ADD_AREA_BUFFER_FX_CMD = "areapot"; // Add potion effects into buffer zone
    public static final String ADD_TEAM_CMD = "addteam"; // Add another team
    public static final String ADD_LOCKED_REQUEST_CMD = "addlreq"; // Add a request to unlock the locked entity
    public static final String CREATE_WORLD_CMD = "create"; // command to create the world
    public static final String REMOVE_EVENT_CMD = "rmevent"; // delete event timeline
    public static final String REMOVE_RESOURCE_SPAWNER_CMD = "rmrspawn"; // delete resource spawner by name
    public static final String REMOVE_SHOP_SPAWN_LOCATION_CMD = "rmshop"; // delete shop spawner
    public static final String REMOVE_TEAM_CMD = "rmteam"; // delete an existing team
    public static final String REMOVE_LOCKED_ENTITY_CMD = "rmlock"; // delete locked entity
    public static final String TP_EDIT_WORLD_CMD = "edit"; // command to go teleport into bedwars and set to builder mode
    public static final String COMMAND_HELP_CMD = "help"; // Help menu for world builder
    public static final String TP_LEAVE_WORLD_CMD = "leave"; // leave world back to where you were
    public static final String SAVE_CMD = "save"; // Save configuration
    public static final String SET_BORDER_CMD = "setborder"; // set world border
    public static final String SET_SHRUNK_BORDER_CMD = "setshrunkborder"; // set border in world shrinking event
    public static final String SET_LOCKED_ENTITY_CMD = "setlock"; // Set requirement by entity to be unlock
    public static final String SET_EVENT_MESSAGE_CMD = "seteventmsg"; // set event message when it will be trigger
    public static final String SET_EVENT_ORDER_CMD = "seteventord"; // set event order, when will it be in order every event in timeline
    public static final String SET_BED_LOCATION_CMD = "setbed"; // set bed location
    public static final String SET_BLOCK_ON_CMD = "setblock"; // set block on, for default bedrock will be spawned on location
    public static final String SET_SHOP_SPAWN_LOCATION_CMD = "setshop"; // set villager shop location
    public static final String SET_QUEUE_SPAWN_CMD = "setqspawn"; // set queue spawn for hosting a bedwars
    public static final String SET_WORLD_SPAWN_CMD = "setspawn"; // set world default spawn, this can be use in all kinds of world
    public static final String SET_TEAM_RAW_COLOR_CMD = "setcolor"; // set team color prefix
    public static final String SET_RESOURCE_SPAWNER_FREQ_CMD = "setrfreq"; // Set resource spawner duration per spawn specifically
    public static final String SET_RESOURCE_SPAWNER_CMD = "setrspawn"; // Set Resource Spawner with it's type
    public static final String SET_TEAM_SPAWN_LOCATION_CMD = "teamspawn"; // Set Team Spawn by name on that location
    public static final String SPAWN_SHOP_CMD = "spawnshop"; // Spawn the shops safely in world
    public static final String TEST_RESOURCE_SPAWNER_CMD = "testres"; // Test respawning resource spawner

    // Information Commands
    public static final String RESOURCE_SPAWNER_INFO_CMD = "rsinfo"; // Look up resource spawner info
    public static final String TEAM_INFO_CMD = "teaminfo"; // Look up the team in world info
    public static final String TIMELINE_INFO_CMD = "tlinfo"; // Look up timeline info
    public static final String WORLD_INFO_CMD = "worldinfo"; // Look up the world info

    // Temporary variables
    private final Map<String, List<ResourceSpawner>> allResourceSpawners = new HashMap<>();
    private final static String FAIL_COMMAND_USAGE = ChatColor.RED + "[Failed] ";
    private final static String WARN_COMMAND_USAGE = ChatColor.YELLOW + "[Warning] ";
    private final static String PASS_COMMAND_USAGE = ChatColor.GREEN + "[Passed] ";

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd,
                             @Nonnull String label, String[] args) {
        if (args.length > 0) {
            // Classify commands, if there's none of them then ignored
            String initialSubCommand = args[0];
            // Only player in server can use this command
            if (sender instanceof Player) {
                Player player = ((Player) sender);
                // Check if player currently playing Bedwars
                if (gameManager.getRoom(player.getWorld().getName()) != null) {
                    player.sendMessage(ChatColor.RED + "You can't edit this world while playing Bedwars");
                    return true;
                }
                // SWorld Help Command
                if (initialSubCommand.equalsIgnoreCase(COMMAND_HELP_CMD)) {
                    if (args.length < 2)
                        helpMessage(sender);
                    else
                        helpMessage(sender, args[1]);
                }
                // Set World Spawn
                else if (initialSubCommand.equalsIgnoreCase(SET_WORLD_SPAWN_CMD) &&
                        player.hasPermission("sleepywar.builder")) {
                    setSpawn(player, player.getWorld());
                }
                // Add event timeline
                else if (initialSubCommand.equalsIgnoreCase(ADD_EVENT_CMD) &&
                        player.hasPermission("sleepywar.builder")) {
                    addEventCommand(player, args);
                }
                // Delete event timeline
                else if (initialSubCommand.equalsIgnoreCase(REMOVE_EVENT_CMD) &&
                        player.hasPermission("sleepywar.builder")) {
                    deleteEventCommand(player, args);
                }
                // Set event procedural or flow order
                else if (initialSubCommand.equalsIgnoreCase(SET_EVENT_ORDER_CMD) &&
                        player.hasPermission("sleepywar.builder")) {
                    setEventOrderCommand(player, args);
                }
                // Set event message
                else if (initialSubCommand.equalsIgnoreCase(SET_EVENT_MESSAGE_CMD) &&
                        player.hasPermission("sleepywar.builder")) {
                    setEventMSGCommand(player, args);
                }
                // Add team command
                else if (initialSubCommand.equalsIgnoreCase(ADD_TEAM_CMD) &&
                        player.hasPermission("sleepywar.builder")) {
                    addNewTeam(player, args);
                }
                // Delete team command
                else if (initialSubCommand.equalsIgnoreCase(REMOVE_TEAM_CMD) &&
                        player.hasPermission("sleepywar.builder")) {
                    deleteTeam(player, args);
                }
                // Set team color prefix
                else if (initialSubCommand.equalsIgnoreCase(SET_TEAM_RAW_COLOR_CMD) &&
                        player.hasPermission("sleepywar.builder")) {
                    setColorPrefix(player, args);
                }
                // Create World
                else if (initialSubCommand.equalsIgnoreCase(CREATE_WORLD_CMD) &&
                        player.hasPermission("sleepywar.builder")) {
                    createWorld(sender, args);
                }
                // Set bed location
                else if (initialSubCommand.equalsIgnoreCase(SET_BED_LOCATION_CMD) &&
                        player.hasPermission("sleepywar.builder")) {
                    setBedLocation(player, args);
                }
                // Set Queue Spawn
                else if (initialSubCommand.equalsIgnoreCase(SET_QUEUE_SPAWN_CMD) &&
                        player.hasPermission("sleepywar.builder")) {
                    setQueueSpawn(player);
                }
                // Set Resource Spawner
                else if (initialSubCommand.equalsIgnoreCase(SET_RESOURCE_SPAWNER_CMD) &&
                        player.hasPermission("sleepywar.builder")) {
                    addResourceSpawner(player, args);
                }
                // Set Block anywhere
                else if (initialSubCommand.equalsIgnoreCase(SET_BLOCK_ON_CMD) &&
                        player.hasPermission("sleepywar.builder")) {
                    setBlockOnWorld(player, args);
                }
                // Set shop location
                else if (initialSubCommand.equalsIgnoreCase(SET_SHOP_SPAWN_LOCATION_CMD) &&
                        player.hasPermission("sleepywar.builder")) {
                    setShopLocation(player, args);
                }
                // Delete shop location
                else if (initialSubCommand.equalsIgnoreCase(REMOVE_SHOP_SPAWN_LOCATION_CMD) &&
                        player.hasPermission("sleepywar.builder")) {
                    deleteShopLocation(player, args);
                }
                // Set world border
                else if (initialSubCommand.equalsIgnoreCase(SET_BORDER_CMD) &&
                        player.hasPermission("sleepywar.builder")) {
                    setWorldBorder(player, args);
                }
                // Set shrunk border
                else if (initialSubCommand.equalsIgnoreCase(SET_SHRUNK_BORDER_CMD) &&
                        player.hasPermission("sleepywar.builder")) {
                    setWorldShrunkBorder(player, args);
                }
                // Set entity react requirement
                else if (initialSubCommand.equalsIgnoreCase(SET_LOCKED_ENTITY_CMD) &&
                        player.hasPermission("sleepywar.builder")) {
                    setLockedEntity(player, args);
                }
                // Add request to unlock an entity
                else if (initialSubCommand.equalsIgnoreCase(ADD_LOCKED_REQUEST_CMD) &&
                        player.hasPermission("sleepywar.builder")) {
                    addRequestLockedEntity(player, args);
                }
                // Delete locked entity
                else if (initialSubCommand.equalsIgnoreCase(REMOVE_LOCKED_ENTITY_CMD) &&
                        player.hasPermission("sleepywar.builder")) {
                    deleteLockedEntity(player, args);
                }
                // Set Area Buffer
                else if (initialSubCommand.equalsIgnoreCase(SET_AREA_BUFFER_CMD) &&
                        player.hasPermission("sleepywar.builder")) {
                    setAreaBufferHandler(player, args);
                }
                // Test resource spawn
                else if (initialSubCommand.equalsIgnoreCase(TEST_RESOURCE_SPAWNER_CMD) &&
                        player.hasPermission("sleepywar.builder")) {
                    testActivateResourceSpawner(player);
                }
                // Test shop spawn
                else if (initialSubCommand.equalsIgnoreCase(SPAWN_SHOP_CMD) &&
                        player.hasPermission("sleepywar.builder")) {
                    spawnShop(player);
                }
                // Delete resource spawn
                else if (initialSubCommand.equalsIgnoreCase(REMOVE_RESOURCE_SPAWNER_CMD) &&
                        player.hasPermission("sleepywar.builder")) {
                    deleteResourceSpawner(player, args);
                }
                // Set duration spawn on specific codename resource spawner
                else if (initialSubCommand.equalsIgnoreCase(SET_RESOURCE_SPAWNER_FREQ_CMD) &&
                        player.hasPermission("sleepywar.builder")) {
                    setResourceSpawnerDuration(player, args);
                }
                // Set Team Spawner
                else if (initialSubCommand.equalsIgnoreCase(SET_TEAM_SPAWN_LOCATION_CMD) &&
                        player.hasPermission("sleepywar.builder")) {
                    setTeamSpawn(player, args);
                }
                // Save configuration, this automatically run when there's nobody in world
                else if (initialSubCommand.equalsIgnoreCase(SAVE_CMD) &&
                        player.hasPermission("sleepywar.builder")) {
                    saveWorldAndConfig(player);
                }
                // Teleport and Edit to bedwars world
                else if (initialSubCommand.equalsIgnoreCase(TP_EDIT_WORLD_CMD)) {
                    editWorld(player, args);
                }
                // Leave World
                else if (initialSubCommand.equalsIgnoreCase(TP_LEAVE_WORLD_CMD)) {
                    leaveWorldEdit(player);
                }
                // World resource spawner info
                else if (initialSubCommand.equalsIgnoreCase(RESOURCE_SPAWNER_INFO_CMD)) {
                    sendResourceSpawnerInfo(player);
                }
                // Game timeline event information
                else if (initialSubCommand.equalsIgnoreCase(TIMELINE_INFO_CMD)) {
                    sendTimelineInfo(player);
                }
                // World general info
                else if (initialSubCommand.equalsIgnoreCase(WORLD_INFO_CMD)) {
                    sendWorldInfo(player);
                }
            }
        } else {
            helpMessage(sender);
        }
        return true;
    }

    //#region

    // Page 1 Default help menu
    private void helpMessage(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "(Page 1/2) Sleeping World Maker Commands for World Building.");
        sender.sendMessage(ChatColor.AQUA + "List of sub-commands:");
        sender.sendMessage(ChatColor.GREEN + CREATE_WORLD_CMD + " => Create bedwars world.");
        sender.sendMessage(ChatColor.GREEN + TP_EDIT_WORLD_CMD + " => Teleport to bedwars world.");
        sender.sendMessage(ChatColor.GREEN + SET_QUEUE_SPAWN_CMD + " => Set room queue spawn.");
        sender.sendMessage(ChatColor.GREEN + SET_RESOURCE_SPAWNER_CMD + " => Set resource spawner.");
        sender.sendMessage(ChatColor.GREEN + SET_WORLD_SPAWN_CMD + " => Set world default spawn on your position.");
    }

    private void helpMessage(CommandSender sender, String page) {
        if (page.equals("2")) {
            sender.sendMessage(ChatColor.GOLD + "(Page 2/2) Sleeping World Maker Commands for World Building.");
            sender.sendMessage(ChatColor.AQUA + "List of sub-commands:");
            sender.sendMessage(ChatColor.GREEN + SET_BLOCK_ON_CMD + " => Set bedrock on air, just for safety.");
            sender.sendMessage(ChatColor.GREEN + SET_TEAM_SPAWN_LOCATION_CMD + " => Set specific team spawner on your current location.");
            sender.sendMessage(ChatColor.GREEN + REMOVE_RESOURCE_SPAWNER_CMD + " => Delete resource spawner.");
        } else { // default is page 1
            sender.sendMessage(ChatColor.GOLD + "(Page 1/2) Sleeping World Maker Commands for World Building.");
            sender.sendMessage(ChatColor.AQUA + "List of sub-commands:");
            sender.sendMessage(ChatColor.GREEN + CREATE_WORLD_CMD + " => Create bedwars world.");
            sender.sendMessage(ChatColor.GREEN + TP_EDIT_WORLD_CMD + " => Teleport to bedwars world.");
            sender.sendMessage(ChatColor.GREEN + SET_QUEUE_SPAWN_CMD + " => Set room queue spawn.");
            sender.sendMessage(ChatColor.GREEN + SET_RESOURCE_SPAWNER_CMD + " => Set resource spawner.");
            sender.sendMessage(ChatColor.GREEN + SET_WORLD_SPAWN_CMD + " => Set world default spawn on your position.");
        }
    }

    private void saveWorldAndConfig(Player player) {
        player.sendMessage(ChatColor.LIGHT_PURPLE + "World and game configuration has been saved.");
        systemConfig.Save();
        player.getWorld().save();
    }

    private void setWorldBorder(Player player, String[] args) {
        World inWorld = player.getWorld();
        if (systemConfig.getWorldNames().contains(inWorld.getName())) {
            Location playerLoc = player.getLocation();
            WorldBorder border = inWorld.getWorldBorder();
            int size = 1024;
            if (args.length >= 2) {
                if (!isPositiveNumber(args[1])) {
                    player.sendMessage(FAIL_COMMAND_USAGE + "Invalid input command. /sworld " +
                            SET_BORDER_CMD + " <size>");
                    return;
                }
                size = Integer.parseInt(args[1]);
            }
            border.setCenter(playerLoc);
            systemConfig.setBorderData(inWorld.getName(), size, false);
            player.sendMessage(PASS_COMMAND_USAGE + "World border set to size: " + size + " wide block(s)");
        } else {
            player.sendMessage(FAIL_COMMAND_USAGE + "You are not in Bedwars world building.");
        }
    }

    private void setWorldShrunkBorder(Player player, String[] args) {
        String inWorldName = player.getWorld().getName();
        if (systemConfig.getWorldNames().contains(inWorldName)) {
            if (args.length >= 2) {
                if (isPositiveNumber(args[1])) {
                    systemConfig.setBorderData(inWorldName, Integer.parseInt(args[1]), true);
                    player.sendMessage(PASS_COMMAND_USAGE + "World shrunk border set size to: " +
                            args[1] + " wide block(s)");;
                    return;
                }
                return;
            }
            player.sendMessage(FAIL_COMMAND_USAGE + "Invalid input command. /sworld " +
                    SET_SHRUNK_BORDER_CMD + " <size>");
        } else {
            player.sendMessage(FAIL_COMMAND_USAGE + "You are not in Bedwars world building.");
        }
    }

    private void addEventCommand(Player player, String[] args) {
        if (args.length < 4) {
            player.sendMessage(ChatColor.GOLD + "You can add one of these event by typing:");
            player.sendMessage(ChatColor.LIGHT_PURPLE + "/sworld " + ADD_EVENT_CMD +
                    " <type> <duration-to-trigger>, List of options:");
            player.sendMessage(ChatColor.AQUA + TimelineEventType.DIAMOND_UPGRADE.toString());
            player.sendMessage(ChatColor.GREEN + TimelineEventType.EMERALD_UPGRADE.toString());
            player.sendMessage(ChatColor.YELLOW + TimelineEventType.DESTROY_ALL_BED.toString());
            player.sendMessage(ChatColor.RED + TimelineEventType.WORLD_SHRINKING.toString());
        } else {
            String worldName = player.getWorld().getName();
            if (systemConfig.getWorldNames().contains(worldName)) {
                if (systemConfig.countEventsInTimeline(worldName) < 10) {
                    TimelineEventType typeEvent = TimelineEventType.fromString(args[1]);
                    if (typeEvent != null && isPositiveNumber(args[2])) {
                        int dur = Integer.parseInt(args[2]);
                        BedwarsTimelineEvent nEvent = new BedwarsTimelineEvent(typeEvent,
                                (float) dur, args[3], 0, typeEvent.toString() + " Event");
                        systemConfig.addEventTimeline(worldName, nEvent);
                        player.sendMessage(ChatColor.GREEN + args[3] + " added to world event.");
                        return;
                    }
                    player.sendMessage(FAIL_COMMAND_USAGE + "Invalid Arguments.");
                } else {
                    player.sendMessage(WARN_COMMAND_USAGE + "Amount of events reach it's limit, " +
                            "delete one of the event by using /sworld " + REMOVE_EVENT_CMD + " <number>");
                }
            } else {
                player.sendMessage(FAIL_COMMAND_USAGE + "You are not in Bedwars world building.");
            }
        }
    }

    private void deleteEventCommand(Player player, String[] args) {
        String worldName = player.getWorld().getName();
        if (args.length < 2) {
            if (systemConfig.getWorldNames().contains(worldName)) {
                StringBuilder listDescription = new StringBuilder(ChatColor.GRAY + "");
                List<BedwarsTimelineEvent> timelineEvents = systemConfig.getTimelineEvents(worldName);
                for (int i = 0; i < timelineEvents.size(); i++) {
                    BedwarsTimelineEvent thisEvent = timelineEvents.get(i);
                    if (i == timelineEvents.size() - 1)
                        listDescription.append(String.format("%d. %s (trigger in %.1f second(s))", i + 1,
                                thisEvent.getEventType(), thisEvent.getSecTrigger()));
                    else
                        listDescription.append(String.format("%d. %s (trigger in %.1f second(s))\n", i + 1,
                                thisEvent.getEventType(), thisEvent.getSecTrigger()));
                }
                player.sendMessage(ChatColor.GOLD + "Delete one of this timeline with /sworld " +
                        REMOVE_EVENT_CMD + " <name-of-event>\n" + listDescription);
            } else {
                player.sendMessage(FAIL_COMMAND_USAGE + "You are not in Bedwars world.");
            }
        } else {
            if (systemConfig.getWorldNames().contains(worldName)) {
                if (systemConfig.deleteTimelineEvent(worldName, args[1])) {
                    player.sendMessage(PASS_COMMAND_USAGE + "Successfully deleted '" + args[1] + "' event.");
                } else {
                    player.sendMessage(FAIL_COMMAND_USAGE + "Event has not been registered.");
                }
            } else {
                player.sendMessage(FAIL_COMMAND_USAGE + "You are not in Bedwars world.");
            }
        }
    }

    private void setEventMSGCommand(Player player, String[] args) {
        // TODO: set event message
    }

    private void setEventOrderCommand(Player player, String[] args) {
        String inWorldName = player.getWorld().getName();
        if (systemConfig.getWorldNames().contains(inWorldName)) {
            if (args.length >= 3) {
                if (isPositiveNumber(args[2])) {
                    if (systemConfig.setEventOrder(inWorldName, args[1], Integer.parseInt(args[2]))) {
                        player.sendMessage(PASS_COMMAND_USAGE + "Event '" + args[1] + "' placed on " + args[2]);
                        return;
                    }
                }
                player.sendMessage(FAIL_COMMAND_USAGE + "Event may not be exists or invalid insert type.");
                return;
            }
            player.sendMessage(FAIL_COMMAND_USAGE + "Invalid Argument. /sworld " + SET_EVENT_ORDER_CMD +
                    " <eventname> <num-order>");
            return;
        }
        player.sendMessage(FAIL_COMMAND_USAGE + "You are not in Bedwars world building.");
    }

    private void addNewTeam(Player player, String[] args) {
        // TODO: Add Team command
    }

    private void deleteTeam(Player player, String[] args) {
        // TODO: Delete Team Command
    }

    private void setColorPrefix(Player player, String[] args) {
        String inWorldName = player.getWorld().getName();
        PlayerBedwarsBuilderEntity playerBE = customBuilderEntity.get(player);
        if (systemConfig.getWorldNames().contains(inWorldName) && playerBE != null) {
            if (args.length < 3) {
                player.sendMessage(FAIL_COMMAND_USAGE + "Invalid Argument, do /sworld " + SET_TEAM_RAW_COLOR_CMD +
                        " <color>");
            } else {
                if (systemConfig.getTeamNames(inWorldName).contains(args[1])) {
                    systemConfig.setRawColor(inWorldName, args[1], args[2]);
                    player.sendMessage(PASS_COMMAND_USAGE + "Color set on team " + args[1] + " to '" +
                            args[2] + "'");
                    return;
                }
                player.sendMessage(FAIL_COMMAND_USAGE + "Invalid Input, team may not available.");
            }
        } else {
            player.sendMessage(FAIL_COMMAND_USAGE + "You are not in Bedwars world building.");
        }
    }

    private void setLockedEntity(Player player, String[] args) {
        String inWorldName = player.getWorld().getName();
        PlayerBedwarsBuilderEntity playerBE = customBuilderEntity.get(player);
        if (systemConfig.getWorldNames().contains(inWorldName) && playerBE != null) {
            if (args.length >= 2) {
                if (systemConfig.getLockedCodename(inWorldName).contains(args[1])) {
                    if (args.length == 2) {
                        player.sendMessage(ChatColor.YELLOW + "[Failed] Locked entity already exists.");
                        return;
                    }
                    if (systemConfig.setLockedRequest(inWorldName, args[1], args[2], null))
                        player.sendMessage(PASS_COMMAND_USAGE + "Added locked resource spawner.");
                    else
                        player.sendMessage(FAIL_COMMAND_USAGE + "Resource spawner may not exists.");
                    return;
                }
                playerBE.setTeamChoice("PUBLIC");
                playerBE.addCodenameHolder(args[1]);
                if (args.length >= 3)
                    playerBE.addCodenameHolder(args[2]);
                player.sendMessage(ChatColor.GREEN + "Place any type of door, it will automatically" +
                        " locked when inside the gameplay.");
                return;
            }
            player.sendMessage(FAIL_COMMAND_USAGE + "Invalid Argument, do /sworld " + SET_LOCKED_ENTITY_CMD +
                    " <codename> [PUBLIC-rs-codename]");
        } else {
            player.sendMessage(FAIL_COMMAND_USAGE + "You are not in Bedwars world building.");
        }
    }

    private void addRequestLockedEntity(Player player, String[] args) {
        String inWorldName = player.getWorld().getName();
        if (systemConfig.getWorldNames().contains(inWorldName)) {
            if (args.length < 4) {
                player.sendMessage(ChatColor.YELLOW + "Invalid Input, /sworld " + ADD_LOCKED_REQUEST_CMD +
                        " <codename> <resource-type> <amount>");
            } else {
                ResourcesType typeReq = ResourcesType.fromString(args[2]);
                if (systemConfig.getLockedCodename(inWorldName).contains(args[1]) && typeReq != null &&
                    isPositiveNumber(args[3])) {
                    if (systemConfig.addLockedRequest(inWorldName, args[1], typeReq, Integer.parseInt(args[3]))) {
                        player.sendMessage(String.format("%sSuccessfully added required to unlock %s in game.",
                                ChatColor.AQUA + "", args[1]));
                        return;
                    }
                }
                player.sendMessage(ChatColor.RED + "Locked entity codename or type request may not exists.");
            }
        } else {
            player.sendMessage(ChatColor.YELLOW + "You are not in Bedwars world building.");
        }
    }

    private void deleteLockedEntity(Player player, String[] args) {
        String inWorldName = player.getWorld().getName();
        if (systemConfig.getWorldNames().contains(inWorldName)) {
            if (args.length < 2) {
                player.sendMessage(ChatColor.YELLOW + "Invalid Input, /sworld " + REMOVE_LOCKED_ENTITY_CMD +
                        " <codename>");
            } else {
                if (systemConfig.getLockedCodename(inWorldName).contains(args[1])) {
                    if (systemConfig.deleteLockedKey(inWorldName, args[1])) {
                        player.sendMessage(String.format("%sSuccessfully deleted locked entity with codename %s.",
                                ChatColor.AQUA + "", args[1]));
                        return;
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
                player.sendMessage(ChatColor.GREEN + "You need to insert team name. " + ChatColor.YELLOW +
                        "/sworld " + SET_BED_LOCATION_CMD + " <teamname>\n");
                player.sendMessage(ChatColor.GREEN + "All beds that has been set:");
                StringBuilder description = new StringBuilder();
                for (String tn : systemConfig.getTeamNames(worldName)) {
                    Location bedLoc = systemConfig.getBedLocation(player.getWorld(), worldName, tn);
                    String teamColorPrefix = systemConfig.getRawColor(worldName, tn);
                    if (PluginStaticFunc.isMaterialBed(bedLoc.getBlock().getType()))
                        description.append(String.format("%s: %s%b; ", PluginStaticColor
                                .getColorString(teamColorPrefix) + tn, ChatColor.GREEN + "", true));
                    else
                        description.append(String.format("%s: %s%b; ", PluginStaticColor
                                .getColorString(teamColorPrefix) + tn, ChatColor.RED + "", false));
                }
                player.sendMessage(description.toString());
            } else {
                if (systemConfig.getTeamNames(worldName).contains(args[1])) {
                    customBuilderEntity.get(player).setTeamChoice(args[1]);
                    player.sendMessage(ChatColor.GREEN + "Holding team '" + args[1] +
                            "', now place any kinds of bed.");
                } else {
                    player.sendMessage(FAIL_COMMAND_USAGE + "Team not exists.");
                }
            }
        } else {
            player.sendMessage(FAIL_COMMAND_USAGE + "You are not in Bedwars world building.");
        }
    }

    private void setQueueSpawn(Player player) {
        String worldName = player.getWorld().getName();
        Location playerPos = player.getLocation();
        if (systemConfig.setQueuePos(worldName, playerPos)) {
            player.sendMessage(ChatColor.LIGHT_PURPLE + "Queue spawn settled on " + ChatColor.AQUA + String
                    .format("(X/Y/Z): %d/%d/%d", playerPos.getBlockX(), playerPos.getBlockY(), playerPos.getBlockZ()));
        } else {
            player.sendMessage(ChatColor.RED + "You are not in Bedwars world building.");
        }
    }

    private void editWorld(Player player, String[] args) {
        if (args.length >= 2) {
            // Check if currently map is being used to play
            if (gameManager.getPlayingMaps().containsKey(args[1])) {
                player.sendMessage(ChatColor.BLUE + "Cannot edit while the map is currently being played.");
                return;
            }
            World w = Bukkit.getServer().getWorld(args[1]);
            if (w != null) {
                if (systemConfig.getWorldNames().contains(w.getName())) {
                    player.sendMessage(ChatColor.LIGHT_PURPLE + "Teleporting to world '" + args[1] + "'!");
                    player.teleport(w.getSpawnLocation());
                } else {
                    player.sendMessage(ChatColor.RED + "Unrecognised world. this world is not registered by plugin.");
                }
            } else {
                player.sendMessage(ChatColor.RED + "World is not exists, create your Bedwars world using" +
                        ChatColor.GOLD + "/sworld create");
            }
            return;
        }
        player.sendMessage(FAIL_COMMAND_USAGE + "You need to insert a world name. " + ChatColor.YELLOW +
                "/sworld " + TP_EDIT_WORLD_CMD + " <worldname>");
    }

    private void leaveWorldEdit(Player player) {
        World bedwarsWorld = player.getWorld();
        if (customBuilderEntity.containsKey(player)) {
            // Teleport back to default world
            customBuilderEntity.get(player).returnEntity();
            if (bedwarsWorld.getPlayers().size() == 0 && allResourceSpawners.containsKey(bedwarsWorld.getName())) {
                // Remove temporary stuff in builder world
                List<ResourceSpawner> listRS = allResourceSpawners.remove(bedwarsWorld.getName());
                for (ResourceSpawner rs : listRS)
                    if (rs.isRunning())
                        rs.setRunning(false);
            }
        } else if (!customBuilderEntity.containsKey(player) && systemConfig
                .getWorldNames().contains(bedwarsWorld.getName())) {
            player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
        } else {
            player.sendMessage(ChatColor.YELLOW + "You are not in builder world.");
        }
    }

    private void setShopLocation(Player player, String[] args) {
        String inWorldName = player.getWorld().getName();
        if (systemConfig.getWorldNames().contains(inWorldName)) {
            if (args.length < 2) {
                player.sendMessage(ChatColor.GREEN + "You need to insert type of shop. " + ChatColor.YELLOW +
                        "/sworld " + SET_SHOP_SPAWN_LOCATION_CMD + " <type>");
            } else {
                BedwarsShopType pickedShopType = BedwarsShopType.fromString(args[1]);
                if (pickedShopType != null) {
                    if (systemConfig.addShopLocationSpawn(inWorldName, pickedShopType, player.getLocation())) {
                        player.sendMessage(ChatColor.GREEN + pickedShopType.toString() +
                                " settled on your location where you are standing.");
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
                player.sendMessage(ChatColor.GREEN + "You need to insert the index number. " + ChatColor.YELLOW +
                        "/sworld " + REMOVE_SHOP_SPAWN_LOCATION_CMD + " <index>\n");
                StringBuilder description = new StringBuilder(ChatColor.LIGHT_PURPLE + "List of shop spawn:\n");
                for (Map.Entry<BedwarsShopType, List<Location>> shopLocTypeEntry : shopLocMap.entrySet()) {
                    // Set types by colors
                    if (shopLocTypeEntry.getKey() == BedwarsShopType.ITEMS_SHOP)
                        description.append(ChatColor.YELLOW).append(": ");
                    else if (shopLocTypeEntry.getKey() == BedwarsShopType.PERMA_SHOP)
                        description.append(ChatColor.AQUA).append(": ");
                    else
                        description.append(ChatColor.WHITE).append(": ");
                    // Get all shop location information
                    for (int i = 0; i < shopLocTypeEntry.getValue().size(); i++) {
                        Location loc = shopLocTypeEntry.getValue().get(i);
                        description.append(String.format("[%d. X:%d Y:%d Z:%d]; ", i + 1, loc.getBlockX(),
                                loc.getBlockY(), loc.getBlockZ()));
                    }
                }
                player.sendMessage(description.toString());
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
            return;
        }
        player.sendMessage(ChatColor.YELLOW + "You are not in Bedwars world building.");
    }

    private void setTeamSpawn(Player player, String[] args) {
        String inWorldName = player.getWorld().getName();
        if (systemConfig.getWorldNames().contains(inWorldName)) {
            if (args.length >= 2) {
                Location loc = player.getLocation();
                if (systemConfig.setTeamSpawner(inWorldName, args[1], loc)) {
                    player.sendMessage(String.format("%sSpawn set for team \"%s\" at (X/Y/Z): %d/%d/%d",
                            ChatColor.AQUA + "", args[1], loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
                    return;
                }
                player.sendMessage(ChatColor.DARK_PURPLE + "Invalid input or team may not be exists");
                return;
            }
            player.sendMessage(ChatColor.GREEN + "You need to insert team name. " +
                    ChatColor.YELLOW + "/sworld " + SET_TEAM_SPAWN_LOCATION_CMD + " <teamname>");
            return;
        }
        player.sendMessage(ChatColor.YELLOW + "You are not in Bedwars world building.");
    }

    private void setBlockOnWorld(Player player, String[] args) {
        String worldName = player.getWorld().getName();
        if (systemConfig.getWorldNames().contains(worldName)) {
            if (args.length >= 4) {
                if (isPositiveNumber(args[1]) && isPositiveNumber(args[2]) && isPositiveNumber(args[3])) {
                    World w = Bukkit.getWorld(worldName);
                    int x = Integer.parseInt(args[1]);
                    int y = Integer.parseInt(args[2]);
                    int z = Integer.parseInt(args[3]);
                    w.getBlockAt(x, y, z).setType(Material.BEDROCK);
                    return;
                }
                player.sendMessage(FAIL_COMMAND_USAGE + "Invalid Argument, input coordinate must be number");
                return;
            }
            player.sendMessage(FAIL_COMMAND_USAGE + "Invalid Argument, do /sworld " + SET_BLOCK_ON_CMD + " <X> <Y> <Z>");
            return;
        }
        player.sendMessage(FAIL_COMMAND_USAGE + "You are not in Bedwars world building.");
    }

    private void setAreaBufferHandler(Player player, String[] args) {
        String inWorldName = player.getWorld().getName();
        PlayerBedwarsBuilderEntity playerBE = customBuilderEntity.get(player);
        if (systemConfig.getWorldNames().contains(inWorldName) && playerBE != null) {
            if (args.length >= 2) {
                if (!systemConfig.getTeamNames(inWorldName).contains(args[1]))
                    args[1] = "PUBLIC";
                playerBE.setTeamChoice(args[1]);
                playerBE.setRequiredAmountLoc(2);
                player.sendMessage(PASS_COMMAND_USAGE + "Place 2 block to create a box of area buffer.");
                return;
            }
            player.sendMessage(FAIL_COMMAND_USAGE + "Invalid Argument, try again with /sworld " +
                    SET_AREA_BUFFER_CMD + " <teamname|PUBLIC>");
            return;
        }
        player.sendMessage(FAIL_COMMAND_USAGE + "You are not in Bedwars world building.");
    }

    private void testActivateResourceSpawner(Player player) {
        String worldName = player.getWorld().getName();
        if (systemConfig.getWorldNames().contains(worldName)) {
            // Check if there's already contains the tester
            if (allResourceSpawners.containsKey(worldName)) {
                // Deactivate and delete a list
                List<ResourceSpawner> listRS = allResourceSpawners.remove(worldName);
                for (ResourceSpawner rSpawner : listRS)
                    rSpawner.setRunning(false);
                listRS.clear();
                player.sendMessage(ChatColor.BLUE + "World is stopping it's spawner.");
            } else {
                // Check if no resource spawners in world
                if (systemConfig.countOverallRS(worldName) == 0) {
                    player.sendMessage(ChatColor.YELLOW + "There are no resource spawner exists.");
                    return;
                }
                // Put list and activate it
                List<ResourceSpawner> spawners = systemConfig.getWorldRS(player.getWorld(), worldName);
                for (ResourceSpawner rSpawner : spawners)
                    rSpawner.setRunning(true);
                allResourceSpawners.put(worldName, spawners);
                player.sendMessage(ChatColor.BLUE + "World is testing it's spawner.");
            }
            return;
        }
        player.sendMessage(ChatColor.RED + "World is not registered as Bedwars world.");
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
                        LivingEntity entLive = (LivingEntity) ent;
                        entLive.addPotionEffect(slowEffect);
                    }
                    if (shopLocEntry.getKey() == BedwarsShopType.PERMA_SHOP)
                        ent.setCustomName(BedwarsUpgradeMenu.VILLAGER_NAME_TAG);
                    else
                        ent.setCustomName(BedwarsShopMenu.VILLAGER_NAME_TAG);
                    ent.setCustomNameVisible(true);
                }
            }
            player.sendMessage(PASS_COMMAND_USAGE + "All shop spawned.");
            return;
        }
        player.sendMessage(FAIL_COMMAND_USAGE + "You are not in Bedwars world building.");
    }

    private void deleteResourceSpawner(Player player, String[] args) {
        String availableWorldName = player.getWorld().getName();
        if (systemConfig.getWorldNames().contains(availableWorldName)) {
            if (args.length >= 3) {
                if (systemConfig.deleteResourceSpawner(availableWorldName, args[1], args[2])) {
                    player.sendMessage(ChatColor.GREEN + "Resource spawner successfully deleted.");
                    return;
                }
                player.sendMessage(ChatColor.RED + "Resource spawner may be not exists or failed.");
                return;
            }
            player.sendMessage(ChatColor.GREEN + "You need to insert an existing spawner name. " +
                    ChatColor.YELLOW + "/sworld delrspawn <teamname|PUBLIC> <codename>");
            return;
        }
        player.sendMessage(ChatColor.YELLOW + "You are not in Bedwars world building.");
    }

    private void setResourceSpawnerDuration(Player player, String[] args) {
        String inWorldName = player.getWorld().getName();
        if (systemConfig.getWorldNames().contains(inWorldName)) {
            if (args.length < 4) {
                player.sendMessage(ChatColor.GREEN + "You need to insert an existing spawner name. " +
                        ChatColor.YELLOW + "/sworld " + SET_RESOURCE_SPAWNER_FREQ_CMD + " <teamname|PUBLIC> <codename>" +
                        " <duration-per-spawn>");
            } else {
                if (isPositiveNumber(args[3])) {
                    int dur = Integer.parseInt(args[3]);
                    // If team name not exists then consider as public
                    if (!systemConfig.getTeamNames(inWorldName).contains(args[1]))
                        args[1] = "PUBLIC";
                    if (systemConfig.setRSDuration(inWorldName, args[1], args[2], dur)) {
                        player.sendMessage(String.format("%s spawner duration set to %d sec(s)",
                                ChatColor.GREEN + args[2], dur));
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
        String inWorldName = player.getWorld().getName();
        if (systemConfig.getWorldNames().contains(inWorldName)) {
            if (args.length >= 4) {
                Location loc = player.getLocation();
                ResourcesType rst = ResourcesType.fromString(args[2]);
                if (rst == null) {
                    player.sendMessage(ChatColor.RED + "Invalid resource type.");
                } else {
                    if (systemConfig.getTeamNames(inWorldName).contains(args[1])) {
                        ResourceSpawner newRSpawner = new ResourceSpawner(args[3], loc, rst);
                        systemConfig.addResourceSpawner(inWorldName, args[1], newRSpawner);
                        player.sendMessage(ChatColor.GREEN + "Added " + rst.toString() +
                                " spawner with name \"" + args[3] + "\" for team " + args[1]);
                    } else {
                        ResourceSpawner newRSpawner = new ResourceSpawner(args[3], loc, rst);
                        systemConfig.addResourceSpawner(inWorldName, "PUBLIC", newRSpawner);
                        player.sendMessage(ChatColor.GREEN + "Added public " + rst.toString() +
                                " spawner with name \"" + args[3]);
                    }
                }
                return;
            }
            player.sendMessage(ChatColor.DARK_PURPLE + "Invalid Input.");
            return;
        }
        player.sendMessage(FAIL_COMMAND_USAGE + "You are not in Bedwars world building.");
    }

    private void sendResourceSpawnerInfo(Player player) {
        String worldName = player.getWorld().getName();
        if (systemConfig.getWorldNames().contains(worldName)) {
            int amountRS = systemConfig.countOverallRS(worldName);
            player.sendMessage(String.format("%s~ Resource Spawners in %s ~", ChatColor.GREEN + "", worldName));
            player.sendMessage(String.format("%s%d %sResource Spawner(s): [codename; second/spawn]",
                    ChatColor.LIGHT_PURPLE + "", amountRS, ChatColor.AQUA + ""));
            if (amountRS == 0) {
                player.sendMessage(ChatColor.ITALIC + "EMPTY...");
                return;
            }
            StringBuilder description = new StringBuilder();
            for (ResourceSpawner rSpawner : systemConfig.getWorldRS(player.getWorld(), worldName)) {
                String colorChatStr;
                switch (rSpawner.getTypeSpawner()) {
                    case IRON:
                        colorChatStr = ChatColor.GRAY + "";
                        break;
                    case GOLD:
                        colorChatStr = ChatColor.GOLD + "";
                        break;
                    case DIAMOND:
                        colorChatStr = ChatColor.AQUA + "";
                        break;
                    case EMERALD:
                        colorChatStr = ChatColor.GREEN + "";
                        break;
                    default:
                        colorChatStr = ChatColor.WHITE + "";
                        break;
                }
                description.append(colorChatStr).append(String.format("[%s; %.1f] ", rSpawner.getCodename(),
                        rSpawner.getSecondsPerSpawn()));
            }
            player.sendMessage(description.toString());
            return;
        }
        player.sendMessage(FAIL_COMMAND_USAGE + "You are not in Bedwars World Building.");
    }

    private void sendTimelineInfo(Player player) {
        String inWorldName = player.getWorld().getName();
        if (systemConfig.getWorldNames().contains(inWorldName)) {
            int amountEvents = systemConfig.countEventsInTimeline(inWorldName);
            player.sendMessage(String.format("%s~ Timeline Events in %s ~", ChatColor.GREEN + "", inWorldName));
            player.sendMessage(String.format("%s%d %sEvent(s) in Timeline: [name; trigger-in; type]",
                    ChatColor.LIGHT_PURPLE + "", amountEvents, ChatColor.AQUA + ""));
            if (amountEvents == 0) {
                player.sendMessage(ChatColor.ITALIC + "EMPTY...");
                return;
            }
            StringBuilder description = new StringBuilder();
            for (BedwarsTimelineEvent ev : systemConfig.getTimelineEvents(inWorldName)) {
                String colorChatStr;
                switch (ev.getEventType()) {
                    case DIAMOND_UPGRADE:
                        colorChatStr = ChatColor.AQUA + "";
                        break;
                    case EMERALD_UPGRADE:
                        colorChatStr = ChatColor.GREEN + "";
                        break;
                    case DESTROY_ALL_BED:
                        colorChatStr = ChatColor.YELLOW + "";
                        break;
                    case BUFFER_ZONE_ACTIVE:
                        colorChatStr = ChatColor.GOLD + "";
                        break;
                    case WORLD_SHRINKING:
                        colorChatStr = ChatColor.RED + "";
                        break;
                    default:
                        colorChatStr = ChatColor.WHITE + "";
                        break;
                }
                description.append(colorChatStr).append(String.format("[%s; %.1f; %s] ", ev.getName(), ev.getSecTrigger(),
                        ev.getEventType().toString()));
            }
            player.sendMessage(description.toString());
            return;
        }
        player.sendMessage(ChatColor.RED + "You are not in Bedwars World Building.");
    }

    private void sendWorldInfo(Player player) {
        String worldName = player.getWorld().getName();
        if (systemConfig.getWorldNames().contains(worldName)) {
            List<String> teamNames = systemConfig.getTeamNames(worldName);
            player.sendMessage(String.format("%s~ ['%s' Info] ~", ChatColor.GREEN + "", worldName));
            player.sendMessage(String.format("%sThere are %s%d %steams:", ChatColor.AQUA + "",
                    ChatColor.LIGHT_PURPLE + "", teamNames.size(), ChatColor.AQUA + ""));
            StringBuilder description = new StringBuilder();
            for (String tn : teamNames) {
                String colorPref = systemConfig.getRawColor(worldName, tn);
                if (colorPref != null)
                    description.append(PluginStaticColor.getColorString(colorPref))
                            .append(tn).append(ChatColor.WHITE).append("; ");
                else
                    description.append(PluginStaticColor.getColorString("white"))
                            .append(tn).append(ChatColor.WHITE).append("; ");
            }
            player.sendMessage(description.toString());
            Location queueLoc = systemConfig.getQueuePos(player.getWorld(), worldName);
            player.sendMessage(String.format("Queue Location on (X/Y/Z): %d/%d/%d; ",
                    queueLoc.getBlockX(), queueLoc.getBlockY(), queueLoc.getBlockZ()));
            player.sendMessage(String.format("%sEvents in timeline count: %s%d; ", ChatColor.AQUA + "",
                    ChatColor.LIGHT_PURPLE + "", systemConfig.countEventsInTimeline(worldName)));
            player.sendMessage(String.format("%sThere are %s%d %sResource Spawner(s)", ChatColor.AQUA + "",
                    ChatColor.LIGHT_PURPLE + "", systemConfig.countOverallRS(worldName), ChatColor.AQUA + ""));
            return;
        }
        player.sendMessage(ChatColor.RED + "You are not in Bedwars World Building.");
    }

    private void createWorld(CommandSender sender, String[] args) {
        if (args.length >= 2) {
            // Creating the world
            if (SleepingWarsPlugin.getPlugin().getServer().getWorld(args[1]) == null) {
                sender.sendMessage(ChatColor.BLUE + "Creating World...");
                WorldCreator creator = new WorldCreator(args[1]).environment(Environment.NORMAL).type(WorldType.FLAT)
                        .hardcore(false).generateStructures(false);
                creator.generator(new VoidGenerator());
                systemConfig.saveWorldConfig(creator);
                sender.sendMessage(PASS_COMMAND_USAGE + "World \"" + args[1] + "\" has been Created.");
                return;
            }
            sender.sendMessage(ChatColor.BLUE + "World \"" + args[1] + "\" already exists. Aborted!!!");
        }
        sender.sendMessage(ChatColor.GREEN + "You need to insert a world name. " +
                ChatColor.YELLOW + "/sworld " + CREATE_WORLD_CMD + " <worldname>");
    }

    private boolean isPositiveNumber(String numStr) {
        int dotCount = 0;
        for (int i = 0; i < numStr.length(); i++) {
            switch (numStr.charAt(i)) {
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
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
