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

 You have just teleport to `MyCustomBedwarsWorld`, it's an empty world that only has small area of bedrocks. Therefore you can start building your own bedwars map in it. For more further building commands, read this part [here](https://github.com/JonasT4n/Sleeping-Wars-Plugin#in-builder-mode)


### In Builder Mode
 `/sworld` command made only for custom bedwars world builder and some cannot be used in normal world that are not registered as custom bedwars world. All exclusive item and tool kit and setting up your own game system are in this command.

 After you got an `op` or `sleepywar.builder` permission, you can use it as you want as long as you still have this permissions. Here's the list of useful commands:

|Commands|Description|
|--------|-----------|
|`/sworld addevent <event-type> <trigger-duration> <display-name>`|Add a game event into timeline|
|`/sworld create <worldname>`|Start creating your own bedwars map|
|`/sworld delevent <display-name>`|Remove an event from timeline|
|`/sworld delrspawn <codename>`|Delete the resource spawner that has been made before|
|`/sworld delshop`|Delete the shop spawn location|
|`/sworld edit <worldname>`|Teleport and edit the world map|
|`/sworld help <page>`|Help sub-command of `/sworld`|
|`/sworld leave`|If you are in custom world building, you can go back to where you were|
|`/sworld setbed <team-name>`|Set team bed location, after use this get yourself a bed and put it somewhere|
|`/sworld setqspawn`|Set a Queue State spawner, when player join the game then it will teleport exactly to that location|
|`/sworld setrspawn <resource-type> <team-or-public> <codename>`|Set a natural resource spawner like iron spawner, diamond spawner, etc.|
|`/sworld setshop`|Set a spawn for shop villager, the safest way to spawn a shop villager|
|`/sworld setspawn`|Set world default spawn|
|`/sworld spawnshop`|After you set the location to spawn the shop villager, then you can spawn it as you want|
|`/sworld teamspawn`|Set an existing team spawn in game|
|`/sworld testres`|Testing the whole world resource spawners|
|`/sworld rsinfo`|See world current settled resource spawners|
|`/sworld worldinfo`|See world current general info|

### In Game Mode
 `/bedwars` command is used to play the bedwars game. After you have made the world you can choose your own map to start playing it. No need any permission to use this command.

 All commands in this section:

|Commands|Description|
|--------|-----------|
|`/bedwars host <worldname>`|Host the game and choose the map to play|
|`/bedwars join <gamecode>`|Join the existing/hosted game|
|`/bedwars start`|Only host can use this to begin the game|
|`/bedwars leave`|Leave the game if you are in the game|

#### Note: 
 - This plugin currently works in Paper Minecraft Server.
 
 
