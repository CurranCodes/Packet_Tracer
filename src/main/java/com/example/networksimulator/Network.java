// In progress Network Class to represent our entire network
// Curran Fitzgerald

package com.example.networksimulator;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class Network {
    ArrayList<Router> routers;

    public Network(){}

    public void createRouter(String routerName, ArrayList<String> edges){
        Router router = new Router(routerName, edges);
    }
}
