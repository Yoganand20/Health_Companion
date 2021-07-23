package com.project.healthcompanion.LogInAndSignUp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.healthcompanion.HomePage;
import com.project.healthcompanion.databinding.FragmentSetGoalBinding;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class SetGoalFragment extends Fragment {
    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    FirebaseFirestore firebaseFirestore;

    private static final String ARG_WEIGHT = "weight";
    private static final String ARG_HEIGHT = "height";
    private static final Float minHealthyBMI = (float) 18.5, maxHealthyBMI = (float) 24.9, maxOverWeightBMI = (float) 29.9;
    private FragmentSetGoalBinding binding;
    private Float weight, height, BMI;
    DocumentReference suggcalDocref;

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

        firebaseFirestore = FirebaseFirestore.getInstance();

        suggcalDocref = firebaseFirestore.collection("profiles").document(currentUser).collection("profile categories").document("suggested calories");

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
            storeToDB();
            Intent intent = new Intent(requireActivity(), HomePage.class);
            startActivity(intent);
        });

        return view;
    }

    public void storeToDB() {

        Float goal_weight_value = Float.parseFloat(binding.editTextGoalWeight.getText().toString());

        Map<String, Object> setGoal = new HashMap<>();
        setGoal.put("goal weight", goal_weight_value);

        firebaseFirestore.collection("profiles").document(currentUser).collection("profile categories").document("goal")
                .set(setGoal)
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

        Map<String, Object> setPace = new HashMap<>();
        setPace.put("pace", "slow");
        suggcalDocref.set(setPace);
    }
}