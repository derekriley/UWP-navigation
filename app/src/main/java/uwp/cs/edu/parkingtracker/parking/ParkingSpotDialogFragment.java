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
