package com.joenastan.sleepingwars.commands.TabCompletor;

import com.joenastan.sleepingwars.commands.WorldMakerCommand;
import com.joenastan.sleepingwars.enumtypes.BedwarsShopType;
import com.joenastan.sleepingwars.SleepingWarsPlugin;
import com.joenastan.sleepingwars.enumtypes.GameCommandType;
import com.joenastan.sleepingwars.enumtypes.InGameFlags;
import com.joenastan.sleepingwars.enumtypes.TimelineEventType;
import com.joenastan.sleepingwars.utility.DataFiles.GameSystemConfig;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SWorldCommands implements TabCompleter {

    private static final GameSystemConfig systemConfig = SleepingWarsPlugin.getGameSystemConfig();

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command,
                                      @Nonnull String alias, @Nonnull String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender);
            if (args.length == 1) {
                List<String> subs = new ArrayList<>();

                // Separate commands by player standing in world
                if (systemConfig.getWorldNames().contains(player.getWorld().getName())) {
                    // Adder Commands
                    subs.add(WorldMakerCommand.ADD_EVENT_CMD);
                    subs.add(WorldMakerCommand.ADD_TEAM_CMD);
                    subs.add(WorldMakerCommand.ADD_LOCKED_REQUEST_CMD);

                    // Setter Commands
                    subs.add(WorldMakerCommand.SET_AREA_BUFFER_CMD);
                    subs.add(WorldMakerCommand.SET_MAX_PLAYER_PER_TEAM_CMD);
                    subs.add(WorldMakerCommand.SET_FLAG_CMD);
                    subs.add(WorldMakerCommand.SET_BORDER_CMD);
                    subs.add(WorldMakerCommand.SET_SHRUNK_BORDER_CMD);
                    subs.add(WorldMakerCommand.SET_LOCKED_ENTITY_CMD);
                    subs.add(WorldMakerCommand.SET_EVENT_ORDER_CMD);
                    subs.add(WorldMakerCommand.SET_BED_LOCATION_CMD);
                    subs.add(WorldMakerCommand.SET_BLOCK_ON_CMD);
                    subs.add(WorldMakerCommand.SET_SHOP_SPAWN_LOCATION_CMD);
                    subs.add(WorldMakerCommand.SET_QUEUE_SPAWN_CMD);
                    subs.add(WorldMakerCommand.SET_WORLD_SPAWN_CMD);
                    subs.add(WorldMakerCommand.SET_TEAM_RAW_COLOR_CMD);
                    subs.add(WorldMakerCommand.SET_RESOURCE_SPAWNER_INTERVAL_CMD);
                    subs.add(WorldMakerCommand.SET_RESOURCE_SPAWNER_CMD);
                    subs.add(WorldMakerCommand.SET_TEAM_SPAWN_LOCATION_CMD);
                    subs.add(WorldMakerCommand.SET_DEFAULT_VALUE_CMD);
                    subs.add(WorldMakerCommand.SET_RESOURCE_TYPE_SPAWN_CMD);

                    // Remover Commands
                    subs.add(WorldMakerCommand.REMOVE_EVENT_CMD);
                    subs.add(WorldMakerCommand.REMOVE_RESOURCE_SPAWNER_CMD);
                    subs.add(WorldMakerCommand.REMOVE_SHOP_SPAWN_LOCATION_CMD);
                    subs.add(WorldMakerCommand.REMOVE_TEAM_CMD);
                    subs.add(WorldMakerCommand.REMOVE_LOCKED_ENTITY_CMD);

                    // Tester Commands
                    subs.add(WorldMakerCommand.TEST_SPAWN_SHOP_CMD);
                    subs.add(WorldMakerCommand.TEST_RESOURCE_SPAWNER_CMD);

                    // Information Commands
                    subs.add(WorldMakerCommand.RESOURCE_SPAWNER_INFO_CMD);
                    subs.add(WorldMakerCommand.TIMELINE_INFO_CMD);
                    subs.add(WorldMakerCommand.WORLD_INFO_CMD);

                    // World Utilities
                    subs.add(WorldMakerCommand.TP_LEAVE_WORLD_CMD);
                }

                // World Utilities
                subs.add(WorldMakerCommand.CREATE_WORLD_CMD);
                subs.add(WorldMakerCommand.TP_EDIT_WORLD_CMD);
                subs.add(WorldMakerCommand.COMMAND_HELP_CMD);
                subs.add(WorldMakerCommand.SAVE_CMD);

                // Adder Commands
                subs.add(WorldMakerCommand.ADD_GAME_BUTTON);
                return subs;
            } else if (args.length == 2) {
                // Check player using command in certain world
                String inWorldName = player.getWorld().getName();
                if (systemConfig.getWorldNames().contains(inWorldName)) {
                    // Gives hint for set block command
                    if (args[0].equalsIgnoreCase(WorldMakerCommand.SET_BLOCK_ON_CMD))
                        return Collections.singletonList("<X> <Y> <Z>");

                    // Gives hint to add new event
                    else if (args[0].equalsIgnoreCase(WorldMakerCommand.ADD_EVENT_CMD)) {
                        List<String> e = new ArrayList<>();
                        e.add(TimelineEventType.EMERALD_UPGRADE.toString() + " <interval-seconds> <display-name>");
                        e.add(TimelineEventType.DIAMOND_UPGRADE.toString() + " <interval-seconds> <display-name>");
                        e.add(TimelineEventType.DESTROY_ALL_BED.toString() + " <interval-seconds> <display-name>");
                        e.add(TimelineEventType.WORLD_SHRINKING.toString() + " <interval-seconds> <display-name>");
                        return e;
                    }

                    // Gives hint for team spawner
                    else if (args[0].equalsIgnoreCase(WorldMakerCommand.SET_TEAM_SPAWN_LOCATION_CMD)) {
                        List<String> tl = systemConfig.getTeamNames(inWorldName, true);
                        tl.add("<select-team>");
                        return tl;
                    }

                    // Gives hint for setting bed location for team
                    else if (args[0].equalsIgnoreCase(WorldMakerCommand.SET_BED_LOCATION_CMD)) {
                        List<String> tl = systemConfig.getTeamNames(inWorldName, true);
                        tl.add("<select-team>");
                        return tl;
                    }

                    // Gives hint for coloring team
                    else if (args[0].equalsIgnoreCase(WorldMakerCommand.SET_TEAM_RAW_COLOR_CMD)) {
                        List<String> tl = systemConfig.getTeamNames(inWorldName, true);
                        tl.add("<select-team> <color>");
                        return tl;
                    }

                    // Gives hint for removing team
                    else if (args[0].equalsIgnoreCase(WorldMakerCommand.REMOVE_TEAM_CMD)) {
                        List<String> tl = systemConfig.getTeamNames(inWorldName, true);
                        tl.add("<select-team> [default=false|is-remove-permanent]");
                        return tl;
                    }

                    // Gives hint for setting resource spawner
                    else if (args[0].equalsIgnoreCase(WorldMakerCommand.SET_RESOURCE_SPAWNER_CMD)) {
                        List<String> tnh = systemConfig.getTeamNames(inWorldName, true);
                        tnh.add("<select-team|PUBLIC>");
                        for (int i = 0; i < tnh.size(); i++) {
                            String finalString = tnh.get(i) + " <spawner-type> <codename>";
                            tnh.set(i, finalString);
                        }
                        return tnh;
                    }

                    // Gives hint to remove resource spawner
                    else if (args[0].equalsIgnoreCase(WorldMakerCommand.REMOVE_RESOURCE_SPAWNER_CMD)) {
                        List<String> tnh = systemConfig.getTeamNames(inWorldName, true);
                        tnh.add("<select-team|PUBLIC> <codename>");
                        return tnh;
                    }

                    // Gives hint to set existing resource spawner interval
                    else if (args[0].equalsIgnoreCase(WorldMakerCommand.SET_RESOURCE_SPAWNER_INTERVAL_CMD)) {
                        List<String> tnh = systemConfig.getTeamNames(inWorldName, true);
                        tnh.add("<select-team|PUBLIC>");
                        for (int i = 0; i < tnh.size(); i++) {
                            String finalString = tnh.get(i) + " <codename> <interval-seconds>";
                            tnh.set(i, finalString);
                        }
                        return tnh;
                    }

                    // Gives hint to set team area buffer
                    else if (args[0].equalsIgnoreCase(WorldMakerCommand.SET_AREA_BUFFER_CMD)) {
                        List<String> tnh = systemConfig.getTeamNames(inWorldName, true);
                        tnh.add("<select-team|PUBLIC>");
                        return tnh;
                    }

                    // Gives hint removing existing event
                    else if (args[0].equalsIgnoreCase(WorldMakerCommand.REMOVE_EVENT_CMD))
                        return systemConfig.getTimelineEventNames(inWorldName);

                    // Gives hint to set procedure order event
                    else if (args[0].equalsIgnoreCase(WorldMakerCommand.SET_EVENT_ORDER_CMD)) {
                        List<String> el = systemConfig.getTimelineEventNames(inWorldName);
                        for (int i = 0; i < el.size(); i++) {
                            String finalString = el.get(i) + " <index-order>";
                            el.set(i, finalString);
                        }
                        return el;
                    }

                    // Gives hint of setting a shop type on location
                    else if (args[0].equalsIgnoreCase(WorldMakerCommand.SET_SHOP_SPAWN_LOCATION_CMD)) {
                        List<String> stn = new ArrayList<>();
                        stn.add(BedwarsShopType.ITEMS_SHOP.toString());
                        stn.add(BedwarsShopType.PERMA_SHOP.toString());
                        return stn;
                    }

                    // Gives codename hint
                    else if (args[0].equalsIgnoreCase(WorldMakerCommand.SET_LOCKED_ENTITY_CMD)) {
                        return Collections.singletonList("<codename> [resource-spawner-codename]");
                    }

                    // Gives hint for adding a locked entity request
                    else if (args[0].equalsIgnoreCase(WorldMakerCommand.ADD_LOCKED_REQUEST_CMD)) {
                        List<String> cn = systemConfig.getLockedCodename(inWorldName);
                        for (int i = 0; i < cn.size(); i++) {
                            String finalString = cn.get(i) + " <type-requests> <price>";
                            cn.set(i, finalString);
                        }
                        return cn;
                    }

                    // Gives hint to remove existing locked entity
                    else if (args[0].equalsIgnoreCase(WorldMakerCommand.REMOVE_LOCKED_ENTITY_CMD))
                        return systemConfig.getLockedCodename(inWorldName);

                    // Gives hint for setting border
                    else if (args[0].equalsIgnoreCase(WorldMakerCommand.SET_BORDER_CMD))
                        return Collections.singletonList("<size|default=1024>");

                    // Gives hint for setting shrunk border
                    else if (args[0].equalsIgnoreCase(WorldMakerCommand.SET_SHRUNK_BORDER_CMD))
                        return Collections.singletonList("<size|default=24>");

                    // Gives hint to set start or leave command button handler
                    else if (args[0].equalsIgnoreCase(WorldMakerCommand.ADD_GAME_BUTTON)) {
                        List<String> bch = new ArrayList<>();
                        bch.add(GameCommandType.START.toString());
                        bch.add(GameCommandType.LEAVE.toString());
                        return bch;
                    }

                    // Gives hint to set maximum player per team
                    else if (args[0].equalsIgnoreCase(WorldMakerCommand.SET_MAX_PLAYER_PER_TEAM_CMD))
                        return Collections.singletonList("<1-100>");

                    // Gives hint to set map flag
                    else if (args[0].equalsIgnoreCase(WorldMakerCommand.SET_FLAG_CMD)) {
                        List<String> fl = new ArrayList<>();
                        fl.add(InGameFlags.ARMOR_RESTRICTION.toString() + " <value:number|decimal>");
                        return fl;
                    }

                    // Gives hint to remove shop spawn location
                    else if (args[0].equalsIgnoreCase(WorldMakerCommand.REMOVE_SHOP_SPAWN_LOCATION_CMD)) {
                        List<String> sl = new ArrayList<>();
                        sl.add("<shop-type> <index-number>");
                        sl.add(BedwarsShopType.ITEMS_SHOP.toString() + " <index-number>");
                        sl.add(BedwarsShopType.PERMA_SHOP.toString() + " <index-number>");
                        return sl;
                    }

                    // Gives hint to set resource spawning type
                    else if (args[0].equalsIgnoreCase(WorldMakerCommand.SET_RESOURCE_TYPE_SPAWN_CMD)) {
                        List<String> tnh = systemConfig.getTeamNames(inWorldName, true);
                        tnh.add("<select-team|PUBLIC>");
                        for (int i = 0; i < tnh.size(); i++) {
                            String finalString = tnh.get(i) + " <codename> <item-type>";
                            tnh.set(i, finalString);
                        }
                        return tnh;
                    }

                    // Gives hint to add a new team
                    else if (args[0].equalsIgnoreCase(WorldMakerCommand.ADD_TEAM_CMD))
                        return Collections.singletonList("<team-name> [color]");
                } else {
                    // Gives hint to set join command button handler
                    if (args[0].equalsIgnoreCase(WorldMakerCommand.ADD_GAME_BUTTON))
                        return Collections.singletonList(GameCommandType.JOIN.toString() + " <map-name>");
                }

                // Gives hint for Create world
                if (args[0].equalsIgnoreCase(WorldMakerCommand.CREATE_WORLD_CMD))
                    return Collections.singletonList("<world|map-name>");

                // Gives hint for edit world command
                else if (args[0].equalsIgnoreCase("edit"))
                    return systemConfig.getWorldNames();

                // Gives hint when need command help
                else if (args[0].equalsIgnoreCase(WorldMakerCommand.COMMAND_HELP_CMD))
                    return Collections.singletonList("[default-page=1]");
            } else if (args.length == 3) {
                // Check player using command in certain world
                String inWorldName = player.getWorld().getName();
                List<String> worldNames = systemConfig.getWorldNames();
                if (worldNames.contains(inWorldName)) {
                    // Gives hint for set block command
                    if (args[0].equalsIgnoreCase(WorldMakerCommand.SET_BLOCK_ON_CMD))
                        return Collections.singletonList("<Y> <Z>");

                    // Gives hint to add new event
                    else if (args[0].equalsIgnoreCase(WorldMakerCommand.ADD_EVENT_CMD))
                        return Collections.singletonList("<interval-seconds> <display-name>");

                    // Gives hint to add a requested resource to unlock locked entity
                    else if (args[0].equalsIgnoreCase(WorldMakerCommand.ADD_LOCKED_REQUEST_CMD)) {
                        List<String> resourceType = new ArrayList<>();
                        resourceType.add(Material.IRON_INGOT.toString().toLowerCase() + " <price>");
                        resourceType.add(Material.GOLD_INGOT.toString().toLowerCase() + " <price>");
                        resourceType.add(Material.DIAMOND.toString().toLowerCase() + " <price>");
                        resourceType.add(Material.EMERALD.toString().toLowerCase() + " <price>");
                        return resourceType;
                    }

                    // Gives hint to set resource spawner type spawner
                    else if (args[0].equalsIgnoreCase(WorldMakerCommand.SET_RESOURCE_SPAWNER_CMD)) {
                        List<String> resourceType = new ArrayList<>();
                        resourceType.add(Material.IRON_INGOT.toString().toLowerCase() + " <codename>");
                        resourceType.add(Material.GOLD_INGOT.toString().toLowerCase() + " <codename>");
                        resourceType.add(Material.DIAMOND.toString().toLowerCase() + " <codename>");
                        resourceType.add(Material.EMERALD.toString().toLowerCase() + " <codename>");
                        return resourceType;
                    }

                    // Gives hint to remove resource spawner
                    else if (args[0].equalsIgnoreCase(WorldMakerCommand.REMOVE_RESOURCE_SPAWNER_CMD))
                        return systemConfig.getRSCodename(inWorldName, args[1]);

                    // Gives hint to set resource spawner interval spawner
                    else if (args[0].equalsIgnoreCase(WorldMakerCommand.SET_RESOURCE_SPAWNER_INTERVAL_CMD)) {
                        List<String> cl = systemConfig.getRSCodename(inWorldName, args[1]);
                        for (int i = 0; i < cl.size(); i++) {
                            String finalString = cl.get(i) + " <interval-seconds>";
                            cl.set(i, finalString);
                        }
                        return cl;
                    }

                    // Gives hint to set event procedure order
                    else if (args[0].equalsIgnoreCase(WorldMakerCommand.SET_EVENT_ORDER_CMD))
                        return Collections.singletonList("<index-order>");

                    // Gives hint all public resource spawners
                    else if (args[0].equalsIgnoreCase(WorldMakerCommand.SET_LOCKED_ENTITY_CMD))
                        return systemConfig.getPublicRSCodename(inWorldName);

                    // Gives hint to set map flag
                    else if (args[0].equalsIgnoreCase(WorldMakerCommand.SET_FLAG_CMD)) {
                        InGameFlags flag = InGameFlags.fromString(args[1]);
                        if (flag != null) {
                            if (flag == InGameFlags.ARMOR_RESTRICTION) {
                                List<String> h = new ArrayList<>();
                                h.add("<true-or-false>");
                                h.add("true");
                                h.add("false");
                                return h;
                            }
                        }
                    }

                    // Gives hint for coloring team
                    else if (args[0].equalsIgnoreCase(WorldMakerCommand.SET_TEAM_RAW_COLOR_CMD)) {
                        List<String> cl = new ArrayList<>();
                        cl.add("<color>");
                        cl.add("white");
                        cl.add("blue");
                        cl.add("yellow");
                        cl.add("green");
                        cl.add("red");
                        cl.add("purple");
                        cl.add("gold");
                        cl.add("gray");
                        cl.add("cyan");
                        return cl;
                    }

                    // Gives hint for adding color to new team
                    else if (args[0].equalsIgnoreCase(WorldMakerCommand.ADD_TEAM_CMD)) {
                        List<String> cl = new ArrayList<>();
                        cl.add("[default=white|color]");
                        cl.add("white");
                        cl.add("blue");
                        cl.add("yellow");
                        cl.add("green");
                        cl.add("red");
                        cl.add("purple");
                        cl.add("gold");
                        cl.add("gray");
                        cl.add("cyan");
                        return cl;
                    }

                    // Gives hint to remove shop spawn location
                    else if (args[0].equalsIgnoreCase(WorldMakerCommand.REMOVE_SHOP_SPAWN_LOCATION_CMD)) {
                        BedwarsShopType t = BedwarsShopType.fromString(args[1]);
                        if (t != null) {
                            // Get all shop location information
                            List<Location> ll = systemConfig.getShops(player.getWorld(), inWorldName).get(t);
                            List<String> h = new ArrayList<>();
                            for (int i = 0; i < ll.size(); i++) {
                                Location l = ll.get(i);
                                String finalString = String.format("%d. (X,Y,Z) => (%d,%d,%d)", i, l.getBlockX(),
                                        l.getBlockY(), l.getBlockZ());
                                h.add(finalString);
                            }
                            return h;
                        }
                    }

                    // Gives hint to set resource spawning type
                    else if (args[0].equalsIgnoreCase(WorldMakerCommand.SET_RESOURCE_TYPE_SPAWN_CMD)) {
                        List<String> cn = systemConfig.getRSCodename(inWorldName, args[1]);
                        for (int i = 0; i < cn.size(); i++) {
                            String finalString = cn.get(i) + " <item-type>";
                            cn.set(i, finalString);
                        }
                        return cn;
                    }

                    // Gives hint for removing team permanently
                    else if (args[0].equalsIgnoreCase(WorldMakerCommand.REMOVE_TEAM_CMD)) {
                        List<String> tof = new ArrayList<>();
                        tof.add("[true-or-false]");
                        tof.add("true");
                        tof.add("false");
                        return tof;
                    }
                } else {
                    // Gives hint to set join command button handler
                    if (args[0].equalsIgnoreCase(WorldMakerCommand.ADD_GAME_BUTTON))
                        return worldNames;
                }
            } else if (args.length == 4) {
                // Check player using command in certain world
                String inWorldName = player.getWorld().getName();
                if (systemConfig.getWorldNames().contains(inWorldName)) {
                    // Gives hint for set block command
                    if (args[0].equalsIgnoreCase(WorldMakerCommand.SET_BLOCK_ON_CMD))
                        return Collections.singletonList("<Z>");

                    // Gives set name hint
                    else if (args[0].equalsIgnoreCase(WorldMakerCommand.ADD_EVENT_CMD))
                        return Collections.singletonList("display-name");

                    // Gives hint to set resource spawner interval spawner
                    else if (args[0].equalsIgnoreCase(WorldMakerCommand.SET_RESOURCE_SPAWNER_INTERVAL_CMD))
                        return Collections.singletonList("<interval-seconds>");

                    // Gives codename hint
                    else if (args[0].equalsIgnoreCase(WorldMakerCommand.SET_RESOURCE_SPAWNER_CMD)) {
                        return Collections.singletonList("<codename>");
                    }

                    // Gives hint to add a requested resource to unlock locked entity
                    else if (args[0].equalsIgnoreCase(WorldMakerCommand.ADD_LOCKED_REQUEST_CMD))
                        return Collections.singletonList("<price>");

                    // Gives hint to set resource spawning type
                    else if (args[0].equalsIgnoreCase(WorldMakerCommand.SET_RESOURCE_TYPE_SPAWN_CMD)) {
                        List<String> it = new ArrayList<>();
                        it.add(Material.IRON_INGOT.toString().toLowerCase());
                        it.add(Material.GOLD_INGOT.toString().toLowerCase());
                        it.add(Material.DIAMOND.toString().toLowerCase());
                        it.add(Material.EMERALD.toString().toLowerCase());
                        return it;
                    }
                }
            }
        }
        return null;
    }

}
