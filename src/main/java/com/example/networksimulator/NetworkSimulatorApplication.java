// Our main driver for our whole spring boot application!

package com.example.networksimulator;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;



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

        System.out.println(network);

        Gson gson = new Gson();
        String graphJSON = gson.toJson(network.toNetworkGraph());
        System.out.println(graphJSON);

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
    @GetMapping("/deleteRouter")
    public String deleteRouter(@RequestParam(name = "ID") String routerName) {
        network.deleteRouter(routerName);
        return getNetworkJson();
    }

    public static String getNetworkJson(){
        Gson gson = new Gson();
        String graphJSON = gson.toJson(network.toNetworkGraph());
        return graphJSON;
    }

}
