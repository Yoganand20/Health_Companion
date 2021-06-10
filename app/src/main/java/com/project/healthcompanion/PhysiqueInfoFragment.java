package com.project.healthcompanion;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.fragment.app.Fragment;

import com.project.healthcompanion.databinding.FragmentPhysiqueInfoBinding;

import org.jetbrains.annotations.NotNull;

public class PhysiqueInfoFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private FragmentPhysiqueInfoBinding binding;
    private String activityLevel;
    private Float weight, height, calculated_PAL, calculated_TDEE, calculated_BMI;

    public PhysiqueInfoFragment() {
        // Required empty public constructor
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

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.activityLevels, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.activityLevelSpinner.setAdapter(adapter);
        binding.activityLevelSpinner.setOnItemSelectedListener(this);

        //reset textViews
        binding.textViewResetBMI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.editTextNumberBMI.setText(calculated_BMI.toString());
            }
        });

        binding.textViewResetPAL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.editTextNumberPAL.setText(calculated_PAL.toString());
            }
        });

        binding.textViewResetTDEE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.editTextNumberTDEE.setText(calculated_TDEE.toString());
            }
        });


        binding.editTextHeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                weight = Float.parseFloat(binding.editTextWeight.getText().toString());
                updateValues();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.editTextWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                height = Float.parseFloat(binding.editTextHeight.getText().toString());
                updateValues();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        binding.buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeToDB();
                Intent intent = new Intent(requireActivity(), HomePage.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void storeToDB() {
        //TODO: Store physical info in DB
        //synatx to get values:   Data_type value = binding.editText___{1}___.getText().toString();
        // {1}->Name of the edit field
    }

    private void setCalculatedValues() {
        binding.editTextNumberPAL.setText(calculated_PAL.toString());
        binding.editTextNumberTDEE.setText(calculated_TDEE.toString());
        binding.editTextNumberBMI.setText(calculated_BMI.toString());
    }

    private void updateValues() {
        float BMR, TEF, EEE, NEAT;
        //BMR Calculation
        if (weight == null || height == null)
            return;

        if (activityLevel.equals(getResources().getString(R.string.light_activity_lifestyle))) {
            calculated_BMI = weight / (height * height);
            //TDEE Calculation
            BMR = weight * 20;
            TEF = BMR * (float) 0.1;
            EEE = 250;//estimate
            NEAT = 250;//estimate
            calculated_TDEE = BMR + TEF + EEE + NEAT;

            //PAL Calculation
            calculated_PAL = (float) 1.53;//estimate
        } else if (activityLevel.equals(getResources().getString(R.string.moderate_activity_lifestyle))) {
            calculated_BMI = weight / (height * height);

            //TDEE Calculation
            BMR = weight * 20;
            TEF = BMR * (float) 0.1;
            EEE = 37;//estimate
            NEAT = 375;//estimate
            calculated_TDEE = BMR + TEF + EEE + NEAT;

            //PAL Calculation
            calculated_PAL = (float) 1.76;//estimate
        } else if (activityLevel.equals(getResources().getString(R.string.vigorous_activity_lifestyle))) {
            calculated_BMI = weight / (height * height);
            //TDEE Calculation
            BMR = weight * 20;
            TEF = BMR * (float) 0.1;
            EEE = 37;//estimate
            NEAT = 375;//estimate
            calculated_TDEE = BMR + TEF + EEE + NEAT;

            //PAL Calculation
            calculated_PAL = (float) 2.25;//estimate
        } else {
            calculated_BMI = (float) 0;
            calculated_TDEE = (float) 0;
            calculated_PAL = (float) 0;

        }

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        activityLevel = parent.getItemAtPosition(position).toString();
        updateValues();
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}