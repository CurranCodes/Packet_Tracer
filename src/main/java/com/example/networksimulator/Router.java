// In progress router class to represent router instances
// Curran Fitzgerald

package com.example.networksimulator;

import java.util.ArrayList;

public class Router {
    private String deviceName;
    private ArrayList<String> connections;

    public Router(String deviceName){
        this.deviceName = deviceName;
        connections = new ArrayList<String>(5);
    }

    public void addConnection (String deviceName){
        connections.add(deviceName);
    }

    public String getDeviceName() {
        return deviceName;
    }

    public ArrayList<String> getConnections() {
        return (ArrayList<String>) connections.clone();
    }

    public void updateConnections(ArrayList<String> newConnections){
        connections = newConnections;
    }
}
