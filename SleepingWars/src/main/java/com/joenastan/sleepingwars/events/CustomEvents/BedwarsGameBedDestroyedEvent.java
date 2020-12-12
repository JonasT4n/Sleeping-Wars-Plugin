package com.joenastan.sleepingwars.events.CustomEvents;

<<<<<<< Updated upstream:sleepingwar/src/main/java/com/joenastan/sleepingwar/plugin/events/CustomEvents/BedwarsGameBedDestroyedEvent.java
import com.joenastan.sleepingwar.plugin.game.TeamGroupMaker;
=======
import com.joenastan.sleepingwars.game.TeamGroupMaker;

>>>>>>> Stashed changes:src/main/java/com/joenastan/sleepingwars/events/CustomEvents/BedwarsGameBedDestroyedEvent.java
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BedwarsGameBedDestroyedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Player whoDestroy;
    private TeamGroupMaker bedTeamDestroyed;
    private TeamGroupMaker byTeam;

    public BedwarsGameBedDestroyedEvent(Player whoDestroy, TeamGroupMaker bedTeamDestroyed, TeamGroupMaker byTeam) {
        this.whoDestroy = whoDestroy;
        this.bedTeamDestroyed = bedTeamDestroyed;
        this.byTeam = byTeam;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public Player byWho() {
        return whoDestroy;
    }

    public TeamGroupMaker getVictim() {
        return bedTeamDestroyed;
    }

    public TeamGroupMaker getTeamDestroyer() {
        return byTeam;
    }

}
