package com.joenastan.sleepingwars.tasks;

import com.joenastan.sleepingwars.game.GameManager;

import java.io.File;

public class DeleteWorldDelayed implements Runnable {

    private final File worldFolder;

    public DeleteWorldDelayed(File worldFolder) {
        this.worldFolder = worldFolder;
    }

    @Override
    public void run() {
        deleteWorld(worldFolder);
        GameManager.instance.getRoomMap().remove(worldFolder.getName());
    }

    private void deleteWorld(File f) {
        if (f.isDirectory()) {
            File[] files = f.listFiles();
            if (files != null)
                for (File file : files)
                    deleteWorld(file);
        }
        // Delete file or folder
        f.delete();
    }

}
