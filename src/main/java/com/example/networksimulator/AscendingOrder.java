package com.example.networksimulator;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

//id will be the value at index 0
//therefore we compare ONLY the distance at index 1
public class AscendingOrder implements Comparator<RouterDistance> {
    //sorts in ascending order by distance
    public int compare(RouterDistance a, RouterDistance b){
        if (a.distance < b.distance){
            return -1;
        } else if (a.distance > b.distance){
            return 1;
        }
        return 0;
    }
}
