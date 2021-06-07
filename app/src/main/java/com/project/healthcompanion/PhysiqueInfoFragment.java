package com.project.healthcompanion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.project.healthcompanion.databinding.FragmentPhysiqueInfoBinding;

import org.jetbrains.annotations.NotNull;

public class PhysiqueInfoFragment extends Fragment {

    private FragmentPhysiqueInfoBinding binding;
    private UserInfoViewModel userInfoViewModel;

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
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPhysiqueInfoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userInfoViewModel = new ViewModelProvider(requireActivity()).get(UserInfoViewModel.class);
    }

    @Override
    public void onPause() {
        Toast.makeText(getContext(), "saving physical info", Toast.LENGTH_SHORT);
        userInfoViewModel.setHeight(binding.editTextHeight.getText().toString());
        userInfoViewModel.setWeight(binding.editTextWeight.getText().toString());
        super.onPause();
    }
}