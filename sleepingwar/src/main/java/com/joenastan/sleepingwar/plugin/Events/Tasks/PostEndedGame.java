package com.joenastan.sleepingwar.plugin.Events.Tasks;

import com.joenastan.sleepingwar.plugin.Game.SleepingRoom;

public class PostEndedGame implements Runnable {

    private SleepingRoom room;

    public PostEndedGame(SleepingRoom room) {
        this.room = room;
    }
    
    @Override
    public void run() {
        room.destroyRoom();
    }

}
