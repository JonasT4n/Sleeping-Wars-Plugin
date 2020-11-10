package com.joenastan.sleepingwar.plugin.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.World.Environment;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

import com.joenastan.sleepingwar.plugin.SleepingWarsPlugin;
import com.joenastan.sleepingwar.plugin.Game.ResourceSpawner;
import com.joenastan.sleepingwar.plugin.Game.ResourcesType;
import com.joenastan.sleepingwar.plugin.Utility.GameSystemConfig;
import com.joenastan.sleepingwar.plugin.Utility.VoidGenerator;

public class WorldMakerCommand implements Listener, CommandExecutor {

    private Map<Player, Location> previousLocation = new HashMap<Player, Location>();
    private String createCMD = "create"; // command to create the world
    private String editWorldCMD = "edit"; // command to go teleport into bedwars and set to builder mode
    private String systemCMD = "system"; // check and edit game system, or you can edit in yml file in plugin folder
    private String leaveWorldCMD = "leave"; // leave world back to where you were
    private String queueSpawnCMD = "setqspawn"; // set queue spawn for hosting a bedwars
    private String setSpawnCMD = "setspawn"; // set world default spawn, this can be use in all kinds of world
    private String setBlockCMD = "setblock"; // set block on, for default bedrock will be spawned on location
    private String openBuilderCMD = "openb"; // exclusive kit for bedwars
    private String setTeamSpawnCMD = "teamspawn"; // Set Team Spawn by name on that location
    private String setResourceSpawnerCMD = "setrspawn"; // Set Resource Spawner with it's type
    private final GameSystemConfig systemConfig = SleepingWarsPlugin.getGameSystemConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 0) {
            // Classify commands, if there's none of them then ignored
            String initialSubCommand = args[0];
            // Game System System
            if (initialSubCommand.equalsIgnoreCase(systemCMD)) {
                gameSystem(sender, args);
            }
            // Create World
            else if (initialSubCommand.equalsIgnoreCase(createCMD)) {
                if (args.length < 2)
                    sender.sendMessage(ChatColor.GREEN + "You need to insert a world name. " + 
                        ChatColor.YELLOW + "/sworld create <worldname>");
                else {
                    String worldName = "";
                    for (int i = 1; i < args.length; i++) {
                        worldName += args[i];
                        if (i == args.length - 1) { break; }
                        worldName += " ";
                    }
                    createWorld(sender, worldName);
                }
            }
            // Only player in server can use this command
            else if (sender instanceof Player) {
                Player player = ((Player)sender);
                // Set World Spawn
                if (initialSubCommand.equalsIgnoreCase(setSpawnCMD)) {
                    setSpawn(player, player.getWorld());
                } 
                // Teleport to bedwars world
                else if (initialSubCommand.equalsIgnoreCase(editWorldCMD)) {
                    if (args.length < 2)
                        sender.sendMessage(ChatColor.GREEN + "You need to insert a world name. " + 
                            ChatColor.YELLOW + "/sworld edit <worldname>");
                    else {
                        String worldName = "";
                        for (int i = 1; i < args.length; i++) {
                            worldName += args[i];
                            if (i == args.length - 1) { break; }
                            worldName += " ";
                        }
                        editWorld(player, worldName);
                    }
                } 
                // Set Queue Spawn
                else if (initialSubCommand.equalsIgnoreCase(queueSpawnCMD)) {
                    String worldName = player.getWorld().getName();
                    if (systemConfig.getAllWorldName().contains(worldName)) {
                        Location settledSpawn = systemConfig.getQueueLocations(worldName, player.getLocation());
                        sender.sendMessage(ChatColor.LIGHT_PURPLE + "Queue spawn settled on " + ChatColor.AQUA + 
                            String.format("(X/Y/Z): %d/%d/%d", (int)settledSpawn.getX(), (int)settledSpawn.getY(), (int)settledSpawn.getZ()));
                    } else {
                        sender.sendMessage(ChatColor.RED + "You are not in Bedwars world building.");
                    }
                }
                // Set Resource Spawner
                else if (initialSubCommand.equalsIgnoreCase(setResourceSpawnerCMD)) {
                    if (args.length < 4) {
                        player.sendMessage(ChatColor.DARK_PURPLE + "Invalid Input.");
                    } else {
                        World currentOnWorld = player.getWorld();
                        if (systemConfig.getAllWorldName().contains(currentOnWorld.getName())) {
                            addResourceSpawner(player, args[1], args[2], args[3]);
                        } else {
                            player.sendMessage(ChatColor.RED + "You are not in Bedwars World Building.");
                        }
                    }
                }
                // Set Block anywhere
                else if (initialSubCommand.equalsIgnoreCase(setBlockCMD)) {
                    String worldName = player.getWorld().getName();
                    if (args.length < 4) {
                        sender.sendMessage(ChatColor.RED + "Invalid Argument, must include X, Y, and Z.");
                    } else {
                        if (systemConfig.getAllWorldName().contains(worldName)) {
                            World w = Bukkit.getWorld(worldName);
                            int x = Integer.parseInt(args[1]);
                            int y = Integer.parseInt(args[2]);
                            int z = Integer.parseInt(args[3]);
                            w.getBlockAt(x, y, z).setType(Material.BEDROCK);
                        } else {
                            sender.sendMessage(ChatColor.RED + "You are not in Bedwars world building.");
                        }
                    }
                }
                // Open Builder GUI
                else if (initialSubCommand.equalsIgnoreCase(openBuilderCMD)) {
                    openBuilderGUI(player);
                }
                // Set Team Spawner
                else if (initialSubCommand.equalsIgnoreCase(setTeamSpawnCMD)) {
                    if (args.length < 2)
                        sender.sendMessage(ChatColor.GREEN + "You need to insert team name. " + 
                            ChatColor.YELLOW + "/sworld teamspawn <teamname>");
                    else {
                        setTeamSpawn(player, args[1]);
                    }
                }
                // Leave World
                else if (initialSubCommand.equalsIgnoreCase(leaveWorldCMD)) {
                    if (previousLocation.containsKey(player))
                        player.teleport(previousLocation.remove(player));
                    else
                        player.teleport(Bukkit.getServer().getWorlds().get(0).getSpawnLocation());
                    SleepingWarsPlugin.getGameSystemConfig().save();
                }
            }
        } else {
            helpMessage(sender);
        }
        return true;
    }

    private void helpMessage(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "Sleeping World Maker Commands for World Building. " + ChatColor.AQUA + "List of sub-commands:\n" + 
            ChatColor.GREEN + "tp => Teleport to Bedwars World\nsave => Save the world into backup\ncreate => Create Bedwars World\n" +
            "system => Check and Edit Game System in Bedwars world\nsetqspawn => Set Queue spawn on your current position\n" + 
            "setspawn => Set World default spawn on your position, builder mode only\nopenb => Open extra kit for Bedwars");
    }

    private void editWorld(Player player, String worldName) {
        World w = Bukkit.getServer().getWorld(worldName);
        if (w != null) {
            if (systemConfig.getAllWorldName().contains(w.getName())) {
                player.sendMessage(ChatColor.LIGHT_PURPLE + "Teleporting to World " + worldName + "!");
                previousLocation.put(player, player.getLocation());
                player.teleport(w.getSpawnLocation());
                player.setGameMode(GameMode.CREATIVE);
            } else {
                player.sendMessage(ChatColor.RED + "Unrecognised world. this world is not registered by plugin.");
            }
        } else {
            player.sendMessage(ChatColor.RED + "World is not exists, create your Bedwars world using" + ChatColor.GOLD + "/sworld create");
        }
    }

    private void openBuilderGUI(Player player) {
        Inventory extraBuilderGUI = Bukkit.getServer().createInventory(null, 18, "Bedwars Builder Pack");

        ItemStack upgradeMobEgg = new ItemStack(Material.VILLAGER_SPAWN_EGG);
        ItemMeta upgradeEggMeta = upgradeMobEgg.getItemMeta();
        upgradeEggMeta.setDisplayName("Upgrade Villager");
        upgradeMobEgg.setItemMeta(upgradeEggMeta);
        
        ItemStack shopMobEgg = new ItemStack(Material.VILLAGER_SPAWN_EGG);
        ItemMeta shopEggMeta = shopMobEgg.getItemMeta();
        shopEggMeta.setDisplayName("Shop Villager");
        shopMobEgg.setItemMeta(shopEggMeta);

        extraBuilderGUI.setItem(0, upgradeMobEgg);
        extraBuilderGUI.setItem(1, shopMobEgg);

        player.openInventory(extraBuilderGUI);
    }

    private void setSpawn(Player player, World world) {
        Location playerLoc = player.getLocation();
        world.setSpawnLocation(playerLoc.getBlockX(), playerLoc.getBlockY(), playerLoc.getBlockZ());
    }

    private void setTeamSpawn(Player player, String teamName) {
        Location loc = player.getLocation();
        String wName = loc.getWorld().getName();
        if (systemConfig.getAllWorldName().contains(wName)) {
            if (systemConfig.getAllTeamName(wName).contains(teamName)) {
                systemConfig.getTeamSpawner(wName).put(teamName, loc);
            } else {
                player.sendMessage(ChatColor.DARK_PURPLE + "Name of team not available in this world.");
            }
        }
    }

    private void gameSystem(CommandSender sender, String[] innerCommand) {
        if (innerCommand.length > 0) {
            // TODO: Inner Command Use
        } else {
            sender.sendMessage(ChatColor.AQUA + "Inner Command of" + ChatColor.GREEN + "/sworld system: \n" + ChatColor.GOLD + 
                "show => Show Current World Game System\n");
        }
    }

    private void addResourceSpawner(Player player, String teamName, String rsname, String type) {
        Location loc = player.getLocation();
        String wname = player.getWorld().getName();
        ResourcesType rcst = ResourcesType.getType(type);
        if (rcst == null) {
            player.sendMessage(ChatColor.RED + "Invalid resource type.");
        } else {
            if (systemConfig.getAllTeamName(wname).contains(teamName)) {
                ResourceSpawner newRSpawener = new ResourceSpawner(rsname, loc, rcst);
                systemConfig.getResourceSpawnersPack(wname, teamName).put(rsname, newRSpawener);
                player.sendMessage(ChatColor.GREEN + "Added " + type + " spawner with name \"" + rsname + "\" on team " + teamName);
            } else if (teamName.equalsIgnoreCase("public")){
                ResourceSpawner newRSpawener = new ResourceSpawner(rsname, loc, rcst);
                systemConfig.getResourceSpawnersPack(wname, "PUBLIC").put(rsname, newRSpawener);
                player.sendMessage(ChatColor.GREEN + "Added public " + type + " spawner with name \"" + rsname);
            } else {
                player.sendMessage(ChatColor.YELLOW + "There is no team \"" + teamName + "\" in this world.");
            }
        }
    }

    private void createWorld(CommandSender sender, String worldName) {
        // Creating the world
        if (SleepingWarsPlugin.getPlugin().getServer().getWorld(worldName) == null) {
            sender.sendMessage(ChatColor.BLUE + "Creating World...");
            WorldCreator creator = new WorldCreator(worldName)
                .environment(Environment.NORMAL)
                .type(WorldType.FLAT)
                .hardcore(false)
                .generateStructures(false);
            creator.generator(new VoidGenerator());
            World world = Bukkit.createWorld(creator);

            // Set world config
            world.setAutoSave(false);
            world.setKeepSpawnInMemory(false);
            world.setDifficulty(Difficulty.PEACEFUL);
            world.setAnimalSpawnLimit(0);
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            world.setTime(0);

            // Add one block bedrock on mid point
            world.getBlockAt(new Location(world, 0, 9, 0)).setType(Material.BEDROCK);
            systemConfig.defaultSystemConfig(creator, world);
            sender.sendMessage(ChatColor.GREEN + "World \"" + worldName + "\" has been Created.");
        } else {
            sender.sendMessage(ChatColor.BLUE + "World \"" + worldName + "\" already exists. Aborted!!!");
        }
    }

    private boolean checkInputIsInteger(String s) {
        return false;
    }

}
