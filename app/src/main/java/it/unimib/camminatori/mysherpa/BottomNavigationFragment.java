package it.unimib.camminatori.mysherpa;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BottomNavigationFragment extends Fragment {

    public static BottomNavigationFragment newInstance(String param1, String param2) {
        BottomNavigationFragment bottomNavigationFragment = new BottomNavigationFragment();
        return bottomNavigationFragment;
    }

    public BottomNavigationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bottom_navigation, container, false);
        return rootView;
    }
}