package com.joenastan.sleepingwars;

import com.joenastan.sleepingwars.commands.HostBedwarsCommand;
import com.joenastan.sleepingwars.commands.SleepingWarsPermissionsCommand;
import com.joenastan.sleepingwars.commands.TabCompletor.HostingCommands;
import com.joenastan.sleepingwars.commands.TabCompletor.SWorldCommands;
import com.joenastan.sleepingwars.commands.WorldMakerCommand;
import com.joenastan.sleepingwars.events.OnBuilderModeEvents;
import com.joenastan.sleepingwars.events.OnGameEvent;
import com.joenastan.sleepingwars.game.GameManager;
import com.joenastan.sleepingwars.game.InventoryMenus.BedwarsShopMenu;
import com.joenastan.sleepingwars.utility.DataFiles.GameButtonHolder;
import com.joenastan.sleepingwars.utility.DataFiles.GameSystemConfig;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class SleepingWarsPlugin extends JavaPlugin {

    private static JavaPlugin instance;
    private static GameSystemConfig gameSystemConfig;
    private static GameButtonHolder buttonHolderCollection;
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

    public static GameButtonHolder getButtonHolder() {
        return buttonHolderCollection;
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
        getCommand("SWorld").setTabCompleter(new SWorldCommands());
        getCommand("Bedwars").setExecutor(new HostBedwarsCommand());
        getCommand("Bedwars").setTabCompleter(new HostingCommands());
        getCommand("SwPerm").setExecutor(new SleepingWarsPermissionsCommand());
        System.out.println("[SleepingWars] ~ Ready to Sleep! ~");
    }

    @Override
    public void onDisable() {
        // Free up memories
        instance = null;
        gameSystemConfig.Save();
        gameSystemConfig = null;
        gameManager.cleanManager();
        gameManager = null;
        buttonHolderCollection.Save();
        buttonHolderCollection = null;
        OnBuilderModeEvents.clearStatic();
        HandlerList.unregisterAll(this);
        BedwarsShopMenu.destroy();
        System.out.println("[SleepingWars] Sleeping war is over, see you next time!");
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
        buttonHolderCollection = new GameButtonHolder(instance, "buttons.yml");
        gameManager = new GameManager();
        BedwarsShopMenu.init();
    }
}
