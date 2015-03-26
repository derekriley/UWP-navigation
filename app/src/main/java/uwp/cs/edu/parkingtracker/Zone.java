package uwp.cs.edu.parkingtracker;

import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

/**
 * Created by Russ on 3/18/2015.
 */
public class Zone {
    private String zoneId;
    private PolygonOptions polygonOptions;
    private double fullness;
    private Polygon polygon;

    public Zone() {
        this.fullness = 0.0;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
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

    public void setFullness(double fullness) {
        this.fullness = fullness;
    }

    public double getFullness() {
        return fullness;
    }

    public Zone(String zoneId, PolygonOptions polygonOptions) {
        this.zoneId = zoneId;
        this.polygonOptions = polygonOptions;
    }
}
