package com.lordstephen77.dreamblocks;

/**
 * Created by Александр on 04.07.2017.
 */
public enum Direction {
    UP_LEFT(-1, -1, 225), UP(0, -1, 270), UP_RIGHT(1, -1, 315),
    LEFT(-1, 0, 180), SOURCE(0, 0, 0), RIGHT(1, 0, 0),
    DOWN_LEFT(-1, 1, 135), DOWN(0, 1, 90), DOWN_RIGHT(1, 1, 45),

    UNKNOWN(Integer.MAX_VALUE, Integer.MAX_VALUE, Double.MAX_VALUE);

    public final int dx;
    public final int dy;
    public final double angleInRad;

    /**
     * @param dx - difference between nearest blocks by x
     * @param dy - difference between nearest blocks by y
     * @param angleInDeg - angle of associated slope
     */
    Direction(int dx, int dy, double angleInDeg) {
        this.dx = dx;
        this.dy = dy;
        this.angleInRad = Math.toRadians(angleInDeg);
    }
}
