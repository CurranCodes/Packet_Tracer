//This structure allows for us to parse the routing table as a JSON object

package com.example.networksimulator;

public class RoutingTableRow {
    private final String destination, line;
    private final int cost;

    public RoutingTableRow(String destination, String line, int cost) {
        this.destination = destination;
        this.line = line;
        this.cost = cost;
    }

    @Override
    public String toString(){
        String routingTableRow = "Destination: ";
        routingTableRow += destination;
        routingTableRow += "\nLine: " + line;
        routingTableRow += "\nCost: " + cost;
        return routingTableRow;
    }
}
