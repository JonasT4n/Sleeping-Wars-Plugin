package com.joenastan.sleepingwars.commands.TabCompletor;

import com.joenastan.sleepingwars.commands.WorldMakerCommand;
import com.joenastan.sleepingwars.enumtypes.BedwarsShopType;
import com.joenastan.sleepingwars.enumtypes.ResourcesType;
import com.joenastan.sleepingwars.SleepingWarsPlugin;
import com.joenastan.sleepingwars.enumtypes.TimelineEventType;
import com.joenastan.sleepingwars.utility.DataFiles.GameSystemConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SWorldCommands implements TabCompleter {

    private static final GameSystemConfig systemConf = SleepingWarsPlugin.getGameSystemConfig();

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command,
                                      @Nonnull String alias, @Nonnull String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender);
            if (args.length == 1) {
                List<String> sWorldSubs = new ArrayList<>();
                sWorldSubs.add(WorldMakerCommand.ADD_EVENT_CMD);
                sWorldSubs.add(WorldMakerCommand.SET_AREA_BUFFER_CMD);
                sWorldSubs.add(WorldMakerCommand.SET_AREA_OPPOSITION_CMD);
                sWorldSubs.add(WorldMakerCommand.SET_AREA_SINGLE_FX_CMD);
                sWorldSubs.add(WorldMakerCommand.ADD_AREA_BUFFER_FX_CMD);
                sWorldSubs.add(WorldMakerCommand.ADD_TEAM_CMD);
                sWorldSubs.add(WorldMakerCommand.ADD_LOCKED_REQUEST_CMD);
                sWorldSubs.add(WorldMakerCommand.CREATE_WORLD_CMD);
                sWorldSubs.add(WorldMakerCommand.REMOVE_EVENT_CMD);
                sWorldSubs.add(WorldMakerCommand.REMOVE_RESOURCE_SPAWNER_CMD);
                sWorldSubs.add(WorldMakerCommand.REMOVE_SHOP_SPAWN_LOCATION_CMD);
                sWorldSubs.add(WorldMakerCommand.REMOVE_TEAM_CMD);
                sWorldSubs.add(WorldMakerCommand.REMOVE_LOCKED_ENTITY_CMD);
                sWorldSubs.add(WorldMakerCommand.TP_EDIT_WORLD_CMD);
                sWorldSubs.add(WorldMakerCommand.COMMAND_HELP_CMD);
                sWorldSubs.add(WorldMakerCommand.TP_LEAVE_WORLD_CMD);
                sWorldSubs.add(WorldMakerCommand.SAVE_CMD);
                sWorldSubs.add(WorldMakerCommand.SET_BORDER_CMD);
                sWorldSubs.add(WorldMakerCommand.SET_SHRUNK_BORDER_CMD);
                sWorldSubs.add(WorldMakerCommand.SET_LOCKED_ENTITY_CMD);
                sWorldSubs.add(WorldMakerCommand.SET_EVENT_MESSAGE_CMD);
                sWorldSubs.add(WorldMakerCommand.SET_EVENT_ORDER_CMD);
                sWorldSubs.add(WorldMakerCommand.SET_BED_LOCATION_CMD);
                sWorldSubs.add(WorldMakerCommand.SET_BLOCK_ON_CMD);
                sWorldSubs.add(WorldMakerCommand.SET_SHOP_SPAWN_LOCATION_CMD);
                sWorldSubs.add(WorldMakerCommand.SET_QUEUE_SPAWN_CMD);
                sWorldSubs.add(WorldMakerCommand.SET_WORLD_SPAWN_CMD);
                sWorldSubs.add(WorldMakerCommand.SET_TEAM_RAW_COLOR_CMD);
                sWorldSubs.add(WorldMakerCommand.SPAWN_SHOP_CMD);
                sWorldSubs.add(WorldMakerCommand.SET_RESOURCE_SPAWNER_FREQ_CMD);
                sWorldSubs.add(WorldMakerCommand.SET_RESOURCE_SPAWNER_CMD);
                sWorldSubs.add(WorldMakerCommand.SET_TEAM_SPAWN_LOCATION_CMD);
                sWorldSubs.add(WorldMakerCommand.TEST_RESOURCE_SPAWNER_CMD);
                sWorldSubs.add(WorldMakerCommand.RESOURCE_SPAWNER_INFO_CMD);
                sWorldSubs.add(WorldMakerCommand.TEAM_INFO_CMD);
                sWorldSubs.add(WorldMakerCommand.TIMELINE_INFO_CMD);
                sWorldSubs.add(WorldMakerCommand.WORLD_INFO_CMD);
                return sWorldSubs;
            } else if (args.length == 2) {
                // Gives world name hint
                if (args[0].equalsIgnoreCase("edit")) {
                    return systemConf.getWorldNames();
                }
                // Gives coordinate X hint
                else if (args[0].equalsIgnoreCase(WorldMakerCommand.SET_BLOCK_ON_CMD)) {
                    List<String> setBlockHint = new ArrayList<>();
                    setBlockHint.add("X");
                    return setBlockHint;
                }
                // Hint for world name
                else if (args[0].equalsIgnoreCase(WorldMakerCommand.CREATE_WORLD_CMD)) {
                    List<String> createWorldHint = new ArrayList<>();
                    createWorldHint.add("<world|map-name>");
                    return createWorldHint;
                }
                // Gives team names hint
                else if (args[0].equalsIgnoreCase(WorldMakerCommand.SET_TEAM_SPAWN_LOCATION_CMD) ||
                        args[0].equalsIgnoreCase(WorldMakerCommand.TEAM_INFO_CMD) ||
                        args[0].equalsIgnoreCase(WorldMakerCommand.SET_BED_LOCATION_CMD) ||
                        args[0].equalsIgnoreCase(WorldMakerCommand.SET_TEAM_RAW_COLOR_CMD) ||
                        args[0].equalsIgnoreCase(WorldMakerCommand.REMOVE_TEAM_CMD)) {
                    String worldName = player.getWorld().getName();
                    if (systemConf.getWorldNames().contains(worldName))
                        return systemConf.getTeamNames(worldName);
                }
                // Gives team names hint and public
                else if (args[0].equalsIgnoreCase(WorldMakerCommand.SET_RESOURCE_SPAWNER_CMD) ||
                        args[0].equalsIgnoreCase(WorldMakerCommand.REMOVE_RESOURCE_SPAWNER_CMD) ||
                        args[0].equalsIgnoreCase(WorldMakerCommand.SET_RESOURCE_SPAWNER_FREQ_CMD) ||
                        args[0].equalsIgnoreCase(WorldMakerCommand.SET_AREA_BUFFER_CMD)) {
                    String worldName = player.getWorld().getName();
                    if (systemConf.getWorldNames().contains(worldName)) {
                        List<String> teamNameHint = new ArrayList<>(systemConf.getTeamNames(worldName));
                        teamNameHint.add("PUBLIC");
                        return teamNameHint;
                    }
                }
                // Gives add event hint
                else if (args[0].equalsIgnoreCase(WorldMakerCommand.ADD_EVENT_CMD)) {
                    List<String> eventType = new ArrayList<>();
                    eventType.add(TimelineEventType.EMERALD_UPGRADE.toString());
                    eventType.add(TimelineEventType.DIAMOND_UPGRADE.toString());
                    eventType.add(TimelineEventType.DESTROY_ALL_BED.toString());
                    eventType.add(TimelineEventType.WORLD_SHRINKING.toString());
                    return eventType;
                }
                // Gives delete event hint
                else if (args[0].equalsIgnoreCase(WorldMakerCommand.REMOVE_EVENT_CMD) ||
                        args[0].equalsIgnoreCase(WorldMakerCommand.SET_EVENT_ORDER_CMD)) {
                    String inWorldName = player.getWorld().getName();
                    if (systemConf.getWorldNames().contains(inWorldName))
                        return systemConf.getTimelineEventNames(inWorldName);
                }
                // Gives hint of shop type
                else if (args[0].equalsIgnoreCase(WorldMakerCommand.SET_SHOP_SPAWN_LOCATION_CMD)) {
                    List<String> shopTypeNames = new ArrayList<>();
                    shopTypeNames.add(BedwarsShopType.ITEMS_SHOP.toString());
                    shopTypeNames.add(BedwarsShopType.PERMA_SHOP.toString());
                    return shopTypeNames;
                }
                // Gives codename hint
                else if (args[0].equalsIgnoreCase(WorldMakerCommand.SET_LOCKED_ENTITY_CMD)) {
                    String inWorldName = player.getWorld().getName();
                    if (systemConf.getWorldNames().contains(inWorldName))
                        return Collections.singletonList("<codename>");
                }
                // Gives locked entities codename hint
                else if (args[0].equalsIgnoreCase(WorldMakerCommand.ADD_LOCKED_REQUEST_CMD) ||
                        args[0].equalsIgnoreCase(WorldMakerCommand.REMOVE_LOCKED_ENTITY_CMD)) {
                    String inWorldName = player.getWorld().getName();
                    if (systemConf.getWorldNames().contains(inWorldName))
                        return systemConf.getLockedCodename(inWorldName);
                }
                // Gives size hint for setting border
                else if (args[0].equalsIgnoreCase(WorldMakerCommand.SET_BORDER_CMD)) {
                    String worldName = player.getWorld().getName();
                    if (systemConf.getWorldNames().contains(worldName))
                        return Collections.singletonList("<size|default=1024>");
                }
                // Gives size hint for setting shrunk border
                else if (args[0].equalsIgnoreCase(WorldMakerCommand.SET_SHRUNK_BORDER_CMD)) {
                    String worldName = player.getWorld().getName();
                    if (systemConf.getWorldNames().contains(worldName))
                        return Collections.singletonList("<size|default=24>");
                }
            } else if (args.length == 3) {
                // Gives coordinate Y hint
                if (args[0].equalsIgnoreCase(WorldMakerCommand.SET_BLOCK_ON_CMD)) {
                    List<String> setBlockHint = new ArrayList<>();
                    setBlockHint.add("Y");
                    return setBlockHint;
                }
                // Gives duration hint
                else if (args[0].equalsIgnoreCase(WorldMakerCommand.ADD_EVENT_CMD)) {
                    return Collections.singletonList("<duration-in-second>");
                }
                // Gives resource type hint
                else if (args[0].equalsIgnoreCase(WorldMakerCommand.SET_RESOURCE_SPAWNER_CMD) ||
                        args[0].equalsIgnoreCase(WorldMakerCommand.ADD_LOCKED_REQUEST_CMD)) {
                    List<String> resourceType = new ArrayList<>();
                    resourceType.add(ResourcesType.IRON.toString());
                    resourceType.add(ResourcesType.GOLD.toString());
                    resourceType.add(ResourcesType.DIAMOND.toString());
                    resourceType.add(ResourcesType.EMERALD.toString());
                    return resourceType;
                }
                // Gives resource spawner codename hint
                else if (args[0].equalsIgnoreCase(WorldMakerCommand.REMOVE_RESOURCE_SPAWNER_CMD) ||
                        args[0].equalsIgnoreCase(WorldMakerCommand.SET_RESOURCE_SPAWNER_FREQ_CMD)) {
                    String worldName = player.getWorld().getName();
                    if (systemConf.getWorldNames().contains(worldName)) {
                        // Give hint by team name or else other public resource spawners
                        return systemConf.getRSCodename(worldName, args[1]);
                    }
                }
                // Give event order hint
                else if (args[0].equalsIgnoreCase(WorldMakerCommand.SET_EVENT_ORDER_CMD)) {
                    return Collections.singletonList("<number-order>");
                }
                // Gives hint all public resource spawners
                else if (args[0].equalsIgnoreCase(WorldMakerCommand.SET_LOCKED_ENTITY_CMD)) {
                    String inWorldName = player.getWorld().getName();
                    if (systemConf.getWorldNames().contains(inWorldName))
                        return systemConf.getPublicRSCodename(inWorldName);
                }
            } else if (args.length == 4) {
                // Gives coordinate Z hint
                if (args[0].equalsIgnoreCase(WorldMakerCommand.SET_BLOCK_ON_CMD)) {
                    List<String> setBlockHint = new ArrayList<>();
                    setBlockHint.add("Z");
                    return setBlockHint;
                }
                // Gives set name hint
                else if (args[0].equalsIgnoreCase(WorldMakerCommand.ADD_EVENT_CMD)) {
                    List<String> eventType = new ArrayList<>();
                    eventType.add("<event-name|display>");
                    return eventType;
                }
                // Gives duration hint
                else if (args[0].equalsIgnoreCase(WorldMakerCommand.SET_RESOURCE_SPAWNER_FREQ_CMD)) {
                    List<String> eventType = new ArrayList<>();
                    eventType.add("<duration-per-spawn>");
                    return eventType;
                }
                // Gives codename hint
                else if (args[0].equalsIgnoreCase(WorldMakerCommand.SET_RESOURCE_SPAWNER_CMD)) {
                    return Collections.singletonList("codename");
                }
                // Gives <amount> hint
                else if (args[0].equalsIgnoreCase(WorldMakerCommand.ADD_LOCKED_REQUEST_CMD)) {
                    String inWorldName = player.getWorld().getName();
                    if (systemConf.getWorldNames().contains(inWorldName))
                        return Collections.singletonList("<amount>");
                }
            }
        }

        return null;
    }

}
