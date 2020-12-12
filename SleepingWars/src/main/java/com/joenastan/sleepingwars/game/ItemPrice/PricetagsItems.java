package com.joenastan.sleepingwars.game.ItemPrice;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PricetagsItems {

    protected Material item;
    protected Material currency;
    protected ItemMeta meta;
    protected int defaultAmountGetter;
    private int price;
    private final int defaultPrice;

    public PricetagsItems(Material item, Material currency, int price, ItemMeta meta, int defaultAmountGetter) {
        this.item = item;
        this.currency = currency;
        this.price = price;
        defaultPrice = price;
        this.meta = meta;
        this.defaultAmountGetter = defaultAmountGetter;

        // Add price tag to the lore
        List<String> lore;
        if (this.meta.hasLore())
            lore = this.meta.getLore();
        else
            lore = new ArrayList<String>();

        lore.add(" ");
        lore.add(getPriceTag());
        this.meta.setLore(lore);
    }

    public ItemStack createItem() {
        ItemStack thisItem = new ItemStack(item, defaultAmountGetter);
        thisItem.setItemMeta(meta);
        return thisItem;
    }

    public ItemStack createItem(int amount) {
        ItemStack thisItem = new ItemStack(item, amount);
        thisItem.setItemMeta(meta);
        return thisItem;
    }

    protected String getPriceTag() {
        switch (currency) {
            case IRON_INGOT:
                return String.format("%s %d %s", ChatColor.GRAY + "", price, "Iron(s)");

            case GOLD_INGOT:
                return String.format("%s %d %s", ChatColor.YELLOW + "", price, "Gold(s)");

            case DIAMOND:
                return String.format("%s %d %s", ChatColor.AQUA + "", price, "Diamond(s)");

            case EMERALD:
                return String.format("%s %d %s", ChatColor.GREEN + "", price, "Emerald(s)");

            default:
                return String.format("%s %d %s", ChatColor.WHITE + "", price, currency.getKey().getNamespace());
        }
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
        List<String> updateLore = meta.getLore();

        // Remove line of current pricetag
        updateLore.remove(updateLore.size() - 1);

        // Readd line of new pricetag
        updateLore.add(getPriceTag());

        // Update Lore
        meta.setLore(updateLore);
    }

    public int getPrice(int price) {
        setPrice(price);
        return price;
    }

    public int getDefaultPrice() {
        return defaultPrice;
    }

    public Material getCurrency() {
        return currency;
    }

    public void setCurrency(Material currency) {
        this.currency = currency;
    }

    public Material getCurrency(Material currency) {
        this.currency = currency;
        return currency;
    }

    public Material getItem() {
        return item;
    }

    public ItemMeta getMeta() {
        return meta;
    }

    public void setMeta(ItemMeta meta) {
        this.meta = meta;
    }

    public int getDefaultAmount() {
        return defaultAmountGetter;
    }

}
