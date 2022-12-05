package com.example.networksimulator;

public class RouterDistance {
    private final Router r;
    private int distance;

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
