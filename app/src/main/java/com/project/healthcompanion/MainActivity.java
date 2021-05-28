package com.project.healthcompanion;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        finish();
    }
}