package com.lordstephen77.dreamblocks.light;

import com.lordstephen77.dreamblocks.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.Random;

import static com.lordstephen77.dreamblocks.Constants.TileID.*;

/**
 * Created by Александр on 15.07.2017.
 */
public class RayTest {

    private World world;
    private static TileStore tileStore = new TileStore();

    @Test
    public void shouldCalculateSlope(){
        Ray<Tile> ray;

        ray = new Ray<>(world.tiles, 1, 1, Direction.LEFT.angleInRad);
        Assert.assertEquals(0, ray.getA(), 0.0001);

        ray = new Ray<>(world.tiles, 1, 1, Direction.RIGHT.angleInRad);
        Assert.assertEquals(0, ray.getA(), 0.0001);

        ray = new Ray<>(world.tiles, 1, 1, Direction.DOWN.angleInRad);
        Assert.assertEquals(0, 1.0/ray.getA(), 0.0001);

        ray = new Ray<>(world.tiles, 1, 1, Direction.UP.angleInRad);
        Assert.assertEquals(0, 1.0/ray.getA(), 0.0001);

        ray = new Ray<>(world.tiles, 1, 1, Direction.DOWN_LEFT.angleInRad);
        Assert.assertEquals(-1, ray.getA(), 0.0001);

        ray = new Ray<>(world.tiles, 1, 1, Direction.DOWN_RIGHT.angleInRad);
        Assert.assertEquals(1, ray.getA(), 0.0001);

        ray = new Ray<>(world.tiles, 1, 1, Math.toRadians(30));
        Assert.assertEquals(0.5773, ray.getA(), 0.0001);

        ray = new Ray<>(world.tiles, 1, 1, Math.toRadians(60));
        Assert.assertEquals(1.7320, ray.getA(), 0.0001);
    }

    @Before
    public void setUp(){
        Random rnd = new Random();
        ProceduralWorldGenerator worldGenerator = new ProceduralWorldGenerator(3, 4, rnd);
        world = new World(5, 6, rnd);
        worldGenerator.transposeWorldData(new Constants.TileID[][]{
            {AIR,    LEAVES, AIR,      TORCH,    AIR},
            {LEAVES, WOOD,   LEAVES,   STONE,   COAL_ORE},
            {LEAVES, WOOD,   COBBLE,   DIRT,    DIAMOND_ORE},
            {WATER,  SAND,   STONE,    COAL_ORE, STONE},
            {SAND,   DIRT,   COBBLE,   IRON_ORE, IRON_ORE},
            {DIRT,   COBBLE, IRON_ORE, STONE,    STONE}
        });
        world.fillWith(tileStore, worldGenerator);
    }

    @Test
    public void castX0Y0A30(){
        Ray<Tile> iterator = world.castRay(0, 0, Math.toRadians(30));
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(LEAVES, iterator.next().type.getId());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(WOOD, iterator.next().type.getId());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(LEAVES, iterator.next().type.getId());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(STONE, iterator.next().type.getId());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(DIRT, iterator.next().type.getId());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(DIAMOND_ORE, iterator.next().type.getId());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void castX0Y0Down(){
        Ray<Tile> iterator = world.castRay(0, 0, Direction.DOWN);
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(LEAVES, iterator.next().type.getId());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(LEAVES, iterator.next().type.getId());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(WATER, iterator.next().type.getId());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(SAND, iterator.next().type.getId());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(DIRT, iterator.next().type.getId());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void castX0Y3Up(){
        Ray<Tile> iterator = world.castRay(0, 3, Direction.UP);
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(LEAVES, iterator.next().type.getId());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(LEAVES, iterator.next().type.getId());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(AIR, iterator.next().type.getId());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void castX2Y1Left(){
        Ray<Tile> iterator = world.castRay(2, 2, Direction.LEFT);
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(WOOD, iterator.next().type.getId());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(LEAVES, iterator.next().type.getId());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void castX0Y0Right(){
        Ray<Tile> iterator = world.castRay(0, 0, Direction.RIGHT);
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(LEAVES, iterator.next().type.getId());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(AIR, iterator.next().type.getId());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(TORCH, iterator.next().type.getId());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(AIR, iterator.next().type.getId());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void castX0Y0A60(){
        Ray<Tile> iterator = world.castRay(0, 0, Math.toRadians(60));
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(LEAVES, iterator.next().type.getId());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(WOOD, iterator.next().type.getId());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(WOOD, iterator.next().type.getId());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(SAND, iterator.next().type.getId());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(STONE, iterator.next().type.getId());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(COBBLE, iterator.next().type.getId());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(IRON_ORE, iterator.next().type.getId());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(STONE, iterator.next().type.getId());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void castX1Y2A60(){
        Ray<Tile> iterator = world.castRay(1, 2, Math.toRadians(60));
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(SAND, iterator.next().type.getId());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(STONE, iterator.next().type.getId());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(COBBLE, iterator.next().type.getId());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(IRON_ORE, iterator.next().type.getId());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(STONE, iterator.next().type.getId());
        Assert.assertFalse(iterator.hasNext());
    }

}
