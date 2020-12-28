package com.joenastan.sleepingwars.utility;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;

public class PluginStaticColor {

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
        } else { // Default is White
            return Material.WHITE_WOOL;
        }
    }
}
