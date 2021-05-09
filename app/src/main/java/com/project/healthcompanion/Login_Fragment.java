package com.project.healthcompanion;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.text.TextUtilsCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Login_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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
    public void onStart() {
        super.onStart();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_login, container, false);

        EditText_email=view.findViewById(R.id.editTextTextEmailAddress);
        EditText_password=view.findViewById(R.id.editTextTextNewPassword);
        mAuth=FirebaseAuth.getInstance();
        button_login = view.findViewById(R.id.button_logIn);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password;
                email = EditText_email.getText().toString();
                password = EditText_password.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getContext(), "Please enter email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getContext(), "Please enter password", Toast.LENGTH_LONG).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_LONG);
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getContext(), "Login Unsuccessful", Toast.LENGTH_LONG);
                                }
                            }
                        });
            }
        });

        return view;
    }



}


