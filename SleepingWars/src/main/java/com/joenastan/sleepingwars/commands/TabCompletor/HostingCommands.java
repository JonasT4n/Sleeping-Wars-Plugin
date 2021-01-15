package com.joenastan.sleepingwars.commands.TabCompletor;

import com.joenastan.sleepingwars.SleepingWarsPlugin;
import com.joenastan.sleepingwars.commands.HostBedwarsCommand;
import com.joenastan.sleepingwars.game.GameManager;
import com.joenastan.sleepingwars.utility.DataFiles.GameSystemConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HostingCommands implements TabCompleter {

    private static final GameSystemConfig systemConfig = SleepingWarsPlugin.getGameSystemConfig();
    private static final GameManager gameManager = GameManager.instance;

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command,
                                      @Nonnull String alias, String[] args) {
        if (args.length == 1) {
            List<String> hints = new ArrayList<>();
            hints.add(HostBedwarsCommand.HOST_CMD);
            hints.add(HostBedwarsCommand.JOIN_CMD);
            hints.add(HostBedwarsCommand.START_CMD);
            hints.add(HostBedwarsCommand.EXIT_CMD);
            return hints;
        } else if (args.length == 2) {
            // Hosting hint
            if (args[0].equalsIgnoreCase(HostBedwarsCommand.HOST_CMD)) {
                return systemConfig.getWorldNames();
            }
            // Join game hint
            else if (args[0].equalsIgnoreCase(HostBedwarsCommand.JOIN_CMD)) {
                return new ArrayList<>(gameManager.getRoomMap().keySet());
            }
        } else if (args.length == 3) {
            // Hosting hint
            if (args[0].equalsIgnoreCase(HostBedwarsCommand.HOST_CMD)) {
                return Collections.singletonList("[custom-room-name]");
            }
        }
        return null;
    }

}
