package com.joenastan.sleepingwar.plugin.game.InventoryMenus;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

interface BedwarsMenus {
    public boolean openMenu(Player player, String menuName);
    public void selectedSlot(Player player, Inventory inv, int slot);
}
