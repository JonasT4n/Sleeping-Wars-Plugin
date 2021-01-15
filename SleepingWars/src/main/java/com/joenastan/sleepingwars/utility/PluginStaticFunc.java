package com.joenastan.sleepingwars.utility;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashSet;

public class PluginStaticFunc {

    public static final HashSet<String> MATERIALS = new HashSet<>();

    public static void InitStatics() {
        if (MATERIALS.isEmpty()) {
            for (Material m : Material.values())
                MATERIALS.add(m.toString());

            // TODO: Remove non item materials
        }
    }

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
     * Check if world is already exists in server folder.
     *
     * @param roomName Name of room
     * @return True if exists, else then false
     */
    public static boolean isFolderWorldExists(String roomName) {
        File f = new File(Bukkit.getWorldContainer().getParentFile(), roomName);
        System.out.println(f.getAbsolutePath());
        return f.exists();
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

    // TODO: Remove this after generalization
    // Temporary Function to determine a currencies in game
    public static boolean isCurrencyInGame(Material mat) {
        switch (mat) {
            case IRON_INGOT:
            case GOLD_INGOT:
            case DIAMOND:
            case EMERALD:
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
    public static String stringToChatColor(String colorString) {
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
        } else if (colorString.equalsIgnoreCase("cyan")) {
            return ChatColor.AQUA + "";
        } else { // Default is White
            return ChatColor.WHITE + "";
        }
    }

    /**
     * Define wool color by string.
     *
     * @param color Raw string color
     * @return Colored wool material
     */
    public static Material woolColor(String color) {
        if (color.equalsIgnoreCase("blue")) {
            return Material.BLUE_WOOL;
        } else if (color.equalsIgnoreCase("green")) {
            return Material.GREEN_WOOL;
        } else if (color.equalsIgnoreCase("yellow")) {
            return Material.YELLOW_WOOL;
        } else if (color.equalsIgnoreCase("aqua")) {
            return Material.CYAN_WOOL;
        } else if (color.equalsIgnoreCase("red")) {
            return Material.RED_WOOL;
        } else if (color.equalsIgnoreCase("purple")) {
            return Material.MAGENTA_WOOL;
        } else if (color.equalsIgnoreCase("gold")) {
            return Material.YELLOW_WOOL;
        } else if (color.equalsIgnoreCase("gray")) {
            return Material.GRAY_WOOL;
        } else if (color.equalsIgnoreCase("cyan")) {
            return Material.CYAN_WOOL;
        } else { // Default is White
            return Material.WHITE_WOOL;
        }
    }

    /**
     * Define chat color of currency in game.
     *
     * @param mat Material
     * @return Chat color code in string
     */
    public static String currencyColor(Material mat) {
        switch (mat) {
            case IRON_INGOT:
                return ChatColor.GRAY + "";
            case GOLD_INGOT:
                return ChatColor.GOLD + "";
            case DIAMOND:
                return ChatColor.AQUA + "";
            case EMERALD:
                return ChatColor.GREEN + "";
            default:
                return ChatColor.WHITE + "";
        }
    }
}
