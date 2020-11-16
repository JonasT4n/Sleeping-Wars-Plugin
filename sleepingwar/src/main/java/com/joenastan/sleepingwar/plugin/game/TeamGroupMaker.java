package com.joenastan.sleepingwar.plugin.game;

import com.joenastan.sleepingwar.plugin.SleepingWarsPlugin;
import com.joenastan.sleepingwar.plugin.game.InventoryMenus.BedwarsUpgradeMenus;
import com.joenastan.sleepingwar.plugin.utility.Timer.PlayerReviveTimer;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
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

        // Initialize team upgrade menu
        upgradeMenu = new BedwarsUpgradeMenus(this);
    }

    public boolean checkPlayer(Player player) {
        if (playersNTimer.keySet().contains(player))
            return true;
        return false;
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

        // Set Leather Armors Color
        ItemMeta bootpMeta = leatherArmorPack[0].getItemMeta();
        LeatherArmorMeta bootMeta = (LeatherArmorMeta)bootpMeta;
        bootMeta.setColor(getPureColor());
        leatherArmorPack[0].setItemMeta(bootpMeta);

        ItemMeta pantspMeta = leatherArmorPack[1].getItemMeta();
        LeatherArmorMeta pantsMeta = (LeatherArmorMeta)pantspMeta;
        pantsMeta.setColor(getPureColor());
        leatherArmorPack[1].setItemMeta(pantspMeta);

        ItemMeta chestpMeta = leatherArmorPack[2].getItemMeta();
        LeatherArmorMeta chestMeta = (LeatherArmorMeta)chestpMeta;
        chestMeta.setColor(getPureColor());
        leatherArmorPack[2].setItemMeta(chestpMeta);

        ItemMeta helmetpMeta = leatherArmorPack[3].getItemMeta();
        LeatherArmorMeta helmetMeta = (LeatherArmorMeta)helmetpMeta;
        helmetMeta.setColor(getPureColor());
        leatherArmorPack[3].setItemMeta(helmetpMeta);

        // Empty player inventory and set to starter pack
        PlayerInventory playerInv = player.getInventory();
        playerInv.clear();
        playerInv.setArmorContents(leatherArmorPack);
        playerInv.setItem(0, new ItemStack(Material.WOODEN_SWORD));
    }

    public void addElimination() {
        
    }

    public boolean isAllMemberElimineted() {
        return eliminetedCount == playersNTimer.size();
    }

    public int getRemainingPlayers() {
        return playersNTimer.size() - eliminetedCount;
    }

    public boolean isBedBroken() {
        Block block = teamBedLocation.getBlock();
        return !isMaterialBed(block.getType());
    }

    public void sendTeamMessage(String msg) {
        for (Player p : playersNTimer.keySet()) {
            p.sendMessage(teamPrefix + "[" + teamName + "]" + ChatColor.WHITE + msg);
        }
    }

    public String getName() {
        return getColor() + teamName;
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

    public ResourceSpawner getResourceSpawner(String resName) {
        if (resourceSpawners.containsKey(resName))
            return resourceSpawners.get(resName);
        return null;
    }

    public Map<String, Integer> getLevelsMap() {
        return permanentLevels;
    }

    public void teamUpgrade(String upgradeName) {
        int currentLvl = permanentLevels.get(upgradeName);
        permanentLevels.put(upgradeName, currentLvl + 1);
        switch (upgradeName) {
            case "Sharper Blade":
                break;

            case "Mine-A-Holic":
                break;

            case "Make it Rain!":
                break;

            case "Holy Light":
                break;

            case "Tough Skin":
                break;

            case "Eye for an Eye":
                break;

            case "Gift for the Poor":
                break;

            default:
                return;
        }
    }

    public List<ResourceSpawner> getAllResourceSpawners() {
        List<ResourceSpawner> spr = new ArrayList<ResourceSpawner>();
        spr.addAll(resourceSpawners.values());
        return spr;
    }

    public int getTeamLevel(String upgradeName) {
        if (permanentLevels.containsKey(upgradeName))
            return permanentLevels.get(upgradeName);
        return -1;
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
