package com.joenastan.sleepingwar.plugin.game.CustomDerivedEntity;

import java.util.Map;

import com.joenastan.sleepingwar.plugin.enumtypes.LockedEntityType;
import com.joenastan.sleepingwar.plugin.enumtypes.ResourcesType;
import com.joenastan.sleepingwar.plugin.utility.UsefulStaticFunctions;
import com.joenastan.sleepingwar.plugin.utility.CustomDerivedEntity.PlayerBedwarsEntity;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Openable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import net.md_5.bungee.api.ChatColor;

public class LockedEntities {

    private Location locLocked;
    private Block blockReference;
    private Openable openableBlock = null;
    private boolean unlocked = false;
    protected LockedEntityType typeLock = LockedEntityType.NORMAL_LOCK;
    protected Map<ResourcesType, Integer> requirements;

    /**
     * Automatically get block that will be lock.
     * @param locLocked Block Location, if there's no entity then it is null
     */
    public LockedEntities(Location locLocked, Map<ResourcesType, Integer> requirements) {
        this.locLocked = locLocked;
        this.requirements = requirements;
        Block block = locLocked.getBlock();
        if (block != null) {
            blockReference = block;
            Material blockType = block.getType();
            if (UsefulStaticFunctions.isStandardDoor(blockType) || UsefulStaticFunctions.isFenceGate(blockType) || 
                    UsefulStaticFunctions.isTrapDoor(blockType)) {
                if (block.getBlockData() instanceof Openable) {
                    openableBlock = (Openable)block.getBlockData();
                    if (openableBlock.isOpen())
                        openableBlock.setOpen(false);
                }
            }
        }
    }

    /**
     * Locked door location.
     */
    public Location getLockedLocation() {
        return locLocked;
    }

    /**
     * Block which was refereced when it was constructed.
     * @return if not exists then it returns null 
     */
    public Block getBlockReference() {
        return blockReference;
    }

    /**
     * Check if it is already unlocked.
     * @return True if it is already, else then false
     */
    public boolean isUnlocked() {
        return unlocked;
    }

    /**
     * Unlock the door with player entity in bedwars. If the requirements is empty it will premanently locked.
     * @param keyEntity Requirement to unlock
     * @return True if successfully unlocked, else then false
     */
    public boolean unlockEntity(PlayerBedwarsEntity keyEntity) {
        // is permanent locked
        if (requirements.isEmpty()) {
            keyEntity.getPlayer().sendMessage(ChatColor.RED + "This gate is permanently locked.");
            return false;
        }
        // You don't need to unlock the unlocked entity
        if (unlocked) 
            return true;
        // Check if the requirement has been fulfilled
        for (Map.Entry<ResourcesType, Integer> rEntry : requirements.entrySet()) {
            if (rEntry.getKey() == ResourcesType.IRON) {
                if (!fulfillByInventoryItems(keyEntity.getPlayer().getInventory(), Material.IRON_INGOT, rEntry.getValue())) {
                    sendRequirements(keyEntity.getPlayer());
                    return false;
                }
            } else if (rEntry.getKey() == ResourcesType.GOLD) {
                if (!fulfillByInventoryItems(keyEntity.getPlayer().getInventory(), Material.GOLD_INGOT, rEntry.getValue())) {
                    sendRequirements(keyEntity.getPlayer());
                    return false;
                }
            } else if (rEntry.getKey() == ResourcesType.DIAMOND) {
                if (!fulfillByInventoryItems(keyEntity.getPlayer().getInventory(), Material.DIAMOND, rEntry.getValue())) {
                    sendRequirements(keyEntity.getPlayer());
                    return false;
                }
            } else if (rEntry.getKey() == ResourcesType.EMERALD) {
                if (!fulfillByInventoryItems(keyEntity.getPlayer().getInventory(), Material.EMERALD, rEntry.getValue())) {
                    sendRequirements(keyEntity.getPlayer());
                    return false;
                }
            }
        }
        // Eat all resources from player's data
        for (Map.Entry<ResourcesType, Integer> rEntry : requirements.entrySet()) {
            if (rEntry.getKey() == ResourcesType.IRON) {
                UsefulStaticFunctions.removeItemInventory(keyEntity.getPlayer().getInventory(), Material.IRON_INGOT, rEntry.getValue());
            } else if (rEntry.getKey() == ResourcesType.GOLD) {
                UsefulStaticFunctions.removeItemInventory(keyEntity.getPlayer().getInventory(), Material.GOLD_INGOT, rEntry.getValue());
            } else if (rEntry.getKey() == ResourcesType.DIAMOND) {
                UsefulStaticFunctions.removeItemInventory(keyEntity.getPlayer().getInventory(), Material.DIAMOND, rEntry.getValue());
            } else if (rEntry.getKey() == ResourcesType.EMERALD) {
                UsefulStaticFunctions.removeItemInventory(keyEntity.getPlayer().getInventory(), Material.EMERALD, rEntry.getValue());
            }
        }
        unlocked = true;
        return unlocked;
    }

    /**
     * Whenever it requires items from player's inventory.
     * @param playerInv Player's refered inventory
     * @param find Find all items with material type
     * @param require Required amount
     * @return True if player has amount of required items, else then false
     */
    private boolean fulfillByInventoryItems(PlayerInventory playerInv, Material find, int require) {
        int currentItemHas = 0;
        for (ItemStack itm : playerInv.getContents()) {
            if (itm == null) 
                continue;
            if (itm.getType() == find) {
                currentItemHas += itm.getAmount();
                if (currentItemHas >= require)
                    return true;
            }
        }
        return false;
    }

    /**
     * Send a description of requirements.
     * @param player Refered Player
     */
    private void sendRequirements(Player player) {
        player.sendMessage(ChatColor.YELLOW + "You need these requirements to unlock:");
        String description = "";
        for (Map.Entry<ResourcesType, Integer> rEntry : requirements.entrySet()) {
            if (rEntry.getKey() == ResourcesType.IRON) {
                description += String.format("%s%d Iron; ", ChatColor.GRAY + "", rEntry.getValue());
            } else if (rEntry.getKey() == ResourcesType.GOLD) {
                description += String.format("%s%d Gold; ", ChatColor.GOLD + "", rEntry.getValue());
            } else if (rEntry.getKey() == ResourcesType.DIAMOND) {
                description += String.format("%s%d Diamond; ", ChatColor.AQUA + "", rEntry.getValue());
            } else if (rEntry.getKey() == ResourcesType.EMERALD) {
                description += String.format("%s%d Emerald; ", ChatColor.GREEN + "", rEntry.getValue());
            }
        }
        player.sendMessage(description);
    }
}
