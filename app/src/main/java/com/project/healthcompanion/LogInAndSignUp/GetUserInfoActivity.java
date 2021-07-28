package com.project.healthcompanion.LogInAndSignUp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.project.healthcompanion.databinding.ActivityGetUserInfoBinding;

public class GetUserInfoActivity extends AppCompatActivity {
    private ActivityGetUserInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGetUserInfoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

    //press back twice to exit
    private boolean backPressedOnce = false;
    private Toast t;

    @Override
    public void onBackPressed() {
        if (backPressedOnce) {
            t.cancel();
            ActivityCompat.finishAffinity(GetUserInfoActivity.this);
            finish();
        }
        backPressedOnce = true;
        t = Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT);
        t.show();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                backPressedOnce = false;
            }
        }, 2000);
    }
}