package com.project.healthcompanion.DietPlansClasses;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.healthcompanion.Model.Food;
import com.project.healthcompanion.Model.Meal;
import com.project.healthcompanion.R;
import com.project.healthcompanion.SearchFoodActivity;
import com.project.healthcompanion.databinding.FragmentLunchBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LunchFragment extends Fragment {

    FragmentLunchBinding binding;

    Meal meal;

    IMealUpdater mealUpdater;

    FoodListAdapter adapter;
    final FoodListAdapter.OnItemClickListener onItemClickListener = new FoodListAdapter.OnItemClickListener() {
        @Override
        public void onRemoveButtonClicked(View view, Food obj, int position) {
            removeFromRecyclerView(position);
        }
    };
    ActivityResultLauncher<Intent> foodResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent intent = result.getData();
                assert intent != null;
                Food food = (Food) intent.getSerializableExtra("selectedFood");

                int qty = intent.getIntExtra("quantity", 1);

                addToRecyclerView(food);
            }
        }
    });

    public LunchFragment() {
        // Required empty public constructor
    }

    public LunchFragment(String mealName) {
        meal = new Meal(mealName);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLunchBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();


        binding.buttonAddFoodItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchFoodActivity.class);
                foodResultLauncher.launch(intent);
            }
        });

        binding.listViewFoodItems.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.listViewFoodItems.setHasFixedSize(true);
        adapter = new FoodListAdapter(meal.getFoods(), getContext());
        adapter.setOnItemClickListener(onItemClickListener);
        binding.listViewFoodItems.setAdapter(adapter);

        return view;
    }

    private void addToRecyclerView(Food food) {
        //add food from meal
        meal.addFood(food);
        //tell adapter about the change
        adapter.notifyDataSetChanged();
        //update diet plan in activity
        mealUpdater.updateMeal(meal);
    }

    private void removeFromRecyclerView(int position) {
        //remove food from meal
        meal.removeFood(position);
        //tell adapter about the change
        adapter.notifyDataSetChanged();
        //update diet plan in activity
        mealUpdater.updateMeal(meal);
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        mealUpdater = (IMealUpdater) context;
    }
}


class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder> {

    private final List<Food> foodList;
    private final Context context;
    private FoodListAdapter.OnItemClickListener mOnItemClickListener;

    FoodListAdapter(List<Food> foodList, Context context) {
        this.foodList = foodList;
        this.context = context;
    }

    public void setOnItemClickListener(final FoodListAdapter.OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    @NonNull
    @NotNull
    @Override
    public FoodListAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        FoodListAdapter.ViewHolder vh;

        View view = LayoutInflater.from(context)
                .inflate(R.layout.food_item_entry, parent, false);
        vh = new FoodListAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FoodListAdapter.ViewHolder holder, int position) {

        Food item = foodList.get(position);

        holder.foodName.setText(item.getFood_name());

        holder.foodQTY.setText(item.getServingQty().toString());

        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onRemoveButtonClicked(view, foodList.get(position), position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public interface OnItemClickListener {
        void onRemoveButtonClicked(View view, Food obj, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView foodName;
        private final TextView foodQTY;
        private final ImageButton removeButton;

        public ViewHolder(View view) {
            super(view);
            foodName = view.findViewById(R.id.textView_food);
            foodQTY = view.findViewById(R.id.textView_foodQty);
            removeButton = view.findViewById(R.id.imageButton_removeEntry);
        }
    }
}