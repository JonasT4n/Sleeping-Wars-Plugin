package com.joenastan.sleepingwar.plugin.commands;

import com.joenastan.sleepingwar.plugin.events.CustomEvents.BedwarsGameStartEvent;
import com.joenastan.sleepingwar.plugin.game.SleepingRoom;
import com.joenastan.sleepingwar.plugin.SleepingWarsPlugin;
import com.joenastan.sleepingwar.plugin.utility.GameSystemConfig;
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
                    hostBedwars(player, args);
                }
                // Join Command
                else if (subCommand.equalsIgnoreCase(joinCMD)) {
                    joinRoom(player, args);
                }
                // Start Command
                else if (subCommand.equalsIgnoreCase(startCMD)) {
                    startBedwars(player, args);
                }
                // Exit Command
                else if (subCommand.equalsIgnoreCase(exitCMD)) {
                    leaveBedwars(player);
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

    private void joinRoom(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Insert a room mate name.");
        } else {
            SleepingWarsPlugin.getGameManager().joinBedwars(player, args[1]);
        }
    }

    private void startBedwars(Player player, String[] args) {
        SleepingRoom r = SleepingWarsPlugin.getGameManager().getRoomByPlayer(player);
        if (r.isPlayerHost(player)) {
            BedwarsGameStartEvent event = new BedwarsGameStartEvent(r);
            Bukkit.getServer().getPluginManager().callEvent(event);
        } else {
            player.sendMessage(ChatColor.YELLOW + "You aren't the host. Unable to use this command.");
        }
    }

    private void hostBedwars(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.GOLD + "Include a world name (/bedwars host <worldname>), use one of the map that you have made.");
        } else {
            if (systemConf.getAllWorldName().contains(args[1])) {
                // Check if world still contains Player
                if(Bukkit.getWorld(args[1]).getPlayers().size() == 0) {
                    World useMap = Bukkit.getWorld(args[1]);
                    SleepingWarsPlugin.getGameManager().hostingBedwars(player, useMap);
                    return;
                }
                player.sendMessage(ChatColor.YELLOW + "Currently under construction, you cannot play this map yet.");
            } else {
                player.sendMessage(ChatColor.GOLD + "World not available.");
            }
        }
    }

    private void leaveBedwars(Player player) {
        SleepingWarsPlugin.getGameManager().leaveBedwars(player);
    }

}
