package com.lordstephen77.dreamblocks.fillers;

import com.lordstephen77.dreamblocks.*;

import java.util.Random;

/**
 * Created by Александр on 01.07.2017.
 */
public class TreeFiller extends WorldFiller {
    public TreeFiller(World world, Random random, TileStore tileStore) {
        super(world, random, tileStore);
    }

    @Override
    public void fill() {
        int maxTreeHeight = 110;
        for (int x = 0; x < world.width; x++) {
            int dy = getHeightOverSurface(x, maxTreeHeight);
            boolean isOnDirt = world.tileIdAt(x, maxTreeHeight + dy) == Constants.TileID.GRASS;
            if (random.nextDouble() > .8 && isOnDirt) {
                addTemplate(TileTemplate.tree, new Int2(x, maxTreeHeight + dy));
            }
        }
    }

    private int getHeightOverSurface(int px, int py){
        int y = py;
        boolean isAir, isLeaves;
        do {
            Constants.TileID tileID = world.tileIdAt(px,y);
            isAir = tileID == Constants.TileID.AIR;
            isLeaves = tileID == Constants.TileID.LEAVES;
            y++;
        }
        while(y < world.height && (isAir || isLeaves));
        return y - py - 1;
    }

    private void addTemplate(TileTemplate tileTemplate, Int2 position) {
        for (int i = 0; i < tileTemplate.template.length; i++) {
            for (int j = 0; j < tileTemplate.template[0].length; j++) {
                if (tileTemplate.template[i][j] != Constants.TileID.NONE) {
                    int px = position.x - tileTemplate.spawnY + i;
                    int py = position.y - tileTemplate.spawnX + j;
                    world.changeTile(px, py, tileStore.make(tileTemplate.template[i][j]));
                }
            }
        }
    }
}
