package com.lordstephen77.dreamblocks.light;

import com.lordstephen77.dreamblocks.*;

/**
 * Created by Александр on 08.07.2017.
 */
public class Sun extends LightingEngine {
    private Ray<Tile> ray;

    public Sun(World world){
        this.world = world;
        this.width = world.width;
        this.height = world.height;
        this.tiles = world.tiles;
        lightValues = new int[width][height];
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                lightValues[x][y] = 0;
            }
        }
        ray = world.castRay(0, 0, Direction.DOWN_RIGHT);
        for (int x = 0; x < width; x++) {
            castSun(x, 0);
        }
        for (int y = 0; y < getGroundY(0); y++){
            castSun(0, y);
        }
    }

    public void castSun(int x, int y){
        updateLightValues(ray.reset(x, y, Direction.DOWN_RIGHT.angleInRad));
        updateLightValues(ray.reset(x, y, Math.toRadians(50)));
        updateLightValues(ray.reset(x, y, Math.toRadians(55)));
        updateLightValues(ray.reset(x, y, Math.toRadians(60)));
        updateLightValues(ray.reset(x, y, Math.toRadians(65)));
        updateLightValues(ray.reset(x, y, Math.toRadians(70)));
    }

    public void removedTile(int px, int py) {
        addedTile(px, py);
    }

    public void addedTile(int px, int py) {
        for (int angleInDeg = 45; angleInDeg < 70; angleInDeg+=5){
            int x0 = Ray.getX0of(px, py, Math.toRadians(angleInDeg));
            resetLightValues(ray.reset(x0, 0, Math.toRadians(angleInDeg)), py);
            //To restore light values, you need to cast more
            for (int x = px - 5; x < px  + 5; x++){
                int targetX = Ray.getX0of(x, py, Math.toRadians(angleInDeg));
                updateLightValues(ray.reset(targetX, 0, Math.toRadians(angleInDeg)));
            }
        }
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

    private void updateLightValues(Ray<Tile> ray){
        lightValues[ray.getNextY()][ray.getNextY()] = LightingEngine.DIRECT_SUN;
        Tile next;
        if (ray.hasNext()) {
            do {
                next = ray.next();
                int px = ray.getNextX();
                int py = ray.getNextY();
                if (lightValues[px][py] < LightingEngine.DIRECT_SUN) {
                    lightValues[px][py] += 3;
                }
            }
            while (ray.hasNext() && next.type.getOpacity() == Constants.TRANSPARENT);
            if(ray.hasNext()){
                ray.next();
                int px = ray.getNextX();
                int py = ray.getNextY();
                if (lightValues[px][py] < LightingEngine.DIRECT_SUN) {
                    lightValues[px][py] += 3;
                }
            }
        }
    }

    private void resetLightValues(Ray<Tile> ray, int y){
        lightValues[ray.getNextY()][ray.getNextY()] = LightingEngine.DIRECT_SUN;
        Tile next;
        if (ray.hasNext()) {
            do {
                next = ray.next();
                int px = ray.getNextX();
                int py = ray.getNextY();
                if (py > y) {
                    lightValues[px][py] = 0;
                }
            }
            while (ray.hasNext() && next.type.getOpacity() == Constants.TRANSPARENT);
            if(ray.hasNext()){
                ray.next();
                int px = ray.getNextX();
                int py = ray.getNextY();
                if (py > y) {
                    lightValues[px][py] = 0;
                }
            }
        }
    }
}
