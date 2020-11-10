package com.joenastan.sleepingwar.plugin.Utility;

import com.joenastan.sleepingwar.plugin.Game.ResourceSpawner;

public class ResourceSpawnTimer extends StopwatchTimer {

    private ResourceSpawner spawner;

    public ResourceSpawnTimer(float duration, ResourceSpawner spawner) {
        super(duration);
        this.spawner = spawner;
    }

    @Override
    protected void runEvent() {
        spawner.spawn();
        reset();
        start();
    }    
}
