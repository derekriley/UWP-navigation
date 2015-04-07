package uwp.cs.edu.parkingtracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().hide();

    }

    public void parkingClick(View view) {
        Intent pIntent = new Intent(this, BasicUser.class);
        startActivity(pIntent);
        finish();
    }

    public void navClick(View view) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

}
