package uwp.cs.edu.parkingtracker;

import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Thread-safe Singleton Class for the main zone list.
 * Works with Activities and the zone service
 * Created by nate eisner
 */
public class ZoneList {

    public static class Zone {
        private String zoneId;
        private PolygonOptions polygonOptions;
        private String fullness;

        public Zone() {
            this.fullness = "0";
            this.polygonOptions.fillColor(Color.GREEN);
        }

        public String getZoneId() {
            return zoneId;
        }

        public void setZoneId(String zoneId) {
            this.zoneId = zoneId;
        }

        public PolygonOptions getPolygonOptions() {
            return polygonOptions;
        }

        public void setPolygonOptions(PolygonOptions polygonOptions) {
            this.polygonOptions = polygonOptions;
        }

        public void setFullness(String fullness) {
            this.fullness = fullness;
            if (Double.valueOf(fullness) > 6.66) {
                this.polygonOptions.fillColor(Color.RED);
            }
            if (Double.valueOf(fullness) >= 3.33 && Double.valueOf(fullness) <= 6.66) {
                this.polygonOptions.fillColor(Color.YELLOW);
            }
            if (Double.valueOf(fullness) < 3.33) {
                this.polygonOptions.fillColor(Color.GREEN);
            }
        }

        public String getFullness() {
            return fullness;
        }

        public Zone(String zoneId, PolygonOptions polygonOptions) {
            this.fullness = "0";
            this.zoneId = zoneId;
            this.polygonOptions = polygonOptions;
            this.polygonOptions.fillColor(Color.GREEN);

        }
    }

    //synchronized thread-safe arraylist
    private CopyOnWriteArrayList<Zone> zoneArrayList;
    static ZoneList mInstance = null;

    /**
     * Constructor
     */
    private ZoneList() {
        zoneArrayList = new CopyOnWriteArrayList<Zone>();
        for (Map.Entry<String, PolygonOptions> entry : CONSTANTS.zones.entrySet()) {
            Zone z = new Zone(entry.getKey(), entry.getValue());

            zoneArrayList.add(z);
        }
    }

    public synchronized static ZoneList getInstance() {
        if (mInstance == null) {
            mInstance = new ZoneList();
            Log.d("ZoneList", "New Instance");
            mInstance.update();
        }
        return mInstance;
    }


    public int getSize() {
        return zoneArrayList.size();
    }

    /**
     * @param zID
     * @param pO
     */
    public void addZone(String zID, PolygonOptions pO) {
        Zone z = new Zone(zID, pO);
        zoneArrayList.add(z);
    }


    public synchronized void update() {
        Runnable runnable = new Runnable() {
            public void run() {
                for (Zone z : zoneArrayList) {
                    z.setFullness(DatabaseExchange.getAverageVote(z.getZoneId()));
                }
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();

    }

    public synchronized ArrayList<PolygonOptions> getPolys() {
        ArrayList<PolygonOptions> polys = new ArrayList<>();
        for (Zone z : zoneArrayList) {
            polys.add(z.getPolygonOptions());
        }
        return polys;
    }

    public boolean pointInPolygon(LatLng point, PolygonOptions polygon) {
        // ray casting alogrithm http://rosettacode.org/wiki/Ray-casting_algorithm
        int crossings = 0;
        List<LatLng> path = polygon.getPoints();
        //path.remove(path.size()); //remove the last point that is added automatically by getPoints()

        // for each edge
        for (int i = 0; i < path.size(); i++) {
            LatLng a = path.get(i);
            int j = i + 1;
            //to close the last edge, you have to take the first point of your polygon
            if (j >= path.size()) {
                j = 0;
            }
            LatLng b = path.get(j);
            if (rayCrossesSegment(point, a, b)) {
                crossings++;
            }
        }

        // odd number of crossings?
        return (crossings % 2 == 1);
    }

    public boolean rayCrossesSegment(LatLng point, LatLng a, LatLng b) {
        // Ray Casting algorithm checks, for each segment, if the point is 1) to the left of the segment and 2) not above nor below the segment. If these two conditions are met, it returns true
        double px = point.longitude,
                py = point.latitude,
                ax = a.longitude,
                ay = a.latitude,
                bx = b.longitude,
                by = b.latitude;
        if (ay > by) {
            ax = b.longitude;
            ay = b.latitude;
            bx = a.longitude;
            by = a.latitude;
        }
        // alter longitude to cater for 180 degree crossings
        if (px < 0 || ax < 0 || bx < 0) {
            px += 360;
            ax += 360;
            bx += 360;
        }
        // if the point has the same latitude as a or b, increase slightly py
        if (py == ay || py == by) py += 0.00000001;


        // if the point is above, below or to the right of the segment, it returns false
        if ((py > by || py < ay) || (px > Math.max(ax, bx))) {
            return false;
        }
        // if the point is not above, below or to the right and is to the left, return true
        else if (px < Math.min(ax, bx)) {
            return true;
        }
        // if the two above conditions are not met, you have to compare the slope of segment [a,b] (the red one here) and segment [a,p] (the blue one here) to see if your point is to the left of segment [a,b] or not
        else {
            double red = (ax != bx) ? ((by - ay) / (bx - ax)) : Double.POSITIVE_INFINITY;
            double blue = (ax != px) ? ((py - ay) / (px - ax)) : Double.POSITIVE_INFINITY;
            return (blue >= red);
        }

    }

    public String[] zoneTapped(LatLng point) {
        for (Zone z : zoneArrayList) {
            if (pointInPolygon(point, z.getPolygonOptions())) {
                return new String[]{z.getZoneId(), z.getFullness()};
            }
        }
        return null;
    }

}
