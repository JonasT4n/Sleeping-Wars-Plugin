package com.joenastan.sleepingwar.plugin.utility;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerBedwarsEntity {
    
    private Player player;
    private Location lastTpFrom;
    private String teamChoice;
    private GameMode lastGameMode;

    public PlayerBedwarsEntity(Player player, Location lastTpfrom, GameMode lastGameMode) {
        this.player = player;
        this.lastTpFrom = lastTpfrom;
        this.lastGameMode = lastGameMode;
    }

    public void setTeamChoice(String teamName) {
        teamChoice = teamName;
    }

    public String getTeamChoice() {
        return teamChoice;
    }

    public void returnEntity() {
        player.teleport(lastTpFrom);
        player.setGameMode(lastGameMode);
        player = null;
        lastTpFrom = null;
        lastGameMode = null;
    }
}
