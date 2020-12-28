package com.joenastan.sleepingwars.game.InventoryMenus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joenastan.sleepingwars.game.TeamGroupMaker;
import com.joenastan.sleepingwars.game.ItemPrice.PricetagItems;
import com.joenastan.sleepingwars.game.ItemPrice.PricetagItemsArmorWeapon;
import com.joenastan.sleepingwars.game.ItemPrice.PricetagItemsPotion;
import com.joenastan.sleepingwars.utility.PluginStaticColor;
import com.joenastan.sleepingwars.utility.PluginStaticFunc;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BedwarsShopMenu {

    public static final String VILLAGER_NAME_TAG = ChatColor.GOLD + String.format(
            "BW %sShop", ChatColor.LIGHT_PURPLE + "");

    private static final Map<String, PricetagItems> PRICED_ITEMS = new HashMap<>();
    private static final Map<String, Inventory> SHOP_MENUS = new HashMap<>();
    private static boolean initialized = false;

    private BedwarsShopMenu() { }

    // TODO: Code Smell
    public static void init() {
        if (!initialized) {
            // Set Initialized to true
            initialized = true;
            // Insert all default shop items
            //#region Weapons and Bows
            // Stone Sword
            PRICED_ITEMS.put("Stone Sword", new PricetagItemsArmorWeapon(Material.STONE_SWORD, Material.IRON_INGOT,
                    "Stone Sword", 32, 1, null, new HashMap<>()));
            PRICED_ITEMS.get("Stone Sword").setDisplayColor(ChatColor.GRAY);
            // Iron Sword
            PRICED_ITEMS.put("Iron Sword", new PricetagItemsArmorWeapon(Material.IRON_SWORD, Material.GOLD_INGOT,
                    "Iron Sword", 16, 1, null, new HashMap<>()));
            // Diamond Sword
            PRICED_ITEMS.put("Diamond Sword", new PricetagItemsArmorWeapon(Material.DIAMOND_SWORD, Material.EMERALD,
                    "Diamond Sword", 8, 1, null, new HashMap<>()));
            PRICED_ITEMS.get("Diamond Sword").setDisplayColor(ChatColor.AQUA);
            // Bow
            PRICED_ITEMS.put("Bow", new PricetagItemsArmorWeapon(Material.BOW, Material.GOLD_INGOT,
                    "Bow", 18, 1, null, new HashMap<>()));
            // Bow Level 2
            Map<Enchantment, Integer> lvl2BowEnhancement = new HashMap<>();
            lvl2BowEnhancement.put(Enchantment.ARROW_DAMAGE, 1);
            PRICED_ITEMS.put("Bow II", new PricetagItemsArmorWeapon(Material.BOW, Material.EMERALD,
                    "Bow II", 6, 1, null, lvl2BowEnhancement));
            PRICED_ITEMS.get("Bow II").setDisplayColor(ChatColor.YELLOW);
            // Bow Level 3
            Map<Enchantment, Integer> lvl3BowEnhancement = new HashMap<>();
            lvl3BowEnhancement.put(Enchantment.ARROW_DAMAGE, 2);
            lvl3BowEnhancement.put(Enchantment.ARROW_KNOCKBACK, 2);
            PRICED_ITEMS.put("Bow III", new PricetagItemsArmorWeapon(Material.BOW, Material.EMERALD,
                    "Bow III", 12, 1, null, lvl3BowEnhancement));
            PRICED_ITEMS.get("Bow III").setDisplayColor(ChatColor.GREEN);
            // Arrow
            PRICED_ITEMS.put("Arrow", new PricetagItemsArmorWeapon(Material.ARROW, Material.GOLD_INGOT,
                    "Arrow", 2, 8, null, new HashMap<>()));
            //#endregion

            //#region Armors and Defenses
            // Shield
            PRICED_ITEMS.put("Shield", new PricetagItemsArmorWeapon(Material.SHIELD, Material.GOLD_INGOT,
                    "Shield", 12, 1, null, new HashMap<>()));
            // Chainmail Armor
            PRICED_ITEMS.put("Chainmail Armor", new PricetagItemsArmorWeapon(Material.CHAINMAIL_CHESTPLATE,
                    Material.IRON_INGOT, "Chainmail Armor", 48, 1, null,
                    new HashMap<>()));
            PRICED_ITEMS.get("Chainmail Armor").setDisplayColor(ChatColor.GRAY);
            // Iron Armor
            PRICED_ITEMS.put("Iron Armor", new PricetagItemsArmorWeapon(Material.IRON_CHESTPLATE,
                    Material.GOLD_INGOT, "Iron Armor", 16, 1, null,
                    new HashMap<>()));
            // Diamond Armor
            PRICED_ITEMS.put("Diamond Armor", new PricetagItemsArmorWeapon(Material.DIAMOND_CHESTPLATE,
                    Material.EMERALD, "Diamond Armor", 8, 1, null, new HashMap<>()));
            PRICED_ITEMS.get("Diamond Armor").setDisplayColor(ChatColor.AQUA);
            //#endregion

            //#region Blocks
            // Wool
            PRICED_ITEMS.put("Wool", new PricetagItems(Material.WHITE_WOOL, Material.IRON_INGOT,
                    "Wool", 4, 16, null));
            // Wood
            PRICED_ITEMS.put("Wood", new PricetagItems(Material.OAK_PLANKS, Material.IRON_INGOT,
                    "Wood", 32, 8, null));
            PRICED_ITEMS.get("Wood").setDisplayColor(ChatColor.GOLD);
            // Glass
            PRICED_ITEMS.put("Glass", new PricetagItems(Material.GLASS, Material.IRON_INGOT,
                    "Glass", 16, 8, null));
            PRICED_ITEMS.get("Glass").setDisplayColor(ChatColor.AQUA);
            // End Stone
            PRICED_ITEMS.put("End Stone", new PricetagItems(Material.END_STONE, Material.GOLD_INGOT,
                    "End Stone", 8,12, null));
            PRICED_ITEMS.get("End Stone").setDisplayColor(ChatColor.YELLOW);
            // Terracotta
            PRICED_ITEMS.put("Terracotta", new PricetagItems(Material.TERRACOTTA, Material.GOLD_INGOT,
                    "Terracotta", 6, 8, null));
            PRICED_ITEMS.get("Terracotta").setDisplayColor(ChatColor.GOLD);
            // Obsidian
            PRICED_ITEMS.put("Obsidian", new PricetagItems(Material.OBSIDIAN, Material.EMERALD,
                    "Obsidian", 6, 8, null));
            PRICED_ITEMS.get("Obsidian").setDisplayColor(ChatColor.DARK_BLUE);
            //#endregion

            //#region Potions
            // Swiftness
            PricetagItemsPotion swift3 = new PricetagItemsPotion(Material.POTION, Material.EMERALD,
                    "Swiftness III", 2, 1, null, new PotionData(PotionType.SPEED));
            swift3.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 15, 3));
            swift3.setDisplayColor(ChatColor.LIGHT_PURPLE);
            PRICED_ITEMS.put("Swiftness III", swift3);
            // Leaping
            PricetagItemsPotion leap4 = new PricetagItemsPotion(Material.POTION, Material.EMERALD,
                    "Leaping IV",2, 1, null, new PotionData(PotionType.JUMP));
            leap4.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 15, 4));
            leap4.setDisplayColor(ChatColor.GREEN);
            PRICED_ITEMS.put("Leaping IV", leap4);
            // Strength
            PricetagItemsPotion strength2 = new PricetagItemsPotion(Material.POTION, Material.EMERALD,
                    "Strength II", 6, 1, null, new PotionData(PotionType.STRENGTH));
            strength2.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 15, 2));
            strength2.setDisplayColor(ChatColor.DARK_PURPLE);
            PRICED_ITEMS.put("Strength II", strength2);
            // Invisibility
            PricetagItemsPotion invisibility = new PricetagItemsPotion(Material.POTION, Material.EMERALD,
                    "Invisibility", 8, 1, null, new PotionData(PotionType.INVISIBILITY));
            invisibility.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 15, 1));
            invisibility.setDisplayColor(ChatColor.LIGHT_PURPLE);
            PRICED_ITEMS.put("Invisibility", invisibility);
            //#endregion

            //#region Foods and Beverages
            // Steak
            PRICED_ITEMS.put("Beef Steak", new PricetagItems(Material.COOKED_BEEF, Material.IRON_INGOT,
                    "Beef Steak", 12, 2, null));
            // Cake
            PRICED_ITEMS.put("Cake", new PricetagItems(Material.CAKE, Material.IRON_INGOT,
                    "Cake", 24, 1, null));
            // Baked Potatoes
            PRICED_ITEMS.put("Baked Potato", new PricetagItems(Material.BAKED_POTATO, Material.IRON_INGOT,
                    "Baked Potato", 6, 3, null));
            // Bread
            PRICED_ITEMS.put("Bread", new PricetagItems(Material.BREAD, Material.IRON_INGOT,
                    "Bread", 8, 1, null));
            PRICED_ITEMS.get("Bread").setDisplayColor(ChatColor.YELLOW);
            // Golden Carrot
            PRICED_ITEMS.put("Golden Carrot", new PricetagItems(Material.GOLDEN_CARROT, Material.GOLD_INGOT,
                    "Golden Carrot", 2, 1, null));
            PRICED_ITEMS.get("Golden Carrot").setDisplayColor(ChatColor.YELLOW);
            // Golden Apple
            PRICED_ITEMS.put("Golden Apple", new PricetagItems(Material.GOLDEN_APPLE, Material.GOLD_INGOT,
                    "Golden Apple", 6, 1, null));
            PRICED_ITEMS.get("Golden Apple").setDisplayColor(ChatColor.YELLOW);
            //#endregion

            //#region Tools
            // Shears
            PRICED_ITEMS.put("Shears", new PricetagItemsArmorWeapon(Material.SHEARS, Material.IRON_INGOT,
                    "Shears", 20, 1, null, new HashMap<>()));
            // Wooden Pickaxe
            PRICED_ITEMS.put("Wooden Pickaxe", new PricetagItemsArmorWeapon(Material.WOODEN_PICKAXE, Material.GOLD_INGOT,
                    "Wooden Pickaxe", 6, 1, null, new HashMap<>()));
            // Iron Axe
            PRICED_ITEMS.put("Iron Axe", new PricetagItemsArmorWeapon(Material.IRON_AXE, Material.GOLD_INGOT,
                    "Iron Axe", 12, 1, null, new HashMap<>()));
            // Diamond Pickaxe
            PRICED_ITEMS.put("Diamond Pickaxe", new PricetagItemsArmorWeapon(Material.DIAMOND_PICKAXE, Material.EMERALD,
                    "Diamond Pickaxe", 4, 1, null, new HashMap<>()));
            PRICED_ITEMS.get("Diamond Pickaxe").setDisplayColor(ChatColor.AQUA);
            //#endregion

            //#region Items and Others
            // Ender Pearl
            PRICED_ITEMS.put("Ender Pearl", new PricetagItems(Material.ENDER_PEARL, Material.EMERALD,
                    "Ender Pearl",8, 1, null));
            PRICED_ITEMS.get("Ender Pearl").setDisplayColor(ChatColor.BLUE);
            // Water Bucket
            PRICED_ITEMS.put("Water Bucket", new PricetagItems(Material.WATER_BUCKET, Material.GOLD_INGOT,
                    "Water Bucket", 20, 1, null));
            PRICED_ITEMS.get("Water Bucket").setDisplayColor(ChatColor.AQUA);
            // Notch Apple
            PRICED_ITEMS.put("Notch Apple", new PricetagItems(Material.GOLDEN_APPLE, Material.GOLD_INGOT,
                    "Notch Apple", 12, 1, null));
            PRICED_ITEMS.get("Notch Apple").setDisplayColor(ChatColor.GOLD);
            // Arrows of Harming
            PRICED_ITEMS.put("Arrow of Harming", new PricetagItemsPotion(Material.TIPPED_ARROW, Material.EMERALD,
                    "Arrow of Harming", 2, 16, null,
                    new PotionData(PotionType.INSTANT_DAMAGE)));
            PRICED_ITEMS.get("Arrow of Harming").setDisplayColor(ChatColor.DARK_PURPLE);
            // Silverfish Egg
            PRICED_ITEMS.put("Silverfish Egg", new PricetagItems(Material.SILVERFISH_SPAWN_EGG,
                    Material.IRON_INGOT, "Silverfish Egg", 64, 1, null));
            // TNT
            PRICED_ITEMS.put("TNT", new PricetagItems(Material.TNT, Material.GOLD_INGOT,
                    "TNT", 12, 1, null));
            //#endregion

            // Create all inventory menus
            //#region Weapon Shop Menu
            String shopName = "Weapon Shop";
            Inventory shopWeaponMenuTemplate = Bukkit.getServer().createInventory(null, 54, shopName);
            SHOP_MENUS.put(shopName, shopWeaponMenuTemplate);
            firstRowMenu(shopWeaponMenuTemplate);

            ItemStack i_stoneSword = PRICED_ITEMS.get("Stone Sword").createItem(1);
            ItemStack i_ironSword = PRICED_ITEMS.get("Iron Sword").createItem(1);
            ItemStack i_diamondSword = PRICED_ITEMS.get("Diamond Sword").createItem(1);
            ItemStack i_regularBow = PRICED_ITEMS.get("Bow").createItem(1);
            ItemStack i_lvl2Bow = PRICED_ITEMS.get("Bow II").createItem(1);
            ItemStack i_lvl3Bow = PRICED_ITEMS.get("Bow III").createItem(1);
            ItemStack i_arrow = PRICED_ITEMS.get("Arrow").createItem(1);

            shopWeaponMenuTemplate.setItem(19, i_stoneSword);
            shopWeaponMenuTemplate.setItem(20, i_ironSword);
            shopWeaponMenuTemplate.setItem(21, i_diamondSword);
            shopWeaponMenuTemplate.setItem(22, i_regularBow);
            shopWeaponMenuTemplate.setItem(23, i_lvl2Bow);
            shopWeaponMenuTemplate.setItem(24, i_lvl3Bow);
            shopWeaponMenuTemplate.setItem(25, i_arrow);
            //#endregion

            //#region Armor Shop Menu
            shopName = "Armor Shop";
            Inventory shopArmorMenuTemplate = Bukkit.getServer().createInventory(null, 54, shopName);
            SHOP_MENUS.put(shopName, shopArmorMenuTemplate);
            firstRowMenu(shopArmorMenuTemplate);

            ItemStack i_shield = PRICED_ITEMS.get("Shield").createItem(1);
            ItemStack i_chainmail = PRICED_ITEMS.get("Chainmail Armor").createItem(1);
            ItemStack i_ironArmor = PRICED_ITEMS.get("Iron Armor").createItem(1);
            ItemStack i_diamondArmor = PRICED_ITEMS.get("Diamond Armor").createItem(1);

            shopArmorMenuTemplate.setItem(19, i_shield);
            shopArmorMenuTemplate.setItem(20, i_chainmail);
            shopArmorMenuTemplate.setItem(21, i_ironArmor);
            shopArmorMenuTemplate.setItem(22, i_diamondArmor);
            //#endregion

            //#region Blocks Shop Menu
            shopName = "Block Shop";
            Inventory shopBlockMenuTemplate = Bukkit.getServer().createInventory(null, 54, shopName);
            SHOP_MENUS.put(shopName, shopBlockMenuTemplate);
            firstRowMenu(shopBlockMenuTemplate);

            ItemStack i_wool = PRICED_ITEMS.get("Wool").createItem(1);
            ItemStack i_wood = PRICED_ITEMS.get("Wood").createItem(1);
            ItemStack i_glass = PRICED_ITEMS.get("Glass").createItem(1);
            ItemStack i_end = PRICED_ITEMS.get("End Stone").createItem(1);
            ItemStack i_terracotta = PRICED_ITEMS.get("Terracotta").createItem(1);
            ItemStack i_obsidian = PRICED_ITEMS.get("Obsidian").createItem(1);

            shopBlockMenuTemplate.setItem(19, i_wool);
            shopBlockMenuTemplate.setItem(20, i_wood);
            shopBlockMenuTemplate.setItem(21, i_glass);
            shopBlockMenuTemplate.setItem(22, i_end);
            shopBlockMenuTemplate.setItem(23, i_terracotta);
            shopBlockMenuTemplate.setItem(24, i_obsidian);
            //#endregion

            //#region Potion Shop Menu
            shopName = "Potion Shop";
            Inventory shopPotionMenuTemplate = Bukkit.getServer().createInventory(null, 54, shopName);
            SHOP_MENUS.put(shopName, shopPotionMenuTemplate);
            firstRowMenu(shopPotionMenuTemplate);

            ItemStack i_potionOfSwiftness = PRICED_ITEMS.get("Swiftness III") == null ?
                    new ItemStack(Material.POTION, 1) : PRICED_ITEMS.get("Swiftness III").createItem(1);
            ItemStack i_potionOfLeaping = PRICED_ITEMS.get("Leaping IV") == null ?
                    new ItemStack(Material.POTION, 1) : PRICED_ITEMS.get("Leaping IV").createItem(1);
            ItemStack i_potionOfStrength = PRICED_ITEMS.get("Strength II") == null ?
                    new ItemStack(Material.POTION, 1) : PRICED_ITEMS.get("Strength II").createItem(1);
            ItemStack i_potionOfInvisibility = PRICED_ITEMS.get("Invisibility") == null ?
                    new ItemStack(Material.POTION, 1) : PRICED_ITEMS.get("Invisibility").createItem(1);

            shopPotionMenuTemplate.setItem(19, i_potionOfSwiftness);
            shopPotionMenuTemplate.setItem(20, i_potionOfLeaping);
            shopPotionMenuTemplate.setItem(21, i_potionOfStrength);
            shopPotionMenuTemplate.setItem(22, i_potionOfInvisibility);
            //#endregion

            //#region Food Shop Menu
            shopName = "Food Shop";
            Inventory shopFoodMenuTemplate = Bukkit.getServer().createInventory(null, 54, shopName);
            SHOP_MENUS.put(shopName, shopFoodMenuTemplate);
            firstRowMenu(shopFoodMenuTemplate);

            ItemStack i_beefSteak = PRICED_ITEMS.get("Beef Steak").createItem(1);
            ItemStack i_cake = PRICED_ITEMS.get("Cake").createItem(1);
            ItemStack i_bakedPotatoes = PRICED_ITEMS.get("Baked Potato").createItem(1);
            ItemStack i_bread = PRICED_ITEMS.get("Bread").createItem(1);
            ItemStack i_goldenCarrot = PRICED_ITEMS.get("Golden Carrot").createItem(1);
            ItemStack i_goldenApple = PRICED_ITEMS.get("Golden Apple").createItem(1);

            shopFoodMenuTemplate.setItem(19, i_beefSteak);
            shopFoodMenuTemplate.setItem(20, i_cake);
            shopFoodMenuTemplate.setItem(21, i_bakedPotatoes);
            shopFoodMenuTemplate.setItem(22, i_bread);
            shopFoodMenuTemplate.setItem(23, i_goldenCarrot);
            shopFoodMenuTemplate.setItem(24, i_goldenApple);
            //#endregion

            //#region Tools Shop Menu
            shopName = "Tool Shop";
            Inventory shopToolsMenuTemplate = Bukkit.getServer().createInventory(null, 54, shopName);
            SHOP_MENUS.put(shopName, shopToolsMenuTemplate);
            firstRowMenu(shopToolsMenuTemplate);

            ItemStack i_shears = PRICED_ITEMS.get("Shears").createItem(1);
            ItemStack i_woodenPick = PRICED_ITEMS.get("Wooden Pickaxe").createItem(1);
            ItemStack i_ironAxe = PRICED_ITEMS.get("Iron Axe").createItem(1);
            ItemStack i_diamondPick = PRICED_ITEMS.get("Diamond Pickaxe").createItem(1);

            shopToolsMenuTemplate.setItem(19, i_shears);
            shopToolsMenuTemplate.setItem(20, i_woodenPick);
            shopToolsMenuTemplate.setItem(21, i_ironAxe);
            shopToolsMenuTemplate.setItem(22, i_diamondPick);
            //#endregion

            //#region Items Shop Menu
            shopName = "Item Shop";
            Inventory shopItemsMenuTemplate = Bukkit.getServer().createInventory(null, 54, shopName);
            SHOP_MENUS.put(shopName, shopItemsMenuTemplate);
            firstRowMenu(shopItemsMenuTemplate);

            ItemStack i_enderPearl = PRICED_ITEMS.get("Ender Pearl").createItem(1);
            ItemStack i_waterBucket = PRICED_ITEMS.get("Water Bucket").createItem(1);
            ItemStack i_notchApple = PRICED_ITEMS.get("Notch Apple").createItem(1);
            ItemStack i_harmingArrow = PRICED_ITEMS.get("Arrow of Harming").createItem(1);
            ItemStack i_silverfishEgg = PRICED_ITEMS.get("Silverfish Egg").createItem(1);
            ItemStack i_tnt = PRICED_ITEMS.get("TNT").createItem(1);

            shopItemsMenuTemplate.setItem(19, i_enderPearl);
            shopItemsMenuTemplate.setItem(20, i_waterBucket);
            shopItemsMenuTemplate.setItem(21, i_notchApple);
            shopItemsMenuTemplate.setItem(22, i_harmingArrow);
            shopItemsMenuTemplate.setItem(23, i_silverfishEgg);
            shopItemsMenuTemplate.setItem(24, i_tnt);
            //#endregion

            //#region Main Shop Menu
            shopName = "Main Shop";
            Inventory shopMenuTemplate = Bukkit.getServer().createInventory(null, 54, shopName);
            SHOP_MENUS.put(shopName, shopMenuTemplate);
            firstRowMenu(shopMenuTemplate);

            shopMenuTemplate.setItem(19, i_wool);
            shopMenuTemplate.setItem(20, i_wood);
            shopMenuTemplate.setItem(21, i_stoneSword);
            shopMenuTemplate.setItem(22, i_ironSword);
            shopMenuTemplate.setItem(23, i_potionOfSwiftness);
            shopMenuTemplate.setItem(24, i_potionOfLeaping);
            shopMenuTemplate.setItem(25, i_bakedPotatoes);
            shopMenuTemplate.setItem(28, i_cake);
            shopMenuTemplate.setItem(29, i_goldenApple);
            shopMenuTemplate.setItem(30, i_regularBow);
            shopMenuTemplate.setItem(31, i_arrow);
            shopMenuTemplate.setItem(32, i_enderPearl);
            shopMenuTemplate.setItem(33, i_tnt);
            //#endregion
        }
    }

    public static void destroy() {
        PRICED_ITEMS.clear();
        SHOP_MENUS.clear();
        initialized = false;
    }

    private static void firstRowMenu(Inventory inv) {
        // Main Menu
        ItemStack mainShopItem = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta mainShopMeta = mainShopItem.getItemMeta();
        assert mainShopMeta != null;
        mainShopMeta.setDisplayName(ChatColor.AQUA + "Main Shop");
        mainShopItem.setItemMeta(mainShopMeta);

        // Weapon Menu
        ItemStack weaponShopItem = new ItemStack(Material.IRON_SWORD);
        ItemMeta weaponShopMeta = weaponShopItem.getItemMeta();
        assert weaponShopMeta != null;
        weaponShopMeta.setDisplayName(ChatColor.AQUA + "Weapon Shop");
        weaponShopMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        weaponShopItem.setItemMeta(weaponShopMeta);

        // Armor Menu
        ItemStack armorShopItem = new ItemStack(Material.IRON_CHESTPLATE);
        ItemMeta armorShopMeta = armorShopItem.getItemMeta();
        assert armorShopMeta != null;
        armorShopMeta.setDisplayName(ChatColor.AQUA + "Armor Shop");
        armorShopMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        armorShopItem.setItemMeta(armorShopMeta);

        // Potions Menu
        ItemStack potionShopItem = new ItemStack(Material.POTION);
        ItemMeta potionShopMeta = potionShopItem.getItemMeta();
        assert potionShopMeta != null;
        potionShopMeta.setDisplayName(ChatColor.AQUA + "Potion Shop");
        PotionMeta realPotionMeta = (PotionMeta) potionShopMeta;
        realPotionMeta.setBasePotionData(new PotionData(PotionType.WATER));
        potionShopItem.setItemMeta(potionShopMeta);

        // Blocks Menu
        ItemStack blockShopItem = new ItemStack(Material.WHITE_WOOL);
        ItemMeta blockShopMeta = blockShopItem.getItemMeta();
        assert blockShopMeta != null;
        blockShopMeta.setDisplayName(ChatColor.AQUA + "Block Shop");
        blockShopItem.setItemMeta(blockShopMeta);

        // Food Menu
        ItemStack foodShopItem = new ItemStack(Material.BEEF);
        ItemMeta foodShopMeta = foodShopItem.getItemMeta();
        assert foodShopMeta != null;
        foodShopMeta.setDisplayName(ChatColor.AQUA + "Food Shop");
        foodShopItem.setItemMeta(foodShopMeta);

        // Tools Menu
        ItemStack toolShopItem = new ItemStack(Material.GOLDEN_PICKAXE);
        ItemMeta toolShopMeta = toolShopItem.getItemMeta();
        assert toolShopMeta != null;
        toolShopMeta.setDisplayName(ChatColor.AQUA + "Tool Shop");
        toolShopMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        toolShopItem.setItemMeta(toolShopMeta);

        // Items Menu
        ItemStack itemShopItem = new ItemStack(Material.TNT);
        ItemMeta itemShopMeta = itemShopItem.getItemMeta();
        assert itemShopMeta != null;
        itemShopMeta.setDisplayName(ChatColor.AQUA + "Item Shop");
        itemShopItem.setItemMeta(itemShopMeta);

        // Assign all
        inv.setItem(0, mainShopItem);
        inv.setItem(1, weaponShopItem);
        inv.setItem(2, armorShopItem);
        inv.setItem(3, potionShopItem);
        inv.setItem(4, blockShopItem);
        inv.setItem(5, foodShopItem);
        inv.setItem(6, toolShopItem);
        inv.setItem(7, itemShopItem);

    }

    public static boolean openMenu(@Nonnull Player player, String menuName) {
        Inventory chMenu = SHOP_MENUS.get(ChatColor.stripColor(menuName));
        if (chMenu == null)
            return false;
        player.openInventory(chMenu);
        return true;
    }

    public static PricetagItems selectedSlot(Inventory invMenu, int slot) {
        ItemStack itemBuy = invMenu.getItem(slot);
        if (itemBuy != null) {
            ItemMeta upgradeMeta = itemBuy.getItemMeta();
            return PRICED_ITEMS.get(ChatColor.stripColor(upgradeMeta.getDisplayName()));
        }
        return null;
    }

    public static void buyItem(@Nonnull Player player, @Nonnull PricetagItems pricedItemTag,
                               @Nullable TeamGroupMaker team) {
        // Check default buy item
        Material currency = pricedItemTag.getCurrency();
        List<Integer> onCurrencySlots = new ArrayList<>();
        PlayerInventory playerInv = player.getInventory();
        int countCurrencyAmount = countItemPlayerInventory(playerInv,
                currency, onCurrencySlots);
        // Check if it's enough to buy item
        if (countCurrencyAmount < pricedItemTag.getPrice()) {
            player.sendMessage(ChatColor.RED + "Not enough currency, you cannot afford this.");
        } else {
            // Pay for item
            if (receiveItem(player, pricedItemTag, team)) {
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
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f);
                return;
            }
        }
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
    }

    /**
     * Check player bought the item by type.
     *
     * @param player Player who buy it
     * @param tag Referred price tag
     * @return True if player successfully bought the item, else then false
     */
    private static boolean receiveItem(@Nonnull Player player, @Nonnull PricetagItems tag,
                                       @Nullable TeamGroupMaker team) {
        PlayerInventory playerInv = player.getInventory();
        if (team != null) {
            if (tag instanceof PricetagItemsArmorWeapon) {
                switch (tag.getItemMaterial()) {
                    case STONE_SWORD:
                    case IRON_SWORD:
                    case DIAMOND_SWORD:
                        // Replace the current Item
                        if (!createSword(tag, playerInv, team)) {
                            player.sendMessage(ChatColor.AQUA + "You already have this or better weapon.");
                            return false;
                        }
                        return true;
                    case CHAINMAIL_CHESTPLATE:
                    case IRON_CHESTPLATE:
                    case DIAMOND_CHESTPLATE:
                        if (!createArmor(tag, playerInv, team)) {
                            player.sendMessage(ChatColor.AQUA + "You already have this or better armor");
                            return false;
                        }
                        return true;
                    case SHEARS:
                    case WOODEN_PICKAXE:
                    case IRON_AXE:
                    case DIAMOND_PICKAXE:
                        ItemStack tools = createNormalItem(tag, playerInv);
                        setUpgrade(team, tools);
                        return true;
                    default:
                        break;
                }
            } else if (tag.getItemMaterial() == Material.WHITE_WOOL) {
                ItemStack coloredWool = new ItemStack(PluginStaticColor.woolColor(team.getRawColor()),
                        tag.getDefaultAmount());
                playerInv.addItem(coloredWool);
                return true;
            }
        }
        createNormalItem(tag, playerInv);
        return true;
    }

    /**
     * Create an item that is the same as its display.
     *
     * @param tag Pricetag reference
     */
    private static ItemStack createNormalItem(@Nonnull PricetagItems tag) {
        // Create a new item without lore
        ItemStack createdItemStack = tag.createItem();
        ItemMeta createdItemMeta = createdItemStack.getItemMeta();
        createdItemMeta.setLore(null);
        createdItemStack.setItemMeta(createdItemMeta);
        return createdItemStack;
    }

    /**
     * Create an item that is the same as its display.
     * Adding it into target inventory immediately.
     *
     * @param tag Pricetag reference
     * @param playerInv Target player inventory
     */
    private static ItemStack createNormalItem(@Nonnull PricetagItems tag, @Nonnull PlayerInventory playerInv) {
        // Create a new item without lore
        ItemStack createdItemStack = tag.createItem();
        ItemMeta createdItemMeta = createdItemStack.getItemMeta();
        createdItemMeta.setLore(null);
        createdItemStack.setItemMeta(createdItemMeta);
        playerInv.addItem(createdItemStack);
        return createdItemStack;
    }

    private static boolean createSword(@Nonnull PricetagItems tag, @Nonnull PlayerInventory playerInv,
                                       @Nullable TeamGroupMaker team) {
        ItemStack sword = null;
        for (int i = 0; i < playerInv.getSize(); i++) {
            ItemStack onSlot = playerInv.getItem(i);
            if (onSlot == null)
                continue;
            if (PluginStaticFunc.isSword(onSlot.getType())) {
                sword = onSlot;
                break;
            }
        }
        // Check if player has any kind of sword
        if (sword != null) {
            Material swordMat = sword.getType();
            switch (tag.getItemMaterial()) {
                case STONE_SWORD:
                    if (swordMat == Material.STONE_SWORD || swordMat == Material.IRON_SWORD ||
                            swordMat == Material.DIAMOND_SWORD)
                        return false;
                    break;
                case IRON_SWORD:
                    if (swordMat == Material.IRON_SWORD || swordMat == Material.DIAMOND_SWORD)
                        return false;
                    break;
                case DIAMOND_SWORD:
                    if (swordMat == Material.DIAMOND_SWORD)
                        return false;
                    break;
            }
            sword.setType(tag.getItemMaterial());
        } else {
            sword = createNormalItem(tag, playerInv);
        }
        // Check team exists
        if (team != null)
            setUpgrade(team, sword);
        return true;
    }

    private static boolean createArmor(@Nonnull PricetagItems tag, @Nonnull PlayerInventory playerInv,
                                       @Nullable TeamGroupMaker team) {
        ItemStack bootSample = playerInv.getBoots();
        ItemStack leggingSample = playerInv.getLeggings();
        // Check boot sample
        if (bootSample != null) {
            Material bootMat = bootSample.getType();
            switch (tag.getItemMaterial()) {
                case CHAINMAIL_CHESTPLATE:
                    if (bootMat == Material.CHAINMAIL_BOOTS || bootMat == Material.IRON_BOOTS ||
                            bootMat == Material.DIAMOND_BOOTS)
                        return false;
                    break;
                case IRON_CHESTPLATE:
                    if (bootMat == Material.IRON_BOOTS || bootMat == Material.DIAMOND_BOOTS)
                        return false;
                    break;
                case DIAMOND_CHESTPLATE:
                    if (bootMat == Material.DIAMOND_BOOTS)
                        return false;
                    break;
                default:
                    return false;
            }
        }
        // Check legging sample
        if (leggingSample != null) {
            Material leggingMat = leggingSample.getType();
            switch (tag.getItemMaterial()) {
                case CHAINMAIL_CHESTPLATE:
                    if (leggingMat == Material.CHAINMAIL_BOOTS || leggingMat == Material.IRON_BOOTS ||
                            leggingMat == Material.DIAMOND_BOOTS)
                        return false;
                    break;
                case IRON_CHESTPLATE:
                    if (leggingMat == Material.IRON_BOOTS || leggingMat == Material.DIAMOND_BOOTS)
                        return false;
                    break;
                case DIAMOND_CHESTPLATE:
                    if (leggingMat == Material.DIAMOND_BOOTS)
                        return false;
                    break;
                default:
                    return false;
            }
        }
        // Set boots and legging
        ItemStack newBoot, newLegging;
        switch (tag.getItemMaterial()) {
            case CHAINMAIL_CHESTPLATE:
                newBoot = new ItemStack(Material.CHAINMAIL_BOOTS, 1);
                newLegging = new ItemStack(Material.CHAINMAIL_LEGGINGS, 1);
                break;
            case IRON_CHESTPLATE:
                newBoot = new ItemStack(Material.IRON_BOOTS, 1);
                newLegging = new ItemStack(Material.IRON_LEGGINGS, 1);
                break;
            case DIAMOND_CHESTPLATE:
                newBoot = new ItemStack(Material.DIAMOND_BOOTS, 1);
                newLegging = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
                break;
            default:
                return false;
        }
        // Check team exists
        if (team != null) {
            setUpgrade(team, newBoot);
            setUpgrade(team, newLegging);
        }
        playerInv.setBoots(newBoot);
        playerInv.setLeggings(newLegging);
        return true;
    }

    private static void setUpgrade(@Nonnull TeamGroupMaker team, ItemStack item) {
        ItemMeta metaItem = item.getItemMeta();
        // Sharper Blade
        if (PluginStaticFunc.isSword(item.getType()) && team.getPermLevels()
                .get(BedwarsUpgradeMenu.SHARPER_BLADE) - 1 != 0) {
            metaItem.addEnchant(Enchantment.DAMAGE_ALL, team.getPermLevels()
                    .get(BedwarsUpgradeMenu.SHARPER_BLADE) - 1, true);
        }
        // Tough Skin
        else if (PluginStaticFunc.isHumanEntityArmor(item.getType())) {
            if (team.getPermLevels().get(BedwarsUpgradeMenu.TOUGH_SKIN) - 1 != 0)
                metaItem.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, team.getPermLevels()
                        .get(BedwarsUpgradeMenu.TOUGH_SKIN) - 1, true);
            if (team.getPermLevels().get(BedwarsUpgradeMenu.EYE_FOR_AN_EYE) - 1 != 0)
                metaItem.addEnchant(Enchantment.THORNS, team.getPermLevels()
                        .get(BedwarsUpgradeMenu.EYE_FOR_AN_EYE) - 1, true);
        }
        // Mine A Holic
        else if ((PluginStaticFunc.isAxe(item.getType()) || item.getType() == Material.SHEARS ||
                PluginStaticFunc.isPickaxe(item.getType())) &&
                team.getPermLevels().get(BedwarsUpgradeMenu.MINE_A_HOLIC) - 1 != 0) {
            metaItem.addEnchant(Enchantment.DIG_SPEED, team.getPermLevels()
                    .get(BedwarsUpgradeMenu.MINE_A_HOLIC) - 1, true);
        }
        item.setItemMeta(metaItem);
    }

    public static boolean isBedwarsShopMenu(InventoryView inv) {
        String title = ChatColor.stripColor(inv.getTitle());
        switch (title) {
            case "Main Shop":
            case "Weapon Shop":
            case "Armor Shop":
            case "Potion Shop":
            case "Block Shop":
            case "Food Shop":
            case "Tool Shop":
            case "Item Shop":
                return true;
        }
        return false;
    }

    /**
     * Check if player has the required items to buy something. Must include a slot indexes references by the list.
     *
     * @param playerInv Referred player inventory
     * @param required Required item type
     * @param targetSlots List of slot index that has the same item type, must be referenced from other variable
     * @return True if player has the requirement, else then false
     */
    private static int countItemPlayerInventory(@Nonnull PlayerInventory playerInv, Material required,
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
}
