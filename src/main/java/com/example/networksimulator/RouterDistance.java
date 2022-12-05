package com.example.networksimulator;

import org.jetbrains.annotations.NotNull;

public class RouterDistance {
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
}
