package com.lordstephen77.dreamblocks.fillers;

import com.lordstephen77.dreamblocks.World;

import java.util.Random;

/**
 * Created by Александр on 01.07.2017.
 */
public abstract class WorldFiller {
    protected World world;
    protected Random random;
    public WorldFiller(World world, Random random){
        this.world = world;
        this.random = random;
    }

    public abstract void fill();
}
