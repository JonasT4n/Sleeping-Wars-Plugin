package com.joenastan.sleepingwar.plugin.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PricetagsItems {

    private List<Enchantment> enchanceList = new ArrayList<Enchantment>();
    private Map<Enchantment, Integer> enchanceLevel = new HashMap<Enchantment, Integer>();
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

    public PricetagsItems addEnchancement(Enchantment enc, int lvl) {
        if (enchanceList.contains(enc))
            enchanceList.add(enc);

        enchanceLevel.put(enc, lvl);
        return this;
    }

    public PricetagsItems addEnchancement(Map<Enchantment, Integer> encs) {
        for (Map.Entry<Enchantment, Integer> enc : encs.entrySet()) {
            if (enchanceList.contains(enc.getKey()))
                enchanceList.add(enc.getKey());

            enchanceLevel.put(enc.getKey(), enc.getValue());
        }
    
        return this;
    }

    public List<String> getEnchancementList() {
        List<String> listEnc = new ArrayList<String>();
        for (int i = 0; i < enchanceList.size(); i++) {
            listEnc.add(enchanceList.get(i).getKey().getNamespace());
        }
        return listEnc;
    }

    public void removeEnchancement(int index) {
        if (index >= enchanceList.size())
            return;

        Enchantment enc = enchanceList.remove(index);
        enchanceLevel.remove(enc);
    }

    public ItemStack createItemStack(int amount) {
        ItemStack thisItem = new ItemStack(item, amount);
        ItemMeta thisItemMeta = thisItem.getItemMeta();

        for (int i = 0; i < enchanceList.size(); i++) {
            Enchantment enc = enchanceList.get(i);
            thisItemMeta.addEnchant(enc, enchanceLevel.get(enc), true);
        }
        
        thisItem.setItemMeta(thisItemMeta);
        return thisItem;
    }

    public ItemStack createItemStack(int amount, String display) {
        ItemStack thisItem = new ItemStack(item, amount);
        ItemMeta thisItemMeta = thisItem.getItemMeta();
        thisItemMeta.setDisplayName(display);

        for (int i = 0; i < enchanceList.size(); i++) {
            Enchantment enc = enchanceList.get(i);
            thisItemMeta.addEnchant(enc, enchanceLevel.get(enc), true);
        }

        thisItem.setItemMeta(thisItemMeta);
        return thisItem;
    }

    public ItemStack createItemStack(int amount, String display, ItemFlag flag) {
        ItemStack thisItem = new ItemStack(item, amount);
        ItemMeta thisItemMeta = thisItem.getItemMeta();
        thisItemMeta.setDisplayName(display);

        for (int i = 0; i < enchanceList.size(); i++) {
            Enchantment enc = enchanceList.get(i);
            thisItemMeta.addEnchant(enc, enchanceLevel.get(enc), true);
        }

        thisItemMeta.addItemFlags(flag);
        thisItem.setItemMeta(thisItemMeta);
        return thisItem;
    }

    public ItemStack createItemStack(int amount, String display, List<String> lore) {
        ItemStack thisItem = new ItemStack(item, amount);
        ItemMeta thisItemMeta = thisItem.getItemMeta();
        thisItemMeta.setDisplayName(display);

        for (int i = 0; i < enchanceList.size(); i++) {
            Enchantment enc = enchanceList.get(i);
            thisItemMeta.addEnchant(enc, enchanceLevel.get(enc), true);
        }

        thisItemMeta.setLore(lore);
        thisItem.setItemMeta(thisItemMeta);
        return thisItem;
    }

    public ItemStack createItemStack(int amount, String display, List<String> lore, ItemFlag flag) {
        ItemStack thisItem = new ItemStack(item, amount);
        ItemMeta thisItemMeta = thisItem.getItemMeta();
        thisItemMeta.setDisplayName(display);

        for (int i = 0; i < enchanceList.size(); i++) {
            Enchantment enc = enchanceList.get(i);
            thisItemMeta.addEnchant(enc, enchanceLevel.get(enc), true);
        }

        thisItemMeta.setLore(lore);
        thisItemMeta.addItemFlags(flag);
        thisItem.setItemMeta(thisItemMeta);
        return thisItem;
    }

}
