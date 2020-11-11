package com.joenastan.sleepingwar.plugin;

import com.joenastan.sleepingwar.plugin.Commands.HostBedwarsCommand;
import com.joenastan.sleepingwar.plugin.Commands.SleepingWarsPermissionsCommand;
import com.joenastan.sleepingwar.plugin.Commands.TabCompletor.HostingCommands;
import com.joenastan.sleepingwar.plugin.Commands.TabCompletor.SworldCommands;
import com.joenastan.sleepingwar.plugin.Commands.WorldMakerCommand;
import com.joenastan.sleepingwar.plugin.Events.OnGameEvent;
import com.joenastan.sleepingwar.plugin.Game.GameManager;
import com.joenastan.sleepingwar.plugin.Utility.GameSystemConfig;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class SleepingWarsPlugin extends JavaPlugin {

    private static JavaPlugin plugin;
    private static GameSystemConfig systemConfig;

    // Private Variable Getter
    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public static GameSystemConfig getGameSystemConfig() {
        return systemConfig;
    }

    @Override
    public void onEnable() {
        // Initial Loader
        plugin = this;
        initSW();

        // Load Game Events
        getServer().getPluginManager().registerEvents(new OnGameEvent(), this);

        // Load Commands
        getCommand("SWorld").setExecutor(new WorldMakerCommand());
        getCommand("SWorld").setTabCompleter(new SworldCommands());
        getCommand("Bedwars").setExecutor(new HostBedwarsCommand());
        getCommand("Bedwars").setTabCompleter(new HostingCommands());
        getCommand("SwPerm").setExecutor(new SleepingWarsPermissionsCommand());

        System.out.println("~ Ready to Sleep! ~");
    }

    @Override
    public void onDisable() {
        System.out.println("Sleeping war is over, see you next time!");

        // Free up memories
        plugin = null;
        systemConfig.save();
        systemConfig = null;
        GameManager.cleanManager();
    }

    // Other Stuff in plugin
    // Initialize plugin before activation
    private void initSW() {
        // Create Directory if not exists
        if (!(getDataFolder().exists())) {
            getDataFolder().mkdir();
        }

        // Create inner directories inside Data Folder if not exists
        File folderPath = new File(getDataFolder().toPath().toAbsolutePath().toString() + "\\WorldList");
        if (!(folderPath.exists())) {
            folderPath.mkdir();
        }

        // Create Configuration Files
        systemConfig = new GameSystemConfig(plugin, "worlds.yml");
    }
}
