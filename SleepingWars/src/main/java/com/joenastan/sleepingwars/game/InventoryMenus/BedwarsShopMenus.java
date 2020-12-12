package com.joenastan.sleepingwars.game.InventoryMenus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joenastan.sleepingwars.game.TeamGroupMaker;
import com.joenastan.sleepingwars.game.ItemPrice.PricetagsItems;
import com.joenastan.sleepingwars.game.ItemPrice.PricetagsItemsArmorWeapon;
import com.joenastan.sleepingwars.game.ItemPrice.PricetagsItemsPotion;
import com.joenastan.sleepingwars.utility.UsefulStaticFunctions;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BedwarsShopMenus implements BedwarsMenus {

    private final Map<String, PricetagsItems> priceItems = new HashMap<>();
    private TeamGroupMaker team;

    public BedwarsShopMenus(TeamGroupMaker team) {
        this.team = team;
        // Insert all default shop items
        //#region Weapons and Bows
        // Stone Sword
        ItemMeta stoneSwordMeta = new ItemStack(Material.STONE_SWORD, 1).getItemMeta();
        stoneSwordMeta.setDisplayName(ChatColor.GRAY + "Stone Sword");
        priceItems.put("Stone Sword", new PricetagsItemsArmorWeapon(Material.STONE_SWORD, Material.IRON_INGOT, 32,
                stoneSwordMeta, 1, new HashMap<Enchantment, Integer>()));
        // Iron Sword
        ItemMeta ironSwordMeta = new ItemStack(Material.IRON_SWORD, 1).getItemMeta();
        ironSwordMeta.setDisplayName(ChatColor.WHITE + "Iron Sword");
        priceItems.put("Iron Sword", new PricetagsItemsArmorWeapon(Material.IRON_SWORD, Material.GOLD_INGOT, 16,
                ironSwordMeta, 1, new HashMap<Enchantment, Integer>()));
        // Diamond Sword
        ItemMeta diamondSwordMeta = new ItemStack(Material.DIAMOND_SWORD, 1).getItemMeta();
        diamondSwordMeta.setDisplayName(ChatColor.AQUA + "Diamond Sword");
        priceItems.put("Diamond Sword", new PricetagsItemsArmorWeapon(Material.DIAMOND_SWORD, Material.EMERALD, 8,
                diamondSwordMeta, 1, new HashMap<Enchantment, Integer>()));
        // Bow
        ItemMeta normalBowMeta = new ItemStack(Material.BOW, 1).getItemMeta();
        normalBowMeta.setDisplayName(ChatColor.WHITE + "Bow");
        priceItems.put("Bow", new PricetagsItemsArmorWeapon(Material.BOW, Material.GOLD_INGOT, 18,
                normalBowMeta, 1, new HashMap<Enchantment, Integer>()));
        // Bow Level 2
        ItemMeta lvl2BowMeta = new ItemStack(Material.BOW, 1).getItemMeta();
        lvl2BowMeta.setDisplayName(ChatColor.YELLOW + "Bow II");
        Map<Enchantment, Integer> lvl2BowEnchancement = new HashMap<Enchantment, Integer>();
        lvl2BowEnchancement.put(Enchantment.ARROW_DAMAGE, 1);
        priceItems.put("Bow II", new PricetagsItemsArmorWeapon(Material.BOW, Material.EMERALD, 6,
                lvl2BowMeta, 1, lvl2BowEnchancement));
        // Bow Level 3
        ItemMeta lvl3BowMeta = new ItemStack(Material.BOW, 1).getItemMeta();
        lvl3BowMeta.setDisplayName(ChatColor.GREEN + "Bow III");
        Map<Enchantment, Integer> lvl3BowEnchancement = new HashMap<Enchantment, Integer>();
        lvl3BowEnchancement.put(Enchantment.ARROW_DAMAGE, 2);
        lvl3BowEnchancement.put(Enchantment.ARROW_KNOCKBACK, 2);
        priceItems.put("Bow III", new PricetagsItemsArmorWeapon(Material.BOW, Material.EMERALD, 14,
                lvl3BowMeta, 1, lvl3BowEnchancement));
        // Arrow
        ItemMeta arrowMeta = new ItemStack(Material.ARROW, 1).getItemMeta();
        arrowMeta.setDisplayName(ChatColor.WHITE + "Arrow");
        priceItems.put("Arrow", new PricetagsItemsArmorWeapon(Material.ARROW, Material.GOLD_INGOT, 3,
                arrowMeta, 8, new HashMap<Enchantment, Integer>()));
        //#endregion

        //#region Armors and Defenses
        // Shield
        ItemMeta shieldMeta = new ItemStack(Material.SHIELD, 1).getItemMeta();
        shieldMeta.setDisplayName(ChatColor.GOLD + "Shield");
        priceItems.put("Shield", new PricetagsItemsArmorWeapon(Material.SHIELD, Material.GOLD_INGOT, 12,
                shieldMeta, 1, new HashMap<Enchantment, Integer>()));
        // Chainmail Armor
        ItemMeta chainmailMeta = new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1).getItemMeta();
        chainmailMeta.setDisplayName(ChatColor.GRAY + "Chainmail Armor");
        priceItems.put("Chainmail Armor", new PricetagsItemsArmorWeapon(Material.CHAINMAIL_CHESTPLATE, Material.IRON_INGOT, 48,
                chainmailMeta, 1, new HashMap<Enchantment, Integer>()));
        // Iron Armor
        ItemMeta ironArmorMeta = new ItemStack(Material.IRON_CHESTPLATE, 1).getItemMeta();
        ironArmorMeta.setDisplayName(ChatColor.WHITE + "Iron Armor");
        priceItems.put("Iron Armor", new PricetagsItemsArmorWeapon(Material.IRON_CHESTPLATE, Material.GOLD_INGOT, 16,
                ironArmorMeta, 1, new HashMap<Enchantment, Integer>()));
        // Diamond Armor
        ItemMeta diamondArmorMeta = new ItemStack(Material.DIAMOND_CHESTPLATE, 1).getItemMeta();
        diamondArmorMeta.setDisplayName(ChatColor.AQUA + "Diamond Armor");
        priceItems.put("Diamond Armor", new PricetagsItemsArmorWeapon(Material.DIAMOND_CHESTPLATE, Material.EMERALD, 8,
                diamondArmorMeta, 1, new HashMap<Enchantment, Integer>()));
        //#endregion

        //#region Blocks
        // Wool
        ItemMeta woolMeta = new ItemStack(Material.WHITE_WOOL, 1).getItemMeta();
        woolMeta.setDisplayName(ChatColor.WHITE + "Wool");
        priceItems.put("Wool", new PricetagsItems(Material.WHITE_WOOL, Material.IRON_INGOT, 4, woolMeta, 16));
        // Wood
        ItemMeta woodMeta = new ItemStack(Material.OAK_PLANKS, 1).getItemMeta();
        woodMeta.setDisplayName(ChatColor.GOLD + "Wood");
        priceItems.put("Wood", new PricetagsItems(Material.OAK_PLANKS, Material.IRON_INGOT, 32, woodMeta, 8));
        // Glass
        ItemMeta glassMeta = new ItemStack(Material.GLASS, 1).getItemMeta();
        glassMeta.setDisplayName(ChatColor.AQUA + "Glass");
        priceItems.put("Glass", new PricetagsItems(Material.GLASS, Material.IRON_INGOT, 16, glassMeta, 8));
        // End Stone
        ItemMeta endBlockMeta = new ItemStack(Material.END_STONE, 1).getItemMeta();
        endBlockMeta.setDisplayName(ChatColor.YELLOW + "End Stone");
        priceItems.put("End Stone", new PricetagsItems(Material.END_STONE, Material.GOLD_INGOT, 8, endBlockMeta, 12));
        // Obsidian
        ItemMeta obsidianMeta = new ItemStack(Material.OBSIDIAN, 1).getItemMeta();
        obsidianMeta.setDisplayName(ChatColor.DARK_BLUE + "Obsidian");
        priceItems.put("Obsidian", new PricetagsItems(Material.OBSIDIAN, Material.EMERALD, 5, obsidianMeta, 8));
        //#endregion

        //#region Potions
        // Swiftness
        ItemMeta potionOfSwiftnessMeta = new ItemStack(Material.POTION, 1).getItemMeta();
        potionOfSwiftnessMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Swiftness III");
        PricetagsItemsPotion swift3 = new PricetagsItemsPotion(Material.POTION, Material.EMERALD, 2,
                potionOfSwiftnessMeta, 1, new PotionData(PotionType.SPEED));
        swift3.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 15, 3));
        priceItems.put("Swiftness III", swift3);
        // Leaping
        ItemMeta potionOfLeapingMeta = new ItemStack(Material.POTION, 1).getItemMeta();
        potionOfLeapingMeta.setDisplayName(ChatColor.GREEN + "Leaping IV");
        PricetagsItemsPotion leap4 = new PricetagsItemsPotion(Material.POTION, Material.EMERALD, 2,
                potionOfLeapingMeta, 1, new PotionData(PotionType.JUMP));
        leap4.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 15, 4));
        priceItems.put("Leaping IV", leap4);
        // Strength
        ItemMeta potionOfStrengthMeta = new ItemStack(Material.POTION, 1).getItemMeta();
        potionOfStrengthMeta.setDisplayName(ChatColor.DARK_PURPLE + "Strength II");
        PricetagsItemsPotion strength2 = new PricetagsItemsPotion(Material.POTION, Material.EMERALD, 5,
                potionOfStrengthMeta, 1, new PotionData(PotionType.STRENGTH));
        strength2.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 15, 2));
        priceItems.put("Strength II", strength2);
        // Invisibility
        ItemMeta potionOfInvisibleMeta = new ItemStack(Material.POTION, 1).getItemMeta();
        potionOfInvisibleMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Invisibility");
        PricetagsItemsPotion invis = new PricetagsItemsPotion(Material.POTION, Material.EMERALD, 8,
                potionOfInvisibleMeta, 1, new PotionData(PotionType.INVISIBILITY));
        invis.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 15, 1));
        priceItems.put("Invisibility", invis);
        //#endregion

        //#region Foods and Beverages
        // Steak
        ItemMeta steakMeta = new ItemStack(Material.COOKED_BEEF, 1).getItemMeta();
        steakMeta.setDisplayName(ChatColor.WHITE + "Beef Steak");
        priceItems.put("Beef Steak", new PricetagsItems(Material.COOKED_BEEF, Material.IRON_INGOT, 12, steakMeta, 2));
        // Cake
        ItemMeta cakeMeta = new ItemStack(Material.CAKE, 1).getItemMeta();
        cakeMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Cake");
        priceItems.put("Cake", new PricetagsItems(Material.CAKE, Material.IRON_INGOT, 24, cakeMeta, 1));
        // Baked Potatoes
        ItemMeta bakedPotatoMeta = new ItemStack(Material.BAKED_POTATO, 1).getItemMeta();
        bakedPotatoMeta.setDisplayName(ChatColor.WHITE + "Baked Potato");
        priceItems.put("Baked Potato", new PricetagsItems(Material.BAKED_POTATO, Material.IRON_INGOT, 6, bakedPotatoMeta, 3));
        // Golden Carrot
        ItemMeta goldenCarrotMeta = new ItemStack(Material.GOLDEN_CARROT, 1).getItemMeta();
        goldenCarrotMeta.setDisplayName(ChatColor.YELLOW + "Golden Carrot");
        priceItems.put("Golden Carrot", new PricetagsItems(Material.GOLDEN_CARROT, Material.GOLD_INGOT, 2, goldenCarrotMeta, 1));
        // Golden Apple
        ItemMeta goldenAppleMeta = new ItemStack(Material.GOLDEN_APPLE, 1).getItemMeta();
        goldenAppleMeta.setDisplayName(ChatColor.YELLOW + "Golden Apple");
        priceItems.put("Golden Apple", new PricetagsItems(Material.GOLDEN_APPLE, Material.GOLD_INGOT, 6, goldenAppleMeta, 1));
        //#endregion

        //#region Tools
        // Shears
        ItemMeta shearsMeta = new ItemStack(Material.SHEARS, 1).getItemMeta();
        shearsMeta.setDisplayName(ChatColor.WHITE + "Shears");
        priceItems.put("Shears", new PricetagsItemsArmorWeapon(Material.SHEARS, Material.IRON_INGOT, 20, shearsMeta, 1, new HashMap<Enchantment, Integer>()));
        // Wooden Pickaxe
        ItemMeta woodPickMeta = new ItemStack(Material.WOODEN_PICKAXE, 1).getItemMeta();
        woodPickMeta.setDisplayName(ChatColor.WHITE + "Wooden Pickaxe");
        priceItems.put("Wooden Pickaxe", new PricetagsItemsArmorWeapon(Material.WOODEN_PICKAXE, Material.GOLD_INGOT, 6, woodPickMeta, 1, new HashMap<Enchantment, Integer>()));
        // Iron Axe
        ItemMeta ironAxeMeta = new ItemStack(Material.IRON_AXE, 1).getItemMeta();
        ironAxeMeta.setDisplayName(ChatColor.WHITE + "Iron Axe");
        priceItems.put("Iron Axe", new PricetagsItemsArmorWeapon(Material.IRON_AXE, Material.GOLD_INGOT, 12, ironAxeMeta, 1, new HashMap<Enchantment, Integer>()));
        // Diamond Pickaxe
        ItemMeta diamondPickMeta = new ItemStack(Material.DIAMOND_PICKAXE, 1).getItemMeta();
        diamondPickMeta.setDisplayName(ChatColor.AQUA + "Diamond Pickaxe");
        priceItems.put("Diamond Pickaxe", new PricetagsItemsArmorWeapon(Material.DIAMOND_PICKAXE, Material.EMERALD, 4, diamondPickMeta, 1, new HashMap<Enchantment, Integer>()));
        //#endregion

        //#region Items and Others
        // Ender Pearl
        ItemMeta enderPearlMeta = new ItemStack(Material.ENDER_PEARL, 1).getItemMeta();
        enderPearlMeta.setDisplayName(ChatColor.BLUE + "Ender Pearl");
        priceItems.put("Ender Pearl", new PricetagsItems(Material.ENDER_PEARL, Material.EMERALD, 8, enderPearlMeta, 1));
        // Water Bucket
        ItemMeta waterBucketMeta = new ItemStack(Material.WATER_BUCKET, 1).getItemMeta();
        waterBucketMeta.setDisplayName(ChatColor.AQUA + "Water Bucket");
        priceItems.put("Water Bucket", new PricetagsItems(Material.WATER_BUCKET, Material.GOLD_INGOT, 20, waterBucketMeta, 1));
        // Notch Apple
        ItemMeta notchAppleMeta = new ItemStack(Material.GOLDEN_APPLE, 1).getItemMeta();
        notchAppleMeta.setDisplayName(ChatColor.GOLD + "Notch Apple");
        priceItems.put("Notch Apple", new PricetagsItems(Material.GOLDEN_APPLE, Material.GOLD_INGOT, 12, notchAppleMeta, 1));
        // Arrows of Harming
        ItemMeta harmingArrowMeta = new ItemStack(Material.TIPPED_ARROW, 1).getItemMeta();
        harmingArrowMeta.setDisplayName(ChatColor.DARK_PURPLE + "Arrow of Harming");
        priceItems.put("Arrow of Harming", new PricetagsItemsPotion(Material.TIPPED_ARROW, Material.EMERALD, 2,
                harmingArrowMeta, 16, new PotionData(PotionType.INSTANT_DAMAGE)));
        // Silverfish Egg
        ItemMeta silverfishEggMeta = new ItemStack(Material.SILVERFISH_SPAWN_EGG, 1).getItemMeta();
        silverfishEggMeta.setDisplayName(ChatColor.AQUA + "Silverfish Egg");
        priceItems.put("Silverfish Egg", new PricetagsItems(Material.SILVERFISH_SPAWN_EGG, Material.IRON_INGOT, 64, silverfishEggMeta, 1));
        // TNT
        ItemMeta TNTMeta = new ItemStack(Material.TNT, 1).getItemMeta();
        TNTMeta.setDisplayName(ChatColor.RED + "TNT");
        priceItems.put("TNT", new PricetagsItems(Material.TNT, Material.GOLD_INGOT, 10, TNTMeta, 1));
        //#endregion
    }

    private Inventory MainShopMenu() {
        Inventory shopMenuTemplate = Bukkit.getServer().createInventory(null, 54, "Main Shop");
        shopMenuTemplate = firstRowMenu(shopMenuTemplate);

        ItemStack woolio = priceItems.get("Wool").createItem(1);
        ItemStack woodio = priceItems.get("Wood").createItem(1);
        ItemStack stoneSword = priceItems.get("Stone Sword").createItem(1);
        ItemStack ironSword = priceItems.get("Iron Sword").createItem(1);
        ItemStack regularBow = priceItems.get("Bow").createItem(1);
        ItemStack arrow = priceItems.get("Arrow").createItem(1);
        ItemStack potionOfSwiftness = priceItems.get("Swiftness III") == null ? new ItemStack(Material.POTION, 1) : priceItems.get("Swiftness III").createItem(1);
        ItemStack potionOfLeaping = priceItems.get("Leaping IV") == null ? new ItemStack(Material.POTION, 1) : priceItems.get("Leaping IV").createItem(1);
        ItemStack cake = priceItems.get("Cake").createItem(1);
        ItemStack bakedPotatoes = priceItems.get("Baked Potato").createItem(1);
        ItemStack goldenApple = priceItems.get("Golden Apple").createItem(1);
        ItemStack enderPearl = priceItems.get("Ender Pearl").createItem(1);
        ItemStack tnt = priceItems.get("TNT").createItem(1);

        shopMenuTemplate.setItem(19, woolio);
        shopMenuTemplate.setItem(20, woodio);
        shopMenuTemplate.setItem(21, stoneSword);
        shopMenuTemplate.setItem(22, ironSword);
        shopMenuTemplate.setItem(23, potionOfSwiftness);
        shopMenuTemplate.setItem(24, potionOfLeaping);
        shopMenuTemplate.setItem(25, bakedPotatoes);
        shopMenuTemplate.setItem(28, cake);
        shopMenuTemplate.setItem(29, goldenApple);
        shopMenuTemplate.setItem(30, regularBow);
        shopMenuTemplate.setItem(31, arrow);
        shopMenuTemplate.setItem(32, enderPearl);
        shopMenuTemplate.setItem(33, tnt);

        return shopMenuTemplate;
    }

    private Inventory WeaponShopMenu() {
        Inventory shopWeaponMenuTemplate = Bukkit.getServer().createInventory(null, 54, "Weapon Shop");
        shopWeaponMenuTemplate = firstRowMenu(shopWeaponMenuTemplate);

        ItemStack stoneSword = priceItems.get("Stone Sword").createItem(1);
        ItemStack ironSword = priceItems.get("Iron Sword").createItem(1);
        ItemStack diamondSword = priceItems.get("Diamond Sword").createItem(1);
        ItemStack regularBow = priceItems.get("Bow").createItem(1);
        ItemStack lvl2Bow = priceItems.get("Bow II").createItem(1);
        ItemStack lvl3Bow = priceItems.get("Bow III").createItem(1);
        ItemStack arrow = priceItems.get("Arrow").createItem(1);

        shopWeaponMenuTemplate.setItem(19, stoneSword);
        shopWeaponMenuTemplate.setItem(20, ironSword);
        shopWeaponMenuTemplate.setItem(21, diamondSword);
        shopWeaponMenuTemplate.setItem(22, regularBow);
        shopWeaponMenuTemplate.setItem(23, lvl2Bow);
        shopWeaponMenuTemplate.setItem(24, lvl3Bow);
        shopWeaponMenuTemplate.setItem(25, arrow);

        return shopWeaponMenuTemplate;
    }

    private Inventory ArmorShopMenu() {
        Inventory shopArmorMenuTemplate = Bukkit.getServer().createInventory(null, 54, "Armor Shop");
        shopArmorMenuTemplate = firstRowMenu(shopArmorMenuTemplate);

        ItemStack shield = priceItems.get("Shield").createItem(1);
        ItemStack chainmail = priceItems.get("Chainmail Armor").createItem(1);
        ItemStack ironArmor = priceItems.get("Iron Armor").createItem(1);
        ItemStack diamondArmor = priceItems.get("Diamond Armor").createItem(1);

        shopArmorMenuTemplate.setItem(19, shield);
        shopArmorMenuTemplate.setItem(20, chainmail);
        shopArmorMenuTemplate.setItem(21, ironArmor);
        shopArmorMenuTemplate.setItem(22, diamondArmor);

        return shopArmorMenuTemplate;
    }

    private Inventory BlockShopMenu() {
        Inventory shopBlockMenuTemplate = Bukkit.getServer().createInventory(null, 54, "Block Shop");
        shopBlockMenuTemplate = firstRowMenu(shopBlockMenuTemplate);

        ItemStack woolio = priceItems.get("Wool").createItem(1);
        ItemStack woodio = priceItems.get("Wood").createItem(1);
        ItemStack glassio = priceItems.get("Glass").createItem(1);
        ItemStack endio = priceItems.get("End Stone").createItem(1);
        ItemStack obsidiano = priceItems.get("Obsidian").createItem(1);

        shopBlockMenuTemplate.setItem(19, woolio);
        shopBlockMenuTemplate.setItem(20, woodio);
        shopBlockMenuTemplate.setItem(21, glassio);
        shopBlockMenuTemplate.setItem(22, endio);
        shopBlockMenuTemplate.setItem(23, obsidiano);

        return shopBlockMenuTemplate;
    }

    private Inventory PotionsShopMenu() {
        Inventory shopPotionMenuTemplate = Bukkit.getServer().createInventory(null, 54, "Potions Shop");
        shopPotionMenuTemplate = firstRowMenu(shopPotionMenuTemplate);

        ItemStack potionOfSwiftness = priceItems.get("Swiftness III") == null ? new ItemStack(Material.POTION, 1) : priceItems.get("Swiftness III").createItem(1);
        ItemStack potionOfLeaping = priceItems.get("Leaping IV") == null ? new ItemStack(Material.POTION, 1) : priceItems.get("Leaping IV").createItem(1);
        ItemStack potionOfStrength = priceItems.get("Strength II") == null ? new ItemStack(Material.POTION, 1) : priceItems.get("Strength II").createItem(1);
        ItemStack potionOfInvisibility = priceItems.get("Invisibility") == null ? new ItemStack(Material.POTION, 1) : priceItems.get("Invisibility").createItem(1);

        shopPotionMenuTemplate.setItem(19, potionOfSwiftness);
        shopPotionMenuTemplate.setItem(20, potionOfLeaping);
        shopPotionMenuTemplate.setItem(21, potionOfStrength);
        shopPotionMenuTemplate.setItem(22, potionOfInvisibility);

        return shopPotionMenuTemplate;
    }

    private Inventory FoodShopMenu() {
        Inventory shopFoodMenuTemplate = Bukkit.getServer().createInventory(null, 54, "Food Shop");
        shopFoodMenuTemplate = firstRowMenu(shopFoodMenuTemplate);

        ItemStack beefSteak = priceItems.get("Beef Steak").createItem(1);
        ItemStack cake = priceItems.get("Cake").createItem(1);
        ItemStack bakedPotatoes = priceItems.get("Baked Potato").createItem(1);
        ItemStack goldenCarrot = priceItems.get("Golden Carrot").createItem(1);
        ItemStack goldenApple = priceItems.get("Golden Apple").createItem(1);

        shopFoodMenuTemplate.setItem(19, beefSteak);
        shopFoodMenuTemplate.setItem(20, cake);
        shopFoodMenuTemplate.setItem(21, bakedPotatoes);
        shopFoodMenuTemplate.setItem(22, goldenCarrot);
        shopFoodMenuTemplate.setItem(23, goldenApple);

        return shopFoodMenuTemplate;
    }

    private Inventory ToolsMenu() {
        Inventory shopToolsMenuTemplate = Bukkit.getServer().createInventory(null, 54, "Tools Shop");
        shopToolsMenuTemplate = firstRowMenu(shopToolsMenuTemplate);

        ItemStack shears = priceItems.get("Shears").createItem(1);
        ItemStack woodenPick = priceItems.get("Wooden Pickaxe").createItem(1);
        ItemStack ironAxe = priceItems.get("Iron Axe").createItem(1);
        ItemStack diamondPick = priceItems.get("Diamond Pickaxe").createItem(1);

        shopToolsMenuTemplate.setItem(19, shears);
        shopToolsMenuTemplate.setItem(20, woodenPick);
        shopToolsMenuTemplate.setItem(21, ironAxe);
        shopToolsMenuTemplate.setItem(22, diamondPick);

        return shopToolsMenuTemplate;
    }

    private Inventory ItemsShopMenu() {
        Inventory shopItemsMenuTemplate = Bukkit.getServer().createInventory(null, 54, "Items Shop");
        shopItemsMenuTemplate = firstRowMenu(shopItemsMenuTemplate);

        ItemStack enderPearl = priceItems.get("Ender Pearl").createItem(1);
        ItemStack waterBucket = priceItems.get("Water Bucket").createItem(1);
        ItemStack notchApple = priceItems.get("Notch Apple").createItem(1);
        ItemStack harmingArrow = priceItems.get("Arrow of Harming").createItem(1);
        ItemStack silverfishEgg = priceItems.get("Silverfish Egg").createItem(1);
        ItemStack tnt = priceItems.get("TNT").createItem(1);

        shopItemsMenuTemplate.setItem(19, enderPearl);
        shopItemsMenuTemplate.setItem(20, waterBucket);
        shopItemsMenuTemplate.setItem(21, notchApple);
        shopItemsMenuTemplate.setItem(22, harmingArrow);
        shopItemsMenuTemplate.setItem(23, silverfishEgg);
        shopItemsMenuTemplate.setItem(24, tnt);

        return shopItemsMenuTemplate;
    }

    private Inventory firstRowMenu(Inventory inv) {
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

        // Tools Menu
        ItemStack toolShopItem = new ItemStack(Material.GOLDEN_PICKAXE);
        ItemMeta toolShopMeta = toolShopItem.getItemMeta();
        toolShopMeta.setDisplayName(ChatColor.AQUA + "Tools Menu");
        toolShopMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        toolShopItem.setItemMeta(toolShopMeta);

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
        inv.setItem(6, toolShopItem);
        inv.setItem(7, itemShopItem);

        return inv;
    }

    @Override
    public boolean openMenu(Player player, String menuName) {
        switch (menuName) {
            case "Main Menu":
                player.openInventory(MainShopMenu());
                return true;

            case "Weapon Menu":
                player.openInventory(WeaponShopMenu());
                return true;

            case "Armor Menu":
                player.openInventory(ArmorShopMenu());
                return true;

            case "Potions Menu":
                player.openInventory(PotionsShopMenu());
                return true;

            case "Blocks Menu":
                player.openInventory(BlockShopMenu());
                return true;

            case "Food Menu":
                player.openInventory(FoodShopMenu());
                return true;

            case "Tools Menu":
                player.openInventory(ToolsMenu());
                return true;

            case "Items Menu":
                player.openInventory(ItemsShopMenu());
                return true;

            default:
                return false;
        }
    }

    @Override
    public void selectedSlot(Player player, Inventory invMenu, int slot) {
        ItemStack upgradeItem = invMenu.getItem(slot);
        ItemMeta upgradeMeta = upgradeItem.getItemMeta();
        Material upgradeMaterial = upgradeItem.getType();
        PlayerInventory playerInv = player.getInventory();
        PricetagsItems selectedItem = priceItems.get(ChatColor.stripColor(upgradeMeta.getDisplayName()));
        if (selectedItem != null) {
            Material currency = selectedItem.getCurrency();
            if (currency == upgradeMaterial) {
                List<Integer> onCurrencySlots = new ArrayList<Integer>();
                int countCurrencyAmount = countItemPlayerInventory(playerInv, selectedItem.getCurrency(), onCurrencySlots);
                // Check if it's enough to buy it
                if (countCurrencyAmount < selectedItem.getPrice()) {
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
                    player.sendMessage(ChatColor.RED + "Not enough currency, you cannot afford this.");
                    return;
                } else {
                    // Pay for item
                    if (buyItemByType(player, selectedItem)) {
                        int mustPay = selectedItem.getPrice();
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
                        return;
                    }
                }
            }
        }
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
    }

    /**
     * Check player bought the item by type.
     * @param player Player who buy it
     * @param tag Refered price tag
     * @return True if player successfully bought the item, else then false
     */
    private boolean buyItemByType(Player player, PricetagsItems tag) {
        PlayerInventory playerInv = player.getInventory();
        if (tag instanceof PricetagsItemsArmorWeapon) {
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
                            checkUpgrade(newSword);
                            playerInv.setItem(i, newSword);
                            break;
                        }
                        // Cancel buy when already have this stuff
                        else if (onSlot.getType() == Material.STONE_SWORD || onSlot.getType() == Material.IRON_SWORD || onSlot.getType() == Material.DIAMOND_SWORD) {
                            player.sendMessage(ChatColor.AQUA + "You already have this or better weapon.");
                            return false;
                        }
                    }
                    break;

                case IRON_SWORD:
                    for (int i = 0; i < playerInv.getSize(); i++) {
                        ItemStack onSlot = playerInv.getItem(i);
                        if (onSlot == null)
                            continue;
                        // Replace the current Item
                        if (onSlot.getType() == Material.WOODEN_SWORD || onSlot.getType() == Material.STONE_SWORD) {
                            ItemStack newSword = tag.createItem();
                            checkUpgrade(newSword);
                            playerInv.setItem(i, newSword);
                            break;
                        }
                        // Cancel buy when already have this stuff
                        else if (onSlot.getType() == Material.IRON_SWORD || onSlot.getType() == Material.DIAMOND_SWORD) {
                            player.sendMessage(ChatColor.AQUA + "You already have this or better weapon.");
                            return false;
                        }
                    }
                    break;

                case DIAMOND_SWORD:
                    for (int i = 0; i < playerInv.getSize(); i++) {
                        ItemStack onSlot = playerInv.getItem(i);
                        if (onSlot == null)
                            continue;
                        // Replace the current Item
                        if (onSlot.getType() == Material.WOODEN_SWORD || onSlot.getType() == Material.STONE_SWORD || onSlot.getType() == Material.IRON_SWORD) {
                            ItemStack newSword = tag.createItem();
                            checkUpgrade(newSword);
                            playerInv.setItem(i, newSword);
                            break;
                        }
                        // Cancel buy when already have this stuff
                        else if (onSlot.getType() == Material.DIAMOND_SWORD) {
                            player.sendMessage(ChatColor.AQUA + "You already have this or better weapon.");
                            return false;
                        }
                    }
                    break;

                case CHAINMAIL_CHESTPLATE:
                    if (bootSample.getType() == Material.LEATHER_BOOTS) {
                        ItemStack chainmailLegging = new ItemStack(Material.CHAINMAIL_LEGGINGS, 1);
                        ItemStack chainmailBoots = new ItemStack(Material.CHAINMAIL_BOOTS, 1);
                        checkUpgrade(chainmailBoots);
                        checkUpgrade(chainmailLegging);
                        playerInv.setBoots(chainmailBoots);
                        playerInv.setLeggings(chainmailLegging);
                    } else {
                        player.sendMessage(ChatColor.AQUA + "You already have this or better armor");
                        return false;
                    }
                    break;

                case IRON_CHESTPLATE:
                    if (bootSample.getType() == Material.LEATHER_BOOTS || bootSample.getType() == Material.CHAINMAIL_BOOTS) {
                        ItemStack ironLegging = new ItemStack(Material.IRON_LEGGINGS, 1);
                        ItemStack ironBoots = new ItemStack(Material.IRON_BOOTS, 1);
                        checkUpgrade(ironBoots);
                        checkUpgrade(ironLegging);
                        playerInv.setBoots(ironBoots);
                        playerInv.setLeggings(ironLegging);
                    } else {
                        player.sendMessage(ChatColor.AQUA + "You already have this or better armor");
                        return false;
                    }
                    break;

                case DIAMOND_CHESTPLATE:
                    if (bootSample.getType() == Material.LEATHER_BOOTS || bootSample.getType() == Material.CHAINMAIL_BOOTS || bootSample.getType() == Material.IRON_BOOTS) {
                        ItemStack diamondLegging = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
                        ItemStack diamondBoots = new ItemStack(Material.DIAMOND_BOOTS, 1);
                        checkUpgrade(diamondBoots);
                        checkUpgrade(diamondLegging);
                        playerInv.setBoots(diamondBoots);
                        playerInv.setLeggings(diamondLegging);
                    } else {
                        player.sendMessage(ChatColor.AQUA + "You already have this or better armor");
                        return false;
                    }
                    break;

                case SHEARS:
                    ItemStack shears = new ItemStack(Material.SHEARS, 1);
                    checkUpgrade(shears);
                    playerInv.addItem(shears);
                    break;

                case WOODEN_PICKAXE:
                    ItemStack woodenPick = new ItemStack(Material.WOODEN_PICKAXE, 1);
                    checkUpgrade(woodenPick);
                    playerInv.addItem(woodenPick);
                    break;

                case IRON_AXE:
                    ItemStack ironAxe = new ItemStack(Material.IRON_AXE, 1);
                    checkUpgrade(ironAxe);
                    playerInv.addItem(ironAxe);
                    break;

                case DIAMOND_PICKAXE:
                    ItemStack diamondPick = new ItemStack(Material.DIAMOND_PICKAXE, 1);
                    checkUpgrade(diamondPick);
                    playerInv.addItem(diamondPick);
                    break;

                default:
                    // Create a new item without lore
                    ItemStack createdItemStack = tag.createItem();
                    ItemMeta createdItemMeta = createdItemStack.getItemMeta();
                    createdItemMeta.setLore(null);
                    createdItemStack.setItemMeta(createdItemMeta);
                    playerInv.addItem(tag.createItem());
                    break;
            }
        } else if (tag.getItem() == Material.WHITE_WOOL) {
            ItemStack coloredWool = new ItemStack(woolColor(team.getTeamColorPrefix()), tag.getDefaultAmount());
            playerInv.addItem(coloredWool);
        } else {
            // Create a new item without lore
            ItemStack createdItemStack = tag.createItem();
            ItemMeta createdItemMeta = createdItemStack.getItemMeta();
            createdItemMeta.setLore(null);
            createdItemStack.setItemMeta(createdItemMeta);
            playerInv.addItem(tag.createItem());
        }
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f);
        return true;
    }

    private Material woolColor(String color) {
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

    public void checkUpgrade(ItemStack item) {
        ItemMeta metaItem = item.getItemMeta();
        // Sharper Blade
        if (UsefulStaticFunctions.isSword(item.getType()) && team.getPermaLevels().get(BedwarsUpgradeMenus.SHARPER_BLADE) - 1 != 0) {
            metaItem.addEnchant(Enchantment.DAMAGE_ALL, team.getPermaLevels().get(BedwarsUpgradeMenus.SHARPER_BLADE) - 1, true);
        }
        // Tough Skin
        else if (UsefulStaticFunctions.isHumanEntityArmor(item.getType())) {
            if (team.getPermaLevels().get(BedwarsUpgradeMenus.TOUGH_SKIN) - 1 != 0)
                metaItem.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, team.getPermaLevels().get(BedwarsUpgradeMenus.TOUGH_SKIN) - 1, true);
            if (team.getPermaLevels().get(BedwarsUpgradeMenus.EYE_FOR_AN_EYE) - 1 != 0)
                metaItem.addEnchant(Enchantment.THORNS, team.getPermaLevels().get(BedwarsUpgradeMenus.EYE_FOR_AN_EYE) - 1, true);
        }
        // Mine A Holic
        else if ((UsefulStaticFunctions.isAxe(item.getType()) || item.getType() == Material.SHEARS || UsefulStaticFunctions.isPickaxe(item.getType())) &&
                team.getPermaLevels().get(BedwarsUpgradeMenus.MINE_A_HOLIC) - 1 != 0) {
            metaItem.addEnchant(Enchantment.DIG_SPEED, team.getPermaLevels().get(BedwarsUpgradeMenus.MINE_A_HOLIC) - 1, true);
        }
        item.setItemMeta(metaItem);
    }

    public boolean isBedwarsShopMenu(InventoryView inv) {
        String title = ChatColor.stripColor(inv.getTitle());
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

            case "Tools Shop":
                return true;

            case "Items Shop":
                return true;

            default:
                return false;
        }
    }

    /**
     * Check if player has the required items to buy something. Must include a slot indexes references by the list.
     * @param playerInv Refered player inventory
     * @param required Required item type
     * @param targetSlots List of slot index that has the same item type
     * @return True if player has the requirement, else then false
     */
    private int countItemPlayerInventory(PlayerInventory playerInv, Material required, List<Integer> targetSlots) {
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
