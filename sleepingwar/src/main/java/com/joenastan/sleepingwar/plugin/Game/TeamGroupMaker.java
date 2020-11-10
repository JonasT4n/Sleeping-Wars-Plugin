package com.joenastan.sleepingwar.plugin.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

import com.joenastan.sleepingwar.plugin.Utility.PlayerReviveTimer;
import com.joenastan.sleepingwar.plugin.SleepingWarsPlugin;

enum ColorTeam { RED, BLUE, YELLOW, GREEN, ORANGE, PURPLE, CYAN, LIME }

public class TeamGroupMaker {

    private String teamName;
    private String worldType;
    private Map<Player, PlayerReviveTimer> playersNTimer = new HashMap<Player, PlayerReviveTimer>();
    private Location spawnPoint;
    private boolean bedDestroyed;
    private float respawnTime;
    private Map<String, ResourceSpawner> resourceSpawners = new HashMap<String, ResourceSpawner>();
    private Location baseLocationMin;
    private Location baseLocationMax;
    private Map<String, Integer> permanentLevels = new HashMap<String, Integer>();
    private String teamPrefix;

    public TeamGroupMaker(String teamName, String worldType, List<Player> players, Location spawnPoint, String teamPrefix) {
        this.teamName = teamName;
        this.spawnPoint = spawnPoint;
        this.teamPrefix = teamPrefix;
        this.worldType = worldType;
        
        respawnTime = 5f;
        for (Player p : players) {
            p.sendMessage("You are in team [" + teamPrefix + teamName + "]");
            playersNTimer.put(p, new PlayerReviveTimer(respawnTime, p, this));
        }

        resourceSpawners = SleepingWarsPlugin.getGameSystemConfig().getResourceSpawnersPack(worldType, teamName);
        for (ResourceSpawner rs : resourceSpawners.values()) {
            rs.isRunning(true);
        }
    }

    public boolean checkPlayer(Player player) {
        if (playersNTimer.keySet().contains(player))
            return true;
        return false;
    }

    public void revive(Player player) {
        if (!bedDestroyed) {
            player.teleport(spawnPoint);
            playersNTimer.get(player).start();
        }
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
}
