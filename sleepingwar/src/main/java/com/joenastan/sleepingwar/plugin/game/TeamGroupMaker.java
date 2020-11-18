package com.joenastan.sleepingwar.plugin.game;

import com.joenastan.sleepingwar.plugin.SleepingWarsPlugin;
import com.joenastan.sleepingwar.plugin.game.InventoryMenus.BedwarsShopMenus;
import com.joenastan.sleepingwar.plugin.game.InventoryMenus.BedwarsUpgradeMenus;
import com.joenastan.sleepingwar.plugin.utility.UsefulStaticFunctions;
import com.joenastan.sleepingwar.plugin.utility.Timer.PlayerReviveTimer;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TeamGroupMaker {

    // Attributes
    private String teamName;
    private String worldOriginalName;
    private Location teamSpawnPoint;
    private Location teamBedLocation;
    private float respawnTime;
    private int eliminetedCount = 0;
    // private Location baseLocationMin;
    // private Location baseLocationMax;
    private String teamPrefix;
    private SleepingRoom inRoom;
    private BedwarsUpgradeMenus upgradeMenu;

    // Maps and Lists
    private Map<Player, PlayerReviveTimer> playersNTimer = new HashMap<Player, PlayerReviveTimer>();
    private Map<String, ResourceSpawner> resourceSpawners = new HashMap<String, ResourceSpawner>();
    private Map<String, Integer> permanentLevels = new HashMap<String, Integer>();

    public TeamGroupMaker(SleepingRoom inRoom, String teamName, String worldOriginalName, List<Player> players, Location spawnPoint, Location bedLocation, String teamPrefix) {
        Location spawnLoc = new Location(inRoom.getWorldQueueSpawn().getWorld(), spawnPoint.getX(), spawnPoint.getY(), spawnPoint.getZ());
        Location bedLoc = new Location(inRoom.getWorldQueueSpawn().getWorld(), bedLocation.getX(), bedLocation.getY(), bedLocation.getZ());
        this.teamName = teamName;
        this.teamSpawnPoint = spawnLoc;
        this.teamPrefix = teamPrefix;
        this.worldOriginalName = worldOriginalName;
        this.teamBedLocation = bedLoc;
        this.inRoom = inRoom;

        // Initialize team upgrade menu
        upgradeMenu = new BedwarsUpgradeMenus(this);

        respawnTime = 5f;
        for (Player p : players) {
            p.sendMessage("You are in team [" + getColor() + teamName + ChatColor.WHITE + "]");
            playersNTimer.put(p, new PlayerReviveTimer(respawnTime, p, this, spawnLoc));
            p.teleport(teamSpawnPoint);
            p.setGameMode(GameMode.SURVIVAL);
            setStarterPack(p);
        }

        for (Map.Entry<String, ResourceSpawner> rsp : SleepingWarsPlugin.getGameSystemConfig().getResourceSpawnersPack(worldOriginalName, teamName).entrySet()) {
            Location resSpawnerLoc = new Location(inRoom.getWorldQueueSpawn().getWorld(), rsp.getValue().getSpawnLocation().getX(),  
                    rsp.getValue().getSpawnLocation().getY(), rsp.getValue().getSpawnLocation().getZ());
            resourceSpawners.put(rsp.getKey(), new ResourceSpawner(rsp.getValue().getCodename(), resSpawnerLoc, rsp.getValue().getTypeResourceSpawner()));
        }
    }

    public boolean checkPlayer(Player player) {
        if (playersNTimer.keySet().contains(player))
            return true;
        return false;
    }

    public void playerDisconnected(Player player) {
        if (checkPlayer(player))
            eliminetedCount++;
    }

    public void playerReconnected(Player player) {
        if (checkPlayer(player))
            eliminetedCount--;
        player.teleport(teamSpawnPoint);
    }

    public void reviving(Player player) {
        if (!isBedBroken()) {
            playersNTimer.get(player).start();
        } else {
            eliminetedCount++;
        }

        player.setHealth(20d);
        player.teleport(inRoom.getWorldQueueSpawn());
        player.setGameMode(GameMode.SPECTATOR);
    }

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
            BedwarsShopMenus.checkUpgrade(this, leatherArmorItemStack);
            leatherArmorItemStack.setItemMeta(leatherArmorMeta);
        }

        // Empty player inventory and set to starter pack
        PlayerInventory playerInv = player.getInventory();
        playerInv.clear();
        playerInv.setArmorContents(leatherArmorPack);

        // Create wooden sword
        ItemStack woodenSword = new ItemStack(Material.WOODEN_SWORD);
        if (permanentLevels.get(BedwarsUpgradeMenus.SHARPER_BLADE) - 1 != 0) {
            ItemMeta wsMeta = woodenSword.getItemMeta();
            wsMeta.addEnchant(Enchantment.DAMAGE_ALL, permanentLevels.get(BedwarsUpgradeMenus.SHARPER_BLADE) - 1, true);
            woodenSword.setItemMeta(wsMeta);
        }
        playerInv.setItem(0, woodenSword);
    }

    public void teamUpgrade(String upgradeName) {
        // Upgrade
        int currentLvl = permanentLevels.get(upgradeName);
        permanentLevels.put(upgradeName, currentLvl + 1);

        // Effect all player in team
        if (upgradeName.equals(BedwarsUpgradeMenus.SHARPER_BLADE)) {
            for (Player p : playersNTimer.keySet()) {
                PlayerInventory pInv = p.getInventory();
                for (int i = 0; i < pInv.getSize(); i++) {
                    ItemStack pItem = pInv.getItem(i);
                    if (pItem == null)
                        continue;
                    if (UsefulStaticFunctions.isSword(pItem.getType()))
                        BedwarsShopMenus.checkUpgrade(this, pItem);
                }
            }
        } else if (upgradeName.equals(BedwarsUpgradeMenus.MINE_A_HOLIC)) {
            for (Player p : playersNTimer.keySet()) {
                PlayerInventory pInv = p.getInventory();
                for (int i = 0; i < pInv.getSize(); i++) {
                    ItemStack pItem = pInv.getItem(i);
                    if (pItem == null)
                        continue;
                    Material itemType = pItem.getType();
                    if (UsefulStaticFunctions.isAxe(itemType) || itemType == Material.SHEARS || UsefulStaticFunctions.isPickaxe(itemType))
                        BedwarsShopMenus.checkUpgrade(this, pItem);
                }
            }
        } else if (upgradeName.equals(BedwarsUpgradeMenus.MAKE_IT_RAIN)) {
            for (ResourceSpawner rspEntry : resourceSpawners.values()) {
                // TODO: Create Generic upgrades, custom persentage upgrade
                // 25% duration reduction
                rspEntry.setSpawnInterval(rspEntry.getSecondsPerSpawn() - (rspEntry.getSecondsPerSpawn() * 25/100));
            }
        } else if (upgradeName.equals(BedwarsUpgradeMenus.HOLY_LIGHT)) {
            // TODO: Area Regeneration
        } else if (upgradeName.equals(BedwarsUpgradeMenus.TOUGH_SKIN) || upgradeName.equals(BedwarsUpgradeMenus.EYE_FOR_AN_EYE)) {
            for (Player p : playersNTimer.keySet()) {
                ItemStack[] armorPack = p.getInventory().getArmorContents();
                for (ItemStack armorStack : armorPack) {
                    BedwarsShopMenus.checkUpgrade(this, armorStack);
                }
            }
        } else if (upgradeName.equals(BedwarsUpgradeMenus.GIFT_FOR_THE_POOR)) {
            // TODO: Easter Egg
        }
    }

    public boolean isAllMemberElimineted() {
        return eliminetedCount == playersNTimer.size();
    }

    public int getRemainingPlayers() {
        return playersNTimer.size() - eliminetedCount;
    }

    public boolean isBedBroken() {
        Block block = teamBedLocation.getBlock();
        return !UsefulStaticFunctions.isMaterialBed(block.getType());
    }

    public void sendTeamMessage(String msg) {
        for (Player p : playersNTimer.keySet()) {
            p.sendMessage(teamPrefix + "[" + teamName + "]" + ChatColor.WHITE + msg);
        }
    }

    public Map<String, Integer> getTeamLevels() {
        return permanentLevels;
    }

    public String getName() {
        return getColor() + teamName;
    }

    public Set<Player> getPlayersInTeam() {
        return playersNTimer.keySet();
    }

    public SleepingRoom getRoom() {
        return inRoom;
    }

    public String getMapName() {
        return worldOriginalName;
    }

    public Location getTeamBedLocation() {
        return teamBedLocation;
    }

    public String getRawColor() {
        return teamPrefix;
    }

    public ResourceSpawner getResourceSpawner(String resName) {
        if (resourceSpawners.containsKey(resName))
            return resourceSpawners.get(resName);
        return null;
    }

    public List<ResourceSpawner> getAllResourceSpawners() {
        List<ResourceSpawner> spr = new ArrayList<ResourceSpawner>();
        spr.addAll(resourceSpawners.values());
        return spr;
    }

    public int getTeamLevel(String upgradeName) {
        return permanentLevels.get(upgradeName);
    }

    public Map<String, Integer> getLevelsMap() {
        return permanentLevels;
    }

    public BedwarsUpgradeMenus getUpgradeMenus() {
        return upgradeMenu;
    }

    public String getColor() {
        if (teamPrefix.equalsIgnoreCase("blue")) {
            return ChatColor.BLUE + "";
        } else if (teamPrefix.equalsIgnoreCase("green")) {
            return ChatColor.GREEN + "";
        } else if (teamPrefix.equalsIgnoreCase("yellow")) {
            return ChatColor.YELLOW + "";
        } else if (teamPrefix.equalsIgnoreCase("aqua")) {
            return ChatColor.AQUA + "";
        } else if (teamPrefix.equalsIgnoreCase("red")) {
            return ChatColor.RED + "";
        } else if (teamPrefix.equalsIgnoreCase("purple")) {
            return ChatColor.LIGHT_PURPLE + "";
        } else if (teamPrefix.equalsIgnoreCase("gold")) {
            return ChatColor.GOLD + "";
        } else if (teamPrefix.equalsIgnoreCase("gray")) {
            return ChatColor.GRAY + "";
        } else { // Default is White
            return ChatColor.WHITE + "";
        }
    }

    private Color getPureColor() {
        if (teamPrefix.equalsIgnoreCase("blue")) {
            return Color.fromBGR(255, 0, 0);
        } else if (teamPrefix.equalsIgnoreCase("green")) {
            return Color.fromRGB(0, 255, 0);
        } else if (teamPrefix.equalsIgnoreCase("yellow")) {
            return Color.YELLOW;
        } else if (teamPrefix.equalsIgnoreCase("aqua")) {
            return Color.AQUA;
        } else if (teamPrefix.equalsIgnoreCase("red")) {
            return Color.fromRGB(255, 0, 0);
        } else if (teamPrefix.equalsIgnoreCase("light-purple")) {
            return Color.PURPLE;
        } else if (teamPrefix.equalsIgnoreCase("gold")) {
            return Color.fromRGB(255,223,0);
        } else if (teamPrefix.equalsIgnoreCase("gray")) {
            return Color.GRAY;
        } else { // Default is White
            return Color.WHITE;
        }
    }
    
}
