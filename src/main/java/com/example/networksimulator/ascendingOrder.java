package com.example.networksimulator;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

//id will be the value at index 0
//therefore we compare ONLY the distance at index 1
public class ascendingOrder implements Comparator<RouterDistance> {
    //sorts in ascending order by distance
    public int compare(RouterDistance a, RouterDistance b){
        return a.getDistance() - b.getDistance();
    }
}
