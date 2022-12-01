// Network Class to represent our entire network
// Curran Fitzgerald

package com.example.networksimulator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Hashtable;

public class Network {
    private final ArrayList<Router> routers;
    private final Hashtable<Integer, Boolean> nameTaken;

    public Network() throws Exception {
        routers = new ArrayList<>();
        nameTaken = new Hashtable<>();
        //gets our file
        BufferedReader br = new BufferedReader(new FileReader("initialConfig.txt"));

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
            routers.add(new Router(routerName));

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

    public void createRouter(String routerName) throws Exception {
        Router router = new Router(routerName);
        routers.add(router);

        //the name is now taken
        addNewName(routerName);
    }

    public void deleteRouter(String routerName){
        Router router = getRouterPointer(routerName);
        ArrayList<Router> neighbors = router.getNeighbors();

        //disconnects all connections of the router to be deleted
        for(Router neighbor : neighbors){
            neighbor.removeConnection(routerName);
        }

        routers.remove(getRouterPointer(routerName));

        //The name is now free to use!
        removeName(routerName);
    }

    public Router getRouterPointer(String routerName){
        for (Router router : routers) {
            if (routerName.equals(router.getDeviceName())) {
                return router;
            }
        }
        return null;
    }

    public boolean connectRouters(String r1Name, String r2Name, int cost){
        return getRouterPointer(r1Name).addConnection(getRouterPointer(r2Name), cost);
    }

    //checks to make sure the router name is unique
    public boolean addNewName(String newName) throws Exception {
        int nameNumber = Integer.parseInt(newName.split("R")[1]);

        if ((!nameTaken.containsKey(nameNumber) || !nameTaken.get(nameNumber))){
            nameTaken.put(nameNumber, Boolean.TRUE);
            return true;
        } else if (nameTaken.get(nameNumber)){
            Exception e = new Exception("All Router Names Must be unique, the name \"" +
                    newName + "\" is already in use");
            throw e;
        }
        return false;
    }

    //deletes a name from "nameTaken" (should only be used in the event of router deletion)
    public void removeName(String name) {
        int nameNumber = Integer.parseInt(name.split("R")[1]);

        nameTaken.put(nameNumber, false);
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
