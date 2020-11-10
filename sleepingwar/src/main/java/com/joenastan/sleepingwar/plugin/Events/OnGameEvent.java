package com.joenastan.sleepingwar.plugin.Events;

import com.joenastan.sleepingwar.plugin.SleepingWarsPlugin;
import com.joenastan.sleepingwar.plugin.Events.CustomEvents.BedwarsGameEnded;
import com.joenastan.sleepingwar.plugin.Events.CustomEvents.BedwarsGamePlayerDeath;
import com.joenastan.sleepingwar.plugin.Events.CustomEvents.BedwarsGamePlayerJoin;
import com.joenastan.sleepingwar.plugin.Events.CustomEvents.BedwarsGamePlayerLeave;
import com.joenastan.sleepingwar.plugin.Events.CustomEvents.BedwarsGameStart;
import com.joenastan.sleepingwar.plugin.Events.Tasks.PostEndedGame;
import com.joenastan.sleepingwar.plugin.Game.BedwarsMenus;
import com.joenastan.sleepingwar.plugin.Game.GameManager;
import com.joenastan.sleepingwar.plugin.Game.SleepingRoom;
import com.joenastan.sleepingwar.plugin.Game.TeamGroupMaker;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.md_5.bungee.api.ChatColor;

public class OnGameEvent implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        int slot = event.getRawSlot();
        Inventory onInv = event.getClickedInventory();
        HumanEntity player = event.getWhoClicked();
        if (player.hasPermission("sleepywar.sleeper") || player.hasPermission("sleepywar.builder")) {
            if (onInv.getType() != InventoryType.PLAYER) {
                if (BedwarsMenus.isBedwarsShopMenu(event.getView()) && slot < 9) {
                    //System.out.println("[DEBUG] " + onInv.getType().getDefaultTitle());
                    ItemStack clickedItem = onInv.getItem(slot);
                    if (clickedItem != null) {
                        ItemMeta clickedItemMeta = clickedItem.getItemMeta();
                        Inventory openInv = BedwarsMenus.getOpenShopMenu(ChatColor.stripColor(clickedItemMeta.getDisplayName()));
                        event.setCancelled(true);
                        player.openInventory(openInv);
                    }
                } else if (BedwarsMenus.isUpgradeMenu(event.getView())) {

                }
            }
        }
    }

    @EventHandler
    public void onGameStarted(BedwarsGameStart event) {
        event.getRoom().gameStart();
    }

    @EventHandler
    public void onGamePlayerJoin(BedwarsGamePlayerJoin event) {
        SleepingRoom room = event.getRoom();
        for (Player p : room.getAllPlayer()) {
            p.sendMessage(ChatColor.GREEN + event.getPlayer().getName() + " joined the party.");
        }
    }

    @EventHandler
    public void onGamePlayerLeave(BedwarsGamePlayerLeave event) {
        SleepingRoom room = event.getRoom();
        for (Player p : room.getAllPlayer()) {
            p.sendMessage(ChatColor.YELLOW + event.getPlayer().getName() + " leave the party.");
        }
    }

    @EventHandler
    public void onGameEnded(BedwarsGameEnded ended) {
        SleepingRoom room = ended.getRoom();
        Bukkit.getScheduler().scheduleSyncDelayedTask(SleepingWarsPlugin.getPlugin(), new PostEndedGame(room), 200L);
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();
        SleepingRoom room = GameManager.getRoomByPlayer(player);
        if (room != null) {
            BedwarsGamePlayerDeath e = new BedwarsGamePlayerDeath(player, room, room.getTeam(player));
            Bukkit.getServer().getPluginManager().callEvent(e);
        }
    }

    @EventHandler
    public void onGamePlayerDeath(BedwarsGamePlayerDeath event) {
        Player player = event.getPlayer();
        player.spigot().respawn();
        player.teleport(event.getRoom().getQueueSpawn());
        
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {

    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {

    }

    @EventHandler
    public void onBlockDestroy(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (player.hasPermission("sleepywar.sleeper")) {
            if (GameManager.getAllPlayerInGame().contains(player)) {
                if (block.getType() != Material.WHITE_WOOL || block.getType() != Material.OAK_PLANKS || 
                    block.getType() != Material.OBSIDIAN || block.getType() != Material.GLASS) 
                {
                    event.setCancelled(true);
                }
            }
        }

        if (!player.hasPermission("sleepywar.builder")) {
            // TODO: On Builder breaking stuff
        }
    }

    @EventHandler
    public void onPlayerEntityInteract(PlayerInteractEntityEvent event) {
        // Custom Villager Egg Spawn
        EntityType entType = event.getRightClicked().getType();
        Player player = event.getPlayer();
        //System.out.println("[DEBUG] " + entType.name());
        if (entType == EntityType.VILLAGER && player.hasPermission("sleepyway.sleeper")) {
            String customName = event.getRightClicked().getCustomName();
            // Upgrade Villager
            if (customName.equalsIgnoreCase("Upgrade Villager")) {
                SleepingRoom room = GameManager.getRoomByPlayer(player);
                if (room != null) {
                    TeamGroupMaker team = room.getTeam(player);
                    if (team != null) {
                        player.openInventory(BedwarsMenus.UpgradeMenu(team));
                        return;
                    }
                }
                player.openInventory(BedwarsMenus.UpgradeMenu());
            } 
            // Shop Villager
            else if (customName.equalsIgnoreCase("Shop Villager")) {
                player.openInventory(BedwarsMenus.getOpenShopMenu("Main Menu"));
            }
        }
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity ent = event.getEntity();
        EntityType entType = event.getEntityType();
        if (ent instanceof LivingEntity) {
            LivingEntity liveEnt = ((LivingEntity)ent);
            if (entType == EntityType.VILLAGER) {
                String customName = event.getEntity().getCustomName();
                PotionEffect slowEffect = new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 9999);
                // Upgrade Villager
                if (customName.equalsIgnoreCase("Upgrade Villager")) {
                    liveEnt.addPotionEffect(slowEffect);
                } 
                // Shop Villager
                else if (customName.equalsIgnoreCase("Shop Villager")) {
                    liveEnt.addPotionEffect(slowEffect);
                }
            }
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {

    }
}
