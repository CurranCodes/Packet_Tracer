//For parsing a routing table as a JSON string
package com.example.networksimulator;

public class RoutingTable {

    //Instance variables
    String routerName;
    RoutingTableRow[] rows;

    //RoutingTable constructor

    public RoutingTable(String routerName, RoutingTableRow[] rows) {
        this.routerName = routerName;
        this.rows = rows;
    }

    @Override
    public String toString(){
        String routingTable = "\n\nrouterName: " + routerName;

        for (RoutingTableRow row : rows){
            routingTable += "\n\n" + row;
        }

        return routingTable;
    }
}
