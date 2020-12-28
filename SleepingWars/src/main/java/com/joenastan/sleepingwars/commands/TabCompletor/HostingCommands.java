package com.joenastan.sleepingwars.commands.TabCompletor;

import com.joenastan.sleepingwars.SleepingWarsPlugin;
import com.joenastan.sleepingwars.game.GameManager;
import com.joenastan.sleepingwars.utility.DataFiles.GameSystemConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class HostingCommands implements TabCompleter {

    private static final GameSystemConfig systemConfig = SleepingWarsPlugin.getGameSystemConfig();
    private static final GameManager gameManager = SleepingWarsPlugin.getGameManager();

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command,
                                      @Nonnull String alias, String[] args) {
        if (args.length == 1) {
            List<String> hints = new ArrayList<>();
            hints.add("host");
            hints.add("join");
            hints.add("start");
            hints.add("leave");
            return hints;
        } else if (args.length == 2) {
            // Hosting hint
            if (args[0].equalsIgnoreCase("host")) {
                return systemConfig.getWorldNames();
            }
            // Join game hint
            else if (args[0].equalsIgnoreCase("join")) {
                return new ArrayList<>(gameManager.getRoomMap().keySet());
            }
        }
        return null;
    }

}
