package com.project.healthcompanion;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.project.healthcompanion.Service.Food;
import com.project.healthcompanion.Service.NutritionixOverlay;
import com.project.healthcompanion.databinding.ActivityFoodDialogBinding;

public class FoodDialogActivity extends AppCompatActivity {

    ActivityFoodDialogBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFoodDialogBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String foodName = extras.getString(getPackageName() + "foodName");

            NutritionixOverlay nutritionixOverlay = new NutritionixOverlay(this);
            nutritionixOverlay.getFood(foodName, new NutritionixOverlay.NutrientResponse() {
                @Override
                public void onSuccess(Food food) {
                    binding.imageViewFoodImage.setImageBitmap(food.getHiResImage());
                    binding.textViewFoodNameDialog.setText(food.getFood_name());

                    binding.textViewCalValue.setText(food.getCalories().toString() + " g");
                    binding.textViewCarbsValue.setText(food.getTotalCarbohydrate().toString() + " g");
                    binding.textViewFatsValue.setText(food.getTotalFat().toString() + " g");
                    binding.textViewProtsValue.setText(food.getProtein().toString() + " g");

                    binding.textViewServingCal.setText(food.getServingUnit());
                    binding.textViewServingFats.setText(food.getServingUnit());
                    binding.textViewServingCarbs.setText(food.getServingUnit());
                    binding.textViewServingProts.setText(food.getServingUnit());

                }

                @Override
                public void onError(String message) {
                    Toast.makeText(FoodDialogActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

}