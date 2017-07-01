package com.lordstephen77.dreamblocks.fillers;

import com.lordstephen77.dreamblocks.TileStore;
import com.lordstephen77.dreamblocks.World;

import java.util.Random;

/**
 * Created by Александр on 01.07.2017.
 */
public abstract class WorldFiller {
    protected World world;
    protected Random random;
    protected TileStore tileStore;
    public WorldFiller(World world, Random random, TileStore tileStore){
        this.world = world;
        this.random = random;
        this.tileStore = tileStore;
    }

    public abstract void fill();
}
