package com.project.healthcompanion;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.healthcompanion.databinding.FragmentPhysiqueInfoBinding;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PhysiqueInfoFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private FragmentPhysiqueInfoBinding binding;
    private String activityLevel;
    private Float weight, height, calculated_PAL, calculated_TDEE, calculated_BMI;

    //jonny's variable
    FirebaseFirestore db;

    public PhysiqueInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();
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
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (binding.editTextHeight.getText().toString().isEmpty()) return;

                height = Float.parseFloat(binding.editTextHeight.getText().toString());
                updateValues();
            }
        });

        binding.editTextWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (binding.editTextWeight.getText().toString().isEmpty()) return;
                weight = Float.parseFloat(binding.editTextWeight.getText().toString());
                updateValues();
            }
        });


        binding.buttonNext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeToDB();
                NavDirections action = PhysiqueInfoFragmentDirections.actionPhysiqueInfoFragmentToSetGoalFragment(weight, height / 100);
                Navigation.findNavController(view).navigate(action);
            }
        });

        return view;
    }

    private void storeToDB() {
        //TODO: Store physical info in DB
        //synatx to get values:   Data_type value = binding.editText___{1}___.getText().toString();
        // {1}->Name of the edit field

        //create user's profile doc:
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("myTag",currentUser);


        //Float weight, height, calculated_PAL, calculated_TDEE, calculated_BMI;

        Float userWeight = weight;
        Float userHeight = height;
        //String usersHeight = binding.editTextHeight.getText().toString(); is he storing string values or float?
        Float userCalculated_TDEE = calculated_TDEE;
        Float userCalculated_BMI = calculated_BMI;
        Float userCalculated_PAL = calculated_PAL;

            /*String firstName = binding.editTextFirstName.getText().toString();
            String lastName = binding.editTextLastName.getText().toString();
            String userGender = gender;
            Date userDOB = dateOfBirth;*/

        Map<String, Object> profileDataPhysique = new HashMap<>();
        profileDataPhysique.put("Weight", userWeight);
        profileDataPhysique.put("Height", userHeight);
        profileDataPhysique.put("TDEE", userCalculated_TDEE);
        profileDataPhysique.put("BMI", userCalculated_BMI);
        profileDataPhysique.put("PAL", userCalculated_PAL);

        db.collection("profiles").document(currentUser).collection("profile categories").document("physical")
                .set(profileDataPhysique)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Write", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Write", "Error writing document", e);
                    }
                });

    }

    private void setCalculatedValues() {
        binding.editTextNumberPAL.setText(calculated_PAL.toString());
        binding.editTextNumberTDEE.setText(calculated_TDEE.toString());
        binding.editTextNumberBMI.setText(calculated_BMI.toString());
    }

    private void updateValues() {
        float BMR, TEF, EEE, NEAT;
        //BMR Calculation
        if (weight == null || height == null) {
            calculated_BMI = (float) 0;
            calculated_PAL = (float) 0;
            calculated_TDEE = (float) 0;
            setCalculatedValues();
            return;
        }

        float height_inMeters = new BigDecimal(Float.toString(height / 100)).setScale(2, RoundingMode.HALF_UP).floatValue();
        calculated_BMI = weight / (height_inMeters * height_inMeters);

        if (activityLevel.equals(getResources().getString(R.string.light_activity_lifestyle))) {
            //TDEE Calculation
            BMR = weight * 20;
            TEF = BMR * (float) 0.1;
            EEE = 250;//estimate
            NEAT = 250;//estimate
            calculated_TDEE = BMR + TEF + EEE + NEAT;

            //PAL Calculation
            calculated_PAL = (float) 1.53;//estimate
        } else if (activityLevel.equals(getResources().getString(R.string.moderate_activity_lifestyle))) {
            //TDEE Calculation
            BMR = weight * 20;
            TEF = BMR * (float) 0.1;
            EEE = 37;//estimate
            NEAT = 375;//estimate
            calculated_TDEE = BMR + TEF + EEE + NEAT;

            //PAL Calculation
            calculated_PAL = (float) 1.76;//estimate
        } else if (activityLevel.equals(getResources().getString(R.string.vigorous_activity_lifestyle))) {
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
        calculated_BMI = new BigDecimal(Float.toString(calculated_BMI)).setScale(2, RoundingMode.HALF_UP).floatValue();

        calculated_TDEE = new BigDecimal(Float.toString(calculated_TDEE)).setScale(2, RoundingMode.HALF_UP).floatValue();

        calculated_PAL = new BigDecimal(Float.toString(calculated_PAL)).setScale(2, RoundingMode.HALF_UP).floatValue();
        setCalculatedValues();

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