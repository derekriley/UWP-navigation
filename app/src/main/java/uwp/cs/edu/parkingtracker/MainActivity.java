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

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import uwp.cs.edu.parkingtracker.mapping.MapTransform;
import uwp.cs.edu.parkingtracker.parking.ParkDialogFragment;
import uwp.cs.edu.parkingtracker.parking.ParkingSpotDialogFragment;
import uwp.cs.edu.parkingtracker.parking.ZoneService;

/**
 * Created by Nate Eisner on 4/14/15.
 */
public class MainActivity extends ActionBarActivity implements LocationListener {
    //variables
    private ProgressBar progress;
    private MapTransform mapTransform = null;
    private final int SERVICE_DELAY = 20000;
    private final int TIMEOUT = 45000;
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private ActionBar actionBar;
    private String[] drawerItems;
    private SharedPreferences preferences;
    private boolean loadComplete;
    private Intent mServiceIntent = null;
    private ProgressDialog pD;
    private Menu actionBarMenu;
    protected LocationManager locationManager;
    private SlidingUpPanelLayout slidingUpPanel;
    private ThisApp thisApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            setupTools();
            return;
        }
        setContentView(R.layout.activity_main);
        thisApp = (ThisApp) getApplication();
        thisApp.setMain(this);

        //progress dialog for initial loading
        pD = new ProgressDialog(this, R.style.TransparentProgressDialog);
        pD.setIndeterminate(true);
        pD.setCancelable(false);
        pD.show();
        //get prefs for roles
        preferences = getSharedPreferences(CONSTANTS.PREFS_NAME, 0);
        //set up different parts
        setupGoogleAnalytics();
        setupTools();
        setupBottomPanel();
        //create map
        if (mapTransform == null) {
            mapTransform = new MapTransform(MainActivity.this);
            mapTransform.setUpMap();
        }
        //location manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 6000, 10, this);
        }
        slidingUpPanel = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);

        //server is taking too long so disable loading dialog
        final Handler timeOutHandler = new Handler();
        Runnable timeOut = new Runnable() {
            @Override
            public void run() {
                if (isMyServiceRunning(ZoneService.class)) {
                    //service is taking too long
                    if (pD.isShowing()) {
                        pD.dismiss();
                    }
                    Toast.makeText(getApplicationContext(), "Network Trouble", Toast.LENGTH_LONG).show();
                }
                return;
            }
        };
        timeOutHandler.postDelayed(timeOut, TIMEOUT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!loadComplete && mServiceIntent != null) {
            stopService(mServiceIntent);
        }
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener when the application is paused
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(loadingStatus);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register for the particular broadcast based on ACTION string
        IntentFilter filter = new IntentFilter(ZoneService.ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(loadingStatus, filter);
        if (mapTransform == null) {
            mapTransform = new MapTransform(MainActivity.this);
            mapTransform.setUpMap();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(loadingStatus);
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

    //assign options menu to bar menu object
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        actionBarMenu = menu;
        actionBarMenu.findItem(R.id.action_cancel).setVisible(false);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            actionBarMenu.findItem(R.id.action_park).setVisible(true);
        } else if (locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            actionBarMenu.findItem(R.id.action_park).setVisible(true);
        } else {
            //location is not working
            actionBarMenu.findItem(R.id.action_park).setVisible(false);
        }
        return true;
    }

    //when an option item is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        handleMenuItem(item);

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //all google analytics should be set here
    private void setupGoogleAnalytics() {
        // Google Analytics

        // Get tracker.
        Tracker t = thisApp.getTracker();

        // Set screen name.
        t.setScreenName("Main");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder()
                .setNewSession()
                .build());
    }

    //shows parkdialogfragment
    public void showParkDialogFragment(String zID) {
        ParkDialogFragment parkDialogFragment = new ParkDialogFragment();
        parkDialogFragment.setzID(zID);
        parkDialogFragment.show(getSupportFragmentManager(), "map");
    }

    //handles and changes from a menu item selected
    private boolean handleMenuItem(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_cancel:
                mapTransform.clearPath();
                setCancelItem(false);
                return true;
            case R.id.action_park:
                ParkingSpotDialogFragment dia = new ParkingSpotDialogFragment();
                dia.setMapTransform(mapTransform);
                dia.show(getFragmentManager(), "Diag");
                return true;
            case R.id.action_student:
                modifyDrawerItems("student");
                return true;
            case R.id.action_visitor:
                modifyDrawerItems("visitor");
                return true;
        }

        return false;
    }

    //setup toolbar draw
    public void setupTools() {
        progress = (ProgressBar) findViewById(R.id.loadingProgress);
        if (progress != null) {
            progress.setMax(CONSTANTS.zones.size());
            progress.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            progress.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        }
        mDrawerList = (ListView) findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mActivityTitle = getTitle().toString();

        setupDrawer();
        // get role for drawer customization
        String role = preferences.getString("role", "");
        modifyDrawerItems(role);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    //sets up side drawer
    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                actionBar.setTitle("Links");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                actionBar.setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    //change items to drawer based off of role.
    private void modifyDrawerItems(String role) {
        //STUDENT ROLE
        if (role.equals("student")) {
            drawerItems = new String[]{"uwp.edu", "D2L", "SOLAR", "Campus Connect"};
        }
        if (role.equals("visitor")) {
            //TODO: CHANGE LINKS BASED OFF OF VISITOR
            drawerItems = new String[]{"Information", "uwp.edu", "Events", "Admissions"};
        }
        mAdapter = new ArrayAdapter<>(this, R.layout.color_textview, drawerItems);
        mDrawerList.setAdapter(mAdapter);

        //for handling links in the side drawer
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivity.this, "Link", Toast.LENGTH_SHORT).show();
                switch (drawerItems[position]) {
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
                    case "Events":
                        openUrl("http://www.uwp.edu/events.cfm");
                        break;
                    case "Information":
                        openUrl("http://www.uwp.edu/explore/aboutuwp/index.cfm");
                        break;
                    case "Admissions":
                        openUrl("http://www.uwp.edu/apply/admissions/index.cfm");
                        break;
                }
            }
        });
    }

    //setup the bottom panel
    private void setupBottomPanel() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //hide bottom sliding panel
                SlidingUpPanelLayout sUPL = ((SlidingUpPanelLayout)
                        findViewById(R.id.sliding_layout));
                //sUPL.setVisibility(View.GONE);
                sUPL.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            }
        }, 1000);
    }

    //used to launch new browser intent with given url
    private void openUrl(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    public boolean isLoadingComplete() {
        return loadComplete;
    }

    //receiver to get loading status and loading amount
    private BroadcastReceiver loadingStatus = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //gets status of loading
            loadComplete = intent.getBooleanExtra(CONSTANTS.DATA_STATUS, true);
            if (!loadComplete) {
                if (progress != null) {
                    progress.setVisibility(View.VISIBLE);
                    progress.setIndeterminate(false);
                    //gets amount of zones loaded
                    int status = intent.getIntExtra(CONSTANTS.DATA_AMOUNT, 0);
                    progress.setProgress(status);
                }
            }
        }
    };

    //ran when service loading complete
    public void loadingComplete() {
        loadComplete = true;
        progress.setProgress(0);
        progress.setIndeterminate(false);
        if (pD.isShowing()) {
            pD.dismiss();
        }
        mapTransform.refreshMap();

        // Timer
        final Handler timerHandler = new Handler();
        //server refresh timer
        Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                serviceRunner();
                return;
            }
        };
        //start service loop
        timerHandler.postDelayed(timerRunnable, SERVICE_DELAY);
    }

    //shows cancel button in the actionbar
    public void setCancelItem(boolean option) {
        actionBarMenu.findItem(R.id.action_cancel).setVisible(option);
        //    invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        //Handle the back button
        //if the sliding is open
        if (slidingUpPanel.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || slidingUpPanel.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED) {
            slidingUpPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            //Ask the user if they want to quit
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Exit")
                    .setMessage("Do you wish to exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Stop the activity
                            MainActivity.this.finish();
                            System.exit(0);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //do nothing
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //status of gps changed
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 10000,
                    1, this);
            actionBarMenu.findItem(R.id.action_park).setVisible(true);
        } else if (locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 10000,
                    1, this);
            actionBarMenu.findItem(R.id.action_park).setVisible(true);
        } else {
            //location is not working
            Toast.makeText(getApplicationContext(), "Please Check Location", Toast.LENGTH_LONG).show();
            actionBarMenu.findItem(R.id.action_park).setVisible(false);
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        //location is enabled
        actionBarMenu.findItem(R.id.action_park).setVisible(true);
    }

    @Override
    public void onProviderDisabled(String provider) {
        //location is disabled
        Toast.makeText(getApplicationContext(), "Please Enable Location", Toast.LENGTH_LONG).show();
        if (actionBarMenu != null) {
            actionBarMenu.findItem(R.id.action_park).setVisible(false);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void serviceRunner() {
        Intent mServiceIntent = new Intent(getApplicationContext(), ZoneService.class);
        mServiceIntent.addCategory(ZoneService.TAG);
        final Handler timeOutHandler = new Handler();
        Runnable timeOut = new Runnable() {
            @Override
            public void run() {
                if (isMyServiceRunning(ZoneService.class)) {
                    //service is taking too long
                    if (pD.isShowing()) {
                        pD.dismiss();
                    }
                    Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
                }
                return;
            }
        };
        mServiceIntent.addCategory(ZoneService.TAG);
        startService(mServiceIntent);
        timeOutHandler.postDelayed(timeOut, 45000);
    }
}
