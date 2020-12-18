package com.joenastan.sleepingwars.enumtypes;

import net.md_5.bungee.api.ChatColor;

public enum BedwarsShopType {
    ITEMS_SHOP("ItemShop"),
    PERMA_SHOP("PermanentShop");

    private final String shopName;

    BedwarsShopType(String shopName) {
        this.shopName = shopName;
    }

    /**
     * Get shop type by name or string. Name must be specific to get the type.
     *
     * @param shopType Name of shop type
     * @return Shop Type, if not exists then it returns null
     */
    public static BedwarsShopType fromString(String shopType) {
        if (shopType.equalsIgnoreCase(PERMA_SHOP.toString())) {
            return PERMA_SHOP;
        } else if (shopType.equalsIgnoreCase(ITEMS_SHOP.toString())) {
            return ITEMS_SHOP;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return shopName;
    }

}
