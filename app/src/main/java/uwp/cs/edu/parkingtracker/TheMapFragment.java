/*   Copyright 2014 University Of Wisconsin Parkside
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

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import uwp.cs.edu.parkingtracker.mapping.MapTransform;
import uwp.cs.edu.parkingtracker.parking.ZoneService;

/**
 * fragment for map
 */
public class TheMapFragment extends SupportMapFragment {
    // Instance variable begin
    protected DeviceListeners deviceListeners = null;
    protected MapTransform mapTransform;
    private GoogleMap mMap;
    private ProgressBar progress;
    private FragmentActivity myContext;

    // Service
    private Intent mServiceIntent = null;

    public TheMapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mServiceIntent = new Intent(MainActivity.getInstance(), ZoneService.class);
        View v = inflater.inflate(R.layout.fragment_parking, container, false);
        mMap = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        mMap.setMyLocationEnabled(true);

        double latitude = CONSTANTS.STUDENT_CENTER_C_LAT;
        double longitude = CONSTANTS.STUDENT_CENTER_C_LNG;
        float zoomFactor = CONSTANTS.DEFAULT_ZOOM_FACTOR;
        MapsInitializer.initialize(MainActivity.getInstance());
        // makes the map focus on the Student Center parking lot.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), zoomFactor));

        // Inflate the layout for this fragment
        return v;
    }
    //    //option for server progress
    private void showLoadingBar(boolean option) {
        //View mapView = findViewById(R.id.mapLayout);

//        ViewGroup.MarginLayoutParams mapParams = (ViewGroup.MarginLayoutParams)
//                mapView.getLayoutParams();
        //show progress bar
        if (option) {
//            mapParams.setMargins(0, 10, 0, 0);
//            mapView.requestLayout();
            progress.setVisibility(View.VISIBLE);
        }
        else {
//            mapParams.setMargins(0, 0, 0, 0);
//            mapView.requestLayout();
            progress.setVisibility(View.GONE);
        }

    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    /**
     * Custom BroadcastReceiver for loading zones, displaying progress and calling to refresh map
     * when complete
     */
    private BroadcastReceiver loadingStatus = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean complete = intent.getBooleanExtra(CONSTANTS.DATA_STATUS,false);
            if(!complete) {
                showLoadingBar(true);
                int status = intent.getIntExtra(CONSTANTS.DATA_AMOUNT,CONSTANTS.zones.size());
                if (status < CONSTANTS.zones.size()) {
                    //progress.setProgress(status);
                    //showLoadingBar(true);
                }
                if (status == CONSTANTS.zones.size()) {
                    //showLoadingBar(false);
                }
                progress.setProgress(status);
            }
            if (complete) {
                progress.setProgress(0);
                showLoadingBar(false);
                //mapTransform.refreshMap();
            }
        }
    };

}
