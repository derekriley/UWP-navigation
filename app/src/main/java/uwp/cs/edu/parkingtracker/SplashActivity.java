package uwp.cs.edu.parkingtracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


public class SplashActivity extends Activity {
    //Introduce an delay
    private final int WAIT_TIME = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.activity_splash);
        //Intent mainIntent = new Intent(SplashActivity.this,BasicUser.class);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Service  */
                ZoneList.getInstance().update();
                Intent menuIntent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(menuIntent);
                SplashActivity.this.finish();
            }
        }, WAIT_TIME);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
