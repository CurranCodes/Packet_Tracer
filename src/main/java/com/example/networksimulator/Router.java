// In progress router class to represent router instances
// Curran Fitzgerald

package com.example.networksimulator;

import java.util.ArrayList;
public class Router {
    private String deviceName;
    private ArrayList<Router> neighbors;
    private ArrayList<String[]> cost;

    Router(String deviceName, ArrayList<Router> neighbors, ArrayList<String[]> cost){
        this.deviceName = deviceName;
        this.neighbors = neighbors;
        this.cost = cost;
    }

    public void route(){

    }

    public String getDeviceName() {
        return deviceName;
    }
}
