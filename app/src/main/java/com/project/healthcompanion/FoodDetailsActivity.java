package com.project.healthcompanion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.project.healthcompanion.Model.Food;
import com.project.healthcompanion.Service.NutritionixOverlay;
import com.project.healthcompanion.databinding.ActivityFoodDetailsBinding;

import org.jetbrains.annotations.NotNull;

public class FoodDetailsActivity extends AppCompatActivity {
    ActivityFoodDetailsBinding binding;
    Food food;
    Integer qty = 1;
    View.OnClickListener selectButton_onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            food.setServingQty(qty);
            Intent intent = new Intent();
            intent.putExtra("selectedFood", FoodDetailsActivity.this.food);
            intent.putExtra("quantity", FoodDetailsActivity.this.qty);
            setResult(RESULT_OK, intent);
            finish();
        }
    };
    View.OnClickListener backButton_onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setResult(RESULT_CANCELED);
            finish();
        }
    };

    View.OnClickListener plusButton_onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FoodDetailsActivity.this.qty++;
            binding.editTextQty.setText(FoodDetailsActivity.this.qty.toString());
        }
    };
    View.OnClickListener minusButton_onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (FoodDetailsActivity.this.qty <= 1) {
                return;
            }
            FoodDetailsActivity.this.qty--;
            binding.editTextQty.setText(FoodDetailsActivity.this.qty.toString());

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFoodDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);

        binding.editTextQty.setText(qty.toString());
        binding.buttonSelect.setOnClickListener(selectButton_onClickListener);
        binding.buttonBack.setOnClickListener(backButton_onClickListener);
        binding.imageButtonPlus.setOnClickListener(plusButton_onClickListener);
        binding.imageButtonMinus.setOnClickListener(minusButton_onClickListener);

        //check if there is any saved instance and restore activity
        if (savedInstanceState != null) {
            this.food = savedInstanceState.getParcelable(getPackageName() + "savedFoodInstance");
            displayFoodInfo(food);
        } else {
            //check for extra that were sent when starting activity and use it to retrieve food info from API
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String foodName = extras.getString(getPackageName() + "foodName");
                binding.progressBar.setVisibility(View.VISIBLE);
                NutritionixOverlay nutritionixOverlay = new NutritionixOverlay(this);
                nutritionixOverlay.getFood(foodName, new NutritionixOverlay.NutrientResponse() {
                    @Override
                    public void onSuccess(Food food) {
                        FoodDetailsActivity.this.food = food;
                        binding.progressBar.setVisibility(View.GONE);
                        displayFoodInfo(food);
                    }

                    @Override
                    public void onError(String message) {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(FoodDetailsActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                });
            }

            //if no extra were sent then there was problem in displaying
            else {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(FoodDetailsActivity.this, "Some problem occurred while fetching data.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(getPackageName() + "savedFoodInstance", food);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.food = savedInstanceState.getParcelable(getPackageName() + "savedFoodInstance");
        displayFoodInfo(food);
    }

    private void displayFoodInfo(Food food) {
        binding.imageViewFoodPic.setImageBitmap(food.getHiResImage());
        binding.textViewFoodNameDialog.setText(food.getFood_name());

        binding.textViewCalValue.setText(food.getCalories().toString() + " g");
        binding.textViewCarbsValue.setText(food.getTotalCarbohydrate().toString() + " g");
        binding.textViewFatsValue.setText(food.getTotalFat().toString() + " g");
        binding.textViewProtsValue.setText(food.getProtein().toString() + " g");

        binding.textViewServingInfo.setText(food.getServingQty() + " " + food.getServingUnit() + " (" + food.getServingWeight() + " grams)");

    }

}