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

public class BedwarsUpgradeMenu {

    public static final String VILLAGER_NAME_TAG = ChatColor.GOLD + String.format(
            "BW %sUpgrade", ChatColor.LIGHT_PURPLE + "");

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

    public BedwarsUpgradeMenu(TeamGroupMaker team) {
        //#region Upgrades
        // Sharper Blade Entity
        pricedItems.put(SHARPER_BLADE, new PricetagItems(Material.DIAMOND_SWORD, Material.DIAMOND, SHARPER_BLADE,
                4, 1, Arrays.asList("Permanently upgrade weapon", "Sharpness by 1 for team.")));
        pricedItems.get(SHARPER_BLADE).addItemFlag(ItemFlag.HIDE_ATTRIBUTES);
        pricedItems.get(SHARPER_BLADE).setDisplayColor(ChatColor.AQUA);
        // Mine-A-Holic Entity
        pricedItems.put(MINE_A_HOLIC, new PricetagItems(Material.GOLDEN_PICKAXE, Material.DIAMOND, MINE_A_HOLIC, 4,
                1, Arrays.asList("Permanently upgrade weapon", "Efficiency by 1 for team.")));
        pricedItems.get(MINE_A_HOLIC).addItemFlag(ItemFlag.HIDE_ATTRIBUTES);
        pricedItems.get(MINE_A_HOLIC).setDisplayColor(ChatColor.AQUA);
        // Make It Rain! Entity
        pricedItems.put(MAKE_IT_RAIN, new PricetagItems(Material.GHAST_TEAR, Material.DIAMOND, MAKE_IT_RAIN,
                8, 1, Collections.singletonList("Permanently upgrade base generators")));
        pricedItems.get(MAKE_IT_RAIN).setDisplayColor(ChatColor.AQUA);
        // Holy Light Entity
        pricedItems.put(HOLY_LIGHT, new PricetagItems(Material.EXPERIENCE_BOTTLE, Material.DIAMOND, HOLY_LIGHT,
                8, 1, Arrays.asList("Permanent health regeneration", "at your team base.")));
        pricedItems.get(HOLY_LIGHT).setDisplayColor(ChatColor.AQUA);
        // Tough Skin Entity
        pricedItems.put(TOUGH_SKIN, new PricetagItems(Material.LEATHER, Material.DIAMOND, TOUGH_SKIN,
                4, 1, Arrays.asList("Permanent upgrade armor", "Protection by 1 for team.")));
        pricedItems.get(TOUGH_SKIN).setDisplayColor(ChatColor.AQUA);
        // Eye for an eye Entity
        pricedItems.put(EYE_FOR_AN_EYE, new PricetagItems(Material.ENDER_EYE, Material.DIAMOND, EYE_FOR_AN_EYE,
                8, 1, Arrays.asList("Permanent upgrade armor", "Thorns by 1 for team.")));
        pricedItems.get(EYE_FOR_AN_EYE).setDisplayColor(ChatColor.AQUA);
        // Gift for the Poor Entity
        pricedItems.put(GIFT_FOR_THE_POOR, new PricetagItems(Material.DEAD_BUSH, Material.DIAMOND, GIFT_FOR_THE_POOR,
                4, 1, Collections.singletonList("Something special is coming.")));
        pricedItems.get(GIFT_FOR_THE_POOR).setDisplayColor(ChatColor.AQUA);
        //#endregion

        // Initialize permanent level upgrades, set all to level 1
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
            String upgradeName = ChatColor.stripColor(pricedItemTag.getName());
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
                        List<String> c_lore = pricedItemTag.getLore();
                        c_lore.remove(c_lore.size() - 1);
                        c_lore.remove(c_lore.size() - 1);
                        c_lore.add(" ");
                        c_lore.add(ChatColor.RED + "MAX LEVEL REACHED");
                        pricedItemTag.refreshMeta(reference, false);
                    } else {
                        pricedItemTag.setPrice(currentPrice + (currentPrice * 50/100));
                        pricedItemTag.refreshMeta(reference, true);
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
     * @param playerInv Referred player inventory
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
