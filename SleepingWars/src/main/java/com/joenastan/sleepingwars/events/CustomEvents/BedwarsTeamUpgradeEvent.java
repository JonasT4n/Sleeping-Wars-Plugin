package com.joenastan.sleepingwars.events.CustomEvents;

import com.joenastan.sleepingwars.game.TeamGroupMaker;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;

public class BedwarsTeamUpgradeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final ItemMeta upgradeMeta;
    private final TeamGroupMaker team;
    private final Player player;

    public BedwarsTeamUpgradeEvent(@Nonnull TeamGroupMaker team, @Nonnull Player player,
                                   @Nonnull ItemMeta upgradeMeta) {
        this.team = team;
        this.player = player;
        this.upgradeMeta = upgradeMeta;
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

    public ItemMeta getMetaData() {
        return upgradeMeta;
    }
}
