package com.project.healthcompanion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.healthcompanion.Model.Food;
import com.project.healthcompanion.Model.SuggestionItem;
import com.project.healthcompanion.Service.NutritionixOverlay;
import com.project.healthcompanion.databinding.ActivitySearchFoodBinding;

import java.util.ArrayList;
import java.util.List;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE;

public class SearchFoodActivity extends AppCompatActivity {
    ActivitySearchFoodBinding binding;
    ActivityResultLauncher<Intent> foodResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent intent = result.getData();
                assert intent != null;
                Food food = (Food) intent.getSerializableExtra("selectedFood");
                intent.putExtra("selectedFood", food);

                int qty = intent.getIntExtra("quantity", 1);
                intent.putExtra("quantity", qty);

                setResult(RESULT_OK, intent);
                finish();
            }
        }
    });
    final SearchSuggestionListAdapter.OnItemClickListener onItemClickListener = new SearchSuggestionListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, SuggestionItem obj, int position) {
            Intent intent = new Intent(getApplicationContext(), FoodDetailsActivity.class);
            intent.putExtra(getPackageName() + "foodName", obj.getFoodName());
            foodResultLauncher.launch(intent);
        }
    };
    private List<SuggestionItem> suggestionItemList;
    private RecyclerView recyclerView;
    private SearchSuggestionListAdapter searchSuggestionListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchFoodBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getWindow().setSoftInputMode(SOFT_INPUT_STATE_VISIBLE);

        recyclerView = binding.recyclerViewSearchResult;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        binding.searchViewFood.requestFocus();
        NutritionixOverlay nutritionixOverlay = new NutritionixOverlay(this);

        binding.searchViewFood.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                nutritionixOverlay.getResults(query, new NutritionixOverlay.SearchSuggestionResponse() {
                    @Override
                    public void onSuccess(ArrayList<SuggestionItem> suggestions) {
                        suggestionItemList = suggestions;
                        showSuggestion(suggestionItemList);
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        //super.onBackPressed();
        finish();
    }

    private void showSuggestion(List<SuggestionItem> suggestions) {
        searchSuggestionListAdapter = new SearchSuggestionListAdapter(suggestionItemList, this);
        searchSuggestionListAdapter.setOnItemClickListener(onItemClickListener);
        recyclerView.setAdapter(searchSuggestionListAdapter);
    }
}