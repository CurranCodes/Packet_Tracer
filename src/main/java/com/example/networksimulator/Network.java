// In progress Network Class to represent our entire network
// Curran Fitzgerald

package com.example.networksimulator;

import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

@RestController
public class Network {
    ArrayList<Router> routers;

    public Network() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("initialConfig.txt"));

        int numRouters = Integer.parseInt(br.readLine());

        String[] routerStrings = new String[numRouters];

        for (int i = 0; i < routerStrings.length; i++){
            routerStrings[i] = br.readLine();
        }



    }

    public void createRouter(String routerName, ArrayList<Router> connections, ArrayList<String[]> cost){
        Router router = new Router(routerName, connections, cost);
        routers.add(router);
    }

    public Router getRouterPointer(String routerName){
        for (int i = 0; i < routers.size(); i++){
            if (routerName.equals(routers.get(i).getDeviceName())){
                return routers.get(i);
            }
        }
        return null;
    }
}
