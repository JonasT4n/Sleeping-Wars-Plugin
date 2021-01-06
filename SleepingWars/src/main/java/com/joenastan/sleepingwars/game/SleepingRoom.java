package com.joenastan.sleepingwars.game;

import com.joenastan.sleepingwars.events.CustomEvents.BedwarsEndedEvent;
import com.joenastan.sleepingwars.events.CustomEvents.BedwarsLockUnlocked;
import com.joenastan.sleepingwars.events.CustomEvents.BedwarsPlayerEliminatedEvent;
import com.joenastan.sleepingwars.game.CustomEntity.LockedNormalEntity;
import com.joenastan.sleepingwars.tasks.DeleteWorldDelayed;
import com.joenastan.sleepingwars.SleepingWarsPlugin;
import com.joenastan.sleepingwars.utility.DataFiles.GameSystemConfig;
import com.joenastan.sleepingwars.timercoro.AreaEffectTimer;
import com.joenastan.sleepingwars.timercoro.PlayerReviveTimer;
import com.joenastan.sleepingwars.timercoro.TimelineTimer;
import com.joenastan.sleepingwars.utility.PluginStaticColor;
import com.joenastan.sleepingwars.utility.PluginStaticFunc;
import com.joenastan.sleepingwars.utility.CustomDerivedEntity.PlayerBedwarsEntity;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.*;

public class SleepingRoom {

    // Constants References
    private final JavaPlugin plugin = SleepingWarsPlugin.getPlugin();
    private final GameManager gameManager = SleepingWarsPlugin.getGameManager();
    // Attributes
    private final World gameWorld;
    private final Location worldQueueSpawn;
    private final String mapName;
    private final float respawnTime;
    private final TimelineTimer inGameEvent;
    private Player hostedBy;
    private boolean isResSpawn = false;
    private boolean isInGameOn = false;
    private boolean isGEnded = false;
    private boolean isGStarting = false;
    // Maps and Lists
    private final Map<String, TeamGroupMaker> createdTeams = new HashMap<>();
    private final Map<UUID, PlayerBedwarsEntity> playerEntities = new HashMap<>();
    private final Map<PlayerBedwarsEntity, PlayerReviveTimer> playersNTimer = new HashMap<>();
    private final List<ResourceSpawner> publicRSpawners = new ArrayList<>();
    private final LinkedList<Block> putBlock = new LinkedList<>();

    private final List<AreaEffectTimer> publicBufferZone;
    private final List<LockedNormalEntity> eLockedList;
    // Scoreboard
    private final RoomUpdater updater;
    private final Scoreboard localScoreBoard;
    private final Objective objectiveLocalSB;

    /**
     * Initialize bedwars room
     *
     * @param mapName      Original map name
     * @param host         Hosted by
     * @param bedwarsWorld Copied world for game
     */
    public SleepingRoom(@Nonnull String mapName, @Nonnull Player host,@Nonnull World bedwarsWorld,
                        @Nullable PlayerBedwarsEntity hostEnt) {
        // Assign Attributes
        GameSystemConfig systemConfig = SleepingWarsPlugin.getGameSystemConfig();
        Location locSpawn = systemConfig.getQueuePos(bedwarsWorld, mapName);
        hostedBy = host;
        gameWorld = bedwarsWorld;
        gameWorld.setDifficulty(Difficulty.NORMAL);
        worldQueueSpawn = locSpawn;
        this.mapName = mapName;
        respawnTime = 5f;
        // Create scoreboard
        localScoreBoard = Bukkit.getScoreboardManager().getNewScoreboard();
        objectiveLocalSB = localScoreBoard.registerNewObjective("queue", "counter",
                ChatColor.YELLOW + "Queueing");
        objectiveLocalSB.setDisplaySlot(DisplaySlot.SIDEBAR);
        updater = new RoomUpdater();
        // Get all public resource spawners
        publicRSpawners.addAll(systemConfig.getWorldRS(bedwarsWorld, mapName, "PUBLIC"));
        hostedBy.sendMessage(ChatColor.GOLD + "Room Created, World name to enter the game: " +
                ChatColor.GREEN + bedwarsWorld.getName());
        // Register all events in room
        inGameEvent = new TimelineTimer(this, systemConfig.getTimelineEvents(mapName, this), publicRSpawners);
        inGameEvent.setShrunkBorderSize(systemConfig.getBorderData(mapName, true));
        // Get list of locked entity
        eLockedList = systemConfig.getLockedEntities(gameWorld, mapName, publicRSpawners);
        // Get all buffer zones
        publicBufferZone = systemConfig.getAreaBuffPublic(gameWorld, mapName);
        // Initialize Team raw data
        for (String teamName : systemConfig.getTeamNames(mapName))
            createdTeams.put(teamName, new TeamGroupMaker(this, teamName));
        // Host entered the room
        playerEnter(host, hostEnt);
    }

    /**
     * Begin the bedwars.
     */
    public void gameStart() {
        // Initialize game
        createTeam();
        setResourceSpawning(true);
        checkRemainingTeam();
        inGameEvent.start();
        isInGameOn = true;
        hostedBy = null;
        // Activate all buffer zones
        for (AreaEffectTimer bzt : publicBufferZone)
            bzt.start();
        // Init Scoreboard
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () ->
                localScoreBoard.resetScores(ChatColor.GREEN + "Player Count: "), 20L);
        objectiveLocalSB.setDisplayName(ChatColor.WHITE + "Bedwars");
        objectiveLocalSB.setDisplaySlot(DisplaySlot.SIDEBAR);
        Objective playerHealthDisplay = localScoreBoard.registerNewObjective("player-health",
                "health", " HP");
        playerHealthDisplay.setDisplaySlot(DisplaySlot.BELOW_NAME);
    }

    /**
     * Destroy room when the game ended or nobody in the room
     */
    public void destroyRoom() {
        // Teleport Players back to where they were
        for (PlayerBedwarsEntity peEntity : playerEntities.values()) {
            try {
                Player ePlayer = peEntity.getPlayer();
                ePlayer.getInventory().clear();
                ePlayer.setGameMode(GameMode.SURVIVAL);
                peEntity.returnEntity();
                ePlayer.sendMessage(ChatColor.YELLOW + "Game Ended, teleporting back to where you were.");
                ePlayer.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            } catch (Exception exc) {
                System.out.println("[SleepingWars] Player may not exists when trying" +
                        " to return the player to where it was. You can ignore this error message.");
            }
        }
        // Clear all resource spawners if the game is still on
        for (TeamGroupMaker t : createdTeams.values())
            t.activateRS(false);
        for (ResourceSpawner rsp : publicRSpawners)
            if (rsp.isRunning())
                rsp.setRunning(false);
        // Stop timeline and all schedules
        updater.destroyUpdater();
        setResourceSpawning(false);
        // Make sure every timeline event is stopped
        inGameEvent.stop();
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
        putBlock.clear();
        publicBufferZone.clear();
        createdTeams.clear();
        eLockedList.clear();
        // Unregister room in game manager and delete world file
        gameManager.getRoomMap().remove(gameWorld.getName());
        File worldDir = gameWorld.getWorldFolder();
        Bukkit.unloadWorld(gameWorld, true);
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
        if (entity == null)
            entity = new PlayerBedwarsEntity(player, player.getLocation(), player.getGameMode());
        playerEntities.put(player.getUniqueId(), entity);
        // Check if the game is ongoing
        if (!isInGameOn) {
            // Teleport to queue spawner
            player.teleport(worldQueueSpawn);
            roomBroadcast(String.format("%s joined the game. [%d player(s) in room]", ChatColor.GOLD +
                    player.getName(), playerEntities.size()));
            player.sendMessage(ChatColor.YELLOW + "This room is hosted by " + ChatColor.GREEN +
                    hostedBy.getName());
            player.setGameMode(GameMode.SURVIVAL);
        } else {
            if (entity.getTeamChoice() != null && !entity.isEliminated()) {
                TeamGroupMaker team = findTeam(entity.getTeamChoice());
                team.playerReconnectedHandler(entity);
                return;
            }
            player.teleport(worldQueueSpawn);
            player.setGameMode(GameMode.SPECTATOR);
        }
    }

    /**
     * Player leave the room
     *
     * @param player Referred player
     * @return Bedwars entity data of player
     */
    public PlayerBedwarsEntity playerLeave(Player player) {
        PlayerBedwarsEntity bent = playerEntities.remove(player.getUniqueId());
        // Destruct a player from team and game if it is exists
        if (bent != null) {
            Player p = bent.getPlayer();
            // Set Empty Scoreboard
            p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            // Check if game in progress
            if (!isInGameOn) {
                roomBroadcast(String.format("%s left the game. [%d player(s) in room]",
                        ChatColor.YELLOW + player.getName(), playerEntities.size()));
                // Change host if the host leave the room
                List<PlayerBedwarsEntity> listPEnt = new ArrayList<>(playerEntities.values());
                if (hostedBy.equals(player) && playerEntities.size() > 0) {
                    hostedBy = listPEnt.get(0).getPlayer();
                    roomBroadcast(ChatColor.LIGHT_PURPLE + hostedBy.getName() +
                            ChatColor.AQUA + " is now the room host.");
                }
            } else {
                TeamGroupMaker team = createdTeams.get(bent.getTeamChoice());
                team.playerDisconnectedHandler(bent);
            }
        }
        // Check if world or game is empty
        if (playerEntities.size() == 0) {
            if (bent != null)
                bent.returnEntity();
            if (gameWorld.getPlayers().size() > 0) {
                for (Player anonymous : gameWorld.getPlayers())
                    anonymous.kickPlayer(ChatColor.AQUA +
                            "World is being destroy, please reconnect after a few moment.");
            }
            destroyRoom();
            return null;
        }
        return bent;
    }

    /**
     * Create teams from list of players randomly.
     */
    private void createTeam() {
        // Get list of players
        List<PlayerBedwarsEntity> listOfPlayerEnt = new ArrayList<>(playerEntities.values());
        Random rand = new Random();
        // Insert player into team randomly
        List<String> teamNameKey = new ArrayList<>(createdTeams.keySet());
        int indexOfTeam = 0;
        while (listOfPlayerEnt.size() > 0) {
            // Get information
            int playerPickedIndex = rand.nextInt(listOfPlayerEnt.size());
            PlayerBedwarsEntity playerEntPicked = listOfPlayerEnt.remove(playerPickedIndex);
            TeamGroupMaker onTeam = createdTeams.get(teamNameKey.get(indexOfTeam));
            // Assign all
            playersNTimer.put(playerEntPicked, new PlayerReviveTimer(respawnTime, playerEntPicked, onTeam));
            onTeam.insertPlayer(playerEntPicked);
            playerEntPicked.setTeamChoice(onTeam.getName());
            // Set colorized team ui properties and scoreboard
            Team tOnScoreboard = localScoreBoard.getTeam(onTeam.getName());
            if (tOnScoreboard == null)
                tOnScoreboard = localScoreBoard.registerNewTeam(onTeam.getName());
            tOnScoreboard.setPrefix(String.format("%s[%s] %s", PluginStaticColor.getColorString(onTeam.getRawColor()),
                    onTeam.getName(), ChatColor.WHITE + " "));
            tOnScoreboard.setCanSeeFriendlyInvisibles(false);
            tOnScoreboard.setAllowFriendlyFire(false);
            tOnScoreboard.addEntry(playerEntPicked.getPlayer().getName());
            // Next index of team
            indexOfTeam = indexOfTeam + 1 >= teamNameKey.size() ? 0 : indexOfTeam + 1;
        }
    }

    /**
     * Update each player scoreboard.
     */
    public void updateScoreBoard() {
        for (PlayerBedwarsEntity bent : playerEntities.values()) {
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
                emptyLine0 = objectiveLocalSB.getScore(" ");
                emptyLine0.setScore(scoreLineCount);
                scoreLineCount--;
                // Add teams on line
                for (TeamGroupMaker t : createdTeams.values()) {
                    String teamFormat = String.format("%s%d %s", PluginStaticColor
                            .getColorString(t.getRawColor()), t.getRemainingPlayers(), t.getName());
                    Score steam = objectiveLocalSB.getScore(teamFormat);
                    steam.setScore(scoreLineCount);
                    scoreLineCount--;
                }
                // Add another empty line
                emptyLine1 = objectiveLocalSB.getScore("  ");
                emptyLine1.setScore(scoreLineCount);
                scoreLineCount--;
                // Events line
                if (inGameEvent.isEventExceeded()) {
                    eventTitle = objectiveLocalSB.getScore(ChatColor.ITALIC + "   Next Event in [0:00] ");
                    scevent = objectiveLocalSB.getScore(ChatColor.GRAY + "[No Upcoming Events]");
                } else {
                    // Time display
                    float rawSeconds = inGameEvent.getCounter();
                    int minutes = (int) rawSeconds / 60;
                    int prevMin = ((int) rawSeconds + 1) / 60;
                    int seconds = (int) rawSeconds % 60;
                    int prevSec = ((int) rawSeconds + 1) % 60;
                    String timeString = seconds < 10 ? String.format("%d:0%d", minutes, seconds) :
                            String.format("%d:%d", minutes, seconds);
                    String prevTimeString = prevSec < 10 ? String.format("%d:0%d", prevMin, prevSec) :
                            String.format("%d:%d", prevMin, prevSec);
                    String formattedTimeString = String.format("%s   Next Event in [%s] ",
                            ChatColor.ITALIC + "", timeString);
                    String prevFormattedTS = String.format("%s   Next Event in [%s] ",
                            ChatColor.ITALIC + "", prevTimeString);
                    localScoreBoard.resetScores(prevFormattedTS);
                    eventTitle = objectiveLocalSB.getScore(formattedTimeString);
                    // Event name display
                    scevent = objectiveLocalSB.getScore(ChatColor.GRAY + inGameEvent.getNextEvent().getName());
                }
                eventTitle.setScore(scoreLineCount);
                scoreLineCount--;
                scevent.setScore(scoreLineCount);
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
     * Reviving player from any death, this function is extremely useful, only if the current game is processing.
     * @param player Death body
     * @return True if player instantly respawn in any mode, if player entity not exists then it returns false
     */
    public boolean reviving(Player player) {
        // Get player entity
        PlayerBedwarsEntity playerEnt = playerEntities.get(player.getUniqueId());
        if (playerEnt == null)
            return false;
        // Check game ended
        if (!isGEnded) {
            // Check if bed is already broken, then the person has been eliminated
            TeamGroupMaker team = createdTeams.get(playerEnt.getTeamChoice());
            if (!team.isBedBroken()) {
                for (PlayerBedwarsEntity pbent : playersNTimer.keySet()) {
                    if (pbent.getPlayer().equals(player))
                        playersNTimer.get(pbent).start();
                }
            } else {
                // Player eliminated handling
                team.removePlayer(playerEnt);
                playerEnt.setEliminated(true);
                BedwarsPlayerEliminatedEvent e = new BedwarsPlayerEliminatedEvent(this, team, playerEnt);
                Bukkit.getPluginManager().callEvent(e);
            }
            player.getInventory().clear();
        }
        // Immediately set player to normal fresh from spawn form
        player.setHealth(20d);
        player.teleport(worldQueueSpawn);
        player.setGameMode(GameMode.SPECTATOR);
        return true;
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
        PlayerBedwarsEntity playerEnt = findPlayer(player);
        if (playerEnt == null)
            return false;
        // Check location, each will check the material
        Location blockLocation = withBlock.getLocation();
        Material mat = withBlock.getType();
        for (int i = 0; i < eLockedList.size(); i++) {
            Location lockedLoc = eLockedList.get(i).getLockedLocation();
            Material matOnLockedLoc = lockedLoc.getBlock().getType();
            // Check if it is a normal or iron door, check it's above and below that block
            if (PluginStaticFunc.isStandardDoor(mat) && PluginStaticFunc.isStandardDoor(matOnLockedLoc)) {
                Block doorBlock = lockedLoc.getBlock();
                // Check immediately same location with same block
                if (lockedLoc.getBlock().equals(blockLocation.getBlock()))
                    return unlockTheDoor(i, playerEnt);
                Block upperRelative = doorBlock.getRelative(BlockFace.UP, 1), lowerRelative =
                        doorBlock.getRelative(BlockFace.DOWN, 1);
                // Check if above or below its location is a part of door
                if (PluginStaticFunc.isStandardDoor(upperRelative.getType())) {
                    if (upperRelative.getLocation().getBlock().equals(blockLocation.getBlock()))
                        return unlockTheDoor(i, playerEnt);
                } else {
                    if (lowerRelative.getLocation().getBlock().equals(blockLocation.getBlock()))
                        return unlockTheDoor(i, playerEnt);
                }
            } else if (PluginStaticFunc.isFenceGate(mat) && PluginStaticFunc.isFenceGate(matOnLockedLoc)) {
                if (lockedLoc.getBlock().equals(blockLocation.getBlock()))
                    return unlockTheDoor(i, playerEnt);
            } else if (PluginStaticFunc.isTrapDoor(mat) && PluginStaticFunc.isTrapDoor(matOnLockedLoc)) {
                if (lockedLoc.getBlock().equals(blockLocation.getBlock()))
                    return unlockTheDoor(i, playerEnt);
            } else if (PluginStaticFunc.isButton(mat) && PluginStaticFunc.isButton(matOnLockedLoc)) {
                if (lockedLoc.getBlock().equals(blockLocation.getBlock()))
                    return unlockTheDoor(i, playerEnt);
            } else if (mat == Material.LEVER && matOnLockedLoc == Material.LEVER) {
                if (lockedLoc.getBlock().equals(blockLocation.getBlock()))
                    return unlockTheDoor(i, playerEnt);
            }
        }
        return true;
    }

    /**
     * Try unlock the locked entity.
     *
     * @param listIndex Index of list
     * @param playerEnt Player who is trying to unlock it
     * @return True if it is successfully unlocked, else then false
     */
    private boolean unlockTheDoor(int listIndex, PlayerBedwarsEntity playerEnt) {
        LockedNormalEntity ent = eLockedList.get(listIndex);
        if (ent.unlockEntity(playerEnt)) {
            playerEnt.getPlayer().sendMessage(ChatColor.GREEN + "Access granted!");
            BedwarsLockUnlocked event = new BedwarsLockUnlocked(eLockedList.remove(listIndex), playerEnt);
            Bukkit.getPluginManager().callEvent(event);
            return true;
        } else {
            playerEnt.getPlayer().sendMessage(ChatColor.RED + "You can't open this.");
            return false;
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
            if (team.isTeamStandStill()) {
                lastStanding = team;
                remainingCount++;
            }
        }
        // if last one standing then game ended
        if (remainingCount <= 1 && lastStanding != null) {
            isGEnded = true;
            BedwarsEndedEvent gameEndedEvent = new BedwarsEndedEvent(this, lastStanding);
            Bukkit.getPluginManager().callEvent(gameEndedEvent);
            // Wait for 10 seconds to automatically return all players to where they were
            Bukkit.getScheduler().scheduleSyncDelayedTask(SleepingWarsPlugin.getPlugin(), this::destroyRoom, 200L);
            // Remove Team, set all to spectator
            for (PlayerBedwarsEntity playerEnt : lastStanding.getPlayerEntities())
                playerEnt.getPlayer().setGameMode(GameMode.SPECTATOR);
        }
    }

    /**
     * When the game is progress, store a block reference which the block has just placed by player.
     *
     * @param block Block reference
     */
    public void putBlock(Block block) {
        putBlock.add(block);
    }

    /**
     * Find and destroy block that has been placed before.
     *
     * @param block Block reference
     * @return True if the block was in list and removed, else then false
     */
    public boolean destroyBlock(Block block) {
        return putBlock.remove(block);
    }

    /**
     * Send message to each player in room.
     */
    public void roomBroadcast(String message) {
        for (PlayerBedwarsEntity ppl : playerEntities.values())
            ppl.getPlayer().sendMessage(message);
    }

    /**
     * Send message to each player in room.
     */
    public void roomBroadcast(String message, boolean forSpectators) {
        for (PlayerBedwarsEntity ppl : playerEntities.values()) {
            if (forSpectators) {
                if (ppl.getPlayer().getGameMode() == GameMode.SPECTATOR)
                    ppl.getPlayer().sendMessage(message);
            } else {
                if (ppl.getPlayer().getGameMode() != GameMode.SPECTATOR)
                    ppl.getPlayer().sendMessage(message);
            }
        }
    }

    /**
     * Send title message in front of each player's face in room.
     */
    public void roomBroadcastTitle(String title, String subs, int fadeIn, int stayOn, int fadeOut) {
        for (PlayerBedwarsEntity ppl : playerEntities.values())
            ppl.getPlayer().sendTitle(title, subs, fadeIn, stayOn, fadeOut);
    }

    /**
     * Get a list of players inside this room. This list is not referenced to any attribute.
     *
     * @return A list of players
     */
    public List<Player> getPlayersInRoom() {
        List<Player> ppl = new ArrayList<>();
        for (PlayerBedwarsEntity pbent : playerEntities.values())
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
        return new ArrayList<>(createdTeams.values());
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
        PlayerBedwarsEntity playerEnt = playerEntities.get(player.getUniqueId());
        if (playerEnt == null)
            return null;
        return createdTeams.get(playerEnt.getTeamChoice());
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
    public PlayerBedwarsEntity findPlayer(Player player) {
        return playerEntities.get(player.getUniqueId());
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
     * Check if the game ended.
     * @return True if game is at ended state, else then false
     */
    public boolean isGameEnded() {
        return isGEnded;
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
     */
    public void setResourceSpawning(boolean active) {
        if (isResSpawn != active) {
            isResSpawn = active;
            // Running public resource spawner
            for (ResourceSpawner spawner : publicRSpawners) {
                spawner.setRunning(isResSpawn);
            }
            // Running all team resource spawner
            for (TeamGroupMaker t : createdTeams.values()) {
                t.activateRS(isResSpawn);
            }
        }
    }

    /**
     * Set the Game status is currently countdown to start.
     * @param active Is active
     */
    public void setStarting(boolean active) {
        isGStarting = active;
    }

    /**
     * Get is currently the game doing countdown to start.
     * @return True if currently starting, else then false
     */
    public boolean isStarting() {
        return isGStarting;
    }

    private class RoomUpdater {

        private final int taskID;

        public RoomUpdater() {
            taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin,
                    SleepingRoom.this::updateScoreBoard, 0L, 10L);
        }

        public void destroyUpdater() {
            Bukkit.getScheduler().cancelTask(taskID);
        }
    }
}
