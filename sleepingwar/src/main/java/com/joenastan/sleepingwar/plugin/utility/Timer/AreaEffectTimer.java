package com.joenastan.sleepingwar.plugin.utility.Timer;

import java.util.List;

import javax.annotation.Nullable;

import com.joenastan.sleepingwar.plugin.game.TeamGroupMaker;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class AreaEffectTimer extends StopwatchTimer {

    private Location minimalPoint;
    private Location maximalPoint;
    private TeamGroupMaker teamEligible;
    private PotionEffect effect;
    private boolean singleShot = false;
    private boolean opposition = false;

    /**
     * Area effect routine whenever the team enters the area. The area is already auto calculation with minimum and maximum so don't worry.
     * @param duration Duration 
     * @param minimalPoint Minimum area X Y Z
     * @param maximalPoint Maximum area X Y Z
     * @param teamEligible The team only eligible to get this effect
     * @param effect Potion effect which will be gift for eligible team
     */
    public AreaEffectTimer(float duration, Location minPoint, Location maxPoint, PotionEffect effect, @Nullable TeamGroupMaker teamEligible) {
        super(duration);
        // Auto calculate minimum and maximum point
        double temp;
        if (minPoint.getX() > maxPoint.getX()) {
            temp = minPoint.getX();
            minPoint.setX(maxPoint.getX());
            maxPoint.setX(temp);
        }
        if (minPoint.getY() > maxPoint.getY()) {
            temp = minPoint.getY();
            minPoint.setY(maxPoint.getY());
            maxPoint.setY(temp);
        }
        if (minPoint.getZ() > maxPoint.getZ()) {
            temp = minPoint.getZ();
            minPoint.setZ(maxPoint.getZ());
            maxPoint.setZ(temp);
        }
        // Assign everything
        minimalPoint = minPoint;
        maximalPoint = maxPoint;
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
        boolean hitPlayer = false;
        List<Player> playersInWorld = minimalPoint.getWorld().getPlayers();
        for (Player p : playersInWorld) {
            Location playerCurrentLocation = p.getLocation();
            if (playerCurrentLocation.getX() <= maximalPoint.getX() && playerCurrentLocation.getX() >= minimalPoint.getX())
                if (playerCurrentLocation.getY() <= maximalPoint.getY() && playerCurrentLocation.getY() >= minimalPoint.getY())
                    if (playerCurrentLocation.getZ() <= maximalPoint.getZ() && playerCurrentLocation.getZ() >= minimalPoint.getZ()) {
                        if (teamEligible != null)
                            if ((teamEligible.getPlayers().contains(p) && !opposition) || (!teamEligible.getPlayers().contains(p) && opposition))
                                hitPlayer = p.addPotionEffect(effect);
                        else
                            hitPlayer = p.addPotionEffect(effect);
                        if (singleShot)
                            break;
                    }
        }
        reset();
        if (!singleShot || (!hitPlayer && singleShot))
            start();
    }

    public TeamGroupMaker getEligibleTeam() {
        return teamEligible;
    }

    public PotionEffect getEffect() {
        return effect;
    }

    /**
     * Set the effect which is not for team mate.
     * @param e set true if the effect area is for opposition.
     */
    public void setForOpposition(boolean e) {
        opposition = e;
    }

    /**
     * Check if it is only single time effect gift
     */
    public boolean isSingleEffect() {
        return singleShot;
    }

    /**
     * Check while set if it is only single time effect gift
     */
    public boolean isSingleEffect(boolean e) {
        if (e != singleShot)
            singleShot = e;
        return singleShot;
    }
}
