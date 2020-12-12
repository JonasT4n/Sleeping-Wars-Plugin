package com.joenastan.sleepingwars.utility;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class VoidGenerator extends ChunkGenerator {

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
        return createChunkData(world);
    }

    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        return new Location(world, 0, 50, 0);
    }

}
