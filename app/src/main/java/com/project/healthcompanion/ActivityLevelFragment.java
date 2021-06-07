package com.project.healthcompanion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.project.healthcompanion.databinding.FragmentActivityLevelBinding;

import org.jetbrains.annotations.NotNull;


public class ActivityLevelFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private UserInfoViewModel userInfoViewModel;
    //private ActivityLevelViewModel viewModel;

    private String selectedActivityLevel;
    private FragmentActivityLevelBinding binding;

    public ActivityLevelFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentActivityLevelBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();


        //initialize ArrayAdapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.activityLevels, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.activityLevelSpinner.setAdapter(adapter);
        binding.activityLevelSpinner.setOnItemSelectedListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userInfoViewModel = new ViewModelProvider(requireActivity()).get(UserInfoViewModel.class);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedActivityLevel = parent.getItemAtPosition(position).toString();
        userInfoViewModel.setActivityLevel(selectedActivityLevel);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}