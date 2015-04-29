package uwp.cs.edu.parkingtracker.navigation;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Able on 4/28/2015.
 */
public class NavigationPathProvider implements PathProvider {

    @Override
    public PolylineOptions getPath(LatLng start, LatLng finish) {

        HashMap <LatLng, Vertex> nodeMap = NodeParser.getLatLngNodeMap();
        // computes all possible paths from starting vertex
        Dijkstra.computePaths(nodeMap.get(start));
        // holds a list of vertices that make up the shortest path between start and finish
        List <Vertex> path = Dijkstra.getShortestPathTo(nodeMap.get(finish));

        PolylineOptions options = new PolylineOptions();

        // add the LatLngs of each vertex in the path to the polyline
        for (int i = 0; i < path.size(); i++) {
            LatLng cords = new LatLng(path.get(i).lat, path.get(i).lon);
            options.add(cords);
        }

        options.color(Color.BLUE);
        return options;
    }
}
