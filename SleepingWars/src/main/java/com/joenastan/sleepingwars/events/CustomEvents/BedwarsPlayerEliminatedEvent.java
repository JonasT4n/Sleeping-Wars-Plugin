package com.joenastan.sleepingwars.events.CustomEvents;

import com.joenastan.sleepingwars.game.SleepingRoom;
import com.joenastan.sleepingwars.game.TeamGroupMaker;
import com.joenastan.sleepingwars.utility.CustomDerivedEntity.PlayerBedwarsEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

public class BedwarsPlayerEliminatedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final SleepingRoom room;
    private final PlayerBedwarsEntity victim;
    private final TeamGroupMaker team;

    public BedwarsPlayerEliminatedEvent(@Nonnull SleepingRoom room, @Nonnull TeamGroupMaker team,
                                        @Nonnull PlayerBedwarsEntity victim) {
        this.room = room;
        this.victim = victim;
        this.team = team;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Nonnull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Player victim who has been eliminated.
     * @return Player entity victim
     */
    public PlayerBedwarsEntity getVictim() {
        return victim;
    }

    /**
     * Which room got invoked.
     */
    public SleepingRoom getRoom() {
        return room;
    }

    /**
     * Player entity has a team.
     */
    public TeamGroupMaker getTeam() {
        return team;
    }
}
