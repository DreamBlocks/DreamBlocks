package com.lordstephen77.dreamblocks.light;

import com.lordstephen77.dreamblocks.Direction;

import java.util.Comparator;

/**
 * Created by Александр on 08.07.2017.
 */
public class LightingPoint {

    public int x, y, lightValue;
    public Direction flow;

    public LightingPoint(int x, int y, Direction flow, int lightValue) {
        this.x = x;
        this.y = y;
        this.flow = flow;
        this.lightValue = lightValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LightingPoint that = (LightingPoint) o;

        if (x != that.x) return false;
        return y == that.y;
    }

    @Override
    public int hashCode() {
        return x * 13 + y * 17;

    }

    public static class LightValueComparator implements Comparator<LightingPoint> {
        @Override
        public int compare(LightingPoint arg0, LightingPoint arg1) {
            if (arg0.lightValue < arg1.lightValue) {
                return 1;
            } else if (arg0.lightValue > arg1.lightValue) {
                return -1;
            }
            return 0;
        }
    }
}
