package com.joenastan.sleepingwars.game.ItemPrice;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class PricetagItems implements Pricetag {

    protected Material item;
    protected Material currency;
    protected ItemMeta meta;
    protected int defaultAmountGetter;

    private final int defaultPrice;
    private final List<String> lore = new ArrayList<>();
    private final List<ItemFlag> flags = new ArrayList<>();
    private final String itemName;

    private int price;
    private ChatColor displayColor = ChatColor.WHITE;

    public PricetagItems(@Nonnull Material item, @Nonnull Material currency, @Nonnull String itemName,
                         int price, int defaultAmountGetter, @Nullable List<String> lore) {
        this.item = item;
        this.currency = currency;
        this.price = price;
        this.itemName = itemName;
        defaultPrice = price;
        this.defaultAmountGetter = defaultAmountGetter;
        if (lore != null)
            this.lore.addAll(lore);
    }

    public ItemStack createItem() {
        ItemStack thisItem = new ItemStack(item, defaultAmountGetter);
        if (meta == null)
            refreshMeta(thisItem, true);
        thisItem.setItemMeta(meta);
        return thisItem;
    }

    public ItemStack createItem(int amount) {
        ItemStack thisItem = new ItemStack(item, amount);
        if (meta == null)
            refreshMeta(thisItem, true);
        thisItem.setItemMeta(meta);
        return thisItem;
    }

    /**
     * Run this function to refresh the meta.
     */
    public void refreshMeta(ItemStack itemSample, boolean showPrice) {
        // Add price tag to the lore
        meta = itemSample.getItemMeta();
        meta.setDisplayName(displayColor + "" + itemName);
        lore.clear();
        if (showPrice) {
            lore.add(" ");
            lore.add(getPriceTag());
        }
        for (ItemFlag f : flags)
            meta.addItemFlags(f);
        meta.setLore(lore);
    }

    public void addItemFlag(ItemFlag flag) {
        if (!flags.contains(flag))
            flags.add(flag);
    }

    public void setDisplayColor(ChatColor color) {
        displayColor = color;
    }

    protected String getPriceTag() {
        switch (currency) {
            case IRON_INGOT:
                return String.format("%s%d %s", ChatColor.GRAY + "", price, "Iron(s)");
            case GOLD_INGOT:
                return String.format("%s%d %s", ChatColor.YELLOW + "", price, "Gold(s)");
            case DIAMOND:
                return String.format("%s%d %s", ChatColor.AQUA + "", price, "Diamond(s)");
            case EMERALD:
                return String.format("%s%d %s", ChatColor.GREEN + "", price, "Emerald(s)");
            default:
                return String.format("%s%d %s", ChatColor.WHITE + "", price, currency.getKey().getNamespace());
        }
    }

    public void setPrice(int price) {
        this.price = price;
        List<String> updateLore = meta.getLore();
        // Remove line of current price tag
        updateLore.remove(updateLore.size() - 1);
        // Read line of new price tag
        updateLore.add(getPriceTag());
        // Update Lore
        meta.setLore(updateLore);
    }

    public int getPrice() {
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

    public Material getItemMaterial() {
        return item;
    }

    public List<String> getLore() {
        return lore;
    }

    public int getDefaultAmount() {
        return defaultAmountGetter;
    }

    public ItemMeta getMeta() {
        if (meta == null)
            refreshMeta(new ItemStack(item), true);
        return meta;
    }

    public String getName() {
        return itemName;
    }
}
