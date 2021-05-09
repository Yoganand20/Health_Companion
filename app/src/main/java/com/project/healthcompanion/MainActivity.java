package com.project.healthcompanion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get instance of firebaseAuth
        mAuth= FirebaseAuth.getInstance();
        //get current user if logged in
        FirebaseUser currentUser=mAuth.getCurrentUser();
        if(currentUser!=null){ //if user is logged in continue
            TextView tv =findViewById(R.id.msg);
            tv.setText("Hi");
        }
        else{//else open login/sign up activity
            Intent LoginNSignUp=new Intent(this,LoginNSignUpActivity.class);
            startActivity(LoginNSignUp);
        }
    }
}