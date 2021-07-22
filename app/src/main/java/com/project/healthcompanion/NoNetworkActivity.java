package com.project.healthcompanion;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.narify.netdetect.NetDetect;
import com.project.healthcompanion.databinding.ActivityNoNetworkBinding;

public class NoNetworkActivity extends AppCompatActivity {
    ActivityNoNetworkBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoNetworkBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        NetDetect.init(this);

        NetDetect.check(isConnected -> {
            if (isConnected)
                finish();
        });

        binding.lytNoConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.lytNoConnection.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        NetDetect.check(isConnected -> {
                            if (isConnected) finish();
                            binding.progressBar.setVisibility(View.GONE);
                            binding.lytNoConnection.setVisibility(View.VISIBLE);
                        });
                    }
                }, 1000);

            }
        });


    }
/*
    private boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isAvailable()
                && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting()) {
            return true;
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.lytNoConnection.setVisibility(View.VISIBLE);
            return false;
        }
    }*/
}