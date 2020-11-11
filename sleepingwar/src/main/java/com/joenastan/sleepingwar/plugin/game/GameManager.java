package com.joenastan.sleepingwar.plugin.game;

import com.joenastan.sleepingwar.plugin.events.CustomEvents.BedwarsGamePlayerJoin;
import com.joenastan.sleepingwar.plugin.events.CustomEvents.BedwarsGamePlayerLeave;
import com.joenastan.sleepingwar.plugin.SleepingWarsPlugin;
import com.joenastan.sleepingwar.plugin.utility.GameSystemConfig;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.util.*;

public class GameManager {

    private static final int IDRange = 4;
    private static final String alphabets = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    private static final GameSystemConfig gameConfig = SleepingWarsPlugin.getGameSystemConfig();
    private static Map<Player, Location> builderPreviousLocation = new HashMap<Player, Location>();
    private static Map<String, SleepingRoom> rooms = new HashMap<String, SleepingRoom>();
    private static Map<Player, String> playerList = new HashMap<Player, String>();

    // Player create a room
    public static void hostingBedwars(Player player, World useMap) {
        if (!checkAlreadyHosted(player)) {
            String createdID = bedwarsWorldID();
            // Copy World
            WorldCreator newCreatedWorld = new WorldCreator(createdID);
            World copied = Bukkit.createWorld(newCreatedWorld.copy(useMap));

            // Get Configuration
            Location queueSpawn = gameConfig.getQueueLocations(useMap.getName());

            // Create Room
            SleepingRoom newRoom = new SleepingRoom(useMap.getName(), player, copied, queueSpawn, 3600L);
            rooms.put(createdID, newRoom);
            joinBedwars(player, createdID);
            playerList.put(player, createdID);

            BedwarsGamePlayerJoin event = new BedwarsGamePlayerJoin(player, newRoom);
            Bukkit.getServer().getPluginManager().callEvent(event);
        } else {
            player.sendMessage(ChatColor.BLUE + "You have already hosted the game.");
        }
    }

    // Player join the room
    public static void joinBedwars(Player player, String worldpw) {
        if (rooms.containsKey(worldpw)) {
            player.sendMessage(ChatColor.GREEN + "Joining...");
            SleepingRoom room = rooms.get(worldpw);
            room.playerEnter(player);
            playerList.put(player, worldpw);
            player.sendMessage(ChatColor.AQUA + "This game hosted by " + ChatColor.LIGHT_PURPLE + room.getHost().getName());

            BedwarsGamePlayerJoin event = new BedwarsGamePlayerJoin(player, room);
            Bukkit.getServer().getPluginManager().callEvent(event);
        } else {
            player.sendMessage(ChatColor.RED + "Game not available.");
        }
    }

    // Player leave the room
    public static void leaveBedwars(Player player) {
        if (playerList.containsKey(player)) {
            String worldKey = playerList.get(player);
            // Check world
            if (rooms.containsKey(worldKey)) {
                SleepingRoom room = rooms.get(worldKey);

                // Leave Room
                room.playerLeave(player);
                playerList.remove(player);

                BedwarsGamePlayerLeave event = new BedwarsGamePlayerLeave(player, room);
                Bukkit.getServer().getPluginManager().callEvent(event);
                return;
            }
            playerList.remove(player);
        }
        player.sendMessage(ChatColor.YELLOW + "You are not in game.");
    }

    // This function only for when the room will be destroy
    public static void allLeave(Set<Player> ppl) {
        for (Player p : ppl) {
            if (playerList.containsKey(p)) {
                playerList.remove(p);
            }
        }
    }

    public static SleepingRoom getRoomByPlayer(Player player) {
        for (SleepingRoom room : rooms.values()) {
            if (room.isPlayerInRoom(player))
                return room;
        }

        return null;
    }

    public static SleepingRoom getRoomByName(String roomName) {
        for (Map.Entry<String, SleepingRoom> roomEntry : rooms.entrySet()) {
            if (roomEntry.getKey().equals(roomName))
                return roomEntry.getValue();
        }

        return null;
    }

    public static Map<String, SleepingRoom> getAllRoom() {
        return rooms;
    }

    public static List<Player> getAllPlayerInGame() {
        List<Player> ppl = new ArrayList<Player>();
        if (playerList.isEmpty())
            return ppl;

        ppl.addAll(playerList.keySet());
        return getAllPlayerInGame();
    }

    public static Map<Player, String> getPlayerInGameList() {
        return playerList;
    }

    public static Map<Player, Location> getBuilders() {
        return builderPreviousLocation;
    }

    private static String bedwarsWorldID() {
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

    private static boolean checkAlreadyHosted(Player player) {
        Collection<SleepingRoom> allRooms = rooms.values();
        for (SleepingRoom s : allRooms) {
            if (s.getHost().equals(player))
                return true;
        }
        return false;
    }

    public static void cleanManager() {
        for (Map.Entry<String, SleepingRoom> sp : rooms.entrySet()) {
            sp.getValue().destroyRoom();
        }
        rooms.clear();
        rooms = null;

        playerList.clear();
        playerList = null;

        for (Map.Entry<Player, Location> prevLoc : builderPreviousLocation.entrySet()) {
            prevLoc.getKey().teleport(prevLoc.getValue());
        }

        builderPreviousLocation.clear();
        builderPreviousLocation = null;
    }

}
