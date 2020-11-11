package com.joenastan.sleepingwar.plugin.utility;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class AbstractFile {

    protected Plugin main;
    protected FileConfiguration filecon;
    private File file;

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
