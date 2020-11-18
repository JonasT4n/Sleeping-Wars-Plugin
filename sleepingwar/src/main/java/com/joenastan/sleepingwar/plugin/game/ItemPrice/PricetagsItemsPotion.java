package com.joenastan.sleepingwar.plugin.game.ItemPrice;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

public class PricetagsItemsPotion extends PricetagsItems {

    private PotionEffect potionEffect;

    public PricetagsItemsPotion(Material item, Material currency, int price, ItemMeta meta, int defaultAmountGetter, PotionEffect potionEffect){
        super(item, currency, price, meta, defaultAmountGetter);
        try {
            if (item == Material.POTION || item == Material.TIPPED_ARROW) {
                this.potionEffect = potionEffect;
            } else {
                throw new Exception("Invalid item, the item is not POTION-able.");
            }
        } catch (Exception exc) {
            this.potionEffect = null;
            System.out.println(exc.getCause());
        }
    }

    @Override
    public ItemStack createItem() {
        if (potionEffect == null)
            return null;

        ItemStack thisItem = new ItemStack(item, defaultAmountGetter);
        PotionMeta potionMeta = (PotionMeta)meta;
        potionMeta.addCustomEffect(potionEffect, true);
        return thisItem;
    }

    @Override
    public ItemStack createItem(int amount) {
        if (potionEffect == null)
            return null;

        ItemStack thisItem = new ItemStack(item, amount);
        PotionMeta potionMeta = (PotionMeta)meta;
        potionMeta.addCustomEffect(potionEffect, true);
        return thisItem;
    }

    // Potion only
    public PricetagsItemsPotion setPotionData(PotionEffect effect) {
        potionEffect = effect;
        return this;
    }

    public PotionEffect getPotionEffect() {
        return potionEffect;
    }

}
