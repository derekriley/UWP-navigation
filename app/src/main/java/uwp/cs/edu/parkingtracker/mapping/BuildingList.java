package uwp.cs.edu.parkingtracker.mapping;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uwp.cs.edu.parkingtracker.CONSTANTS;

/**
 * Created by nathaneisner on 4/19/15.
 */
public class BuildingList extends MapObject{
    private HashMap<String,Building> buildingHashMap;

    private class Building {
        private String name;
        private PolygonOptions polygonOptions;

        private Building(String n, PolygonOptions pO) {
            this.name = n;
            this.polygonOptions = pO;
        }

        public String getName() {
            return this.name;
        }

        public PolygonOptions getPolygonOptions() {
            return this.polygonOptions;
        }
    }

    //Constructor - creates from constants map
    public BuildingList() {
        buildingHashMap = new HashMap<>();
        for (Map.Entry<String, PolygonOptions> entry : CONSTANTS.buildingOutlines.entrySet()) {
            Building b = new Building(entry.getKey(), entry.getValue());
            buildingHashMap.put(b.getName(), b);
        }
    }

    public String BuildingTapped(LatLng point) {
        ArrayList<Building> buildings = new ArrayList<>(buildingHashMap.values());
        for (Building b : buildings) {
            if (pointInPolygon(point, b.getPolygonOptions())) {
                return b.getName();
            }
        }
        return null;
    }
}
