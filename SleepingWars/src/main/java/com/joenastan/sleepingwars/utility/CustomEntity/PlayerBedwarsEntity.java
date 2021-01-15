package com.joenastan.sleepingwars.utility.CustomEntity;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class PlayerBedwarsEntity {

    // Personal information
    private UUID playerID;
    private Player player;

    // Before teleport to game information
    private Location lastTpFrom;
    private GameMode lastGameMode;

    // In game information
    protected boolean isCommandLeave = false;
    private String teamChoice;
    private boolean eliminated = false;
    private PlayerBedwarsEntity lastHitBy = null;

    /**
     * Saving a player entity, this object only to prevent player's previous activity went gone.
     *
     * @param player       Referred player
     * @param lastTpFrom   Last location before tp to bedwars world
     * @param lastGameMode Previous game mode before entering bedwars
     */
    public PlayerBedwarsEntity(@Nonnull Player player, @Nonnull Location lastTpFrom, GameMode lastGameMode) {
        this.player = player;
        this.lastTpFrom = lastTpFrom;
        this.lastGameMode = lastGameMode;
        playerID = player.getUniqueId();
    }

    /**
     * Return the entity to where player were before entering bedwars.
     */
    public void returnEntity() {
        for (PotionEffect effect : player.getActivePotionEffects())
            player.removePotionEffect(effect.getType());
        player.getInventory().clear();
        player.teleport(lastTpFrom);
        player.setGameMode(lastGameMode);
        player = null;
        playerID = null;
        lastTpFrom = null;
        lastGameMode = null;
    }

    public void setPlayer(Player player) {
        this.player = player;
        playerID = player.getUniqueId();
    }

    public String getTeamChoice() {
        return teamChoice;
    }

    public void setTeamChoice(String teamName) {
        teamChoice = teamName;
    }

    public Player getPlayer() {
        return player;
    }

    public UUID getUniqueID() {
        return playerID;
    }

    public boolean isLeavingUsingCommand() {
        return isCommandLeave;
    }

    public void setLeavingUsingCommand(boolean e) {
        isCommandLeave = e;
    }

    public boolean isEliminated() {
        return eliminated;
    }

    public void setEliminated(boolean eliminated) {
        this.eliminated = eliminated;
    }

    public void setLastHitBy(@Nullable PlayerBedwarsEntity player) {
        lastHitBy = player;
    }

    public PlayerBedwarsEntity getLastHitBy() {
        return lastHitBy;
    }
}
