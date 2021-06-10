package com.project.healthcompanion;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.project.healthcompanion.databinding.ActivityGetUserInfoBinding;

public class GetUserInfo extends AppCompatActivity {
    private ActivityGetUserInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGetUserInfoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

    }
}

