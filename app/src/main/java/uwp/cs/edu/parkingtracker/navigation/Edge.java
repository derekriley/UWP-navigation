package uwp.cs.edu.parkingtracker.navigation;

/**
 * Created by Able on 4/27/2015.
 */
class Edge {

    public final Vertex target;
    public final double weight;

    public Edge(Vertex argTarget, double argWeight) {
        target = argTarget;
        weight = argWeight;
    }
}
