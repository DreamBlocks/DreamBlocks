package com.lordstephen77.dreamblocks.fillers;

import com.lordstephen77.dreamblocks.Constants;
import com.lordstephen77.dreamblocks.TileStore;
import com.lordstephen77.dreamblocks.World;

import java.util.Random;

/**
 * Created by Александр on 01.07.2017.
 */
public class WaterFiller extends WorldFiller {
    public WaterFiller(World world, Random random) {
        super(world, random);
    }

    @Override
    public void fill() {
        int median = (int) (.5 * world.height);
        for (int i = 0; i < world.width; i++) {
            if (world.tiles[i][median].type != TileStore.TYPE_AIR) {
                continue;
            }

            // flood fill down
            for (int j = median; j < world.height; j++) {

                if (world[i][j] != Constants.TileID.NONE) {
                    carve(world, i, j - 1, 1 + random.nextDouble() * 2, Constants.TileID.SAND, new Constants.TileID[] {
                            Constants.TileID.WATER, Constants.TileID.NONE }, false);
                    break;
                }
                world[i][j] = Constants.TileID.WATER;
            }
        }
    }
}
