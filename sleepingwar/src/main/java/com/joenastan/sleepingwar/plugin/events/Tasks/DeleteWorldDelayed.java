package com.joenastan.sleepingwar.plugin.events.Tasks;

import java.io.File;

public class DeleteWorldDelayed implements Runnable {

    private File worldFolder;

    public DeleteWorldDelayed(File worldFolder) {
        this.worldFolder = worldFolder;
    }

    @Override
    public void run() {
        deleteWorld(worldFolder);
    }

    private void deleteWorld(File f) {
        if (f.isDirectory()) {
            File[] files = f.listFiles();
            for (File file : files) {
                deleteWorld(file);
            }
        }

        f.delete();
    }

}
