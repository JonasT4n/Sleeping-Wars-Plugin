package com.joenastan.sleepingwars.events;

import com.joenastan.sleepingwars.enumtypes.InGameFlags;
import com.joenastan.sleepingwars.events.CustomEvents.*;
import com.joenastan.sleepingwars.game.InventoryMenus.BedwarsShopMenu;
import com.joenastan.sleepingwars.game.InventoryMenus.BedwarsUpgradeMenu;
import com.joenastan.sleepingwars.game.ItemPrice.PricesItems;
import com.joenastan.sleepingwars.game.SleepingRoom;
import com.joenastan.sleepingwars.game.GameManager;
import com.joenastan.sleepingwars.game.TeamGroupMaker;
import com.joenastan.sleepingwars.SleepingWarsPlugin;
import com.joenastan.sleepingwars.enumtypes.TimelineEventType;
import com.joenastan.sleepingwars.utility.CustomEntity.ButtonCommandEntity;
import com.joenastan.sleepingwars.utility.DataFiles.GameButtonHolder;
import com.joenastan.sleepingwars.utility.DataFiles.GameSystemConfig;
import com.joenastan.sleepingwars.utility.PluginStaticFunc;
import com.joenastan.sleepingwars.utility.CustomEntity.PlayerBedwarsEntity;
import com.joenastan.sleepingwars.timercoro.StartCountdownTimer;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Bed;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class OnGameEvent implements Listener {

    private final GameSystemConfig systemConfig = SleepingWarsPlugin.getGameSystemConfig();
    private final GameManager gameManager = GameManager.instance;
    private final Map<UUID, PlayerBedwarsEntity> disconnectionHandler = new HashMap<>();
    private final Map<TNTPrimed, PlayerBedwarsEntity> customTNTHit = new HashMap<>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        SleepingRoom room = gameManager.getRoom(player.getWorld().getName());
        PlayerBedwarsEntity playerEnt = disconnectionHandler.remove(player.getUniqueId());

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
        SleepingRoom room = gameManager.getRoom(player.getWorld().getName());

        // Check if player exit from the gameplay world
        if (room != null) {
            PlayerBedwarsEntity playerEnt = room.playerLeave(player);
            if (room.isGameProcessing()) {
                if (!playerEnt.isLeavingUsingCommand())
                    disconnectionHandler.put(player.getUniqueId(), playerEnt);
                room.checkRemainingTeam();
            } else {
                if (playerEnt != null)
                    playerEnt.returnEntity();
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory onInv = event.getClickedInventory();
        HumanEntity humanEnt = event.getWhoClicked();
        SleepingRoom room = gameManager.getRoom(humanEnt.getWorld().getName());

        // Change menu by clicking on item, only if player in Bedwars world able to do this
        if (room != null && onInv != null && humanEnt instanceof Player) {
            int slot = event.getRawSlot();
            ItemStack clickedItem = onInv.getItem(slot);
            Player player = (Player) humanEnt;
            TeamGroupMaker team = room.findTeam(player);
            BedwarsShopMenu shopMenu = room.getShopMenu();
            InventoryView view = event.getView();

            // Check restriction
            if (team.getUpgradeInventory().equals(view.getTopInventory()) ||
                    shopMenu.getShopMenus().contains(view.getTopInventory()))
                event.setCancelled(true);

            // Disable moving item from and to the bedwars menu
            // Check the item selection
            if (clickedItem != null) {
                // Check what's to select in menu
                if (shopMenu.isBedwarsShopMenu(view)) {
                    ItemMeta i_clickedMeta = clickedItem.getItemMeta();
                    if (i_clickedMeta != null)
                        if (shopMenu.openMenu(player, i_clickedMeta.getDisplayName()))
                            return;
                    PricesItems tag = shopMenu.selectedSlot(onInv, slot);
                    if (tag != null)
                        shopMenu.buyItem(player, tag, team);
                } else if (BedwarsUpgradeMenu.MENU_NAME.equals(view.getTitle())) {
                    team.selectUpgrade(player, onInv, clickedItem, slot);
                }
            }

            // Check if moving inventory armor slot is enabled
            if (event.getSlotType().equals(InventoryType.SlotType.ARMOR) &&
                    room.getFlag(InGameFlags.ARMOR_RESTRICTION))
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropEvent(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        Item droppedItem = event.getItemDrop();
        SleepingRoom room = gameManager.getRoom(player.getWorld().getName());
        if (room != null) {
            // TODO: Add into flag
            // NEVER EVER DROP YOUR SWORD
            if (PluginStaticFunc.isSword(droppedItem.getItemStack().getType()))
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        SleepingRoom room = gameManager.getRoom(player.getWorld().getName());
        if (room != null) {
            Block block = event.getBlock();
            if (!room.isGameProcessing()) {
                event.setCancelled(true);
            } else {
                if (PluginStaticFunc.isBed(block.getType())) {
                    TeamGroupMaker bedTeamDestroyed = null;
                    for (TeamGroupMaker t : room.getTeams()) {
                        Block bedBlockTeam = t.getTeamBedLocation().getBlock();
                        Bed thisTeamBed = PluginStaticFunc.isBed(bedBlockTeam.getType()) ?
                                (Bed) bedBlockTeam.getBlockData() : null;
                        if (thisTeamBed == null)
                            continue;
                        Block headBedTeam;
                        if (thisTeamBed.getPart() == Bed.Part.FOOT) {
                            headBedTeam = bedBlockTeam.getRelative(thisTeamBed.getFacing(), 1);
                        } else {
                            headBedTeam = bedBlockTeam;
                            bedBlockTeam = bedBlockTeam.getRelative(thisTeamBed.getFacing(), -1);
                        }
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
                            return;
                        }
                        if (byTeam.getPlayerEntities().size() != 0) {
                            BedwarsBedDestroyEvent ev = new BedwarsBedDestroyEvent(player,
                                    bedTeamDestroyed, byTeam);
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
    public void onItemSpawned(ItemSpawnEvent event) {
        Item it = event.getEntity();
        SleepingRoom room = gameManager.getRoom(event.getLocation().getWorld().getName());
        if (room != null) {
            if (PluginStaticFunc.isBed(it.getItemStack().getType()))
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlacingBlock(BlockPlaceEvent event) {
        Block block = event.getBlock();
        SleepingRoom room = gameManager.getRoom(block.getLocation().getWorld().getName());

        // Check put something in gameplay world
        if (room != null) {
            Player player = event.getPlayer();

            // Check if player put TNT explosion in game world or builder world
            if (block.getType() == Material.TNT) {
                PlayerBedwarsEntity playerEnt = room.findPlayer(player);
                block.setType(Material.AIR);
                Location middleBlockLoc = new Location(block.getWorld(), block.getLocation().getX() + 0.5d,
                        block.getLocation().getY() + 0.5d, block.getLocation().getZ() + 0.5d);
                TNTPrimed tnt = block.getWorld().spawn(middleBlockLoc, TNTPrimed.class);
                if (playerEnt != null) {
                    customTNTHit.put(tnt, playerEnt);
                    // Task delay, remove reference from custom tnt hit
                    Bukkit.getScheduler().scheduleSyncDelayedTask(SleepingWarsPlugin.getPlugin(),
                            () -> customTNTHit.remove(tnt), tnt.getFuseTicks() + 200L);
                }
            }

            // Any other else, insert it into breakable blocks
            else
                room.putBlock(block);
        }
    }

    @EventHandler
    public void onExplodeEvent(EntityExplodeEvent event) {
        List<Block> blockList = event.blockList();
        SleepingRoom room = gameManager.getRoom(event.getLocation().getWorld().getName());
        if (room != null) {
            // Ignore block break on unbreakable blocks when explosion occurs
            for (Block b : blockList) {
                if (room.destroyBlock(b)) {
                    if (Math.random() < 0.5d) {
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
        SleepingRoom room = gameManager.getRoom(player.getWorld().getName());

        // Check if player is killed by someone or death
        if (room != null) {
            if (!room.isGameProcessing())
                player.teleport(room.getQueueLocation());
        }
    }

    @EventHandler
    public void onEntityGotDamage(EntityDamageEvent event) {
        Entity ent = event.getEntity();
        if (ent instanceof Player) {
            Player player = (Player) ent;
            SleepingRoom room = gameManager.getRoom(player.getWorld().getName());
            if (room != null) {
                DamageCause cause = event.getCause();

                // Check if player going to die after hit
                if (player.getHealth() - event.getDamage() < 1) {
                    // Check if the game is already ended or not
                    if (room.isGameEnded()) {
                        player.teleport(room.getQueueLocation());
                        player.setGameMode(GameMode.SPECTATOR);
                        event.setCancelled(true);
                    }

                    // When the game is still on going
                    else {
                        // Check if player have Totem Undying on hand slot
                        ItemStack handHold = player.getInventory().getItemInOffHand();
                        if (handHold.getType() == Material.TOTEM_OF_UNDYING) {
                            handHold.setAmount(handHold.getAmount() - 1);
                            TeamGroupMaker team = room.findTeam(player);
                            if (cause.equals(DamageCause.VOID) && team != null) {
                                player.teleport(team.getTeamSpawnLocation());
                            } else {
                                player.playEffect(EntityEffect.TOTEM_RESURRECT);
                                player.setHealth(10d);
                            }
                        } else {
                            if (room.reviving(player)) {
                                PlayerBedwarsEntity playerEnt = room.findPlayer(player);
                                if (playerEnt != null) {
                                    BedwarsPlayerDeathEvent e = new BedwarsPlayerDeathEvent(room, playerEnt,
                                            playerEnt.getLastHitBy());
                                    Bukkit.getPluginManager().callEvent(e);
                                }
                                event.setCancelled(true);
                            }
                        }
                    }
                }

                // Instant death on hitting the void
                if (cause.equals(DamageCause.VOID)) {
                    player.damage(event.getDamage() * 100);
                    return;
                }

                // Prevent armor broken when got hit
                PlayerInventory playerInv = player.getInventory();
                ItemStack[] armorSet = playerInv.getArmorContents();
                for (ItemStack armorStack : armorSet) {
                    if (armorStack != null) {
                        ItemMeta armorMeta = armorStack.getItemMeta();
                        if (armorMeta instanceof Damageable) {
                            Damageable dmgMeta = (Damageable) armorMeta;
                            dmgMeta.setDamage(0);
                        }
                        armorStack.setItemMeta(armorMeta);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityHitEntity(EntityDamageByEntityEvent event) {
        DamageCause cause = event.getCause();
        Entity entGotHit = event.getEntity();
        Entity entHitter = event.getDamager();
        SleepingRoom room = gameManager.getRoom(entGotHit.getWorld().getName());
        if (room != null && entGotHit instanceof LivingEntity) {
            LivingEntity entLiveGotHit = (LivingEntity) entGotHit;

            // Ignore any damage on Villager
            if (entLiveGotHit.getType() == EntityType.VILLAGER) {
                event.setCancelled(true);
                return;
            }

            // Cause by any explosion
            if (cause == DamageCause.ENTITY_EXPLOSION) {

                // Choose players that took the explosion damage
                if (entHitter instanceof TNTPrimed && entGotHit instanceof Player) {
                    TNTPrimed tnt = (TNTPrimed)entHitter;
                    PlayerBedwarsEntity playerEntVictim = room.findPlayer((Player)entGotHit);

                    // Assign last hit
                    if (customTNTHit.containsKey(tnt)) {
                        PlayerBedwarsEntity tntOwner = customTNTHit.get(tnt);
                        if (tntOwner.getTeamChoice().equals(playerEntVictim.getTeamChoice())) {
                            event.setCancelled(true);
                            return;
                        }
                        playerEntVictim.setLastHitBy(tntOwner);
                    }
                }
            }

            // Cause by any attack by something or somebody
            else if (cause == DamageCause.ENTITY_ATTACK || cause == DamageCause.ENTITY_SWEEP_ATTACK) {
                if (entGotHit instanceof Player && entHitter instanceof Player) {
                    PlayerBedwarsEntity playerGotHit = room.findPlayer((Player)entGotHit);
                    PlayerBedwarsEntity playerHitter = room.findPlayer((Player)entHitter);
                    TeamGroupMaker teamVictim = room.findTeam(playerGotHit.getPlayer());
                    TeamGroupMaker teamKiller = room.findTeam(playerHitter.getPlayer());
                    if (!room.isGameProcessing() || teamVictim.equals(teamKiller))
                        event.setCancelled(true);
                    else
                        playerGotHit.setLastHitBy(playerHitter);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        SleepingRoom room = gameManager.getRoom(player.getWorld().getName());
        if (room != null) {
            // Chat goes back to normal when the game is ended
            if (room.isGameEnded())
                return;

            // Manage chat restrictions
            PlayerBedwarsEntity playerEnt = room.findPlayer(player);
            if (playerEnt != null) {
                if (playerEnt.isEliminated()) {
                    room.roomBroadcast(String.format("%s: %s", player.getName(), ChatColor.GRAY +
                            event.getMessage()), true);
                    event.setCancelled(true);
                    return;
                }

                // Check if and only if the game room is currently progress
                TeamGroupMaker team = room.findTeam(player);
                if (room.isGameProcessing() && team != null) {
                    team.sendTeamMessage(String.format("%s: %s", player.getName(), event.getMessage()));
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block blockInteract = event.getClickedBlock();
            String inWorldName = event.getPlayer().getWorld().getName();
            SleepingRoom room = gameManager.getRoom(inWorldName);
            if (room != null && blockInteract != null) {
                Material blockMat = blockInteract.getType();

                // Check if player press command button in room when the game is not started yet
                if (!room.isGameProcessing() && PluginStaticFunc.isButton(blockMat)) {
                    if (GameButtonHolder.buttons.containsKey(inWorldName)) {
                        Map<Block, ButtonCommandEntity> buttonEnt = GameButtonHolder.buttons.get(inWorldName);
                        ButtonCommandEntity ent = buttonEnt.get(blockInteract);
                        if (ent != null)
                            ent.pressButton(event.getPlayer());
                        return;
                    }
                }

                // Check if player trying to unlock locked entity
                if (PluginStaticFunc.isGateOrDoor(blockMat) || PluginStaticFunc.isButton(blockMat) ||
                        blockMat == Material.LEVER) {
                    if (!room.checkInteraction(event.getPlayer(), blockInteract) || !room.isGameProcessing())
                        event.setCancelled(true);
                }
            } else if (room == null && !systemConfig.getWorldNames().contains(inWorldName) && blockInteract != null){
                // Check if player press button handler outside the bedwars world
                if (PluginStaticFunc.isButton(blockInteract.getType())) {
                    if (GameButtonHolder.buttons.containsKey(inWorldName)) {
                        Map<Block, ButtonCommandEntity> buttonEnt = GameButtonHolder.buttons.get(inWorldName);
                        for (Block button : buttonEnt.keySet()) {
                            if (blockInteract.equals(button))
                                buttonEnt.get(button).pressButton(event.getPlayer());
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerEntityInteract(PlayerInteractEntityEvent event) {
        // Custom Villager Egg Spawn
        EntityType entType = event.getRightClicked().getType();
        Player player = event.getPlayer();
        SleepingRoom room = gameManager.getRoom(player.getWorld().getName());
        if (room != null) {
            // Restrict interaction when the game ended
            if (room.isGameEnded())
                return;

            // Checks interactions with a special mobs in game
            TeamGroupMaker team = room.findTeam(player);
            String customName = event.getRightClicked().getCustomName();
            if (entType == EntityType.VILLAGER && team != null && customName != null) {
                // Upgrade Villager
                if (customName.equals(BedwarsUpgradeMenu.VILLAGER_NAME_TAG))
                    team.openUpgradeMenu(player);

                // Shop Villager
                else if (customName.equals(BedwarsShopMenu.VILLAGER_NAME_TAG))
                    room.getShopMenu().openMenu(player, "Main Shop");
            }
        }
    }

    @EventHandler
    public void onPlayerRegen(EntityRegainHealthEvent event) {
        Entity ent = event.getEntity();
        if (ent instanceof Player) {
            Player player = (Player)ent;
            SleepingRoom room = gameManager.getRoom(player.getWorld().getName());
            if (room != null) {
                PlayerBedwarsEntity playerEnt = room.findPlayer(player);
                if (room.isGameProcessing() && playerEnt != null && player.getHealth() >= 20)
                    playerEnt.setLastHitBy(null);
            }
        }
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity ent = event.getEntity();
        EntityType entType = event.getEntityType();
        if (ent instanceof LivingEntity && entType == EntityType.VILLAGER) {
            LivingEntity liveEnt = ((LivingEntity) ent);
            String customName = event.getEntity().getCustomName();
            if (customName != null) {
                PotionEffect slowEffect = new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 9999);

                // Upgrade Villager
                if (customName.equalsIgnoreCase(ChatColor.GOLD + String.format("BW %sUpgrade",
                        ChatColor.LIGHT_PURPLE + "")))
                    liveEnt.addPotionEffect(slowEffect);

                // Shop Villager
                else if (customName.equalsIgnoreCase(ChatColor.GOLD + String.format("BW %sShop",
                        ChatColor.LIGHT_PURPLE + "")))
                    liveEnt.addPotionEffect(slowEffect);
            }
        }
    }

    // Game Events are all down here
    @EventHandler
    public void onGamePlayerDeath(BedwarsPlayerDeathEvent event) {
        // Get information
        SleepingRoom room = event.getRoom();
        PlayerBedwarsEntity playerVictim = event.getVictim();
        TeamGroupMaker teamVictim = room.findTeam(playerVictim.getTeamChoice());
        PlayerBedwarsEntity killer = event.getKiller();
        TeamGroupMaker teamKiller = killer == null ? null : room.findTeam(killer.getTeamChoice());
        // Check if there's no killer
        if (killer != null & teamKiller != null) {
            room.roomBroadcast(String.format("%s has been killed by %s", PluginStaticFunc.stringToChatColor(teamVictim
                    .getRawColor()) + playerVictim.getPlayer().getName() + ChatColor.WHITE, (PluginStaticFunc
                    .stringToChatColor(teamKiller.getRawColor())) + killer.getPlayer().getName()));
        } else {
            room.roomBroadcast(String.format("%s died", PluginStaticFunc.stringToChatColor(teamVictim
                    .getRawColor()) + playerVictim.getPlayer().getName() + ChatColor.WHITE));
        }
        // Check remaining teams
        room.checkRemainingTeam();
    }

    @EventHandler
    public void onGameStarted(BedwarsStartEvent event) {
        SleepingRoom room = event.getRoom();
        if (room.isStarting())
            return;
        if (room.getPlayersInRoom().size() <= 1) {
            room.roomBroadcast(ChatColor.BLUE + "You can't play alone, you need at least 2 person in room.");
            return;
        }
        room.setStarting(true);
        StartCountdownTimer counter = new StartCountdownTimer(5f, room);
        counter.start();
    }

    @EventHandler
    public void onTeamUpgrade(BedwarsTeamUpgradeEvent event) {
        TeamGroupMaker team = event.getTeam();
        Player player = event.getPlayer();
        ItemMeta upgrade = event.getMetaData();
        team.sendTeamMessage(String.format("%s has just upgraded %s", PluginStaticFunc.stringToChatColor(team
                .getRawColor()) + player.getName() + ChatColor.AQUA, ChatColor.GOLD + upgrade.getDisplayName()));
        for (Player p : team.getPlayers())
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
    }

    @EventHandler
    public void onGameEnded(BedwarsEndedEvent event) {
        SleepingRoom room = event.getRoom();
        TeamGroupMaker team = event.getWinnerTeam();
        room.roomBroadcast(ChatColor.WHITE + " ");
        room.roomBroadcast("Bedwars winner team: " + PluginStaticFunc
                .stringToChatColor(team.getRawColor()) + team.getName());
        room.roomBroadcast(ChatColor.WHITE + " ");
    }

    @EventHandler
    public void onRoomTimelineUpdate(BedwarsTimelineEvent event) {
        TimelineEventType typeEvent = event.getEventType();
        SleepingRoom room = event.getRoom();

        // TODO: Enable custom event message
        // Check if there's a custom message of event
        if (event.getEventMessage() != null) {
            room.roomBroadcast(event.getEventMessage());
            if (typeEvent == TimelineEventType.DESTROY_ALL_BED)
                for (Player p : room.getPlayersInRoom())
                    p.playSound(p.getLocation(), Sound.BLOCK_SMITHING_TABLE_USE, 1f, 1f);
            return;
        }

        // Go default
        if (typeEvent == TimelineEventType.DIAMOND_UPGRADE) {
            room.roomBroadcast(ChatColor.AQUA + "Diamond Generator has been upgraded!");
        } else if (typeEvent == TimelineEventType.EMERALD_UPGRADE) {
            room.roomBroadcast(ChatColor.GREEN + "Emerald Generator has been upgraded!");
        } else if (typeEvent == TimelineEventType.DESTROY_ALL_BED) {
            room.roomBroadcast(ChatColor.WHITE + " ");
            room.roomBroadcast(ChatColor.GOLD + "All beds has been destroyed.");
            room.roomBroadcast(ChatColor.WHITE + " ");
            for (Player p : room.getPlayersInRoom())
                p.playSound(p.getLocation(), Sound.BLOCK_SMITHING_TABLE_USE, 0.5f, 0.1f);
        } else if (typeEvent == TimelineEventType.WORLD_SHRINKING) {
            room.roomBroadcast(ChatColor.WHITE + " ");
            room.roomBroadcast(ChatColor.RED + "[CAUTION!]");
            room.roomBroadcast(ChatColor.RED + "World start shrinking!");
            room.roomBroadcast(ChatColor.WHITE + " ");
        }
    }

    @EventHandler
    public void onBedDestroyed(BedwarsBedDestroyEvent event) {
        TeamGroupMaker victim = event.getVictim();
        SleepingRoom room = victim.getRoom();
        for (Player p : room.getWorld().getPlayers()) {
            p.playSound(p.getLocation(), Sound.ENTITY_WITHER_DEATH, 1f, 1f);
            if (victim.getPlayers().contains(p))
                p.sendTitle("Bed Destroyed", ChatColor.YELLOW + "You are unable to respawn", 10, 45, 5);
            else
                p.sendTitle("Bed Destroyed", String.format("Team %s's bed got destroyed by %s", PluginStaticFunc
                        .stringToChatColor(victim.getRawColor()) + victim.getName() + ChatColor.WHITE, PluginStaticFunc
                        .stringToChatColor(event.getTeamDestroyer().getRawColor()) + event.byWho().getName()),
                        10, 45, 5);
        }
    }

    @EventHandler
    public void onBuyItemInShop(BedwarsBuyEvent event) {
        // Do Nothing
    }

    @EventHandler
    public void onPlayerEliminated(BedwarsPlayerEliminatedEvent event) {
        SleepingRoom room = event.getRoom();
        PlayerBedwarsEntity player = event.getVictim();
        TeamGroupMaker team = event.getTeam();
        room.roomBroadcast(String.format("%s has been eliminated.", PluginStaticFunc.stringToChatColor(team
                .getRawColor()) + player.getPlayer().getName() + ChatColor.WHITE));
    }
}
