package usi.memotion2personal.UI.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import usi.memotion2personal.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutApplicationFragment extends Fragment {


    public AboutApplicationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.about_application, container, false);

    }

}
