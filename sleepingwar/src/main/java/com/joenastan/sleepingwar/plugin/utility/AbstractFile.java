package com.joenastan.sleepingwar.plugin.utility;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class AbstractFile {

    protected JavaPlugin main;
    protected FileConfiguration filecon;
    private File file;

    public AbstractFile(JavaPlugin main2, String filename) {
        this.main = main2;
        file = new File(main2.getDataFolder(), filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        filecon = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Save plugin config file.
     */
    public void Save() {
        try {
            filecon.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
