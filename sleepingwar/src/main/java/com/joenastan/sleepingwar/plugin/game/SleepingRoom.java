package com.joenastan.sleepingwar.plugin.game;

import com.joenastan.sleepingwar.plugin.events.Tasks.DeleteWorldDelayed;
import com.joenastan.sleepingwar.plugin.SleepingWarsPlugin;
import com.joenastan.sleepingwar.plugin.utility.GameSystemConfig;
import com.joenastan.sleepingwar.plugin.utility.Timer.StopwatchTimer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class SleepingRoom {

    private final JavaPlugin plugin = SleepingWarsPlugin.getPlugin();
    private final GameSystemConfig systemConfig = SleepingWarsPlugin.getGameSystemConfig();
    private Player hostedBy;
    private Map<Player, Location> tpFrom = new HashMap<Player, Location>();
    private Map<String, TeamGroupMaker> teams = new HashMap<String, TeamGroupMaker>();
    private Map<String, Location> teamSpawnerList;
    private List<ResourceSpawner> publicResourceSpawners = new ArrayList<ResourceSpawner>();
    private boolean isResSpawn;
    private float maxGameDuration;
    private World gameWorld;
    private Location queueSpawn;
    private String worldOriginalName;
    private StopwatchTimer timer;
    private boolean isInGameOn;
    private List<Block> putedBlock = new ArrayList<Block>();

    public SleepingRoom(String worldOriginalName, Player host, World bedwarsWorld, Location queueSpawn, long maxGameDuration) {
        hostedBy = host;
        tpFrom.put(host, host.getLocation());
        gameWorld = bedwarsWorld;
        isResSpawn = false;
        isInGameOn = false;
        this.queueSpawn = queueSpawn;
        this.maxGameDuration = maxGameDuration;
        this.worldOriginalName = worldOriginalName;
        teamSpawnerList = systemConfig.getTeamSpawner(worldOriginalName);
        publicResourceSpawners.addAll(systemConfig.getResourceSpawnersPack(worldOriginalName, "PUBLIC").values());
        host.sendMessage(ChatColor.GOLD + "Room Created, World name to enter the game: " + ChatColor.GREEN + bedwarsWorld.getName());

        timer = new StopwatchTimer(maxGameDuration);
    }

    public void playerEnter(Player player) {
        tpFrom.put(player, player.getLocation());
        player.teleport(queueSpawn);
        for (Player pplInRoom : tpFrom.keySet()) {
            pplInRoom.sendMessage(ChatColor.GOLD + player.getName() + " joined the party, there are " + tpFrom.size() + " in room.");
        }
    }

    public void playerLeave(Player player) {
        // player leave
        for (Map.Entry<Player, Location> tf : tpFrom.entrySet()) {
            if (tf.getKey().equals(player)) {
                player.teleport(tpFrom.remove(tf.getKey()));
                break;
            }
        }

        // If nobody inside the room then destroy it
        if (tpFrom.size() == 0 || gameWorld.getPlayerCount() == 0) {
            destroyRoom();
        } else {
            // Announcement
            for (Player p : tpFrom.keySet()) {
                p.sendMessage(ChatColor.YELLOW + player.getName() + " left the party, there are " + tpFrom.size() + " in room.");
            }

            if (player.equals(hostedBy)) {
                List<Player> plys = new ArrayList<Player>();
                plys.addAll(tpFrom.keySet());
                hostedBy = plys.get(0);

                for (Player p : tpFrom.keySet()) {
                    p.sendMessage(ChatColor.YELLOW + hostedBy.getName() + " is now the host.");
                }
            }
        }
    }

    public void gameStart() {
        // Announcement
        for (Player p : tpFrom.keySet()) {
            p.sendMessage(ChatColor.LIGHT_PURPLE + "Starting...");
        }
        // Initialize game
        // Creating teams
        createTeam();
        // Activate all resource spawner
        for (ResourceSpawner rSpawn : publicResourceSpawners) {
            rSpawn.isRunning(true);
        }
        System.out.println("[DEBUG] run 4");
        // Scheduling the game ended
        isResourceSpawning(true);
        isInGameOn = true;
        timer.start();
    }

    public void destroyRoom() {
        timer.stop();

        // Teleport Players back to where they were
        for (Map.Entry<Player, Location> ppl : tpFrom.entrySet()) {
            Player p = ppl.getKey();
            p.sendMessage(ChatColor.YELLOW + "Game Ended, teleporting back to where you were.");
            p.teleport(ppl.getValue());
        }

        // Stop all resources spawn schedules
        isResourceSpawning(false);

        File worldDir = gameWorld.getWorldFolder();
        GameManager.allLeave(tpFrom.keySet());
        GameManager.getAllRoom().remove(gameWorld.getName());
        Bukkit.unloadWorld(gameWorld, false);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(SleepingWarsPlugin.getPlugin(),
                new DeleteWorldDelayed(worldDir), 60L);
    }

    public void roomBroadcast(String message) {
        for (Map.Entry<Player, Location> ppl : tpFrom.entrySet()) {
            ppl.getKey().sendMessage(message);
        }
    }

    private void createTeam() {
        Map<String, String> teamPrefix = systemConfig.getTeamPrefix(worldOriginalName);
        List<Player> mapSetPlayer = new ArrayList<Player>();
        mapSetPlayer.addAll(tpFrom.keySet());
        List<List<Player>> teamUp = new ArrayList<List<Player>>();
        int sizeMap = tpFrom.size() / teamPrefix.size() + 1;
        Random rand = new Random();

        // Seperate with empty list of players
        for (int j = 0; j < teamPrefix.size(); j++) {
            teamUp.add(new ArrayList<Player>());
        }

        System.out.println("[DEBUG] run 2");

        // Randomize player into the empty list
        for (int i = 0; i < sizeMap; i++) {
            for (int j = 0; j < teamPrefix.size(); j++) {
                if (mapSetPlayer.size() < 1)
                    break;

                int indexPicked = rand.nextInt(mapSetPlayer.size());
                Player playerPicked = mapSetPlayer.remove(indexPicked);
                teamUp.get(j).add(playerPicked);
            }
        }

        System.out.println("[DEBUG] run 3");

        for (int i = teamPrefix.size() - 1; i >= 0; i--) {
            if (teamUp.get(i).size() < 1)
                teamUp.remove(i);
        }

        for (Map.Entry<String, String> tm : teamPrefix.entrySet()) {
            if (teamUp.size() < 1)
                break;

            String teamName = tm.getKey();
            TeamGroupMaker team = new TeamGroupMaker(this, teamName, worldOriginalName, teamUp.remove(0),
                    teamSpawnerList.get(teamName), tm.getValue());
            teams.put(team.getName(), team);
        }
    }

    public String getOriginalWorldName() {
        return worldOriginalName;
    }

    public TeamGroupMaker getTeam(Player p) {
        for (Map.Entry<String, TeamGroupMaker> group : teams.entrySet()) {
            if (group.getValue().checkPlayer(p)) {
                return group.getValue();
            }
        }
        return null;
    }

    public boolean isPlayerInRoom(Player player) {
        for (Player p : tpFrom.keySet()) {
            if (p.equals(player))
                return true;
        }

        return false;
    }

    public boolean isGameProcessing() {
        return isInGameOn;
    }

    public void putBlock(Block block) {
        putedBlock.add(block);
    }

    public boolean destroyBlock(Block block) {
        if (putedBlock.contains(block)) {
            putedBlock.remove(block);
            return true;
        }

        return false;
    }

    public List<Player> getAllPlayer() {
        List<Player> ppl = new ArrayList<Player>();
        ppl.addAll(tpFrom.keySet());
        return ppl;
    }

    public boolean isPlayerHost(Player player) {
        if (hostedBy.equals(player))
            return true;
        return false;
    }

    public Player getHost() {
        return hostedBy;
    }

    public boolean isResourceSpawning() {
        return isResSpawn;
    }

    public boolean isResourceSpawning(boolean active) {
        isResSpawn = active;

        // Running public resource spawner
        for (ResourceSpawner spawner : publicResourceSpawners) {
            spawner.isRunning(isResSpawn);
        }

        // Running all team resource spawner
        for (TeamGroupMaker resSpawner : teams.values()) {
            for (ResourceSpawner spawner : resSpawner.getAllResourceSpawners()) {
                spawner.isRunning(isResSpawn);
            }
        }

        return isResSpawn;
    }

    public float getGameDuration() {
        return maxGameDuration;
    }

    public void setGameDuration(int dur) {
        maxGameDuration = dur;
    }

    public Location getQueueSpawn() {
        return queueSpawn;
    }

    public List<ResourceSpawner> getResourcesSpawner() {
        return publicResourceSpawners;
    }

}
