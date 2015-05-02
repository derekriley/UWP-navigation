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
