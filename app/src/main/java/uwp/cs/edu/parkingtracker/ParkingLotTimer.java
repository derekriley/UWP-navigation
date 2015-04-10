/**
 *   Copyright 2014 University Of Wisconsin Parkside
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package uwp.cs.edu.parkingtracker;

import android.content.Intent;

import java.util.TimerTask;


 //Created by David on 11/21/14.

public class ParkingLotTimer extends TimerTask {

    //Instance Variables
    private DeviceListeners deviceListeners = null;
    private Intent mService;

    // Setter/Getters
    public DeviceListeners getDeviceListeners() {
        return deviceListeners;
    }

    public ParkingLotTimer(DeviceListeners passedDeviceListeners, Intent service) {
        if (passedDeviceListeners != null && service != null) {
            this.deviceListeners = passedDeviceListeners;
            this.mService = service;
        } else
            throw new AssertionError("Can not pass null!");


    }

    public void run() {

/*        Location loc = deviceListeners.getLocationManager().getLastKnownLocation(deviceListeners.getLocationManager().GPS_PROVIDER);
        if (loc == null)
            return;

        if (Math.abs((loc.getLongitude() - getDeviceListeners().getLocation().getLongitude())) < 0.0001 && !BasicUser.hasAsked1) {
            if (Math.abs((loc.getLatitude() - getDeviceListeners().getLocation().getLatitude())) < 0.0001) {
                if ((loc.getLongitude() <= -87.853320))
                {
                    if ((loc.getLatitude() <= 42.648890) && (loc.getLatitude() >= 42.648476)) {
                        BasicUser.zone2 = "1";
                    } else if ((loc.getLatitude() <= 42.648475) && (loc.getLatitude() >= 42.648069)) {
                        BasicUser.zone2 = "2";
                    } else if ((loc.getLatitude() <= 42.648068) && (loc.getLatitude() >= 42.647660)) {
                        BasicUser.zone2 = "3";
                    }
                } else if ((loc.getLongitude() > -87.853320)) {
                    if ((loc.getLatitude() <= 42.648890) && (loc.getLatitude() >= 42.648476)) {
                        BasicUser.zone2 = "4";
                    } else if ((loc.getLatitude() <= 42.648475) && (loc.getLatitude() >= 42.648069)) {
                        BasicUser.zone2 = "5";
                    } else if ((loc.getLatitude() <= 42.648068) && (loc.getLatitude() >= 42.647660)) {
                        BasicUser.zone2 = "6";
                    }
                }
                BasicUser.hasAsked1 = true;
           }
        }*/
    }
}
