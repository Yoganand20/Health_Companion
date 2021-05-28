package com.project.healthcompanion;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login_Fragment extends Fragment {

    private EditText EditText_email,EditText_password;
    private Button button_login;

    private FirebaseAuth mAuth;

    public Login_Fragment() {
        // Required empty public constructor
    }


    public static Login_Fragment newInstance() {
        return new Login_Fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        EditText_email = view.findViewById(R.id.editTextTextEmailAddress);
        EditText_password = view.findViewById(R.id.editTextTextPassword);
        mAuth = FirebaseAuth.getInstance();
        button_login = view.findViewById(R.id.button_logIn);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password;
                email = EditText_email.getText().toString();
                password = EditText_password.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getContext(), "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getContext(), "Please enter correct password", Toast.LENGTH_LONG).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getActivity(), HomePage.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getContext(), "Login Unsuccessful. Try again", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        return view;
    }
}