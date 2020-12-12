package com.joenastan.sleepingwars.utility.DataFiles;

import java.util.ArrayList;
import java.util.List;

import com.joenastan.sleepingwars.utility.Hologram.Hologram;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class HologramDataConfig extends AbstractFile {

    public HologramDataConfig(JavaPlugin main2, String filename) {
        super(main2, filename);
    }

    /**
     * Load list of hologram in world. It is automatically displayed in world when loaded.
     * @param mapName Original map name
     * @return A list of holographic display
     */
    public List<Hologram> loadHologram(String mapName) {
        List<Hologram> listHolo = new ArrayList<Hologram>();
        World targetBedwarsWorld = Bukkit.getWorld(mapName);
        return listHolo;
    }
    
}
