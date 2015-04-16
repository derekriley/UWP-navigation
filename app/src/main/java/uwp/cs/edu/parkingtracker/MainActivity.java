package uwp.cs.edu.parkingtracker;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import uwp.cs.edu.parkingtracker.mapping.MapTransform;
import uwp.cs.edu.parkingtracker.parking.ParkDialogFragment;

/**
 * Created by nate eisner on 4/14/15.
 */
public class MainActivity extends ActionBarActivity {
    private static MainActivity Instance;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ProgressBar progress;
    private MapTransform mapTransform;

    protected DeviceListeners deviceListeners = null;

    public static MainActivity getInstance() {
        return Instance;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Instance = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupTools();

        setupGoogleAnalytics();

        // Setup map.
        mapTransform = new MapTransform(MainActivity.this);
        mapTransform.setUpMap();

        // Being restored from a previous state,
        // then we don't need to do anything and should return or else
        // we could end up with overlapping fragments.
        if (savedInstanceState != null) {
            return;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(Gravity.START);
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

    /*
    * Triggers a call to get the tapped zone from other methods
    *
    * */
    public void tapEvent(int x, int y) {
        String zInfo = mapTransform.getZoneTapped(x, y);
        if (zInfo != null) {
            showParkDialogFragment(zInfo);
        }

    }


    //setup toolbar draw loadingbar
    public void setupTools() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            //toolbar
            toolbar.setVisibility(View.VISIBLE);
            toolbar.setNavigationIcon(R.drawable.ic_ab_drawer);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    switch (menuItem.getItemId()) {
                        case R.id.action_park:
                            Toast.makeText(MainActivity.this, "Park", Toast.LENGTH_SHORT).show();
                            return true;
                    }

                    return false;
                }
            });
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //drawer
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        if (drawer != null) {
            drawer.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);
        }
        if (progress != null) {
            progress = (ProgressBar) findViewById(R.id.loadingProgress);
            progress.setMax(CONSTANTS.zones.size());
        }
    }

    //gets devicelisteners
    public DeviceListeners getDeviceListeners() {
        if (deviceListeners == null) {
            // Instantiate new device listener.
            deviceListeners = new DeviceListeners(this);
        }
        return deviceListeners;
    }
}
