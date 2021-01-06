package com.joenastan.sleepingwars.game.ItemPrice;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class PricesItemsPotion extends PricesItems {

    private PotionData potionBaseData;
    private final List<PotionEffect> effects = new ArrayList<>();

    public PricesItemsPotion(@Nonnull Material item, @Nonnull Material currency, @Nonnull String itemName, int price,
                             int defaultAmountGetter, @Nullable List<String> lore, PotionData potionBaseData) {
        super(item, currency, itemName, price, defaultAmountGetter, lore);
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
        if (meta == null)
            refreshMeta(thisItem, true);
        thisItem.setItemMeta(meta);
        return thisItem;
    }

    @Override
    public ItemStack createItem(int amount) {
        if (potionBaseData == null)
            return null;
        // Create new potion item
        ItemStack thisItem = new ItemStack(item, amount);
        if (meta == null)
            refreshMeta(thisItem, true);
        thisItem.setItemMeta(meta);
        return thisItem;
    }

    @Override
    public void refreshMeta(ItemStack itemSample, boolean showPrice) {
        super.refreshMeta(itemSample, showPrice);
        PotionMeta potionMeta = (PotionMeta) meta;
        potionMeta.clearCustomEffects();
        for (PotionEffect ef : effects)
            potionMeta.addCustomEffect(ef, true);
        itemSample.setItemMeta(potionMeta);
    }

    // Potion only
    public void setPotionData(PotionData data) {
        potionBaseData = data;
    }

    public PotionData getPotionBaseData() {
        return potionBaseData;
    }

    public List<PotionEffect> getDrinkEffects() {
        return effects;
    }

    public void addPotionEffect(PotionEffect potionEffect) {
        effects.add(potionEffect);
    }
}
