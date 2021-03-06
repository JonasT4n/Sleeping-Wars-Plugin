package com.joenastan.sleepingwars.events;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joenastan.sleepingwars.SleepingWarsPlugin;
import com.joenastan.sleepingwars.enumtypes.GameCommandType;
import com.joenastan.sleepingwars.game.GameManager;
import com.joenastan.sleepingwars.utility.CustomEntity.ButtonCommandEntity;
import com.joenastan.sleepingwars.utility.DataFiles.GameButtonHolder;
import com.joenastan.sleepingwars.utility.DataFiles.GameSystemConfig;
import com.joenastan.sleepingwars.utility.CustomEntity.PlayerBedwarsBuilderEntity;
import com.joenastan.sleepingwars.utility.PluginStaticFunc;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class OnBuilderModeEvents implements Listener {

    private final GameSystemConfig systemConfig = SleepingWarsPlugin.getGameSystemConfig();

    public static final Map<Player, PlayerBedwarsBuilderEntity> CUSTOM_BUILDER_ENTITY = new HashMap<>();
    public static final Map<Player, GameCommandType> BUTTON_COMMAND_HANDLER = new HashMap<>();
    public static final Map<Player, String> WORLD_SELECTION = new HashMap<>();

    public static void clearStatic() {
        for (Map.Entry<Player, PlayerBedwarsBuilderEntity> builderEntry : CUSTOM_BUILDER_ENTITY.entrySet())
            builderEntry.getValue().returnEntity();

        CUSTOM_BUILDER_ENTITY.clear();
        BUTTON_COMMAND_HANDLER.clear();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent event) {
        String inWorldName = event.getPlayer().getWorld().getName();
        Player player = event.getPlayer();

        // Check if player joined into the world builder
        if (systemConfig.getWorldNames().contains(inWorldName))
            CUSTOM_BUILDER_ENTITY.put(player, new PlayerBedwarsBuilderEntity(player, Bukkit.getWorlds()
                    .get(0).getSpawnLocation(), GameMode.SURVIVAL));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        String toWorldName = event.getTo().getWorld().getName();
        String fromWorldName = event.getFrom().getWorld().getName();

        // Check buffers
        BUTTON_COMMAND_HANDLER.remove(player);
        WORLD_SELECTION.remove(player);

        // Check if player teleport from world builder to other world builder, ignore this
        if (systemConfig.getWorldNames().contains(toWorldName) && systemConfig.getWorldNames().contains(fromWorldName))
            return;

        // Check if player teleport into world builder
        if (systemConfig.getWorldNames().contains(toWorldName)) {
            if (!CUSTOM_BUILDER_ENTITY.containsKey(player)) {
                GameMode prevMode = player.getGameMode();
                PlayerBedwarsBuilderEntity newEnt = new PlayerBedwarsBuilderEntity(player, event.getFrom(), prevMode);
                CUSTOM_BUILDER_ENTITY.put(player, newEnt);
            }
            player.setGameMode(GameMode.CREATIVE);
        }

        // Check if player leave from world builder
        else if (systemConfig.getWorldNames().contains(fromWorldName)) {
            CUSTOM_BUILDER_ENTITY.remove(player);
            if (player.getWorld().getPlayers().size() == 0) {
                systemConfig.save();
                player.sendMessage(ChatColor.LIGHT_PURPLE + "All game config in plugin has been saved.");
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerExit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String inWorldName = event.getPlayer().getWorld().getName();

        // Check if player exit from the world builder
        if (systemConfig.getWorldNames().contains(inWorldName)) {
            if (CUSTOM_BUILDER_ENTITY.containsKey(player)) {
                PlayerBedwarsBuilderEntity playerEnt = CUSTOM_BUILDER_ENTITY.remove(player);
                playerEnt.returnEntity();
                playerEnt.clearLocationsBuffer();
            }
            if (player.getWorld().getPlayers().size() == 0)
                systemConfig.save();
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        String inWorldName = player.getWorld().getName();
        Block block = event.getBlockPlaced();
        // Check if player put something in builder world
        if (systemConfig.getWorldNames().contains(inWorldName)) {
            // Check if player put TNT explosion in builder world, this is very restricted
            if (block.getType() == Material.TNT) {
                block.setType(Material.AIR);
                Location middleBlockLoc = new Location(block.getWorld(), block.getLocation().getX() + 0.5d,
                        block.getLocation().getY() + 0.5d, block.getLocation().getZ() + 0.5d);
                block.getWorld().spawn(middleBlockLoc, TNTPrimed.class);
            }

            // Check if player has builder permission
            if (player.hasPermission("sleepywar.builder")) {
                // Create a button handler inside bedwars world
                if (PluginStaticFunc.isButton(block.getType())) {
                    GameCommandType entry = BUTTON_COMMAND_HANDLER.remove(player);
                    if (entry != null) {
                        Map<String, Map<Block, ButtonCommandEntity>> mb = GameButtonHolder.buttons;
                        if (!mb.containsKey(inWorldName))
                            mb.put(inWorldName, new HashMap<>());
                        mb.get(inWorldName).put(block, new ButtonCommandEntity(entry, block.getLocation(), inWorldName));
                        player.sendMessage(ChatColor.LIGHT_PURPLE + "Button command handler has been created.");
                        return;
                    }
                }

                PlayerBedwarsBuilderEntity playerBE = CUSTOM_BUILDER_ENTITY.get(player);
                if (playerBE != null) {
                    String teamName = playerBE.getTeamChoice();
                    if (teamName != null) {
                        // Check required location buffer amount
                        if (playerBE.getRequiredAmountLoc() == 0) {
                            // Check if player put bed in world
                            if (PluginStaticFunc.isBed(block.getType()) && !teamName.equals("PUBLIC")) {
                                Location onPlacedLoc = block.getLocation();
                                if (systemConfig.setBedLocation(inWorldName, teamName, onPlacedLoc)) {
                                    player.sendMessage(String.format("%sBed for team %s has been settled" +
                                            " on (X/Y/Z): %d/%d/%d", ChatColor.DARK_AQUA + "", teamName,
                                            onPlacedLoc.getBlockX(), onPlacedLoc.getBlockY(),
                                            onPlacedLoc.getBlockZ()));
                                    playerBE.setTeamChoice(null);
                                }
                            }

                            // Check if player put any kind of intractable
                            else if (teamName.equals("PUBLIC") && playerBE.countCodenameHolder() > 0 &&
                                    (PluginStaticFunc.isGateOrDoor(block.getType()) || PluginStaticFunc
                                            .isButton(block.getType()) || block.getType() == Material.LEVER)) {
                                Location onPlacedLoc = block.getLocation();
                                if (playerBE.countCodenameHolder() == 1) {
                                    if (systemConfig.setLockedRequest(inWorldName, playerBE.removeCodename(), onPlacedLoc)) {
                                        player.sendMessage(String.format("%sLocked on (X/Y/Z): %d/%d/%d",
                                                ChatColor.AQUA + "", onPlacedLoc.getBlockX(),
                                                onPlacedLoc.getBlockY(), onPlacedLoc.getBlockZ()));
                                    }
                                } else if (playerBE.countCodenameHolder() == 2) {
                                    if (systemConfig.setLockedRequest(inWorldName, playerBE.removeCodename(), playerBE.removeCodename(), onPlacedLoc)) {
                                        player.sendMessage(String.format("%sLocked the Resource Spawner" +
                                                " on (X/Y/Z): %d/%d/%d", ChatColor.AQUA + "", onPlacedLoc.getBlockX(),
                                                onPlacedLoc.getBlockY(), onPlacedLoc.getBlockZ()));
                                    }
                                }
                                playerBE.setTeamChoice(null);
                            }
                        } else if (playerBE.getRequiredAmountLoc() == 2) {
                            // With any other block set location buffer
                            playerBE.addLocationBuffer(block.getLocation());
                            if (playerBE.countLocationsBuffer() >= 2) {
                                systemConfig.setAreaBuff(inWorldName, teamName, playerBE.removeBufferLoc(), playerBE.removeBufferLoc());
                                playerBE.setTeamChoice(null);
                                playerBE.setRequiredAmountLoc(0);
                                player.sendMessage(ChatColor.BLUE + "Buffer Zone has been set.");
                            }
                            event.setCancelled(true);
                        }
                    }
                }
            } else {
                event.setCancelled(true);
                player.sendMessage(ChatColor.YELLOW + "You don't have permission to build.");
            }
        } else {
            // Create a button holder outside the bedwars world
            GameCommandType entry = BUTTON_COMMAND_HANDLER.remove(player);
            String worldEntry = WORLD_SELECTION.remove(player);
            if (entry != null && worldEntry != null && player.hasPermission("sleepywar.builder") &&
                    PluginStaticFunc.isButton(block.getType())) {
                Map<String, Map<Block, ButtonCommandEntity>> mb = GameButtonHolder.buttons;
                if (!mb.containsKey(worldEntry))
                    mb.put(worldEntry, new HashMap<>());
                mb.get(worldEntry).put(block, new ButtonCommandEntity(entry, block.getLocation(), worldEntry));
                player.sendMessage(ChatColor.LIGHT_PURPLE + "Button command handler has been created.");
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        String inWorldName = player.getWorld().getName();
        Block block = event.getBlock();

        // Check if player in world builder
        if (systemConfig.getWorldNames().contains(inWorldName)) {
            if (player.hasPermission("sleepywar.builder")) {
                if (PluginStaticFunc.isButton(block.getType()) && GameButtonHolder.buttons.containsKey(inWorldName)) {
                    Map<Block, ButtonCommandEntity> buttons = GameButtonHolder.buttons.get(inWorldName);
                    if (buttons.containsKey(block)) {
                        buttons.remove(block);
                        player.sendMessage(ChatColor.LIGHT_PURPLE + "Button command handler has been removed");
                    }
                }
                return;
            }
            event.setCancelled(true);
        } else {
            if (!GameManager.instance.getRoomMap().containsKey(inWorldName) && PluginStaticFunc.isButton(block.getType())) {
                if (GameButtonHolder.buttons.containsKey(inWorldName)) {
                    Map<Block, ButtonCommandEntity> buttons = GameButtonHolder.buttons.get(inWorldName);
                    if (buttons.containsKey(block)) {
                        if (player.hasPermission("sleepywar.builder")) {
                            GameButtonHolder.buttons.get(inWorldName).remove(block);
                            player.sendMessage(ChatColor.LIGHT_PURPLE + "Button command handler has been removed");
                            return;
                        }
                        player.sendMessage(ChatColor.YELLOW + "Cannot break the button command handler.");
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onExplodeEvent(EntityExplodeEvent event) {
        List<Block> blockList = event.blockList();
        String inWorldName = event.getLocation().getWorld().getName();

        // Check if player put TNT explosion in game world or builder world
        if (systemConfig.getWorldNames().contains(inWorldName))
            blockList.clear();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onArmorStandReact(PlayerArmorStandManipulateEvent event) {
        // DO NOTHING
    }
}
