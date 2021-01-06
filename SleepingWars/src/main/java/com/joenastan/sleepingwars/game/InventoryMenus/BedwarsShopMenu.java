package com.joenastan.sleepingwars.game.InventoryMenus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joenastan.sleepingwars.game.TeamGroupMaker;
import com.joenastan.sleepingwars.game.ItemPrice.PricesItems;
import com.joenastan.sleepingwars.game.ItemPrice.PricesItemsArmorWeapon;
import com.joenastan.sleepingwars.game.ItemPrice.PricesItemsPotion;
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

    private static final Map<String, PricesItems> PRICED_ITEMS = new HashMap<>();
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
            String wn_stoneSw = ChatColor.GRAY + "Stone Sword";
            PRICED_ITEMS.put(wn_stoneSw, new PricesItemsArmorWeapon(Material.STONE_SWORD, Material.IRON_INGOT,
                    wn_stoneSw, 32, 1, null, new HashMap<>()));
            // Iron Sword
            String wn_ironSw = ChatColor.WHITE + "Iron Sword";
            PRICED_ITEMS.put(wn_ironSw, new PricesItemsArmorWeapon(Material.IRON_SWORD, Material.GOLD_INGOT,
                    wn_ironSw, 16, 1, null, new HashMap<>()));
            // Diamond Sword
            String wn_diamondSw = ChatColor.AQUA + "Diamond Sword";
            PRICED_ITEMS.put(wn_diamondSw, new PricesItemsArmorWeapon(Material.DIAMOND_SWORD, Material.EMERALD,
                    wn_diamondSw, 8, 1, null, new HashMap<>()));
            // Bow
            String wn_bow = ChatColor.WHITE + "Bow";
            PRICED_ITEMS.put(wn_bow, new PricesItemsArmorWeapon(Material.BOW, Material.GOLD_INGOT,
                    wn_bow, 18, 1, null, new HashMap<>()));
            // Bow Level 2
            Map<Enchantment, Integer> lvl2BowEnhancement = new HashMap<>();
            lvl2BowEnhancement.put(Enchantment.ARROW_DAMAGE, 1);
            String wn_bow2 = ChatColor.YELLOW + "Bow II";
            PRICED_ITEMS.put(wn_bow2, new PricesItemsArmorWeapon(Material.BOW, Material.EMERALD,
                    wn_bow2, 6, 1, null, lvl2BowEnhancement));
            // Bow Level 3
            Map<Enchantment, Integer> lvl3BowEnhancement = new HashMap<>();
            lvl3BowEnhancement.put(Enchantment.ARROW_DAMAGE, 2);
            lvl3BowEnhancement.put(Enchantment.ARROW_KNOCKBACK, 2);
            String wn_bow3 = ChatColor.GREEN + "Bow III";
            PRICED_ITEMS.put(wn_bow3, new PricesItemsArmorWeapon(Material.BOW, Material.EMERALD,
                    wn_bow3, 12, 1, null, lvl3BowEnhancement));
            // Arrow
            String wn_arrow = ChatColor.WHITE + "Arrow";
            PRICED_ITEMS.put(wn_arrow, new PricesItemsArmorWeapon(Material.ARROW, Material.GOLD_INGOT,
                    wn_arrow, 2, 8, null, new HashMap<>()));
            //#endregion

            //#region Armors and Defenses
            // Shield
            String dn_shield = ChatColor.WHITE + "Shield";
            PRICED_ITEMS.put(dn_shield, new PricesItemsArmorWeapon(Material.SHIELD, Material.GOLD_INGOT,
                    dn_shield, 12, 1, null, new HashMap<>()));
            // Chainmail Armor
            String dn_chainMArmor = ChatColor.GRAY + "Chainmail Armor";
            PRICED_ITEMS.put(dn_chainMArmor, new PricesItemsArmorWeapon(Material.CHAINMAIL_CHESTPLATE, Material.IRON_INGOT,
                    dn_chainMArmor, 48, 1, null, new HashMap<>()));
            // Iron Armor
            String dn_ironArmor = ChatColor.WHITE + "Iron Armor";
            PRICED_ITEMS.put(dn_ironArmor, new PricesItemsArmorWeapon(Material.IRON_CHESTPLATE, Material.GOLD_INGOT,
                    dn_ironArmor, 16, 1, null, new HashMap<>()));
            // Diamond Armor
            String dn_diamondArmor = ChatColor.AQUA + "Diamond Armor";
            PRICED_ITEMS.put(dn_diamondArmor, new PricesItemsArmorWeapon(Material.DIAMOND_CHESTPLATE, Material.EMERALD,
                    dn_diamondArmor, 8, 1, null, new HashMap<>()));
            //#endregion

            //#region Blocks
            // Wool
            String bn_wool = ChatColor.WHITE + "Wool";
            PRICED_ITEMS.put(bn_wool, new PricesItems(Material.WHITE_WOOL, Material.IRON_INGOT,
                    bn_wool, 4, 16, null));
            // Wood
            String bn_wood = ChatColor.GOLD + "Wood";
            PRICED_ITEMS.put(bn_wood, new PricesItems(Material.OAK_PLANKS, Material.IRON_INGOT,
                    bn_wood, 32, 8, null));
            // Glass
            String bn_glass = ChatColor.AQUA + "Glass";
            PRICED_ITEMS.put(bn_glass, new PricesItems(Material.GLASS, Material.IRON_INGOT,
                    bn_glass, 16, 8, null));
            // End Stone
            String bn_endStone = ChatColor.YELLOW + "End Stone";
            PRICED_ITEMS.put(bn_endStone, new PricesItems(Material.END_STONE, Material.GOLD_INGOT,
                    bn_endStone, 8,12, null));
            // Terracotta
            String bn_terracotta = ChatColor.GOLD + "Terracotta";
            PRICED_ITEMS.put(bn_terracotta, new PricesItems(Material.TERRACOTTA, Material.GOLD_INGOT,
                    bn_terracotta, 6, 8, null));
            // Obsidian
            String bn_obsidian = ChatColor.DARK_BLUE + "Obsidian";
            PRICED_ITEMS.put(bn_obsidian, new PricesItems(Material.OBSIDIAN, Material.EMERALD,
                    bn_obsidian, 6, 8, null));
            //#endregion

            //#region Potions
            // Swiftness
            String pn_swift = ChatColor.LIGHT_PURPLE + "Swiftness III";
            PricesItemsPotion swift3 = new PricesItemsPotion(Material.POTION, Material.EMERALD,
                    pn_swift, 2, 1, null, new PotionData(PotionType.SPEED));
            swift3.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 15 * 20, 2));
            PRICED_ITEMS.put(pn_swift, swift3);
            // Leaping
            String pn_leap = ChatColor.GREEN + "Leaping IV";
            PricesItemsPotion leap4 = new PricesItemsPotion(Material.POTION, Material.EMERALD,
                    pn_leap,2, 1, null, new PotionData(PotionType.JUMP));
            leap4.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 15 * 20, 3));
            PRICED_ITEMS.put(pn_leap, leap4);
            // Strength
            String pn_strength = ChatColor.DARK_PURPLE + "Strength II";
            PricesItemsPotion strength2 = new PricesItemsPotion(Material.POTION, Material.EMERALD,
                    pn_strength, 6, 1, null, new PotionData(PotionType.STRENGTH));
            strength2.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 15 * 20, 1));
            PRICED_ITEMS.put(pn_strength, strength2);
            // Invisibility
            String pn_invisible = ChatColor.LIGHT_PURPLE + "Invisibility";
            PricesItemsPotion invisibility = new PricesItemsPotion(Material.POTION, Material.EMERALD,
                    pn_invisible, 8, 1, null, new PotionData(PotionType.INVISIBILITY));
            invisibility.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 15 * 20, 0));
            PRICED_ITEMS.put(pn_invisible, invisibility);
            //#endregion

            //#region Foods and Beverages
            // Steak
            String fn_beefSteak = ChatColor.WHITE + "Beef Steak";
            PRICED_ITEMS.put(fn_beefSteak, new PricesItems(Material.COOKED_BEEF, Material.IRON_INGOT,
                    fn_beefSteak, 12, 2, null));
            // Cake
            String fn_cake = ChatColor.WHITE + "Cake";
            PRICED_ITEMS.put(fn_cake, new PricesItems(Material.CAKE, Material.IRON_INGOT,
                    fn_cake, 24, 1, null));
            // Baked Potatoes
            String fn_bakedPotato = ChatColor.WHITE + "Baked Potato";
            PRICED_ITEMS.put(fn_bakedPotato, new PricesItems(Material.BAKED_POTATO, Material.IRON_INGOT,
                    fn_bakedPotato, 6, 3, null));
            // Bread
            String fn_bread = ChatColor.YELLOW + "Bread";
            PRICED_ITEMS.put(fn_bread, new PricesItems(Material.BREAD, Material.IRON_INGOT,
                    fn_bread, 8, 1, null));
            // Golden Carrot
            String fn_goldenCarrot = ChatColor.YELLOW + "Golden Carrot";
            PRICED_ITEMS.put(fn_goldenCarrot, new PricesItems(Material.GOLDEN_CARROT, Material.GOLD_INGOT,
                    fn_goldenCarrot, 2, 1, null));
            // Golden Apple
            String fn_goldenApple = ChatColor.YELLOW + "Golden Apple";
            PRICED_ITEMS.put(fn_goldenApple, new PricesItems(Material.GOLDEN_APPLE, Material.GOLD_INGOT,
                    fn_goldenApple, 6, 1, null));
            //#endregion

            //#region Tools
            // Shears
            String tn_shears = ChatColor.WHITE + "Shears";
            PRICED_ITEMS.put(tn_shears, new PricesItemsArmorWeapon(Material.SHEARS, Material.IRON_INGOT,
                    tn_shears, 20, 1, null, new HashMap<>()));
            // Wooden Pickaxe
            String tn_woodPickaxe = ChatColor.WHITE + "Wooden Pickaxe";
            PRICED_ITEMS.put(tn_woodPickaxe, new PricesItemsArmorWeapon(Material.WOODEN_PICKAXE, Material.GOLD_INGOT,
                    tn_woodPickaxe, 6, 1, null, new HashMap<>()));
            // Iron Axe
            String tn_ironAxe = ChatColor.WHITE + "Iron Axe";
            PRICED_ITEMS.put(tn_ironAxe, new PricesItemsArmorWeapon(Material.IRON_AXE, Material.GOLD_INGOT,
                    tn_ironAxe, 12, 1, null, new HashMap<>()));
            // Diamond Pickaxe
            String tn_diamondPick = ChatColor.AQUA + "Diamond Pickaxe";
            PRICED_ITEMS.put(tn_diamondPick, new PricesItemsArmorWeapon(Material.DIAMOND_PICKAXE, Material.EMERALD,
                    tn_diamondPick, 4, 1, null, new HashMap<>()));
            //#endregion

            //#region Items and Others
            // Ender Pearl
            String in_endPearl = ChatColor.BLUE + "Ender Pearl";
            PRICED_ITEMS.put(in_endPearl, new PricesItems(Material.ENDER_PEARL, Material.EMERALD,
                    in_endPearl,8, 1, null));
            // Water Bucket
            String in_waterBucket = ChatColor.AQUA + "Water Bucket";
            PRICED_ITEMS.put(in_waterBucket, new PricesItems(Material.WATER_BUCKET, Material.GOLD_INGOT,
                    in_waterBucket, 20, 1, null));
            // Notch Apple
            String in_notchApple = ChatColor.GOLD + "Notch Apple";
            PRICED_ITEMS.put(in_notchApple, new PricesItems(Material.GOLDEN_APPLE, Material.GOLD_INGOT,
                    in_notchApple, 12, 1, null));
            // Arrows of Harming
            String in_harmArrow = ChatColor.DARK_PURPLE + "Arrow of Harming";
            PRICED_ITEMS.put(in_harmArrow, new PricesItemsPotion(Material.TIPPED_ARROW, Material.EMERALD,
                    in_harmArrow, 2, 16, null, new PotionData(PotionType.INSTANT_DAMAGE)));
            // Silverfish Egg
            String in_silverFish = ChatColor.WHITE + "Silverfish Egg";
            PRICED_ITEMS.put(in_silverFish, new PricesItems(Material.SILVERFISH_SPAWN_EGG, Material.IRON_INGOT,
                    in_silverFish, 64, 1, null));
            // TNT
            String in_tnt = ChatColor.WHITE + "TNT";
            PRICED_ITEMS.put(in_tnt, new PricesItems(Material.TNT, Material.GOLD_INGOT,
                    in_tnt, 12, 1, null));
            //#endregion

            // Create all inventory menus
            //#region Weapon Shop Menu
            String shopName = "Weapon Shop";
            Inventory shopWeaponMenuTemplate = Bukkit.getServer().createInventory(null, 54, shopName);
            SHOP_MENUS.put(shopName, shopWeaponMenuTemplate);
            firstRowMenu(shopWeaponMenuTemplate);

            ItemStack i_stoneSword = PRICED_ITEMS.get(wn_stoneSw).createItem(1);
            ItemStack i_ironSword = PRICED_ITEMS.get(wn_ironSw).createItem(1);
            ItemStack i_diamondSword = PRICED_ITEMS.get(wn_diamondSw).createItem(1);
            ItemStack i_regularBow = PRICED_ITEMS.get(wn_bow).createItem(1);
            ItemStack i_lvl2Bow = PRICED_ITEMS.get(wn_bow2).createItem(1);
            ItemStack i_lvl3Bow = PRICED_ITEMS.get(wn_bow3).createItem(1);
            ItemStack i_arrow = PRICED_ITEMS.get(wn_arrow).createItem(1);

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

            ItemStack i_shield = PRICED_ITEMS.get(dn_shield).createItem(1);
            ItemStack i_chainmail = PRICED_ITEMS.get(dn_chainMArmor).createItem(1);
            ItemStack i_ironArmor = PRICED_ITEMS.get(dn_ironArmor).createItem(1);
            ItemStack i_diamondArmor = PRICED_ITEMS.get(dn_diamondArmor).createItem(1);

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

            ItemStack i_wool = PRICED_ITEMS.get(bn_wool).createItem(1);
            ItemStack i_wood = PRICED_ITEMS.get(bn_wood).createItem(1);
            ItemStack i_glass = PRICED_ITEMS.get(bn_glass).createItem(1);
            ItemStack i_end = PRICED_ITEMS.get(bn_endStone).createItem(1);
            ItemStack i_terracotta = PRICED_ITEMS.get(bn_terracotta).createItem(1);
            ItemStack i_obsidian = PRICED_ITEMS.get(bn_obsidian).createItem(1);

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

            ItemStack i_potionOfSwiftness = PRICED_ITEMS.get(pn_swift) == null ?
                    new ItemStack(Material.POTION, 1) : PRICED_ITEMS.get(pn_swift).createItem(1);
            ItemStack i_potionOfLeaping = PRICED_ITEMS.get(pn_leap) == null ?
                    new ItemStack(Material.POTION, 1) : PRICED_ITEMS.get(pn_leap).createItem(1);
            ItemStack i_potionOfStrength = PRICED_ITEMS.get(pn_strength) == null ?
                    new ItemStack(Material.POTION, 1) : PRICED_ITEMS.get(pn_strength).createItem(1);
            ItemStack i_potionOfInvisibility = PRICED_ITEMS.get(pn_invisible) == null ?
                    new ItemStack(Material.POTION, 1) : PRICED_ITEMS.get(pn_invisible).createItem(1);

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

            ItemStack i_beefSteak = PRICED_ITEMS.get(fn_beefSteak).createItem(1);
            ItemStack i_cake = PRICED_ITEMS.get(fn_cake).createItem(1);
            ItemStack i_bakedPotatoes = PRICED_ITEMS.get(fn_bakedPotato).createItem(1);
            ItemStack i_bread = PRICED_ITEMS.get(fn_bread).createItem(1);
            ItemStack i_goldenCarrot = PRICED_ITEMS.get(fn_goldenCarrot).createItem(1);
            ItemStack i_goldenApple = PRICED_ITEMS.get(fn_goldenApple).createItem(1);

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

            ItemStack i_shears = PRICED_ITEMS.get(tn_shears).createItem(1);
            ItemStack i_woodenPick = PRICED_ITEMS.get(tn_woodPickaxe).createItem(1);
            ItemStack i_ironAxe = PRICED_ITEMS.get(tn_ironAxe).createItem(1);
            ItemStack i_diamondPick = PRICED_ITEMS.get(tn_diamondPick).createItem(1);

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

            ItemStack i_enderPearl = PRICED_ITEMS.get(in_endPearl).createItem(1);
            ItemStack i_waterBucket = PRICED_ITEMS.get(in_waterBucket).createItem(1);
            ItemStack i_notchApple = PRICED_ITEMS.get(in_notchApple).createItem(1);
            ItemStack i_harmingArrow = PRICED_ITEMS.get(in_harmArrow).createItem(1);
            ItemStack i_silverfishEgg = PRICED_ITEMS.get(in_silverFish).createItem(1);
            ItemStack i_tnt = PRICED_ITEMS.get(in_tnt).createItem(1);

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
        mainShopMeta.setDisplayName(ChatColor.AQUA + "Main Shop");
        mainShopItem.setItemMeta(mainShopMeta);

        // Weapon Menu
        ItemStack weaponShopItem = new ItemStack(Material.IRON_SWORD);
        ItemMeta weaponShopMeta = weaponShopItem.getItemMeta();
        weaponShopMeta.setDisplayName(ChatColor.AQUA + "Weapon Shop");
        weaponShopMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        weaponShopItem.setItemMeta(weaponShopMeta);

        // Armor Menu
        ItemStack armorShopItem = new ItemStack(Material.IRON_CHESTPLATE);
        ItemMeta armorShopMeta = armorShopItem.getItemMeta();
        armorShopMeta.setDisplayName(ChatColor.AQUA + "Armor Shop");
        armorShopMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        armorShopItem.setItemMeta(armorShopMeta);

        // Potions Menu
        ItemStack potionShopItem = new ItemStack(Material.POTION);
        ItemMeta potionShopMeta = potionShopItem.getItemMeta();
        potionShopMeta.setDisplayName(ChatColor.AQUA + "Potion Shop");
        PotionMeta realPotionMeta = (PotionMeta) potionShopMeta;
        realPotionMeta.setBasePotionData(new PotionData(PotionType.WATER));
        potionShopItem.setItemMeta(potionShopMeta);

        // Blocks Menu
        ItemStack blockShopItem = new ItemStack(Material.WHITE_WOOL);
        ItemMeta blockShopMeta = blockShopItem.getItemMeta();
        blockShopMeta.setDisplayName(ChatColor.AQUA + "Block Shop");
        blockShopItem.setItemMeta(blockShopMeta);

        // Food Menu
        ItemStack foodShopItem = new ItemStack(Material.BEEF);
        ItemMeta foodShopMeta = foodShopItem.getItemMeta();
        foodShopMeta.setDisplayName(ChatColor.AQUA + "Food Shop");
        foodShopItem.setItemMeta(foodShopMeta);

        // Tools Menu
        ItemStack toolShopItem = new ItemStack(Material.GOLDEN_PICKAXE);
        ItemMeta toolShopMeta = toolShopItem.getItemMeta();
        toolShopMeta.setDisplayName(ChatColor.AQUA + "Tool Shop");
        toolShopMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        toolShopItem.setItemMeta(toolShopMeta);

        // Items Menu
        ItemStack itemShopItem = new ItemStack(Material.TNT);
        ItemMeta itemShopMeta = itemShopItem.getItemMeta();
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

    public static PricesItems selectedSlot(Inventory invMenu, int slot) {
        ItemStack itemBuy = invMenu.getItem(slot);
        if (itemBuy != null) {
            ItemMeta itemBuyMeta = itemBuy.getItemMeta();
            return PRICED_ITEMS.get(itemBuyMeta.getDisplayName());
        }
        return null;
    }

    public static void buyItem(@Nonnull Player player, @Nonnull PricesItems pricedItemTag,
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
    private static boolean receiveItem(@Nonnull Player player, @Nonnull PricesItems tag,
                                       @Nullable TeamGroupMaker team) {
        PlayerInventory playerInv = player.getInventory();
        if (team != null) {
            if (tag instanceof PricesItemsArmorWeapon) {
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
    private static ItemStack createNormalItem(@Nonnull PricesItems tag) {
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
    private static ItemStack createNormalItem(@Nonnull PricesItems tag, @Nonnull PlayerInventory playerInv) {
        // Create a new item without lore
        ItemStack createdItemStack = tag.createItem();
        ItemMeta createdItemMeta = createdItemStack.getItemMeta();
        createdItemMeta.setLore(null);
        createdItemStack.setItemMeta(createdItemMeta);
        playerInv.addItem(createdItemStack);
        return createdItemStack;
    }

    private static boolean createSword(@Nonnull PricesItems tag, @Nonnull PlayerInventory playerInv,
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

    private static boolean createArmor(@Nonnull PricesItems tag, @Nonnull PlayerInventory playerInv,
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
