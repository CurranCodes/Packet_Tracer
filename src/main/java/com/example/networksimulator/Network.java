// Network Class to represent our entire network
// Curran Fitzgerald

package com.example.networksimulator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class Network {
    private final ArrayList<Router> routers;

    //contains keys for the edgeTable, keys are ordered by lowest id first
    private final Hashtable<String, Integer> edgeTable;
    private final Hashtable<Integer, Boolean> nameTaken;

    public Network(String filePath) throws Exception {
        routers = new ArrayList<>();
        nameTaken = new Hashtable<>();
        edgeTable = new Hashtable<>();

        //gets our file
        BufferedReader br = new BufferedReader(new FileReader(filePath));

        //gets the amount of routers from the first line of the file
        int numRouters = Integer.parseInt(br.readLine());

        //creates a string array for all router lines and stores the router lines
        //in the array
        String[] routerStrings = new String[numRouters];

        for (int i = 0; i < routerStrings.length; i++){
            routerStrings[i] = br.readLine();
            routerStrings[i].trim();

            //checks to make sure the text was formatted correctly before continuing
            if (!routerStrings[i].matches(
                    "(R[0-9]+: (\\(R[0-9]+, [0-9]+\\), )*\\(R[0-9]+, [0-9]+\\)).{0}"))
            {
                Exception e = new Exception ("Text File Not Formatted Correctly.");
                throw e;
            }
        }

        //gets all the router names and creates routers using the basic constructor
        for(String routerString : routerStrings){
            String routerName  = routerString.split(": \\(")[0];
            routers.add(new Router(routerName, this));

            //gets the number after R and sets the corresponding index to true (the name is now taken)
            addNewName(routerName);
        }

        //connects the routers to one another
        for(String routerString : routerStrings){

            String currRouterName  = routerString.split(": \\(")[0];

            String connectionString = routerString.split(": \\(")[1];
            connectionString.trim();
            connectionString = connectionString.substring(0, connectionString.length() - 1);
            String[] connections = connectionString.split("\\), \\(");


            for(String connection : connections){
                String destRouterName = connection.split(", ")[0];
                int cost = Integer.parseInt(connection.split(", ")[1]);
                connectRouters(currRouterName, destRouterName, cost);
            }
        }
    }

    public Router getRouter(String routerName){
        for(Router currentRouter : routers){
            if (currentRouter.getDeviceName().equals(routerName)){
                return currentRouter;
            }
        }

        return null;
    }

    public void createRouter(String routerName) throws Exception {
        Router router = new Router(routerName, this);
        routers.add(router);

        //the name is now taken
        addNewName(routerName);
    }

    public void deleteRouter(String routerName){
        Router router = getRouterPointer(routerName);
        assert router != null;
        ArrayList<Router> neighbors = router.getNeighbors();

        //disconnects all connections of the router to be deleted
        for(Router neighbor : neighbors){
            neighbor.removeConnection(routerName);
        }

        routers.remove(getRouterPointer(routerName));

        //The name is now free to use!
        removeName(routerName);
    }

    private Router getRouterPointer(String routerName){
        for (Router router : routers) {
            if (routerName.equals(router.getDeviceName())) {
                return router;
            }
        }
        return null;
    }

    //returns false if r1==r2 or if either router doesn't exist. else returns true
    public boolean connectRouters(String r1Name, String r2Name, int cost){
        //if one or both routers don't exist, stop
        if (!routersExist(r1Name, r2Name)){
            return false;
        }

        String edgeKey = generateEdgeKey(r1Name, r2Name);
        //names were the same so don't do anything
        if (edgeKey == null){
            return false;
        }

        String edge = getEdge(edgeKey);
        //if the connection does not exist create the connection
        if(edge == null){

            //adds the "edge" that represents our new connection
            edgeTable.put(edgeKey, cost);

            //connects the two routers and returns true if successful
            return getRouterPointer(r1Name).addConnection(getRouterPointer(r2Name), cost);
        }
        //if the connection does exist just update the cost
        else{
            editConnection(r1Name,r2Name,cost);
        }
        return false;
    }

    // returns false if one of the routers does not exist or the edge doesn't exist
    // otherwise, it disconnects the two routers
    public boolean disconnectRouters(String r1Name, String r2Name){
        if (!routersExist(r1Name, r2Name)) return false;

        String edgeKey = generateEdgeKey(r1Name, r2Name);
        String edge = getEdge(edgeKey);

        if (edgeTable.get(edge) != null){
            edgeTable.remove(edgeKey);
            return getRouterPointer(r1Name).removeConnection(r2Name);
        }
        else return false;
    }

    //changes the cost of a specific connection
    private boolean editConnection(String r1Name, String r2Name, int cost){
        disconnectRouters(r1Name, r2Name);
        return connectRouters(r1Name, r2Name, cost);
    }

    //checks to make sure the router name is unique
    private boolean addNewName(String newName) throws Exception {
        int nameNumber = getNameNum(newName);

        if ((!nameTaken.containsKey(nameNumber) || !nameTaken.get(nameNumber))){
            nameTaken.put(nameNumber, Boolean.TRUE);
            return true;
        } else if (nameTaken.get(nameNumber)){
            Exception e = new Exception("All router names must be unique, the name \"" +
                    newName + "\" is already in use.");
            throw e;
        }
        return false;
    }

    //deletes a name from "nameTaken" (should only be used in the event of router deletion)
    private void removeName(String name) {
        int nameNumber = getNameNum(name);

        nameTaken.put(nameNumber, false);
    }

    //gets the number in a routerName
    private int getNameNum(String name){
        return Integer.parseInt(name.split("R")[1]);
    }

    private String generateEdgeKey(String r1Name, String r2Name){
        int r1NameNum = getNameNum(r1Name);
        int r2NameNum = getNameNum(r2Name);
        String edgeKey = null;

        if (r1NameNum < r2NameNum){
            edgeKey = r1Name + "," + r2Name;
        } else if (r2NameNum < r1NameNum){
            edgeKey = r2Name + "," + r1Name;
        }

        return edgeKey;
    }

    //gets the object reference for a certain key in edgeTable
    private String getEdge(String edgeKey){
        Enumeration e = edgeTable.keys();

        while (e.hasMoreElements()) {
            String currKey = (String) e.nextElement();
            if (currKey.equals(edgeKey)) {
                return currKey;
            }
        }
        return null;
    }

    private boolean routersExist(String r1Name, String r2Name){
        int r1NameNum = getNameNum(r1Name);
        int r2NameNum = getNameNum(r2Name);
        if((nameTaken.get(r1NameNum) == null || !nameTaken.get(r1NameNum)) || (nameTaken.get(r2NameNum) == null || !nameTaken.get(r2NameNum))){
            return false;
        }
        return true;
    }

    public Router[] getRouters(){
        Router[] routerArr = new Router[routers.size()];
        return routers.toArray(routerArr);
    }

    public NetworkGraph toNetworkGraph(){
        Node[] nodes = new Node[routers.size()];
        int index = 0;

        for (Router router : routers){
            nodes[index] = router.toNode();
            index++;
        }

        Edge[] edges = new Edge[edgeTable.size()];
        index = 0;

        Enumeration<String> e = edgeTable.keys();

        while(e.hasMoreElements()){
            String currEdge = e.nextElement();
            String from = currEdge.split(",")[0];
            String to = currEdge.split(",")[1];
            String cost = Integer.toString(edgeTable.get(currEdge));

            edges[index] = new Edge(from, to, cost);

            index++;
        }

        return new NetworkGraph(nodes, edges);
    }

    //creates routing table on each router
    public RoutingTable[] routeAll(){
        clearAllRoutingTables();//clears all the routing tables before creating new ones
        Router[] routerArr = new Router[routers.size()];
        routerArr = routers.toArray(routerArr);
        RoutingTable[] allTables = new RoutingTable[routers.size()];
        int index = 0;

        for(Router router : routers){
            router.route(routerArr);
            allTables[index] = router.getRoutingTable();
            index++;
        }

        return allTables;
    }

    private void clearAllRoutingTables(){
        for (Router router: routers){
            router.clearRoutingTable();
        }
    }

    @Override
    public String toString(){
        String network = "\n\n======= Start Initial Network ======\n\n";

        for (Router router : routers){
            network += router + "\n";
        }

        network += "======== End Initial Network =======\n\n";

        return network;
    }
}
