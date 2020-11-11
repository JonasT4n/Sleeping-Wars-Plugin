package com.joenastan.sleepingwar.plugin.Game;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;

import java.util.Arrays;

public class PricetagsItemsPotion extends PricetagsItems {

    private PotionData potionData;

    public PricetagsItemsPotion(Material item, Material currency, int price, PotionData potionData) throws Exception {
        super(item, currency, price);
        if (item != Material.POTION)
            throw new Exception("Invalid item, the item is not POTION.");
        this.potionData = potionData;
    }

    // Potion only
    public PricetagsItemsPotion setPotionData(PotionData data) {
        potionData = data;
        return this;
    }

    @Override
    public ItemStack createItemStack(int amount) {
        ItemStack thisItem = new ItemStack(getItem(), amount);
        ItemMeta thisItemMeta = thisItem.getItemMeta();
        PotionMeta potionMeta = (PotionMeta) thisItemMeta;
        potionMeta.setBasePotionData(potionData);
        thisItemMeta.setLore(Arrays.asList("", getPriceTag()));
        thisItem.setItemMeta(thisItemMeta);
        return thisItem;
    }
}
