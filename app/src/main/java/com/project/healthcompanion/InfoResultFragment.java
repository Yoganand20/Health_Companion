package com.project.healthcompanion;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.project.healthcompanion.databinding.FragmentInfoResultBinding;

import org.jetbrains.annotations.NotNull;

public class InfoResultFragment extends Fragment {
    private FragmentInfoResultBinding binding;
    private UserInfoViewModel userInfoViewModel;
    private Integer activityLevel;
    private Float PAL, TDEE, BMI, calculated_PAL, calculated_TDEE, calculated_BMI;

    public InfoResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInfoResultBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        binding.textViewResetAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCalculatedValues();
            }
        });

        binding.textViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableEditTextView(binding.editTextNumberBMI);
                enableEditTextView(binding.editTextNumberPAL);
                enableEditTextView(binding.editTextNumberTDEE);
                binding.buttonSave.setVisibility(View.VISIBLE);
                binding.buttonDiscard.setVisibility(View.VISIBLE);
            }
        });

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

        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableEditTextView(binding.editTextNumberPAL);
                disableEditTextView(binding.editTextNumberTDEE);
                disableEditTextView(binding.editTextNumberBMI);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userInfoViewModel = new ViewModelProvider(requireActivity()).get(UserInfoViewModel.class);
        userInfoViewModel.getActivityLevel().observe(getViewLifecycleOwner(), value -> {
            updateUI(value);
        });
    }

    private void updateUI(Integer value) {
        //calculate values
    }

    private void setCalculatedValues() {
        binding.editTextNumberPAL.setText(calculated_PAL.toString());
        binding.editTextNumberTDEE.setText(calculated_TDEE.toString());
        binding.editTextNumberBMI.setText(calculated_BMI.toString());
    }

    private void disableEditTextView(@NotNull EditText editText) {
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
        editText.setInputType(InputType.TYPE_NULL);
    }

    private void enableEditTextView(@NotNull EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
    }
}