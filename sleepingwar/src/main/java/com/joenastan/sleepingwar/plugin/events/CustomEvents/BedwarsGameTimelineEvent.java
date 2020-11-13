package com.joenastan.sleepingwar.plugin.events.CustomEvents;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BedwarsGameTimelineEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private String eventName;
    private TimelineEventType type;
    private float secondsToTrigger;

    public BedwarsGameTimelineEvent(TimelineEventType type, float secondsToTrigger, String eventName) {
        this.type = type;
        this.secondsToTrigger = secondsToTrigger;
        this.eventName = eventName;
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

    public String getName() {
        return eventName;
    }
    
}
