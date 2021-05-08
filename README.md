# Sleeping Wars Plugin
 Sleeping Wars Plugin is a custom minecraft plugin for creating a Bedwars Game in your own personal server. You can set your own teams, spawners, shop items, game system, and more. Everything for custom Bedwars game is in this one pack plugin. Start make your own bedwars game! 
 
## Getting Started
 To get started, first you need to create custom world by using the command below. You need an `op` permission or `sleepywar.builder` to be able to use these commands.

```python
/sworld create MyCustomBedwarsWorld
```

 Now edit your world and start building it.
 
```python
/sworld edit MyCustomBedwarsWorld
```

 You have just teleport to `MyCustomBedwarsWorld`, it's an empty world that only has small area of bedrocks. Therefore you can start building your own bedwars map in it. For more builder commands, read this part [here](https://github.com/JonasT4n/Sleeping-Wars-Plugin#in-builder-mode)

### In Builder Mode
 `/sworld` command made only for custom bedwars world builder and some cannot be used in normal world that are not registered as custom bedwars world. All exclusive item and tool kit and setting up your own game system are in this command.

 After you got an `op` or `sleepywar.builder` permission, you can use it as you want as long as you still have this permissions. Here's the list of useful commands:

|Commands|Description|
|--------|-----------|
|`/sworld addcmd <command-type> [target-map]`|Create a command handler with using minecraft `Button`, it is better than typing commands to play the game.|
|`/sworld addevent <event-type> <interval-seconds> <display-name>`|Add a game event into event list or timeline, for example: `/sworld addevent world-shrink 20 WorldStartShrinking`, it means adding world shrinking event that will be called in 20 seconds, displayed as WorldStartShrinking.|
|`/sworld addlockedreq <team-name> <codename> <price>`|Add requirements to unlock like locked door, gate, lever, or buttons.|
|`/sworld addteam <team-name> [color]`|Add a new team or restore an unremoved team, for example: `/sworld addteam Blue blue`, it means adding a new team called 'Blue' team with prefix color blue.|
|`/sworld create <worldname>`|Start creating your own bedwars map, for example `/sworld create MyBedwarsMap`, it means to create a new bedwars map called 'MyBedwarsMap'.|
|`/sworld edit <worldname>`|Teleport and edit the bedwars world map.|
|`/sworld help <default=1/page>`|Help sub-commands of `/sworld`|
|`/sworld inforspawner`|Get almost all settled resource spawners information in current world map.|
|`/sworld infoworld`|Get bedwars world map general info.|
|`/sworld infotimeline`|Get event list or timeline information in current world map.|
|`/sworld leave`|Go back to exactly where you were before entering bedwars world map.|
|`/sworld rmevent <display-name>`|Remove an event from timeline|
|`/sworld rmlock <codename>`|Remove locked entity.|
|`/sworld rmrspawn <default=PUBLIC/select-team> <codename>`|Remove resource spawner.|
|`/sworld rmshop <shop-type> <index-number>`|Remove shop villager spawn location.|
|`/sworld rmteam <team-name> [default=false/is-permanent-remove]`|Remove an existing team from game.|
|`/sworld setarea <team-name>`|Set zone for team, this area can affect team and enemies with potion effects. Place 2 any solid blocks to set the area after using it.|
|`/sworld setbed <team-name>`|Set team bed location, after use this command, you must get yourself a bed and put it somewhere around the map.|
|`/sworld setborder <default=1024/size-radius>`|Set bedwars world map starting border.|
|`/sworld seteventorder <event-display-name> <index-order>`|Set procedure order when will it be called after or before other events, for example `/sworld seteventorder WorldStartShrinking 5`, it means `WorldStartShrinking` event will be call after all event orders which less than 5.|
|`/sworld setflag <key> <true-or-false>`|Set game flag, for example `/sworld setflag armor-restriction true`, it means player will not be able to take off their armor in game.|
|`/sworld setlock <codename> [resource-spawner-codename]`|Lock path which player cannot enter it. Place any kinds of door/gate/lever/buttons after using this command to set the location.|
|`/sworld setmaxplayerteam <amount/1-100>`|Set maximum player per team in game, for example `/sworld setmaxplayerteam 5`, it means that each team can have at maximum 5 players.|
|`/sworld setqspawn`|Set a Queue State spawner, when player join the game then it will teleport exactly to that location|
|`/sworld setrinterval <default=PUBLIC/select-team> <codename> <interval-seconds>`|Change how fast resource spawner can spawn the item in seconds.|
|`/sworld setrspawn <resource-type> <team-or-public> <codename>`|Set a natural resource spawner like iron ingot spawner, diamond spawner, etc.|
|`/sworld setrtype <default=PUBLIC/select-team> <codename> <item-type>`|Change item spawn of existing resource spawner.|
|`/sworld setshop <shop-type>`|Set a spawn for shop villager, the safest way to spawn a shop villager.|
|`/sworld setshrunkborder <default=size-radius>`|Set world shrinking border for world shrinking event.|
|`/sworld setspawn`|Set world default spawn.|
|`/sworld setteamspawner <team-name>`|Set an existing team spawner in game.|
|`/sworld testshopspawn`|Spawn villager shops, You must use this command to make the villagers appear in game.|
|`/sworld testrspawner`|Testing all resource spawners that you have set.|


### In Game Mode
 `/bedwars` command is used to play the bedwars game. After you have made the world you can choose your own map to start playing it. No need any permission to use this command.

 All commands in this section:

|Commands|Description|
|--------|-----------|
|`/bedwars host <worldname>`|Host the game and choose the map to play|
|`/bedwars join <roomcode>`|Join the existing/hosted game|
|`/bedwars start`|Only host can use this to begin the game|
|`/bedwars leave`|Leave the game if you are in the game|

#### Note: 
 - This plugin currently works in Spigot servers, aswell as Spigot forks such as Paper.
 
 
