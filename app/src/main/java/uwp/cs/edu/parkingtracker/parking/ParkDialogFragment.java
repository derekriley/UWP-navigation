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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;


/**
 * Created by kifle on 7/9/2014.
 * Modified by David Krawchuk, Russ, Nate
 *
 * */
public class ParkDialogFragment extends DialogFragment {

    protected String zID;
    protected String fullness;
    protected ParkDialogListener mListener;
    protected int fullnessValue;


    public interface ParkDialogListener {
        public void onDialogSend(DialogFragment dialog, int val);
    }


    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     * */
    static ParkDialogFragment newInstance(String zID, String fullness) {
        ParkDialogFragment f = new ParkDialogFragment();
        f.zID = zID;
        f.fullness = fullness;
        // Supply num input as an argument.
//        Bundle args = new Bundle();
//        args.putInt("num", num);
//        f.setArguments(args);

        return f;
    }

    /**
     * Called at the initial phase of fragment's lifecycle.
     *
     * @param activity
     */
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try {
//            mListener = (ParkDialogListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement ParkDialogListener");
//        }
    }

    /**
     * Called after onAttach as part of the fragment lifecycle.
     *
     * @param savedInstanceState
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder votePopup = new AlertDialog.Builder(getActivity());
        votePopup.setTitle("How full is this zone?");

        LinearLayout linear = new LinearLayout(getActivity());
        linear.setOrientation(LinearLayout.VERTICAL);
        SeekBar fullnessSlider = new SeekBar(getActivity());

        fullnessSlider.setMax(4);
        fullnessSlider.setPadding(25,50,25,-10);
        final TextView result = new TextView(getActivity());
        result.setPadding(20, 10, 10 , 10);
        result.setText("0 %");

        linear.addView(fullnessSlider);
        linear.addView(result);

        votePopup.setView(linear);

        fullnessSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int fullness, boolean fromUser){
                fullnessValue = fullness;
                result.setText(fullness*25 + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        votePopup.setPositiveButton("Send", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onDialogSend(ParkDialogFragment.this, fullnessValue);
                dismiss();
            }
        });
        votePopup.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        return votePopup.create();
    }

    public String getID () {return this.zID;};

    public void setListener (ParkDialogListener pdL) {
        mListener = pdL;
    }
    /**
     * catchs the illegal state exception in two methods
     * and ignore it, allowing the app to continue running.
     * */
    @Override
    public int show(FragmentTransaction transaction, String tag) {
        try {
            return super.show(transaction, tag);
        } catch (IllegalStateException e) {
            // ignore
        }
        return -1;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            super.show(manager, tag);
        } catch (IllegalStateException e) {
            // ignore
        }
    }

    public void setzID(String zID) {
        this.zID = zID;
    }
}
