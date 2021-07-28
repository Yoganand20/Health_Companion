package com.project.healthcompanion;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.narify.netdetect.NetDetect;
import com.project.healthcompanion.databinding.ActivityNoNetworkBinding;

public class NoNetworkActivity extends AppCompatActivity {
    ActivityNoNetworkBinding binding;

    //press back twice to exit
    private boolean backPressedOnce = false;
    private Toast t;

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

    @Override
    public void onBackPressed() {
        if (backPressedOnce) {
            t.cancel();
            ActivityCompat.finishAffinity(NoNetworkActivity.this);
            finish();
        }
        backPressedOnce = true;
        t = Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT);
        t.show();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                backPressedOnce = false;
            }
        }, 2000);
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