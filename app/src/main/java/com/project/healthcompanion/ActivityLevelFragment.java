package com.project.healthcompanion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


public class ActivityLevelFragment extends Fragment implements AdapterView.OnItemSelectedListener {


    public ActivityLevelFragment() {
        // Required empty public constructor
    }


    public static ActivityLevelFragment newInstance() {
        return new ActivityLevelFragment();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity_level, container, false);

        //get view
        Spinner activityLevelSpinner = view.findViewById(R.id.activityLevelSpinner);
        //initialize ArrayAdapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.activityLevels, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activityLevelSpinner.setAdapter(adapter);
        activityLevelSpinner.setOnItemSelectedListener(this);

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedActivityLevel = parent.getItemAtPosition(position).toString();
        Toast.makeText(getContext(), selectedActivityLevel, Toast.LENGTH_LONG);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}