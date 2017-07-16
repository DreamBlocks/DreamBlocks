package com.lordstephen77.dreamblocks;

import java.util.Random;

/**
 * Created by Александр on 08.07.2017.
 */
public class ProceduralWorldGenerator extends WorldGenerator {
    private Constants.TileID[][] worldData;
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

    public void transposeWorldData(Constants.TileID[][] worldData){
        Constants.TileID[][] worldDataT = new Constants.TileID[worldData[0].length][worldData.length];
        for (int x = 0; x < worldData.length; x++){
            for (int y = 0; y < worldData[0].length; y++){
                worldDataT[y][x] = worldData[x][y];
            }
        }
        this.worldData = worldDataT;
    }
}
