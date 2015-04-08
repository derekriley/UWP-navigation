package uwp.cs.edu.parkingtracker;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


public class MainActivity extends Activity {
    ProgressBar pBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
