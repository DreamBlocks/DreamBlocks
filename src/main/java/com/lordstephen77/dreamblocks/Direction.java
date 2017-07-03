package com.lordstephen77.dreamblocks;

/**
 * Created by Александр on 04.07.2017.
 */
public enum Direction {
    UP_LEFT(-1, -1), UP(0, -1), UP_RIGHT(1, -1),
    LEFT(-1, 0), SOURCE(0, 0), RIGHT(1, 0),
    DOWN_LEFT(-1, 1), DOWN(0, 1), DOWN_RIGHT(1, 1),

    UNKNOWN(Integer.MAX_VALUE, Integer.MAX_VALUE);

    public final int dx;
    public final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }
}
