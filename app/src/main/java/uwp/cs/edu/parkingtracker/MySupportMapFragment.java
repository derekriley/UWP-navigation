package uwp.cs.edu.parkingtracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by Russ on 3/18/2015.
 */

public class MySupportMapFragment extends SupportMapFragment {
    private View mOriginalContentView;
    private TouchableWrapper mTouchView;
    private BasicUser mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (BasicUser) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        mOriginalContentView = super.onCreateView(inflater, parent,
                savedInstanceState);
        mTouchView = new TouchableWrapper();
        mTouchView.addView(mOriginalContentView);
        return mTouchView;
    }

    @Override
    public View getView() {
        return mOriginalContentView;
    }

    class TouchableWrapper extends FrameLayout {

        public TouchableWrapper() {
            super(mActivity);
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_UP: {
                    int x = (int) event.getX();
                    int y = (int) event.getY();
                    mActivity.tapEvent(x, y);
                    break;
                }
            }
            return super.dispatchTouchEvent(event);
        }
    }

}
