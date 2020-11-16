package com.joenastan.sleepingwar.plugin.events;

import com.joenastan.sleepingwar.plugin.events.CustomEvents.*;
import com.joenastan.sleepingwar.plugin.game.SleepingRoom;
import com.joenastan.sleepingwar.plugin.game.GameManager;
import com.joenastan.sleepingwar.plugin.game.TeamGroupMaker;
import com.joenastan.sleepingwar.plugin.SleepingWarsPlugin;
import com.joenastan.sleepingwar.plugin.utility.GameSystemConfig;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
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
    private final GameManager gameManager = SleepingWarsPlugin.getGameManager();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        int slot = event.getRawSlot();
        Inventory onInv = event.getClickedInventory();
        HumanEntity entPlayer = event.getWhoClicked();
        String inWorldName = entPlayer.getWorld().getName();

        // Change menu by clicking on item, only if player in Bedwars world able to do this
        if ((gameManager.getAllRoom().containsKey(inWorldName) || systemConfig.getAllWorldName().contains(inWorldName)) && onInv != null) {
            if ((onInv.getType() != InventoryType.PLAYER || onInv.getType() == InventoryType.CREATIVE) && entPlayer instanceof HumanEntity) {
                Player player = (Player)entPlayer;
                ItemStack clickedItem = onInv.getItem(slot);
                SleepingRoom room = gameManager.getRoomByName(inWorldName);
                TeamGroupMaker team = room.getTeam(player);
                if (clickedItem != null && team != null) {
                    if (room.getShopMenus().isBedwarsShopMenu(event.getView()) && slot < 9) {
                        ItemMeta clickedItemMeta = clickedItem.getItemMeta();
                        room.getShopMenus().openMenu(player, ChatColor.stripColor(clickedItemMeta.getDisplayName()));
                        event.setCancelled(true);
                    } else if (event.getView().getTitle() == "Upgrade Menu") {
                        team.getUpgradeMenus().selectedSlot(player, onInv, slot);
                        event.setCancelled(true);
                    } else {
                        room.getShopMenus().selectedSlot(player, onInv, slot);
                        event.setCancelled(true);
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

        //System.out.println("[DEBUG] " + inWorldName);
        if (gameManager.getAllRoom().containsKey(inWorldName)) {
            SleepingRoom room = gameManager.getAllRoom().get(inWorldName);
            if (room != null) {
                if (!room.isGameProcessing()) {
                    event.setCancelled(true);
                } else {
                    if (isMaterialBed(block.getType())) {
                        TeamGroupMaker bedTeamDestroyed = null;
                        Location blockLoc = block.getLocation();
                        for (TeamGroupMaker t : room.getAllTeams()) {
                            Location bedTeamLoc = t.getTeamBedLocation();
                            if (blockLoc.getBlockX() == bedTeamLoc.getBlockX() && blockLoc.getBlockY() == bedTeamLoc.getBlockY() && 
                                    blockLoc.getBlockZ() == bedTeamLoc.getBlockZ()) {
                                bedTeamDestroyed = t;
                                break;
                            }
                        }
                        if (bedTeamDestroyed != null) {
                            TeamGroupMaker byTeam = room.getTeam(player);
                            if (byTeam.equals(bedTeamDestroyed)) {
                                player.sendMessage(ChatColor.RED + "You can't destroy your own bed.");
                                event.setCancelled(true);
                            } else {
                                block.getDrops().clear();
                                BedwarsGameBedDestroyedEvent ev = new BedwarsGameBedDestroyedEvent(player, bedTeamDestroyed, byTeam);
                                Bukkit.getPluginManager().callEvent(ev);
                            }
                        }
                    } else if (!room.destroyBlock(block)) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlacingBlock(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        String inWorldName = block.getLocation().getWorld().getName();

        // Check put something in gameplay world
        if (gameManager.getAllRoom().containsKey(inWorldName)) {
            SleepingRoom roomIn = gameManager.getRoomByPlayer(player);
            // Check if player put TNT explosion in game world or builder world
            if (block.getType() == Material.TNT) {
                block.setType(Material.AIR);
                Location middleBlockLoc = new Location(block.getWorld(), block.getLocation().getX() + 0.5d, 
                        block.getLocation().getY() + 0.5d, block.getLocation().getZ() + 0.5d);
                block.getWorld().spawn(middleBlockLoc, TNTPrimed.class);
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
        if (!gameManager.getAllRoom().isEmpty()) {
            if (gameManager.getAllRoom().keySet().contains(inWorldName)) {
                SleepingRoom room = gameManager.getRoomByPlayer(player);
                if (room != null) {
                    if (room.isGameProcessing()) {
                        gameManager.getPlayerInGameList().put(player, inWorldName);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerExit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String inWorldName = event.getPlayer().getWorld().getName();

        // Check if player exit from the gameplay world
        if (!gameManager.getAllRoom().isEmpty()) {
            if (gameManager.getAllRoom().keySet().contains(inWorldName)) {
                SleepingRoom room = gameManager.getRoomByPlayer(player);
                gameManager.getPlayerInGameList().remove(player);

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

        if (!gameManager.getAllRoom().isEmpty()) {
            if (gameManager.getAllRoom().keySet().contains(inWorldName)) {
                SleepingRoom room = gameManager.getRoomByName(inWorldName);
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
        String inWorldName = player.getWorld().getName();
        Player killer = event.getEntity().getKiller();
        SleepingRoom room = gameManager.getRoomByName(inWorldName);
        if (gameManager.getAllRoom().containsKey(inWorldName) && room != null) {
            TeamGroupMaker teamVictim = room.getTeam(player);
            if (room.isGameProcessing()) {
                teamVictim.reviving(player);
                BedwarsGamePlayerDeathEvent e = new BedwarsGamePlayerDeathEvent(player, room, room.getTeam(player), killer);
                Bukkit.getServer().getPluginManager().callEvent(e);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String inWorldName = player.getWorld().getName();
        SleepingRoom room = gameManager.getRoomByName(inWorldName);
        if (room != null) {
            TeamGroupMaker team = room.getTeam(player);
            // If and only if the game room is currently progress
            if (room.isGameProcessing() && team != null) {
                team.sendTeamMessage(event.getMessage());
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerEntityInteract(PlayerInteractEntityEvent event) {
        // Custom Villager Egg Spawn
        EntityType entType = event.getRightClicked().getType();
        Player player = event.getPlayer();
        String inWorldName = player.getWorld().getName();
        if (gameManager.getAllPlayerInGame().containsKey(player) || systemConfig.getAllWorldName().contains(inWorldName)) {
            SleepingRoom room = gameManager.getRoomByName(inWorldName);
            TeamGroupMaker team = room == null ? null : room.getTeam(player);
            if (entType == EntityType.VILLAGER && room != null && team != null) {
                String customName = event.getRightClicked().getCustomName();
                // Upgrade Villager
                if (customName.equals("Bedwars Upgrade Villager")) {
                    team.getUpgradeMenus().openMenu(player, "Upgrade Menu");
                }
                // Shop Villager
                else if (customName.equals("Bedwars Shop Villager")) {
                    room.getShopMenus().openMenu(player, "Main Menu");
                }
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
                    if (customName.equalsIgnoreCase("Bedwars Upgrade Villager")) {
                        liveEnt.addPotionEffect(slowEffect);
                    }
                    // Shop Villager
                    else if (customName.equalsIgnoreCase("Bedwars Shop Villager")) {
                        liveEnt.addPotionEffect(slowEffect);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityHitbyEntity(EntityDamageByEntityEvent event) {
        DamageCause cause = event.getCause();
        Entity entGotHit = event.getEntity();
        Entity entDamager = event.getDamager();
        String inWorldName = entGotHit.getWorld().getName();
        SleepingRoom room = gameManager.getRoomByName(inWorldName);
        if (room != null && entGotHit instanceof LivingEntity) {
            LivingEntity entLiveGotHit = (LivingEntity)entGotHit;
            // Cause by any explosion
            if (cause == DamageCause.ENTITY_EXPLOSION) {
                if (entLiveGotHit.getType() == EntityType.VILLAGER) {
                    event.setCancelled(true);
                }
            }
            // Cause by any attack by something or somebody
            else if (cause == DamageCause.ENTITY_ATTACK) {
                if (entLiveGotHit.getType() == EntityType.VILLAGER) {
                    event.setCancelled(true);
                }
                if (entGotHit instanceof Player && entDamager instanceof Player) {
                    Player playerGotHit = (Player)entGotHit;
                    Player playerDamager = (Player)entDamager;
                    TeamGroupMaker teamVictim = room.getTeam(playerGotHit);
                    TeamGroupMaker teamKiller = room.getTeam(playerDamager);
                    if (!room.isGameProcessing() || teamVictim.equals(teamKiller)) {
                        event.setCancelled(true);
                    }
                }
            }
            // Cause by any explosion
            else if (cause == DamageCause.ENTITY_SWEEP_ATTACK) {
                if (entLiveGotHit.getType() == EntityType.VILLAGER) {
                    event.setCancelled(true);
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
        Player killer = event.getKiller();

        if (killer == null) {
            room.roomBroadcast(String.format("%s%s %sdied", team.getColor(), player.getName(), ChatColor.WHITE + ""));
        } else {
            room.roomBroadcast(String.format("%s%s %shas been killed by %s%s", team.getColor(), player.getName(), ChatColor.WHITE + "", 
                    room.getTeam(killer) == null ? ChatColor.WHITE + "" : room.getTeam(killer).getColor(), killer.getName()));
        }

        // if Bed is broken
        if (team.isBedBroken())
            room.roomBroadcast(String.format("%s%s %shas been eliminated.", team.getColor(), player.getName(), ChatColor.WHITE + ""));

        // Check remaining teams
        room.checkRemainingTeam();
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
    public void onTeamUpgrade(BedwarsGameOnUpgradeEvent event) {
        TeamGroupMaker team = event.getTeam();
        Player player = event.getPlayer();
        ItemStack upgrade = event.getUpgradeItem();
        team.sendTeamMessage(ChatColor.AQUA + player.getName() + " has just Upgraded " + ChatColor.GOLD + upgrade.getItemMeta().getDisplayName());
    }

    @EventHandler
    public void onGameEnded(BedwarsGameEndedEvent ended) {

    }

    @EventHandler
    public void onRoomTimelineUpdate(BedwarsGameTimelineEvent event) {

    }

    @EventHandler
    public void onBedDestroyed(BedwarsGameBedDestroyedEvent event) {
        TeamGroupMaker victim = event.getVictim();
        SleepingRoom room = victim.getRoom();
        room.roomBroadcast(String.format("Team %s%s's bed got destroyed by %s%s", victim.getName(), 
                ChatColor.WHITE + "", event.getTeamDestroyer().getColor(), event.byWho().getName()));
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
