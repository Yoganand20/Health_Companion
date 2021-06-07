package com.project.healthcompanion;

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
import com.project.healthcompanion.databinding.FragmentLoginBinding;

import org.jetbrains.annotations.NotNull;

public class Login_Fragment extends Fragment {

    private FragmentLoginBinding binding;

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
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        mAuth = FirebaseAuth.getInstance();

        binding.buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password;
                email = binding.editTextTextEmailAddress.getText().toString();
                password = binding.editTextTextPassword.getText().toString();

                if (!Validate.ValidateEmail(binding.editTextTextEmailAddress)
                        || !Validate.CheckPassword(binding.editTextTextPassword)) {
                    return;
                }
                ProgressBar progressBar = requireActivity().findViewById(R.id.progressBar_ls);
                progressBar.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getActivity(), HomePage.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getContext(), "Login Unsuccessful. Wrong Credentials", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        return view;
    }
}