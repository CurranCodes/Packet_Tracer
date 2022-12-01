// Our main driver for our whole spring boot application!

package com.example.networksimulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;



@SpringBootApplication
@RestController
public class NetworkSimulatorApplication {
    public static Network network;
    public static String currentNetworkState;
    public static void main(String[] args) throws Exception {
        network = new Network();
        System.out.println(network);
        SpringApplication.run(NetworkSimulatorApplication.class, args);
    }

    @GetMapping("/getNetwork")
    public void postNetwork (@RequestBody String networkEncoding ){
        System.out.println(networkEncoding);

    }

}
