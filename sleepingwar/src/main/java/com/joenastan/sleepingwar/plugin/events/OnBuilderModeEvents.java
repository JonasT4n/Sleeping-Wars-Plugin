package com.joenastan.sleepingwar.plugin.events;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joenastan.sleepingwar.plugin.SleepingWarsPlugin;
import com.joenastan.sleepingwar.plugin.utility.GameSystemConfig;
import com.joenastan.sleepingwar.plugin.utility.PlayerBedwarsEntity;

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
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import net.md_5.bungee.api.ChatColor;

public class OnBuilderModeEvents implements Listener {

    private final GameSystemConfig systemConfig = SleepingWarsPlugin.getGameSystemConfig();
    private static Map<Player, PlayerBedwarsEntity> customBuilderEntity = new HashMap<Player, PlayerBedwarsEntity>();
    
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent event) {
        String inWorldName = event.getPlayer().getWorld().getName();
        Player player = event.getPlayer();

        // Check if player joined into the world builder
        if (systemConfig.getAllWorldName().contains(inWorldName)) {
            player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        String inWorldName = event.getTo().getWorld().getName();
        // Check if player teleport from world builder to other world builder, ignore this
        if (systemConfig.getAllWorldName().contains(inWorldName) && systemConfig.getAllWorldName().contains(event.getFrom().getWorld().getName())) {
            return;
        }
        // Check if player teleport into world builder
        else if (systemConfig.getAllWorldName().contains(inWorldName)) {
            customBuilderEntity.put(player, new PlayerBedwarsEntity(player, event.getFrom(), player.getGameMode()));
            player.setGameMode(GameMode.CREATIVE);
        }
        // Check if player leave from world builder
        else if (systemConfig.getAllWorldName().contains(event.getFrom().getWorld().getName())) {
            customBuilderEntity.remove(player);
            if (player.hasPermission("sleepywar.builder")) {
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
        if (systemConfig.getAllWorldName().contains(inWorldName)) {
            if (customBuilderEntity.containsKey(player)) {
                customBuilderEntity.remove(player).returnEntity();
                systemConfig.Save();
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        String inWorldName = player.getWorld().getName();
        Block block = event.getBlockPlaced();

        // Check if player exit from the world builder
        if (systemConfig.getAllWorldName().contains(inWorldName)) {
            if (customBuilderEntity.containsKey(player) && isMaterialBed(block.getType())) {
                String teamName = customBuilderEntity.get(player).getTeamChoice();
                if (teamName != null) {
                    Location onPlacedLoc = block.getLocation();
                    player.sendMessage(ChatColor.DARK_AQUA + "Bed for team " + teamName + 
                            String.format(" has been settled on X:%d Y:%d Z:%d.", onPlacedLoc.getBlockX(), onPlacedLoc.getBlockY(), onPlacedLoc.getBlockZ()));
                    Location existingBed = systemConfig.getBedLocationMap(inWorldName).get(teamName);
                    if (isMaterialBed(existingBed.getBlock().getType()))
                        existingBed.getBlock().setType(Material.AIR);

                    systemConfig.getBedLocationMap(inWorldName).put(teamName, onPlacedLoc);
                    customBuilderEntity.get(player).setTeamChoice(null);
                }
            }
        }

        // Check put something in builder world
        if (systemConfig.getAllWorldName().contains(inWorldName)) {
            // Check if player put TNT explosion in game world or builder world
            if (block.getType() == Material.TNT) {
                block.setType(Material.AIR);
                Location middleBlockLoc = new Location(block.getWorld(), block.getLocation().getX() + 0.5d, 
                        block.getLocation().getY() + 0.5d, block.getLocation().getZ() + 0.5d);
                block.getWorld().spawn(middleBlockLoc, TNTPrimed.class);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        String inWorldName = player.getWorld().getName();

        // Check if player in world builder
        if (systemConfig.getAllWorldName().contains(inWorldName)) {
            if (!player.hasPermission("sleepywar.builder"))
                event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onExplodeEvent(EntityExplodeEvent event) {
        List<Block> blockList = event.blockList();
        String inWorldName = event.getLocation().getWorld().getName();

        // Check if player put TNT explosion in game world or builder world
        if (systemConfig.getAllWorldName().contains(inWorldName)) {
            blockList.clear();
        }
    }

    public static Map<Player, PlayerBedwarsEntity> getCustomBuilderEntity() {
        return customBuilderEntity;
    }

    public static void clearStatic() {
        for (Map.Entry<Player, PlayerBedwarsEntity> builderEntry : customBuilderEntity.entrySet()) {
            builderEntry.getValue().returnEntity();
        }
        customBuilderEntity.clear();
        customBuilderEntity = null;
    }

    private boolean isMaterialBed(Material material) {
        switch (material) {
            case BLACK_BED:
                return true;
            case BLUE_BED:
                return true;
            case BROWN_BED:
                return true;
            case CYAN_BED:
                return true;
            case GREEN_BED:
                return true;
            case YELLOW_BED:
                return true;
            case RED_BED:
                return true;
            case ORANGE_BED:
                return true;
            case GRAY_BED:
                return true;
            case LIGHT_BLUE_BED:
                return true;
            case LIME_BED:
                return true;
            case PINK_BED:
                return true;
            case MAGENTA_BED:
                return true;
            case PURPLE_BED:
                return true;
            case WHITE_BED:
                return true;
            case LIGHT_GRAY_BED:
                return true;
            default:
                return false;
        }
    }
}
