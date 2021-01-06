package com.joenastan.sleepingwars.events;

import com.joenastan.sleepingwars.SleepingWarsPlugin;
import com.joenastan.sleepingwars.utility.DataFiles.GameButtonHolder;
import com.joenastan.sleepingwars.utility.DataFiles.GameSystemConfig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class OnButtonEventHandler implements Listener {

    private final GameSystemConfig systemConfig = SleepingWarsPlugin.getGameSystemConfig();
    private final GameButtonHolder buttonHolder = SleepingWarsPlugin.getButtonHolder();

    @EventHandler
    public void interactEvent(PlayerInteractEvent event) {

    }
}
