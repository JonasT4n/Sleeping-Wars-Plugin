package com.joenastan.sleepingwars.timercoro;

import java.util.List;

import com.joenastan.sleepingwars.game.TeamGroupMaker;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import javax.annotation.Nullable;

public class AreaEffectTimer extends StopwatchTimer {

    private final Location minimalPoint;
    private final Location maximalPoint;
    private final TeamGroupMaker teamEligible;

    private PotionEffect effect;
    private boolean singleShot = false;
    private boolean opposition = false;

    /**
     * Area effect routine whenever the team enters the area. The area is already auto calculation with minimum and maximum so don't worry.
     * @param duration Duration 
     * @param minPoint Minimum area X Y Z
     * @param maxPoint Maximum area X Y Z
     * @param teamEligible The team only eligible to get this effect
     */
    public AreaEffectTimer(float duration, Location minPoint, Location maxPoint,
                           @Nullable TeamGroupMaker teamEligible) {
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
    }

    @Override
    public void start() {
        if (effect == null)
            return;
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (counter <= 0f) {
                runEvent();
                return;
            }
            counter -= 0.5f;
        }, 0L, 10L);
    }

    @Override
    protected void runEvent() {
        boolean hitPlayer = false;
        List<Player> playersInWorld = minimalPoint.getWorld().getPlayers();
        for (Player p : playersInWorld) {
            Location pCurrentLoc = p.getLocation();
            if (pCurrentLoc.getX() <= maximalPoint.getX() && pCurrentLoc.getX() >= minimalPoint.getX() &&
                    pCurrentLoc.getY() <= maximalPoint.getY() && pCurrentLoc.getY() >= minimalPoint.getY() &&
                    pCurrentLoc.getZ() <= maximalPoint.getZ() && pCurrentLoc.getZ() >= minimalPoint.getZ()) {
                if (teamEligible != null)
                    if ((teamEligible.getPlayers().contains(p) && !opposition) ||
                            (!teamEligible.getPlayers().contains(p) && opposition))
                        hitPlayer = p.addPotionEffect(effect);
                    else
                        hitPlayer = p.addPotionEffect(effect);
                if (singleShot)
                    break;
            }
        }
        reset();
        if (!singleShot || !hitPlayer)
            start();
    }

    public TeamGroupMaker getEligibleTeam() {
        return teamEligible;
    }

    public PotionEffect getEffect() {
        return effect;
    }

    public void setEffect(PotionEffect effect) {
        this.effect = effect;
    }

    /**
     * Set the effect which is not for team mate.
     *
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
