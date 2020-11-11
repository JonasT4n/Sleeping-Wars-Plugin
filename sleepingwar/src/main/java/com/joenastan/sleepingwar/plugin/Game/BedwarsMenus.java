package com.joenastan.sleepingwar.plugin.Game;

import net.md_5.bungee.api.ChatColor;
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

import java.util.Arrays;

public class BedwarsMenus {

    public static Inventory UpgradeMenu(TeamGroupMaker team) {
        Inventory upgradeMenuTemplate = Bukkit.getServer().createInventory(null, InventoryType.BARREL, "Upgrades");

        // Sharper Blade Entity
        PricetagsItems sharperBladeMaker = new PricetagsItems(Material.DIAMOND_SWORD, Material.DIAMOND, 4);
        ItemStack sharperBlade = sharperBladeMaker.createItemStack(1, ChatColor.AQUA + "Sharper Blade",
                Arrays.asList("Permanently upgrade weapon", "Sharpness by 1 for team."),
                ItemFlag.HIDE_ATTRIBUTES);

        // Mine-A-Holic Entity
        PricetagsItems mineAHolicMaker = new PricetagsItems(Material.GOLDEN_PICKAXE, Material.DIAMOND, 4);
        ItemStack mineAHolic = mineAHolicMaker.createItemStack(1, ChatColor.AQUA + "Mine-A-Holic",
                Arrays.asList("Permanently upgrade weapon", "Efficiency by 1 for team."),
                ItemFlag.HIDE_ATTRIBUTES);

        // Make It Rain! Entity
        PricetagsItems makeItRainMaker = new PricetagsItems(Material.GHAST_TEAR, Material.DIAMOND, 6);
        ItemStack makeItRain = makeItRainMaker.createItemStack(1,
                ChatColor.AQUA + "Make it Rain!",
                Arrays.asList("Permanently upgrade resource", "spawning faster."));

        // Holy Light
        PricetagsItems holyLightMaker = new PricetagsItems(Material.EXPERIENCE_BOTTLE, Material.DIAMOND, 6);
        ItemStack holyLight = holyLightMaker.createItemStack(1, ChatColor.AQUA + "Holy Light",
                Arrays.asList("Permanent health regeneration", "at your team base."));

        // Tough Skin
        PricetagsItems toughSkinMaker = new PricetagsItems(Material.LEATHER, Material.DIAMOND, 4);
        ItemStack toughSkin = toughSkinMaker.createItemStack(1, ChatColor.AQUA + "Tough Skin",
                Arrays.asList("Permanent upgrade armor", "Protection by 1 for team."));

        // Eye for an eye
        PricetagsItems eyeForEyeMaker = new PricetagsItems(Material.ENDER_EYE, Material.DIAMOND, 6);
        ItemStack eyeForEye = eyeForEyeMaker.createItemStack(1, ChatColor.AQUA + "Eye for an Eye",
                Arrays.asList("Permanent upgrade armor", "Thorns by 1 for team."));

        // Gift for the Poor
        PricetagsItems giftPoorMaker = new PricetagsItems(Material.DEAD_BUSH, Material.DIAMOND, 6);
        ItemStack giftPoor = giftPoorMaker.createItemStack(1, ChatColor.AQUA + "Gift for the Poor",
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
        PricetagsItems sharperBladeMaker = new PricetagsItems(Material.DIAMOND_SWORD, Material.DIAMOND, 4);
        ItemStack sharperBlade = sharperBladeMaker.createItemStack(1, ChatColor.AQUA + "Sharper Blade",
                Arrays.asList("Permanently upgrade weapon", "Sharpness by 1 for team."),
                ItemFlag.HIDE_ATTRIBUTES);

        System.out.println("[DEBUG] Running 1");

        // Mine-A-Holic Entity
        PricetagsItems mineAHolicMaker = new PricetagsItems(Material.GOLDEN_PICKAXE, Material.DIAMOND, 4);
        ItemStack mineAHolic = mineAHolicMaker.createItemStack(1, ChatColor.AQUA + "Mine-A-Holic",
                Arrays.asList("Permanently upgrade weapon", "Efficiency by 1 for team."),
                ItemFlag.HIDE_ATTRIBUTES);

        System.out.println("[DEBUG] Running 1");

        // Make It Rain! Entity
        PricetagsItems makeItRainMaker = new PricetagsItems(Material.GHAST_TEAR, Material.DIAMOND, 6);
        ItemStack makeItRain = makeItRainMaker.createItemStack(1,
                ChatColor.AQUA + "Make it Rain!",
                Arrays.asList("Permanently upgrade resource", "spawning faster."));

        System.out.println("[DEBUG] Running 1");

        // Holy Light
        PricetagsItems holyLightMaker = new PricetagsItems(Material.EXPERIENCE_BOTTLE, Material.DIAMOND, 6);
        ItemStack holyLight = holyLightMaker.createItemStack(1, ChatColor.AQUA + "Holy Light",
                Arrays.asList("Permanent health regeneration", "at your team base."));

        // Tough Skin
        PricetagsItems toughSkinMaker = new PricetagsItems(Material.LEATHER, Material.DIAMOND, 4);
        ItemStack toughSkin = toughSkinMaker.createItemStack(1, ChatColor.AQUA + "Tough Skin",
                Arrays.asList("Permanent upgrade armor", "Protection by 1 for team."));

        // Eye for an eye
        PricetagsItems eyeForEyeMaker = new PricetagsItems(Material.ENDER_EYE, Material.DIAMOND, 6);
        ItemStack eyeForEye = eyeForEyeMaker.createItemStack(1, ChatColor.AQUA + "Eye for an Eye",
                Arrays.asList("Permanent upgrade armor", "Thorns by 1 for team."));

        System.out.println("[DEBUG] Running 4");

        // Gift for the Poor
        PricetagsItems giftPoorMaker = new PricetagsItems(Material.DEAD_BUSH, Material.DIAMOND, 6);
        ItemStack giftPoor = giftPoorMaker.createItemStack(1, ChatColor.AQUA + "Gift for the Poor",
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

        PricetagsItems stoneSwordMaker = new PricetagsItems(Material.STONE_SWORD, Material.IRON_INGOT, 20);
        ItemStack stoneSword = stoneSwordMaker.createItemStack(1);

        PricetagsItems ironSwordMaker = new PricetagsItems(Material.IRON_SWORD, Material.GOLD_INGOT, 10);
        ItemStack ironSword = ironSwordMaker.createItemStack(1);

        shopMenuTemplate.setItem(21, stoneSword);
        shopMenuTemplate.setItem(22, ironSword);

        return shopMenuTemplate;
    }

    private static Inventory WeaponShopMenu() {
        Inventory shopWeaponMenuTemplate = Bukkit.getServer().createInventory(null, 54, "Weapon Shop");
        shopWeaponMenuTemplate = firstRowMenu(shopWeaponMenuTemplate);

        PricetagsItems stoneSwordMaker = new PricetagsItems(Material.STONE_SWORD, Material.IRON_INGOT, 20);
        ItemStack stoneSword = stoneSwordMaker.createItemStack(1);

        PricetagsItems ironSwordMaker = new PricetagsItems(Material.IRON_SWORD, Material.GOLD_INGOT, 10);
        ItemStack ironSword = ironSwordMaker.createItemStack(1);

        PricetagsItems diamondSwordMaker = new PricetagsItems(Material.DIAMOND_SWORD, Material.EMERALD, 20);
        ItemStack diamondSword = diamondSwordMaker.createItemStack(1);

        PricetagsItems normalBowMaker = new PricetagsItems(Material.BOW, Material.IRON_INGOT, 50);
        ItemStack regularBow = normalBowMaker.createItemStack(1);

        PricetagsItems level1BowMaker = new PricetagsItems(Material.BOW, Material.GOLD_INGOT, 20).addEnchancement(Enchantment.ARROW_DAMAGE, 1);
        ItemStack punchBow1 = level1BowMaker.createItemStack(1, "Bow Level 1");

        PricetagsItems level2BowMaker = new PricetagsItems(Material.BOW, Material.GOLD_INGOT, 32)
                .addEnchancement(Enchantment.ARROW_DAMAGE, 2).addEnchancement(Enchantment.ARROW_KNOCKBACK, 2);
        ItemStack punchBow2 = level2BowMaker.createItemStack(1, "Bow Level 2");

        PricetagsItems arrowMaker = new PricetagsItems(Material.ARROW, Material.GOLD_INGOT, 2);
        ItemStack arrow = arrowMaker.createItemStack(1);

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

        PricetagsItems shieldMaker = new PricetagsItems(Material.SHIELD, Material.EMERALD, 8);
        ItemStack shield = shieldMaker.createItemStack(1);

        PricetagsItems chainmailMaker = new PricetagsItems(Material.CHAINMAIL_CHESTPLATE, Material.IRON_INGOT, 40);
        ItemStack chainmail = chainmailMaker.createItemStack(1);

        PricetagsItems ironArmorMaker = new PricetagsItems(Material.IRON_CHESTPLATE, Material.GOLD_INGOT, 12);
        ItemStack ironArmor = ironArmorMaker.createItemStack(1);

        PricetagsItems diamondArmorMaker = new PricetagsItems(Material.DIAMOND_CHESTPLATE, Material.EMERALD, 6);
        ItemStack diamondArmor = diamondArmorMaker.createItemStack(1);

        shopArmorMenuTemplate.setItem(19, shield);
        shopArmorMenuTemplate.setItem(20, chainmail);
        shopArmorMenuTemplate.setItem(21, ironArmor);
        shopArmorMenuTemplate.setItem(22, diamondArmor);

        return shopArmorMenuTemplate;
    }

    private static Inventory PotionsShopMenu() {
        Inventory shopPotionMenuTemplate = Bukkit.getServer().createInventory(null, 54, "Potions Shop");
        shopPotionMenuTemplate = firstRowMenu(shopPotionMenuTemplate);

        PricetagsItemsPotion potionOfSwiftness2Maker;
        PricetagsItemsPotion potionOfLeapingMaker;
        PricetagsItemsPotion potionOfStrengthMaker;
        PricetagsItemsPotion potionOfInvisibilityMaker;

        try {
            potionOfSwiftness2Maker = new PricetagsItemsPotion(Material.POTION, Material.IRON_INGOT, 20, new PotionData(PotionType.SPEED));
            potionOfLeapingMaker = new PricetagsItemsPotion(Material.POTION, Material.EMERALD, 6, new PotionData(PotionType.JUMP));
            potionOfStrengthMaker = new PricetagsItemsPotion(Material.POTION, Material.GOLD_INGOT, 12, new PotionData(PotionType.STRENGTH));
            potionOfInvisibilityMaker = new PricetagsItemsPotion(Material.POTION, Material.EMERALD, 8, new PotionData(PotionType.INVISIBILITY));

            ItemStack potionOfSwiftness2 = potionOfSwiftness2Maker.createItemStack(1);
            ItemStack potionOfLeaping = potionOfLeapingMaker.createItemStack(1);
            ItemStack potionOfStrength = potionOfStrengthMaker.createItemStack(1);
            ItemStack potionOfInvisibility = potionOfInvisibilityMaker.createItemStack(1);

            shopPotionMenuTemplate.setItem(19, potionOfSwiftness2);
            shopPotionMenuTemplate.setItem(20, potionOfLeaping);
            shopPotionMenuTemplate.setItem(21, potionOfStrength);
            shopPotionMenuTemplate.setItem(22, potionOfInvisibility);

        } catch (Exception exc) {
            System.out.println(exc.getCause());
        }

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
        PotionMeta realPotionMeta = (PotionMeta) potionShopMeta;
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

}
