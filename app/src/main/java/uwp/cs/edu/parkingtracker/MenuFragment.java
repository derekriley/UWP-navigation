package uwp.cs.edu.parkingtracker;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {
//    OnMenuSelectedListener mCallback;
//
//    // The container Activity must implement this interface so the frag can deliver messages
//    public interface OnMenuSelectedListener {
//        /** Called by HeadlinesFragment when a list item is selected */
//        public void onButtonSelected(int position);
//    }

    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity mainAct = MainActivity.getInstance();

        if (mainAct!=null) {
            // Google Analytics
            // Get tracker.
            Tracker t = ((ThisApp) mainAct.getApplication()).getTracker();

            // Set screen name.
            t.setScreenName("Menu");

            // Send a screen view.
            t.send(new HitBuilders.ScreenViewBuilder()
                    .setNewSession()
                    .build());
            // Google Analytics
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);

    }
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//
//        // This makes sure that the container activity has implemented
//        // the callback interface. If not, it throws an exception
//        try {
//            mCallback = (OnMenuSelectedListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnMenuSelectedListener");
//        }
//    }
}
