// Router class to represent router instances
// Curran Fitzgerald

package com.example.networksimulator;

import java.util.ArrayList;
import java.util.Objects;

public class Router {
    Network parentNetwork;

    // a Router's unique identifier (e.g. R1, R2, R3, ..., Rn)
    private final String deviceName;

    // an ArrayList of Routers that represents the connections that an
    // instance of Router has to other Routers
    private final ArrayList<Router> neighbors;
    /** NOTE: Because this contains Router references, we can traverse the graph of the network with the above structure **/

    //an ArrayList of String arrays that contain a router name in index 0 and a costList in index 1
    private final ArrayList<String[]> costList;

    private RoutingTable routingTable;

    Router(String deviceName, Network parentNetwork){
        this.parentNetwork = parentNetwork;
        this.deviceName = deviceName;
        this.neighbors = new ArrayList<Router>();
        this.costList = new ArrayList<String[]>();
    }

    /** This method is for you to implement Maysam! **/
    public void route(Router[] routers){
        //TODO: construct a routing table dynamically given the values of our instance variables
        neighbors.size();

        //placeholder
        for(Router router: neighbors){
            System.out.println(router.deviceName);
        }

        this.routingTable = routingTable;
    }

    public RoutingTable getRoutingTable(){
        return routingTable;
    }

    public String getDeviceName() {
        return deviceName;
    }

    // "Removes" a connection associated with a given destination device and returns true
    // if the connection to said device was non-existent it returns false
    public boolean removeConnection(String deviceName){
        Router toRemove = null;
        for (Router neighbor : neighbors){
            if (neighbor.getDeviceName().equals(deviceName)) {
                toRemove = neighbor;
            }
        }

        if (toRemove == null) {
            //removes connection
            neighbors.removeIf(n -> (Objects.equals(n.getDeviceName(), deviceName)));

            //removes cost entry
            costList.removeIf(n -> (n[0].equals(deviceName)));

            //removes the connection and cost entry in the previously connected router
            toRemove.removeConnection(this.deviceName);
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

    public ArrayList<Router> getNeighbors(){
        return neighbors;
    }

    public Node toNode(){
        return new Node(deviceName, "Router");
    }

    //gets the cost of a certain edge
    private int getCost(Router router){
        for(String[] costEntry : costList){
            if (costEntry[0].equals(router.getDeviceName())){
                return Integer.parseInt(costEntry[1]);
            }
        }
        return -1;
    }

    @Override
    public String toString(){
        String router ="\n**** Router: "+ deviceName + " ****\n\n";
        router += "Number of Connections: " + neighbors.size() + "\n\n";

        for (String[] cost : costList){
            router += "Connected to " + cost[0] + ", cost is " + cost[1] + " milliseconds\n\n";
        }

        return router;
    }
}
