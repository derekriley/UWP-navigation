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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import uwp.cs.edu.parkingtracker.parking.ZoneService;

/**
 * Creates a menu activity layout that will help the user choose between the
 * parking application and the navigation application
 */
public class MenuActivity extends Activity {

    private SharedPreferences preferences;
    private SharedPreferences.Editor prefEditor;
    private int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private TextView statusText;
    private Button studentBtn;
    private Button visitorBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        //preferences on app
        preferences = getSharedPreferences(CONSTANTS.PREFS_NAME, Context.MODE_PRIVATE);
        prefEditor = preferences.edit();

        //find UI objects
        statusText = (TextView) findViewById(R.id.status);
        studentBtn = (Button) findViewById(R.id.buttonStudents);
        visitorBtn = (Button) findViewById(R.id.buttonVisitors);

        statusChecker();

        final Handler timerHandler = new Handler();
        //timer
        Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                statusChecker();
                timerHandler.postDelayed(this, 5000);
                return;

            }
        };
        //start service loop
        timerHandler.postDelayed(timerRunnable, 2500);

        // Google Analytics
        // Get tracker.
        Tracker t = ((ThisApp) getApplication()).getTracker();

        // Set screen name.
        t.setScreenName("Menu");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder()
                .setNewSession()
                .build());
        // Google Analytics
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //enable buttons based off of status
    private void setButtons(boolean status) {
        studentBtn.setEnabled(status);
        visitorBtn.setEnabled(status);
    }

    //student button clicked
    public void studentClick(View v) {
        setRole("student");
        startMainActivity();
    }

    //visitor button clicked
    public void visitorClick(View v) {
        setRole("visitor");
        startMainActivity();
    }

    //sets the user's role
    private void setRole(String role) {
        prefEditor.putString("role", role);
        prefEditor.commit();
    }

    //starts main activity
    private void startMainActivity() {
        checkPlayServices();

        ProgressDialog pd = new ProgressDialog(this);
        pd.setIndeterminate(true);
        pd.show();

        Intent mServiceIntent = new Intent(getApplicationContext(), ZoneService.class);
        mServiceIntent.addCategory(ZoneService.TAG);
        startService(mServiceIntent);

        Intent mIntent = new Intent(MenuActivity.this, MainActivity.class);
        startActivity(mIntent);
        pd.dismiss();
        finish();

    }

    //play service checker
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                finish();
            }
            return false;
        }
        return true;
    }

    //checks services
    private void statusChecker() {
        Log.i("Status", "Checking status");
        if (!isConnectingToInternet()) {
            setButtons(false);
            statusText.setText("Check Network Connection");
        }
        if (isConnectingToInternet()) {
            setButtons(true);
            statusText.setText("");
        }
    }

    //returns the status of internet connectivity
    private boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        Toast.makeText(this, "No network connection", Toast.LENGTH_SHORT).show();
        return false;
    }
}
