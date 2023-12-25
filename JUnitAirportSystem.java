import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class JUnitAirportSystem{

    // 1st JUnit Test
    public static void main(String[] args){
        AirportSystem a = new AirportSystem();
        // Adding the Cities
        String[] cities = {"RDU", "JFK", "EWR", "SFO", "ABQ", "CLE", "LAX", "DFW"};
        for (String city : cities) {
            a.addVertex(city);
        }
    
         //Adding the Connections
         a.addEdge("RDU", "SFO", 650); a.addEdge("RDU", "JFK", 150);
         a.addEdge("RDU", "ABQ", 400); a.addEdge("RDU", "EWR", 125);
         a.addEdge("RDU", "CLE", 250); a.addEdge("RDU", "LAX", 575);
         a.addEdge("LAX", "SFO", 150); a.addEdge("LAX", "JFK", 600);
         a.addEdge("DFW", "ABQ", 150); a.addEdge("RDU", "DFW", 225);
         a.addEdge("DFW", "CLE", 250); a.addEdge("DFW", "LAX", 175);
         a.addEdge("JFK", "SFO", 575); a.addEdge("CLE", "JFK", 150);
         a.addEdge("DFW", "EWR", 275); a.addEdge("CLE", "ABQ", 350);
         a.addEdge("SFO", "CLE", 450); a.addEdge("EWR", "LAX", 550);  a.addEdge("DFW", "SFO", 225);
         a.addEdge("ABQ", "JFK", 325); a.addEdge("EWR", "ABQ", 315);
         a.addEdge("RDU", "ABQ", 400); a.addEdge("RDU", "EWR", 125);
         a.addEdge("JFK", "EWR", 10); a.addEdge("ABQ", "LAX", 125);
         a.addEdge("LAX", "CLE", 350); a.addEdge("SFO", "EWR", 590);
        // Print the results
        System.out.print("Minimum Spanning Tree: ");
        System.out.println(a.minimumSpanningTree());
        System.out.print('\n');
    
        System.out.println("Graph:");
        a.printGraph();
        System.out.print('\n');
    
       int distanceAB = a.shortestDistance("RDU", "SFO");
       assertEquals(450, distanceAB);
       System.out.println("Distance between RDU and SFO is: " + distanceAB);
       int distanceAC = a.shortestDistance("CLE", "");
       assertEquals(3, distanceAC);
       System.out.println("Distance between cityA and cityC is: " + distanceAC);

    
        System.out.print("Breadth First Search from RDU: ");
        System.out.println(a.breadthFirstSearch("RDU"));
    }
}
