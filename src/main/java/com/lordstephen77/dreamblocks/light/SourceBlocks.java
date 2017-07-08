package com.lordstephen77.dreamblocks.light;

import com.lordstephen77.dreamblocks.Direction;
import com.lordstephen77.dreamblocks.World;

/**
 * Created by Александр on 08.07.2017.
 */
public class SourceBlocks extends LightingEngine {
    public SourceBlocks(World world){
        this.world = world;
        this.width = world.width;
        this.height = world.height;
        this.tiles = world.tiles;
        lightValues = new int[width][height];
        lightFlow = new Direction[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                lightValues[x][y] = 0;
                lightFlow[x][y] = Direction.UNKNOWN;
            }
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (tiles[x][y].type.getBrightness() > 0) {
                    lightFlow[x][y] = Direction.SOURCE;
                    lightValues[x][y] = tiles[x][y].type.getBrightness();
                }
            }
        }
    }

    public void removedTile(int x, int y) {
        if (lightFlow[x][y] == Direction.SOURCE) {
            lightFlow[x][y] = Direction.UNKNOWN;
            resetLighting(x, y);
            return;
        }
        lightFlow[x][y] = Direction.UNKNOWN;
        LightingPoint p = new LightingPoint(x, y, Direction.SOURCE, lightValues[x][y]);
        spreadLightingDijkstra(getNeighbors(p));
    }

    public void addedTile(int x, int y) {
        lightFlow[x][y] = Direction.UNKNOWN;
        lightValues[x][y] = tiles[x][y].type.getBrightness();
        lightFlow[x][y] = Direction.SOURCE;
        resetLighting(x, y);
    }
}
