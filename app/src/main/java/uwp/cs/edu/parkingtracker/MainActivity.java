package uwp.cs.edu.parkingtracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


public class MainActivity extends Activity {
    public static final String PREFS_NAME = "UwpNavSettings";
    ProgressBar pBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // check if role preference is given, if not ask
        SharedPreferences pref = getSharedPreferences(PREFS_NAME,0);

        // TODO uncomment me
        //if(pref.getString("role","") == "") {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Choose Role");
            alertDialog.setMessage("Are you a student or a visitor of UW-Parkside?");
            alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Student", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    setRole("student");
                }
            });
            alertDialog.setButton(Dialog.BUTTON_NEUTRAL, "Visitor", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    setRole("visitor");
                }
            });
            alertDialog.show();
       // }

        getActionBar().hide();

        /* Google Analytics */
        // Get tracker.
        Tracker t = ((ThisApp) getApplication()).getTracker();

        // Set screen name.
        t.setScreenName("Menu");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder()
                .setNewSession()
                .build());
        /* Google Analytics */

        pBar = (ProgressBar)findViewById(R.id.parkingProgressBar);
        new LoadTask().execute();


    }
    private void setRole(String role) {
        SharedPreferences pref = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.putString("role", role);
        prefEditor.commit();

        // TODO delete me
        Toast.makeText(getApplicationContext(),"Your role is now: " + pref.getString("role",""), Toast.LENGTH_LONG).show();
    }

    private class LoadTask extends AsyncTask<Void, Void, Void> {

        /** This method runs on a background thread (not on the UI thread) */
        @Override
        protected Void doInBackground(Void... params) {
            ZoneList.getInstance().update();
            int i = 0;
            while (i <= 50) {
                try {
                    Thread.sleep(100);
                    i++;
                }
                catch (Exception e) {
                }
            }
            return null;
        }

        /**
         * Called after doInBackground() method
         * This method runs on the UI thread
         */
        @Override
        protected void onPostExecute(Void v) {
            Button btn = (Button)findViewById(R.id.button);
            btn.setEnabled(true);
            pBar.setVisibility(View.GONE);
        }
    }

    public void parkingClick(View view) {
        Intent mIntent  = new Intent(MainActivity.this, BasicUser.class);
        startActivity(mIntent);
        finish();
    }

    public void navClick(View view) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
