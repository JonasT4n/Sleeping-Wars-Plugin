package com.joenastan.sleepingwar.plugin.game;

import com.joenastan.sleepingwar.plugin.SleepingWarsPlugin;
import com.joenastan.sleepingwar.plugin.game.InventoryMenus.BedwarsShopMenus;
import com.joenastan.sleepingwar.plugin.game.InventoryMenus.BedwarsUpgradeMenus;
import com.joenastan.sleepingwar.plugin.utility.GameSystemConfig;
import com.joenastan.sleepingwar.plugin.utility.UsefulStaticFunctions;
import com.joenastan.sleepingwar.plugin.utility.CustomDerivedEntity.PlayerBedwarsEntity;
import com.joenastan.sleepingwar.plugin.utility.Timer.AreaEffectTimer;
import com.joenastan.sleepingwar.plugin.utility.Timer.PlayerReviveTimer;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamGroupMaker {

    private final GameSystemConfig systemConfig = SleepingWarsPlugin.getGameSystemConfig();

    // Attributes
    private String teamName;
    private Location teamSpawnPoint;
    private Location teamBedLocation;
    private float respawnTime;
    private int eliminetedCount = 0;
    private AreaEffectTimer bufferZone;
    private String teamColorPrefix;
    private SleepingRoom inRoom;
    private BedwarsUpgradeMenus upgradeMenu;
    private BedwarsShopMenus shopMenu;

    // Maps and Lists
    private Map<PlayerBedwarsEntity, PlayerReviveTimer> playersNTimer = new HashMap<PlayerBedwarsEntity, PlayerReviveTimer>();
    private Map<String, Integer> permaLevelData = new HashMap<String, Integer>();
    private List<ResourceSpawner> resourceSpawners = new ArrayList<ResourceSpawner>();

    /**
     * Create team group, you need players to be able to create a team
     * @param inRoom Currently in room
     * @param teamName Name of team
     * @param players List of selected players
     */
    public TeamGroupMaker(SleepingRoom inRoom, String teamName, List<PlayerBedwarsEntity> players) {
        // Assign attributes
        this.teamName = teamName;
        this.inRoom = inRoom;
        teamSpawnPoint = systemConfig.getTeamSpawnLoc(inRoom.getWorld(), inRoom.getMapName(), teamName);
        teamBedLocation = systemConfig.getTeamBedLocation(inRoom.getWorld(), inRoom.getMapName(), teamName);
        respawnTime = 5f;
        teamColorPrefix = systemConfig.getTeamColorPrefix(inRoom.getMapName(), teamName);
        // Initialize team upgrade menu and shop menu
        upgradeMenu = new BedwarsUpgradeMenus(this);
        shopMenu = new BedwarsShopMenus(this);
        // Initialize players in team
        for (PlayerBedwarsEntity pbent : players) {
            pbent.getPlayer().sendMessage("You are in team [" + UsefulStaticFunctions.getColorString(teamColorPrefix) + teamName + ChatColor.WHITE + "]");
            playersNTimer.put(pbent, new PlayerReviveTimer(respawnTime, pbent.getPlayer(), this, teamSpawnPoint));
            pbent.getPlayer().teleport(teamSpawnPoint);
            pbent.getPlayer().setGameMode(GameMode.SURVIVAL);
            setStarterPack(pbent.getPlayer());
        }
        // Get all owned by team resource spawners
        resourceSpawners = systemConfig.getWorldResourceSpawners(inRoom.getWorld(), inRoom.getMapName(), teamName);
        bufferZone = systemConfig.getBufferZoneCoroutine(inRoom.getWorld(), inRoom.getMapName(), this);
    }

    /**
     * When player revive or the game started, then setup the starter items and equipments to player
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
        for (ItemStack leatherArmorItemStack : leatherArmorPack) {
            ItemMeta leatherArmorMeta = leatherArmorItemStack.getItemMeta();
            ((LeatherArmorMeta)leatherArmorMeta).setColor(getPureColor());
            shopMenu.checkUpgrade(leatherArmorItemStack);
            leatherArmorItemStack.setItemMeta(leatherArmorMeta);
        }
        // Empty player inventory and set to starter pack
        PlayerInventory playerInv = player.getInventory();
        playerInv.clear();
        playerInv.setArmorContents(leatherArmorPack);
        // Create wooden sword
        ItemStack woodenSword = new ItemStack(Material.WOODEN_SWORD);
        if (permaLevelData.get(BedwarsUpgradeMenus.SHARPER_BLADE) - 1 != 0) {
            ItemMeta wsMeta = woodenSword.getItemMeta();
            wsMeta.addEnchant(Enchantment.DAMAGE_ALL, permaLevelData.get(BedwarsUpgradeMenus.SHARPER_BLADE) - 1, true);
            woodenSword.setItemMeta(wsMeta);
        }
        playerInv.setItem(0, woodenSword);
    }

    /**
     * Upgrade team something by 1
     * @param upgradeName The name of an upgrade from BedwarsUpgradeMenus constants
     */
    public void teamUpgrade(String upgradeName) {
        // Upgrade
        int currentLvl = permaLevelData.get(upgradeName);
        permaLevelData.put(upgradeName, currentLvl + 1);
        // Effect all player in team
        if (upgradeName.equals(BedwarsUpgradeMenus.SHARPER_BLADE)) {
            for (PlayerBedwarsEntity pbent : playersNTimer.keySet()) {
                PlayerInventory pInv = pbent.getPlayer().getInventory();
                for (int i = 0; i < pInv.getSize(); i++) {
                    ItemStack pItem = pInv.getItem(i);
                    if (pItem == null)
                        continue;
                    if (UsefulStaticFunctions.isSword(pItem.getType()))
                        shopMenu.checkUpgrade(pItem);
                }
            }
        } else if (upgradeName.equals(BedwarsUpgradeMenus.MINE_A_HOLIC)) {
            for (PlayerBedwarsEntity pbent : playersNTimer.keySet()) {
                PlayerInventory pInv = pbent.getPlayer().getInventory();
                for (int i = 0; i < pInv.getSize(); i++) {
                    ItemStack pItem = pInv.getItem(i);
                    if (pItem == null)
                        continue;
                    Material itemType = pItem.getType();
                    if (UsefulStaticFunctions.isAxe(itemType) || itemType == Material.SHEARS || UsefulStaticFunctions.isPickaxe(itemType))
                        shopMenu.checkUpgrade(pItem);
                }
            }
        } else if (upgradeName.equals(BedwarsUpgradeMenus.MAKE_IT_RAIN)) {
            for (ResourceSpawner rspEntry : resourceSpawners) {
                // TODO: Create Generic upgrades, custom percentage upgrade
                // 25% duration reduction
                rspEntry.setSpawnInterval(rspEntry.getSecondsPerSpawn() - (rspEntry.getSecondsPerSpawn() * 25/100));
            }
        } else if (upgradeName.equals(BedwarsUpgradeMenus.HOLY_LIGHT)) {
            bufferZone.setEffect(new PotionEffect(PotionEffectType.HEAL, 6, 1));
        } else if (upgradeName.equals(BedwarsUpgradeMenus.TOUGH_SKIN) || upgradeName.equals(BedwarsUpgradeMenus.EYE_FOR_AN_EYE)) {
            for (PlayerBedwarsEntity pbent : playersNTimer.keySet()) {
                ItemStack[] armorPack = pbent.getPlayer().getInventory().getArmorContents();
                for (ItemStack armorStack : armorPack)
                    shopMenu.checkUpgrade(armorStack);
            }
        } else if (upgradeName.equals(BedwarsUpgradeMenus.GIFT_FOR_THE_POOR)) {
            // TODO: Easter Egg
        }
    }

    /**
     * Reviving player from any death, this function is extremely useful
     * @param player Death body
     */
    public void reviving(Player player) {
        // Check if bed is already broken, then the person has been eliminated
        if (!isBedBroken()) {
            for (PlayerBedwarsEntity pbent : playersNTimer.keySet()) {
                if (pbent.getPlayer().equals(player))
                    playersNTimer.get(pbent).start();
            }
        } else {
            eliminetedCount++;
        }
        // Immediately set player to normal fresh from spawn form
        player.setHealth(20d);
        player.teleport(inRoom.getQueueLocation());
        player.setGameMode(GameMode.SPECTATOR);
    }

    /**
     * Open an upgrade menu, team progress only.
     * @param player Player who gonna open upgrade menu
     */
    public void openUpgradeMenu(Player player) {
        upgradeMenu.openMenu(player, "Upgrade Menu");
    }

    /**
     * Open a shop menu, owned by team only.
     * @param player
     */
    public void openShopMenu(Player player, String shopName) {
        shopMenu.openMenu(player, shopName);
    }

    /**
     * When player is buying an item in any bedwars menu.
     * @param player Player who selected it
     * @param menu Inventory as a menu
     * @param menuView The view inventory menu
     * @param selectedItem Item which selected by player
     * @param slotIndex Slot index selected
     */
    public void selectInventoryMenu(Player player, Inventory menu, InventoryView menuView, ItemStack selectedItem, int slotIndex) {
        String menuTitle = ChatColor.stripColor(menuView.getTitle());
        if (menuTitle.equals("Upgrade Menu")) {
            upgradeMenu.selectedSlot(player, menu, slotIndex);
        } else if (shopMenu.isBedwarsShopMenu(menuView)) {
            ItemMeta itemMeta = selectedItem.getItemMeta();
            if (shopMenu.openMenu(player, ChatColor.stripColor(itemMeta.getDisplayName())))
                return;
            shopMenu.selectedSlot(player, menu, slotIndex);
        }
    }

    /**
     * Usage of this function only if the game is in progress. Handle a player which trying to reconnect into the game.
     * @param player Who just reconnected
     * @return True if player reconnected and join back to player's team, else then false
     */
    public boolean playerReconnectedHandler(Player player) {
        if (!isTeamEliminated()) {
            inRoom.roomBroadcast(UsefulStaticFunctions.getColorString(getTeamColorPrefix()) + player.getName() + ChatColor.WHITE + " reconnected to the game.");
        } else {
            player.sendMessage(ChatColor.BLUE + "Your team recently has been eliminated.");
            return false;
        }
        // Search the entity
        PlayerBedwarsEntity pent = null;
        for (PlayerBedwarsEntity pbent : playersNTimer.keySet())
            if (pbent.getPlayer().equals(player) || pbent.getPlayerName().equals(player.getName())) {
                pent = pbent;
                break;
            }
        // Assign entity
        if (pent != null) {
            pent.setPlayer(player);
            playersNTimer.get(pent).changePlayer(player);
            eliminetedCount--;
            // Delete recent scoreboard score
            String recentScoreboardScore = String.format("%s%d %s", UsefulStaticFunctions.getColorString(teamColorPrefix),
                    getRemainingPlayers() - 1, teamName);
            inRoom.getScoreboard().resetScores(recentScoreboardScore);
            return true;
        } 
        return false;
    }

    /**
     * Handle player disconnected from the server.
     * @param playerEnt Player entity reference
     */
    public void playerDisconnectedHandler(PlayerBedwarsEntity playerEnt) {
        // Check if player disconnected from the game by command
        if (playerEnt.isLeavingUsingCommand()) {
            playersNTimer.remove(playerEnt).stop();
            inRoom.roomBroadcast(String.format("%s leave the game.", UsefulStaticFunctions.getColorString(teamColorPrefix) + 
                    playerEnt.getPlayer().getName()));
        } else {
            inRoom.roomBroadcast(String.format("%s disconnected from the game.", UsefulStaticFunctions.getColorString(teamColorPrefix) + 
                    playerEnt.getPlayer().getName()));
        }
        eliminetedCount++;
        // Delete recent scoreboard score
        String recentScoreboardScore = String.format("%s%d %s", UsefulStaticFunctions.getColorString(teamColorPrefix),
                getRemainingPlayers() + 1, teamName);
        inRoom.getScoreboard().resetScores(recentScoreboardScore);
    }

    /**
     * Activate resource spawners in team.
     * @param active
     */
    public void activateRS(boolean active) {
        for (ResourceSpawner rs : resourceSpawners) {
            if (rs.isRunning() == active)
                continue;
            rs.isRunning(active);
        }
    }

    /**
     * Set activation on team area effect buffer zone.
     * @param active Set active
     */
    public void setRunningEffectBZ(boolean active) {
        if (active == true) {
            bufferZone.start();
        } else {
            bufferZone.stop();
        }
    }

    /**
     * Send a message locally in team only
     * @param msg The message that will be send
     */
    public void sendTeamMessage(String msg) {
        for (PlayerBedwarsEntity pbent : playersNTimer.keySet())
            pbent.getPlayer().sendMessage(UsefulStaticFunctions.getColorString(teamColorPrefix) + "[" + teamName + "] " + ChatColor.WHITE + msg);
    }

    /**
     * @return Map of permanent levels
     */
    public Map<String, Integer> getPermaLevels() {
        return permaLevelData;
    }

    /**
     * Raw color prefix in string.
     */
    public String getTeamColorPrefix() {
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
        return playersNTimer.size() - eliminetedCount;
    }

    /**
     * All players in team, this list of player not associated with team's data, only a copy of it.
     */
    public List<Player> getPlayers() {
        List<Player> listPlayer = new ArrayList<Player>();
        for (PlayerBedwarsEntity pbent : playersNTimer.keySet())
            listPlayer.add(pbent.getPlayer());
        return listPlayer;
    }

    /**
     * List of player entities. This function is not derived from the object data.
     */
    public List<PlayerBedwarsEntity> getPlayerEntities() {
        List<PlayerBedwarsEntity> listEntity = new ArrayList<PlayerBedwarsEntity>();
        listEntity.addAll(playersNTimer.keySet());
        return listEntity;
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
            return Color.fromRGB(255,223,0);
        } else if (teamColorPrefix.equalsIgnoreCase("gray")) {
            return Color.GRAY;
        } else { // Default is White
            return Color.WHITE;
        }
    }

    /**
     * Check if team is already has been eliminated
     * @return true if yes, else then false
     */
    public boolean isTeamEliminated() {
        return eliminetedCount == playersNTimer.size();
    }

    /**
     * Check bed broken on team's bed location.
     * @return true if already broken
     */
    public boolean isBedBroken() {
        Block block = teamBedLocation.getBlock();
        return !UsefulStaticFunctions.isMaterialBed(block.getType());
    }
}
