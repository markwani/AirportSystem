import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Comparator;

/*
 * This is the class that represents an Airport System
 * It can calculate the shortest routes between airports via three different algorithms
 * It can set up an airpot hub using the vertex and the distance using an edge
 * There is a nested Vertex and Edge class
 * 
 * @author Mark Wani
 * caseid = mmw158
 */
public class AirportSystem {

    private List<Vertex> connections = new ArrayList<Vertex>();

    /**
     * This is the method for adding an edge to the graph
     * @param source the vertex which will be the starting point
     * @param destination the vertex which is the ending vertex
     * @param weight the weight of the edge 
     * @return boolean if it's added then it returns true, else it returns false
     */
    public boolean addEdge(String source, String destination, int weight) {
        // case 1: the weight is negative cannot be added due to dijkstras only takes positive weights
        if (weight <= -1) {
            System.out.println("Invalid weight");
            return false;
        }

        Vertex sourceVertex = findVertex(source);
        Vertex destinationVertex = findVertex(destination);

        // case 2: checks to make sure both the vertices exist
        if (sourceVertex == null || destinationVertex == null) {
            System.out.println("One or both vertices not found");
            return false;
        }

        // case 3: checks to make sure that both the vertices aren't already connected. Avoiding duplicates
        if (sourceVertex.containsEdge(destination)) {
            System.out.println("Edge already exists");
            return false;
        }

        // Add edge to both vertices for an undirected graph if everything else passes
        sourceVertex.addEdge(destination, weight);
        destinationVertex.addEdge(source, weight);
        return true;
    }

        /**
         * This is the method which checks which distance is the shortest via Dijkstra's Algorithm
         * @param cityA the starting source node 
         * @param cityB the ending source node 
         * @return the int of the shortest distance  
         */
    public int shortestDistance(String cityA, String cityB) {
        Map<String, Integer> distances = new HashMap<>();
        PriorityQueue<String> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));
        Set<String> visited = new HashSet<>();

        //checks each vertex and retrives the 
        for (Vertex vertex : connections) {
            distances.put(vertex.getID(), Integer.MAX_VALUE);
        }
        distances.put(cityA, 0);

        queue.add(cityA);

        while (!queue.isEmpty()) {
            String currentID = queue.poll();
            visited.add(currentID);

            if (currentID.equals(cityB)) {
                break;
            }

            Vertex currentVertex = findVertex(currentID);

            for (Edge edge : currentVertex.getEdges()) {
                String neighborID = edge.getDestination();
                if (!visited.contains(neighborID)) {
                    int newDist = distances.get(currentID) + edge.getDistance();
                    if (newDist < distances.get(neighborID)) {
                        distances.put(neighborID, newDist);
                        // Update the priority queue
                        queue.remove(neighborID); // Remove the old distance
                        queue.add(neighborID); // Add back with new distance
                    }
                }
            }
        }
        return distances.get(cityB);
    }

    /**
     * This is the minimum spanning tree method which uses Prim's algorithm to get the shortest path without creating a cycle
     * @return the list of Edges (n - 1) that creates the shortest spanning tree 
     */
    public List<Edge> minimumSpanningTree() {
        List<Edge> result = new ArrayList<>();
        Set<String> inMST = new HashSet<>();
        PriorityQueue<Edge> edgeQueue = new PriorityQueue<>();
    
        if (connections.isEmpty()) {
            return result;
        }
    
        // Start from the first vertex
        Vertex startVertex = connections.get(0);
        inMST.add(startVertex.getID());
        edgeQueue.addAll(startVertex.getEdges());
    
        // checks 
        while (!edgeQueue.isEmpty() && result.size() < connections.size() - 1) {
            Edge edge = edgeQueue.poll();
    
            // Check if the destination of the edge is already in MST
            if (inMST.contains(edge.getDestination())) {
                continue;
            }
    
            // Add the edge to the result and mark the destination as part of MST
            result.add(edge);
            inMST.add(edge.getDestination());
    
            // Add the edges of the new vertex to the queue
            Vertex nextVertex = findVertex(edge.getDestination());
            for (Edge adjacentEdge : nextVertex.getEdges()) {
                if (!inMST.contains(adjacentEdge.getDestination())) {
                    edgeQueue.add(adjacentEdge);
                }
            }
        }
    
        return result;
    }

        /**
         * This is the method for the BFS algorithm 
         * @param start the starting node at which the BFS will start the search at
         * @return a list of Strings 
         */
        public List<String> breadthFirstSearch(String start) {
            Map<String, Boolean> visited = new HashMap<>();

            // Initialize all vertices as not visited
            for (Vertex vertex : connections) {
                visited.put(vertex.getID(), false);
            }

            List<String> cities = new ArrayList<>();
            LinkedList<String> queue = new LinkedList<>();

            // Check if the start vertex exists in the graph
            Vertex startVertex = findVertex(start);
            if (startVertex == null) {
                System.out.println("Start vertex not found: " + start);
                return cities; // Return empty list or handle accordingly
            }

            // Start from the given vertex
            queue.add(start);
            visited.put(start, true);

            while (!queue.isEmpty()) {
                String city = queue.poll();
                cities.add(city);

                // Get the vertex corresponding to the city
                Vertex currentVertex = findVertex(city);
                
                // Iterate over all adjacent vertices
                for (Edge edge : currentVertex.getEdges()) {
                    String adjacentCity = edge.getDestination();
                    if (!visited.get(adjacentCity)) {
                        queue.add(adjacentCity);
                        visited.put(adjacentCity, true);
                    }
                }
            }
            return cities;
        }

    /**
     * This is the method that creates the graph to be seen
     */
    public void printGraph() {
        // checks every vertex inside the list of vertices and prints them out with the airport and the connections with the respective miles it takes to get there
        for (Vertex vertex : connections) {
            System.out.print("Airport " + vertex.getID() + " connects to: ");
            for (Edge edge : vertex.getEdges()) {
                System.out.print("(" + edge.getDestination() + ", " + edge.getDistance() + " miles) ");
            }
            System.out.println(); // Move to next line after listing all connections for a vertex
        }
    }
    
    /**
     * This is the method that adds vertex to the list of vertices a.k.a connections
     * @param inputID the string inputID which corresponds with the city that is the vertex
     * @return the vertex when it is added to the list
     */
    public Vertex addVertex(String inputID) {
        Vertex existingVertex = findVertex(inputID);
        if (existingVertex != null) {
            System.out.println("Vertex already exists");
            return existingVertex;
        } else {
            Vertex newVertex = new Vertex(inputID);
            connections.add(newVertex);
            return newVertex;
        }
    }

    /**
     * @param id
     * @return
     */
    private Vertex findVertex(String id) {
        for (Vertex vertex : connections) {
            if (vertex.getID().equals(id)) {
                return vertex;
            }
        }
        return null;
    }
    
    /*
     * This is the private nested class of Edge and it implements the comparable interface
     */
    public class Edge implements Comparable<Edge>{
        private String source;
        private String destination;
        private int distance ; 

        /**
         * This is the constructor for the Edge nested class
         * @param start this is the string for the starting Vertex  
         * @param end the string for the ending vertex
         * @param miles the number of miles of how long it takes to get from the start to the end
         */
        public Edge(String start, String end, int miles){
            this.source = start;
            this.destination = end;
            this.distance = miles;
        }

        @Override
        public String toString(){
            return "[" + source + ", " + destination + "]"; 
        }

        /**
         * This is the helper method to get the destination
         * @return the String that corresponds with the destination
         */
        private String getDestination(){
            return this.destination;
        }

        /**
         * The helper method to retrive the distance 
         * @return the int of the distance
         */
        public int getDistance(){
            return this.distance;
        }

        @Override
        public int compareTo(Edge e){
            if(this.distance <= e.distance)
                return -1;
            else   
                return 1;
        }
    }

    private class Vertex{
        private String id;
        private List<Edge> edges = new ArrayList<Edge>();

        /**
         * This is the constructor for the vertex class 
         * @param inputID it takes the name of the city that is created
         */
        public Vertex(String inputID){
            this.id = inputID;
        }
        
        /**
         * This is the method in which the edge is added to the list of edges
         * @param destination the destination a.k.a. the ending vertex
         * @param distance the distance or the weight of the edge
         */
        public void addEdge(String destination, int distance) {
            this.edges.add(new Edge(this.id, destination, distance));
        }

        /**
         * This is a method that checks to see if edges is within the list of edges
         * @param destination the String that matches with the destination
         * @return a boolean true or false
         */
        public boolean containsEdge(String destination) {
            // iterates through the list of edges if it finds it, it will return true
            for (Edge edge : this.edges) {
                if (edge.getDestination().equals(destination)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * This is the helper method to get each ID for the vertex
         * @return the String of the vertex 
         */
        private String getID(){
            return id;
        }
        
        /**
         * This is the method to get the edges from the vertex
         * @return the list of edges the vertex is connected to
         */
        private List<Edge> getEdges(){
          return this.edges;
        }

        @Override
        public String toString(){
           return id;
        }
    }
}