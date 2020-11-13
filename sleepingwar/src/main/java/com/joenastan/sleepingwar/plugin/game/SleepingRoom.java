package com.joenastan.sleepingwar.plugin.game;

import com.joenastan.sleepingwar.plugin.events.CustomEvents.BedwarsGameEndedEvent;
import com.joenastan.sleepingwar.plugin.events.Tasks.DeleteWorldDelayed;
import com.joenastan.sleepingwar.plugin.SleepingWarsPlugin;
import com.joenastan.sleepingwar.plugin.utility.GameSystemConfig;
import com.joenastan.sleepingwar.plugin.utility.PlayerBedwarsEntity;
import com.joenastan.sleepingwar.plugin.utility.Timer.StopwatchTimer;
import com.joenastan.sleepingwar.plugin.utility.Timer.TimelineTimer;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.io.File;
import java.util.*;

public class SleepingRoom {

    private class RoomUpdater {
        
        private int taskID;

        public RoomUpdater() {
            taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){
                @Override
                public void run() {
                    updateScoreBoard();
                }
            }, 0L, 10L);
        }

        public void destroyUpdater() {
            Bukkit.getScheduler().cancelTask(taskID);
        }

    }

    // Constants
    private final JavaPlugin plugin = SleepingWarsPlugin.getPlugin();
    private final GameSystemConfig systemConfig = SleepingWarsPlugin.getGameSystemConfig();

    // Attributes
    private Player hostedBy;
    private boolean isResSpawn = false;
    private int timelineIndex;
    private float maxGameDuration;
    private World gameWorld;
    private Location worldQueueSpawn;
    private String worldOriginalName;
    private StopwatchTimer timer;
    private boolean isInGameOn = false;

    // Maps and Lists
    private Map<String, Location> teamSpawnerList = new HashMap<String, Location>();
    private Map<Player, PlayerBedwarsEntity> bedwarsSpecialEntities = new HashMap<Player, PlayerBedwarsEntity>();
    private Map<String, TeamGroupMaker> teams = new HashMap<String, TeamGroupMaker>();
    private List<ResourceSpawner> publicResourceSpawners = new ArrayList<ResourceSpawner>();
    private List<Block> putedBlock = new ArrayList<Block>();
    private List<TimelineTimer> inGameEvents = new ArrayList<TimelineTimer>();

    // Scoreboard
    private RoomUpdater updater;
    private Scoreboard localScoreBoard;
    private Objective objectiveLocalSB;

    public SleepingRoom(String worldOriginalName, Player host, World bedwarsWorld, Location queueSpawn, long maxGameDuration) {
        // Add host to bedwars entity list
        bedwarsSpecialEntities.put(host, new PlayerBedwarsEntity(host, host.getLocation(), host.getGameMode()));
        Location locSpawn = new Location(bedwarsWorld, queueSpawn.getX(), queueSpawn.getY(), queueSpawn.getZ());
        host.teleport(locSpawn);
        host.setGameMode(GameMode.SPECTATOR);
        
        hostedBy = host;
        gameWorld = bedwarsWorld;

        this.worldQueueSpawn = locSpawn;
        this.maxGameDuration = maxGameDuration;
        this.worldOriginalName = worldOriginalName;

        localScoreBoard = Bukkit.getScoreboardManager().getNewScoreboard();
        objectiveLocalSB = localScoreBoard.registerNewObjective("queue", "counter", ChatColor.YELLOW + "Queueing");
        objectiveLocalSB.setDisplaySlot(DisplaySlot.SIDEBAR);
        timer = new StopwatchTimer(maxGameDuration);
        updater = new RoomUpdater();

        //System.out.println("[DEBUG] " + systemConfig.getTeamSpawner(worldOriginalName) == null);
        teamSpawnerList.putAll(systemConfig.getTeamSpawner(worldOriginalName));
        for (ResourceSpawner rsp : systemConfig.getResourceSpawnersPack(worldOriginalName, "PUBLIC").values()) {
            Location spawnResLoc = new Location(bedwarsWorld, rsp.getSpawnLocation().getX(), rsp.getSpawnLocation().getY(), rsp.getSpawnLocation().getZ());
            publicResourceSpawners.add(new ResourceSpawner(rsp.getCodename(), spawnResLoc, rsp.getTypeResourceSpawner()));
        }
        hostedBy.sendMessage(ChatColor.GOLD + "Room Created, World name to enter the game: " + ChatColor.GREEN + bedwarsWorld.getName());
    }

    public void playerEnter(Player player) {
        bedwarsSpecialEntities.put(player, new PlayerBedwarsEntity(player, player.getLocation(), player.getGameMode()));
        player.teleport(worldQueueSpawn);
        for (Player pplInRoom : bedwarsSpecialEntities.keySet()) {
            pplInRoom.sendMessage(ChatColor.GOLD + player.getName() + " joined the party, there are " + bedwarsSpecialEntities.size() + " in room.");
        }
        player.setGameMode(GameMode.SPECTATOR);
    }

    public void playerLeave(Player player) {
        // player leave
        for (Map.Entry<Player, PlayerBedwarsEntity> tf: bedwarsSpecialEntities.entrySet()) {
            if (tf.getKey().equals(player)) {
                bedwarsSpecialEntities.remove(tf.getKey()).returnEntity();
                player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                break;
            }
        }

        // If nobody inside the room then destroy it
        if (bedwarsSpecialEntities.size() == 0 || gameWorld.getPlayerCount() == 0) {
            destroyRoom();
        } else {
            // Announcement
            for (Player p : bedwarsSpecialEntities.keySet()) {
                p.sendMessage(ChatColor.YELLOW + player.getName() + " left the party, there are " + bedwarsSpecialEntities.size() + " in room.");
            }

            if (player.equals(hostedBy)) {
                List<Player> plys = new ArrayList<Player>();
                plys.addAll(bedwarsSpecialEntities.keySet());
                hostedBy = plys.get(0);

                for (Player p : bedwarsSpecialEntities.keySet()) {
                    p.sendMessage(ChatColor.YELLOW + hostedBy.getName() + " is now the host.");
                }
            }
        }
    }

    public void gameStart() {
        // Announcement
        for (Player p : bedwarsSpecialEntities.keySet()) {
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
        isResourceSpawning(true);
        isInGameOn = true;
        localScoreBoard.resetScores("Player Count");
        objectiveLocalSB.setDisplayName(ChatColor.GRAY + "Team List");
        timer.start();
        checkRemainingTeam();
    }

    public void destroyRoom() {
        timer.stop();
        updater.destroyUpdater();

        // Teleport Players back to where they were
        for (Map.Entry<Player, PlayerBedwarsEntity> ppl : bedwarsSpecialEntities.entrySet()) {
            Player p = ppl.getKey();
            ppl.getValue().returnEntity();
            p.sendMessage(ChatColor.YELLOW + "Game Ended, teleporting back to where you were.");
            p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }

        // Stop all resources spawn schedules
        isResourceSpawning(false);

        File worldDir = gameWorld.getWorldFolder();
        GameManager.allLeave(bedwarsSpecialEntities.keySet());
        GameManager.getAllRoom().remove(gameWorld.getName());
        Bukkit.unloadWorld(gameWorld, false);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(SleepingWarsPlugin.getPlugin(),
                new DeleteWorldDelayed(worldDir), 60L);
    }

    public void roomBroadcast(String message) {
        for (Map.Entry<Player, PlayerBedwarsEntity> ppl : bedwarsSpecialEntities.entrySet()) {
            ppl.getKey().sendMessage(message);
        }
    }

    public void checkRemainingTeam() {
        TeamGroupMaker lastStanding = null;
        int remainingCount = 0;
        for (TeamGroupMaker team : teams.values()) {
            if (!team.isAllMemberElimineted()) {
                remainingCount++;
                if (lastStanding == null)
                    lastStanding = team;
            }
        }

        // if last one standing then game ended
        if (remainingCount <= 1 && lastStanding != null) {
            BedwarsGameEndedEvent gameEndedEvent = new BedwarsGameEndedEvent(this);
            Bukkit.getPluginManager().callEvent(gameEndedEvent);
            roomBroadcast(lastStanding.getName() + ChatColor.GOLD + " team WIN the game!");
            Bukkit.getScheduler().scheduleSyncDelayedTask(SleepingWarsPlugin.getPlugin(), new Runnable(){
                @Override
                public void run() {
                    destroyRoom();
                }
            }, 200L);
        }
    }

    private void createTeam() {
        Map<String, String> teamPrefix = systemConfig.getTeamPrefix(worldOriginalName);
        List<Player> mapSetPlayer = new ArrayList<Player>();
        mapSetPlayer.addAll(bedwarsSpecialEntities.keySet());
        List<List<Player>> teamUp = new ArrayList<List<Player>>();
        int sizeMap = bedwarsSpecialEntities.size() / teamPrefix.size() + 1;
        Random rand = new Random();

        // Seperate with empty list of players
        for (int j = 0; j < teamPrefix.size(); j++) {
            teamUp.add(new ArrayList<Player>());
        }

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

        for (int i = teamPrefix.size() - 1; i >= 0; i--) {
            if (teamUp.get(i).size() < 1)
                teamUp.remove(i);
        }

        for (Map.Entry<String, String> tm : teamPrefix.entrySet()) {
            if (teamUp.size() < 1)
                break;

            String teamName = tm.getKey();
            TeamGroupMaker team = new TeamGroupMaker(this, teamName, worldOriginalName, teamUp.remove(0),
                    teamSpawnerList.get(teamName), systemConfig.getTeamBedLocation(worldOriginalName, teamName), tm.getValue());
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
        for (Player p : bedwarsSpecialEntities.keySet()) {
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

    public List<TeamGroupMaker> getAllTeams() {
        List<TeamGroupMaker> listTeam = new ArrayList<TeamGroupMaker>();
        listTeam.addAll(teams.values());
        return listTeam;
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
        ppl.addAll(bedwarsSpecialEntities.keySet());
        return ppl;
    }

    public boolean isPlayerHost(Player player) {
        if (hostedBy.equals(player))
            return true;
        return false;
    }

    public void updateScoreBoard() {
        for (Player player : bedwarsSpecialEntities.keySet()) {
            if (isInGameOn) {
                for (Map.Entry<String, TeamGroupMaker> t : teams.entrySet()) {
                    Score sc = objectiveLocalSB.getScore(t.getValue().getName());
                    sc.setScore(t.getValue().getRemainingPlayers());
                }
            } else {
                Score sc = objectiveLocalSB.getScore(ChatColor.GREEN + "Player Count");
                sc.setScore(bedwarsSpecialEntities.size());
            }

            player.setScoreboard(localScoreBoard);
        }
    }

    public void timelineUpdate() {
        // Go to next update
        timelineIndex++;
        if (timelineIndex < inGameEvents.size()) {
            inGameEvents.get(timelineIndex);
        }
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

    public Location getWorldQueueSpawn() {
        return worldQueueSpawn;
    }

    public List<ResourceSpawner> getResourcesSpawner() {
        return publicResourceSpawners;
    }

}
