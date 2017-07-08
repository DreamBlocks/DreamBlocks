package com.lordstephen77.dreamblocks.light;

import com.lordstephen77.dreamblocks.Constants;
import com.lordstephen77.dreamblocks.Direction;
import com.lordstephen77.dreamblocks.World;

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
        for (int x = 0; x < width; x++) {
            updateLightValues(x);
        }
    }

    public void removedTile(int x, int y) {
        updateLightValues(x);
    }

    private int getGroundY(int x){
        if (world.isOutOfWorld(x, 0)){
            return Integer.MIN_VALUE;
        }
        int y = 0;
        while(tiles[x][y].type.getOpacity() == Constants.TRANSPARENT){
            y++;
        }
        return y;
    }

    private void lightDown(int x, int beginY, int endY, int lightValue){
        for (int y = beginY; y < endY && y < height; y++){
            lightValues[x][y] = lightValue;
        }
    }

    private void updateLightValues(int x) {
        int leftX = x + Direction.LEFT.dx;
        int leftX2 = leftX + Direction.LEFT.dx;
        int rightX = x + Direction.RIGHT.dx;
        int rightX2 = rightX + Direction.RIGHT.dx;
        int middleGroundY = getGroundY(x);
        int leftGroundY = getGroundY(leftX);
        int rightGroundY = getGroundY(rightX);
        int rightGroundY2 = getGroundY(rightX2);
        int leftGroundY2 = getGroundY(leftX2);
        updateColumnAssumeAdjacent(x, middleGroundY, leftGroundY, rightGroundY);
        if(!world.isOutOfWorld(leftX, 0)) {
            updateColumnAssumeAdjacent(leftX, leftGroundY, leftGroundY2, middleGroundY);
        }
        if(!world.isOutOfWorld(rightX, 0)) {
            updateColumnAssumeAdjacent(rightX, rightGroundY, middleGroundY, rightGroundY2);
        }
    }

    public void updateColumnAssumeAdjacent(int x, int middleGroundY, int leftGroundY, int rightGroundY){
        //overground blocks
        lightDown(x, 0, middleGroundY, LightingEngine.DIRECT_SUN);
        if (middleGroundY < leftGroundY) {
            //lit ground blocks adjacent to light
            lightDown(x, middleGroundY, leftGroundY, LightingEngine.INDIRECT_SUN);
            //top ground block
            lightDown(x, leftGroundY + 1, leftGroundY + 2, LightingEngine.INDIRECT_SUN);
            //underground blocks
            lightDown(x, leftGroundY + 1, world.height, DARKNESS);
        }
        if (middleGroundY < rightGroundY) {
            //lit ground blocks adjacent to light
            lightDown(x, middleGroundY, rightGroundY, LightingEngine.INDIRECT_SUN);
            //top ground block
            lightDown(x, rightGroundY + 1, rightGroundY + 2, LightingEngine.INDIRECT_SUN);
            //underground blocks
            lightDown(x, rightGroundY + 1, world.height, DARKNESS);
        }
        if (middleGroundY >= leftGroundY && middleGroundY >= rightGroundY) {
            //top ground block
            lightDown(x, middleGroundY, middleGroundY + 1, LightingEngine.INDIRECT_SUN);
            //underground blocks
            lightDown(x, middleGroundY + 1, world.height, DARKNESS);
        }
    }

    public void addedTile(int x, int y) {
        updateLightValues(x);
    }
}
