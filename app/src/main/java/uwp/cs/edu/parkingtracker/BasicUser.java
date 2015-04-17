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
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


//TODO: Implement a network connectivity checker and google play services check
//ConnectivityManager connMgr = (ConnectivityManager)
//this.activity.getSystemService(Context.CONNECTIVITY_SERVICE);

public class BasicUser extends FragmentActivity {

    /* Instance variables begin */

    private String[] drawerItems = {"Parking", "Navigate", "Other"};
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    protected DeviceListeners deviceListeners = null;
    protected MapTransform mapTransform;


    public DeviceListeners getDeviceListeners() {
        if (deviceListeners == null) {
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

        /* Google Analytics */

        // Get tracker.
        Tracker t = ((ThisApp) getApplication()).getTracker();

        // Set screen name.
        t.setScreenName("BasicUser");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder()
                .setNewSession()
                .build());
        /* Google Analytics */

        /*    START NAV DRAWER     */
        //added nav drawer
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        // Set the adapter for the list view

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

        /*   END  NAV DRAWER     */

        // Setup device listeners.
        getDeviceListeners();

        /* Service  */
        final Intent mServiceIntent = new Intent(this, ZoneService.class);

        /* Timer */
        final Handler timerHandler = new Handler();

        Runnable timerRunnable = new Runnable() {

            @Override
            public void run() {
                startService(mServiceIntent);

                timerHandler.postDelayed(this, 10000);
            }
        };
        timerHandler.postDelayed(timerRunnable, 10000);
        Runnable timerRunnable2 = new Runnable() {

            @Override
            public void run() {
                mapTransform.refreshMap();
                timerHandler.postDelayed(this, 5000);
            }
        };
        timerHandler.postDelayed(timerRunnable2, 5000);


        // Setup map.
        mapTransform = new MapTransform(BasicUser.this);
        mapTransform.setUpMap();

//        myTask = new ParkingLotTimer(this.deviceListeners,mServiceIntent);
//        myTimer = new Timer();
//        myTimer.schedule(myTask, 1000, 9000);
    }

    @Override
    public void onStart(){
        super.onStart();

        // get role for drawer customization
        SharedPreferences pref = getSharedPreferences(MainActivity.PREFS_NAME, 0);

        if(pref.getString("role","").equals("student")) {
            drawerItems = new String[]{"Parking", "Navigate", "Other", "D2L", "SOLAR", "Campus Connect", "uwp.edu"};
        }
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, drawerItems));

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "item " + position + "selected", Toast.LENGTH_LONG).show();

                switch (drawerItems[position]){
                    case "D2L":
                        openUrl("https://uwp.courses.wisconsin.edu/Shibboleth.sso/Login?target=https://uwp.courses.wisconsin.edu/d2l/shibbolethSSO/deepLinkLogin.d2l");
                        break;
                    case "SOLAR":
                        openUrl("https://solar.uwp.edu/solar/signon.html");
                        break;
                    case "Campus Connect":
                        openUrl("https://campusconnect.uwp.edu/");
                        break;
                    case "uwp.edu":
                        openUrl("http://www.uwp.edu/");
                        break;
                }
            }
        });
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
//        if (id == R.id.basic_user_action_settings) {
//            return true;
//        }
        if (id == R.id.exit) {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
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


    public void showParkDialogFragment(String zID) {
        ParkDialogFragment parkDialogFragment = new ParkDialogFragment();
        parkDialogFragment.mListener = deviceListeners;
        parkDialogFragment.setzID(zID);
        parkDialogFragment.show(getSupportFragmentManager(), "map");
    }

    public void tapEvent(int x, int y) {
        String zInfo = mapTransform.getZoneTapped(x, y);
        if (zInfo != null) {
            showParkDialogFragment(zInfo);
        }

    }

    private void openUrl(String url){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

}
