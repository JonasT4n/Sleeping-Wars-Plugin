package com.joenastan.sleepingwars.game;

import com.joenastan.sleepingwars.SleepingWarsPlugin;
import com.joenastan.sleepingwars.utility.DataFiles.GameButtonHolder;
import com.joenastan.sleepingwars.utility.PluginStaticFunc;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.util.*;

import com.joenastan.sleepingwars.utility.CustomEntity.PlayerBedwarsEntity;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class GameManager {

    public static GameManager instance;

    private static final int IDRange = 4;
    private static final String alphabets = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    public static boolean isGameShuttingDown = false;

    private Map<String, SleepingRoom> createdRooms = new HashMap<>();

    private GameManager() { }

    public static void init() {
        if (instance == null)
            instance = new GameManager();
    }

    /**
     * Create a newly room, which the use who use the command will be the game host
     *
     * @param player Player who used command
     * @param useMap Map that will be used
     */
    public void createRoom(Player player, World useMap, @Nullable String roomName) {
        // Check if player is a host
        String inWorldCurrentName = player.getWorld().getName();
        SleepingRoom currentRoom = createdRooms.get(inWorldCurrentName);
        if (currentRoom != null) {
            // Check player is a Host
            if (playerIsHost(player)) {
                player.sendMessage(ChatColor.BLUE + "You have already created a game.");
                return;
            }

            // Check player in room currently the game is ongoing
            else if (currentRoom.isGameProcessing()) {
                player.sendMessage(ChatColor.BLUE + "You are currently in game now, can't create new game.");
                return;
            }
        }

        // Create room id or set it from parameter
        String createdRoomID;
        do {
            if (roomName == null)
                createdRoomID = bedwarsWorldID();
            else
                createdRoomID = roomName;
        } while (PluginStaticFunc.isFolderWorldExists(createdRoomID));

        // Copy World
        File folderLoc = new File(useMap.getWorldFolder().getAbsolutePath());
        File newCopy = new File(Bukkit.getWorldContainer().getAbsolutePath() + "/" + createdRoomID);
        copyWorld(folderLoc, newCopy);
        WorldCreator newCreatedWorld = new WorldCreator(createdRoomID);
        World copiedWorld = Bukkit.createWorld(newCreatedWorld);
        if (copiedWorld != null) {
            copiedWorld.setAutoSave(false);

            // Create Room
            PlayerBedwarsEntity playerEnt = currentRoom == null ? null : currentRoom.playerLeave(player);
            SleepingRoom newRoom = new SleepingRoom(useMap.getName(), player, copiedWorld, playerEnt);
            createdRooms.put(createdRoomID, newRoom);

            // Initialize button command holder
            GameButtonHolder.copyRoomButton(useMap.getName(), createdRoomID, copiedWorld);
        }
    }

    /**
     * Get created room by id
     *
     * @param id Room ID
     * @return Selected room, if not exists then null
     */
    public SleepingRoom getRoom(String id) {
        return createdRooms.get(id);
    }

    /**
     * Get list of created rooms.
     */
    public Map<String, SleepingRoom> getRoomMap() {
        return createdRooms;
    }

    /**
     * Check if the map is currently being played.
     *
     * @return Map of maps currently being played with room counts each
     */
    public Map<String, Integer> getPlayingMaps() {
        Map<String, Integer> mapMaps = new HashMap<>();
        for (SleepingRoom room : createdRooms.values()) {
            if (mapMaps.get(room.getMapName()) == null) {
                mapMaps.put(room.getMapName(), 1);
            } else {
                int count = mapMaps.get(room.getMapName());
                mapMaps.put(room.getMapName(), count + 1);
            }
        }
        return mapMaps;
    }

    /**
     * Check if player is already a host in one game.
     *
     * @param player Referred player
     * @return true if player is a host, else then false
     */
    private boolean playerIsHost(Player player) {
        for (SleepingRoom room : createdRooms.values()) {
            if (room.getHost().equals(player))
                return true;
        }
        return false;
    }

    /**
     * Create a unique room with unique id.
     *
     * @return Unique string id
     */
    private String bedwarsWorldID() {
        String result = "";
        Random rand = new Random();
        while (result.length() < IDRange) {
            int indexAlpha = rand.ints(0, alphabets.length()).findFirst().getAsInt();
            result = result + alphabets.charAt(indexAlpha);
            if (createdRooms.containsKey(result))
                result = "";
        }
        return result;
    }

    /**
     * Clean garbage and statics, used one time when the plugin will be disabled
     */
    public void cleanManager() {
        for (SleepingRoom sp : createdRooms.values())
            sp.destroyRoom();

        createdRooms.clear();
        createdRooms = null;
        instance = null;
    }

    /**
     * Copy a world to a new one.
     *
     * @param source Original map file location
     * @param target Target file location
     */
    private void copyWorld(File source, File target) {
        try {
            ArrayList<String> ignore = new ArrayList<>(Arrays.asList("uid.dat", "session.dat"));
            if (!ignore.contains(source.getName())) {
                if (source.isDirectory()) {
                    if (!target.exists())
                        if (target.mkdirs())
                            System.out.println("Room folder has been created: " + target.getName());
                    String[] files = source.list();
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
