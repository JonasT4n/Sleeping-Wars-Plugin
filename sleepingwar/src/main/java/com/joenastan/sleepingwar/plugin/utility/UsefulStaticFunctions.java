package com.joenastan.sleepingwar.plugin.utility;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class UsefulStaticFunctions {

    /**
     * Remove items in any inventory by specific item type.
     *
     * @param inv      Inventory referenced
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
     * @param inv      Inventory referenced
     * @param itemType Material type
     * @param amount   amount to be remove
     */
    public static void addItemInventory(Inventory inv, Material itemType, int amount) {
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

    public static boolean isSword(Material mat) {
        switch (mat) {
            case WOODEN_SWORD:
                return true;

            case STONE_SWORD:
                return true;

            case IRON_SWORD:
                return true;

            case GOLDEN_SWORD:
                return true;

            case DIAMOND_SWORD:
                return true;

            case NETHERITE_SWORD:
                return true;

            default:
                return false;
        }
    }

    public static boolean isPickaxe(Material mat) {
        switch (mat) {
            case WOODEN_PICKAXE:
                return true;

            case STONE_PICKAXE:
                return true;

            case IRON_PICKAXE:
                return true;

            case GOLDEN_PICKAXE:
                return true;

            case DIAMOND_PICKAXE:
                return true;

            case NETHERITE_PICKAXE:
                return true;

            default:
                return false;
        }
    }

    public static boolean isAxe(Material mat) {
        switch (mat) {
            case WOODEN_AXE:
                return true;

            case STONE_AXE:
                return true;

            case IRON_AXE:
                return true;

            case GOLDEN_AXE:
                return true;

            case DIAMOND_AXE:
                return true;

            case NETHERITE_AXE:
                return true;

            default:
                return false;
        }
    }

    public static boolean isMaterialBed(Material material) {
        switch (material) {
            case BLACK_BED:
                return true;
            case BLUE_BED:
                return true;
            case BROWN_BED:
                return true;
            case CYAN_BED:
                return true;
            case GREEN_BED:
                return true;
            case YELLOW_BED:
                return true;
            case RED_BED:
                return true;
            case ORANGE_BED:
                return true;
            case GRAY_BED:
                return true;
            case LIGHT_BLUE_BED:
                return true;
            case LIME_BED:
                return true;
            case PINK_BED:
                return true;
            case MAGENTA_BED:
                return true;
            case PURPLE_BED:
                return true;
            case WHITE_BED:
                return true;
            case LIGHT_GRAY_BED:
                return true;
            default:
                return false;
        }
    }

    public static boolean isHumanEntityArmor(Material material) {
        switch (material) {
            case LEATHER_BOOTS:
                return true;
            case LEATHER_CHESTPLATE:
                return true;
            case LEATHER_HELMET:
                return true;
            case LEATHER_LEGGINGS:
                return true;
            case TURTLE_HELMET:
                return true;
            case CHAINMAIL_BOOTS:
                return true;
            case CHAINMAIL_CHESTPLATE:
                return true;
            case CHAINMAIL_HELMET:
                return true;
            case CHAINMAIL_LEGGINGS:
                return true;
            case IRON_BOOTS:
                return true;
            case IRON_CHESTPLATE:
                return true;
            case IRON_HELMET:
                return true;
            case IRON_LEGGINGS:
                return true;
            case GOLDEN_BOOTS:
                return true;
            case GOLDEN_CHESTPLATE:
                return true;
            case GOLDEN_HELMET:
                return true;
            case GOLDEN_LEGGINGS:
                return true;
            case DIAMOND_BOOTS:
                return true;
            case DIAMOND_CHESTPLATE:
                return true;
            case DIAMOND_LEGGINGS:
                return true;
            case DIAMOND_HELMET:
                return true;
            case NETHERITE_BOOTS:
                return true;
            case NETHERITE_LEGGINGS:
                return true;
            case NETHERITE_HELMET:
                return true;
            case NETHERITE_CHESTPLATE:
                return true;
            default:
                return false;
        }
    }

    public static boolean isStandardDoor(Material mat) {
        switch (mat) {
            case IRON_DOOR:
                return true;

            case DARK_OAK_DOOR:
                return true;

            case OAK_DOOR:
                return true;

            case SPRUCE_DOOR:
                return true;

            case JUNGLE_DOOR:
                return true;

            case BIRCH_DOOR:
                return true;

            case ACACIA_DOOR:
                return true;

            case CRIMSON_DOOR:
                return true;

            case WARPED_DOOR:
                return true;

            default:
                return false;
        }
    }

    public static boolean isTrapDoor(Material mat) {
        switch (mat) {
            case IRON_TRAPDOOR:
                return true;

            case OAK_TRAPDOOR:
                return true;

            case DARK_OAK_TRAPDOOR:
                return true;

            case SPRUCE_TRAPDOOR:
                return true;

            case JUNGLE_TRAPDOOR:
                return true;

            case BIRCH_TRAPDOOR:
                return true;

            case ACACIA_TRAPDOOR:
                return true;

            case CRIMSON_TRAPDOOR:
                return true;

            case WARPED_TRAPDOOR:
                return true;

            default:
                return false;
        }
    }

    public static boolean isFenceGate(Material mat) {
        switch (mat) {
            case OAK_FENCE_GATE:
                return true;

            case SPRUCE_FENCE_GATE:
                return true;

            case BIRCH_FENCE_GATE:
                return true;

            case JUNGLE_FENCE_GATE:
                return true;

            case ACACIA_FENCE_GATE:
                return true;

            case DARK_OAK_FENCE_GATE:
                return true;

            case CRIMSON_FENCE_GATE:
                return true;

            case WARPED_FENCE_GATE:
                return true;

            default:
                return false;
        }
    }

    /**
     * Team color string.
     *
     * @return returns the color code, if the color not match any, then it returns white
     */
    public static String getColorString(String colorString) {
        if (colorString.equalsIgnoreCase("blue")) {
            return ChatColor.BLUE + "";
        } else if (colorString.equalsIgnoreCase("green")) {
            return ChatColor.GREEN + "";
        } else if (colorString.equalsIgnoreCase("yellow")) {
            return ChatColor.YELLOW + "";
        } else if (colorString.equalsIgnoreCase("aqua")) {
            return ChatColor.AQUA + "";
        } else if (colorString.equalsIgnoreCase("red")) {
            return ChatColor.RED + "";
        } else if (colorString.equalsIgnoreCase("purple")) {
            return ChatColor.LIGHT_PURPLE + "";
        } else if (colorString.equalsIgnoreCase("gold")) {
            return ChatColor.GOLD + "";
        } else if (colorString.equalsIgnoreCase("gray")) {
            return ChatColor.GRAY + "";
        } else { // Default is White
            return ChatColor.WHITE + "";
        }
    }
}
