package com.joenastan.sleepingwars.events.CustomEvents;

<<<<<<< Updated upstream:sleepingwar/src/main/java/com/joenastan/sleepingwar/plugin/events/CustomEvents/BedwarsGameOnUpgradeEvent.java
import com.joenastan.sleepingwar.plugin.game.TeamGroupMaker;
=======
import com.joenastan.sleepingwars.game.TeamGroupMaker;

>>>>>>> Stashed changes:src/main/java/com/joenastan/sleepingwars/events/CustomEvents/BedwarsGameOnUpgradeEvent.java
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class BedwarsGameOnUpgradeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private ItemStack itemUpgrade;
    private TeamGroupMaker team;
    private Player player;

    public BedwarsGameOnUpgradeEvent(TeamGroupMaker team, Player player, ItemStack itemUpgrade) {
        this.team = team;
        this.player = player;
        this.itemUpgrade = itemUpgrade;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public TeamGroupMaker getTeam() {
        return team;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getUpgradeItem() {
        return itemUpgrade;
    }

}
