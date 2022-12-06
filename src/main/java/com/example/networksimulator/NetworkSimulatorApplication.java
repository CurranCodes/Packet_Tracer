// Our main driver for our whole spring boot application!

package com.example.networksimulator;

import com.google.gson.Gson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;



@SpringBootApplication
@RestController
public class NetworkSimulatorApplication {
    public static Network network;
    public static String currentNetworkState;
    public static void main(String[] args) {

        //instantiates network if the initial config file can be found
        try {
            // replace this string argument with the file path of your choice
            // relative to the NetworkSimulator Directory
            network = new Network("InitialConfig.txt");
        } catch (Exception e){
            System.out.println(e);
        }

        /** PUT CODE BELOW HERE YOU WANT TO TEST**/
        // prints network config
        System.out.println(network);

        /** PUT CODE ABOVE HERE YOU WANT TO TEST**/

        //starts the application
        SpringApplication.run(NetworkSimulatorApplication.class, args);

    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/getNetworkGraph")
    public String postNetworkJson () throws IOException {
        return getNetworkJson();
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/getRoutingTables")
    public String getRoutingTables() {
        Gson gson = new Gson();
        return gson.toJson(network.routeAll());
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/createDevice")
    public String createDevice(@RequestParam(name = "deviceName") String routerName) throws Exception {
        try {
            network.createRouter(routerName);
        } catch (Exception e) {
            return e.getMessage();
        }
        network.routeAll();
        return "Success";
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/deleteDevice")
    public String deleteRouter(@RequestParam(name = "deviceName") String routerName) {
        network.deleteRouter(routerName);
        network.routeAll();
        return "Success";
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/createEdge")
    public String createEdge(@RequestParam(name = "source") String device1,
                             @RequestParam(name = "dest") String device2,
                             @RequestParam(name = "cost") String cost) {
        network.connectRouters(device1, device2, Integer.parseInt(cost));
        network.routeAll();
        return "Success";
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/deleteEdge")
    public String deleteEdge(@RequestParam(name = "r1") String device1,
                             @RequestParam(name = "r2") String device2) {
        network.disconnectRouters(device1, device2);
        network.routeAll();
        return "Success";
    }

    public static String getNetworkJson(){
        Gson gson = new Gson();
        return gson.toJson(network.toNetworkGraph());
    }

}
