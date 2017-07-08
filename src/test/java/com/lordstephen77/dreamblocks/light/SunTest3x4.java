package com.lordstephen77.dreamblocks.light;

import com.lordstephen77.dreamblocks.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static com.lordstephen77.dreamblocks.Constants.*;
import static com.lordstephen77.dreamblocks.Constants.TileID.*;
import static com.lordstephen77.dreamblocks.light.LightingEngine.*;

/**
 * Created by Александр on 08.07.2017.
 */
public class SunTest3x4 {
    private World world;
    private Random rnd;
    private ProceduralWorldGenerator worldGenerator;
    private static TileStore tileStore = new TileStore();
    private LightingEngine sun;

    @Before
    public void setUp(){
        rnd = new Random();
        worldGenerator = new ProceduralWorldGenerator(3, 4, rnd);
        world = new World(3, 4, rnd);
    }

    private void printArr(int[][] arr){
        for (int i = 0; i < arr.length; i++){
            for (int j = 0; j < arr[0].length; j++){
                System.out.print(arr[i][j]);
                System.out.print(' ');
            }
            System.out.println();
        }
    }

    @Test
    public void flatSurface(){
        worldGenerator.setWorldData(new TileID[][]{
            {AIR, AIR, AIR, STONE},
            {AIR, AIR, AIR, STONE},
            {AIR, AIR, AIR, STONE}
        });
        world.fillWith(tileStore, worldGenerator);
        sun = new Sun(world);
        int[][] expectedValues = new int[][]{
            {DIRECT_SUN, DIRECT_SUN, DIRECT_SUN, INDIRECT_SUN},
            {DIRECT_SUN, DIRECT_SUN, DIRECT_SUN, INDIRECT_SUN},
            {DIRECT_SUN, DIRECT_SUN, DIRECT_SUN, INDIRECT_SUN}
        };
        printArr(sun.lightValues);
        Assert.assertArrayEquals(expectedValues, sun.lightValues);
    }

    @Test
    public void onlyRightColumnHigherOneBlock(){
        worldGenerator.setWorldData(new TileID[][]{
            {AIR, AIR, AIR, STONE},
            {AIR, AIR, AIR, STONE},
            {AIR, AIR, STONE, STONE}
        });
        world.fillWith(tileStore, worldGenerator);
        sun = new Sun(world);
        int[][] expectedValues = new int[][]{
            {DIRECT_SUN, DIRECT_SUN, DIRECT_SUN, INDIRECT_SUN},
            {DIRECT_SUN, DIRECT_SUN, DIRECT_SUN, INDIRECT_SUN},
            {DIRECT_SUN, DIRECT_SUN, INDIRECT_SUN, DARKNESS}
        };
        printArr(sun.lightValues);
        Assert.assertArrayEquals(expectedValues, sun.lightValues);
    }

    @Test
    public void onlyRightColumnHigherTwoBlocks(){
        worldGenerator.setWorldData(new TileID[][]{
            {AIR, AIR, AIR, STONE},
            {AIR, AIR, AIR, STONE},
            {AIR, STONE, STONE, STONE}
        });
        world.fillWith(tileStore, worldGenerator);
        sun = new Sun(world);
        int[][] expectedValues = new int[][]{
            {DIRECT_SUN, DIRECT_SUN, DIRECT_SUN, INDIRECT_SUN},
            {DIRECT_SUN, DIRECT_SUN, DIRECT_SUN, INDIRECT_SUN},
            {DIRECT_SUN, INDIRECT_SUN, INDIRECT_SUN, DARKNESS}
        };
        printArr(sun.lightValues);
        Assert.assertArrayEquals(expectedValues, sun.lightValues);
    }

    @Test
    public void onlyMiddleColHigherOneBlock(){
        worldGenerator.setWorldData(new TileID[][]{
            {AIR, AIR, AIR, STONE},
            {AIR, AIR, STONE, STONE},
            {AIR, AIR, AIR, STONE}
        });
        world.fillWith(tileStore, worldGenerator);
        sun = new Sun(world);
        int[][] expectedValues = new int[][]{
            {DIRECT_SUN, DIRECT_SUN, DIRECT_SUN, INDIRECT_SUN},
            {DIRECT_SUN, DIRECT_SUN, INDIRECT_SUN, DARKNESS},
            {DIRECT_SUN, DIRECT_SUN, DIRECT_SUN, INDIRECT_SUN}
        };
        printArr(sun.lightValues);
        Assert.assertArrayEquals(expectedValues, sun.lightValues);
    }

    @Test
    public void onlyMiddleColHigherTwoBlocks(){
        worldGenerator.setWorldData(new TileID[][]{
            {AIR, AIR, AIR, STONE},
            {AIR, STONE, STONE, STONE},
            {AIR, AIR, AIR, STONE}
        });
        world.fillWith(tileStore, worldGenerator);
        sun = new Sun(world);
        int[][] expectedValues = new int[][]{
            {DIRECT_SUN, DIRECT_SUN, DIRECT_SUN, INDIRECT_SUN},
            {DIRECT_SUN, INDIRECT_SUN, INDIRECT_SUN, DARKNESS},
            {DIRECT_SUN, DIRECT_SUN, DIRECT_SUN, INDIRECT_SUN}
        };
        printArr(sun.lightValues);
        Assert.assertArrayEquals(expectedValues, sun.lightValues);
    }
    
    @Test
    public void OnlyLeftColHigherOneBlock(){
        worldGenerator.setWorldData(new TileID[][]{
            {AIR, AIR, STONE, STONE},
            {AIR, AIR, AIR, STONE},
            {AIR, AIR, AIR, STONE}
        });
        world.fillWith(tileStore, worldGenerator);
        sun = new Sun(world);
        int[][] expectedValues = new int[][]{
            {DIRECT_SUN, DIRECT_SUN, INDIRECT_SUN, DARKNESS},
            {DIRECT_SUN, DIRECT_SUN, DIRECT_SUN, INDIRECT_SUN},
            {DIRECT_SUN, DIRECT_SUN, DIRECT_SUN, INDIRECT_SUN}
        };
        printArr(sun.lightValues);
        Assert.assertArrayEquals(expectedValues, sun.lightValues);
    }
    
    @Test
    public void onlyLeftColHigherTwoBlocks(){
        worldGenerator.setWorldData(new TileID[][]{
            {AIR, STONE, STONE, STONE},
            {AIR, AIR, AIR, STONE},
            {AIR, AIR, AIR, STONE}
        });
        world.fillWith(tileStore, worldGenerator);
        sun = new Sun(world);
        int[][] expectedValues = new int[][]{
            {DIRECT_SUN, INDIRECT_SUN, INDIRECT_SUN, DARKNESS},
            {DIRECT_SUN, DIRECT_SUN, DIRECT_SUN, INDIRECT_SUN},
            {DIRECT_SUN, DIRECT_SUN, DIRECT_SUN, INDIRECT_SUN}
        };
        printArr(sun.lightValues);
        Assert.assertArrayEquals(expectedValues, sun.lightValues);
    }
    
    @Test
    public void rightOneLeftOne(){
        worldGenerator.setWorldData(new TileID[][]{
            {AIR, AIR, STONE, STONE},
            {AIR, AIR, AIR, STONE},
            {AIR, AIR, STONE, STONE}
        });
        world.fillWith(tileStore, worldGenerator);
        sun = new Sun(world);
        int[][] expectedValues = new int[][]{
            {DIRECT_SUN, DIRECT_SUN, INDIRECT_SUN, DARKNESS},
            {DIRECT_SUN, DIRECT_SUN, DIRECT_SUN, INDIRECT_SUN},
            {DIRECT_SUN, DIRECT_SUN, INDIRECT_SUN, DARKNESS}
        };
        printArr(sun.lightValues);
        Assert.assertArrayEquals(expectedValues, sun.lightValues);
    }
    
    @Test
    public void rightTwoLeftOne(){
        worldGenerator.setWorldData(new TileID[][]{
            {AIR, AIR, STONE, STONE},
            {AIR, AIR, AIR, STONE},
            {AIR, STONE, STONE, STONE}
        });
        world.fillWith(tileStore, worldGenerator);
        sun = new Sun(world);
        int[][] expectedValues = new int[][]{
            {DIRECT_SUN, DIRECT_SUN, INDIRECT_SUN, DARKNESS},
            {DIRECT_SUN, DIRECT_SUN, DIRECT_SUN, INDIRECT_SUN},
            {DIRECT_SUN, INDIRECT_SUN, INDIRECT_SUN, DARKNESS}
        };
        printArr(sun.lightValues);
        Assert.assertArrayEquals(expectedValues, sun.lightValues);
    }
    
    @Test
    public void rightTwoLeftTwo(){
        worldGenerator.setWorldData(new TileID[][]{
            {AIR, STONE, STONE, STONE},
            {AIR, AIR, AIR, STONE},
            {AIR, STONE, STONE, STONE}
        });
        world.fillWith(tileStore, worldGenerator);
        sun = new Sun(world);
        int[][] expectedValues = new int[][]{
            {DIRECT_SUN, INDIRECT_SUN, INDIRECT_SUN, DARKNESS},
            {DIRECT_SUN, DIRECT_SUN, DIRECT_SUN, INDIRECT_SUN},
            {DIRECT_SUN, INDIRECT_SUN, INDIRECT_SUN, DARKNESS}
        };
        printArr(sun.lightValues);
        Assert.assertArrayEquals(expectedValues, sun.lightValues);
    }

}
