package com.joenastan.sleepingwars.utility.DataFiles;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

public abstract class AbstractFile {

    protected JavaPlugin main;
    protected YamlConfiguration fileConfig;
    private final File file;

    public AbstractFile(JavaPlugin main, String filename) {
        this.main = main;
        file = new File(main.getDataFolder(), filename);
        if (!file.exists()) {
            try {
                if (file.createNewFile())
                    System.out.println("[SleepingWars] Created configuration file: " + filename);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        loadFileConfig();
    }

    /**
     * Load config.
     */
    public void loadFileConfig() {
        fileConfig = YamlConfiguration.loadConfiguration(file);
        try {
            fileConfig.load(file);
        } catch (Exception e) {
            System.out.println("[SleepingWars] Cannot load configuration file: " + file.getName());
        }
        save();
    }

    /**
     * Save plugin config file.
     */
    public void save() {
        try {
            fileConfig.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Load worlds and config
     */
    public abstract void load();
}
