package com.joenastan.sleepingwars.utility.CustomEntity;

import com.joenastan.sleepingwars.enumtypes.GameCommandType;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.Queue;

public class PlayerBedwarsBuilderEntity extends PlayerBedwarsEntity {

    private final Queue<Location> placedLocation = new LinkedList<>();
    private final Queue<String> codenameHolder = new LinkedList<>();

    private int requiredAmountLocations = 0;

    public PlayerBedwarsBuilderEntity(Player player, Location lastFromLoc, GameMode lastGameMode) {
        super(player, lastFromLoc, lastGameMode);
    }

    public void addLocationBuffer(Location location) {
        placedLocation.add(location);
    }

    public Location removeBufferLoc() {
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
