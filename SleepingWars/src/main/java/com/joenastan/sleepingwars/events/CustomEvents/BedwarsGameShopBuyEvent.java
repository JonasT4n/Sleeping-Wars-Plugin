package com.joenastan.sleepingwars.events.CustomEvents;

import com.joenastan.sleepingwars.game.TeamGroupMaker;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BedwarsGameShopBuyEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private Material itemBought;
    private TeamGroupMaker team;

    public BedwarsGameShopBuyEvent(Player player, Material itemBought, TeamGroupMaker team) {
        this.player = player;
        this.itemBought = itemBought;
        this.team = team;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public Material getBoughtMaterial() {
        return itemBought;
    }

    public TeamGroupMaker getTeam() {
        return team;
    }

}
