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

package uwp.cs.edu.parkingtracker.parking;

import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import uwp.cs.edu.parkingtracker.CONSTANTS;
import uwp.cs.edu.parkingtracker.mapping.MapObject;
import uwp.cs.edu.parkingtracker.network.DatabaseExchange;

/**
 * Thread-safe Singleton Class for the main zone list.
 * Works with Activities and the zone service
 * Created by nate eisner
 * */
public class ZoneList extends MapObject{

    private static class Zone {
        private String zoneId;
        private String fullness;
        private String confidence;
        private int color;
        String navId;

        public Zone() {
            this.fullness = "0";
        }

        public String getZoneId() {
            return zoneId;
        }

        public void setZoneId(String zoneId) {
            this.zoneId = zoneId;
        }

        public String getFullness() {
            return fullness;
        }

        public String getNavId() {
            return navId;
        }

        public void setNavId(String id) {
            navId = id;
        }

        /**
         * Used to set fullness of the zone
         * @param fullness - fullness amount
         */
        public void setFullness(String fullness) {
            this.fullness = fullness;
            //this.confidence=confidence;
            if (Double.valueOf(fullness) > 66) {
                this.color = Color.RED;
            }
            if (Double.valueOf(fullness) >= 33 && Double.valueOf(fullness) <= 66) {
                this.color = Color.YELLOW;
            }
            if (Double.valueOf(fullness) < 33) {
                this.color = Color.GREEN;
            }
            if (Double.valueOf(fullness) < 0){
                this.color = Color.BLACK;
            }
//             if (Double.valueOf(confidence) > 66){
//                this.color = Color.argb(255,Color.red(color),Color.green(color),Color.blue(color));
//            }
//            if (Double.valueOf(confidence) >= 33 && Double.valueOf(confidence) <= 66){
//                this.color = Color.argb(170,Color.red(color),Color.green(color),Color.blue(color));
//            }
//            if (Double.valueOf(confidence) < 33){
//                this.color = Color.argb(85,Color.red(color),Color.green(color),Color.blue(color));
//            }
        }

        public int getColor() {
            return color;
        }

        public Zone(String zoneId) {
            this.fullness = "0";
            this.zoneId = zoneId;
        }
    }

    //thread-safe hashmap
    private ConcurrentHashMap<String,Zone> zoneMap;
    public static volatile ZoneList mInstance = null;
    private boolean beingUsed = false;

     //Constructor - creates from constants map
    private ZoneList() {
        zoneMap = new ConcurrentHashMap<>();
        for (Map.Entry<String, PolygonOptions> entry : CONSTANTS.zones.entrySet()) {
            Zone z = new Zone(entry.getKey());
            zoneMap.put(z.getZoneId(), z);
            zoneMap.get(z.getZoneId()).setNavId(z.getZoneId());
        }

    }

    public synchronized static ZoneList getInstance() {
        if (mInstance == null) {
            synchronized (ZoneList.class) {
                if (mInstance == null) {
                    mInstance = new ZoneList();
                    Log.d("ZoneList", "New Instance");
                }
            }
        }
        return mInstance;
    }

    public synchronized ArrayList<String> getZoneIDs () {
        synchronized (zoneMap) {
            ArrayList<String> zIDS = new ArrayList<>();
                zIDS.addAll(zoneMap.keySet());
            return zIDS;
        }
    }

    public synchronized boolean setFullness (String zID) {
        try {
            Zone z = zoneMap.get(zID);
            String result = DatabaseExchange.getFullness(zID);
            String fullness = result;//.split(",")[0];
//            String confidence = "100";
//            if (result.split(",").length>1) {
//                confidence = result.split(",")[1];
//            }

            z.setFullness(fullness);

            zoneMap.put(zID, z);
            return true;
        } catch (Exception e) {
            //TODO: Handle exceptions
            return false;
        }
    }

    public synchronized int getColor(String zID) {
        return zoneMap.get(zID).getColor();
    }

    public synchronized String getFullness(String zID) {
        return zoneMap.get(zID).getFullness();
    }
}
