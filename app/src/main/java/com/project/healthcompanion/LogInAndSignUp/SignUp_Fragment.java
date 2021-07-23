package com.project.healthcompanion.LogInAndSignUp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.project.healthcompanion.R;
import com.project.healthcompanion.Service.Validate;
import com.project.healthcompanion.databinding.FragmentSignUpBinding;


public class SignUp_Fragment extends Fragment {

    private FragmentSignUpBinding binding;

    FirebaseAuth mAuth;

    public SignUp_Fragment() {
        // Required empty public constructor
    }

    public static SignUp_Fragment newInstance() {
        return new SignUp_Fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        mAuth = FirebaseAuth.getInstance();

        binding.buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = binding.editTextTextNewEmailAddress.getText().toString();
                password = binding.editTextTextNewPassword.getText().toString();


                if (!Validate.ValidateEmail(binding.editTextTextNewEmailAddress)
                        || !Validate.ValidatePassword(binding.editTextTextNewPassword, binding.editTextTextConfirmPassword)) {
                    return;
                }

                ProgressBar progressBar = requireActivity().findViewById(R.id.progressBar_ls);
                progressBar.setVisibility(View.VISIBLE);


               /* if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getContext(), "Please enter valid email address", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
                    return;
                }
*/
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Registration Successful", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getActivity(), GetUserInfoActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getContext(), "Registration Unsuccessful. Email address already registered.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    return view;
    }
}