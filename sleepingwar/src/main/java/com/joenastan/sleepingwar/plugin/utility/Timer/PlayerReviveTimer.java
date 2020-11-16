package com.joenastan.sleepingwar.plugin.utility.Timer;

import com.joenastan.sleepingwar.plugin.game.TeamGroupMaker;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerReviveTimer extends StopwatchTimer {

    private Location teamSpawner;
    private Player player;
    private TeamGroupMaker team;

    public PlayerReviveTimer(float duration, Player player, TeamGroupMaker team, Location teamSpawner) {
        super(duration);
        this.player = player;
        this.team = team;
        this.teamSpawner = teamSpawner;
    }

    @Override
    public void start() {
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (counter <= 0f) {
                    runEvent();
                    reset();
                    return;
                }

                counter -= 0.05f;
                player.sendTitle("Reviving...", String.format("In %.1f...", counter), 0, 12, 0);
            }
        }, 0L, 1L);
    }

    @Override
    protected void runEvent() {
        player.teleport(teamSpawner);
        player.setGameMode(GameMode.SURVIVAL);
        team.setStarterPack(player);
    }

}
