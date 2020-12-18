package com.joenastan.sleepingwars.game.ItemPrice;

import org.bukkit.Material;

interface Pricetag {
    Material getCurrency();
    void setCurrency(Material currency);
    int getPrice();
    void setPrice(int price);
}
