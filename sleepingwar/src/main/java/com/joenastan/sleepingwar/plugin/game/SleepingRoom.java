package com.joenastan.sleepingwar.plugin.game;

import com.joenastan.sleepingwar.plugin.SleepingWarsPlugin;
import com.joenastan.sleepingwar.plugin.events.CustomEvents.BedwarsGameEndedEvent;
import com.joenastan.sleepingwar.plugin.events.CustomEvents.BedwarsGameTimelineEvent;
import com.joenastan.sleepingwar.plugin.events.Tasks.DeleteWorldDelayed;
import com.joenastan.sleepingwar.plugin.game.CustomDerivedEntity.LockedEntities;
import com.joenastan.sleepingwar.plugin.game.CustomDerivedEntity.LockedResourceSpawner;
import com.joenastan.sleepingwar.plugin.utility.CustomDerivedEntity.PlayerBedwarsEntity;
import com.joenastan.sleepingwar.plugin.utility.GameSystemConfig;
import com.joenastan.sleepingwar.plugin.utility.Timer.AreaEffectTimer;
import com.joenastan.sleepingwar.plugin.utility.Timer.TimelineTimer;
import com.joenastan.sleepingwar.plugin.utility.UsefulStaticFunctions;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;

import javax.annotation.Nullable;
import java.io.File;
import java.util.*;

public class SleepingRoom {

    // Constants
    private final JavaPlugin plugin = SleepingWarsPlugin.getPlugin();
    private final GameSystemConfig systemConfig = SleepingWarsPlugin.getGameSystemConfig();
    private final GameManager gameManager = SleepingWarsPlugin.getGameManager();
    // Attributes
    private Player hostedBy;
    private boolean isResSpawn = false;
    private World gameWorld;
    private Location worldQueueSpawn;
    private String mapName;
    private boolean isInGameOn = false;
    private TimelineTimer currentlyRunningTimer = null;
    // Maps and Lists
    private Map<String, TeamGroupMaker> createdTeams = new HashMap<String, TeamGroupMaker>();
    private List<PlayerBedwarsEntity> playerEntities = new ArrayList<PlayerBedwarsEntity>();
    private List<ResourceSpawner> publicRSpawners = new ArrayList<ResourceSpawner>();
    private List<Block> putedBlock = new ArrayList<Block>();
    private List<TimelineTimer> inGameEvents = new ArrayList<TimelineTimer>();
    private List<AreaEffectTimer> publicBufferZone = new ArrayList<AreaEffectTimer>();
    private List<LockedEntities> eLockedList = new ArrayList<LockedEntities>();
    // Scoreboard
    private RoomUpdater updater;
    private Scoreboard localScoreBoard;
    private Objective objectiveLocalSB;
    private Objective playerHealthDisplayer;
    /**
     * Initialize bedwars room
     *
     * @param mapName      Original map name
     * @param host         Hosted by
     * @param bedwarsWorld Copied world for game
     */
    public SleepingRoom(String mapName, Player host, World bedwarsWorld, @Nullable PlayerBedwarsEntity hostEnt) {
        // Configure host
        Location locSpawn = systemConfig.getQueueLocations(bedwarsWorld, mapName);
        host.setGameMode(GameMode.SURVIVAL);
        // Assign Attributes
        hostedBy = host;
        gameWorld = bedwarsWorld;
        gameWorld.setDifficulty(Difficulty.NORMAL);
        this.worldQueueSpawn = locSpawn;
        this.mapName = mapName;
        // Create scoreboard
        localScoreBoard = Bukkit.getScoreboardManager().getNewScoreboard();
        objectiveLocalSB = localScoreBoard.registerNewObjective("queue", "counter", ChatColor.YELLOW + "Queueing");
        objectiveLocalSB.setDisplaySlot(DisplaySlot.SIDEBAR);
        updater = new RoomUpdater();
        // Get all public resource spawners
        publicRSpawners.addAll(systemConfig.getWorldResourceSpawners(bedwarsWorld, mapName, "PUBLIC"));
        hostedBy.sendMessage(ChatColor.GOLD + "Room Created, World name to enter the game: " + ChatColor.GREEN + bedwarsWorld.getName());
        // Register all events in room
        for (BedwarsGameTimelineEvent eventEntry : systemConfig.getTimelineEvents(mapName)) {
            eventEntry.setRoom(this);
            inGameEvents.add(new TimelineTimer(eventEntry.getTriggerSeconds(), this, eventEntry, publicRSpawners));
        }
        eLockedList = systemConfig.getLockedRequestEntity(gameWorld, mapName);
        publicBufferZone = systemConfig.getPublicBZCoroutines(gameWorld, mapName);
        for (ResourceSpawner rsp : publicRSpawners) {
            LockedResourceSpawner lockedRS = systemConfig.getLockedRSEntity(gameWorld, mapName, rsp.getCoroutine());
            if (lockedRS != null) {
                eLockedList.add(lockedRS);
                rsp.getCoroutine().setLocked(true);
            }
        }
        // Host entered the room
        playerEnter(host, hostEnt);
    }

    /**
     * Begin the bedwars.
     */
    public void gameStart() {
        if (playerEntities.size() <= 1) {
            roomBroadcast(ChatColor.BLUE + "You can't play alone, you need at least 2 person in room.");
            return;
        }
        // Initialize game
        // Creating teams
        createTeam();
        // Activate all resource spawner
        for (ResourceSpawner rSpawn : publicRSpawners)
            rSpawn.isRunning(true);
        // Scheduling the game ended
        isResourceSpawning(true);
        checkRemainingTeam();
        currentlyRunningTimer = inGameEvents.get(0);
        currentlyRunningTimer.start();
        isInGameOn = true;
        hostedBy = null;
        // Activate all buffer zones
        for (AreaEffectTimer bzt : publicBufferZone)
            bzt.start();
        // Init Scoreboard
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                localScoreBoard.resetScores(String.format("%sPlayer Count: ", ChatColor.GREEN + ""));
            }
        }, 20L);
        objectiveLocalSB.setDisplayName(ChatColor.WHITE + "Bedwars");
        objectiveLocalSB.setDisplaySlot(DisplaySlot.SIDEBAR);
        playerHealthDisplayer = localScoreBoard.registerNewObjective("player-health", "health", " HP");
        playerHealthDisplayer.setDisplaySlot(DisplaySlot.BELOW_NAME);
    }

    /**
     * Destroy room when the game ended or nobody in the room
     */
    public void destroyRoom() {
        // Teleport Players back to where they were
        for (PlayerBedwarsEntity peEntity : playerEntities) {
            try {
                Player ePlayer = peEntity.getPlayer();
                ePlayer.getInventory().clear();
                peEntity.returnEntity();
                ePlayer.sendMessage(ChatColor.YELLOW + "Game Ended, teleporting back to where you were.");
                ePlayer.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            } catch (Exception exc) {
                System.out.println("[SleepingWars] Player may not exists when trying to return the player to where it was. " +
                        "You can ignore this error message.");
                System.out.println(exc.getCause());
            }
        }
        // Clear all resource spawners if the game is still on
        if (isInGameOn)
            for (TeamGroupMaker t : createdTeams.values())
                t.activateRS(false);
        for (ResourceSpawner rsp : publicRSpawners)
            if (rsp.isRunning())
                rsp.isRunning(false);
        // Stop timeline and all schedules
        updater.destroyUpdater();
        isResourceSpawning(false);
        // Make sure every timeline event is stopped
        for (TimelineTimer tm : inGameEvents)
            tm.stop();
        for (TeamGroupMaker tm : createdTeams.values())
            tm.setRunningEffectBZ(false);
        // Stop all running effects in all buffer zone
        for (AreaEffectTimer bzt : publicBufferZone)
            bzt.stop();
        for (TeamGroupMaker tMaker : createdTeams.values())
            tMaker.setRunningEffectBZ(false);
        // Clear lists and maps
        publicRSpawners.clear();
        playerEntities.clear();
        putedBlock.clear();
        inGameEvents.clear();
        publicBufferZone.clear();
        createdTeams.clear();
        eLockedList.clear();
        // Unregister room in game manager and delete world file
        gameManager.getRoomMap().remove(gameWorld.getName());
        File worldDir = gameWorld.getWorldFolder();
        Bukkit.unloadWorld(gameWorld, false);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new DeleteWorldDelayed(worldDir), 60L);
    }

    /**
     * Player enter the room with this function.
     *
     * @param player Player who enters
     * @param entity Entity data which player owned
     */
    public void playerEnter(Player player, @Nullable PlayerBedwarsEntity entity) {
        // Insert player bedwars entity data
        if (entity != null)
            playerEntities.add(entity);
        else
            playerEntities.add(new PlayerBedwarsEntity(player, player.getLocation(), player.getGameMode()));
        // Check if the game is ongoing
        if (!isInGameOn) {
            // Teleport to queue spawner
            player.teleport(worldQueueSpawn);
            roomBroadcast(String.format("%s joined the game. [%d player(s) in room]", ChatColor.GOLD + player.getName(), playerEntities.size()));
            player.sendMessage(ChatColor.YELLOW + "This room is hosted by " + ChatColor.GREEN + hostedBy.getName());
            player.setGameMode(GameMode.SURVIVAL);
        } else {
            TeamGroupMaker team = findTeam(player);
            if (team != null) {
                if (team.playerReconnectedHandler(player))
                    return;
            }
            player.teleport(worldQueueSpawn);
            player.setGameMode(GameMode.SPECTATOR);
        }
    }

    /**
     * Player leave the room
     *
     * @param player Refered player
     * @return Bedwars entity data of player
     */
    public PlayerBedwarsEntity playerLeave(Player player) {
        PlayerBedwarsEntity pbent = null;
        for (int i = 0; i < playerEntities.size(); i++) {
            if (playerEntities.get(i).getPlayer().equals(player)) {
                pbent = playerEntities.remove(i);
                break;
            }
        }
        // Destruct a player from team and game if it is exists
        if (pbent != null) {
            Player p = pbent.getPlayer();
            // Set Empty Scoreboard
            p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            // Check if game in progress
            if (!isInGameOn) {
                roomBroadcast(String.format("%s left the game. [%d player(s) in room]", ChatColor.YELLOW + player.getName(),
                        playerEntities.size()));
                // Change host if the host leave the room
                if (hostedBy.equals(player) && playerEntities.size() > 0) {
                    hostedBy = playerEntities.get(0).getPlayer();
                    roomBroadcast(ChatColor.LIGHT_PURPLE + hostedBy.getName() + ChatColor.AQUA + " is now the room host.");
                }
                pbent.returnEntity();
            } else {
                TeamGroupMaker team = createdTeams.get(pbent.getTeamChoice());
                team.playerDisconnectedHandler(pbent);
            }
        }
        // Check if world or game is empty
        if (playerEntities.size() == 0) {
            if (gameWorld.getPlayerCount() > 0) {
                for (Player anonymous : gameWorld.getPlayers())
                    anonymous.kickPlayer(ChatColor.AQUA + "World is being destroy, please reconnect after a few moment.");
            }
            destroyRoom();
        }
        if (gameWorld.getPlayerCount() == 0)
            destroyRoom();
        return pbent;
    }

    /**
     * Create teams from list of players randomly.
     */
    private void createTeam() {
        List<String> teamList = systemConfig.getTeamNames(mapName);
        List<PlayerBedwarsEntity> mapSetPlayer = new ArrayList<PlayerBedwarsEntity>(playerEntities);
        List<List<PlayerBedwarsEntity>> teamUp = new ArrayList<List<PlayerBedwarsEntity>>();
        int sizeMap = playerEntities.size() / teamList.size() + 1;
        Random rand = new Random();
        // Seperate with empty list of players
        for (int j = 0; j < teamList.size(); j++)
            teamUp.add(new ArrayList<PlayerBedwarsEntity>());
        // Randomize player into the empty list
        for (int i = 0; i < sizeMap; i++) {
            for (int j = 0; j < teamList.size(); j++) {
                if (mapSetPlayer.size() < 1)
                    break;
                int indexPicked = rand.nextInt(mapSetPlayer.size());
                PlayerBedwarsEntity playerPicked = mapSetPlayer.remove(indexPicked);
                teamUp.get(j).add(playerPicked);
            }
        }
        // Remove empty team list
        for (int i = teamList.size() - 1; i >= 0; i--) {
            if (teamUp.get(i).size() < 1)
                teamUp.remove(i);
        }
        // Create team
        for (String teamName : teamList) {
            if (teamUp.size() < 1)
                break;
            TeamGroupMaker team = new TeamGroupMaker(this, teamName, teamUp.remove(0));
            Team tOnScoreboard = localScoreBoard.registerNewTeam(teamName);
            tOnScoreboard.setPrefix(UsefulStaticFunctions.getColorString(team.getTeamColorPrefix()) + team.getName() + ChatColor.WHITE + " ");
            tOnScoreboard.setCanSeeFriendlyInvisibles(false);
            tOnScoreboard.setAllowFriendlyFire(false);
            for (PlayerBedwarsEntity pbent : team.getPlayerEntities()) {
                tOnScoreboard.addEntry(pbent.getPlayer().getName());
                pbent.setTeamChoice(teamName);
            }
            createdTeams.put(teamName, team);
        }
    }

    /**
     * Update each player scoreboard.
     */
    public void updateScoreBoard() {
        for (PlayerBedwarsEntity bent : playerEntities) {
            Player player = bent.getPlayer();
            if (Bukkit.getPlayer(player.getName()) == null)
                continue;
            // Check if the game is in progress
            if (isInGameOn) {
                // Next Event, timer currently running
                // Row start from up to bottom
                // Show up team remaining survivor
                Score scevent, eventTitle, emptyLine0, emptyLine1;
                int scoreLineCount = createdTeams.size() + 4; // Size of team added with 4 lines of score
                // Add Empty Line
                emptyLine0 = objectiveLocalSB.getScore(ChatColor.RESET.toString());
                emptyLine0.setScore(scoreLineCount);
                scoreLineCount--;
                // Add teams on line
                for (TeamGroupMaker t : createdTeams.values()) {
                    String teamFormat = String.format("%s%d %s", UsefulStaticFunctions.getColorString(t.getTeamColorPrefix()),
                            t.getRemainingPlayers(), t.getName());
                    Score scteam = objectiveLocalSB.getScore(teamFormat);
                    scteam.setScore(scoreLineCount);
                    scoreLineCount--;
                }
                // Add another empty line
                emptyLine1 = objectiveLocalSB.getScore(ChatColor.RESET.toString() + ChatColor.RESET.toString());
                emptyLine1.setScore(scoreLineCount);
                scoreLineCount--;
                // Events line
                if (currentlyRunningTimer != null) {
                    // Time display
                    float rawSeconds = currentlyRunningTimer.getCounter();
                    int minutes = (int) rawSeconds / 60;
                    int prevMin = ((int) rawSeconds + 1) / 60;
                    int seconds = (int) rawSeconds % 60;
                    int prevSec = ((int) rawSeconds + 1) % 60;
                    String timeString = seconds < 10 ? String.format("%d:0%d", minutes, seconds) : String.format("%d:%d", minutes, seconds);
                    String prevTimeString = prevSec < 10 ? String.format("%d:0%d", prevMin, prevSec) : String.format("%d:%d", prevMin, prevSec);
                    String formatedTimeString = String.format("%s   Next Event in [%s] ", ChatColor.ITALIC + "", timeString);
                    String prevFormatedTS = String.format("%s   Next Event in [%s] ", ChatColor.ITALIC + "", prevTimeString);
                    localScoreBoard.resetScores(prevFormatedTS);
                    eventTitle = objectiveLocalSB.getScore(formatedTimeString);
                    // Event name display
                    scevent = objectiveLocalSB.getScore(ChatColor.GRAY + currentlyRunningTimer.getBedwarsEventName());
                } else {
                    eventTitle = objectiveLocalSB.getScore(ChatColor.ITALIC + "   Next Event in [0:00] ");
                    scevent = objectiveLocalSB.getScore(ChatColor.GRAY + "[No Upcoming Events]");
                }
                eventTitle.setScore(scoreLineCount);
                scoreLineCount--;
                scevent.setScore(scoreLineCount);
                scoreLineCount--;
            } else {
                String formatPlayerCount = String.format("%sPlayer Count: ", ChatColor.GREEN + "");
                Score sc = objectiveLocalSB.getScore(formatPlayerCount);
                sc.setScore(playerEntities.size());
            }
            player.setHealth(player.getHealth());
            player.setScoreboard(localScoreBoard);
        }
    }

    /**
     * Check player interaction with some blocks, this function only checks whether the block is being locked or not.
     *
     * @param player    Player who interact with the block
     * @param withBlock This block
     * @return True if player able to interact with it, else then false
     */
    public boolean checkInteraction(Player player, Block withBlock) {
        // Check if player entity is exists here
        PlayerBedwarsEntity playerEnt = findPlayerEntityInRoom(player);
        if (playerEnt == null)
            return false;
        // Check location, each will check the material
        Location blockLocation = withBlock.getLocation();
        Material mat = withBlock.getType();
        for (int i = 0; i < eLockedList.size(); i++) {
            LockedEntities entLock = eLockedList.get(i);
            Location lockedLoc = entLock.getLockedLocation();
            Material matOnLockedLoc = lockedLoc.getBlock().getType();
            // Check if it is a normal or iron door, check it's above and below that block
            if (UsefulStaticFunctions.isStandardDoor(mat) && UsefulStaticFunctions.isStandardDoor(matOnLockedLoc)) {
                Block doorBlock = lockedLoc.getBlock();
                // Check immediately same location with same block
                if (lockedLoc.getBlockX() == blockLocation.getBlockX() && lockedLoc.getBlockY() == blockLocation.getBlockY() &&
                        lockedLoc.getBlockZ() == blockLocation.getBlockZ()) {
                    if (!entLock.unlockEntity(playerEnt)) {
                        player.sendMessage(ChatColor.RED + "You can't open this.");
                        return false;
                    } else {
                        eLockedList.remove(i);
                        player.sendMessage(ChatColor.GREEN + "Access granted!");
                        return true;
                    }
                }
                Block upperRelative = doorBlock.getRelative(BlockFace.UP, 1), lowerRelative = doorBlock.getRelative(BlockFace.DOWN, 1);
                // Check if above or below its location is a part of door
                if (UsefulStaticFunctions.isStandardDoor(upperRelative.getType())) {
                    if (upperRelative.getLocation().getBlockX() == blockLocation.getBlockX() && upperRelative.getLocation().getBlockY() ==
                            blockLocation.getBlockY() && upperRelative.getLocation().getBlockZ() == blockLocation.getBlockZ()) {
                        if (!entLock.unlockEntity(playerEnt)) {
                            player.sendMessage(ChatColor.RED + "You can't open this.");
                            return false;
                        } else {
                            eLockedList.remove(i);
                            player.sendMessage(ChatColor.GREEN + "Access granted!");
                            return true;
                        }
                    }
                } else {
                    if (lowerRelative.getLocation().getBlockX() == blockLocation.getBlockX() && lowerRelative.getLocation().getBlockY() ==
                            blockLocation.getBlockY() && lowerRelative.getLocation().getBlockZ() == blockLocation.getBlockZ()) {
                        if (!entLock.unlockEntity(playerEnt)) {
                            player.sendMessage(ChatColor.RED + "You can't open this.");
                            return false;
                        } else {
                            eLockedList.remove(i);
                            player.sendMessage(ChatColor.GREEN + "Access granted!");
                            return true;
                        }
                    }
                }
            } else if (UsefulStaticFunctions.isFenceGate(mat) && UsefulStaticFunctions.isFenceGate(matOnLockedLoc)) {
                if (lockedLoc.getBlockX() == blockLocation.getBlockX() && lockedLoc.getBlockY() == blockLocation.getBlockY() &&
                        lockedLoc.getBlockZ() == blockLocation.getBlockZ()) {
                    if (!entLock.unlockEntity(playerEnt)) {
                        player.sendMessage(ChatColor.RED + "You can't open this.");
                        return false;
                    } else {
                        eLockedList.remove(i);
                        player.sendMessage(ChatColor.GREEN + "Access granted!");
                        return true;
                    }
                }
            } else if (UsefulStaticFunctions.isTrapDoor(mat) && UsefulStaticFunctions.isTrapDoor(matOnLockedLoc)) {
                if (lockedLoc.getBlockX() == blockLocation.getBlockX() && lockedLoc.getBlockY() == blockLocation.getBlockY() &&
                        lockedLoc.getBlockZ() == blockLocation.getBlockZ()) {
                    if (!entLock.unlockEntity(playerEnt)) {
                        player.sendMessage(ChatColor.RED + "You can't open this.");
                        return false;
                    } else {
                        eLockedList.remove(i);
                        player.sendMessage(ChatColor.GREEN + "Access granted!");
                        return true;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Update timeline event to next index.
     */
    public void gotoNextTimelineEvent() {
        // Remove score name from sidebar scoreboard
        localScoreBoard.resetScores(ChatColor.ITALIC + "   Next Event in [0:00] ");
        localScoreBoard.resetScores(ChatColor.GRAY + currentlyRunningTimer.getBedwarsEventName());
        // Go to next update
        int timelineIndex = inGameEvents.indexOf(currentlyRunningTimer);
        if (timelineIndex + 1 < inGameEvents.size()) {
            currentlyRunningTimer = inGameEvents.get(timelineIndex + 1);
            currentlyRunningTimer.start();
        }
    }

    /**
     * Check remaining team survive.
     */
    public void checkRemainingTeam() {
        // Check team last standing
        TeamGroupMaker lastStanding = null;
        int remainingCount = 0;
        for (TeamGroupMaker team : createdTeams.values()) {
            if (!team.isTeamEliminated()) {
                remainingCount++;
                if (lastStanding == null)
                    lastStanding = team;
            }
        }
        // if last one standing then game ended
        if (remainingCount <= 1 && lastStanding != null) {
            BedwarsGameEndedEvent gameEndedEvent = new BedwarsGameEndedEvent(this, lastStanding);
            Bukkit.getPluginManager().callEvent(gameEndedEvent);
            Bukkit.getScheduler().scheduleSyncDelayedTask(SleepingWarsPlugin.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    destroyRoom();
                }
            }, 200L);
        }
    }

    /**
     * When the game is progress, store a block reference which the block has just placed by player.
     *
     * @param block Block reference
     */
    public void putBlock(Block block) {
        putedBlock.add(block);
    }

    /**
     * Find and destroy block that has been placed before.
     *
     * @param block Block reference
     * @return True if the block was in list and removed, else then false
     */
    public boolean destroyBlock(Block block) {
        if (putedBlock.contains(block)) {
            putedBlock.remove(block);
            return true;
        }
        return false;
    }

    /**
     * Send message to each player in room.
     */
    public void roomBroadcast(String message) {
        for (PlayerBedwarsEntity ppl : playerEntities) {
            ppl.getPlayer().sendMessage(message);
        }
    }

    /**
     * Send title message in front of each player's face in room.
     */
    public void roomBroadcastTitle(String title, String subs, int fadeIn, int stayOn, int fadeOut) {
        for (PlayerBedwarsEntity ppl : playerEntities) {
            ppl.getPlayer().sendTitle(title, subs, fadeIn, stayOn, fadeOut);
        }
    }

    /**
     * Get a list of players inside this room. This list is not referenced to any attribute.
     *
     * @return A list of players
     */
    public List<Player> getPlayersInRoom() {
        List<Player> ppl = new ArrayList<Player>();
        for (PlayerBedwarsEntity pbent : playerEntities)
            ppl.add(pbent.getPlayer());
        return ppl;
    }

    /**
     * Get map name
     */
    public String getMapName() {
        return mapName;
    }

    /**
     * Get all teams in current game.
     *
     * @return List of teams
     */
    public List<TeamGroupMaker> getTeams() {
        List<TeamGroupMaker> teams = new ArrayList<TeamGroupMaker>();
        teams.addAll(createdTeams.values());
        return teams;
    }

    /**
     * Get world in current room, not a map world.
     */
    public World getWorld() {
        return gameWorld;
    }

    /**
     * Game is currently hosted by this player.
     *
     * @return Current host, if the game is ongoing then it is null
     */
    public Player getHost() {
        return hostedBy;
    }

    /**
     * Get world queue location in current room.
     *
     * @return Queue location
     */
    public Location getQueueLocation() {
        return worldQueueSpawn;
    }

    /**
     * Get player entity in room
     *
     * @param player Find this player
     * @return Entity, if not exists then returns null
     */
    public PlayerBedwarsEntity getPlayerEntity(Player player) {
        for (PlayerBedwarsEntity pBedwarsEntity : playerEntities) {
            if (pBedwarsEntity.getPlayer().equals(player))
                return pBedwarsEntity;
        }
        return null;
    }

    /**
     * Get scoreboard reference.
     */
    public Scoreboard getScoreboard() {
        return localScoreBoard;
    }

    /**
     * Find team by player.
     *
     * @return Team, if not found then it returns null
     */
    public TeamGroupMaker findTeam(Player player) {
        if (player == null)
            return null;

        for (TeamGroupMaker team : createdTeams.values()) {
            for (PlayerBedwarsEntity pbent : team.getPlayerEntities()) {
                // Find player if it is equal or its name are identical
                if (pbent.getPlayer() != null) {
                    if (pbent.getPlayer().equals(player) || pbent.getPlayerName().equals(player.getName()))
                        return team;
                }
            }
        }
        return null;
    }

    /**
     * Find team by team name.
     *
     * @return Team, if not found then it returns null
     */
    public TeamGroupMaker findTeam(String teamName) {
        return createdTeams.get(teamName);
    }

    /**
     * Get player entity in room.
     *
     * @param player Player to find.
     * @return Player entity, if not found then it returns null
     */
    public PlayerBedwarsEntity findPlayerEntityInRoom(Player player) {
        for (PlayerBedwarsEntity pBedwarsEntity : playerEntities) {
            if (pBedwarsEntity.getPlayer().equals(player))
                return pBedwarsEntity;
        }
        return null;
    }

    /**
     * Check if bedwars currently ongoing.
     *
     * @return True if it is ongoing, else then false
     */
    public boolean isGameProcessing() {
        return isInGameOn;
    }

    /**
     * Check if resource spawners in room active.
     *
     * @return True if resource spawners are active, else then false
     */
    public boolean isResourceSpawning() {
        return isResSpawn;
    }

    /**
     * Check if resource spawners in room active.
     *
     * @param active Set active
     * @return True if resource spawners are active, else then false
     */
    public boolean isResourceSpawning(boolean active) {
        if (isResSpawn != active) {
            isResSpawn = active;
            // Running public resource spawner
            for (ResourceSpawner spawner : publicRSpawners) {
                spawner.isRunning(isResSpawn);
            }
            // Running all team resource spawner
            for (TeamGroupMaker t : createdTeams.values()) {
                t.activateRS(isResSpawn);
            }
        }
        return isResSpawn;
    }

    private class RoomUpdater {

        private int taskID;

        public RoomUpdater() {
            taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
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
}
