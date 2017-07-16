package com.lordstephen77.dreamblocks;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Created by Александр on 16.07.2017.
 */
public class Ray<T> implements Iterator<T> {
    private T[][] scope;
    private double angleInRad;
    private int x0;
    private int x;
    private int y;
    private int nextX;
    private int nextY;

    /**
     * @param scope      - array of items to return
     * @param x0         - initial x of ray
     * @param y0         - initial y of ray
     * @param angleInRad - angleInRad between horizontal axis(X) and ray
     */
    public Ray(T[][] scope, int x0, int y0, double angleInRad) {
        this.scope = scope;
        reset(x0, y0, angleInRad);
    }

    public Ray<T> reset(int x0, int y0, double angleInRad){
        this.x0 = x0;
        this.x = x0;
        this.y = y0;
        this.nextX = x;
        this.nextY = y;
        this.angleInRad = angleInRad;
        return this;
    }

    public double getA() {
        return Math.tan(angleInRad);
    }

    private void updateNextXY() {
        int tY;
        if (angleInRad < Math.toRadians(180)) {
            tY = y + 1;
        } else {
            tY = y - 1;
        }
        double tX = tY / getA() + x0;
        double delta = tX - x;
        if (Math.abs(delta) < 1) {//nextX ~= x
            nextX = x;
            nextY = tY;

        } else if (0 < delta) {
            nextX = x + 1;
            nextY = y;

        } else if (delta < 0) {
            nextX = x - 1;
            nextY = y;

        } else {
            throw new IllegalStateException();
        }
    }

    public int getNextX() {
        return nextX;
    }

    public int getNextY() {
        return nextY;
    }

    private boolean isOutOfScope(int x, int y){
        return x < 0 || x >= scope.length || y < 0 || y >= scope[0].length;
    }

    @Override
    public boolean hasNext() {
        updateNextXY();
        return !isOutOfScope(this.nextX, this.nextY);
    }

    @Override
    public T next() {
        updateNextXY();
        x = this.nextX;
        y = this.nextY;
        return scope[x][y];
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        throw new UnsupportedOperationException();
    }

    /**
     * Function is used to find possible source of ray cast at target point
     * @param x - target x
     * @param y - target y
     * @param angleInRad - angle between horizontal axis(X) and ray
     * @return x at y=0
     */
    public static int getX0of(int x, int y, double angleInRad){
        return (int) (x - y / Math.tan(angleInRad));
    }
}
