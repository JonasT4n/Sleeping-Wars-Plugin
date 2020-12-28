package com.joenastan.sleepingwars.game;

import com.joenastan.sleepingwars.SleepingWarsPlugin;
import com.joenastan.sleepingwars.events.CustomEvents.BedwarsTeamUpgradeEvent;
import com.joenastan.sleepingwars.game.InventoryMenus.BedwarsUpgradeMenu;
import com.joenastan.sleepingwars.game.ItemPrice.PricetagItems;
import com.joenastan.sleepingwars.utility.DataFiles.GameSystemConfig;
import com.joenastan.sleepingwars.timercoro.AreaEffectTimer;
import com.joenastan.sleepingwars.utility.PluginStaticColor;
import com.joenastan.sleepingwars.utility.PluginStaticFunc;
import com.joenastan.sleepingwars.utility.CustomDerivedEntity.PlayerBedwarsEntity;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TeamGroupMaker {

    // Attributes
    private final String teamName;
    private final Location teamSpawnPoint;
    private final Location teamBedLocation;
    private final AreaEffectTimer bufferZone;
    private final String teamColorPrefix;
    private final SleepingRoom inRoom;
    private final BedwarsUpgradeMenu upgradeMenu;

    // Maps and Lists
    private final Map<String, Integer> perLevelData = new HashMap<>();
    private final List<ResourceSpawner> resourceSpawners;
    private final Map<UUID, PlayerBedwarsEntity> playerEntities = new HashMap<>();

    /**
     * Create team group, you need players to be able to create a team
     *
     * @param inRoom   Currently in room
     * @param teamName Name of team
     */
    public TeamGroupMaker(SleepingRoom inRoom, String teamName) {
        // Assign attributes
        this.teamName = teamName;
        this.inRoom = inRoom;
        GameSystemConfig systemConfig = SleepingWarsPlugin.getGameSystemConfig();
        teamSpawnPoint = systemConfig.getTeamSpawner(inRoom.getWorld(), inRoom.getMapName(), teamName);
        teamBedLocation = systemConfig.getBedLocation(inRoom.getWorld(), inRoom.getMapName(), teamName);
        teamColorPrefix = systemConfig.getRawColor(inRoom.getMapName(), teamName);
        // Initialize team upgrade menu and shop menu
        upgradeMenu = new BedwarsUpgradeMenu(this);
        // Get all owned by team resource spawners
        resourceSpawners = systemConfig.getWorldRS(inRoom.getWorld(), inRoom.getMapName(), teamName);
        bufferZone = systemConfig.getAreaBuffRoutine(inRoom.getWorld(), inRoom.getMapName(), this);
    }

    /**
     * When player revive or the game started, then setup the starter items and equipments to player
     *
     * @param player Selected player
     */
    public void setStarterPack(Player player) {
        // Setting up leather armor
        ItemStack[] leatherArmorPack = {
                new ItemStack(Material.LEATHER_BOOTS, 1),
                new ItemStack(Material.LEATHER_LEGGINGS, 1),
                new ItemStack(Material.LEATHER_CHESTPLATE, 1),
                new ItemStack(Material.LEATHER_HELMET, 1)
        };
        // Set Colors and upgrades of armor
        for (ItemStack i_armor : leatherArmorPack) {
            ItemMeta leatherArmorMeta = i_armor.getItemMeta();
            ((LeatherArmorMeta) leatherArmorMeta).setColor(getPureColor());
            // Upgrade by level
            if (perLevelData.get(BedwarsUpgradeMenu.TOUGH_SKIN) - 1 != 0) {
                leatherArmorMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, perLevelData
                        .get(BedwarsUpgradeMenu.TOUGH_SKIN) - 1, true);
            }
            if (perLevelData.get(BedwarsUpgradeMenu.EYE_FOR_AN_EYE) - 1 != 0) {
                leatherArmorMeta.addEnchant(Enchantment.THORNS, perLevelData
                        .get(BedwarsUpgradeMenu.TOUGH_SKIN) - 1, true);
            }
            i_armor.setItemMeta(leatherArmorMeta);
        }
        // Empty player inventory and set to starter pack
        PlayerInventory playerInv = player.getInventory();
        playerInv.clear();
        playerInv.setArmorContents(leatherArmorPack);
        // Create wooden sword
        ItemStack woodenSword = new ItemStack(Material.WOODEN_SWORD);
        if (perLevelData.get(BedwarsUpgradeMenu.SHARPER_BLADE) - 1 != 0) {
            ItemMeta wsMeta = woodenSword.getItemMeta();
            wsMeta.addEnchant(Enchantment.DAMAGE_ALL, perLevelData
                    .get(BedwarsUpgradeMenu.SHARPER_BLADE) - 1,true);
            woodenSword.setItemMeta(wsMeta);
        }
        playerInv.setItem(0, woodenSword);
    }

    /**
     * Upgrade team something by 1
     *
     * @param upgradeName The name of an upgrade from BedwarsUpgradeMenus constants
     */
    public void teamUpgrade(String upgradeName) {
        // Upgrade
        int currentLvl = perLevelData.get(upgradeName);
        perLevelData.put(upgradeName, currentLvl + 1);
        // Effect all player in team
        switch (upgradeName) {
            case BedwarsUpgradeMenu.SHARPER_BLADE:
                for (PlayerBedwarsEntity pbent : playerEntities.values()) {
                    PlayerInventory pInv = pbent.getPlayer().getInventory();
                    for (int i = 0; i < pInv.getSize(); i++) {
                        ItemStack pItem = pInv.getItem(i);
                        if (pItem == null)
                            continue;
                        if (PluginStaticFunc.isSword(pItem.getType())) {
                            // Enhance each player sword
                            ItemMeta i_swordMeta = pItem.getItemMeta();
                            i_swordMeta.addEnchant(Enchantment.DAMAGE_ALL, perLevelData
                                    .get(BedwarsUpgradeMenu.SHARPER_BLADE) - 1,true);
                            pItem.setItemMeta(i_swordMeta);
                        }
                    }
                }
                break;

            case BedwarsUpgradeMenu.MINE_A_HOLIC:
                for (PlayerBedwarsEntity pbent : playerEntities.values()) {
                    PlayerInventory pInv = pbent.getPlayer().getInventory();
                    for (int i = 0; i < pInv.getSize(); i++) {
                        ItemStack pItem = pInv.getItem(i);
                        if (pItem == null)
                            continue;
                        Material itemType = pItem.getType();
                        if (PluginStaticFunc.isAxe(itemType) || itemType == Material.SHEARS ||
                                PluginStaticFunc.isPickaxe(itemType)) {
                            ItemMeta i_toolMeta = pItem.getItemMeta();
                            i_toolMeta.addEnchant(Enchantment.DIG_SPEED, perLevelData
                                    .get(BedwarsUpgradeMenu.MINE_A_HOLIC) - 1, true);
                            pItem.setItemMeta(i_toolMeta);
                        }
                    }
                }
                break;

            case BedwarsUpgradeMenu.MAKE_IT_RAIN:
                for (ResourceSpawner rspEntry : resourceSpawners) {
                    // TODO: Create Generic upgrades, custom percentage upgrade
                    // 25% duration reduction
                    rspEntry.setSpawnInterval(rspEntry.getSecondsPerSpawn() -
                            (rspEntry.getSecondsPerSpawn() * 25 / 100));
                }
                break;

            case BedwarsUpgradeMenu.HOLY_LIGHT:
                bufferZone.setEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 0));
                bufferZone.start();
                break;

            case BedwarsUpgradeMenu.TOUGH_SKIN:
                for (PlayerBedwarsEntity pbent : playerEntities.values()) {
                    ItemStack[] armorPack = pbent.getPlayer().getInventory().getArmorContents();
                    for (ItemStack i_armorStack : armorPack) {
                        ItemMeta i_armorMeta = i_armorStack.getItemMeta();
                        i_armorMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, perLevelData
                                .get(BedwarsUpgradeMenu.TOUGH_SKIN) - 1, true);
                        i_armorStack.setItemMeta(i_armorMeta);
                    }
                }
                break;

            case BedwarsUpgradeMenu.EYE_FOR_AN_EYE:
                for (PlayerBedwarsEntity pbent : playerEntities.values()) {
                    ItemStack[] armorPack = pbent.getPlayer().getInventory().getArmorContents();
                    for (ItemStack i_armorStack : armorPack) {
                        ItemMeta i_armorMeta = i_armorStack.getItemMeta();
                        i_armorMeta.addEnchant(Enchantment.THORNS, perLevelData
                                .get(BedwarsUpgradeMenu.EYE_FOR_AN_EYE) - 1, true);
                        i_armorStack.setItemMeta(i_armorMeta);
                    }
                }
                break;

            case BedwarsUpgradeMenu.GIFT_FOR_THE_POOR:
                // TODO: Easter Egg
                break;
        }
    }

    /**
     * Insert Player into team.
     * @param playerEnt Player entity
     */
    public void insertPlayer(PlayerBedwarsEntity playerEnt) {
        playerEnt.getPlayer().sendMessage(String.format("You are in team %s[%s]",
                PluginStaticColor.getColorString(teamColorPrefix), teamName));
        playerEnt.getPlayer().teleport(teamSpawnPoint);
        playerEnt.getPlayer().setGameMode(GameMode.SURVIVAL);
        playerEntities.put(playerEnt.getPlayer().getUniqueId(), playerEnt);
        setStarterPack(playerEnt.getPlayer());
    }

    /**
     * Remove player from team.
     * @param playerEnt Player entity
     */
    public void removePlayer(PlayerBedwarsEntity playerEnt) {
        playerEntities.remove(playerEnt.getPlayer().getUniqueId());
        // Scoreboard sync
        String recentScore = String.format("%s%d %s", PluginStaticColor
                .getColorString(teamColorPrefix), playerEntities.size() + 1, teamName);
        inRoom.getScoreboard().resetScores(recentScore);
    }

    /**
     * Open an upgrade menu, team progress only.
     *
     * @param player Player who gonna open upgrade menu
     */
    public void openUpgradeMenu(Player player) {
        upgradeMenu.openUpgradeMenu(player, "Upgrade Menu");
    }

    /**
     * When player is buying an item in any bedwars menu.
     *
     * @param player       Player who selected it
     * @param menu         Inventory as a menu
     * @param selectedItem Item which selected by player
     * @param slotIndex Slot index selected
     */
    public void selectUpgrade(@Nonnull Player player, @Nonnull Inventory menu,
                              ItemStack selectedItem, int slotIndex) {
        PricetagItems selectedUpgradeTag = upgradeMenu.selectedSlot(menu, slotIndex);
        if (selectedUpgradeTag != null) {
            if (upgradeMenu.chooseUpgrade(player, selectedUpgradeTag, selectedItem, this)) {
                BedwarsTeamUpgradeEvent event = new BedwarsTeamUpgradeEvent(this, player,
                        selectedItem.getItemMeta());
                Bukkit.getPluginManager().callEvent(event);
            }
        }
    }

    /**
     * Usage of this function only if the game is in progress.
     * Handle a player which trying to reconnect into the game.
     * Also it fix the scoreboard.
     *
     * @param playerEnt Who just reconnected
     */
    public void playerReconnectedHandler(PlayerBedwarsEntity playerEnt) {
        Player player = playerEnt.getPlayer();
        if (isTeamStandStill()) {
            playerEntities.put(player.getUniqueId(), playerEnt);
            inRoom.roomBroadcast(PluginStaticColor.getColorString(getRawColor()) +
                    player.getName() + ChatColor.WHITE + " reconnected to the game.");
            // Scoreboard sync
            String recentScore = String.format("%s%d %s", PluginStaticColor.getColorString(teamColorPrefix),
                    playerEntities.size() - 1, teamName);
            inRoom.getScoreboard().resetScores(recentScore);
        } else {
            player.sendMessage(ChatColor.BLUE + "Your team recently has been eliminated.");
        }
    }

    /**
     * Handle player disconnected from the server.
     * It will fix the Scoreboard and tell other players that this player is disconnected.
     *
     * @param playerEnt Player entity reference
     */
    public void playerDisconnectedHandler(PlayerBedwarsEntity playerEnt) {
        // Check if player disconnected from the game by command
        if (playerEnt.isLeavingUsingCommand()) {
            inRoom.roomBroadcast(String.format("%s leave the game.", PluginStaticColor
                    .getColorString(teamColorPrefix) + playerEnt.getPlayer().getName()));
        } else {
            inRoom.roomBroadcast(String.format("%s disconnected from the game.", PluginStaticColor
                    .getColorString(teamColorPrefix) + playerEnt.getPlayer().getName()));
        }
        playerEntities.remove(playerEnt.getPlayer().getUniqueId());
        // Scoreboard sync
        String recentScore = String.format("%s%d %s", PluginStaticColor.getColorString(teamColorPrefix),
                playerEntities.size() + 1, teamName);
        inRoom.getScoreboard().resetScores(recentScore);
    }

    /**
     * Activate resource spawners in team.
     *
     * @param active Set Active
     */
    public void activateRS(boolean active) {
        for (ResourceSpawner rs : resourceSpawners) {
            if (rs.isRunning() == active)
                continue;
            rs.setRunning(active);
        }
    }

    /**
     * Set activation on team area effect buffer zone.
     *
     * @param active Set active
     */
    public void setRunningEffectBZ(boolean active) {
        if (active) {
            bufferZone.start();
        } else {
            bufferZone.stop();
        }
    }

    /**
     * Send a message locally in team only
     *
     * @param msg The message that will be send
     */
    public void sendTeamMessage(String msg) {
        for (PlayerBedwarsEntity pbent : playerEntities.values())
            pbent.getPlayer().sendMessage(PluginStaticColor.getColorString(teamColorPrefix) +
                    "[" + teamName + "] " + ChatColor.WHITE + msg);
    }

    /**
     * @return Map of permanent levels
     */
    public Map<String, Integer> getPermLevels() {
        return perLevelData;
    }

    /**
     * Raw color prefix in string. For Example: 'blue', 'green', 'yellow', 'red', etc.
     */
    public String getRawColor() {
        return teamColorPrefix;
    }

    /**
     * Every team created by room itself. This refer to the current room the team playing
     */
    public SleepingRoom getRoom() {
        return inRoom;
    }

    /**
     * Get remaining player which still alive.
     */
    public int getRemainingPlayers() {
        return playerEntities.size();
    }

    /**
     * All players in team, this list of player not associated with team's data, only a copy of it.
     */
    public List<Player> getPlayers() {
        List<Player> listPlayer = new ArrayList<>();
        for (PlayerBedwarsEntity pbent : playerEntities.values())
            listPlayer.add(pbent.getPlayer());
        return listPlayer;
    }

    /**
     * List of player entities. This function is not derived from the object data.
     */
    public List<PlayerBedwarsEntity> getPlayerEntities() {
        return new ArrayList<>(playerEntities.values());
    }

    /**
     * @return Team name in string
     */
    public String getName() {
        return teamName;
    }

    /**
     * @return Bed Location
     */
    public Location getTeamBedLocation() {
        return teamBedLocation;
    }

    /**
     * @return Team spawn point
     */
    public Location getTeamSpawnLocation() {
        return teamSpawnPoint;
    }

    /**
     * Get raw color data.
     */
    private Color getPureColor() {
        if (teamColorPrefix.equalsIgnoreCase("blue")) {
            return Color.fromBGR(255, 0, 0);
        } else if (teamColorPrefix.equalsIgnoreCase("green")) {
            return Color.fromRGB(0, 255, 0);
        } else if (teamColorPrefix.equalsIgnoreCase("yellow")) {
            return Color.YELLOW;
        } else if (teamColorPrefix.equalsIgnoreCase("aqua")) {
            return Color.AQUA;
        } else if (teamColorPrefix.equalsIgnoreCase("red")) {
            return Color.fromRGB(255, 0, 0);
        } else if (teamColorPrefix.equalsIgnoreCase("light-purple")) {
            return Color.PURPLE;
        } else if (teamColorPrefix.equalsIgnoreCase("gold")) {
            return Color.fromRGB(255, 223, 0);
        } else if (teamColorPrefix.equalsIgnoreCase("gray")) {
            return Color.GRAY;
        } else { // Default is White
            return Color.WHITE;
        }
    }

    /**
     * Check if team is already has been eliminated
     *
     * @return true if yes, else then false
     */
    public boolean isTeamStandStill() {
        return playerEntities.size() != 0;
    }

    /**
     * Check bed broken on team's bed location.
     *
     * @return true if already broken
     */
    public boolean isBedBroken() {
        Block block = teamBedLocation.getBlock();
        return !PluginStaticFunc.isMaterialBed(block.getType());
    }
}
