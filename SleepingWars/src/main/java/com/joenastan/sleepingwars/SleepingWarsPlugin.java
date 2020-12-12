package com.joenastan.sleepingwars;

import com.joenastan.sleepingwars.commands.HostBedwarsCommand;
import com.joenastan.sleepingwars.commands.SleepingWarsPermissionsCommand;
import com.joenastan.sleepingwars.commands.TabCompletor.HostingCommands;
import com.joenastan.sleepingwars.commands.TabCompletor.SworldCommands;
import com.joenastan.sleepingwars.commands.WorldMakerCommand;
import com.joenastan.sleepingwars.events.OnBuilderModeEvents;
import com.joenastan.sleepingwars.events.OnGameEvent;
import com.joenastan.sleepingwars.game.GameManager;
import com.joenastan.sleepingwars.utility.DataFiles.GameSystemConfig;
import com.joenastan.sleepingwars.utility.Hologram.HologramManager;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class SleepingWarsPlugin extends JavaPlugin {

    private static JavaPlugin instance;
    private static GameSystemConfig gameSystemConfig;
    private static HologramManager hologramManager;
    private static GameManager gameManager;

    // Private Variable Getter
    public static JavaPlugin getPlugin() {
        return instance;
    }

    public static GameSystemConfig getGameSystemConfig() {
        return gameSystemConfig;
    }

    public static GameManager getGameManager() {
        return gameManager;
    }

    @Override
    public void onEnable() {
        // Initial Loader
        instance = this;
        initSW();

        // Load Game Events
        getServer().getPluginManager().registerEvents(new OnGameEvent(), this);
        getServer().getPluginManager().registerEvents(new OnBuilderModeEvents(), this);

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
        // Free up memories
        instance = null;
        gameSystemConfig.Save();
        gameSystemConfig = null;
        gameManager.cleanManager();
        gameManager = null;
        hologramManager.shutdown();
        hologramManager = null;
        OnBuilderModeEvents.clearStatic();
        HandlerList.unregisterAll(this);

        System.out.println("Sleeping war is over, see you next time!");
    }

    // Other Stuff in plugin
    // Initialize plugin before activation
    private void initSW() {
        // Create Directory if not exists
        if (!getDataFolder().exists())
            if (getDataFolder().mkdir())
                System.out.println("[SleepingWars] Plugin Directory has been created.");
        // Create inner directories inside Data Folder if not exists
        File folderPath = new File(getDataFolder(), "/world_backup");
        if (!folderPath.exists())
            if (folderPath.mkdir())
                System.out.println("[SleepingWars] world_backup Directory has been created.");
        // Create Configuration Files
        gameSystemConfig = new GameSystemConfig(instance, "worlds.yml");
        hologramManager = new HologramManager(instance);
        gameManager = new GameManager();
    }
}

/**
 * Bug:
 - Bow (DONE)
 - Shield (DONE)
 - Event Score board not showing (DONE)
 - Make an eliminated players can chat globally (DONE)
 - Player left minus on scoreboard (DONE)
 - Last standing win bug (DONE)
 - Public chest bug (DONE)

 Design:
 - Make iron Usefull
 - Gapple 6 Emeralds

 Future Feature:
 - World shrinking Event (DONE)
 - All bed destroyed Event (DONE)
 */