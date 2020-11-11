package com.joenastan.sleepingwar.plugin.Game;

import com.joenastan.sleepingwar.plugin.SleepingWarsPlugin;
import com.joenastan.sleepingwar.plugin.Utility.Timer.PlayerReviveTimer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

enum ColorTeam {RED, BLUE, YELLOW, GREEN, ORANGE, PURPLE, CYAN, LIME}

public class TeamGroupMaker {

    private String teamName;
    private String worldOriginalName;
    private Map<Player, PlayerReviveTimer> playersNTimer = new HashMap<Player, PlayerReviveTimer>();
    private Location spawnPoint;
    private boolean bedBroken;
    private float respawnTime;
    private int eliminetedCount = 0;
    private Map<String, ResourceSpawner> resourceSpawners = new HashMap<String, ResourceSpawner>();
    private Location baseLocationMin;
    private Location baseLocationMax;
    private Map<String, Integer> permanentLevels = new HashMap<String, Integer>();
    private String teamPrefix;

    public TeamGroupMaker(SleepingRoom inRoom, String teamName, String worldOriginalName, List<Player> players, Location spawnPoint, String teamPrefix) {
        this.teamName = teamName;
        this.spawnPoint = spawnPoint;
        this.teamPrefix = teamPrefix;
        this.worldOriginalName = worldOriginalName;

        respawnTime = 5f;
        for (Player p : players) {
            p.sendMessage(getColor(teamPrefix) + "You are in team [" + teamName + "]");
            playersNTimer.put(p, new PlayerReviveTimer(respawnTime, p, this));
        }

        resourceSpawners = SleepingWarsPlugin.getGameSystemConfig().getResourceSpawnersPack(worldOriginalName, teamName);
    }

    public boolean checkPlayer(Player player) {
        if (playersNTimer.keySet().contains(player))
            return true;
        return false;
    }

    public void revive(Player player) {
        playersNTimer.get(player).start();
    }

    public boolean isAllMemberElimineted() {
        eliminetedCount++;
        return eliminetedCount == playersNTimer.size();
    }

    public boolean isBedBroken() {
        return bedBroken;
    }

    public void sendMessage(Player p, String msg) {
        p.sendMessage(teamPrefix + "[" + teamName + "]" + ChatColor.WHITE + msg);
    }

    public String getName() {
        return teamName;
    }

    public Location getSpawnLoc() {
        return spawnPoint;
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

    private ChatColor getColor(String prefix) {
        if (prefix.equalsIgnoreCase("blue")) {
            return ChatColor.BLUE;
        } else if (prefix.equalsIgnoreCase("green")) {
            return ChatColor.GREEN;
        } else if (prefix.equalsIgnoreCase("yellow")) {
            return ChatColor.YELLOW;
        } else if (prefix.equalsIgnoreCase("aqua")) {
            return ChatColor.AQUA;
        } else if (prefix.equalsIgnoreCase("red")) {
            return ChatColor.RED;
        } else if (prefix.equalsIgnoreCase("light-putple")) {
            return ChatColor.LIGHT_PURPLE;
        } else if (prefix.equalsIgnoreCase("gold")) {
            return ChatColor.GOLD;
        } else if (prefix.equalsIgnoreCase("gray")) {
            return ChatColor.GRAY;
        } else {
            return ChatColor.WHITE;
        }
    }
}
