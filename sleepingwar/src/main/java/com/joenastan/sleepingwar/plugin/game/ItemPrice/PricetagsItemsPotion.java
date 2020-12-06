package com.joenastan.sleepingwar.plugin.game.ItemPrice;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

public class PricetagsItemsPotion extends PricetagsItems {

    private PotionData potionBaseData;
    private List<PotionEffect> effects = new ArrayList<PotionEffect>();

    public PricetagsItemsPotion(Material item, Material currency, int price, ItemMeta meta, int defaultAmountGetter, PotionData potionBaseData) {
        super(item, currency, price, meta, defaultAmountGetter);
        try {
            if (item == Material.POTION || item == Material.TIPPED_ARROW) {
                this.potionBaseData = potionBaseData;
            } else {
                throw new Exception("Invalid item, the item is not POTION-able.");
            }
        } catch (Exception exc) {
            this.potionBaseData = null;
            System.out.println(exc.getCause());
        }
    }

    @Override
    public ItemStack createItem() {
        if (potionBaseData == null)
            return null;
        // Create new potion item
        ItemStack thisItem = new ItemStack(item, defaultAmountGetter);
        PotionMeta potionMeta = (PotionMeta) meta;
        potionMeta.setBasePotionData(potionBaseData);
        for (PotionEffect effect : effects) {
            potionMeta.addCustomEffect(effect, true);
        }
        thisItem.setItemMeta(potionMeta);
        return thisItem;
    }

    @Override
    public ItemStack createItem(int amount) {
        if (potionBaseData == null)
            return null;
        // Create new potion item
        ItemStack thisItem = new ItemStack(item, amount);
        PotionMeta potionMeta = (PotionMeta) meta;
        potionMeta.setBasePotionData(potionBaseData);
        for (PotionEffect effect : effects) {
            potionMeta.addCustomEffect(effect, true);
        }
        thisItem.setItemMeta(potionMeta);
        return thisItem;
    }

    // Potion only
    public PricetagsItemsPotion setPotionData(PotionData data) {
        potionBaseData = data;
        return this;
    }

    public PotionData getPotionEffect() {
        return potionBaseData;
    }

    public void addPotionEffect(PotionEffect potionEffect) {
        effects.add(potionEffect);
    }
}
