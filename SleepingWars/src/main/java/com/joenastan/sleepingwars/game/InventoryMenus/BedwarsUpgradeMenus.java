package com.joenastan.sleepingwars.game.InventoryMenus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joenastan.sleepingwars.game.ItemPrice.PricetagItems;
import com.joenastan.sleepingwars.game.TeamGroupMaker;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class BedwarsUpgradeMenus {

    public static final String SHARPER_BLADE = "Sharper Blade";
    public static final String MINE_A_HOLIC = "Mine-A-Holic";
    public static final String MAKE_IT_RAIN = "Make it Rain!";
    public static final String HOLY_LIGHT = "Holy Light";
    public static final String TOUGH_SKIN = "Tough Skin";
    public static final String EYE_FOR_AN_EYE = "Eye for an Eye";
    public static final String GIFT_FOR_THE_POOR = "Gift for the Poor";
    public static final String MENU_NAME = "Upgrade Menu";

    // Attributes
    private final Inventory upgradeInvMenu;

    // Maps and Lists
    private final Map<String, PricetagItems> pricedItems = new HashMap<>();

    public BedwarsUpgradeMenus(TeamGroupMaker team) {
        //#region Upgrades
        // Sharper Blade Entity
        ItemMeta sharperBladeMeta = new ItemStack(Material.DIAMOND_SWORD, 1).getItemMeta();
        assert sharperBladeMeta != null;
        sharperBladeMeta.setDisplayName(ChatColor.AQUA + SHARPER_BLADE);
        sharperBladeMeta.setLore(Arrays.asList("Permanently upgrade weapon", "Sharpness by 1 for team."));
        sharperBladeMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        pricedItems.put(SHARPER_BLADE, new PricetagItems(Material.DIAMOND_SWORD, Material.DIAMOND, 4,
                sharperBladeMeta, 1));
        // Mine-A-Holic Entity
        ItemMeta mineAHolicMeta = new ItemStack(Material.GOLDEN_PICKAXE, 1).getItemMeta();
        assert mineAHolicMeta != null;
        mineAHolicMeta.setDisplayName(ChatColor.AQUA + MINE_A_HOLIC);
        mineAHolicMeta.setLore(Arrays.asList("Permanently upgrade weapon", "Efficiency by 1 for team."));
        mineAHolicMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        pricedItems.put(MINE_A_HOLIC, new PricetagItems(Material.GOLDEN_PICKAXE, Material.DIAMOND, 4,
                mineAHolicMeta, 1));
        // Make It Rain! Entity
        ItemMeta makeItRainMeta = new ItemStack(Material.GHAST_TEAR, 1).getItemMeta();
        assert makeItRainMeta != null;
        makeItRainMeta.setDisplayName(ChatColor.AQUA + MAKE_IT_RAIN);
        makeItRainMeta.setLore(Collections.singletonList("Permanently upgrade base generators"));
        pricedItems.put(MAKE_IT_RAIN, new PricetagItems(Material.GHAST_TEAR, Material.DIAMOND, 8,
                makeItRainMeta, 1));
        // Holy Light Entity
        ItemMeta holyLightMeta = new ItemStack(Material.EXPERIENCE_BOTTLE, 1).getItemMeta();
        assert holyLightMeta != null;
        holyLightMeta.setDisplayName(ChatColor.AQUA + HOLY_LIGHT);
        holyLightMeta.setLore(Arrays.asList("Permanent health regeneration", "at your team base."));
        pricedItems.put(HOLY_LIGHT, new PricetagItems(Material.EXPERIENCE_BOTTLE, Material.DIAMOND, 8,
                holyLightMeta, 1));
        // Tough Skin Entity
        ItemMeta toughSkinMeta = new ItemStack(Material.LEATHER, 1).getItemMeta();
        assert toughSkinMeta != null;
        toughSkinMeta.setDisplayName(ChatColor.AQUA + TOUGH_SKIN);
        toughSkinMeta.setLore(Arrays.asList("Permanent upgrade armor", "Protection by 1 for team."));
        pricedItems.put(TOUGH_SKIN, new PricetagItems(Material.LEATHER, Material.DIAMOND, 4,
                toughSkinMeta, 1));
        // Eye for an eye Entity
        ItemMeta eyeForEyeMeta = new ItemStack(Material.ENDER_EYE, 1).getItemMeta();
        assert eyeForEyeMeta != null;
        eyeForEyeMeta.setDisplayName(ChatColor.AQUA + EYE_FOR_AN_EYE);
        eyeForEyeMeta.setLore(Arrays.asList("Permanent upgrade armor", "Thorns by 1 for team."));
        pricedItems.put(EYE_FOR_AN_EYE, new PricetagItems(Material.ENDER_EYE, Material.DIAMOND, 8,
                eyeForEyeMeta, 1));
        // Gift for the Poor Entity
        ItemMeta giftPoorMeta = new ItemStack(Material.DEAD_BUSH, 1).getItemMeta();
        assert giftPoorMeta != null;
        giftPoorMeta.setDisplayName(ChatColor.AQUA + GIFT_FOR_THE_POOR);
        giftPoorMeta.setLore(Collections.singletonList("Something special is coming."));
        pricedItems.put(GIFT_FOR_THE_POOR, new PricetagItems(Material.DEAD_BUSH, Material.DIAMOND, 4,
                giftPoorMeta, 1));
        //#endregion

        // Initialize perma-level upgrades, set all to level 1
        for (Map.Entry<String, PricetagItems> listUpgradeEntry : pricedItems.entrySet())
            team.getPermLevels().put(listUpgradeEntry.getKey(), 1);
        // Initialize upgrade inventory menu
        upgradeInvMenu = Bukkit.getServer().createInventory(null,
                InventoryType.BARREL, "Upgrade Menu");

        ItemStack sharperBlade = pricedItems.get(SHARPER_BLADE).createItem(team.getPermLevels().get(SHARPER_BLADE));
        ItemStack mineAHolic = pricedItems.get(MINE_A_HOLIC).createItem(team.getPermLevels().get(MINE_A_HOLIC));
        ItemStack makeItRain = pricedItems.get(MAKE_IT_RAIN).createItem(team.getPermLevels().get(MAKE_IT_RAIN));
        ItemStack holyLight = pricedItems.get(HOLY_LIGHT).createItem(team.getPermLevels().get(HOLY_LIGHT));
        ItemStack toughSkin = pricedItems.get(TOUGH_SKIN).createItem(team.getPermLevels().get(TOUGH_SKIN));
        ItemStack eyeForEye = pricedItems.get(EYE_FOR_AN_EYE).createItem(team.getPermLevels().get(EYE_FOR_AN_EYE));
        ItemStack giftPoor = pricedItems.get(GIFT_FOR_THE_POOR).createItem(team.getPermLevels().get(GIFT_FOR_THE_POOR));

        upgradeInvMenu.setItem(10, sharperBlade);
        upgradeInvMenu.setItem(11, mineAHolic);
        upgradeInvMenu.setItem(12, makeItRain);
        upgradeInvMenu.setItem(13, holyLight);
        upgradeInvMenu.setItem(14, toughSkin);
        upgradeInvMenu.setItem(15, eyeForEye);
        upgradeInvMenu.setItem(16, giftPoor);
    }

    public void openUpgradeMenu(@Nonnull Player player, String menuName) {
        if (MENU_NAME.equals(menuName)) {
            player.openInventory(upgradeInvMenu);
        }
    }

    public PricetagItems selectedSlot(Inventory inv, int slot) {
        // Get information
        ItemStack upgradeItem = inv.getItem(slot);
        if (upgradeItem != null) {
            ItemMeta upgradeMeta = upgradeItem.getItemMeta();
            assert upgradeMeta != null;
            return pricedItems.get(ChatColor.stripColor(upgradeMeta.getDisplayName()));
        }
        return null;
    }

    public boolean chooseUpgrade(@Nonnull Player player, @Nonnull PricetagItems pricedItemTag,
                                 @Nonnull ItemStack reference, @Nullable TeamGroupMaker team) {
        if (team != null) {
            String upgradeName = ChatColor.stripColor(pricedItemTag.getMeta().getDisplayName());
            if (getUpgradeMaxLevel(upgradeName) <= team.getPermLevels().get(upgradeName)) {
                player.sendMessage(ChatColor.LIGHT_PURPLE + "You have already reach its max level");
            } else {
                PlayerInventory playerInv = player.getInventory();
                Material currency = pricedItemTag.getCurrency();
                List<Integer> onCurrencySlots = new ArrayList<>();
                int countCurrencyAmount = countItemPlayerInventory(playerInv, currency, onCurrencySlots);
                // Check if it's enough to buy it
                if (countCurrencyAmount < pricedItemTag.getPrice()) {
                    player.sendMessage(ChatColor.RED + "Not enough currency, you cannot afford this.");
                } else {
                    // Pay with price
                    int mustPay = pricedItemTag.getPrice();
                    for (int j : onCurrencySlots) {
                        if (mustPay == 0)
                            break;
                        ItemStack i_stack = playerInv.getItem(j);
                        if (i_stack != null) {
                            if (i_stack.getAmount() > mustPay) {
                                i_stack.setAmount(i_stack.getAmount() - mustPay);
                                break;
                            }
                            mustPay -= i_stack.getAmount();
                            i_stack.setAmount(0);
                        }
                    }
                    // Price for next level 150% of current price
                    int currentPrice = pricedItemTag.getPrice();
                    team.teamUpgrade(upgradeName);
                    // If reach max level then edit lore without price
                    if (team.getPermLevels().get(upgradeName) >= getUpgradeMaxLevel(upgradeName)) {
                        pricedItemTag.setPrice(0);
                        ItemMeta upPriceMeta = pricedItemTag.getMeta();
                        List<String> c_lore = new ArrayList<>();
                        if (upPriceMeta.hasLore())
                            c_lore.addAll(upPriceMeta.getLore());
                        c_lore.add(ChatColor.RED + "MAX LEVEL REACHED");
                        upPriceMeta.setLore(c_lore);
                        pricedItemTag.setMeta(upPriceMeta);
                    } else {
                        pricedItemTag.setPrice(currentPrice + (currentPrice * 50/100));
                    }
                    reference.setItemMeta(pricedItemTag.getMeta());
                    reference.setAmount(reference.getAmount() + 1);
                    return true;
                }
            }
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
        }
        return false;
    }

    /**
     * @return Upgrade menu
     */
    public Inventory getMenu() {
        return upgradeInvMenu;
    }

    /**
     * Check if player has the required items to buy something. Must include a slot indexes references by the list.
     * @param playerInv Refered player inventory
     * @param required Required item type
     * @param targetSlots List of slot index that has the same item type
     * @return True if player has the requirement, else then false
     */
    private int countItemPlayerInventory(@Nonnull PlayerInventory playerInv, Material required,
                                         List<Integer> targetSlots) {
        int countAmount = 0;
        // Get Player current currency amount
        for (int i = 0; i < playerInv.getSize(); i++) {
            ItemStack playerItem = playerInv.getItem(i);
            if (playerItem != null)
                if (playerItem.getType() == required) {
                    targetSlots.add(i);
                    countAmount += playerItem.getAmount();
                }
        }
        return countAmount;
    }

    public static int getUpgradeMaxLevel(String upgradeName) {
        switch (upgradeName) {
            case SHARPER_BLADE:
            case GIFT_FOR_THE_POOR:
                return 5;

            case MINE_A_HOLIC:
            case MAKE_IT_RAIN:
            case TOUGH_SKIN:
                return 4;

            case HOLY_LIGHT:
            case EYE_FOR_AN_EYE:
                return 2;

            default:
                return 0;
        }
    }
}
