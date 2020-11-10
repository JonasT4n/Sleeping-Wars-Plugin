package com.joenastan.sleepingwar.plugin.Game;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import net.md_5.bungee.api.ChatColor;

public class BedwarsMenus {

    private static final Map<String, PricetagsItems> itemBuyList = new HashMap<String, PricetagsItems>() {{
        // Weapons in Shop
        put("Stone Sword", new PricetagsItems(Material.STONE_SWORD, Material.IRON_INGOT, 20));
        put("Iron Sword", new PricetagsItems(Material.IRON_SWORD, Material.GOLD_INGOT, 10));
        put("Diamond Sword", new PricetagsItems(Material.DIAMOND_SWORD, Material.EMERALD, 20));
        put("Regular Bow", new PricetagsItems(Material.BOW, Material.IRON_INGOT, 50));
        put("Punch Bow", new PricetagsItems(Material.BOW, Material.GOLD_INGOT, 20)
            .addEnchancement(Enchantment.ARROW_DAMAGE, 1));
        put("Punch 2 Bow", new PricetagsItems(Material.BOW, Material.GOLD_INGOT, 32)
            .addEnchancement(new HashMap<Enchantment, Integer>() {{
                put(Enchantment.ARROW_DAMAGE, 2);
                put(Enchantment.ARROW_KNOCKBACK, 2);
            }}));
        put("Shield", new PricetagsItems(Material.SHIELD, Material.EMERALD, 8));
        put("Arrow", new PricetagsItems(Material.ARROW, Material.GOLD_INGOT, 2));

        // Armors in Shop
        put("Chainmail Armor", new PricetagsItems(Material.CHAINMAIL_CHESTPLATE, Material.IRON_INGOT, 40));
        put("Iron Armor", new PricetagsItems(Material.IRON_CHESTPLATE, Material.GOLD_INGOT, 12));
        put("Diamond Armor", new PricetagsItems(Material.DIAMOND_CHESTPLATE, Material.EMERALD, 6));

        // Perma Upgrade items
        put("Sharper Blade", new PricetagsItems(Material.DIAMOND_SWORD, Material.DIAMOND, 4));
        put("Gift for the Poor", new PricetagsItems(Material.DEAD_BUSH, Material.DIAMOND, 6));
        put("Eye for an Eye", new PricetagsItems(Material.ENDER_EYE, Material.DIAMOND, 6));
        put("Mine A Holic", new PricetagsItems(Material.GOLDEN_PICKAXE, Material.DIAMOND, 4));
        put("Make It Rain", new PricetagsItems(Material.GHAST_TEAR, Material.DIAMOND, 6));
        put("Holy Light", new PricetagsItems(Material.EXPERIENCE_BOTTLE, Material.DIAMOND, 6));
        put("Tough Skin", new PricetagsItems(Material.LEATHER, Material.DIAMOND, 4));
    }};
    
    public static Inventory UpgradeMenu(TeamGroupMaker team) {
        Inventory upgradeMenuTemplate = Bukkit.getServer().createInventory(null, InventoryType.BARREL, "Upgrades");

        // Sharper Blade Entity
        ItemStack sharperBlade = itemBuyList.get("Sharper Blade").createItemStack(1,
            ChatColor.AQUA + "Sharper Blade",
            Arrays.asList("Permanently upgrade weapon\nSharpness by 1 for team."),
            ItemFlag.HIDE_ATTRIBUTES);

        // Mine-A-Holic Entity
        ItemStack mineAHolic = itemBuyList.get("Mine A Holic").createItemStack(1, 
            ChatColor.AQUA + "Mine-A-Holic",
            Arrays.asList("Permanently upgrade weapon\nEfficiency by 1 for team."),
            ItemFlag.HIDE_ATTRIBUTES);

        // Make It Rain! Entity
        ItemStack makeItRain = itemBuyList.get("Make It Rain").createItemStack(1,
            ChatColor.AQUA + "Make it Rain!",
            Arrays.asList("Permanently upgrade resource\nspawning faster."));

        // Holy Light
        ItemStack holyLight = itemBuyList.get("Holy Light").createItemStack(1,
            ChatColor.AQUA + "Holy Light",
            Arrays.asList("Permanent health regeneration\nat your team base."));

        // Tough Skin
        ItemStack toughSkin = itemBuyList.get("Tough Skin").createItemStack(1,
            ChatColor.AQUA + "Tough Skin",
            Arrays.asList("Permanent upgrade armor\nProtection by 1 for team."));

        // Eye for an eye
        ItemStack eyeForEye = itemBuyList.get("Eye for an Eye").createItemStack(1,
            ChatColor.AQUA + "Eye for an Eye",
            Arrays.asList("Permanent upgrade armor\nThorns by 1 for team."));

        // Gift for the Poor
        ItemStack giftPoor = itemBuyList.get("Gift for the Poor").createItemStack(1, 
            ChatColor.AQUA + "Gift for the Poor", 
            Arrays.asList("Something special is coming."));

        upgradeMenuTemplate.setItem(10, sharperBlade);
        upgradeMenuTemplate.setItem(11, mineAHolic);
        upgradeMenuTemplate.setItem(12, makeItRain);
        upgradeMenuTemplate.setItem(13, holyLight);
        upgradeMenuTemplate.setItem(14, toughSkin);
        upgradeMenuTemplate.setItem(15, eyeForEye);
        upgradeMenuTemplate.setItem(16, giftPoor);

        return upgradeMenuTemplate;
    }

    public static Inventory UpgradeMenu() {
        Inventory upgradeMenuTemplate = Bukkit.getServer().createInventory(null, InventoryType.BARREL, "Upgrade Menu");

        // Sharper Blade Entity
        ItemStack sharperBlade = itemBuyList.get("Sharper Blade").createItemStack(1,
            ChatColor.AQUA + "Sharper Blade",
            Arrays.asList("Permanently upgrade weapon\nSharpness by 1 for team."),
            ItemFlag.HIDE_ATTRIBUTES);

        // Mine-A-Holic Entity
        ItemStack mineAHolic = itemBuyList.get("Mine A Holic").createItemStack(1, 
            ChatColor.AQUA + "Mine-A-Holic",
            Arrays.asList("Permanently upgrade weapon\nEfficiency by 1 for team."),
            ItemFlag.HIDE_ATTRIBUTES);

        // Make It Rain! Entity
        ItemStack makeItRain = itemBuyList.get("Make It Rain").createItemStack(1,
            ChatColor.AQUA + "Make it Rain!",
            Arrays.asList("Permanently upgrade resource\nspawning faster."));

        // Holy Light
        ItemStack holyLight = itemBuyList.get("Holy Light").createItemStack(1,
            ChatColor.AQUA + "Holy Light",
            Arrays.asList("Permanent health regeneration\nat your team base."));

        // Tough Skin
        ItemStack toughSkin = itemBuyList.get("Tough Skin").createItemStack(1,
            ChatColor.AQUA + "Tough Skin",
            Arrays.asList("Permanent upgrade armor\nProtection by 1 for team."));

        // Eye for an eye
        ItemStack eyeForEye = itemBuyList.get("Eye for an Eye").createItemStack(1,
            ChatColor.AQUA + "Eye for an Eye",
            Arrays.asList("Permanent upgrade armor\nThorns by 1 for team."));

        // Gift for the Poor
        ItemStack giftPoor = itemBuyList.get("Gift for the Poor").createItemStack(1, 
            ChatColor.AQUA + "Gift for the Poor", 
            Arrays.asList("Something special is coming."));

        upgradeMenuTemplate.setItem(10, sharperBlade);
        upgradeMenuTemplate.setItem(11, mineAHolic);
        upgradeMenuTemplate.setItem(12, makeItRain);
        upgradeMenuTemplate.setItem(13, holyLight);
        upgradeMenuTemplate.setItem(14, toughSkin);
        upgradeMenuTemplate.setItem(15, eyeForEye);
        upgradeMenuTemplate.setItem(16, giftPoor);

        return upgradeMenuTemplate;
    }

    private static Inventory MainShopMenu() {
        Inventory shopMenuTemplate = Bukkit.getServer().createInventory(null, 54, "Main Shop");
        shopMenuTemplate = firstRowMenu(shopMenuTemplate);

        ItemStack stoneSword = itemBuyList.get("Stone Sword").createItemStack(1);
        ItemStack ironSword = itemBuyList.get("Iron Sword").createItemStack(1);

        shopMenuTemplate.setItem(21, stoneSword);
        shopMenuTemplate.setItem(22, ironSword);

        return shopMenuTemplate;
    }

    private static Inventory WeaponShopMenu() {
        Inventory shopWeaponMenuTemplate = Bukkit.getServer().createInventory(null, 54, "Weapon Shop");
        shopWeaponMenuTemplate = firstRowMenu(shopWeaponMenuTemplate);

        ItemStack stoneSword = itemBuyList.get("Stone Sword").createItemStack(1);
        ItemStack ironSword = itemBuyList.get("Iron Sword").createItemStack(1);
        ItemStack diamondSword = itemBuyList.get("Diamond Sword").createItemStack(1);
        ItemStack regularBow = itemBuyList.get("Regular Bow").createItemStack(1);
        ItemStack punchBow1 = itemBuyList.get("Punch Bow").createItemStack(1);
        ItemStack punchBow2 = itemBuyList.get("Punch 2 Bow").createItemStack(1);
        
        ItemStack arrow = itemBuyList.get("Arrow").createItemStack(1);

        shopWeaponMenuTemplate.setItem(19, stoneSword);
        shopWeaponMenuTemplate.setItem(20, ironSword);
        shopWeaponMenuTemplate.setItem(21, diamondSword);
        shopWeaponMenuTemplate.setItem(22, regularBow);
        shopWeaponMenuTemplate.setItem(23, punchBow1);
        shopWeaponMenuTemplate.setItem(24, punchBow2);
        shopWeaponMenuTemplate.setItem(25, arrow);

        return shopWeaponMenuTemplate;
    }

    private static Inventory ArmorShopMenu() {
        Inventory shopArmorMenuTemplate = Bukkit.getServer().createInventory(null, 54, "Armor Shop");
        shopArmorMenuTemplate = firstRowMenu(shopArmorMenuTemplate);

        ItemStack shield = itemBuyList.get("Shield").createItemStack(1);
        ItemStack chainmail = itemBuyList.get("Chainmail Armor").createItemStack(1);
        ItemStack ironArmor = itemBuyList.get("Iron Armor").createItemStack(1);
        ItemStack diamondArmor = itemBuyList.get("Diamond Armor").createItemStack(1);

        shopArmorMenuTemplate.setItem(19, shield);
        shopArmorMenuTemplate.setItem(20, chainmail);
        shopArmorMenuTemplate.setItem(21, ironArmor);
        shopArmorMenuTemplate.setItem(22, diamondArmor);

        return shopArmorMenuTemplate;
    }

    private static Inventory PotionsShopMenu() {
        Inventory shopPotionMenuTemplate = Bukkit.getServer().createInventory(null, 54, "Potions Shop");
        shopPotionMenuTemplate = firstRowMenu(shopPotionMenuTemplate);

        return shopPotionMenuTemplate;
    }

    private static Inventory BlockShopMenu() {
        Inventory shopBlockMenuTemplate = Bukkit.getServer().createInventory(null, 54, "Block Shop");
        shopBlockMenuTemplate = firstRowMenu(shopBlockMenuTemplate);

        return shopBlockMenuTemplate;
    }

    private static Inventory FoodShopMenu() {
        Inventory shopFoodMenuTemplate = Bukkit.getServer().createInventory(null, 54, "Food Shop");
        shopFoodMenuTemplate = firstRowMenu(shopFoodMenuTemplate);

        return shopFoodMenuTemplate;
    }

    private static Inventory ItemsShopMenu() {
        Inventory shopItemsMenuTemplate = Bukkit.getServer().createInventory(null, 54, "Items Shop");
        shopItemsMenuTemplate = firstRowMenu(shopItemsMenuTemplate);

        return shopItemsMenuTemplate;
    }

    private static Inventory firstRowMenu(Inventory inv) {
        // Main Menu
        ItemStack mainShopItem = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta mainShopMeta = mainShopItem.getItemMeta();
        mainShopMeta.setDisplayName(ChatColor.AQUA + "Main Menu");
        mainShopItem.setItemMeta(mainShopMeta);

        // Weapon Menu
        ItemStack weaponShopItem = new ItemStack(Material.IRON_SWORD);
        ItemMeta weaponShopMeta = weaponShopItem.getItemMeta();
        weaponShopMeta.setDisplayName(ChatColor.AQUA + "Weapon Menu");
        weaponShopMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        weaponShopItem.setItemMeta(weaponShopMeta);

        // Armor Menu
        ItemStack armorShopItem = new ItemStack(Material.IRON_CHESTPLATE);
        ItemMeta armorShopMeta = armorShopItem.getItemMeta();
        armorShopMeta.setDisplayName(ChatColor.AQUA + "Armor Menu");
        armorShopMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        armorShopItem.setItemMeta(armorShopMeta);

        // Potions Menu
        ItemStack potionShopItem = new ItemStack(Material.POTION);
        ItemMeta potionShopMeta = potionShopItem.getItemMeta();
        potionShopMeta.setDisplayName(ChatColor.AQUA + "Potions Menu");
        PotionMeta realPotionMeta = (PotionMeta)potionShopMeta;
        realPotionMeta.setBasePotionData(new PotionData(PotionType.WATER));
        potionShopItem.setItemMeta(potionShopMeta);

        // Blocks Menu
        ItemStack blockShopItem = new ItemStack(Material.WHITE_WOOL);
        ItemMeta blockShopMeta = blockShopItem.getItemMeta();
        blockShopMeta.setDisplayName(ChatColor.AQUA + "Blocks Menu");
        blockShopItem.setItemMeta(blockShopMeta);

        // Food Menu
        ItemStack foodShopItem = new ItemStack(Material.BEEF);
        ItemMeta foodShopMeta = foodShopItem.getItemMeta();
        foodShopMeta.setDisplayName(ChatColor.AQUA + "Food Menu");
        foodShopItem.setItemMeta(foodShopMeta);

        // Items Menu
        ItemStack itemShopItem = new ItemStack(Material.TNT);
        ItemMeta itemShopMeta = itemShopItem.getItemMeta();
        itemShopMeta.setDisplayName(ChatColor.AQUA + "Items Menu");
        itemShopItem.setItemMeta(itemShopMeta);

        // Assign all
        inv.setItem(0, mainShopItem);
        inv.setItem(1, weaponShopItem);
        inv.setItem(2, armorShopItem);
        inv.setItem(3, potionShopItem);
        inv.setItem(4, blockShopItem);
        inv.setItem(5, foodShopItem);
        inv.setItem(6, itemShopItem);

        return inv;
    }

    public static Inventory getOpenShopMenu(String menuName) {
        switch (menuName) {
            case "Main Menu":
                return MainShopMenu();

            case "Weapon Menu":
                return WeaponShopMenu();

            case "Armor Menu":
                return ArmorShopMenu();

            case "Potions Menu":
                return PotionsShopMenu();

            case "Blocks Menu":
                return BlockShopMenu();

            case "Food Menu":
                return FoodShopMenu();

            case "Items Menu":
                return ItemsShopMenu();
        
            default:
                return null;
        }
    }

    public static boolean isBedwarsShopMenu(InventoryView inv) {
        String title = inv.getTitle();
        switch (title) {
            case "Main Shop":
                return true;

            case "Weapon Shop":
                return true;

            case "Armor Shop":
                return true;

            case "Potions Shop":
                return true;

            case "Block Shop":
                return true;

            case "Food Shop":
                return true;

            case "Items Shop":
                return true;
        
            default:
                return false;
        }
    }

    public static boolean isUpgradeMenu(InventoryView inv) {
        if (inv.getTitle() == "Upgrades")
            return true;
        return false;
    }

    public static PricetagsItems getPriceTag(String itemName) {
        if (itemBuyList.containsKey(itemName))
            return itemBuyList.get(itemName);
        return null;
    }

}
