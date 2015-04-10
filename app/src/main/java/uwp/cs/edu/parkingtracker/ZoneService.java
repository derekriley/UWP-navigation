/**
 *
 *   Copyright 2014 University Of Wisconsin Parkside
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package uwp.cs.edu.parkingtracker;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Service to handle background zone syncing.
 * Created by nate eisner
 */
public class ZoneService extends IntentService {

   //An IntentService must always have a constructor that calls the super constructor. The
   //string supplied to the super constructor is used to give a name to the IntentService's
   //background thread.

    public ZoneService() {

        super("ZoneService");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        ZoneList.getInstance().update();
        Log.d("ZoneService", "Loading from server");
    }
}
