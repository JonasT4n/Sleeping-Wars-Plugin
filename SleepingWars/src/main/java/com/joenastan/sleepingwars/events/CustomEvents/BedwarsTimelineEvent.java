package com.joenastan.sleepingwars.events.CustomEvents;

import com.joenastan.sleepingwars.enumtypes.TimelineEventType;
import com.joenastan.sleepingwars.game.SleepingRoom;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nullable;

public class BedwarsTimelineEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final String eventName;
    private final TimelineEventType type;
    private final float secondsToTrigger;
    private final int order;
    private final String eventmsg;

    private SleepingRoom room;

    public BedwarsTimelineEvent(TimelineEventType type, float secondsToTrigger, String eventName, int order,
                                @Nullable String eventMsg) {
        this.type = type;
        this.secondsToTrigger = secondsToTrigger;
        this.eventName = eventName;
        this.order = order;
        this.eventmsg = eventMsg;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public TimelineEventType getEventType() {
        return type;
    }

    public float getTriggerSeconds() {
        return secondsToTrigger;
    }

    /**
     * Custom event name.
     */
    public String getName() {
        return eventName;
    }

    public SleepingRoom getRoom() {
        return room;
    }

    public void setRoom(SleepingRoom room) {
        this.room = room;
    }

    public int getTimelineOrder() {
        return order;
    }

    public String getEventMessage() {
        return eventmsg;
    }

}
