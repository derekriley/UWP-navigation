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

package uwp.cs.edu.parkingtracker.parking;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import uwp.cs.edu.parkingtracker.R;
import uwp.cs.edu.parkingtracker.mapping.MapTransform;

/**
 * Created by nathaneisner on 4/28/15.
 */
public class ParkingSpotDialogFragment extends DialogFragment{
    MapTransform mapTransform;

    public void setMapTransform(MapTransform mt) {
        this.mapTransform = mt;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.parked_dia)
                .setPositiveButton(R.string.to_park, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //parked
                        mapTransform.attachNewParkingSpot();
                        Toast.makeText(getActivity(), "Parked!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setCancelable(true)
                .setNegativeButton(R.string.not_park, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        mapTransform.removeParkingSpot();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
