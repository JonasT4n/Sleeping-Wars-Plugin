package com.joenastan.sleepingwars.utility.DataFiles;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public abstract class AbstractFile {

    protected JavaPlugin main;
    protected YamlConfiguration fileConfig;
    private final File file;

    public AbstractFile(JavaPlugin main2, String filename) {
        this.main = main2;
        file = new File(main2.getDataFolder(), filename);
        if (!file.exists()) {
            try {
                if (file.createNewFile())
                    System.out.println("[SleepingWars] Created configuration file.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        LoadFileConfig();
    }

    /**
     * Load config.
     */
    public void LoadFileConfig() {
        fileConfig = YamlConfiguration.loadConfiguration(file);
        Save();
    }

    /**
     * Save plugin config file.
     */
    public void Save() {
        try {
            fileConfig.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Load worlds and config
     */
    public abstract void Load();
}
