package uwp.cs.edu.parkingtracker.navigation;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by Russ on 4/22/2015.
 */
public class StraightLinePathProvider implements PathProvider {
    @Override
    public PolylineOptions getPath(LatLng start, LatLng finish) {
        PolylineOptions options = new PolylineOptions();
        options.add(start,finish);
        options.color(Color.BLUE);
        return options;
    }
}
