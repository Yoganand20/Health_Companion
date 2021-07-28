package com.project.healthcompanion;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.narify.netdetect.NetDetect;
import com.project.healthcompanion.databinding.FragmentNoNetworkBinding;

import org.jetbrains.annotations.NotNull;


public class NoNetworkFragment extends Fragment {

    FragmentNoNetworkBinding binding;
    NavController navController;

    public NoNetworkFragment() {
        // Required empty public constructor
    }


    public static NoNetworkFragment newInstance(String param1, String param2) {
        NoNetworkFragment fragment = new NoNetworkFragment();
        Bundle args = new Bundle();
        //set args here
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //get args from here
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNoNetworkBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        navController = NavHostFragment.findNavController(this);

        NetDetect.init(requireContext());
/*
        NetDetect.check(isConnected -> {
            if (isConnected){
                getParentFragmentManager().popBackStackImmediate();
            }
        });*/

        binding.lytNoConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.lytNoConnection.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        NetDetect.check(isConnected -> {
                            if (isConnected) getParentFragmentManager().popBackStackImmediate();
                            binding.progressBar.setVisibility(View.GONE);
                            binding.lytNoConnection.setVisibility(View.VISIBLE);
                        });
                    }
                }, 1000);

            }
        });
        return view;
    }
}