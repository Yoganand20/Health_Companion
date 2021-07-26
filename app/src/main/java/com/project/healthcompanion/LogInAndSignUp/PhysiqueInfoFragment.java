package com.project.healthcompanion.LogInAndSignUp;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.healthcompanion.R;
import com.project.healthcompanion.databinding.FragmentPhysiqueInfoBinding;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PhysiqueInfoFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    String gender;
    Date datOfBirth;

    private FragmentPhysiqueInfoBinding binding;
    boolean isCustom = false;
    private Float weight, height, BMI, PAL, TDEE;

    //jonny's variable
    FirebaseFirestore db;

    TextWatcher heightWeightWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (binding.editTextWeight.getText().toString().isEmpty() ||
                    binding.editTextHeight.getText().toString().isEmpty()) return;

            weight = Float.parseFloat(binding.editTextWeight.getText().toString());
            height = Float.parseFloat(binding.editTextHeight.getText().toString());

            calculateBMI();
        }
    };


    public PhysiqueInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gender = PhysiqueInfoFragmentArgs.fromBundle(getArguments()).getGender();
        datOfBirth = PhysiqueInfoFragmentArgs.fromBundle(getArguments()).getDOB();

        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPhysiqueInfoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.activityLevelsWithCustom,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.activityLevelSpinner.setAdapter(adapter);
        binding.activityLevelSpinner.setOnItemSelectedListener(this);

        binding.editTextHeight.addTextChangedListener(heightWeightWatcher);
        binding.editTextWeight.addTextChangedListener(heightWeightWatcher);

        binding.editTextNumberPAL.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty() || !isCustom) return;
                PAL = Float.valueOf(s.toString());
                calculateTDEE();
                showTDEE();
            }
        });

        binding.buttonNext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeToDB();
                NavDirections action = PhysiqueInfoFragmentDirections.actionPhysiqueInfoFragmentToSetGoalFragment(
                        weight,
                        height / 100,
                        gender
                );
                Navigation.findNavController(view).navigate(action);
            }
        });

        return view;
    }

    private void storeToDB() {

        //create user's profile doc:
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("myTag", currentUser);


        Float userWeight = weight;
        Float userHeight = height;
        Float userCalculated_TDEE = TDEE;
        Float userCalculated_BMI = BMI;
        Float userCalculated_PAL = PAL;


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

        //storing the user's initial weight as the first entry of records
        Date rec_date;
        rec_date = new Date();

        Map<String, Object> data = new HashMap<>();
        data.put("record date", rec_date);
        data.put("weight", userWeight);

        db.collection("records").document(currentUser).collection("Graph Data").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("WriteRec", "Document Snapshot written with ID: " + documentReference.getId() + " || Record date:" + rec_date + /*", Entry number:" + x +*/ ", Weight:" + userWeight);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.w("WriteRec", "Error adding docuemnt", e);
            }
        });
    }

    private void calculateBMI() {
        //BMI Calculation
        float height_inMeters = new BigDecimal(Float.toString(height / 100)).setScale(2, RoundingMode.HALF_UP).floatValue();
        BMI = weight / (height_inMeters * height_inMeters);
        BMI = new BigDecimal(Float.toString(BMI)).setScale(2, RoundingMode.HALF_UP).floatValue();
        showBMI();
    }

    private void showBMI() {
        binding.editTextBMI.setText(BMI.toString());
        Log.d("BMI", String.valueOf(BMI));
    }

    private void calculateTDEE() {
        //BMR Calculation
        float BMR;
        int age = getAge(datOfBirth);
        if (gender.equals("Male")) {
            BMR = (weight * 10.0f) + (height * 6.25f) - (age * 5.0f) + 5.0f;
            Log.d("BMR", String.valueOf(BMR));
        } else if (gender.equals("Female")) {
            BMR = (weight * 10.0f) + (height * 6.25f) - (age * 5.0f) - 161.0f;
            Log.d("BMR", String.valueOf(BMR));
        } else {
            Log.d(getTag(), "some problem occurred: gender:" + gender);
            return;
        }

        TDEE = BMR * PAL;
        Log.d("Calculated TDEE", String.valueOf(TDEE));


        TDEE = new BigDecimal(Float.toString(TDEE)).setScale(2, RoundingMode.HALF_UP).floatValue();
        PAL = new BigDecimal(Float.toString(PAL)).setScale(2, RoundingMode.HALF_UP).floatValue();
    }

    private void showPAL() {

        binding.editTextNumberPAL.setText(PAL.toString());
        Log.d("PAL", String.valueOf(PAL));
    }

    private void showTDEE() {
        binding.editTextNumberTDEE.setText(TDEE.toString());
        Log.d("TDEE", String.valueOf(TDEE));
    }


    private int getAge(Date date) {
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

        String CurrentYear = yearFormat.format(Calendar.getInstance().getTime());
        String dobYear = yearFormat.format(date);

        return Integer.parseInt(CurrentYear) - Integer.parseInt(dobYear);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                binding.editTextNumberPAL.setText("");
                return;
            case 1:
                isCustom = false;
                binding.editTextNumberPAL.setFocusableInTouchMode(false);
                binding.editTextNumberPAL.setFocusable(false);
                PAL = 1.53f;
                break;
            case 2:
                isCustom = false;
                binding.editTextNumberPAL.setFocusableInTouchMode(false);
                binding.editTextNumberPAL.setFocusable(false);
                PAL = 1.76f;
                break;
            case 3:
                isCustom = false;
                binding.editTextNumberPAL.setFocusableInTouchMode(false);
                binding.editTextNumberPAL.setFocusable(false);
                PAL = 2.25f;
                break;
            case 4:
                isCustom = true;
                binding.editTextNumberPAL.setFocusableInTouchMode(true);
                binding.editTextNumberPAL.setFocusable(true);
                return;
        }
        calculateTDEE();
        showPAL();
        showTDEE();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}