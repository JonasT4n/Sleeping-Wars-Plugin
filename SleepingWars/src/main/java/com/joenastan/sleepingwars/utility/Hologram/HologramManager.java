package com.joenastan.sleepingwars.utility.Hologram;

import com.joenastan.sleepingwars.utility.DataFiles.HologramDataConfig;

import org.bukkit.plugin.java.JavaPlugin;

public class HologramManager {
    
    private JavaPlugin plugin;
    private static HologramManager instance;

    private HologramDataConfig dataConfig;

    public HologramManager(JavaPlugin plugin) {
        // Make it singleton
        if (instance != null) {
            System.out.println("[SleepingWars] Manager already exists.");
            return;
        }
        instance = this;
        this.plugin = plugin;
        dataConfig = new HologramDataConfig(plugin, "floattext.yml");
    }

    public void shutdown() {
        instance = null;
    }

}
