package com.joenastan.sleepingwar.plugin.Game;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.joenastan.sleepingwar.plugin.SleepingWarsPlugin;
import com.joenastan.sleepingwar.plugin.Events.Tasks.DeleteWorldDelayed;
import com.joenastan.sleepingwar.plugin.Utility.GameSystemConfig;
import com.joenastan.sleepingwar.plugin.Utility.StopwatchTimer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class SleepingRoom {
    
    private final JavaPlugin plugin = SleepingWarsPlugin.getPlugin();
    private final GameSystemConfig systemConfig = SleepingWarsPlugin.getGameSystemConfig();
    private Player hostedBy;
    private Map<Player, Location> tpFrom = new HashMap<Player, Location>();
    private Map<String, TeamGroupMaker> teams = new HashMap<String, TeamGroupMaker>();
    private Map<String, Location> teamSpawnerList;
    private List<ResourceSpawner> publicResourceSpawners = new ArrayList<ResourceSpawner>();
    private float maxGameDuration;
    private World gameWorld;
    private Location queueSpawn;
    private String worldOriginalName;
    private StopwatchTimer timer;

    public SleepingRoom(String worldOriginalName, Player host, World bedwarsWorld, Location queueSpawn, long maxGameDuration) {
        hostedBy = host;
        tpFrom.put(host, host.getLocation());
        gameWorld = bedwarsWorld;
        this.queueSpawn = queueSpawn;
        this.maxGameDuration = maxGameDuration;
        this.worldOriginalName = worldOriginalName;
        teamSpawnerList = systemConfig.getTeamSpawner(worldOriginalName);
        publicResourceSpawners.addAll(systemConfig.getResourceSpawnersPack(worldOriginalName, "PUBLIC").values());

        timer = new StopwatchTimer(maxGameDuration);
    }
    
    public void playerEnter(Player player) {
        tpFrom.put(player, player.getLocation());
        player.teleport(queueSpawn);
    }

    public void playerLeave(Player player) {
        for (Map.Entry<Player, Location> tf : tpFrom.entrySet()) {
            if (tf.getKey().equals(player)) {
                player.teleport(tpFrom.remove(tf.getKey()));
                break;
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
        // Scheduling the game ended
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
        for (ResourceSpawner spawnerRes : publicResourceSpawners) {
            spawnerRes.isRunning(false);
        }

        File worldDir = gameWorld.getWorldFolder();
        Bukkit.unloadWorld(gameWorld, false);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(SleepingWarsPlugin.getPlugin(), 
            new DeleteWorldDelayed(worldDir), 60L);
    }

    private void createTeam() {
        GameSystemConfig systemConf = SleepingWarsPlugin.getGameSystemConfig();
        Map<String, String> teamPrefix = systemConf.getTeamPrefix(gameWorld.getName());
        List<Player> mapSetPlayer = new ArrayList<Player>();
        mapSetPlayer.addAll(tpFrom.keySet());
        List<List<Player>> teamUp = new ArrayList<List<Player>>();
        int sizeMap = tpFrom.size() / teamPrefix.size() + 1;
        Random rand = new Random();

        // Seperate with empty list of players
        for (int j = 0; j < teamPrefix.size(); j++) {
            teamUp.add(new ArrayList<Player>());
        }

        // Randomize player into the empty list
        for (int i = 0; i < sizeMap; i++) {
            for (int j = 0; j < teamPrefix.size(); j++) {
                if (tpFrom.size() < 1)
                    break;

                int indexPicked = rand.ints(0, mapSetPlayer.size()).findFirst().getAsInt();
                Player playerPicked = mapSetPlayer.remove(indexPicked);
                teamUp.get(j).add(playerPicked);
            }
        }

        for (Map.Entry<String, String> tm : teamPrefix.entrySet()) {
            String teamName = tm.getKey();
            TeamGroupMaker team = new TeamGroupMaker(teamName, worldOriginalName, teamUp.remove(0), 
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

    public void setGameDuration(int dur) {
        maxGameDuration = dur;
    }

    public float getGameDuration() {
        return maxGameDuration;
    }

    public Location getQueueSpawn() {
        return queueSpawn;
    }

    public List<ResourceSpawner> getResourcesSpawner() {
        return publicResourceSpawners;
    }

}
