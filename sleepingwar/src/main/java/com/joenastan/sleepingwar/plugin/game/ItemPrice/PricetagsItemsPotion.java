package com.joenastan.sleepingwar.plugin.game.ItemPrice;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;

public class PricetagsItemsPotion extends PricetagsItems {

    private PotionData potionData;

    public PricetagsItemsPotion(Material item, Material currency, int price, ItemMeta meta, int defaultAmountGetter, PotionData potionData){
        super(item, currency, price, meta, defaultAmountGetter);
        try {
            if (item == Material.POTION || item == Material.TIPPED_ARROW) {
                this.potionData = potionData;
            } else {
                throw new Exception("Invalid item, the item is not POTION-able.");
            }
        } catch (Exception exc) {
            this.potionData = null;
        }
    }

    @Override
    public ItemStack createItem() {
        if (potionData == null)
            return null;

        ItemStack thisItem = new ItemStack(item, defaultAmountGetter);
        PotionMeta potionMeta = (PotionMeta)meta;
        potionMeta.setBasePotionData(potionData);
        return thisItem;
    }

    @Override
    public ItemStack createItem(int amount) {
        if (potionData == null)
            return null;

        ItemStack thisItem = new ItemStack(item, amount);
        PotionMeta potionMeta = (PotionMeta)meta;
        potionMeta.setBasePotionData(potionData);
        return thisItem;
    }

    // Potion only
    public PricetagsItemsPotion setPotionData(PotionData data) {
        potionData = data;
        return this;
    }

    public PotionData getPotionData() {
        return potionData;
    }

}
