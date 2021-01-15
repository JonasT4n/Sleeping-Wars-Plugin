package com.joenastan.sleepingwars.utility.DataFiles;

import com.joenastan.sleepingwars.enumtypes.GameCommandType;
import com.joenastan.sleepingwars.game.GameManager;
import com.joenastan.sleepingwars.utility.CustomEntity.ButtonCommandEntity;
import com.joenastan.sleepingwars.utility.PluginStaticFunc;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class GameButtonHolder extends AbstractFile {

    // Data path
    private static final String JOIN_GAME_BUTTON_PATH = GameCommandType.JOIN.toString() + "-button";
    private static final String LEAVE_GAME_BUTTON_PATH = GameCommandType.START.toString() + "-button";
    private static final String START_GAME_BUTTON_PATH = GameCommandType.LEAVE.toString() + "-button";

    // Temporary data holder
    public static Map<String, Map<Block, ButtonCommandEntity>> buttons;

    public GameButtonHolder(JavaPlugin main2, String filename) {
        super(main2, filename);
        load();
    }

    static {
        buttons = new HashMap<>();
    }

    public void saveConfig() {
        // Reset configuration by world names
        ConfigurationSection joinSection = fileConfig.getConfigurationSection(JOIN_GAME_BUTTON_PATH);
        if (joinSection != null) {
            for (String wn : joinSection.getKeys(false))
                fileConfig.set(JOIN_GAME_BUTTON_PATH + "." + wn, null);
        }
        ConfigurationSection startSection = fileConfig.getConfigurationSection(START_GAME_BUTTON_PATH);
        if (startSection != null) {
            for (String wn : startSection.getKeys(false))
                fileConfig.set(START_GAME_BUTTON_PATH + "." + wn, null);
        }
        ConfigurationSection leaveSection = fileConfig.getConfigurationSection(LEAVE_GAME_BUTTON_PATH);
        if (leaveSection != null) {
            for (String wn : leaveSection.getKeys(false))
                fileConfig.set(LEAVE_GAME_BUTTON_PATH + "." + wn, null);
        }

        Set<String> roomSet = GameManager.instance.getRoomMap().keySet();
        for (Map.Entry<String, Map<Block, ButtonCommandEntity>> btnEntry : buttons.entrySet()) {
            if (!roomSet.contains(btnEntry.getKey())) {
                int i = 0, j = 0, k = 0;
                for (Map.Entry<Block, ButtonCommandEntity> bEntry : btnEntry.getValue().entrySet()) {
                    GameCommandType type = bEntry.getValue().getTypeCommand();
                    String path = "." + bEntry.getKey().getWorld().getName() + ".";
                    ButtonCommandEntity ent = bEntry.getValue();
                    List<Integer> pos = new ArrayList<>();
                    pos.add(ent.getButtonLocation().getBlockX());
                    pos.add(ent.getButtonLocation().getBlockY());
                    pos.add(ent.getButtonLocation().getBlockZ());
                    if (type == GameCommandType.JOIN) {
                        fileConfig.set(JOIN_GAME_BUTTON_PATH + path + i + ".target-map", ent.getSelectedMap());
                        fileConfig.set(JOIN_GAME_BUTTON_PATH + path + i + ".position", pos);
                        i++;
                    } else if (type == GameCommandType.START) {
                        fileConfig.set(START_GAME_BUTTON_PATH + path + j + ".position", pos);
                        j++;
                    } else if (type == GameCommandType.LEAVE) {
                        fileConfig.set(LEAVE_GAME_BUTTON_PATH + path + k + ".position", pos);
                        k++;
                    }
                }
            }
        }
        save();
    }

    @Override
    public void load() {
        if (!fileConfig.contains(JOIN_GAME_BUTTON_PATH))
            fileConfig.createSection(JOIN_GAME_BUTTON_PATH);
        if (!fileConfig.contains(START_GAME_BUTTON_PATH))
            fileConfig.createSection(START_GAME_BUTTON_PATH);
        if (!fileConfig.contains(LEAVE_GAME_BUTTON_PATH))
            fileConfig.createSection(LEAVE_GAME_BUTTON_PATH);

        // Load join buttons
        ConfigurationSection joinSection = fileConfig.getConfigurationSection(JOIN_GAME_BUTTON_PATH);
        if (joinSection != null) {
            for (String inWorld : joinSection.getKeys(false)) {
                String worldPath = JOIN_GAME_BUTTON_PATH + "." + inWorld;
                Map<Block, ButtonCommandEntity> entMap;
                if (!buttons.containsKey(inWorld))
                    buttons.put(inWorld, new HashMap<>());
                entMap = buttons.get(inWorld);
                ConfigurationSection cs = fileConfig.getConfigurationSection(worldPath);
                if (cs != null) {
                    for (String indexes : cs.getKeys(false)) {
                        List<Integer> pos = fileConfig.getIntegerList(worldPath + "." + indexes + ".position");
                        Location loc = new Location(Bukkit.getWorld(inWorld), pos.get(0), pos.get(1), pos.get(2));
                        String mapSelected = fileConfig.getString(worldPath + "." + indexes + ".target-map");
                        if (PluginStaticFunc.isButton(loc.getBlock().getType()) && mapSelected != null && entMap != null)
                            entMap.put(loc.getBlock(), new ButtonCommandEntity(GameCommandType.JOIN, loc,
                                    mapSelected));
                    }
                }
            }
        }

        // Load start buttons
        ConfigurationSection startSection = fileConfig.getConfigurationSection(START_GAME_BUTTON_PATH);
        if (startSection != null) {
            for (String inWorld : startSection.getKeys(false)) {
                String worldPath = START_GAME_BUTTON_PATH + "." + inWorld;
                Map<Block, ButtonCommandEntity> entMap;
                if (!buttons.containsKey(inWorld))
                    buttons.put(inWorld, new HashMap<>());
                entMap = buttons.get(inWorld);
                ConfigurationSection cs = fileConfig.getConfigurationSection(worldPath);
                if (cs != null) {
                    for (String indexes : cs.getKeys(false)) {
                        List<Integer> pos = fileConfig.getIntegerList(worldPath + "." + indexes + ".position");
                        Location loc = new Location(Bukkit.getWorld(inWorld), pos.get(0), pos.get(1), pos.get(2));
                        if (PluginStaticFunc.isButton(loc.getBlock().getType()) && entMap != null)
                            entMap.put(loc.getBlock(), new ButtonCommandEntity(GameCommandType.START, loc, inWorld));
                    }
                }
            }
        }

        // Load leave buttons
        ConfigurationSection leaveSection = fileConfig.getConfigurationSection(LEAVE_GAME_BUTTON_PATH);
        if (leaveSection != null) {
            for (String inWorld : leaveSection.getKeys(false)) {
                String worldPath = LEAVE_GAME_BUTTON_PATH + "." + inWorld;
                Map<Block, ButtonCommandEntity> entMap;
                if (!buttons.containsKey(inWorld))
                    buttons.put(inWorld, new HashMap<>());
                entMap = buttons.get(inWorld);
                ConfigurationSection cs = fileConfig.getConfigurationSection(worldPath);
                if (cs != null) {
                    for (String indexes : cs.getKeys(false)) {
                        List<Integer> pos = fileConfig.getIntegerList(worldPath + "." + indexes + ".position");
                        Location loc = new Location(Bukkit.getWorld(inWorld), pos.get(0), pos.get(1), pos.get(2));
                        if (PluginStaticFunc.isButton(loc.getBlock().getType()) && entMap != null)
                            entMap.put(loc.getBlock(), new ButtonCommandEntity(GameCommandType.LEAVE, loc, inWorld));
                    }
                }
            }
        }
    }

    /**
     * Copy button reference from selected map.
     *
     * @param mapName Original map name
     * @param roomName In game room
     * @param world Gameplay world
     */
    public static void copyRoomButton(String mapName, String roomName, World world) {
        Map<Block, ButtonCommandEntity> entMap = buttons.get(mapName);
        buttons.put(roomName, new HashMap<>());
        Map<Block, ButtonCommandEntity> entRoomTarget = buttons.get(roomName);
        if (entMap != null && entRoomTarget != null) {
            for (Map.Entry<Block, ButtonCommandEntity> entEntry : entMap.entrySet()) {
                Location mapLoc = entEntry.getValue().getButtonLocation();
                Location roomLoc = new Location(world, mapLoc.getBlockX(), mapLoc.getBlockY(), mapLoc.getBlockZ());
                if (PluginStaticFunc.isButton(roomLoc.getBlock().getType()))
                    entRoomTarget.put(roomLoc.getBlock(), new ButtonCommandEntity(entEntry.getValue()
                            .getTypeCommand(), roomLoc, roomName));
            }
        }
    }
}
