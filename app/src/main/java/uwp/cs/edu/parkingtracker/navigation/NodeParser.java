package uwp.cs.edu.parkingtracker.navigation;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;


/**
 * Created by Able on 4/27/2015.
 */

public class NodeParser {
    /**
     * Column index of the building column.
     */
    private static final int BUILDING = 0;
    /**
     * Column index of the node type column.
     */
    private static final int NODE_TYPE = 1;
    /**
     * Column index of the node longitude column.
     */
    private static final int NODE_LONG = 2;
    /**
     * Column index of the node latitude column.
     */
    private static final int NODE_LAT = 3;
    /**
     * Column index of the node id column.
     */
    private static final int NODE_ID = 4;
    /**
     * Column index of the node neighbors column.
     */
    private static final int NODE_NEIGHBORS = 5;

    private static final int RADIUS = 6371;

    private static HashMap<String, Vertex> nodeMap = new HashMap();


    public static void parseBuildingFile(InputStream inputStream) {

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

        fillVertex();

    }

    private static void parseLine(String line) {

        String[] cells = line.split(",");
        if (cells.length > 0) {

            String nodeLong = cells[NODE_LONG].toUpperCase(Locale.ENGLISH);
            String nodeLat = cells[NODE_LAT].toUpperCase(Locale.ENGLISH);
            String nodeId = cells[NODE_ID].toUpperCase(Locale.ENGLISH);
            String nodeNeigh = cells[NODE_NEIGHBORS].toUpperCase(Locale.ENGLISH);

            String tests[] = nodeNeigh.split(" ");

            Vertex temp = new Vertex(nodeId);
            temp.neigh = new ArrayList(Arrays.asList(tests));
            temp.lat = Double.parseDouble(nodeLong);
            temp.lon = Double.parseDouble(nodeLat);

            nodeMap.put(temp.name, temp);

        }
    }

    private static void fillVertex() {

        ArrayList<String> keySet = new ArrayList(nodeMap.keySet());
        for (String key : keySet) {
            Vertex temp = nodeMap.get(key);
            ArrayList<String> neighs = new ArrayList(temp.neigh);
            double weight;

            for (int i = 0; i < neighs.size(); i++) {
                Vertex targetTemp = nodeMap.get(neighs.get(i));
                weight = calculateEdge(targetTemp, temp.lat, temp.lon, targetTemp.lat, targetTemp.lon);
                temp.adjacencies.add(new Edge(targetTemp, weight));
            }

        }
    }

    public static double calculateEdge(Vertex neighbor, double lat, double lon, double lat2, double lon2) {

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

}
