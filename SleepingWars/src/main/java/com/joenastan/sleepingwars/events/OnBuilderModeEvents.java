package com.joenastan.sleepingwars.events;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joenastan.sleepingwars.SleepingWarsPlugin;
import com.joenastan.sleepingwars.utility.DataFiles.GameSystemConfig;
import com.joenastan.sleepingwars.utility.CustomDerivedEntity.PlayerBedwarsBuilderEntity;
import com.joenastan.sleepingwars.utility.Hologram.HologramManager;
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

    private static Map<Player, PlayerBedwarsBuilderEntity> customBuilderEntity = new HashMap<>();
    private final GameSystemConfig systemConfig = SleepingWarsPlugin.getGameSystemConfig();
    private static final HologramManager holoManager = SleepingWarsPlugin.getHologramManager();

    public static Map<Player, PlayerBedwarsBuilderEntity> getCustomBuilderEntity() {
        return customBuilderEntity;
    }

    public static void clearStatic() {
        for (Map.Entry<Player, PlayerBedwarsBuilderEntity> builderEntry : customBuilderEntity.entrySet()) {
            builderEntry.getValue().returnEntity();
        }
        customBuilderEntity.clear();
        customBuilderEntity = null;
        holoManager.shutdown();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent event) {
        String inWorldName = event.getPlayer().getWorld().getName();
        Player player = event.getPlayer();
        // Check if player joined into the world builder
        if (systemConfig.getWorldNames().contains(inWorldName))
            customBuilderEntity.put(player, new PlayerBedwarsBuilderEntity(player, Bukkit.getWorlds()
                    .get(0).getSpawnLocation(), GameMode.SURVIVAL));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        String inWorldName = event.getTo().getWorld().getName();
        String fromWorldName = event.getFrom().getWorld().getName();
        // Check if player teleport from world builder to other world builder, ignore this
        if (systemConfig.getWorldNames().contains(inWorldName) && systemConfig.getWorldNames()
                .contains(fromWorldName))
            return;
        // Check if player teleport into world builder
        if (systemConfig.getWorldNames().contains(inWorldName)) {
            if (!customBuilderEntity.containsKey(player))
                customBuilderEntity.put(player, new PlayerBedwarsBuilderEntity(player, event.getFrom(),
                        player.getGameMode()));
            player.setGameMode(GameMode.CREATIVE);
        }
        // Check if player leave from world builder
        else if (systemConfig.getWorldNames().contains(fromWorldName)) {
            customBuilderEntity.remove(player);
            if (player.getWorld().getPlayers().size() == 0) {
                systemConfig.Save();
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
            if (customBuilderEntity.containsKey(player)) {
                PlayerBedwarsBuilderEntity playerEnt = customBuilderEntity.remove(player);
                playerEnt.returnEntity();
                playerEnt.clearLocationsBuffer();
            }
            if (player.getWorld().getPlayers().size() == 0)
                systemConfig.Save();
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
                PlayerBedwarsBuilderEntity playerBE = customBuilderEntity.get(player);
                if (playerBE != null) {
                    String teamName = playerBE.getTeamChoice();
                    if (teamName != null) {
                        // Check required location buffer amount
                        if (playerBE.getRequiredAmountLoc() == 0) {
                            // Check if player put bed in world
                            if (PluginStaticFunc.isMaterialBed(block.getType()) && !teamName.equals("PUBLIC")) {
                                Location onPlacedLoc = block.getLocation();
                                if (systemConfig.setBedLocation(inWorldName, teamName, onPlacedLoc)) {
                                    player.sendMessage(String.format("%sBed for team %s has been settled on (X/Y/Z): %d/%d/%d",
                                            ChatColor.DARK_AQUA + "", teamName, onPlacedLoc.getBlockX(), onPlacedLoc.getBlockY(),
                                            onPlacedLoc.getBlockZ()));
                                    playerBE.setTeamChoice(null);
                                }
                            }
                            // Check if player put any kind of doors
                            else if (teamName.equals("PUBLIC") && playerBE.countCodenameHolder() > 0 &&
                                    (PluginStaticFunc.isFenceGate(block.getType()) || PluginStaticFunc
                                            .isTrapDoor(block.getType()) || PluginStaticFunc
                                            .isStandardDoor(block.getType()))) {
                                Location onPlacedLoc = block.getLocation();
                                if (playerBE.countCodenameHolder() == 1) {
                                    if (systemConfig.setLockedRequest(inWorldName, playerBE.removeCodename(),
                                            onPlacedLoc)) {
                                        player.sendMessage(String.format("%sLocking the door on (X/Y/Z): %d/%d/%d",
                                                ChatColor.AQUA + "", onPlacedLoc.getBlockX(),
                                                onPlacedLoc.getBlockY(), onPlacedLoc.getBlockZ()));
                                    }
                                } else if (playerBE.countCodenameHolder() == 2) {
                                    if (systemConfig.setLockedRequest(inWorldName, playerBE.removeCodename(),
                                            playerBE.removeCodename(), onPlacedLoc)) {
                                        player.sendMessage(String.format("%sLocking the Resource Spawner on (X/Y/Z): %d/%d/%d",
                                                ChatColor.AQUA + "", onPlacedLoc.getBlockX(),
                                                onPlacedLoc.getBlockY(), onPlacedLoc.getBlockZ()));
                                    }
                                }
                                playerBE.setTeamChoice(null);
                            }
                        } else if (playerBE.getRequiredAmountLoc() == 2) {
                            // With any other block set location buffer
                            playerBE.addLocationBuffer(block.getLocation());
                            if (playerBE.countLocationsBuffer() >= 2) {
                                systemConfig.setAreaBuff(inWorldName, teamName, playerBE.removeLocationBuffer(),
                                        playerBE.removeLocationBuffer());
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
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        String inWorldName = player.getWorld().getName();
        // Check if player in world builder
        if (systemConfig.getWorldNames().contains(inWorldName)) {
            if (!player.hasPermission("sleepywar.builder"))
                event.setCancelled(true);
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

    @EventHandler
    public void onArmorStandReact(PlayerArmorStandManipulateEvent event) {

    }
}
