package com.project.healthcompanion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.narify.netdetect.NetDetect;
import com.project.healthcompanion.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(view);
        NetDetect.init(this);

        NetDetect.check(isConnected -> {
            if (!isConnected) {
                Intent intent = new Intent(MainActivity.this, NoNetworkActivity.class);
                startActivity(intent);
            }
            // get instance of firebaseAuth
            mAuth = FirebaseAuth.getInstance();

            //get current user if logged in
            FirebaseUser currentUser = mAuth.getCurrentUser();

            if (currentUser != null) { //if user is logged in continue to home screen
                Intent intent = new Intent(this, HomePage.class);
                startActivity(intent);
            } else {//else open login/sign up activity
                Intent LoginNSignUp = new Intent(this, LoginNSignUpActivity.class);
                startActivity(LoginNSignUp);
            }
        });


    }
}