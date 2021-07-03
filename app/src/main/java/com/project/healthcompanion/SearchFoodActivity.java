package com.project.healthcompanion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.project.healthcompanion.Service.Food;
import com.project.healthcompanion.Service.NutritionixOverlay;
import com.project.healthcompanion.databinding.ActivitySearchFoodBinding;

import java.util.List;

public class SearchFoodActivity extends AppCompatActivity {
    ActivitySearchFoodBinding binding;
    private Food selectedFood;

    private RecyclerView.LayoutManager layoutManager;

    // ListAdapter listAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchFoodBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        //setRecycleViewLayoutManager();
        NutritionixOverlay nutritionixOverlay = new NutritionixOverlay(this);

        binding.searchViewFood.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                /*nutritionixOverlay.getSearchResult(binding.searchViewFood.getQuery().toString(), new NutritionixOverlay.SearchSuggestionResponse() {
                    @Override
                    public void onSuccess(List<SuggestionItem> suggestions) {
                        showSuggestion(suggestions);
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(getApplicationContext(),"error occurred",Toast.LENGTH_SHORT).show();
                    }
                });*/
                nutritionixOverlay.getFood(query, new NutritionixOverlay.NutrientResponse() {
                    @Override
                    public void onSuccess(List<Food> Foods) {
                        showItem(Foods);
                    }

                    @Override
                    public void onError(String message) {

                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        binding.buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("SearchResult", selectedFood);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        setContentView(view);
    }

    private void showItem(List<Food> foods) {
        Food[] foodsArray = new Food[foods.size()];
        foodsArray = foods.toArray(foodsArray);
        ArrayAdapter arrayAdapter = new ArrayAdapter(SearchFoodActivity.this, android.R.layout.simple_list_item_1, foodsArray);
        binding.listViewFoodDetails.setAdapter(arrayAdapter);
        selectedFood = foodsArray[0];
    }
/*
    private void setRecycleViewLayoutManager() {
        int scrollPosition=0;
        if(binding.recyclerViewSearchResult.getLayoutManager()!=null){
            scrollPosition=((LinearLayoutManager)binding.recyclerViewSearchResult.getLayoutManager()).findFirstVisibleItemPosition();
            layoutManager=new LinearLayoutManager(this);
            binding.recyclerViewSearchResult.setLayoutManager(layoutManager);
            binding.recyclerViewSearchResult.scrollToPosition(scrollPosition);
        }
    }

    private void showSuggestion(List<SuggestionItem> suggestions) {
        ListAdapter listAdapter=new ListAdapter(suggestions);
        binding.recyclerViewSearchResult.setAdapter(listAdapter);
    }*/
}