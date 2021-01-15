package com.joenastan.testplugin;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public class MainPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new EventListener(), this);
        System.out.println("[TesTPlugin] Testing Plugin");
        System.out.println(Material.IRON_INGOT.name());
        int ord = Material.IRON_INGOT.ordinal();
        System.out.println(Material.values()[ord].toString());
    }

    @Override
    public void onDisable() {

    }

}
