package com.joenastan.sleepingwar.plugin.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class PricetagsItems {

    private Map<Enchantment, Integer> enchanceListLevel = new HashMap<Enchantment, Integer>();
    private Material item;
    private Material currency;
    private int price;
    private int defaultPrice;

    public PricetagsItems(Material item, Material currency, int price) {
        this.item = item;
        this.currency = currency;
        this.price = price;
        defaultPrice = price;
    }

    public PricetagsItems addEnchancement(Enchantment enc, int lvl) {
        if (enchanceListLevel.containsKey(enc))
            enchanceListLevel.put(enc, lvl);

        return this;
    }

    public PricetagsItems addEnchancement(Map<Enchantment, Integer> encs) {
        for (Map.Entry<Enchantment, Integer> enc : encs.entrySet()) {
            if (enchanceListLevel.containsKey(enc.getKey()))
                enchanceListLevel.put(enc.getKey(), enc.getValue());
        }
    
        return this;
    }

    public List<Enchantment> getEnchancementList() {
        List<Enchantment> listEnc = new ArrayList<Enchantment>();
        listEnc.addAll(enchanceListLevel.keySet());
        return listEnc;
    }

    public ItemStack createItemStack(int amount) {
        ItemStack thisItem = new ItemStack(item, amount);
        ItemMeta thisItemMeta = thisItem.getItemMeta();

        for (Map.Entry<Enchantment, Integer> enchance : enchanceListLevel.entrySet()) {
            thisItemMeta.addEnchant(enchance.getKey(), enchance.getValue(), true);
        }
        
        thisItemMeta.setLore(Arrays.asList("", getPriceTag()));
        thisItem.setItemMeta(thisItemMeta);
        return thisItem;
    }

    public ItemStack createItemStack(int amount, String display) {
        ItemStack thisItem = new ItemStack(item, amount);
        ItemMeta thisItemMeta = thisItem.getItemMeta();
        thisItemMeta.setDisplayName(display);

        for (Map.Entry<Enchantment, Integer> enchance : enchanceListLevel.entrySet()) {
            thisItemMeta.addEnchant(enchance.getKey(), enchance.getValue(), true);
        }

        thisItemMeta.setLore(Arrays.asList("", getPriceTag()));
        thisItem.setItemMeta(thisItemMeta);
        return thisItem;
    }

    public ItemStack createItemStack(int amount, String display, ItemFlag flag) {
        ItemStack thisItem = new ItemStack(item, amount);
        ItemMeta thisItemMeta = thisItem.getItemMeta();
        thisItemMeta.setDisplayName(display);

        for (Map.Entry<Enchantment, Integer> enchance : enchanceListLevel.entrySet()) {
            thisItemMeta.addEnchant(enchance.getKey(), enchance.getValue(), true);
        }

        thisItemMeta.addItemFlags(flag);
        thisItemMeta.setLore(Arrays.asList("", getPriceTag()));
        thisItem.setItemMeta(thisItemMeta);
        return thisItem;
    }

    public ItemStack createItemStack(int amount, String display, List<String> lore) {
        ItemStack thisItem = new ItemStack(item, amount);
        ItemMeta thisItemMeta = thisItem.getItemMeta();
        thisItemMeta.setDisplayName(display);

        for (Map.Entry<Enchantment, Integer> enchance : enchanceListLevel.entrySet()) {
            thisItemMeta.addEnchant(enchance.getKey(), enchance.getValue(), true);
        }

        List<String> copiedLore = new ArrayList<>();
        copiedLore.addAll(lore);
        copiedLore.add("");
        copiedLore.add(getPriceTag());
        thisItemMeta.setLore(copiedLore);
        thisItem.setItemMeta(thisItemMeta);
        return thisItem;
    }

    public ItemStack createItemStack(int amount, String display, List<String> lore, ItemFlag flag) {
        ItemStack thisItem = new ItemStack(item, amount);
        ItemMeta thisItemMeta = thisItem.getItemMeta();
        thisItemMeta.setDisplayName(display);

        for (Map.Entry<Enchantment, Integer> enchance : enchanceListLevel.entrySet()) {
            thisItemMeta.addEnchant(enchance.getKey(), enchance.getValue(), true);
        }

        List<String> copiedLore = new ArrayList<>();
        copiedLore.addAll(lore);
        copiedLore.add("");
        copiedLore.add(getPriceTag());
        thisItemMeta.setLore(copiedLore);
        thisItemMeta.addItemFlags(flag);
        thisItem.setItemMeta(thisItemMeta);
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

    public int getPrice(int price) {
        this.price = price;
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDefaultPrice() {
        return defaultPrice;
    }

    public Material getCurrency() {
        return currency;
    }

    public Material getCurrency(Material currency) {
        this.currency = currency;
        return currency;
    }

    public void setCurrency(Material currency) {
        this.currency = currency;
    }

    public Material getItem() {
        return item;
    }

}
