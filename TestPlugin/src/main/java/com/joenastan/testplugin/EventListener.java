package com.joenastan.testplugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Bed;
import org.bukkit.entity.Item;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;

public class EventListener implements Listener {

    @EventHandler
    public void onPlacingBlock(BlockPlaceEvent event) {
        Block blockPlaced = event.getBlockPlaced();
        if (isMaterialBed(blockPlaced.getType()))
            blockPlaced.breakNaturally();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        System.out.println("[DEBUG] Block just broken");
    }

    @EventHandler
    public void onDropEvent(ItemSpawnEvent event) {
        Item it = event.getEntity();
        System.out.println("[DEBUG] Spawned Item: " + it.getName());
        if (isMaterialBed(it.getItemStack().getType()))
            event.setCancelled(true);
    }

    private static boolean isMaterialBed(Material material) {
        switch (material) {
            case BLACK_BED:
            case BLUE_BED:
            case BROWN_BED:
            case CYAN_BED:
            case GREEN_BED:
            case YELLOW_BED:
            case RED_BED:
            case ORANGE_BED:
            case GRAY_BED:
            case LIGHT_BLUE_BED:
            case LIME_BED:
            case PINK_BED:
            case MAGENTA_BED:
            case PURPLE_BED:
            case WHITE_BED:
            case LIGHT_GRAY_BED:
                return true;
            default:
                return false;
        }
    }

}
