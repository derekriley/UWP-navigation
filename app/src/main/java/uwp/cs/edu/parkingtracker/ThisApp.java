package uwp.cs.edu.parkingtracker;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by nate eisner on 4/8/15.
 */
public class ThisApp extends Application {
    Tracker t;
    private final String PROPERTY_ID = "UA-61649168-1";

    @Override
    public void onCreate() {
        super.onCreate();
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
        t = analytics.newTracker(PROPERTY_ID);
    }

    synchronized Tracker getTracker() {
        return t;
    }
}
