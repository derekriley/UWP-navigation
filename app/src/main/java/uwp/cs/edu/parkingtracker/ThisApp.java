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

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 *
 * Maintains all needs associated with google analytics
 * Created by nate eisner on 4/8/15.
 * */
public class ThisApp extends Application {
    Tracker t;
    private final String PROPERTY_ID = "UA-61649168-1";
    private MainActivity mainActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
        t = analytics.newTracker(PROPERTY_ID);
    }

    public synchronized Tracker getTracker() {
        return t;
    }

    public void setMain(MainActivity main) {
        mainActivity = main;
    }

    public MainActivity getMain() {
        return mainActivity;
    }
}