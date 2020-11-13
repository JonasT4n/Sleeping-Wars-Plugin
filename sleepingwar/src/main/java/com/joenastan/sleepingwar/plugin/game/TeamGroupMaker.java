package com.joenastan.sleepingwar.plugin.game;

import com.joenastan.sleepingwar.plugin.SleepingWarsPlugin;
import com.joenastan.sleepingwar.plugin.utility.Timer.PlayerReviveTimer;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
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

    // Maps and Lists
    private Map<Player, PlayerReviveTimer> playersNTimer = new HashMap<Player, PlayerReviveTimer>();
    private Map<String, ResourceSpawner> resourceSpawners = new HashMap<String, ResourceSpawner>();
    private Map<String, Integer> permanentLevels = new HashMap<String, Integer>();

    public TeamGroupMaker(SleepingRoom inRoom, String teamName, String worldOriginalName, List<Player> players, 
            Location spawnPoint, Location bedLocation, String teamPrefix) {
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
            p.sendMessage(getColor(teamPrefix) + "You are in team [" + teamName + "]");
            playersNTimer.put(p, new PlayerReviveTimer(respawnTime, p, this));
            p.teleport(teamSpawnPoint);
            p.setGameMode(GameMode.SURVIVAL);
        }

        for (Map.Entry<String, ResourceSpawner> rsp : SleepingWarsPlugin.getGameSystemConfig().getResourceSpawnersPack(worldOriginalName, teamName).entrySet()) {
            Location resSpawnerLoc = new Location(inRoom.getWorldQueueSpawn().getWorld(), rsp.getValue().getSpawnLocation().getX(),  
                    rsp.getValue().getSpawnLocation().getY(), rsp.getValue().getSpawnLocation().getZ());
            resourceSpawners.put(rsp.getKey(), new ResourceSpawner(rsp.getValue().getCodename(), spawnLoc, rsp.getValue().getTypeResourceSpawner()));
        }
    }

    public boolean checkPlayer(Player player) {
        if (playersNTimer.keySet().contains(player))
            return true;
        return false;
    }

    public void revive(Player player) {
        playersNTimer.get(player).start();
    }

    public void addElimination() {
        eliminetedCount++;
    }

    public boolean isAllMemberElimineted() {
        return eliminetedCount == playersNTimer.size();
    }

    public int getRemainingPlayers() {
        return playersNTimer.size() - eliminetedCount;
    }

    public boolean isBedBroken() {
        Block block = teamBedLocation.getBlock();
        return isMaterialBed(block.getType());
    }

    public void sendMessage(Player p, String msg) {
        p.sendMessage(teamPrefix + "[" + teamName + "]" + ChatColor.WHITE + msg);
    }

    public String getName() {
        return getColor(teamPrefix) + teamName;
    }

    public Location getSpawnLoc() {
        return teamSpawnPoint;
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

    private String getColor(String prefix) {
        if (prefix.equalsIgnoreCase("blue")) {
            return ChatColor.BLUE + "";
        } else if (prefix.equalsIgnoreCase("green")) {
            return ChatColor.GREEN + "";
        } else if (prefix.equalsIgnoreCase("yellow")) {
            return ChatColor.YELLOW + "";
        } else if (prefix.equalsIgnoreCase("aqua")) {
            return ChatColor.AQUA + "";
        } else if (prefix.equalsIgnoreCase("red")) {
            return ChatColor.RED + "";
        } else if (prefix.equalsIgnoreCase("light-putple")) {
            return ChatColor.LIGHT_PURPLE + "";
        } else if (prefix.equalsIgnoreCase("gold")) {
            return ChatColor.GOLD + "";
        } else if (prefix.equalsIgnoreCase("gray")) {
            return ChatColor.GRAY + "";
        } else {
            return ChatColor.WHITE + "";
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
