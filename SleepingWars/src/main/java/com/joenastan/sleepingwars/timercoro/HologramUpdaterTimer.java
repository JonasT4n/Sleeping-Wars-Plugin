package com.joenastan.sleepingwars.timercoro;

import com.joenastan.sleepingwars.utility.Hologram.Hologram;

public class HologramUpdaterTimer extends StopwatchTimer {

    private final Hologram targetUpdater;

    public HologramUpdaterTimer(float duration, Hologram target) {
        super(duration);
        targetUpdater = target;
    }

    public Hologram getHologram() {
        return targetUpdater;
    }
    
}
