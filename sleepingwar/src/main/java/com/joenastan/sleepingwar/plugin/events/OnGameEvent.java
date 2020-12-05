package com.joenastan.sleepingwar.plugin.events;

import com.joenastan.sleepingwar.plugin.events.CustomEvents.*;
import com.joenastan.sleepingwar.plugin.game.SleepingRoom;
import com.joenastan.sleepingwar.plugin.game.GameManager;
import com.joenastan.sleepingwar.plugin.game.TeamGroupMaker;
import com.joenastan.sleepingwar.plugin.SleepingWarsPlugin;
import com.joenastan.sleepingwar.plugin.enumtypes.TimelineEventType;
import com.joenastan.sleepingwar.plugin.utility.UsefulStaticFunctions;
import com.joenastan.sleepingwar.plugin.utility.CustomDerivedEntity.PlayerBedwarsEntity;
import com.joenastan.sleepingwar.plugin.utility.Timer.StartCountdownTimer;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Bed;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OnGameEvent implements Listener {

    private final GameManager gameManager = SleepingWarsPlugin.getGameManager();
    private final Map<String, PlayerBedwarsEntity> disconnectionHandler = new HashMap<String, PlayerBedwarsEntity>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String inWorldName = event.getPlayer().getWorld().getName();
        Player player = event.getPlayer();
        SleepingRoom room = gameManager.getRoom(inWorldName);
        PlayerBedwarsEntity playerEnt = disconnectionHandler.remove(player.getName());
        // Check if player enter the gameplay world
        if (room != null && playerEnt != null) {
            playerEnt.setPlayer(player);
            room.playerEnter(player, playerEnt);
            room.checkRemainingTeam();
        } else if (room == null && playerEnt != null) {
            playerEnt.setPlayer(player);
            playerEnt.returnEntity();
        }
    }

    @EventHandler
    public void onPlayerExit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String inWorldName = event.getPlayer().getWorld().getName();
        SleepingRoom room = gameManager.getRoom(inWorldName);
        // Check if player exit from the gameplay world
        if (room != null) {
            PlayerBedwarsEntity playerEnt = room.playerLeave(player);
            room.checkRemainingTeam();
            if (!playerEnt.isLeavingUsingCommand())
                disconnectionHandler.put(player.getName(), playerEnt);
            else
                playerEnt.returnEntity();
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        int slot = event.getRawSlot();
        Inventory onInv = event.getClickedInventory();
        HumanEntity entPlayer = event.getWhoClicked();
        String inWorldName = entPlayer.getWorld().getName();
        // Change menu by clicking on item, only if player in Bedwars world able to do this
        if (gameManager.getRoomMap().containsKey(inWorldName) && onInv != null) {
            if (onInv.getType() != InventoryType.PLAYER && entPlayer instanceof Player) {
                Player player = (Player)entPlayer;
                ItemStack clickedItem = onInv.getItem(slot);
                TeamGroupMaker team = gameManager.getRoomMap().get(inWorldName).findTeam(player);
                if (clickedItem != null)
                    team.selectInventoryMenu(player, onInv, event.getView(), clickedItem, slot);
                event.setCancelled(true);
            }
            if (event.getSlotType() == SlotType.ARMOR)
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityGotDamage(EntityDamageEvent event) {
        Entity ent = event.getEntity();
        if (ent instanceof Player) {
            Player player = (Player)ent;
            String inWorldName = player.getWorld().getName();
            SleepingRoom room = gameManager.getRoom(inWorldName);
            if (room != null) {
                DamageCause cause = event.getCause();
                // Instant death on hitting the void
                if (cause.equals(DamageCause.VOID)) {
                    player.damage(event.getDamage() * 100);
                    return;
                }
                // Prevent armor broken
                PlayerInventory playerInv = player.getInventory();
                ItemStack[] armorSet = playerInv.getArmorContents();
                for (ItemStack armorStack : armorSet) {
                    if (armorStack != null) {
                        ItemMeta armorMeta = armorStack.getItemMeta();
                        if (armorMeta instanceof Damageable) {
                            Damageable dmgMeta = (Damageable)armorMeta;
                            dmgMeta.setDamage(0);
                        }
                        armorStack.setItemMeta(armorMeta);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDropEvent(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        Item droppedItem = event.getItemDrop();
        SleepingRoom room = gameManager.getRoom(player.getWorld().getName());
        if (room != null) {
            if (UsefulStaticFunctions.isSword(droppedItem.getItemStack().getType()))
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        String inWorldName = player.getWorld().getName();
        Block block = event.getBlock();
        SleepingRoom room = gameManager.getRoom(inWorldName);
        if (room != null) {
            if (!room.isGameProcessing()) {
                event.setCancelled(true);
            } else {
                if (UsefulStaticFunctions.isMaterialBed(block.getType())) {
                    //System.out.println("[DEBUG] Bed Broken");
                    TeamGroupMaker bedTeamDestroyed = null;
                    event.setDropItems(false);
                    for (TeamGroupMaker t : room.getTeams()) {
                        Block bedBlockTeam = t.getTeamBedLocation().getBlock();
                        Bed thisTeamBed = UsefulStaticFunctions.isMaterialBed(bedBlockTeam.getType()) ? (Bed)bedBlockTeam.getBlockData() : null;
                        if (thisTeamBed == null)
                            continue;
                        Block headBedTeam = bedBlockTeam.getRelative(thisTeamBed.getFacing(), 1);
                        // Check equal
                        if (headBedTeam.equals(block) || bedBlockTeam.equals(block)) {
                            bedTeamDestroyed = t;
                            break;
                        }
                    }
                    if (bedTeamDestroyed != null) {
                        TeamGroupMaker byTeam = room.findTeam(player);
                        if (byTeam.equals(bedTeamDestroyed)) {
                            player.sendMessage(ChatColor.RED + "You can't destroy your own bed.");
                            event.setCancelled(true);
                        } else {
                            //System.out.println("[DEBUG] Bed Destroyed");
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

    @EventHandler
    public void onPlacingBlock(BlockPlaceEvent event) {
        Block block = event.getBlock();
        String inWorldName = block.getLocation().getWorld().getName();
        SleepingRoom room = gameManager.getRoom(inWorldName);
        // Check put something in gameplay world
        if (room != null) {
            // Check if player put TNT explosion in game world or builder world
            if (block.getType() == Material.TNT) {
                block.setType(Material.AIR);
                Location middleBlockLoc = new Location(block.getWorld(), block.getLocation().getX() + 0.5d, 
                        block.getLocation().getY() + 0.5d, block.getLocation().getZ() + 0.5d);
                block.getWorld().spawn(middleBlockLoc, TNTPrimed.class);
            }
            // Any other else
            else {
                room.putBlock(block);
            }
        }
    }

    @EventHandler
    public void onExplodeEvent(EntityExplodeEvent event) {
        List<Block> blockList = event.blockList();
        String inWorldName = event.getLocation().getWorld().getName();
        SleepingRoom room = gameManager.getRoom(inWorldName);
        if (room != null) {
            for (Block b : blockList) {
                if (room.destroyBlock(b)) {
                    if(Math.random() < 0.5d) {
                        World inWorld = b.getLocation().getWorld();
                        inWorld.dropItemNaturally(b.getLocation(), new ItemStack(b.getType(), 1));
                    }
                    b.setType(Material.AIR);
                }
            }
            blockList.clear();
        }
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();
        String inWorldName = player.getWorld().getName(); 
        SleepingRoom room = gameManager.getRoom(inWorldName);
        // Check if player is killed by someone or death
        if (room != null) {
            Player killer = event.getEntity().getKiller();
            if (room.isGameProcessing()) {
                TeamGroupMaker teamVictim = room.findTeam(player);
                teamVictim.reviving(player);
                BedwarsGamePlayerDeathEvent e = new BedwarsGamePlayerDeathEvent(player, room, room.findTeam(player), killer);
                Bukkit.getServer().getPluginManager().callEvent(e);
            } else {
                player.teleport(room.getQueueLocation());
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String inWorldName = player.getWorld().getName();
        SleepingRoom room = gameManager.getRoom(inWorldName);
        if (room != null) {
            TeamGroupMaker team = room.findTeam(player);
            // If and only if the game room is currently progress
            if (room.isGameProcessing() && team != null) {
                team.sendTeamMessage(event.getMessage());
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block blockInteract = event.getClickedBlock();
        SleepingRoom room = gameManager.getRoom(event.getPlayer().getWorld().getName());
        if (room != null && blockInteract != null && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Material blockMat = blockInteract.getType();
            if (UsefulStaticFunctions.isTrapDoor(blockMat) || UsefulStaticFunctions.isStandardDoor(blockMat) || 
                    UsefulStaticFunctions.isFenceGate(blockMat)) {
                if (!room.checkInteraction(event.getPlayer(), blockInteract) || !room.isGameProcessing())
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
        SleepingRoom room = gameManager.getRoom(inWorldName);
        if (room != null) {
            TeamGroupMaker team = room.findTeam(player);
            if (entType == EntityType.VILLAGER && room != null && team != null) {
                String customName = event.getRightClicked().getCustomName();
                // Upgrade Villager
                if (customName.equals("Bedwars Upgrade Villager"))
                    team.openUpgradeMenu(player);
                // Shop Villager
                else if (customName.equals("Bedwars Shop Villager"))
                    team.openShopMenu(player, "Main Menu");
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
                    if (customName.equalsIgnoreCase("Bedwars Upgrade Villager"))
                        liveEnt.addPotionEffect(slowEffect);
                    // Shop Villager
                    else if (customName.equalsIgnoreCase("Bedwars Shop Villager"))
                        liveEnt.addPotionEffect(slowEffect);
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
        SleepingRoom room = gameManager.getRoom(inWorldName);
        if (room != null && entGotHit instanceof LivingEntity) {
            LivingEntity entLiveGotHit = (LivingEntity)entGotHit;
            // Cause by any explosion
            if (cause == DamageCause.ENTITY_EXPLOSION) {
                if (entLiveGotHit.getType() == EntityType.VILLAGER)
                    event.setCancelled(true);
            }
            // Cause by any attack by something or somebody
            else if (cause == DamageCause.ENTITY_ATTACK) {
                if (entLiveGotHit.getType() == EntityType.VILLAGER)
                    event.setCancelled(true);
                if (entGotHit instanceof Player && entDamager instanceof Player) {
                    Player playerGotHit = (Player)entGotHit;
                    Player playerDamager = (Player)entDamager;
                    TeamGroupMaker teamVictim = room.findTeam(playerGotHit);
                    TeamGroupMaker teamKiller = room.findTeam(playerDamager);
                    if (!room.isGameProcessing() || teamVictim.equals(teamKiller)) {
                        event.setCancelled(true);
                    }
                }
            }
            // Cause by any explosion
            else if (cause == DamageCause.ENTITY_SWEEP_ATTACK) {
                if (entLiveGotHit.getType() == EntityType.VILLAGER)
                    event.setCancelled(true);
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
        TeamGroupMaker teamKiller = room.findTeam(killer);
        // Check if the killer is null
        if (killer == null) {
            room.roomBroadcast(String.format("%s %sdied", UsefulStaticFunctions.getColorString(team.getTeamColorPrefix()) + player.getName(), 
                    ChatColor.WHITE + ""));
        } else {
            room.roomBroadcast(String.format("%s %shas been killed by %s", UsefulStaticFunctions.getColorString(team.getTeamColorPrefix()) + 
                    player.getName(), ChatColor.WHITE + "", (teamKiller == null ? ChatColor.WHITE + "" : 
                    UsefulStaticFunctions.getColorString(teamKiller.getTeamColorPrefix())) + killer.getName()));
        }
        // if Bed is broken
        if (team.isBedBroken()) {
            room.roomBroadcast(String.format("%s %shas been eliminated.", UsefulStaticFunctions.getColorString(team.getTeamColorPrefix()) + 
                    player.getName(), ChatColor.WHITE + ""));
            // Delete recent scoreboard score
            String recentScoreboardScore = String.format("%s%d %s", UsefulStaticFunctions.getColorString(team.getTeamColorPrefix()),
                    team.getRemainingPlayers() + 1, team.getName());
            room.getScoreboard().resetScores(recentScoreboardScore);
        }
            
        // Check remaining teams
        room.checkRemainingTeam();
    }

    @EventHandler
    public void onGameStarted(BedwarsGameStartEvent event) {
        SleepingRoom room = event.getRoom();
        StartCountdownTimer counter = new StartCountdownTimer(6f, room);
        counter.start();
    }

    @EventHandler
    public void onTeamUpgrade(BedwarsGameOnUpgradeEvent event) {
        TeamGroupMaker team = event.getTeam();
        Player player = event.getPlayer();
        ItemStack upgrade = event.getUpgradeItem();
        team.sendTeamMessage(ChatColor.AQUA + player.getName() + " has just upgraded " + ChatColor.GOLD + upgrade.getItemMeta().getDisplayName());
        for (Player p : team.getPlayers())
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
    }

    @EventHandler
    public void onGameEnded(BedwarsGameEndedEvent event) {
        SleepingRoom room = event.getRoom();
        TeamGroupMaker team = event.getWinnerTeam();
        room.roomBroadcast("Bedwars winner team: " + team.getName());
    }

    @EventHandler
    public void onRoomTimelineUpdate(BedwarsGameTimelineEvent event) {
        TimelineEventType typeEvent = event.getEventType();
        SleepingRoom room = event.getRoom();
        if (typeEvent == TimelineEventType.DIAMOND_UPGRADE) {
            room.roomBroadcast(ChatColor.AQUA + "Diamond Generator has been upgraded!");
        } else if (typeEvent == TimelineEventType.EMERALD_UPGRADE) {
            room.roomBroadcast(ChatColor.GREEN + "Emerald Generator has been upgraded!");
        }
    }

    @EventHandler
    public void onBedDestroyed(BedwarsGameBedDestroyedEvent event) {
        TeamGroupMaker victim = event.getVictim();
        SleepingRoom room = victim.getRoom();
        for (Player p : room.getWorld().getPlayers()) {
            p.playSound(p.getLocation(), Sound.ENTITY_WITHER_DEATH, 1f, 1f);
            if (victim.getPlayers().contains(p))
                p.sendTitle("Bed Destroyed", ChatColor.YELLOW + "You are unable to respawn", 10, 45, 5);
            else
                p.sendTitle("Bed Destroyed", String.format("Team %s's bed got destroyed by %s", 
                        UsefulStaticFunctions.getColorString(victim.getTeamColorPrefix()) + victim.getName() + ChatColor.WHITE + "", 
                        UsefulStaticFunctions.getColorString(event.getTeamDestroyer().getTeamColorPrefix()) + event.byWho().getName()), 
                        10, 45, 5);
        }
    }
}
