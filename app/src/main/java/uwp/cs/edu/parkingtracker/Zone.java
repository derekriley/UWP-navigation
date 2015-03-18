package uwp.cs.edu.parkingtracker;

import com.google.android.gms.maps.model.PolygonOptions;

/**
 * Created by Russ on 3/18/2015.
 */
public class Zone {
    private String zoneId;
    private PolygonOptions polygonOptions;
    private int fullness;

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

    public int getFullness() {
        return fullness;
    }

    public void setFullness(int fullness) {
        this.fullness = fullness;
    }

    public Zone(String zoneId, PolygonOptions polygonOptions) {
        this.zoneId = zoneId;
        this.polygonOptions = polygonOptions;
    }
}
