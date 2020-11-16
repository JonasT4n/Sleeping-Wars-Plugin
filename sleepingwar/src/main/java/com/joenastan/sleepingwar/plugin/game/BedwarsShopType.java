package com.joenastan.sleepingwar.plugin.game;

public enum BedwarsShopType {
    ITEMS_SHOP("ItemShop"),
    PERMA_SHOP("PermanentShop");

    private String shopName;

    private BedwarsShopType(String shopName) {
        this.shopName = shopName;
    }

    @Override
    public String toString() {
        return shopName;
    }

    public static BedwarsShopType fromString(String shopType) {
        if (shopType.equalsIgnoreCase(PERMA_SHOP.toString())) {
            return PERMA_SHOP;
        } else if (shopType.equalsIgnoreCase(ITEMS_SHOP.toString())) {
            return ITEMS_SHOP;
        } else {
            return null;
        }
    }
    
}
