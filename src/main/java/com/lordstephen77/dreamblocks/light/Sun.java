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

    private void updateLightValues(int x){
        int y = 0;
        while(tiles[x][y].type.getOpacity() == Constants.TRANSPARENT){
            setLightAt(x, y, Direction.LEFT, Constants.LIGHT_VALUE_SUN - 2);
            setLightAt(x, y, Direction.RIGHT, Constants.LIGHT_VALUE_SUN - 2);
            setLightAt(x, y, Direction.LEFT, Constants.LIGHT_VALUE_SUN - 2);
            lightValues[x][y] = Constants.LIGHT_VALUE_SUN;
            y++;
        }
        while (y < height){
            boolean sunnyLeft = getLightAt(x, y, Direction.LEFT) == Constants.LIGHT_VALUE_SUN;
            boolean sunnyRight = getLightAt(x, y, Direction.RIGHT) == Constants.LIGHT_VALUE_SUN;
            boolean sunnyUp = getLightAt(x, y, Direction.UP) == Constants.LIGHT_VALUE_SUN;
            if(sunnyLeft || sunnyRight || sunnyUp){
                lightValues[x][y] = Constants.LIGHT_VALUE_SUN - 2;
            } else {
                lightValues[x][y] = 0;
            }
            y++;
        }
    }

    private int getLightAt(int x, int y, Direction dir){
        if (world.isOutOfWorld(x, y, dir)){
            return 0;
        }
        return lightValues[x + dir.dx][y + dir.dy];
    }

    private void setLightAt(int x, int y, Direction dir, int lightValue){
        if (world.isOutOfWorld(x, y, dir)){
            return;
        }
        if (lightValues[x + dir.dx][y + dir.dy] < Constants.LIGHT_VALUE_SUN){
            lightValues[x + dir.dx][y + dir.dy] = lightValue;
        }
    }

    public void addedTile(int x, int y) {
        updateLightValues(x);
    }
}
