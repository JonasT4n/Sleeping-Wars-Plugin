package com.joenastan.sleepingwars.utility.DataFiles;

import com.joenastan.sleepingwars.SleepingWarsPlugin;
import org.bukkit.plugin.java.JavaPlugin;

public class GameButtonHolder extends AbstractFile {

    private static final GameSystemConfig GAME_CONFIG = SleepingWarsPlugin.getGameSystemConfig();
    private static final GameButtonHolder BUTTON_HOLDER = SleepingWarsPlugin.getButtonHolder();

    public GameButtonHolder(JavaPlugin main2, String filename) {
        super(main2, filename);
        Load();
    }

    public static void buttonCreate() {

    }

    @Override
    public void Load() {

    }

}
