package com.joenastan.sleepingwar.plugin.events;

import com.joenastan.sleepingwar.plugin.events.CustomEvents.*;
import com.joenastan.sleepingwar.plugin.game.BedwarsMenus;
import com.joenastan.sleepingwar.plugin.game.GameManager;
import com.joenastan.sleepingwar.plugin.game.SleepingRoom;
import com.joenastan.sleepingwar.plugin.game.TeamGroupMaker;
import com.joenastan.sleepingwar.plugin.SleepingWarsPlugin;
import com.joenastan.sleepingwar.plugin.utility.GameSystemConfig;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class OnGameEvent implements Listener {

    private final GameSystemConfig systemConfig = SleepingWarsPlugin.getGameSystemConfig();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        int slot = event.getRawSlot();
        Inventory onInv = event.getClickedInventory();
        HumanEntity player = event.getWhoClicked();
        if ((player.hasPermission("sleepywar.sleeper") || player.hasPermission("sleepywar.builder")) && onInv != null) {
            if (onInv.getType() != InventoryType.PLAYER) {
                if (BedwarsMenus.isBedwarsShopMenu(event.getView()) && slot < 9) {
                    //System.out.println("[DEBUG] " + onInv.getType().getDefaultTitle());
                    ItemStack clickedItem = onInv.getItem(slot);
                    if (clickedItem != null) {
                        ItemMeta clickedItemMeta = clickedItem.getItemMeta();
                        Inventory openInv = BedwarsMenus.getOpenShopMenu(ChatColor.stripColor(clickedItemMeta.getDisplayName()));
                        event.setCancelled(true);

                        assert openInv != null;
                        player.openInventory(openInv);
                    }
                } else if (BedwarsMenus.isUpgradeMenu(event.getView())) {
                    ItemStack clickedItem = onInv.getItem(slot);
                    if (clickedItem != null) {
                        ItemMeta clickedItemMeta = clickedItem.getItemMeta();
                        Inventory openInv = BedwarsMenus.getOpenShopMenu(ChatColor.stripColor(clickedItemMeta.getDisplayName()));
                        event.setCancelled(true);

                        assert openInv != null;
                        player.openInventory(openInv);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        String inWorldName = player.getWorld().getName();
        Block block = event.getBlock();

        // Check if player in world gameplay
        if (GameManager.getAllPlayerInGame().containsKey(player)) {
            //System.out.println("[DEBUG] " + inWorldName);
            if (GameManager.getAllRoom().containsKey(inWorldName)) {
                SleepingRoom room = GameManager.getAllRoom().get(inWorldName);
                if (isMaterialBed(block.getType())) {
                    if (!room.isGameProcessing()) {
                        event.setCancelled(true);
                    } else {
                        TeamGroupMaker bedTeamDestroyed = null;
                        for (TeamGroupMaker t : room.getAllTeams()) {
                            if (t.getTeamBedLocation().equals(block.getLocation()))
                                bedTeamDestroyed = t;
                        }
                        if (bedTeamDestroyed != null) {
                            TeamGroupMaker byTeam = room.getTeam(player);
                            BedwarsGameBedDestroyedEvent ev = new BedwarsGameBedDestroyedEvent(player, bedTeamDestroyed, byTeam);
                            Bukkit.getPluginManager().callEvent(ev);
                        }
                    }
                } else {
                    if (!room.destroyBlock(block))
                        event.setCancelled(true);
                }
            }
            //System.out.println("[DEBUG] " + GameManager.getAllRoom().containsKey(inWorldName));
        }
    }

    @EventHandler
    public void onPlacingBlock(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        String inWorldName = block.getLocation().getWorld().getName();

        // Check put something in builder world
        if (systemConfig.getAllWorldName().contains(inWorldName)) {
            // Check if player put TNT explosion in game world or builder world
            if (block.getType() == Material.TNT) {
                block.setType(Material.AIR);
                block.getWorld().spawn(block.getLocation(), TNTPrimed.class);
            }
        }

        // Check put something in gameplay world
        if (GameManager.getAllPlayerInGame().containsKey(player)) {
            SleepingRoom roomIn = GameManager.getRoomByPlayer(player);
            // Check if player put TNT explosion in game world or builder world
            if (block.getType() == Material.TNT) {
                block.setType(Material.AIR);
                block.getWorld().spawn(block.getLocation(), TNTPrimed.class);
            }
            // Any other else
            else {
                roomIn.putBlock(block);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String inWorldName = event.getPlayer().getWorld().getName();
        Player player = event.getPlayer();

        // Check if player enter the gameplay world
        if (!GameManager.getAllRoom().isEmpty()) {
            if (GameManager.getAllRoom().keySet().contains(inWorldName)) {
                SleepingRoom room = GameManager.getRoomByPlayer(player);
                if (room != null) {
                    if (room.isGameProcessing()) {
                        GameManager.getPlayerInGameList().put(player, inWorldName);
                    }
                }
            }
        }

        // Check if player joined into the world builder
        if (systemConfig.getAllWorldName().contains(inWorldName)) {
            player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
        }
    }

    @EventHandler
    public void onPlayerExit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String inWorldName = event.getPlayer().getWorld().getName();

        // Check if player exit from the gameplay world
        if (!GameManager.getAllRoom().isEmpty()) {
            if (GameManager.getAllRoom().keySet().contains(inWorldName)) {
                SleepingRoom room = GameManager.getRoomByPlayer(player);
                GameManager.getPlayerInGameList().remove(player);

                if (!room.isGameProcessing()) {
                    room.playerLeave(player);
                }
            }
        }
    }

    @EventHandler
    public void onExplodeEvent(EntityExplodeEvent event) {
        List<Block> blockList = event.blockList();
        String inWorldName = event.getLocation().getWorld().getName();

        // Check if player put TNT explosion in game world or builder world
        if (systemConfig.getAllWorldName().contains(inWorldName)) {
            blockList.clear();
        }

        if (GameManager.getAllRoom() != null) {
            if (GameManager.getAllRoom().keySet().contains(inWorldName)) {
                SleepingRoom room = GameManager.getRoomByName(inWorldName);
                for (Block b : blockList) {
                    room.destroyBlock(b);
                }

                blockList.clear();
            }
        }
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();
        SleepingRoom room = GameManager.getRoomByName(player.getWorld().getName());
        if (room != null) {
            if (room.isGameProcessing()) {
                event.setCancelled(true);
                BedwarsGamePlayerDeathEvent e = new BedwarsGamePlayerDeathEvent(player, room, room.getTeam(player));
                Bukkit.getServer().getPluginManager().callEvent(e);
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        // TODO: Chat in Gameplay
    }

    @EventHandler
    public void onPlayerEntityInteract(PlayerInteractEntityEvent event) {
        // Custom Villager Egg Spawn
        EntityType entType = event.getRightClicked().getType();
        Player player = event.getPlayer();
        if (entType == EntityType.VILLAGER) {
            String customName = event.getRightClicked().getCustomName();
            // Upgrade Villager
            if (customName.equalsIgnoreCase("Bedwars Upgrade Villager")) {
                SleepingRoom room = GameManager.getRoomByPlayer(player);
                if (room != null) {
                    TeamGroupMaker team = room.getTeam(player);
                    //System.out.println("[DEBUG] " + team);
                    if (team != null) {
                        player.openInventory(BedwarsMenus.UpgradeMenu(team));
                        //System.out.println("[DEBUG] Opened Menu");
                        return;
                    }
                }
                //System.out.println("[DEBUG] Opened Default Menu");
                player.openInventory(BedwarsMenus.UpgradeMenu());
            }
            // Shop Villager
            else if (customName.equalsIgnoreCase("Bedwars Shop Villager")) {
                player.openInventory(BedwarsMenus.getOpenShopMenu("Main Menu"));
            }
        }
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity ent = event.getEntity();
        EntityType entType = event.getEntityType();
        if (ent instanceof LivingEntity) {
            LivingEntity liveEnt = ((LivingEntity) ent);
            if (entType == EntityType.VILLAGER) {
                String customName = event.getEntity().getCustomName();
                if (customName != null) {
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
    }

    // Game Events are all down here
    @EventHandler
    public void onGamePlayerDeath(BedwarsGamePlayerDeathEvent event) {
        Player player = event.getPlayer();
        TeamGroupMaker team = event.getTeam();
        SleepingRoom room = event.getRoom();
        player.setHealth(20);
        player.teleport(room.getWorldQueueSpawn());
        PotionEffect invisibleEffect = new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 9999);
        player.addPotionEffect(invisibleEffect);

        // Check if team has it's bed
        if (!team.isBedBroken()) {
            player.sendMessage(ChatColor.YELLOW + "You have been killed, reviving in 5 seconds...");
            team.revive(player);
        }
        // else then check player and team is still alive, and check remaining and last team
        else {
            team.addElimination();
            room.roomBroadcast(ChatColor.LIGHT_PURPLE + player.getName() + " has been eliminated.");
            if (team.isAllMemberElimineted()) {
                room.roomBroadcast(ChatColor.DARK_PURPLE + "Team " + team.getName() + " has been elimineted.");
                room.checkRemainingTeam();
            }
        }
    }

    @EventHandler
    public void onPlayerRevive(BedwarsGamePlayerReviveEvent event) {
        Player player = event.getPlayer();
        player.removePotionEffect(PotionEffectType.INVISIBILITY);
    }

    @EventHandler
    public void onGameStarted(BedwarsGameStartEvent event) {
        event.getRoom().gameStart();
    }

    @EventHandler
    public void onGamePlayerJoin(BedwarsGamePlayerJoinEvent event) {
        // SleepingRoom room = event.getRoom();
        // for (Player p : room.getAllPlayer()) {
        //     p.sendMessage(ChatColor.GREEN + event.getPlayer().getName() + " joined the party.");
        // }
    }

    @EventHandler
    public void onGamePlayerLeave(BedwarsGamePlayerLeaveEvent event) {
        // SleepingRoom room = event.getRoom();
        // for (Player p : room.getAllPlayer()) {
        //     p.sendMessage(ChatColor.YELLOW + event.getPlayer().getName() + " leave the party.");
        // }
    }

    @EventHandler
    public void onGameEnded(BedwarsGameEndedEvent ended) {

    }

    @EventHandler
    public void onRoomTimelineUpdate(BedwarsGameTimelineEvent event) {

    }

    @EventHandler
    public void onBedDestroyed(BedwarsGameBedDestroyedEvent event) {

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
