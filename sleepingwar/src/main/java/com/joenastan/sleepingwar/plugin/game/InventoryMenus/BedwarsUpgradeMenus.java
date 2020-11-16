package com.joenastan.sleepingwar.plugin.game.InventoryMenus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joenastan.sleepingwar.plugin.game.ItemPrice.PricetagsItems;
import com.joenastan.sleepingwar.plugin.events.CustomEvents.BedwarsGameOnUpgradeEvent;
import com.joenastan.sleepingwar.plugin.game.TeamGroupMaker;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class BedwarsUpgradeMenus implements BedwarsMenus {

    public final String SHARPER_BLADE = "Sharper Blade";

    // Attributes
    private TeamGroupMaker team;

    // Maps and Lists
    private Map<String, PricetagsItems> pricedItems = new HashMap<String, PricetagsItems>();

    public BedwarsUpgradeMenus(TeamGroupMaker team) {
        this.team = team;

        //#region Upgrades
        // Sharper Blade Entity
        ItemMeta sharperBladeMeta = new ItemStack(Material.DIAMOND_SWORD, 1).getItemMeta();
        sharperBladeMeta.setDisplayName(ChatColor.AQUA + SHARPER_BLADE);
        sharperBladeMeta.setLore(Arrays.asList("Permanently upgrade weapon", "Sharpness by 1 for team."));
        sharperBladeMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        pricedItems.put(SHARPER_BLADE, new PricetagsItems(Material.DIAMOND_SWORD, Material.DIAMOND, 5, sharperBladeMeta, 1));
        // Mine-A-Holic Entity
        ItemMeta mineAHolicMeta = new ItemStack(Material.GOLDEN_PICKAXE, 1).getItemMeta();
        mineAHolicMeta.setDisplayName(ChatColor.AQUA + "Mine-A-Holic");
        mineAHolicMeta.setLore(Arrays.asList("Permanently upgrade weapon", "Efficiency by 1 for team."));
        mineAHolicMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        pricedItems.put("Mine-A-Holic", new PricetagsItems(Material.GOLDEN_PICKAXE, Material.DIAMOND, 5, mineAHolicMeta, 1));
        // Make It Rain! Entity
        ItemMeta makeItRainMeta = new ItemStack(Material.GHAST_TEAR, 1).getItemMeta();
        makeItRainMeta.setDisplayName(ChatColor.AQUA + "Make it Rain!");
        makeItRainMeta.setLore(Arrays.asList("Permanently upgrade base generators"));
        pricedItems.put("Make it Rain!", new PricetagsItems(Material.GHAST_TEAR, Material.DIAMOND, 8, makeItRainMeta, 1));
        // Holy Light Entity
        ItemMeta holyLightMeta = new ItemStack(Material.EXPERIENCE_BOTTLE, 1).getItemMeta();
        holyLightMeta.setDisplayName(ChatColor.AQUA + "Holy Light");
        holyLightMeta.setLore(Arrays.asList("Permanent health regeneration", "at your team base."));
        pricedItems.put("Holy Light", new PricetagsItems(Material.EXPERIENCE_BOTTLE, Material.DIAMOND, 8, holyLightMeta, 1));
        // Tough Skin Entity
        ItemMeta toughSkinMeta = new ItemStack(Material.LEATHER, 1).getItemMeta();
        toughSkinMeta.setDisplayName(ChatColor.AQUA + "Tough Skin");
        toughSkinMeta.setLore(Arrays.asList("Permanent upgrade armor", "Protection by 1 for team."));
        pricedItems.put("Tough Skin", new PricetagsItems(Material.LEATHER, Material.DIAMOND, 5, toughSkinMeta, 1));
        // Eye for an eye Entity
        ItemMeta eyeForEyeMeta = new ItemStack(Material.ENDER_EYE, 1).getItemMeta();
        eyeForEyeMeta.setDisplayName(ChatColor.AQUA + "Eye for an Eye");
        eyeForEyeMeta.setLore(Arrays.asList("Permanent upgrade armor", "Thorns by 1 for team."));
        pricedItems.put("Eye for an Eye", new PricetagsItems(Material.ENDER_EYE, Material.DIAMOND, 8, eyeForEyeMeta, 1));
        // Gift for the Poor Entity
        ItemMeta giftPoorMeta = new ItemStack(Material.DEAD_BUSH, 1).getItemMeta();
        giftPoorMeta.setDisplayName(ChatColor.AQUA + "Gift for the Poor");
        giftPoorMeta.setLore(Arrays.asList("Something special is coming."));
        pricedItems.put("Gift for the Poor", new PricetagsItems(Material.DEAD_BUSH, Material.DIAMOND, 5, giftPoorMeta, 1));
        //#endregion

        // Initialize perma-level upgrades, set all to level 1
        for (Map.Entry<String, PricetagsItems> listUpgradeEntry : pricedItems.entrySet()) {
            team.getLevelsMap().put(listUpgradeEntry.getKey(), 1);
        }
    }

    private Inventory UpgradeMenu() {
        Inventory upgradeMenuTemplate = Bukkit.getServer().createInventory(null, InventoryType.BARREL, "Upgrade Menu");

        ItemStack sharperBlade = pricedItems.get(SHARPER_BLADE).createItem(team.getTeamLevel(SHARPER_BLADE));
        ItemStack mineAHolic = pricedItems.get("Mine-A-Holic").createItem(team.getTeamLevel("Mine-A-Holic"));
        ItemStack makeItRain = pricedItems.get("Make it Rain!").createItem(team.getTeamLevel("Make it Rain!"));
        ItemStack holyLight = pricedItems.get("Holy Light").createItem(team.getTeamLevel("Holy Light"));
        ItemStack toughSkin = pricedItems.get("Tough Skin").createItem(team.getTeamLevel("Tough Skin"));
        ItemStack eyeForEye = pricedItems.get("Eye for an Eye").createItem(team.getTeamLevel("Eye for an Eye"));
        ItemStack giftPoor = pricedItems.get("Gift for the Poor").createItem(team.getTeamLevel("Gift for the Poor"));

        upgradeMenuTemplate.setItem(10, sharperBlade);
        upgradeMenuTemplate.setItem(11, mineAHolic);
        upgradeMenuTemplate.setItem(12, makeItRain);
        upgradeMenuTemplate.setItem(13, holyLight);
        upgradeMenuTemplate.setItem(14, toughSkin);
        upgradeMenuTemplate.setItem(15, eyeForEye);
        upgradeMenuTemplate.setItem(16, giftPoor);

        return upgradeMenuTemplate;
    }

    @Override
    public void openMenu(Player player, String menuName) {
        player.openInventory(UpgradeMenu());
    }

    @Override
    public void selectedSlot(Player player, Inventory inv, int slot) {
        ItemStack upgradeItem = inv.getItem(slot);
        ItemMeta upgradeMeta = upgradeItem.getItemMeta();
        Material upgradeMaterial = upgradeItem.getType();
        Inventory playerInv = player.getInventory();

        for (Map.Entry<String, PricetagsItems> pricedItEntry : pricedItems.entrySet()) {
            boolean displayNameIsTrue = ChatColor.stripColor(upgradeMeta.getDisplayName())
                    .equals(pricedItEntry.getKey());
            boolean materialIsTheSame = pricedItEntry.getValue().getItem() == upgradeMaterial;
            if (displayNameIsTrue && materialIsTheSame) {
                PricetagsItems tag = pricedItEntry.getValue();
                int countCurrencyAmount = 0;
                List<Integer> onCurrencySlots = new ArrayList<Integer>();

                // Get Player current currency amount
                for (int i = 0; i < playerInv.getSize(); i++) {
                    ItemStack playerItem = playerInv.getItem(i);
                    if (playerItem != null) {
                        if (playerItem.getType() == tag.getCurrency()) {
                            onCurrencySlots.add(i);
                            countCurrencyAmount += playerItem.getAmount();
                        }
                    }
                }

                // Check if it's enough to buy it
                if (countCurrencyAmount < tag.getPrice()) {
                    player.sendMessage(ChatColor.RED + "Not enough currency, you cannot afford this.");
                } else {
                    int mustPay = tag.getPrice();
                    for (int j : onCurrencySlots) {
                        if (mustPay == 0)
                            break;

                        if (playerInv.getItem(j).getAmount() > mustPay) {
                            playerInv.getItem(j).setAmount(playerInv.getItem(j).getAmount() - mustPay);
                            break;
                        }

                        mustPay -= playerInv.getItem(j).getAmount();
                        playerInv.setItem(j, null);
                    }

                    team.teamUpgrade(pricedItEntry.getKey());
                    BedwarsGameOnUpgradeEvent event = new BedwarsGameOnUpgradeEvent(team, player, upgradeItem);
                    Bukkit.getPluginManager().callEvent(event);
                }
                break;
            }   
        }
    }

}
