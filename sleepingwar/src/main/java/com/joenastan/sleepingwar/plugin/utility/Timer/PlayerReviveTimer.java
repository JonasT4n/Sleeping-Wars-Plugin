package com.joenastan.sleepingwar.plugin.utility.Timer;

import com.joenastan.sleepingwar.plugin.events.CustomEvents.BedwarsGamePlayerRevive;
import com.joenastan.sleepingwar.plugin.game.TeamGroupMaker;
import org.bukkit.Bukkit;
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
    protected void runEvent() {
        player.teleport(team.getSpawnLoc());
        BedwarsGamePlayerRevive event = new BedwarsGamePlayerRevive(player, team);
        Bukkit.getPluginManager().callEvent(event);
    }

}