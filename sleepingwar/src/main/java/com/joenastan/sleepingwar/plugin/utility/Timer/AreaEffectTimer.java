package com.joenastan.sleepingwar.plugin.utility.Timer;

import com.joenastan.sleepingwar.plugin.game.TeamGroupMaker;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class AreaEffectTimer extends StopwatchTimer {

    private Location midPoint;
    private int radius;
    private TeamGroupMaker teamEligible;
    private PotionEffect effect;

    public AreaEffectTimer(float duration, Location midPoint, int radius, TeamGroupMaker teamEligible, PotionEffect effect) {
        super(duration);
        this.midPoint = midPoint;
        this.radius = radius;
        this.teamEligible = teamEligible;
        this.effect = effect;
    }

    @Override
    public void start() {
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (counter <= 0f) {
                    runEvent();
                    return;
                }

                counter -= 0.5f;
            }
        }, 0L, 10L);
    }

    @Override
    protected void runEvent() {
        for (Player p : teamEligible.getPlayersInTeam()) {
            p.addPotionEffect(effect);
        }

        reset();
        start();
    }

    public Location getMidPoint() {
        return midPoint;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }

    public TeamGroupMaker getEligibleTeam() {
        return teamEligible;
    }

    public PotionEffect getEffect() {
        return effect;
    }
    
}
