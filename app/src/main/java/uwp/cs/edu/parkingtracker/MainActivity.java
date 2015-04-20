package uwp.cs.edu.parkingtracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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
    //Service
    private Intent mServiceIntent = null;
    private boolean initialLoad = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Instance = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getSharedPreferences(CONSTANTS.PREFS_NAME, 0);
        setupGoogleAnalytics();
        setupTools();
        setupService();
        setupBottomPanel();
        deviceListeners = getDeviceListeners();

        // Setup map if null
        if (mapTransform == null) {
            mapTransform = new MapTransform(MainActivity.this);
            mapTransform.setUpMap();
        }

        // Being restored from a previous state,
        // then we don't need to do anything and should return or else
        // we could end up with overlapping fragments.
        if (savedInstanceState != null) {
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener when the application is paused
        LocalBroadcastManager.getInstance(this).unregisterReceiver(loadingStatus);
        stopService(mServiceIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register for the particular broadcast based on ACTION string
        IntentFilter filter = new IntentFilter(ZoneService.ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(loadingStatus, filter);
        setupService();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

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

    private void setupGoogleAnalytics () {
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

//    /*
//    * Triggers a call to get the tapped zone from other methods
//    * */
//    public void tapEvent(int x, int y) {
//        String zInfo = mapTransform.getZoneTapped(x, y);
//        if (zInfo != null) {
//            showParkDialogFragment(zInfo);
//        }
//
//    }

    private boolean handleMenuItem(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_park:
                //TODO: Set a dialog to REPARK? UNPARK?
                mapTransform.attachNewParkingSpot();
                Toast.makeText(MainActivity.this, "Parked!", Toast.LENGTH_SHORT).show();
                return true;
        }

        return false;
    }

    //setup toolbar draw
    public void setupTools() {
        progress = (ProgressBar) findViewById(R.id.loadingProgress);
        if (progress != null) {
            progress.setMax(CONSTANTS.zones.size());
        }
        mDrawerList = (ListView)findViewById(R.id.navList);mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer);
        mActivityTitle = getTitle().toString();

        setupDrawer();
        addDrawerItems();
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

    //add items to drawer
    private void addDrawerItems() {
        // get role for drawer customization
        String role = preferences.getString("role","");
        if(role.equals("student")) {
            drawerItems = new String[]{ "uwp.edu", "D2L", "SOLAR", "Campus Connect"};
        }
        else {
            drawerItems = new String[]{""};
        }
        mAdapter = new ArrayAdapter<String>(this,R.layout.color_textview, drawerItems);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "Link", Toast.LENGTH_SHORT).show();
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

    //sets up zone service handler
    private void setupService() {
        if (mServiceIntent == null) {
            mServiceIntent = new Intent(this,ZoneService.class);
        }
        // Timer
        final Handler timerHandler = new Handler();
        //server refresh timer
        Runnable timerRunnable = new Runnable() {

            @Override
            public void run() {
                if (!initialLoad) {
                    startService(mServiceIntent);
                } else {
                    initialLoad = false;
                }
                //restart after SERVICE_DELAY
                timerHandler.postDelayed(this, SERVICE_DELAY);
            }
        };
        //start service loop
        timerHandler.postDelayed(timerRunnable, SERVICE_DELAY+100);

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
                ((SlidingUpPanelLayout)findViewById(R.id.sliding_layout)).setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            }
        },1000);
    }

    //used to launch new browser intent with given url
    private void openUrl(String url){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    /**
     * Custom BroadcastReceiver for loading zones, displaying progress and calling to refresh map
     * when complete
     */
    private BroadcastReceiver loadingStatus = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean loadComplete= intent.getBooleanExtra(CONSTANTS.DATA_STATUS,false);
            if(!loadComplete) {
                    if (progress != null) {
                        progress.setVisibility(View.VISIBLE);
                        int status = intent.getIntExtra(CONSTANTS.DATA_AMOUNT, 0);
                        progress.setProgress(status);
                    }
            }
            if (loadComplete) {
                progress.setVisibility(View.GONE);
                mapTransform.refreshMap();
            }
        }
    };
}
