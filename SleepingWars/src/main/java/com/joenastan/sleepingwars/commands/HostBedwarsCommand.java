package com.joenastan.sleepingwars.commands;

import com.joenastan.sleepingwars.events.CustomEvents.BedwarsStartEvent;
import com.joenastan.sleepingwars.game.GameManager;
import com.joenastan.sleepingwars.game.SleepingRoom;
import com.joenastan.sleepingwars.SleepingWarsPlugin;
import com.joenastan.sleepingwars.utility.DataFiles.GameSystemConfig;
import com.joenastan.sleepingwars.utility.CustomEntity.PlayerBedwarsEntity;

import com.joenastan.sleepingwars.utility.PluginStaticFunc;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import javax.annotation.Nonnull;

public class HostBedwarsCommand implements Listener, CommandExecutor {

    private final GameSystemConfig systemConf = SleepingWarsPlugin.getGameSystemConfig();
    private final GameManager gameManager = GameManager.instance;

    public static final String HOST_CMD = "host"; // Host the game
    public static final String JOIN_CMD = "join"; // Join the game
    public static final String START_CMD = "start"; // Start the game, if user who use the command is a host
    public static final String EXIT_CMD = "leave"; // Leave the game
    public static final String CHANGE_MAP_CMD = "cmap"; // Change map on game, if user who use the command is a host
    public static final String LEAVE_ALL = "leaveall"; // All player leave the room

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command,
                             @Nonnull String label, @Nonnull String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender);
            if (args.length > 0) {
                // Host Command
                String subCommand = args[0];
                if (subCommand.equalsIgnoreCase(HOST_CMD))
                    hostBedwars(player, args);

                // Join Command
                else if (subCommand.equalsIgnoreCase(JOIN_CMD))
                    joinRoom(player, args);

                // Start Command
                else if (subCommand.equalsIgnoreCase(START_CMD))
                    startBedwars(player, args);

                // Exit Command
                else if (subCommand.equalsIgnoreCase(EXIT_CMD))
                    leaveBedwars(player);
            } else {
                bedwarsHelpMessage(sender);
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Command cannot be used in console.");
        }
        return true;
    }

    private void bedwarsHelpMessage(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "Sleeping World Maker Commands for Hosting a Game. " + ChatColor.AQUA +
                "List of sub-commands:\n" + ChatColor.GREEN +
                "host => Create room\njoin => join room, must include password\n" +
                "start => Start the game, only host able to do it\n" +
                "leave => Leave room, cannot use while on game");
    }

    private void joinRoom(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Insert a room mate name.");
        } else {
            String inWorldName = player.getWorld().getName();
            SleepingRoom currentRoom = gameManager.getRoom(inWorldName);
            SleepingRoom toRoom = gameManager.getRoom(args[1]);
            if (toRoom != null) {
                // Check if room already full
                if (toRoom.isRoomFull()) {
                    player.sendMessage(ChatColor.YELLOW + "Room already full.");
                    return;
                }

                // Enter a new room
                PlayerBedwarsEntity playerEntB;
                if (currentRoom != null)
                    playerEntB = currentRoom.playerLeave(player);
                else
                    playerEntB = null;
                toRoom.playerEnter(player, playerEntB);
            } else {
                player.sendMessage(ChatColor.YELLOW + "Room not available");
            }
        }
    }

    private void startBedwars(Player player, String[] args) {
        String inWorldName = player.getWorld().getName();
        SleepingRoom room = gameManager.getRoom(inWorldName);
        if (room != null) {
            if (room.getHost().equals(player)) {
                BedwarsStartEvent event = new BedwarsStartEvent(room);
                Bukkit.getServer().getPluginManager().callEvent(event);
            } else {
                player.sendMessage(ChatColor.YELLOW + "You aren't the host. Unable to use this command.");
            }
        } else {
            player.sendMessage(ChatColor.RED + "You can only use this command in bedwars world.");
        }
    }

    private void hostBedwars(Player player, String[] args) {
        if (args.length >= 2) {
            if (systemConf.getWorldNames().contains(args[1])) {
                boolean folderExists = false;
                if (args.length >= 3)
                    folderExists = PluginStaticFunc.isFolderWorldExists(args[2]);

                // Check world folder exists
                if (folderExists) {
                    player.sendMessage(ChatColor.YELLOW + "Cannot use that name as room name.");
                    return;
                }

                // Check if world still contains Player
                World map = Bukkit.getWorld(args[1]);
                if (map != null) {
                    if (map.getPlayers().size() == 0) {
                        World useMap = Bukkit.getWorld(args[1]);
                        if (args.length < 3)
                            gameManager.createRoom(player, useMap, null);
                        else
                            gameManager.createRoom(player, useMap, args[2]);
                        return;
                    }
                    player.sendMessage(ChatColor.YELLOW + "Currently under construction.");
                    return;
                }
                player.sendMessage(ChatColor.RED + "World not exists");
                return;
            }
            player.sendMessage(ChatColor.GOLD + "World not available.");
            return;
        }
        player.sendMessage(ChatColor.GOLD + "Invalid input, do /bedwars " + HOST_CMD + " <map-name> " +
                "[room-name], use one of the map that you have made.");
    }

    private void leaveBedwars(Player player) {
        String inWorldName = player.getWorld().getName();
        SleepingRoom room = gameManager.getRoom(inWorldName);
        if (room != null) {
            PlayerBedwarsEntity playerEnt = room.findPlayer(player);
            if (playerEnt != null) {
                playerEnt.setLeavingUsingCommand(true);
                playerEnt = room.playerLeave(player);
                if (room.isGameProcessing())
                    room.checkRemainingTeam();
                if (playerEnt != null)
                    playerEnt.returnEntity();
            } else {
                player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
            }
            player.sendMessage(ChatColor.YELLOW + "You leave the game.");
        } else {
            player.sendMessage(ChatColor.YELLOW + "You are not in bedwars.");
        }
    }
}
