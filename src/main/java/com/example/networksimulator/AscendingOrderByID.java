package com.example.networksimulator;

import java.util.Comparator;

public class AscendingOrderByID implements Comparator<RoutingTableRow> {
    public int compare(RoutingTableRow a , RoutingTableRow b){
        return Integer.parseInt(a.getDestination().split("R")[1]) - Integer.parseInt(b.getDestination().split("R")[1]);
    }
}
