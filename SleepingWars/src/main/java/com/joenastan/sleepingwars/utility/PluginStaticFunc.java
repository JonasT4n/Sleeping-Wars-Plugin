package com.joenastan.sleepingwars.utility;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PluginStaticFunc {

    /**
     * Remove items in any inventory by specific item type.
     *
     * @param inv      Inventory reference
     * @param itemType Material type
     * @param amount   amount to be remove
     */
    public static void removeItemInventory(Inventory inv, Material itemType, int amount) {
        for (ItemStack itm : inv.getContents()) {
            if (itm == null)
                continue;
            if (itm.getType() == itemType) {
                if (itm.getAmount() < amount) {
                    amount -= itm.getAmount();
                    itm.setAmount(0);
                } else {
                    itm.setAmount(itm.getAmount() - amount);
                    amount = 0;
                }
            }
            if (amount <= 0)
                break;
        }
    }

    /**
     * Add items into inventory.
     *
     * @param inv      Inventory reference
     * @param itemType Material type
     * @param amount   amount to be remove
     */
    public static void addItemIntoInventory(Inventory inv, Material itemType, int amount) {
        int maxStack = (amount / 64) + 1;
        for (int i = 0; i < maxStack; i++) {
            if (i == maxStack - 1) {
                inv.addItem(new ItemStack(itemType, amount));
            } else {
                inv.addItem(new ItemStack(itemType, 64));
                amount -= 64;
            }
        }
    }

    /**
     * Search an item in inventory.
     *
     * @param inv Inventory reference
     * @param itemType Material type
     * @return First Inventory slot when found, else then it returns -1
     */
    public static int searchItemInventory(Inventory inv, Material itemType) {
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack == null)
                continue;

            if (stack.getType() == itemType)
                return i;
        }
        return -1;
    }

    public static boolean isSword(Material mat) {
        switch (mat) {
            case WOODEN_SWORD:
            case STONE_SWORD:
            case IRON_SWORD:
            case GOLDEN_SWORD:
            case DIAMOND_SWORD:
            case NETHERITE_SWORD:
                return true;

            default:
                return false;
        }
    }

    public static boolean isPickaxe(Material mat) {
        switch (mat) {
            case WOODEN_PICKAXE:
            case STONE_PICKAXE:
            case IRON_PICKAXE:
            case GOLDEN_PICKAXE:
            case DIAMOND_PICKAXE:
            case NETHERITE_PICKAXE:
                return true;

            default:
                return false;
        }
    }

    public static boolean isAxe(Material mat) {
        switch (mat) {
            case WOODEN_AXE:
            case STONE_AXE:
            case IRON_AXE:
            case GOLDEN_AXE:
            case DIAMOND_AXE:
            case NETHERITE_AXE:
                return true;
            default:
                return false;
        }
    }

    public static boolean isButton(Material mat) {
        switch (mat) {
            case OAK_BUTTON:
            case ACACIA_BUTTON:
            case BIRCH_BUTTON:
            case CRIMSON_BUTTON:
            case DARK_OAK_BUTTON:
            case JUNGLE_BUTTON:
            case POLISHED_BLACKSTONE_BUTTON:
            case SPRUCE_BUTTON:
            case STONE_BUTTON:
            case WARPED_BUTTON:
                return true;
            default:
                return false;
        }
    }

    public static boolean isBed(Material mat) {
        switch (mat) {
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

    public static boolean isHumanEntityArmor(Material material) {
        switch (material) {
            case LEATHER_BOOTS:
            case LEATHER_CHESTPLATE:
            case LEATHER_HELMET:
            case LEATHER_LEGGINGS:
            case TURTLE_HELMET:
            case CHAINMAIL_BOOTS:
            case CHAINMAIL_CHESTPLATE:
            case CHAINMAIL_HELMET:
            case CHAINMAIL_LEGGINGS:
            case IRON_BOOTS:
            case IRON_CHESTPLATE:
            case IRON_HELMET:
            case IRON_LEGGINGS:
            case GOLDEN_BOOTS:
            case GOLDEN_CHESTPLATE:
            case GOLDEN_HELMET:
            case GOLDEN_LEGGINGS:
            case DIAMOND_BOOTS:
            case DIAMOND_CHESTPLATE:
            case DIAMOND_LEGGINGS:
            case DIAMOND_HELMET:
            case NETHERITE_BOOTS:
            case NETHERITE_LEGGINGS:
            case NETHERITE_HELMET:
            case NETHERITE_CHESTPLATE:
                return true;
            default:
                return false;
        }
    }

    public static boolean isGateOrDoor(Material mat) {
        return isStandardDoor(mat) || isFenceGate(mat) || isTrapDoor(mat);
    }

    public static boolean isStandardDoor(Material mat) {
        switch (mat) {
            case DARK_OAK_DOOR:
            case OAK_DOOR:
            case SPRUCE_DOOR:
            case JUNGLE_DOOR:
            case BIRCH_DOOR:
            case ACACIA_DOOR:
            case CRIMSON_DOOR:
            case WARPED_DOOR:
                return true;
            default:
                return false;
        }
    }

    public static boolean isTrapDoor(Material mat) {
        switch (mat) {
            case IRON_TRAPDOOR:
            case OAK_TRAPDOOR:
            case DARK_OAK_TRAPDOOR:
            case SPRUCE_TRAPDOOR:
            case JUNGLE_TRAPDOOR:
            case BIRCH_TRAPDOOR:
            case ACACIA_TRAPDOOR:
            case CRIMSON_TRAPDOOR:
            case WARPED_TRAPDOOR:
                return true;
            default:
                return false;
        }
    }

    public static boolean isFenceGate(Material mat) {
        switch (mat) {
            case OAK_FENCE_GATE:
            case SPRUCE_FENCE_GATE:
            case BIRCH_FENCE_GATE:
            case JUNGLE_FENCE_GATE:
            case ACACIA_FENCE_GATE:
            case DARK_OAK_FENCE_GATE:
            case CRIMSON_FENCE_GATE:
            case WARPED_FENCE_GATE:
                return true;
            default:
                return false;
        }
    }
}
