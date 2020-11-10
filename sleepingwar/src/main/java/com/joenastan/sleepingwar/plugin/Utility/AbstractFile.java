package com.joenastan.sleepingwar.plugin.Utility;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class AbstractFile {

    protected Plugin main;
    private File file;
    protected FileConfiguration filecon;

    public AbstractFile(Plugin main, String filename) {
        this.main = main;
        file = new File(main.getDataFolder(), filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        filecon = YamlConfiguration.loadConfiguration(file);
    }

    protected void Save() {
        try {
            filecon.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
