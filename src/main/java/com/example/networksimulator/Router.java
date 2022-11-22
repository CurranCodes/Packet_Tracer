// In progress router class to represent router instances
// Curran Fitzgerald

package com.example.networksimulator;

import java.util.ArrayList;
public class Router {
    private String deviceName;
    private ArrayList<String> connections;
    private ArrayList<ArrayList<String>> routingTable;

    Router(String deviceName, ArrayList<String> connections){
        this.deviceName = deviceName;
        this.connections = connections;
    }

    public void route(){

    }
}
