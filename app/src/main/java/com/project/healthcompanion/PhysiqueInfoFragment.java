package com.project.healthcompanion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class PhysiqueInfoFragment extends Fragment {

    public PhysiqueInfoFragment() {
        // Required empty public constructor
    }

    public static PhysiqueInfoFragment newInstance() {
        return new PhysiqueInfoFragment();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_physique_info, container, false);
        return view;
    }
}