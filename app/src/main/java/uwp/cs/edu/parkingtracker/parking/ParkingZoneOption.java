package uwp.cs.edu.parkingtracker.parking;

import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by Russ on 4/21/2015.
 */
public class ParkingZoneOption implements Comparable<ParkingZoneOption>{
    public String id;
    public float distance;
    public int color;
    public PolylineOptions path;

    public ParkingZoneOption(String id, float distance, int color, PolylineOptions path){
        super();
        this.color = color;
        this.id=id;
        this.distance=distance;
        this.path=path;
    }

    @Override
    public int compareTo(ParkingZoneOption otherOption){
        Double fullness1 = Double.valueOf(ZoneList.getInstance().getFullness(id));
        Double fullness2 = Double.valueOf(ZoneList.getInstance().getFullness(otherOption.id));
        int i = 0;
        if (fullness1 == fullness2 || (fullness1<66 && fullness2 < 66)) {
            i = 0;
        }
        else {
            if (fullness1 < fullness2) i = -1;
            if (fullness1 > fullness2) i = 1;
        }
        if (i != 0 ) return i;

        return Float.valueOf(distance).compareTo(otherOption.distance);
    }
}
