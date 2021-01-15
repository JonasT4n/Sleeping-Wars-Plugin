package com.joenastan.sleepingwars.timercoro;

import com.joenastan.sleepingwars.game.TeamGroupMaker;
import com.joenastan.sleepingwars.utility.CustomEntity.PlayerBedwarsEntity;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;

public class PlayerReviveTimer extends StopwatchTimer {

    private final PlayerBedwarsEntity playerEnt;
    private final TeamGroupMaker team;

    public PlayerReviveTimer(float duration, PlayerBedwarsEntity playerEnt, TeamGroupMaker team) {
        super(duration);
        this.playerEnt = playerEnt;
        this.team = team;
    }

    @Override
    public void start() {
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (counter <= 0f) {
                runEvent();
                reset();
                return;
            }
            counter -= 0.1f;
            playerEnt.getPlayer().sendTitle("Reviving...",
                    String.format("In %.1f...", counter), 0, 4, 0);
        }, 0L, 2L);
    }

    @Override
    protected void runEvent() {
        if (playerEnt.getPlayer() == null)
            stop();
        playerEnt.getPlayer().teleport(team.getTeamSpawnLocation());
        playerEnt.getPlayer().setGameMode(GameMode.SURVIVAL);
        team.setStarterPack(playerEnt.getPlayer());
    }

    public PlayerBedwarsEntity getPlayerEntity() {
        return playerEnt;
    }

}
