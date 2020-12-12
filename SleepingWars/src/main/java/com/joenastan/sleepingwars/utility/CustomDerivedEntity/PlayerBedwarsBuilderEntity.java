package com.joenastan.sleepingwars.utility.CustomDerivedEntity;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.Queue;

public class PlayerBedwarsBuilderEntity extends PlayerBedwarsEntity {

    private Queue<Location> placedLocation = new LinkedList<Location>();
    private int requiredAmountLocations = 0;
    private Queue<String> codenameHolder = new LinkedList<String>();

    public PlayerBedwarsBuilderEntity(Player player, Location lastTpfrom, GameMode lastGameMode) {
        super(player, lastTpfrom, lastGameMode);
    }

    public void addLocationBuffer(Location location) {
        placedLocation.add(location);
    }

    public Location removeLocationBuffer() {
        return placedLocation.remove();
    }

    public void addCodenameHolder(String codename) {
        codenameHolder.add(codename);
    }

    public String removeCodename() {
        return codenameHolder.remove();
    }

    public int countCodenameHolder() {
        return codenameHolder.size();
    }

    public int getRequiredAmountLoc() {
        return requiredAmountLocations;
    }

    public void setRequiredAmountLoc(int amount) {
        requiredAmountLocations = amount;
    }

    public void clearLocationsBuffer() {
        placedLocation.clear();
    }

    public int countLocationsBuffer() {
        return placedLocation.size();
    }
}
