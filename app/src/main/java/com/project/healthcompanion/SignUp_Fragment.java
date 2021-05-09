package com.project.healthcompanion;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUp_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUp_Fragment extends Fragment {


    EditText editText_email,editText_newPassword,editText_confirmPassword;
    Button button_signUp;
    FirebaseAuth mAuth;
    public SignUp_Fragment() {
        // Required empty public constructor
    }


    public static SignUp_Fragment newInstance(String param1, String param2) {
        SignUp_Fragment fragment = new SignUp_Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        mAuth=FirebaseAuth.getInstance();
        editText_email = view.findViewById(R.id.editTextTextNewEmailAddress);
        editText_newPassword = view.findViewById(R.id.editTextTextNewPassword);
        editText_confirmPassword = view.findViewById(R.id.editTextTextConfirmPassword);
        button_signUp = view.findViewById(R.id.button_signUp);
        button_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password, c_password;
                email = editText_email.getText().toString();
                password = editText_newPassword.getText().toString();
                c_password = editText_confirmPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getContext(), "Please enter email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getContext(), "Please enter password", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(c_password)) {
                    Toast.makeText(getContext(), "Please confirm your password", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!TextUtils.equals(password, c_password)) {
                    Toast.makeText(getContext(), "Password do not match", Toast.LENGTH_LONG).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Registration Successful", Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent(getActivity(),MainActivity.class);
                                } else {
                                    Toast.makeText(getContext(), "Registration Unsuccessful", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    return view;
    }


}