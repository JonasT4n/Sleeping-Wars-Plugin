package com.joenastan.sleepingwars.events.CustomEvents;

<<<<<<< Updated upstream:sleepingwar/src/main/java/com/joenastan/sleepingwar/plugin/events/CustomEvents/BedwarsGameTimelineEvent.java
import com.joenastan.sleepingwar.plugin.enumtypes.TimelineEventType;
import com.joenastan.sleepingwar.plugin.game.SleepingRoom;
=======
import com.joenastan.sleepingwars.enumtypes.TimelineEventType;
import com.joenastan.sleepingwars.game.SleepingRoom;

>>>>>>> Stashed changes:src/main/java/com/joenastan/sleepingwars/events/CustomEvents/BedwarsGameTimelineEvent.java
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BedwarsGameTimelineEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private String eventName;
    private TimelineEventType type;
    private float secondsToTrigger;
    private SleepingRoom room;
    private int order;
    private String eventmsg;

    public BedwarsGameTimelineEvent(TimelineEventType type, float secondsToTrigger, String eventName, int order, String eventmsg) {
        this.type = type;
        this.secondsToTrigger = secondsToTrigger;
        this.eventName = eventName;
        this.order = order;
        this.eventmsg = eventmsg;
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
     * Event name.
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
