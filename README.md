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
|`/sworld setqspawn`|Set a Queue State spawner, when player join the game then it will teleport exactly to that location|
|`/sworld setrspawn <resource-type> <team-or-public> <codename>`|Set a natural resource spawner like iron spawner, diamond spawner, etc.|
|`/sworld setbed <team-name>`|Set team bed location|
|`/sworld delrspawn <codename>`|Delete the resource spawner that has been made before|
|`/sworld addevent <event-type> <trigger-duration> <display-name>`|Add a game event into timeline|
|`/sworld delevent <display-name>`|Remove an event from timeline|
|`/sworld openb`|Exclusive item kit for custom bedwars world building|

#### Note: 
 - This plugin currently works in Paper Minecraft Server.
 
 
