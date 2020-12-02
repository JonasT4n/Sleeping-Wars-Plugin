package com.joenastan.sleepingwar.plugin.utility.CustomDerivedEntity;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerBedwarsEntity {
    
    private String playerName;
    private Player player;
    private Location lastTpFrom;
    private String teamChoice;
    private GameMode lastGameMode;
    // TODO: Put temporary item inventory
    //private ItemStack 

    /**
     * Saving a player entity, this object only to prevent player's previous activity went gone.
     * @param player Refered player
     * @param lastTpfrom Last location before tp to bedwars world
     * @param lastGameMode Previous game mode before entering bedwars
     */
    public PlayerBedwarsEntity(Player player, Location lastTpfrom, GameMode lastGameMode) {
        this.player = player;
        this.lastTpFrom = lastTpfrom;
        this.lastGameMode = lastGameMode;
        playerName = player.getName();
    }

    public void setTeamChoice(String teamName) {
        teamChoice = teamName;
    }

    public String getTeamChoice() {
        return teamChoice;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
        playerName = player.getName();
    }

    public String getPlayerName() {
        return playerName;
    }

    /**
     * Return the entity to where player were before entering bedwars.
     */
    public void returnEntity() {
        player.teleport(lastTpFrom);
        player.setGameMode(lastGameMode);
        player = null;
        playerName = null;
        lastTpFrom = null;
        lastGameMode = null;
    }
}
