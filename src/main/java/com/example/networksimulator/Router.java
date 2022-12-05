// Router class to represent router instances
// Curran Fitzgerald

package com.example.networksimulator;

import java.lang.reflect.Array;
import java.util.*;

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
    private static final double EPS = 1e-6;

    Router(String deviceName, Network parentNetwork){
        this.parentNetwork = parentNetwork;
        this.deviceName = deviceName;
        this.neighbors = new ArrayList<Router>();
        this.costList = new ArrayList<String[]>();
    }

    /** This method is for you to implement Maysam! **/
    //encapsulated your work within the route method as was expected
    //also you do not populate the routing table anywhere in your work
   public void route(Router[] routers){
       //commented your current implementation out so we can iterate upon it
//       class Edge {
//           double cost;
//           int from, to;
//           public Edge(int from, int to, double cost) {
//               this.from = from;
//               this.to = to;
//               this.cost = cost;
//           }
//       }
//       class Node {
//           int id;
//           double value;
//
//           public Node(int id, double value) {
//               this.id = id;
//               this.value = value;
//           }
//
//           public Node(String deviceName, String router) {
//           }
//       }
//
//       int n;
//       double[] dist;
//       Integer[] prev;
//       List<List<Edge>> graph;
//
//       Comparator<Node> comparator = new Comparator<Node>() {
//           @Override
//           public int compare(Node R1, Node R2) {
//               return 0;
//           }
//
//           //Seems to be static, needs to be updated
//           //    -Curran
//           public int compare(Node nodeR1, Node nodeR2, Node nodeR3, Node nodeR4, Node nodeR5) {
//               if (Math.abs(nodeR1.value - nodeR2.value - nodeR3.value - nodeR4.value - nodeR5.value) < EPS) return 0;
//               return (nodeR1.value - nodeR2.value - nodeR3.value - nodeR4.value - nodeR5.value) > 0 ? +1 : -1;
//           }
//       };

       //Keeps track of the current minimum distance between our node and another
//       int[][] distanceMatrix = new int[routers.length][2];
//       int index = 0;
//
//       for (int[] distanceEntry : distanceMatrix){
//           distanceEntry[0] = routers[index].getDeviceName();
//           if(routers[index] != this) {
//               distanceEntry[2] = Integer.toString(Integer.MAX_VALUE);//"infinity"
//           } else{
//               distanceEntry[2] = "0";
//           }
//       }
//
//
//       for (Router dest : routers){
//           if
//       }

       //first entry is routerName second entry is it's corresponding shortestPath
       //Keeps track of the finalPaths of a certain Node
       String[][] finalPaths = new String[routers.length][2];

       //priority queue that will give us the router with the least distance first
       ArrayList<RouterDistance> notFinalized = new ArrayList<>();

       int index = 0;

       //adds all routers that are not this one, to notFinalized.
       for(Router r : routers){
           //instantiate a path for each router in finalPaths
               finalPaths[index][0] = r.getDeviceName();
               finalPaths[index][1] = "";

           //updates notFinalized for each router accordingly
               if (r != this) notFinalized.add(new RouterDistance(r, 99999999));
               else notFinalized.add(new RouterDistance(r, 0));

         index++;
       }

       //runs until we have finalized our path/distance of each router
       while (!notFinalized.isEmpty()){
           notFinalized.sort(new AscendingOrder());
           RouterDistance rd = notFinalized.remove(0);
           Router currRouter = rd.getR();
           int currDistance = rd.getDistance();

           ArrayList<Router> currRouterNeighbors = currRouter.getNeighbors();

           //converts notFinalized to an array for random access
           RouterDistance[] rdArr = new RouterDistance[notFinalized.size()];
           rdArr = notFinalized.toArray(rdArr);

           for (Router neighbor : currRouterNeighbors){
               for(int i = 0; i < rdArr.length; i++){
                   //enters only if the current neighbor is not finalized
                   //AND the router distance is strictly grater than the distance
                   // of our current router plus the edge
                   if (rdArr[i].getR() == neighbor &&
                           ((currRouter.getCost(neighbor) + currDistance) < rdArr[i].getDistance()))
                   {
                       //update distance entry
                       rdArr[i].setDistance((currRouter.getCost(neighbor) + currDistance));

                       String[] pathToAdd = new String[2];
                       String[] pathToUpdate = new String[2];
                       int numFound = 0;
                       for (String[] currPath : finalPaths){
                           if (currPath[0].equals(neighbor.getDeviceName())){
                               pathToUpdate = currPath;
                               numFound++;
                           }
                           if (currPath[0].equals(currRouter.getDeviceName())){
                               pathToAdd = currPath;
                               numFound++;
                           }
                           if (numFound == 2){
                               break;
                           }
                       }

                       //updates our path
                       pathToUpdate[1] = pathToAdd[1] + " " + neighbor.getDeviceName();
                   }
               }
           }
       }

       PriorityQueue<RoutingTableRow> rows = new PriorityQueue<>(new AscendingOrderByID());

       for (String[] path : finalPaths){
           String line;
           int cost;
           if (path[0].equals(getDeviceName())) {
               line = "N.A.";
               cost = 0;
           }else if(path[1].equals("")){
               line = "N.A.";
               cost = 1000000;
           } else {
               line = path[1].split(" ")[1];
               cost = getCost(line);
           }


           rows.add(new RoutingTableRow(path[0], line , cost));
       }

       RoutingTableRow[] rowsArr = new RoutingTableRow[routers.length];
       rowsArr = rows.toArray(rowsArr);

       routingTable = new RoutingTable(deviceName, rowsArr);

       System.out.println(routingTable);
   }

   public void clearRoutingTable(){
       routingTable = null;
    }


    public RoutingTable getRoutingTable() {

        //if routing table doesn't exist, create routingTable
        if (routingTable == null) {
            route(parentNetwork.getRouters());
        }

        return routingTable;
    }

    public ArrayList<Router> getNeighbors(){
        return neighbors;
    }


    public String getDeviceName() {
        return deviceName;
    }

    private int getDeviceNum(){
       return Integer.parseInt(deviceName.split("R")[1]);
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

    public Node toNode(){
           return new Node(deviceName);
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

    private int getCost(String routerName){
       for(String[] costEntry : costList){
           if (costEntry[0].equals(routerName)){
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
