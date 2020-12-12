package com.joenastan.sleepingwars.game.ItemPrice;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class PricetagsItemsArmorWeapon extends PricetagsItems {

    private final Map<Enchantment, Integer> enchantments;

    public PricetagsItemsArmorWeapon(Material item, Material currency, int price, ItemMeta meta, int defaultAmountGetter, Map<Enchantment, Integer> enchancements) {
        super(item, currency, price, meta, defaultAmountGetter);
        this.enchantments = enchancements;
    }

    @Override
    public ItemStack createItem() {
        ItemStack thisItem = super.createItem(defaultAmountGetter);
        thisItem.addEnchantments(enchantments);
        return thisItem;
    }

    @Override
    public ItemStack createItem(int amount) {
        ItemStack thisItem = super.createItem(amount);
        thisItem.addEnchantments(enchantments);
        return thisItem;
    }

    public Map<Enchantment, Integer> getListEnchant() {
        return enchantments;
    }

}
