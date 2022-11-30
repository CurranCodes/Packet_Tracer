// In progress router class to represent router instances
// Curran Fitzgerald

package com.example.networksimulator;

import java.util.ArrayList;
public class Router {

    // a Router's unique identifier (e.g. R1, R2, R3, ..., Rn)
    private final String deviceName;

    // an ArrayList of Routers that represents the connections that an
    // instance of Router has to other Routers
    private ArrayList<Router> neighbors;
    /** NOTE: Because These are Router references we can traverse the graph of the network with the above structure**/

    //an ArrayList of String arrays that contain a router name in index 0 and a cost in index 1
    private ArrayList<String[]> cost;

    Router(String deviceName, ArrayList<Router> neighbors, ArrayList<String[]> cost){
        this.deviceName = deviceName;
        this.neighbors = neighbors;
        this.cost = cost;
    }

    public void route(){
        //TODO: construct a routing table dynamically given the values of our instance variables
    }

    public String getDeviceName() {
        return deviceName;
    }
}
