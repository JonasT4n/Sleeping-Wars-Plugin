package com.joenastan.sleepingwar.plugin.game;

import com.joenastan.sleepingwar.plugin.events.CustomEvents.BedwarsGamePlayerJoinEvent;
import com.joenastan.sleepingwar.plugin.events.CustomEvents.BedwarsGamePlayerLeaveEvent;
import com.joenastan.sleepingwar.plugin.SleepingWarsPlugin;
import com.joenastan.sleepingwar.plugin.utility.GameSystemConfig;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.util.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class GameManager {

    private static final int IDRange = 4;
    private static final String alphabets = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    private static final GameSystemConfig gameConfig = SleepingWarsPlugin.getGameSystemConfig();
    private Map<String, SleepingRoom> rooms = new HashMap<String, SleepingRoom>();
    private Map<Player, String> playerList = new HashMap<Player, String>();

    public GameManager() {
        // TODO: Manager performance
    }

    // Player create a room
    public void hostingBedwars(Player player, World useMap) {
        if (!checkAlreadyHosted(player)) {
            String createdID = bedwarsWorldID();
            // Copy World
            File folderLoc = new File(useMap.getWorldFolder().getAbsolutePath());
            File newCopy = new File(Bukkit.getWorldContainer().getAbsolutePath() + "/" + createdID);
            copyWorld(folderLoc, newCopy);
            WorldCreator newCreatedWorld = new WorldCreator(createdID);
            World copied = Bukkit.createWorld(newCreatedWorld);

            // Create Room
            Location queueSpawn = gameConfig.getQueueLocations(useMap.getName());
            SleepingRoom newRoom = new SleepingRoom(useMap.getName(), player, copied, queueSpawn, 720000L);
            rooms.put(createdID, newRoom);
            playerList.put(player, createdID);

            BedwarsGamePlayerJoinEvent event = new BedwarsGamePlayerJoinEvent(player, newRoom);
            Bukkit.getServer().getPluginManager().callEvent(event);
        } else {
            player.sendMessage(ChatColor.BLUE + "You have already hosted the game.");
        }
    }

    // Player join the room
    public void joinBedwars(Player player, String worldpw) {
        if (rooms.containsKey(worldpw)) {
            player.sendMessage(ChatColor.GREEN + "Joining...");
            SleepingRoom room = rooms.get(worldpw);
            room.playerEnter(player);
            playerList.put(player, worldpw);
            player.sendMessage(ChatColor.AQUA + "This game hosted by " + ChatColor.LIGHT_PURPLE + room.getHost().getName());

            BedwarsGamePlayerJoinEvent event = new BedwarsGamePlayerJoinEvent(player, room);
            Bukkit.getServer().getPluginManager().callEvent(event);
        } else {
            player.sendMessage(ChatColor.RED + "Game not available.");
        }
    }

    // Player leave the room
    public void leaveBedwars(Player player) {
        if (playerList.containsKey(player)) {
            String worldKey = playerList.get(player);
            // Check world
            if (rooms.containsKey(worldKey)) {
                SleepingRoom room = rooms.get(worldKey);

                // Leave Room
                room.playerLeave(player);
                playerList.remove(player);

                BedwarsGamePlayerLeaveEvent event = new BedwarsGamePlayerLeaveEvent(player, room);
                Bukkit.getServer().getPluginManager().callEvent(event);
                return;
            }
            playerList.remove(player);
        }
        player.sendMessage(ChatColor.YELLOW + "You are not in game.");
    }

    // This function only for when the room will be destroy
    public void allLeave(Set<Player> ppl) {
        for (Player p : ppl) {
            if (playerList.containsKey(p)) {
                playerList.remove(p);
            }
        }
    }

    public SleepingRoom getRoomByPlayer(Player player) {
        for (SleepingRoom room : rooms.values()) {
            if (room.isPlayerInRoom(player))
                return room;
        }

        return null;
    }

    public SleepingRoom getRoomByName(String roomName) {
        for (Map.Entry<String, SleepingRoom> roomEntry : rooms.entrySet()) {
            if (roomEntry.getKey().equals(roomName))
                return roomEntry.getValue();
        }

        return null;
    }

    public Map<String, SleepingRoom> getAllRoom() {
        return rooms;
    }

    public Map<Player, String> getAllPlayerInGame() {
        return playerList;
    }

    public Map<Player, String> getPlayerInGameList() {
        return playerList;
    }

    private String bedwarsWorldID() {
        String result = "";
        Random rand = new Random();
        while (result.length() < IDRange) {
            int indexAlpha = rand.ints(0, alphabets.length()).findFirst().getAsInt();
            result = result + alphabets.charAt(indexAlpha);

            if (rooms.containsKey(result))
                result = "";
        }
        return result;
    }

    private boolean checkAlreadyHosted(Player player) {
        Collection<SleepingRoom> allRooms = rooms.values();
        for (SleepingRoom s : allRooms) {
            if (s.getHost().equals(player))
                return true;
        }
        return false;
    }

    public void cleanManager() {
        for (Map.Entry<String, SleepingRoom> sp : rooms.entrySet()) {
            sp.getValue().destroyRoom();
        }
        rooms.clear();
        rooms = null;

        playerList.clear();
        playerList = null;
    }

    private void copyWorld(File source, File target){
        try {
            ArrayList<String> ignore = new ArrayList<String>(Arrays.asList("uid.dat", "session.dat"));
            if(!ignore.contains(source.getName())) {
                if(source.isDirectory()) {
                    if(!target.exists())
                    target.mkdirs();
                    String files[] = source.list();
                    for (String file : files) {
                        File srcFile = new File(source, file);
                        File destFile = new File(target, file);
                        copyWorld(srcFile, destFile);
                    }
                } else {
                    InputStream in = new FileInputStream(source);
                    OutputStream out = new FileOutputStream(target);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0)
                        out.write(buffer, 0, length);
                    in.close();
                    out.close();
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
