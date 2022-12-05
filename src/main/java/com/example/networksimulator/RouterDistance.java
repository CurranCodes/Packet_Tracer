package com.example.networksimulator;

import org.jetbrains.annotations.NotNull;

public class RouterDistance implements Comparable {
    private final Router r;
    public int distance;

    public RouterDistance (Router r, int distance){
        this.r = r;
        this.distance = distance;
    }

    public Router getR() {
        return r;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        return distance - ((RouterDistance)o).distance;
    }
}
