package com.joenastan.testplugin;

import org.bukkit.plugin.java.JavaPlugin;

public class MainPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new EventListener(), this);
        System.out.println("[TesTPlugin] Testing Plugin");
    }

    @Override
    public void onDisable() {

    }

}
