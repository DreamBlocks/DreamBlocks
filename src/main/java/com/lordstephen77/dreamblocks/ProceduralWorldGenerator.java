package com.lordstephen77.dreamblocks;

import java.util.Random;

/**
 * Created by Александр on 08.07.2017.
 */
public class ProceduralWorldGenerator extends WorldGenerator {
    Constants.TileID[][] worldData;
    public ProceduralWorldGenerator(int width, int height, Random random) {
        super(width, height, random);
        worldData = new Constants.TileID[width][height];
    }

    @Override
    public Constants.TileID[][] generate() {
        return worldData;
    }

    public void setWorldData(Constants.TileID[][] worldData) {
        this.worldData = worldData;
    }
}
