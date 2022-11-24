package it.unimib.camminatori.mysherpa;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class UserPageFragment extends Fragment {

    public UserPageFragment() {
        // Required empty public constructor
    }

    public static UserPageFragment newInstance() {
        UserPageFragment userPageFragment = new UserPageFragment();
        return userPageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_userpage, container, false);
    }
}
