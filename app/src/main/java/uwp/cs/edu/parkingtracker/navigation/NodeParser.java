package uwp.cs.edu.parkingtracker.navigation;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;


/**
 * Created by Able on 4/27/2015.
 */

public class NodeParser {

    // Column index of the building column.
    private static final int BUILDING = 0;

    // Column index of the node type column.
    private static final int NODE_TYPE = 1;

    // Column index of the node longitude column.
    private static final int NODE_LONG = 2;

    // Column index of the node latitude column.
    private static final int NODE_LAT = 3;

    // Column index of the node id column.
    private static final int NODE_ID = 4;

    // Column index of the node neighbors column.
    private static final int NODE_NEIGHBORS = 5;

    // radius of earth in km
    private static final int RADIUS = 6371;

    //map used to contain all vertices. <Vertex Id, Vertex>
    public static HashMap<String, Vertex> nodeMap;

    //singleton instance
    private static NodeParser mInstance = null;

    //constructor
    public NodeParser(Context context) {
        nodeMap = new HashMap<>();
        int id = context.getResources().getIdentifier("nodemap", "raw", context.getPackageName());
        InputStream is = context.getResources().openRawResource(id);
        parseNodeFile(is);
    }

    //get single instance
    public static NodeParser getInstance(Context context) {
        if (mInstance == null) {
            synchronized (NodeParser.class) {
                if (mInstance == null) {
                    mInstance = new NodeParser(context);
                }
            }
        }
        return mInstance;
    }

    public static void parseNodeFile(InputStream inputStream) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                inputStream));
        try {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                parseLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        setAdjacencies();

    }

    private static void parseLine(String line) {

        String[] cells = line.split(",");
        if (cells.length > 0) {

            String nodeLong = cells[NODE_LONG].toUpperCase(Locale.ENGLISH);
            String nodeLat = cells[NODE_LAT].toUpperCase(Locale.ENGLISH);
            String nodeId = cells[NODE_ID].toUpperCase(Locale.ENGLISH);
            //
            String nodeNeigh = cells[NODE_NEIGHBORS].toUpperCase(Locale.ENGLISH);

            String neighbors[] = nodeNeigh.split(" ");

            Vertex temp = new Vertex(nodeId);
            temp.neighbors = new ArrayList(Arrays.asList(neighbors));
            temp.lat = Double.parseDouble(nodeLong);
            temp.lon = Double.parseDouble(nodeLat);

            nodeMap.put(temp.name, temp);

        }
    }

    // updates the nodemap to set the adjacencies for each vertex
    private static void setAdjacencies() {

        ArrayList<String> keySet = new ArrayList(nodeMap.keySet());
        for (String key : keySet) {
            // current vertex
            Vertex temp = nodeMap.get(key);
            ArrayList<String> neighbors = new ArrayList(temp.neighbors);
            double weight;

            // for each neighbor
            for (int i = 0; i < neighbors.size(); i++) {
                // a neighbor to current vertex
                Vertex targetTemp = nodeMap.get(neighbors.get(i));
                weight = calculateEdge(temp, targetTemp);
                temp.adjacencies.add(new Edge(targetTemp, weight));
            }

        }
    }
    // returns the distance between two vertices
    public static double calculateEdge(Vertex current, Vertex neighbor) {

        double lat = current.lat;
        double lon = current.lon;
        double lat2 = neighbor.lat;
        double lon2 = neighbor.lon;

        Double latDistance = deg2rad(lat2 - lat);
        Double lonDistance = deg2rad(lon2 - lon);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(deg2rad(lat)) * Math.cos(deg2rad(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = RADIUS * c * 1000; // convert to meters
        return distance;

    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // returns the nodeMap as <vertex ID, Vertex>
    public static HashMap <String, Vertex> getNodeMap(){
        return nodeMap;
    }

    // returns the nodeMap as <Vertex LatLng, Vertex>
    public static HashMap <LatLng, Vertex> getLatLngNodeMap(){

        ArrayList<String> keySet = new ArrayList(nodeMap.keySet());
        HashMap <LatLng, Vertex> latLngNodeMap = new HashMap();

        for (String key : keySet) {
            Vertex temp = nodeMap.get(key);
            LatLng latLngTemp = new LatLng(temp.lat, temp.lon);
            latLngNodeMap.put(latLngTemp, temp);
        }

        return latLngNodeMap;
    }

}
