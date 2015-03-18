package uwp.cs.edu.parkingtracker;

import com.google.android.gms.maps.model.PolygonOptions;

/**
 * Created by Able on 3/18/2015.
 */
public class Zone {

    private int fullness;
    private String ID = new String();
    private PolygonOptions poly = new PolygonOptions();

    //constructor
    public Zone (String ID, PolygonOptions poly) {

        this.ID = ID;
        this.poly = poly;

    }

    // setters
    public void setFullness(int fullness) {
        this.fullness = fullness;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setPoly(PolygonOptions poly) {
        this.poly = poly;
    }

    //getters
    public int getFullness(){
        return fullness;
    }

    public String getID(){
        return ID;
    }

    public PolygonOptions getPoly () {
        return poly;
    }

}
