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
        private String navId;
        private PolygonOptions polygonOptions;

        private Building(String n, PolygonOptions pO) {
            this.name = n;
            this.polygonOptions = pO;
        }

        public String getNavId() {
            return navId;
        }

        public void setNavId(String id) {
            navId = id;
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
        buildingHashMap.get("Student Center").setNavId("Student_Center");
        buildingHashMap.get("Molinaro Hall").setNavId("Molinaro_Hall");
        buildingHashMap.get("Greenquist Hall").setNavId("Greenquist_Hall");
        buildingHashMap.get("Wyllie Hall").setNavId("Wyllie_Hall");
        buildingHashMap.get("The Rita").setNavId("The_Rita");
        buildingHashMap.get("Sports and Activity Center").setNavId("Sports_and_Activity_Center");
        buildingHashMap.get("Student Health and Counseling Center").setNavId("Student_Health_and_Counseling_Center");
    }

    public String BuildingTapped(LatLng point) {
        ArrayList<Building> buildings = new ArrayList<>(buildingHashMap.values());
        for (Building b : buildings) {
            if (pointInPolygon(point, b.getPolygonOptions())) {
                return b.getNavId();
            }
        }
        return null;
    }
}
