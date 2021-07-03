package com.project.healthcompanion;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.project.healthcompanion.Service.Food;
import com.project.healthcompanion.databinding.ActivityDashboardBinding;
import com.project.healthcompanion.databinding.DialogDashboardBinding;

import java.sql.Date;

public class DashboardActivity extends AppCompatActivity {
    DietPlan dietPlan;
    ActivityResultLauncher<Intent> searchResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent intent = result.getData();
                    if (intent == null) {
                        Toast.makeText(DashboardActivity.this, "Error getting search result", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Food selectedFood = intent.getParcelableExtra("SearchResult");
                }
            });
    private ActivityDashboardBinding binding;
    private Date today;
    private double totalCaloriesConsumed, totalCaloriesPlanned;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        binding.textViewMacroBreakdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DashboardActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                loadDialog();
            }
        });
        dietPlan = getDietPlan();
        displayDietPlan(dietPlan);
        setContentView(view);
    }

    private void displayDietPlan(DietPlan dietPlan) {
    }

    private DietPlan getDietPlan() {
        DietPlan dietPlan = new DietPlan();
        //connect to db and retrieve Diet Plan
        //return diet plan
        return dietPlan;
    }

    private void loadDialog() {
        DialogDashboardBinding binding = DialogDashboardBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        binding.btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}