package com.project.healthcompanion;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.project.healthcompanion.databinding.FragmentSetGoalBinding;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SetGoalFragment extends Fragment {

    private static final String ARG_WEIGHT = "weight";
    private static final String ARG_HEIGHT = "height";
    private static final Float minHealthyBMI = (float) 18.5, maxHealthyBMI = (float) 24.9, maxOverWeightBMI = (float) 29.9;
    private FragmentSetGoalBinding binding;
    private Float weight, height, BMI;

    public SetGoalFragment() {
        // Required empty public constructor
    }


    public static SetGoalFragment newInstance(Float weight, Float height) {
        SetGoalFragment fragment = new SetGoalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_WEIGHT, String.valueOf(weight));
        args.putString(ARG_HEIGHT, String.valueOf(height));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weight = SetGoalFragmentArgs.fromBundle(getArguments()).getWeight();
        height = SetGoalFragmentArgs.fromBundle(getArguments()).getHeight();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSetGoalBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        BMI = new BigDecimal(Double.toString(weight / (height * height)))
                .setScale(2, RoundingMode.HALF_UP).floatValue();
        if (BMI <= minHealthyBMI) {
            binding.textViewCondition.setText("Underweight range");
        } else if (BMI > minHealthyBMI && BMI <= maxHealthyBMI) {
            binding.textViewCondition.setText("Healthy range");
        } else if (BMI > maxHealthyBMI && BMI <= maxOverWeightBMI) {
            binding.textViewCondition.setText("Overweight range");
        } else if (BMI > maxOverWeightBMI) {
            binding.textViewCondition.setText("Obese range");
        }

        BigDecimal minWeight = new BigDecimal(Double.toString(minHealthyBMI * height * height + 0.05));
        minWeight = minWeight.setScale(1, RoundingMode.HALF_UP);

        BigDecimal maxWeight = new BigDecimal(Double.toString(maxHealthyBMI * height * height - 0.05));
        maxWeight = maxWeight.setScale(1, RoundingMode.HALF_UP);

        binding.textViewWeightRange.setText(minWeight + " - " + maxWeight);

        binding.buttonDone.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), HomePage.class);
            startActivity(intent);
        });

        return view;
    }
}