package com.joenastan.sleepingwar.plugin.utility.Timer;

import com.joenastan.sleepingwar.plugin.events.CustomEvents.BedwarsGamePlayerReviveEvent;
import com.joenastan.sleepingwar.plugin.game.TeamGroupMaker;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class PlayerReviveTimer extends StopwatchTimer {

    private Player player;
    private TeamGroupMaker team;

    public PlayerReviveTimer(float duration, Player player, TeamGroupMaker team) {
        super(duration);
        this.player = player;
        this.team = team;
    }

    @Override
    public void start() {
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (counter <= 0f) {
                    runEvent();
                    stop();
                    return;
                }

                counter -= 0.5f;
                player.sendTitle("Reviving...", String.format("In %.1f...", counter), 0, 10, 0);
            }
        }, 0L, 10L);
    }

    @Override
    protected void runEvent() {
        player.teleport(team.getSpawnLoc());
        player.setGameMode(GameMode.SURVIVAL);
        team.setStarterPack(player);
        BedwarsGamePlayerReviveEvent event = new BedwarsGamePlayerReviveEvent(player, team);
        Bukkit.getPluginManager().callEvent(event);
    }

}
