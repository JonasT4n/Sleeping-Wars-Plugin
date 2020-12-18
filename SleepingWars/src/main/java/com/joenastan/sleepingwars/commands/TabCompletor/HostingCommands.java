package com.joenastan.sleepingwars.commands.TabCompletor;

import com.joenastan.sleepingwars.SleepingWarsPlugin;
import com.joenastan.sleepingwars.game.GameManager;
import com.joenastan.sleepingwars.utility.DataFiles.GameSystemConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class HostingCommands implements TabCompleter {

    private static final GameSystemConfig systemConfig = SleepingWarsPlugin.getGameSystemConfig();
    private static final GameManager gameManager = SleepingWarsPlugin.getGameManager();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> hints = new ArrayList<String>();
            hints.add("host");
            hints.add("join");
            hints.add("start");
            hints.add("leave");
            return hints;
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("host")) {
                return systemConfig.getWorldNames();
            } else if (args[0].equalsIgnoreCase("join")) {
                return new ArrayList<String>(gameManager.getRoomMap().keySet());
            }
        }
        return null;
    }

}
