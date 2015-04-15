package uwp.cs.edu.parkingtracker;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by nathaneisner on 4/14/15.
 */
public class MainActivity extends ActionBarActivity {
    private static MainActivity Instance;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    final private int FRAG_CONTAINER = R.id.fragment_container;

    public static MainActivity getInstance() {  return Instance; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Instance = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);

        //set fragment to menu to start off
        MenuFragment firstFragment = new MenuFragment();
        // Add the fragment to the 'fragment_container' FrameLayout
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(FRAG_CONTAINER,firstFragment);
        ft.commit();


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

//    @Override
//    public void onButtonSelected(int position) {
//        if (position == 0) {
//            //parking selected
//            ParkingFragment parkingFragment = new ParkingFragment();
//            FragmentTransaction ft = getFragmentManager().beginTransaction();
//            ft.replace(FRAG_CONTAINER,parkingFragment);
//            ft.addToBackStack(null);
//            ft.commit();
//        }
//        if (position == 1) {
//            //navigation selected
//            ParkingFragment parkingFragment = new ParkingFragment();
//            FragmentTransaction ft = getFragmentManager().beginTransaction();
//            ft.replace(FRAG_CONTAINER,parkingFragment);
//            ft.addToBackStack(null);
//            ft.commit();
//        }
//    }

    /*
    * Triggers a call to get the tapped zone from other methods
    *
    * */
    public void tapEvent(int x, int y) {
//        String zInfo = mapTransform.getZoneTapped(x, y);
//        if (zInfo != null) {
//            showParkDialogFragment(zInfo);
//        }

    }

    public void parkingClick(View v) {
        //parking selected
        setupTools();
        ParkingFragment parkingFragment = new ParkingFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(FRAG_CONTAINER,parkingFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void setupTools() {
        //toolbar
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setNavigationIcon(R.drawable.ic_ab_drawer);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //drawer
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);
    }
}
