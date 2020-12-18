package com.joenastan.sleepingwars.utility.Hologram;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class Hologram {

    private static final double RANGE_BETWEEN_LINE = 0.25d;

    private final List<ArmorStand> textLines = new ArrayList<>();
    private final Location defaultLocation;

    private Location onLocation;

    public Hologram(@Nonnull Location onLocation, String... text) {
        this.onLocation = onLocation;
        defaultLocation = onLocation;
        if (text.length != 0)
            createNewLine(text);
    }

    public void replaceText(String... text) {
        // Clear text first
        clear();
        // Make new text
        createNewLine(text);
    }

    public void addLine(String... text) {
        // Just add a new line
        createNewLine(text);
    }

    /**
     * Edit line by line index.
     * @param lineIndex Line index from 0 to n
     * @param text Change with text
     * @return True if successfully edited, if line not exists then it returns false
     */
    public boolean editLine(int lineIndex, String text) {
        if (lineIndex >= textLines.size())
            return false;
        ArmorStand line = textLines.get(lineIndex);
        line.setCustomName(text);
        return true;
    }

    private void createNewLine(String... text) {
        World inWorld = onLocation.getWorld();
        for (String t : text) {
            // Next line
            onLocation = new Location(inWorld, onLocation.getX(), onLocation.getY() -
                    RANGE_BETWEEN_LINE, onLocation.getZ());
            ArmorStand line = (ArmorStand)inWorld.spawnEntity(onLocation, EntityType.ARMOR_STAND);
            textLines.add(line);
            // Setting armor stand to be ignored
            line.setArms(false);
            line.setGravity(false);
            line.setSmall(false);
            line.setVisible(false);
            line.setCustomName(t);
            line.setCustomNameVisible(true);
            line.setCanPickupItems(false);
        }
    }

    /**
     * This must be called when the server is shutdown or the world is being destroy.
     */
    public void clear() {
        onLocation = defaultLocation;
        for (ArmorStand l : textLines)
            l.remove();
        textLines.clear();
    }

}
