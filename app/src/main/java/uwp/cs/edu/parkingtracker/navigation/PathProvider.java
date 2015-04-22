package uwp.cs.edu.parkingtracker.navigation;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by Russ on 4/22/2015.
 */
public interface PathProvider {
    public PolylineOptions getPath(LatLng start,LatLng finish);
}
