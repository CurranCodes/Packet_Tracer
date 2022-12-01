// In progress router class to represent router instances
// Curran Fitzgerald

package com.example.networksimulator;

import java.util.ArrayList;
import java.util.Objects;

public class Router {

    // a Router's unique identifier (e.g. R1, R2, R3, ..., Rn)
    private final String deviceName;

    // an ArrayList of Routers that represents the connections that an
    // instance of Router has to other Routers
    private final ArrayList<Router> neighbors;
    /** NOTE: Because this contains Router references, we can traverse the graph of the network with the above structure **/

    //an ArrayList of String arrays that contain a router name in index 0 and a costList in index 1
    private final ArrayList<String[]> costList;

    private String[][] routingTable;

    Router(String deviceName, ArrayList<Router> neighbors, ArrayList<String[]> costList){
        this.deviceName = deviceName;
        this.neighbors = neighbors;
        this.costList = costList;
    }

    public void route(){
        //TODO: construct a routing table dynamically given the values of our instance variables
    }

    public String getDeviceName() {
        return deviceName;
    }

    // "Removes" a connection associated with a given destination device and returns true
    // if the connection to said device was non-existent it returns false
    public boolean removeConnection(String deviceName){
        for (Router neighbor : neighbors){
            if (neighbor.getDeviceName().equals(deviceName)) {
                //removes connection
                neighbors.removeIf(n -> (Objects.equals(n.getDeviceName(), deviceName)));

                //removes cost entry
                costList.removeIf(n -> (n[0].equals(deviceName)));

                //removes the connection and cost entry in the previously connected router
                neighbor.removeConnection(this.deviceName);

                //informs caller of successful operation
                return true;
            }
        }
        return false;
    }

    // "Connects" this router to another router if the connection does not already exist
    // returns true if operation is successful
    public boolean addConnection(Router r, int cost){
        // if the connection does not exist already (prevents infinite loop)
        if (!neighbors.contains(r)){
            //add the router reference to neighbors
            neighbors.add(r);

            //add a connection to this Router in Router r
            r.addConnection(this, cost);

            //create and enter the costEntry into costList
            String[] costEntry = {r.getDeviceName(), Integer.toString(cost)};
            costList.add(costEntry);

            //inform method caller of successful operation
            return true;
        }
        return false;
    }

    // Returns true if a "connection" exists for a given device name
    public boolean connectedTo(String deviceName){
        for (Router neighbor : neighbors) {
            if (neighbor.getDeviceName().equals(deviceName)) {
                return true;
            }
        }
        return false;
    }
}
