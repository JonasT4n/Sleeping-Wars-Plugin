package com.joenastan.sleepingwars.game.InventoryMenus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joenastan.sleepingwars.game.TeamGroupMaker;
import com.joenastan.sleepingwars.game.ItemPrice.PricetagItems;
import com.joenastan.sleepingwars.game.ItemPrice.PricetagItemsArmorWeapon;
import com.joenastan.sleepingwars.game.ItemPrice.PricetagItemsPotion;
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

public class BedwarsShopMenus {

    private static final Map<String, PricetagItems> PRICED_ITEMS = new HashMap<>();
    private static final Map<String, Inventory> SHOP_MENUS = new HashMap<>();
    private static boolean initialized = false;

    private BedwarsShopMenus() { }

    public static void init() {
        if (!initialized) {
            // Set Initialized to true
            initialized = true;
            // Insert all default shop items
            //#region Weapons and Bows
            // Stone Sword
            ItemMeta stoneSwordMeta = new ItemStack(Material.STONE_SWORD, 1).getItemMeta();
            stoneSwordMeta.setDisplayName(ChatColor.GRAY + "Stone Sword");
            PRICED_ITEMS.put("Stone Sword", new PricetagItemsArmorWeapon(Material.STONE_SWORD, Material.IRON_INGOT,
                    32, stoneSwordMeta, 1, new HashMap<Enchantment, Integer>()));
            // Iron Sword
            ItemMeta ironSwordMeta = new ItemStack(Material.IRON_SWORD, 1).getItemMeta();
            ironSwordMeta.setDisplayName(ChatColor.WHITE + "Iron Sword");
            PRICED_ITEMS.put("Iron Sword", new PricetagItemsArmorWeapon(Material.IRON_SWORD, Material.GOLD_INGOT,
                    16, ironSwordMeta, 1, new HashMap<Enchantment, Integer>()));
            // Diamond Sword
            ItemMeta diamondSwordMeta = new ItemStack(Material.DIAMOND_SWORD, 1).getItemMeta();
            diamondSwordMeta.setDisplayName(ChatColor.AQUA + "Diamond Sword");
            PRICED_ITEMS.put("Diamond Sword", new PricetagItemsArmorWeapon(Material.DIAMOND_SWORD, Material.EMERALD,
                    8, diamondSwordMeta, 1, new HashMap<Enchantment, Integer>()));
            // Bow
            ItemMeta normalBowMeta = new ItemStack(Material.BOW, 1).getItemMeta();
            normalBowMeta.setDisplayName(ChatColor.WHITE + "Bow");
            PRICED_ITEMS.put("Bow", new PricetagItemsArmorWeapon(Material.BOW, Material.GOLD_INGOT, 18,
                    normalBowMeta, 1, new HashMap<Enchantment, Integer>()));
            // Bow Level 2
            ItemMeta lvl2BowMeta = new ItemStack(Material.BOW, 1).getItemMeta();
            lvl2BowMeta.setDisplayName(ChatColor.YELLOW + "Bow II");
            Map<Enchantment, Integer> lvl2BowEnhancement = new HashMap<Enchantment, Integer>();
            lvl2BowEnhancement.put(Enchantment.ARROW_DAMAGE, 1);
            PRICED_ITEMS.put("Bow II", new PricetagItemsArmorWeapon(Material.BOW, Material.EMERALD, 6,
                    lvl2BowMeta, 1, lvl2BowEnhancement));
            // Bow Level 3
            ItemMeta lvl3BowMeta = new ItemStack(Material.BOW, 1).getItemMeta();
            lvl3BowMeta.setDisplayName(ChatColor.GREEN + "Bow III");
            Map<Enchantment, Integer> lvl3BowEnhancement = new HashMap<Enchantment, Integer>();
            lvl3BowEnhancement.put(Enchantment.ARROW_DAMAGE, 2);
            lvl3BowEnhancement.put(Enchantment.ARROW_KNOCKBACK, 2);
            PRICED_ITEMS.put("Bow III", new PricetagItemsArmorWeapon(Material.BOW, Material.EMERALD, 12,
                    lvl3BowMeta, 1, lvl3BowEnhancement));
            // Arrow
            ItemMeta arrowMeta = new ItemStack(Material.ARROW, 1).getItemMeta();
            arrowMeta.setDisplayName(ChatColor.WHITE + "Arrow");
            PRICED_ITEMS.put("Arrow", new PricetagItemsArmorWeapon(Material.ARROW, Material.GOLD_INGOT, 2,
                    arrowMeta, 8, new HashMap<Enchantment, Integer>()));
            //#endregion

            //#region Armors and Defenses
            // Shield
            ItemMeta shieldMeta = new ItemStack(Material.SHIELD, 1).getItemMeta();
            shieldMeta.setDisplayName(ChatColor.GOLD + "Shield");
            PRICED_ITEMS.put("Shield", new PricetagItemsArmorWeapon(Material.SHIELD, Material.GOLD_INGOT, 12,
                    shieldMeta, 1, new HashMap<>()));
            // Chainmail Armor
            ItemMeta chainmailMeta = new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1).getItemMeta();
            chainmailMeta.setDisplayName(ChatColor.GRAY + "Chainmail Armor");
            PRICED_ITEMS.put("Chainmail Armor", new PricetagItemsArmorWeapon(Material.CHAINMAIL_CHESTPLATE,
                    Material.IRON_INGOT, 48, chainmailMeta, 1, new HashMap<>()));
            // Iron Armor
            ItemMeta ironArmorMeta = new ItemStack(Material.IRON_CHESTPLATE, 1).getItemMeta();
            ironArmorMeta.setDisplayName(ChatColor.WHITE + "Iron Armor");
            PRICED_ITEMS.put("Iron Armor", new PricetagItemsArmorWeapon(Material.IRON_CHESTPLATE,
                    Material.GOLD_INGOT, 16, ironArmorMeta, 1, new HashMap<>()));
            // Diamond Armor
            ItemMeta diamondArmorMeta = new ItemStack(Material.DIAMOND_CHESTPLATE, 1).getItemMeta();
            diamondArmorMeta.setDisplayName(ChatColor.AQUA + "Diamond Armor");
            PRICED_ITEMS.put("Diamond Armor", new PricetagItemsArmorWeapon(Material.DIAMOND_CHESTPLATE,
                    Material.EMERALD, 8, diamondArmorMeta, 1, new HashMap<>()));
            //#endregion

            //#region Blocks
            // Wool
            ItemMeta woolMeta = new ItemStack(Material.WHITE_WOOL, 1).getItemMeta();
            woolMeta.setDisplayName(ChatColor.WHITE + "Wool");
            PRICED_ITEMS.put("Wool", new PricetagItems(Material.WHITE_WOOL, Material.IRON_INGOT, 4,
                    woolMeta, 16));
            // Wood
            ItemMeta woodMeta = new ItemStack(Material.OAK_PLANKS, 1).getItemMeta();
            woodMeta.setDisplayName(ChatColor.GOLD + "Wood");
            PRICED_ITEMS.put("Wood", new PricetagItems(Material.OAK_PLANKS, Material.IRON_INGOT, 32,
                    woodMeta, 8));
            // Glass
            ItemMeta glassMeta = new ItemStack(Material.GLASS, 1).getItemMeta();
            glassMeta.setDisplayName(ChatColor.AQUA + "Glass");
            PRICED_ITEMS.put("Glass", new PricetagItems(Material.GLASS, Material.IRON_INGOT, 16,
                    glassMeta, 8));
            // End Stone
            ItemMeta endBlockMeta = new ItemStack(Material.END_STONE, 1).getItemMeta();
            endBlockMeta.setDisplayName(ChatColor.YELLOW + "End Stone");
            PRICED_ITEMS.put("End Stone", new PricetagItems(Material.END_STONE, Material.GOLD_INGOT, 8,
                    endBlockMeta, 12));
            // Obsidian
            ItemMeta obsidianMeta = new ItemStack(Material.OBSIDIAN, 1).getItemMeta();
            obsidianMeta.setDisplayName(ChatColor.DARK_BLUE + "Obsidian");
            PRICED_ITEMS.put("Obsidian", new PricetagItems(Material.OBSIDIAN, Material.EMERALD, 6,
                    obsidianMeta, 8));
            //#endregion

            //#region Potions
            // Swiftness
            ItemMeta potionOfSwiftnessMeta = new ItemStack(Material.POTION, 1).getItemMeta();
            potionOfSwiftnessMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Swiftness III");
            PricetagItemsPotion swift3 = new PricetagItemsPotion(Material.POTION, Material.EMERALD, 2,
                    potionOfSwiftnessMeta, 1, new PotionData(PotionType.SPEED));
            swift3.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 15, 3));
            PRICED_ITEMS.put("Swiftness III", swift3);
            // Leaping
            ItemMeta potionOfLeapingMeta = new ItemStack(Material.POTION, 1).getItemMeta();
            potionOfLeapingMeta.setDisplayName(ChatColor.GREEN + "Leaping IV");
            PricetagItemsPotion leap4 = new PricetagItemsPotion(Material.POTION, Material.EMERALD, 2,
                    potionOfLeapingMeta, 1, new PotionData(PotionType.JUMP));
            leap4.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 15, 4));
            PRICED_ITEMS.put("Leaping IV", leap4);
            // Strength
            ItemMeta potionOfStrengthMeta = new ItemStack(Material.POTION, 1).getItemMeta();
            potionOfStrengthMeta.setDisplayName(ChatColor.DARK_PURPLE + "Strength II");
            PricetagItemsPotion strength2 = new PricetagItemsPotion(Material.POTION, Material.EMERALD, 6,
                    potionOfStrengthMeta, 1, new PotionData(PotionType.STRENGTH));
            strength2.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 15, 2));
            PRICED_ITEMS.put("Strength II", strength2);
            // Invisibility
            ItemMeta potionOfInvisibleMeta = new ItemStack(Material.POTION, 1).getItemMeta();
            potionOfInvisibleMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Invisibility");
            PricetagItemsPotion invisibility = new PricetagItemsPotion(Material.POTION, Material.EMERALD, 8,
                    potionOfInvisibleMeta, 1, new PotionData(PotionType.INVISIBILITY));
            invisibility.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 15, 1));
            PRICED_ITEMS.put("Invisibility", invisibility);
            //#endregion

            //#region Foods and Beverages
            // Steak
            ItemMeta steakMeta = new ItemStack(Material.COOKED_BEEF, 1).getItemMeta();
            steakMeta.setDisplayName(ChatColor.WHITE + "Beef Steak");
            PRICED_ITEMS.put("Beef Steak", new PricetagItems(Material.COOKED_BEEF, Material.IRON_INGOT, 12,
                    steakMeta, 2));
            // Cake
            ItemMeta cakeMeta = new ItemStack(Material.CAKE, 1).getItemMeta();
            cakeMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Cake");
            PRICED_ITEMS.put("Cake", new PricetagItems(Material.CAKE, Material.IRON_INGOT, 24,
                    cakeMeta, 1));
            // Baked Potatoes
            ItemMeta bakedPotatoMeta = new ItemStack(Material.BAKED_POTATO, 1).getItemMeta();
            bakedPotatoMeta.setDisplayName(ChatColor.WHITE + "Baked Potato");
            PRICED_ITEMS.put("Baked Potato", new PricetagItems(Material.BAKED_POTATO, Material.IRON_INGOT, 6,
                    bakedPotatoMeta, 3));
            // Golden Carrot
            ItemMeta goldenCarrotMeta = new ItemStack(Material.GOLDEN_CARROT, 1).getItemMeta();
            goldenCarrotMeta.setDisplayName(ChatColor.YELLOW + "Golden Carrot");
            PRICED_ITEMS.put("Golden Carrot", new PricetagItems(Material.GOLDEN_CARROT, Material.GOLD_INGOT, 2,
                    goldenCarrotMeta, 1));
            // Golden Apple
            ItemMeta goldenAppleMeta = new ItemStack(Material.GOLDEN_APPLE, 1).getItemMeta();
            goldenAppleMeta.setDisplayName(ChatColor.YELLOW + "Golden Apple");
            PRICED_ITEMS.put("Golden Apple", new PricetagItems(Material.GOLDEN_APPLE, Material.GOLD_INGOT, 6,
                    goldenAppleMeta, 1));
            //#endregion

            //#region Tools
            // Shears
            ItemMeta shearsMeta = new ItemStack(Material.SHEARS, 1).getItemMeta();
            shearsMeta.setDisplayName(ChatColor.WHITE + "Shears");
            PRICED_ITEMS.put("Shears", new PricetagItemsArmorWeapon(Material.SHEARS, Material.IRON_INGOT, 20,
                    shearsMeta, 1, new HashMap<>()));
            // Wooden Pickaxe
            ItemMeta woodPickMeta = new ItemStack(Material.WOODEN_PICKAXE, 1).getItemMeta();
            woodPickMeta.setDisplayName(ChatColor.WHITE + "Wooden Pickaxe");
            PRICED_ITEMS.put("Wooden Pickaxe", new PricetagItemsArmorWeapon(Material.WOODEN_PICKAXE,
                    Material.GOLD_INGOT, 6, woodPickMeta, 1, new HashMap<>()));
            // Iron Axe
            ItemMeta ironAxeMeta = new ItemStack(Material.IRON_AXE, 1).getItemMeta();
            ironAxeMeta.setDisplayName(ChatColor.WHITE + "Iron Axe");
            PRICED_ITEMS.put("Iron Axe", new PricetagItemsArmorWeapon(Material.IRON_AXE, Material.GOLD_INGOT, 12,
                    ironAxeMeta, 1, new HashMap<Enchantment, Integer>()));
            // Diamond Pickaxe
            ItemMeta diamondPickMeta = new ItemStack(Material.DIAMOND_PICKAXE, 1).getItemMeta();
            diamondPickMeta.setDisplayName(ChatColor.AQUA + "Diamond Pickaxe");
            PRICED_ITEMS.put("Diamond Pickaxe", new PricetagItemsArmorWeapon(Material.DIAMOND_PICKAXE,
                    Material.EMERALD, 4, diamondPickMeta, 1, new HashMap<>()));
            //#endregion

            //#region Items and Others
            // Ender Pearl
            ItemMeta enderPearlMeta = new ItemStack(Material.ENDER_PEARL, 1).getItemMeta();
            enderPearlMeta.setDisplayName(ChatColor.BLUE + "Ender Pearl");
            PRICED_ITEMS.put("Ender Pearl", new PricetagItems(Material.ENDER_PEARL, Material.EMERALD, 8,
                    enderPearlMeta, 1));
            // Water Bucket
            ItemMeta waterBucketMeta = new ItemStack(Material.WATER_BUCKET, 1).getItemMeta();
            waterBucketMeta.setDisplayName(ChatColor.AQUA + "Water Bucket");
            PRICED_ITEMS.put("Water Bucket", new PricetagItems(Material.WATER_BUCKET, Material.GOLD_INGOT, 20,
                    waterBucketMeta, 1));
            // Notch Apple
            ItemMeta notchAppleMeta = new ItemStack(Material.GOLDEN_APPLE, 1).getItemMeta();
            notchAppleMeta.setDisplayName(ChatColor.GOLD + "Notch Apple");
            PRICED_ITEMS.put("Notch Apple", new PricetagItems(Material.GOLDEN_APPLE, Material.GOLD_INGOT, 12,
                    notchAppleMeta, 1));
            // Arrows of Harming
            ItemMeta meta_harmArrow = new ItemStack(Material.TIPPED_ARROW, 1).getItemMeta();
            meta_harmArrow.setDisplayName(ChatColor.DARK_PURPLE + "Arrow of Harming");
            PRICED_ITEMS.put("Arrow of Harming", new PricetagItemsPotion(Material.TIPPED_ARROW, Material.EMERALD, 2,
                    meta_harmArrow, 16, new PotionData(PotionType.INSTANT_DAMAGE)));
            // Silverfish Egg
            ItemMeta meta_silverEgg = new ItemStack(Material.SILVERFISH_SPAWN_EGG, 1).getItemMeta();
            meta_silverEgg.setDisplayName(ChatColor.AQUA + "Silverfish Egg");
            PRICED_ITEMS.put("Silverfish Egg", new PricetagItems(Material.SILVERFISH_SPAWN_EGG,
                    Material.IRON_INGOT, 64, meta_silverEgg, 1));
            // TNT
            ItemMeta TNTMeta = new ItemStack(Material.TNT, 1).getItemMeta();
            TNTMeta.setDisplayName(ChatColor.RED + "TNT");
            PRICED_ITEMS.put("TNT", new PricetagItems(Material.TNT, Material.GOLD_INGOT, 12,
                    TNTMeta, 1));
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
            ItemStack i_obsidian = PRICED_ITEMS.get("Obsidian").createItem(1);

            shopBlockMenuTemplate.setItem(19, i_wool);
            shopBlockMenuTemplate.setItem(20, i_wood);
            shopBlockMenuTemplate.setItem(21, i_glass);
            shopBlockMenuTemplate.setItem(22, i_end);
            shopBlockMenuTemplate.setItem(23, i_obsidian);
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
            ItemStack i_goldenCarrot = PRICED_ITEMS.get("Golden Carrot").createItem(1);
            ItemStack i_goldenApple = PRICED_ITEMS.get("Golden Apple").createItem(1);

            shopFoodMenuTemplate.setItem(19, i_beefSteak);
            shopFoodMenuTemplate.setItem(20, i_cake);
            shopFoodMenuTemplate.setItem(21, i_bakedPotatoes);
            shopFoodMenuTemplate.setItem(22, i_goldenCarrot);
            shopFoodMenuTemplate.setItem(23, i_goldenApple);
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

    public static PricetagItems selectedSlot(@Nonnull Player player, Inventory invMenu, int slot) {
        ItemStack upgradeItem = invMenu.getItem(slot);
        if (upgradeItem != null) {
            ItemMeta upgradeMeta = upgradeItem.getItemMeta();
            assert upgradeMeta != null;
            return PRICED_ITEMS.get(ChatColor.stripColor(upgradeMeta.getDisplayName()));
        }
        return null;
    }

    public static boolean buyItem(@Nonnull Player player, @Nonnull PricetagItems pricedItemTag,
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
                return true;
            }
        }
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
        return false;
    }

    /**
     * Check player bought the item by type.
     *
     * @param player Player who buy it
     * @param tag Refered price tag
     * @return True if player successfully bought the item, else then false
     */
    private static boolean receiveItem(@Nonnull Player player, @Nonnull PricetagItems tag,
                                       @Nullable TeamGroupMaker team) {
        PlayerInventory playerInv = player.getInventory();
        if (team != null) {
            if (tag instanceof PricetagItemsArmorWeapon) {
                ItemStack bootSample = playerInv.getBoots();
                switch (tag.getItem()) {
                    case STONE_SWORD:
                        for (int i = 0; i < playerInv.getSize(); i++) {
                            ItemStack onSlot = playerInv.getItem(i);
                            if (onSlot == null)
                                continue;
                            // Replace the current Item
                            if (onSlot.getType() == Material.WOODEN_SWORD) {
                                ItemStack newSword = tag.createItem();
                                setUpgrade(team, newSword);
                                playerInv.setItem(i, newSword);
                                break;
                            }
                            // Cancel buy when already have this stuff
                            else if (onSlot.getType() == Material.STONE_SWORD || onSlot.getType() ==
                                    Material.IRON_SWORD || onSlot.getType() == Material.DIAMOND_SWORD) {
                                player.sendMessage(ChatColor.AQUA + "You already have this or better weapon.");
                                return false;
                            }
                        }
                        return true;

                    case IRON_SWORD:
                        for (int i = 0; i < playerInv.getSize(); i++) {
                            ItemStack onSlot = playerInv.getItem(i);
                            if (onSlot == null)
                                continue;
                            // Replace the current Item
                            if (onSlot.getType() == Material.WOODEN_SWORD || onSlot.getType() ==
                                    Material.STONE_SWORD) {
                                ItemStack newSword = tag.createItem();
                                setUpgrade(team, newSword);
                                playerInv.setItem(i, newSword);
                                break;
                            }
                            // Cancel buy when already have this stuff
                            else if (onSlot.getType() == Material.IRON_SWORD || onSlot.getType()
                                    == Material.DIAMOND_SWORD) {
                                player.sendMessage(ChatColor.AQUA + "You already have this or better weapon.");
                                return false;
                            }
                        }
                        return true;

                    case DIAMOND_SWORD:
                        for (int i = 0; i < playerInv.getSize(); i++) {
                            ItemStack onSlot = playerInv.getItem(i);
                            if (onSlot == null)
                                continue;
                            // Replace the current Item
                            if (onSlot.getType() == Material.WOODEN_SWORD || onSlot.getType() ==
                                    Material.STONE_SWORD || onSlot.getType() == Material.IRON_SWORD) {
                                ItemStack newSword = tag.createItem();
                                setUpgrade(team, newSword);
                                playerInv.setItem(i, newSword);
                                break;
                            }
                            // Cancel buy when already have this stuff
                            else if (onSlot.getType() == Material.DIAMOND_SWORD) {
                                player.sendMessage(ChatColor.AQUA + "You already have this or better weapon.");
                                return false;
                            }
                        }
                        return true;

                    case CHAINMAIL_CHESTPLATE:
                        if (bootSample.getType() == Material.LEATHER_BOOTS) {
                            ItemStack chainmailLegging = new ItemStack(Material.CHAINMAIL_LEGGINGS, 1);
                            ItemStack chainmailBoots = new ItemStack(Material.CHAINMAIL_BOOTS, 1);
                            setUpgrade(team, chainmailBoots);
                            setUpgrade(team, chainmailLegging);
                            playerInv.setBoots(chainmailBoots);
                            playerInv.setLeggings(chainmailLegging);
                        } else {
                            player.sendMessage(ChatColor.AQUA + "You already have this or better armor");
                            return false;
                        }
                        return true;

                    case IRON_CHESTPLATE:
                        if (bootSample.getType() == Material.LEATHER_BOOTS || bootSample.getType() ==
                                Material.CHAINMAIL_BOOTS) {
                            ItemStack ironLegging = new ItemStack(Material.IRON_LEGGINGS, 1);
                            ItemStack ironBoots = new ItemStack(Material.IRON_BOOTS, 1);
                            setUpgrade(team, ironBoots);
                            setUpgrade(team, ironLegging);
                            playerInv.setBoots(ironBoots);
                            playerInv.setLeggings(ironLegging);
                        } else {
                            player.sendMessage(ChatColor.AQUA + "You already have this or better armor");
                            return false;
                        }
                        return true;

                    case DIAMOND_CHESTPLATE:
                        if (bootSample.getType() == Material.LEATHER_BOOTS || bootSample.getType() ==
                                Material.CHAINMAIL_BOOTS || bootSample.getType() == Material.IRON_BOOTS) {
                            ItemStack diamondLegging = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
                            ItemStack diamondBoots = new ItemStack(Material.DIAMOND_BOOTS, 1);
                            setUpgrade(team, diamondBoots);
                            setUpgrade(team, diamondLegging);
                            playerInv.setBoots(diamondBoots);
                            playerInv.setLeggings(diamondLegging);
                        } else {
                            player.sendMessage(ChatColor.AQUA + "You already have this or better armor");
                            return false;
                        }
                        return true;

                    case SHEARS:
                        ItemStack shears = new ItemStack(Material.SHEARS, 1);
                        setUpgrade(team, shears);
                        playerInv.addItem(shears);
                        return true;

                    case WOODEN_PICKAXE:
                        ItemStack woodenPick = new ItemStack(Material.WOODEN_PICKAXE, 1);
                        setUpgrade(team, woodenPick);
                        playerInv.addItem(woodenPick);
                        return true;

                    case IRON_AXE:
                        ItemStack ironAxe = new ItemStack(Material.IRON_AXE, 1);
                        setUpgrade(team, ironAxe);
                        playerInv.addItem(ironAxe);
                        return true;

                    case DIAMOND_PICKAXE:
                        ItemStack diamondPick = new ItemStack(Material.DIAMOND_PICKAXE, 1);
                        setUpgrade(team, diamondPick);
                        playerInv.addItem(diamondPick);
                        return true;

                    default:
                        break;
                }
            } else if (tag.getItem() == Material.WHITE_WOOL) {
                ItemStack coloredWool = new ItemStack(woolColor(team.getRawColor()), tag.getDefaultAmount());
                playerInv.addItem(coloredWool);
                return true;
            }
        }
        // Create a new item without lore
        ItemStack createdItemStack = tag.createItem();
        ItemMeta createdItemMeta = createdItemStack.getItemMeta();
        createdItemMeta.setLore(null);
        createdItemStack.setItemMeta(createdItemMeta);
        playerInv.addItem(tag.createItem());
        return true;
    }

    private static Material woolColor(String color) {
        if (color.equalsIgnoreCase("blue")) {
            return Material.BLUE_WOOL;
        } else if (color.equalsIgnoreCase("green")) {
            return Material.GREEN_WOOL;
        } else if (color.equalsIgnoreCase("yellow")) {
            return Material.YELLOW_WOOL;
        } else if (color.equalsIgnoreCase("aqua")) {
            return Material.CYAN_WOOL;
        } else if (color.equalsIgnoreCase("red")) {
            return Material.RED_WOOL;
        } else if (color.equalsIgnoreCase("purple")) {
            return Material.MAGENTA_WOOL;
        } else if (color.equalsIgnoreCase("gold")) {
            return Material.YELLOW_WOOL;
        } else if (color.equalsIgnoreCase("gray")) {
            return Material.GRAY_WOOL;
        } else { // Default is White
            return Material.WHITE_WOOL;
        }
    }

    private static void setUpgrade(@Nonnull TeamGroupMaker team, ItemStack item) {
        ItemMeta metaItem = item.getItemMeta();
        // Sharper Blade
        if (PluginStaticFunc.isSword(item.getType()) && team.getPermLevels()
                .get(BedwarsUpgradeMenus.SHARPER_BLADE) - 1 != 0) {
            metaItem.addEnchant(Enchantment.DAMAGE_ALL, team.getPermLevels()
                    .get(BedwarsUpgradeMenus.SHARPER_BLADE) - 1, true);
        }
        // Tough Skin
        else if (PluginStaticFunc.isHumanEntityArmor(item.getType())) {
            if (team.getPermLevels().get(BedwarsUpgradeMenus.TOUGH_SKIN) - 1 != 0)
                metaItem.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, team.getPermLevels()
                        .get(BedwarsUpgradeMenus.TOUGH_SKIN) - 1, true);
            if (team.getPermLevels().get(BedwarsUpgradeMenus.EYE_FOR_AN_EYE) - 1 != 0)
                metaItem.addEnchant(Enchantment.THORNS, team.getPermLevels()
                        .get(BedwarsUpgradeMenus.EYE_FOR_AN_EYE) - 1, true);
        }
        // Mine A Holic
        else if ((PluginStaticFunc.isAxe(item.getType()) || item.getType() == Material.SHEARS ||
                PluginStaticFunc.isPickaxe(item.getType())) &&
                team.getPermLevels().get(BedwarsUpgradeMenus.MINE_A_HOLIC) - 1 != 0) {
            metaItem.addEnchant(Enchantment.DIG_SPEED, team.getPermLevels()
                    .get(BedwarsUpgradeMenus.MINE_A_HOLIC) - 1, true);
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

            default:
                return false;
        }
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
