package com.joenastan.sleepingwars.game.ItemPrice;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class PricesItemsArmorWeapon extends PricesItems {

    private final Map<Enchantment, Integer> enchantments;

    public PricesItemsArmorWeapon(@Nonnull Material item, @Nonnull Material currency, @Nonnull String itemName,
                                  int price, int defaultAmountGetter, @Nullable List<String> lore,
                                  Map<Enchantment, Integer> enhancements) {
        super(item, currency, itemName, price, defaultAmountGetter, lore);
        this.enchantments = enhancements;
    }

    @Override
    public ItemStack createItem() {
        ItemStack thisItem = super.createItem();
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
