package com.example.networksimulator;

import org.json.simple.parser.JSONParser;

public class FileParser {
    JSONParser parser;
    String encoding;
    String fileName;

    public FileParser(String fileName, String encoding){
        this.fileName = fileName;
        this.encoding = encoding;
        parser = new JSONParser();
    }

    public String refresh(){
        if (fileName.equals("Network.json")){
            return refreshNetwork();
        } else {
            return null;
        }
    }

    public String refreshNetwork(){
        Network network = new Network();
        return null;
    }
}
