package com.joenastan.sleepingwars.utility.CustomEntity;

import com.joenastan.sleepingwars.enumtypes.GameCommandType;
import com.joenastan.sleepingwars.events.CustomEvents.BedwarsStartEvent;
import com.joenastan.sleepingwars.game.GameManager;
import com.joenastan.sleepingwars.game.SleepingRoom;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ButtonCommandEntity {

    private final GameCommandType type;
    private final Location location;
    private final String selectedMap;

    public ButtonCommandEntity(GameCommandType type, Location location, @Nonnull String selectedMap) {
        this.type = type;
        this.location = location;
        this.selectedMap = selectedMap;
    }

    public void pressButton(Player player) {
        if (type == GameCommandType.JOIN) {
            // Find existing room and join
            for (SleepingRoom r : GameManager.instance.getRoomMap().values()) {
                if (r.getMapName().equals(selectedMap) && !r.isRoomFull() && !r.isGameProcessing()) {
                    r.playerEnter(player, null);
                    return;
                }
            }

            // New room hosting
            World map = Bukkit.getWorld(selectedMap);
            if (map != null) {
                GameManager.instance.createRoom(player, map, null);
                return;
            }
            player.sendMessage(ChatColor.RED + "World not exists");
        } else if (type == GameCommandType.LEAVE) {
            SleepingRoom room = GameManager.instance.getRoom(selectedMap);
            if (room != null) {
                if (!room.isStarting()) {
                    GameManager.instance.getRoom(selectedMap).playerLeave(player);
                } else {
                    player.sendMessage(ChatColor.YELLOW + "The game is starting, cannot leave the game.");
                }
            }
        } else if (type == GameCommandType.START) {
            SleepingRoom room = GameManager.instance.getRoom(selectedMap);
            if (room.getHost().equals(player)) {
                BedwarsStartEvent event = new BedwarsStartEvent(room);
                Bukkit.getServer().getPluginManager().callEvent(event);
            } else {
                player.sendMessage(ChatColor.YELLOW + "You aren't the host. Unable to use this command.");
            }
        }
    }

    public GameCommandType getTypeCommand() {
        return type;
    }

    public Location getButtonLocation() {
        return location;
    }

    public String getSelectedMap() {
        return selectedMap;
    }

}
