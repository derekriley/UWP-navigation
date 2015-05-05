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

package uwp.cs.edu.parkingtracker;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.support.v4.app.DialogFragment;

import uwp.cs.edu.parkingtracker.network.DatabaseExchange;
import uwp.cs.edu.parkingtracker.parking.ParkDialogFragment;


// Created by David on 11/22/14.

public class DeviceListeners implements LocationListener, ParkDialogFragment.ParkDialogListener, View.OnClickListener, View.OnLongClickListener {

    // Instance Variables Begin

    // objects for the phone sensors that detect shaking
    private SensorManager mSensorManager = null;
    private float mAccel = 0.00f; // acceleration apart from gravity
    private float mAccelCurrent = SensorManager.GRAVITY_EARTH; // current acceleration including gravity
    private float mAccelLast = SensorManager.GRAVITY_EARTH; // last acceleration including gravity

    // location manager that gets the current gps location from the phone
    private LocationManager locationManager = null;
    private Location location = null;

    // Passed Activity.
    private MainActivity passedActivity = null;
    // Instance Variables End

    // Getters : Lazy Instantiation
    public SensorManager getmSensorManager() {
        if (mSensorManager == null) {
            mSensorManager = (SensorManager) passedActivity.getSystemService(Context.SENSOR_SERVICE);
            mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        }
        return mSensorManager;
    }

    public LocationManager getLocationManager() {
        if (locationManager == null) {
            locationManager = (LocationManager) passedActivity.getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 6000, 10, this);
        }
        return locationManager;
    }

    public Location getLocation() {
        if (location == null) {
            location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
        }
        return location;
    }

    /**
     * Default constructor.
     *
     * @param passedActivity
     */
    public DeviceListeners(MainActivity passedActivity) {
        // Set the instance variables.
        this.passedActivity = passedActivity;
        // Set up sensor manager.
        getmSensorManager();
        // Set up location manager.
        getLocationManager();
    }

    /**
     * Called when the location has changed.
     * <p/>
     * <p> There are no restrictions on the use of the supplied Location object.
     *
     * @param location The new location, as a Location object.
     */
    @Override
    public void onLocationChanged(Location location) {
        //passedActivity.setLocation(location);
        if ((location.getLatitude() >= 42.647382) && (location.getLatitude() <= 42.647717)) {
            if ((location.getLongitude() >= -87.854570) && location.getLongitude() <= -87.854157) {
                //Toast.makeText(this.passedActivity.getApplicationContext(), passedActivity.zone2,
                //        Toast.LENGTH_SHORT).show();
                //DatabaseExchange.vectorSub(passedActivity, CONSTANTS.STUDENT_CENTER_PARKING_LOT, passedActivity.zone2);
            }
        }
        if ((location.getLatitude() >= 42.648933) && (location.getLatitude() <= 42.649227)) {
            if ((location.getLongitude() >= -87.853804) && location.getLongitude() <= -87.854021) {
                //Toast.makeText(this.passedActivity.getApplicationContext(), "N. Parking",
                //        Toast.LENGTH_SHORT).show();
                //DatabaseExchange.vectorAdd(passedActivity, CONSTANTS.STUDENT_CENTER_PARKING_LOT, passedActivity.zone2);
            }
        }
        if ((location.getLatitude() >= 42.647898) && (location.getLatitude() <= 42.647980)) {
            if ((location.getLongitude() >= -87.852234) && location.getLongitude() <= -87.852037) {
                //Toast.makeText(this.passedActivity.getApplicationContext(), "E, Parking",
                //       Toast.LENGTH_SHORT).show();
                //DatabaseExchange.vectorAdd(passedActivity, CONSTANTS.STUDENT_CENTER_PARKING_LOT, passedActivity.zone2);
            }
        }
    }

    /**
     * Called when the provider status changes. This method is called when
     * a provider is unable to fetch a location or if the provider has recently
     * become available after a period of unavailability.
     *
     * @param provider the name of the location provider associated with this
     *                 update.
     * @param status   {@link android.location.LocationProvider#OUT_OF_SERVICE} if the
     *                 provider is out of service, and this is not expected to change in the
     *                 near future; {@link android.location.LocationProvider#TEMPORARILY_UNAVAILABLE} if
     *                 the provider is temporarily unavailable but is expected to be available
     *                 shortly; and {@link android.location.LocationProvider#AVAILABLE} if the
     *                 provider is currently available.
     * @param extras   an optional Bundle which will contain provider specific
     *                 status variables.
     *                 <p/>
     *                 <p> A number of common key/value pairs for the extras Bundle are listed
     *                 below. Providers that use any of the keys on this list must
     *                 provide the corresponding value as described below.
     *                 <p/>
     *                 <ul>
     *                 <li> satellites - the number of satellites used to derive the fix
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    /**
     * Called when the provider is enabled by the user.
     *
     * @param provider the name of the location provider associated with this
     *                 update.
     */
    @Override
    public void onProviderEnabled(String provider) {

    }

    /**
     * Called when the provider is disabled by the user. If requestLocationUpdates
     * is called on an already disabled provider, this method is called
     * immediately.
     *
     * @param provider the name of the location provider associated with this
     *                 update.
     */
    @Override
    public void onProviderDisabled(String provider) {

    }

     //onClickListener that tracks which button has been pressed,
     //and either resets the app or displays other lots


     //method / instantiation that determines what zone the user is in
     //and allows them to vote.

    private final SensorEventListener mSensorListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter
            if (mAccel > 4) {
                Location loco = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
                if ((loco.getLongitude() <= -87.853320)) {

                    if ((loco.getLatitude() <= 42.648890) && (loco.getLatitude() >= 42.648476)) {
                        //Toast.makeText(passedActivity.getApplicationContext(), "Zone 1",
                                //Toast.LENGTH_SHORT).show();
//                        passedActivity.zone = "1";
                    } else if ((loco.getLatitude() <= 42.648475) && (loco.getLatitude() >= 42.648069)) {
                        //Toast.makeText(passedActivity.getApplicationContext(), "Zone 2",
                          //      Toast.LENGTH_SHORT).show();
                        //passedActivity.zone = "2";
                    } else if ((loco.getLatitude() <= 42.648068) && (loco.getLatitude() >= 42.647660)) {
//                        Toast.makeText(passedActivity.getApplicationContext(), "Zone 3",
//                                Toast.LENGTH_SHORT).show();
                        //BasicUser.zone = "3";
                    }
                } else if ((loco.getLongitude() > -87.853320)) {
                    if ((loco.getLatitude() <= 42.648890) && (loco.getLatitude() >= 42.648476)) {
//                        Toast.makeText(passedActivity.getApplicationContext(), "Zone 4",
//                                Toast.LENGTH_SHORT).show();
                        //passedActivity.zone = "4";
                    } else if ((loco.getLatitude() <= 42.648475) && (loco.getLatitude() >= 42.648069)) {
//                        Toast.makeText(passedActivity.getApplicationContext(), "Zone 5",
//                                Toast.LENGTH_SHORT).show();
                        //BasicUser.zone = "5";
                    } else if ((loco.getLatitude() <= 42.648068) && (loco.getLatitude() >= 42.647660)) {
//                        Toast.makeText(passedActivity.getApplicationContext(), "Zone 6",
//                                Toast.LENGTH_SHORT).show();
                        //passedActivity.zone = "6";
                    }
                }
                //if (!passedActivity.hasAsked2) {
//                    this.showDialog();
                //    passedActivity.hasAsked2 = true; }
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };


    @Override
    public void onDialogSend(DialogFragment dialog, int val){
        String zID = ((ParkDialogFragment) dialog).getID();
        DatabaseExchange.sendVote(zID, val*25);
    }
    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
//        switch (v.getId())
//        {
//            case R.id.basic_user_refresh_button :
//                Toast.makeText(passedActivity.getApplicationContext(), "Refreshing Map",
//                        Toast.LENGTH_SHORT).show();
//                /*
//                 * Should be made more generic; perhaps dependent upon device location.
//                 */
//                passedActivity.getMapTransform().resetMap(CONSTANTS.STUDENT_CENTER_C_LAT, CONSTANTS.STUDENT_CENTER_C_LNG, CONSTANTS.DEFAULT_ZOOM_FACTOR);
//                break;
//
//            case R.id.basic_user_other_lots_button :
//                Toast.makeText(passedActivity.getApplicationContext(), "Displaying Other Lots",
//                        Toast.LENGTH_SHORT).show();
//                break;
//        }
    }

    /**
     * Called when a view has been clicked and held.
     * <p/>
     * onLongClickListener that checks for a button hold for half
     * a second, which allows experts to change the map
     *
     * @param v The view that was clicked and held.
     * @return true if the callback consumed the long click, false otherwise.
     */
    @Override
    public boolean onLongClick(View v) {
        //Offer additional options when the view is held.
        Toast.makeText(passedActivity.getApplicationContext(), "Expert Button Held Down",
                Toast.LENGTH_SHORT).show();
        //passedActivity.goToExpert(v);
        return true;
    }
}
