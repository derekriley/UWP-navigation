/**
 *
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
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;


public class BasicUser extends FragmentActivity {

    /* Instance variables begin */

    private String[] drawerItems ={"Parking","Buildings","Events"};
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    // arrays that hold the data retrieved from the database
    private ArrayList<Double> lat = null;
    private ArrayList<Double> lng = null;
    private ArrayList<Double> colors = null;

    protected static boolean hasAsked1, hasAsked2 = false;
    protected static String zone = "1";
    protected static String zone2 = "1";

    protected ParkingLotTimer myTask;
    protected Timer myTimer;

    protected DeviceListeners deviceListeners = null;
    protected MapTransform mapTransform = null;
    private ArrayList<Zone> zoneList;

    /* Instance variables end */

    /* Setters/Getters */
    public ArrayList<Double> getLat()
    {
        if (lat == null)
        {
            lat = new ArrayList<Double>(6);
        }
        return lat;
    }

    public ArrayList<Double> getLng()
    {
        if (lng == null)
        {
            lng = new ArrayList<Double>(6);
        }
        return lng;
    }

    public ArrayList<Double> getColors()
    {
        if (colors == null)
        {
            colors = new ArrayList<Double>(6);
        }
        return colors;
    }

    public void clearLocationArrays()
    {
        lat = null;
        lng = null;
        colors = null;
    }

    public MapTransform getMapTransform() {
        if (mapTransform == null)
        {
            // Transform Map. Set map position to student center parking lot.
            mapTransform = new MapTransform(this);
            mapTransform.setUpMap(CONSTANTS.STUDENT_CENTER_C_LAT, CONSTANTS.STUDENT_CENTER_C_LNG, CONSTANTS.DEFAULT_ZOOM_FACTOR);
        }
        return mapTransform;
    }

    public DeviceListeners getDeviceListeners() {
        if (deviceListeners == null)
        {
            // Instantiate new device listener.
            deviceListeners = new DeviceListeners(this);
            // Attach listener to the refresh, expert, and other lots buttons.
//            findViewById(R.id.basic_user_refresh_button).setOnClickListener(deviceListeners);
//            findViewById(R.id.basic_user_expert_button).setOnLongClickListener(deviceListeners);
//            findViewById(R.id.basic_user_other_lots_button).setOnClickListener(deviceListeners);
        }
        return deviceListeners;
    }

    /*  Activity Methods */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_user);
        zoneList = CONSTANTS.zones;
        //added nav drawer
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, drawerItems));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                               @Override
                                               public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                   Toast.makeText(getApplicationContext(),"item "+position+"selected",Toast.LENGTH_LONG).show();
                                               }
                                           });
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);


                // fills up the arrays that will be used in other methods / classes
                //DatabaseExchange.fillArrayOfZones(this, CONSTANTS.STUDENT_CENTER_PARKING_LOT, getLat(), getLng(), getColors());

        // Setup device listeners.
        getDeviceListeners();

        //get zone data from server
        for (Zone zone : CONSTANTS.zones) {
            zone.setFullness(Double.valueOf(DatabaseExchange.getAverageVote(BasicUser.this,zone)));
        }

        // Setup map.
        getMapTransform();

        myTask = new ParkingLotTimer(this.deviceListeners);
        myTimer = new Timer();
        myTimer.schedule(myTask, 1000, 9000);

        // assigns the textboxes to the objects
//        TextView dateTime = (TextView) findViewById(R.id.basic_user_date_and_time_output_textView);

        // gets the current date and time, down to the second and places
        // both inside the textbox
//        String currentDT = DateFormat.getDateTimeInstance().format(new Date());
//        dateTime.setText(currentDT);


    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
//        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_basic_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.basic_user_action_settings) {
            return true;
        }
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    // method that changes the activity on the screen for experts
    public void goToExpert(View view) {
        Intent intent = new Intent(this, ExpertActivity.class);
        startActivity(intent);
    }

    // method that changes the activity on the screen for experts
    public void showParkDialogFragment(Zone z) {
        ParkDialogFragment parkDialogFragment = new ParkDialogFragment();
        parkDialogFragment.mListener = deviceListeners;
        parkDialogFragment.z=z;
        parkDialogFragment.show(getSupportFragmentManager(), "map");
    }

    public void tapEvent(int x, int y) {
        Zone z = mapTransform.getZoneTapped(x,y);
        if (z!=null){
//            Toast.makeText(getApplicationContext(),"Tapped zone "+z.getZoneId(),Toast.LENGTH_LONG).show();
//            DatabaseExchange.sendVote(this,z,1);
            double va = Double.valueOf(DatabaseExchange.getAverageVote(this,z));
            z.setFullness(va);
//            Toast.makeText(getApplicationContext(),"Vote avg: "+va,Toast.LENGTH_LONG).show();
            showParkDialogFragment(z);
        }

    }
    public void updateZone(Zone z) {
        double va = Double.valueOf(DatabaseExchange.getAverageVote(this,z));
        for ( Zone zone : zoneList) {
            if (zone.getZoneId().equals(z.getZoneId())) {
                zone.setFullness(z.getFullness());
                mapTransform.updateZones(zoneList);
            }
        }

    }

}
