package com.joenastan.sleepingwar.plugin.events.Tasks;

import com.joenastan.sleepingwar.plugin.game.SleepingRoom;

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
