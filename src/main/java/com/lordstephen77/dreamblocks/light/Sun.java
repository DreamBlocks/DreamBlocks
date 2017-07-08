package com.lordstephen77.dreamblocks.light;

import com.lordstephen77.dreamblocks.Constants;
import com.lordstephen77.dreamblocks.Direction;
import com.lordstephen77.dreamblocks.World;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Александр on 08.07.2017.
 */
public class Sun extends LightingEngine {
    public Sun(World world){
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
        LinkedList<LightingPoint> sources = new LinkedList<>();
        for (int x = 0; x < width; x++) {
            sources.addAll(getSunSources(x));
        }
        spreadLightingDijkstra(sources);
    }

    public void removedTile(int x, int y) {
        lightFlow[x][y] = Direction.UNKNOWN;
        spreadLightingDijkstra(getSunSources(x));
    }

    public List<LightingPoint> getSunSources(int column) {
        LinkedList<LightingPoint> sources = new LinkedList<>();
        for (int y = 0; y < height - 1; y++) {
            if (tiles[column][y].type.getLightBlocking() != 0) {
                break;
            }
            sources.add(new LightingPoint(column, y, Direction.SOURCE, Constants.LIGHT_VALUE_SUN));
        }
        return sources;
    }

    public void addedTile(int x, int y) {
        lightFlow[x][y] = Direction.UNKNOWN;
        // redo the column for sun
        boolean sun = true;
        for (int i = 0; i < height; i++) {
            if (tiles[x][i].type.getLightBlocking() != 0) {
                sun = false;
            }
            if (sun) {
                lightFlow[x][i] = Direction.SOURCE;
            } else {
                lightFlow[x][i] = Direction.UNKNOWN;
            }
        }
        resetLighting(x, y);
    }
}
