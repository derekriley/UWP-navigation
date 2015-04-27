package uwp.cs.edu.parkingtracker.navigation;


import java.util.ArrayList;


/**
 * Created by Able on 4/27/2015.
 */
class Vertex implements Comparable<Vertex> {

    public final String name;
    public ArrayList<Edge> adjacencies = new ArrayList();
    public ArrayList<String> neigh = new ArrayList();
    public double minDistance = Double.POSITIVE_INFINITY;
    public Vertex previous;
    public double lat;
    public double lon;

    public Vertex(String argName) {
        name = argName;
    }

    public String toString() {
        return name;
    }

    public int compareTo(Vertex other) {
        return Double.compare(minDistance, other.minDistance);
    }

}


