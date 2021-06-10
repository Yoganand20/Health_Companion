package com.project.healthcompanion;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.healthcompanion.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        checkInternetConnection();

        binding.lytNoConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.lytNoConnection.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkInternetConnection();
                    }
                }, 1000);

            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isAvailable()
                && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting()) {
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
            return true;
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.lytNoConnection.setVisibility(View.VISIBLE);
            return false;
        }
    }

}