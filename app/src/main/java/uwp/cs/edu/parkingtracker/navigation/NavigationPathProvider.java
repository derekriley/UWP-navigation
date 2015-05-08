/*
 * Copyright 2014 University Of Wisconsin Parkside
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uwp.cs.edu.parkingtracker.navigation;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by Able on 4/28/2015.
 */
public class NavigationPathProvider implements PathProvider {

    HashMap <String, Vertex> nodeMap = NodeParser.getNodeMap();
    HashMap <LatLng, Vertex> latLngNodeMap = NodeParser.getLatLngNodeMap();

    @Override
    public PolylineOptions getPath(LatLng start, LatLng finish) {

        // computes all possible paths from starting vertex
        computePaths(latLngNodeMap.get(start));
        // holds a list of vertices that make up the shortest path between start and finish
        List <Vertex> path = getShortestPathTo(latLngNodeMap.get(finish));

        PolylineOptions options = new PolylineOptions();

        // add the LatLngs of each vertex in the path to the polyline
        for (int i = 0; i < path.size(); i++) {
            LatLng cords = new LatLng(path.get(i).lat, path.get(i).lon);
            options.add(cords);
        }

        options.color(Color.BLUE);
        return options;
    }

    public PolylineOptions getPath (String start, String finish) {

        // computes all possible paths from starting vertex
        computePaths(nodeMap.get(start));
        // holds a list of vertices that make up the shortest path between start and finish
        List <Vertex> path = getShortestPathTo(nodeMap.get(finish));

        PolylineOptions options = new PolylineOptions();

        // add the LatLngs of each vertex in the path to the polyline
        for (int i = 0; i < path.size(); i++) {
            LatLng cords = new LatLng(path.get(i).lat, path.get(i).lon);
            options.add(cords);
        }

        options.color(Color.BLUE);
        return options;

    }

    public static void computePaths(Vertex source) {
        source.minDistance = 0.;
        PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
        vertexQueue.add(source);

        while (!vertexQueue.isEmpty()) {
            Vertex current = vertexQueue.poll();

            // Visit each edge exiting current
            for (Edge edge : current.adjacencies) {
                Vertex neighbor = edge.target;
                double weight = edge.weight;
                double distanceThroughU = current.minDistance + weight;
                if (distanceThroughU < neighbor.minDistance) {
                    vertexQueue.remove(current);

                    neighbor.minDistance = distanceThroughU;
                    neighbor.previous = current;
                    vertexQueue.add(neighbor);
                }
            }
        }
    }

    public static List<Vertex> getShortestPathTo(Vertex target) {
        List<Vertex> path = new ArrayList<Vertex>();
        for (Vertex vertex = target; vertex != null; vertex = vertex.previous) {
            path.add(vertex);
        }

        Collections.reverse(path);
        path.remove(0);
        return path;
    }
}
