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
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import uwp.cs.edu.parkingtracker.mapping.MapTransform;
import uwp.cs.edu.parkingtracker.parking.ParkDialogFragment;
import uwp.cs.edu.parkingtracker.parking.ParkingSpotDialogFragment;
import uwp.cs.edu.parkingtracker.parking.ZoneService;

/**
 * Created by nate eisner on 4/14/15.
 */
public class MainActivity extends ActionBarActivity {
    private ProgressBar progress;
    private MapTransform mapTransform = null;
    private DeviceListeners deviceListeners = null;
    private final int SERVICE_DELAY = 30000;
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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            setupTools();
            return;
        }
        setContentView(R.layout.activity_main);
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
        deviceListeners = getDeviceListeners();
        //create map
        if (mapTransform == null) {
            mapTransform = new MapTransform(MainActivity.this);
            mapTransform.setUpMap();
        }
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
        LocalBroadcastManager.getInstance(this).unregisterReceiver(loadingStatus);
        if (mServiceIntent != null) {
            stopService(mServiceIntent);
        }
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
        return true;
    }

    //when an option item is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

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
        Tracker t = ((ThisApp) getApplication()).getTracker();

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
        parkDialogFragment.setListener(deviceListeners);
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
            drawerItems = new String[]{"Information" , "uwp.edu", "Events", "Admissions" };
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

    //gets devicelisteners
    public DeviceListeners getDeviceListeners() {
        if (deviceListeners == null) {
            // Instantiate new device listener.
            deviceListeners = new DeviceListeners(this);
        }
        return deviceListeners;
    }

    //setup the bottom panel
    private void setupBottomPanel() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //hide bottom sliding panel
                SlidingUpPanelLayout sUPL = ((SlidingUpPanelLayout) findViewById(R.id.sliding_layout));
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

    //receiver to get loading status and loading amount
    private BroadcastReceiver loadingStatus = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //gets status of loading
            loadComplete = intent.getBooleanExtra(CONSTANTS.DATA_STATUS, true);
            if (!loadComplete) {
                if (progress != null) {
                    progress.setVisibility(View.VISIBLE);
                    //gets amount of zones loaded
                    int status = intent.getIntExtra(CONSTANTS.DATA_AMOUNT, 0);
                    progress.setProgress(status);
                }
            }
            if (loadComplete) {
                loadingComplete();
            }
        }
    };

    //ran when service loadingcomplete
    private void loadingComplete() {
        progress.setProgress(0);
        if (pD.isShowing()) {
            pD.dismiss();
        }
        mapTransform.refreshMap();
        mServiceIntent = new Intent(MainActivity.this, ZoneService.class);
        // Timer
        final Handler timerHandler = new Handler();
        //server refresh timer
        Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                startService(mServiceIntent);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Handle the back button
        if(keyCode == KeyEvent.KEYCODE_BACK) {
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

            return true;
        }
        else {
            return super.onKeyDown(keyCode, event);
        }

    }
}
