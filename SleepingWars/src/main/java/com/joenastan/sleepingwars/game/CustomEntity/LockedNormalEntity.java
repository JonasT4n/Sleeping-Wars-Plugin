package com.joenastan.sleepingwars.game.CustomEntity;

import java.util.Map;

import com.joenastan.sleepingwars.enumtypes.LockedEntityType;
import com.joenastan.sleepingwars.utility.Hologram.Hologram;
import com.joenastan.sleepingwars.utility.PluginStaticFunc;
import com.joenastan.sleepingwars.utility.CustomEntity.PlayerBedwarsEntity;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Openable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import javax.annotation.Nonnull;

public class LockedNormalEntity {

    protected LockedEntityType typeLock = LockedEntityType.NORMAL_LOCK;
    protected Map<Material, Integer> requirements;

    private final Location locLocked;
    private final Block blockReference;

    private boolean unlocked = false;
    private Hologram holoSign;

    /**
     * Automatically get block that will be lock.
     *
     * @param locLocked Block Location, if there's no entity then it is null
     */
    public LockedNormalEntity(@Nonnull Location locLocked, Map<Material, Integer> requirements) {
        this.locLocked = locLocked;
        this.requirements = requirements;
        Block block = locLocked.getBlock();
        blockReference = block;
        Material blockType = block.getType();
        if (PluginStaticFunc.isGateOrDoor(blockType)) {
            if (block.getBlockData() instanceof Openable) {
                Openable openableBlock = (Openable) block.getBlockData();
                if (openableBlock.isOpen())
                    openableBlock.setOpen(false);
            }
        }

        // Create hologram sign
        holoSign = new Hologram(new Location(locLocked.getWorld(), locLocked.getBlockX() + 0.5d,
                locLocked.getBlockY() - 1d, locLocked.getBlockZ() + 0.25d));
        if (requirements.isEmpty()) {
            holoSign.addLine(ChatColor.RED + "Permanently locked");
        } else {
            holoSign.addLine(ChatColor.YELLOW + "Required:");
            for (Map.Entry<Material, Integer> r_entry : requirements.entrySet()) {
                if (r_entry.getKey() == Material.IRON_INGOT)
                    holoSign.addLine(ChatColor.GRAY + String.format("%d IRON INGOT(s)", r_entry.getValue()));
                else if (r_entry.getKey() == Material.GOLD_INGOT)
                    holoSign.addLine(ChatColor.GOLD + String.format("%d GOLD INGOT(s)", r_entry.getValue()));
                else if (r_entry.getKey() == Material.DIAMOND)
                    holoSign.addLine(ChatColor.AQUA + String.format("%d DIAMOND(s)", r_entry.getValue()));
                else if (r_entry.getKey() == Material.EMERALD)
                    holoSign.addLine(ChatColor.GREEN + String.format("%d EMERALD(s)", r_entry.getValue()));
                else
                    holoSign.addLine(String.format("%d Undefined(s)", r_entry.getValue()));
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
     * Block which was referenced when it was constructed.
     *
     * @return if not exists then it returns null
     */
    public Block getBlockReference() {
        return blockReference;
    }

    /**
     * Check if it is already unlocked.
     *
     * @return True if it is already, else then false
     */
    public boolean isUnlocked() {
        return unlocked;
    }

    /**
     * Unlock the door with player entity in bedwars.
     * If the requirements is empty it will permanently locked.
     *
     * @param keyEntity Requirement to unlock
     * @return True if successfully unlocked, else then false
     */
    public boolean unlockEntity(PlayerBedwarsEntity keyEntity) {
        // is permanent locked
        if (requirements.isEmpty()) {
            keyEntity.getPlayer().sendMessage(ChatColor.RED + "This entity is permanently locked.");
            return false;
        }

        // You don't need to unlock the unlocked entity
        if (unlocked)
            return true;

        // Check if the requirement has been fulfilled
        for (Map.Entry<Material, Integer> rEntry : requirements.entrySet()) {
            if (rEntry.getKey() == Material.IRON_INGOT) {
                if (checkAffordable(keyEntity.getPlayer().getInventory(), Material.IRON_INGOT, rEntry.getValue())) {
                    sendRequirements(keyEntity.getPlayer());
                    return false;
                }
            } else if (rEntry.getKey() == Material.GOLD_INGOT) {
                if (checkAffordable(keyEntity.getPlayer().getInventory(), Material.GOLD_INGOT, rEntry.getValue())) {
                    sendRequirements(keyEntity.getPlayer());
                    return false;
                }
            } else if (rEntry.getKey() == Material.DIAMOND) {
                if (checkAffordable(keyEntity.getPlayer().getInventory(), Material.DIAMOND, rEntry.getValue())) {
                    sendRequirements(keyEntity.getPlayer());
                    return false;
                }
            } else if (rEntry.getKey() == Material.EMERALD) {
                if (checkAffordable(keyEntity.getPlayer().getInventory(), Material.EMERALD, rEntry.getValue())) {
                    sendRequirements(keyEntity.getPlayer());
                    return false;
                }
            }
        }

        // Eat all resources from player's data
        for (Map.Entry<Material, Integer> rEntry : requirements.entrySet())
            PluginStaticFunc.removeItemInventory(keyEntity.getPlayer().getInventory(),
                    rEntry.getKey(), rEntry.getValue());

        // Unlocked
        holoSign.clear();
        holoSign = null;
        unlocked = true;
        return true;
    }

    /**
     * Whenever it requires items from player's inventory.
     *
     * @param playerInv Player's referred inventory
     * @param find      Find all items with material type
     * @param require   Required amount
     * @return True if player cannot afford the requirement, else then false
     */
    private boolean checkAffordable(PlayerInventory playerInv, Material find, int require) {
        int currentItemHas = 0;
        for (ItemStack itm : playerInv.getContents()) {
            if (itm == null)
                continue;
            if (itm.getType() == find) {
                currentItemHas += itm.getAmount();
                if (currentItemHas >= require)
                    return false;
            }
        }
        return true;
    }

    /**
     * Send a description of requirements.
     *
     * @param player Referred Player
     */
    private void sendRequirements(Player player) {
        player.sendMessage(ChatColor.YELLOW + "You need these requirements to unlock:");
        StringBuilder description = new StringBuilder();
        for (Map.Entry<Material, Integer> rEntry : requirements.entrySet()) {
            if (rEntry.getKey() == Material.IRON_INGOT) {
                description.append(String.format("%s%d Iron Ingot; ", ChatColor.GRAY + "", rEntry.getValue()));
            } else if (rEntry.getKey() == Material.GOLD_INGOT) {
                description.append(String.format("%s%d Gold Ingot; ", ChatColor.GOLD + "", rEntry.getValue()));
            } else if (rEntry.getKey() == Material.DIAMOND) {
                description.append(String.format("%s%d Diamond; ", ChatColor.AQUA + "", rEntry.getValue()));
            } else if (rEntry.getKey() == Material.EMERALD) {
                description.append(String.format("%s%d Emerald; ", ChatColor.GREEN + "", rEntry.getValue()));
            }
        }
        player.sendMessage(description.toString());
    }
}
