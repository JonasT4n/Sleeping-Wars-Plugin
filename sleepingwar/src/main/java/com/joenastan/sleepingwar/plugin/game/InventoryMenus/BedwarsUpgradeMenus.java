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
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class BedwarsUpgradeMenus implements BedwarsMenus {

    public static final String SHARPER_BLADE = "Sharper Blade";
    public static final String MINE_A_HOLIC = "Mine-A-Holic";
    public static final String MAKE_IT_RAIN = "Make it Rain!";
    public static final String HOLY_LIGHT = "Holy Light";
    public static final String TOUGH_SKIN = "Tough Skin";
    public static final String EYE_FOR_AN_EYE = "Eye for an Eye";
    public static final String GIFT_FOR_THE_POOR = "Gift for the Poor";

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
        mineAHolicMeta.setDisplayName(ChatColor.AQUA + MINE_A_HOLIC);
        mineAHolicMeta.setLore(Arrays.asList("Permanently upgrade weapon", "Efficiency by 1 for team."));
        mineAHolicMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        pricedItems.put(MINE_A_HOLIC, new PricetagsItems(Material.GOLDEN_PICKAXE, Material.DIAMOND, 5, mineAHolicMeta, 1));
        // Make It Rain! Entity
        ItemMeta makeItRainMeta = new ItemStack(Material.GHAST_TEAR, 1).getItemMeta();
        makeItRainMeta.setDisplayName(ChatColor.AQUA + MAKE_IT_RAIN);
        makeItRainMeta.setLore(Arrays.asList("Permanently upgrade base generators"));
        pricedItems.put(MAKE_IT_RAIN, new PricetagsItems(Material.GHAST_TEAR, Material.DIAMOND, 8, makeItRainMeta, 1));
        // Holy Light Entity
        ItemMeta holyLightMeta = new ItemStack(Material.EXPERIENCE_BOTTLE, 1).getItemMeta();
        holyLightMeta.setDisplayName(ChatColor.AQUA + HOLY_LIGHT);
        holyLightMeta.setLore(Arrays.asList("Permanent health regeneration", "at your team base."));
        pricedItems.put(HOLY_LIGHT, new PricetagsItems(Material.EXPERIENCE_BOTTLE, Material.DIAMOND, 8, holyLightMeta, 1));
        // Tough Skin Entity
        ItemMeta toughSkinMeta = new ItemStack(Material.LEATHER, 1).getItemMeta();
        toughSkinMeta.setDisplayName(ChatColor.AQUA + TOUGH_SKIN);
        toughSkinMeta.setLore(Arrays.asList("Permanent upgrade armor", "Protection by 1 for team."));
        pricedItems.put(TOUGH_SKIN, new PricetagsItems(Material.LEATHER, Material.DIAMOND, 5, toughSkinMeta, 1));
        // Eye for an eye Entity
        ItemMeta eyeForEyeMeta = new ItemStack(Material.ENDER_EYE, 1).getItemMeta();
        eyeForEyeMeta.setDisplayName(ChatColor.AQUA + EYE_FOR_AN_EYE);
        eyeForEyeMeta.setLore(Arrays.asList("Permanent upgrade armor", "Thorns by 1 for team."));
        pricedItems.put(EYE_FOR_AN_EYE, new PricetagsItems(Material.ENDER_EYE, Material.DIAMOND, 8, eyeForEyeMeta, 1));
        // Gift for the Poor Entity
        ItemMeta giftPoorMeta = new ItemStack(Material.DEAD_BUSH, 1).getItemMeta();
        giftPoorMeta.setDisplayName(ChatColor.AQUA + GIFT_FOR_THE_POOR);
        giftPoorMeta.setLore(Arrays.asList("Something special is coming."));
        pricedItems.put(GIFT_FOR_THE_POOR, new PricetagsItems(Material.DEAD_BUSH, Material.DIAMOND, 5, giftPoorMeta, 1));
        //#endregion

        // Initialize perma-level upgrades, set all to level 1
        for (Map.Entry<String, PricetagsItems> listUpgradeEntry : pricedItems.entrySet()) {
            team.getPermaLevels().put(listUpgradeEntry.getKey(), 1);
        }
    }

    private Inventory UpgradeMenu() {
        Inventory upgradeMenuTemplate = Bukkit.getServer().createInventory(null, InventoryType.BARREL, "Upgrade Menu");

        ItemStack sharperBlade = pricedItems.get(SHARPER_BLADE).createItem(team.getPermaLevels().get(SHARPER_BLADE));
        ItemStack mineAHolic = pricedItems.get(MINE_A_HOLIC).createItem(team.getPermaLevels().get(MINE_A_HOLIC));
        ItemStack makeItRain = pricedItems.get(MAKE_IT_RAIN).createItem(team.getPermaLevels().get(MAKE_IT_RAIN));
        ItemStack holyLight = pricedItems.get(HOLY_LIGHT).createItem(team.getPermaLevels().get(HOLY_LIGHT));
        ItemStack toughSkin = pricedItems.get(TOUGH_SKIN).createItem(team.getPermaLevels().get(TOUGH_SKIN));
        ItemStack eyeForEye = pricedItems.get(EYE_FOR_AN_EYE).createItem(team.getPermaLevels().get(EYE_FOR_AN_EYE));
        ItemStack giftPoor = pricedItems.get(GIFT_FOR_THE_POOR).createItem(team.getPermaLevels().get(GIFT_FOR_THE_POOR));

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
    public boolean openMenu(Player player, String menuName) {
        player.openInventory(UpgradeMenu());
        return true;
    }

    @Override
    public void selectedSlot(Player player, Inventory inv, int slot) {
        ItemStack upgradeItem = inv.getItem(slot);
        ItemMeta upgradeMeta = upgradeItem.getItemMeta();
        Material upgradeMaterial = upgradeItem.getType();
        PlayerInventory playerInv = player.getInventory();

        for (Map.Entry<String, PricetagsItems> pricedItEntry : pricedItems.entrySet()) {
            boolean displayNameIsTrue = ChatColor.stripColor(upgradeMeta.getDisplayName())
                    .equals(pricedItEntry.getKey());
            boolean materialIsTheSame = pricedItEntry.getValue().getItem() == upgradeMaterial;
            int maxLevel = getUpgradeMaxLevel(pricedItEntry.getKey());
            if (displayNameIsTrue && materialIsTheSame) {
                if (maxLevel <= team.getPermaLevels().get(pricedItEntry.getKey())) {
                    player.sendMessage(ChatColor.LIGHT_PURPLE + "You have already reach its max level");
                    return;
                }
                
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

                    // Price for next level 150% of current price
                    int currentPrice = tag.getPrice();
                    tag.setPrice(currentPrice + (int)(currentPrice * 50/100));
                    team.teamUpgrade(pricedItEntry.getKey());
                    inv.getItem(slot).setAmount(team.getPermaLevels().get(pricedItEntry.getKey()));
                    BedwarsGameOnUpgradeEvent event = new BedwarsGameOnUpgradeEvent(team, player, upgradeItem);
                    Bukkit.getPluginManager().callEvent(event);
                }
                break;
            }   
        }
    }

    public static int getUpgradeMaxLevel(String upgradeName) {
        switch (upgradeName) {
            case SHARPER_BLADE:
                return 5;

            case MINE_A_HOLIC:
                return 4;

            case MAKE_IT_RAIN:
                return 4;

            case HOLY_LIGHT:
                return 1;

            case TOUGH_SKIN:
                return 4;

            case EYE_FOR_AN_EYE:
                return 1;

            case GIFT_FOR_THE_POOR:
                return 5;

            default:
                return 0;
        }
    }
}
