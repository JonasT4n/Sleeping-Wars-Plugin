package com.joenastan.sleepingwar.plugin.Commands;

import com.joenastan.sleepingwar.plugin.Events.CustomEvents.BedwarsGameStart;
import com.joenastan.sleepingwar.plugin.Game.GameManager;
import com.joenastan.sleepingwar.plugin.Game.SleepingRoom;
import com.joenastan.sleepingwar.plugin.SleepingWarsPlugin;
import com.joenastan.sleepingwar.plugin.Utility.GameSystemConfig;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class HostBedwarsCommand implements Listener, CommandExecutor {

    private final GameSystemConfig systemConf = SleepingWarsPlugin.getGameSystemConfig();
    private String hostCMD = "host";
    private String joinCMD = "join";
    private String startCMD = "start";
    private String exitCMD = "leave";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender);
            if (args.length > 0) {
                // Host Comamnd
                String subCommand = args[0];
                if (subCommand.equalsIgnoreCase(hostCMD)) {
                    if (args.length < 2) {
                        sender.sendMessage(ChatColor.GOLD + "Include a world name (/bedwars host <worldname>), use one of the map that you have made.");
                    } else {
                        if (systemConf.getAllWorldName().contains(args[1])) {
                            World useMap = Bukkit.getWorld(args[1]);
                            GameManager.hostingBedwars(player, useMap);
                        } else {
                            sender.sendMessage(ChatColor.GOLD + "World not available.");
                        }
                    }
                }
                // Join Command
                else if (subCommand.equalsIgnoreCase(joinCMD)) {
                    if (args.length < 2) {
                        sender.sendMessage(ChatColor.RED + "Insert a room mate name.");
                    } else {
                        GameManager.joinBedwars(player, args[1]);
                    }
                }
                // Start Command
                else if (subCommand.equalsIgnoreCase(startCMD)) {
                    SleepingRoom r = GameManager.getRoomByPlayer(player);
                    if (r.isPlayerHost(player)) {
                        BedwarsGameStart event = new BedwarsGameStart(r);
                        Bukkit.getServer().getPluginManager().callEvent(event);
                    } else {
                        player.sendMessage(ChatColor.YELLOW + "You aren't the host. Unable to use this command.");
                    }
                }
                // Exit Command
                else if (subCommand.equalsIgnoreCase(exitCMD)) {
                    GameManager.leaveBedwars(player);
                }
            } else {
                bedwarsHelpMessage(sender);
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Command cannot be used in console.");
        }
        return true;
    }

    private void bedwarsHelpMessage(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "Sleeping World Maker Commands for Hosting a Game. " + ChatColor.AQUA + "List of sub-commands:\n" +
                ChatColor.GREEN + "host => Create room\njoin => join room, must include password\nstart => Start the game, only host able to do it\n" +
                "leave => Leave room, cannot use while on game");
    }

}
