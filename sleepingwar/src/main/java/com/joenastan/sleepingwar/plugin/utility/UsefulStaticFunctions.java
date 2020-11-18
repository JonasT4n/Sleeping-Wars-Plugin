package com.joenastan.sleepingwar.plugin.utility;

import org.bukkit.Material;

public class UsefulStaticFunctions {
    
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
}
