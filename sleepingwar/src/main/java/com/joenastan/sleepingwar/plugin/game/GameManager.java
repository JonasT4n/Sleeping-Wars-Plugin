package com.joenastan.sleepingwar.plugin.game;

import com.joenastan.sleepingwar.plugin.utility.CustomDerivedEntity.PlayerBedwarsEntity;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.*;

public class GameManager {

    private static final int IDRange = 4;
    private static final String alphabets = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private Map<String, SleepingRoom> createdRooms = new HashMap<String, SleepingRoom>();

    public GameManager() {
        // TODO: Manager performance
    }

    /**
     * Create a newly room, which the use who use the command will be the game host
     *
     * @param player Player who used command
     * @param useMap Map that will be used
     */
    public void createRoom(Player player, World useMap) {
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
        String createdRoomID = bedwarsWorldID();

        // Copy World
        File folderLoc = new File(useMap.getWorldFolder().getAbsolutePath());
        File newCopy = new File(Bukkit.getWorldContainer().getAbsolutePath() + "/" + createdRoomID);
        copyWorld(folderLoc, newCopy);
        WorldCreator newCreatedWorld = new WorldCreator(createdRoomID);
        World copiedWorld = Bukkit.createWorld(newCreatedWorld);
        assert copiedWorld != null;
        copiedWorld.setAutoSave(false);

        // Create Room
        PlayerBedwarsEntity playerEnt = currentRoom == null ? null : currentRoom.playerLeave(player);
        SleepingRoom newRoom = new SleepingRoom(useMap.getName(), player, copiedWorld, playerEnt);
        createdRooms.put(createdRoomID, newRoom);
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
        Map<String, Integer> mapMaps = new HashMap<String, Integer>();
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
     * @param player
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
        for (SleepingRoom sp : createdRooms.values()) {
            sp.destroyRoom();
        }
        createdRooms.clear();
        createdRooms = null;
    }

    /**
     * Copy a world to a new one.
     *
     * @param source Original map file location
     * @param target Target file location
     */
    private void copyWorld(File source, File target) {
        try {
            ArrayList<String> ignore = new ArrayList<String>(Arrays.asList("uid.dat", "session.dat"));
            if (!ignore.contains(source.getName())) {
                if (source.isDirectory()) {
                    if (!target.exists())
                        target.mkdirs();
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
