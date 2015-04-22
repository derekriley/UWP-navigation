package uwp.cs.edu.parkingtracker.parking;

import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by Russ on 4/21/2015.
 */
public class ParkingZoneOption implements Comparable<ParkingZoneOption>{
    public String title;
    public float distance;
    public ZoneList.Zone zone;
    public PolylineOptions path;

    public ParkingZoneOption(String title, float distance, ZoneList.Zone zone, PolylineOptions path){
        super();
        this.zone = zone;
        this.title=title;
        this.distance=distance;
        this.path=path;
    }

    @Override
    public int compareTo(ParkingZoneOption otherOption){
        Double fullness1 = Double.valueOf(zone.getFullness());
        Double fullness2 = Double.valueOf(otherOption.zone.getFullness());
        int i = 0;
        if (fullness1==fullness2 || (fullness1<6.66&&fullness2<6.66)) {
            i = 0;
        }
        else {
            if (fullness1 < fullness2) i = -1;
            if (fullness1 > fullness2) i = 1;
        }
        if (i != 0 ) return i;

        return Float.valueOf(distance).compareTo(Float.valueOf(otherOption.distance));
    }
}
