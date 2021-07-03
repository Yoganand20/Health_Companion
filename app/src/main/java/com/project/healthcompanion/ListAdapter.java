package com.project.healthcompanion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.project.healthcompanion.Service.Food;
import com.project.healthcompanion.Service.SuggestionItem;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private Food[] suggestionList;

    ListAdapter(List<SuggestionItem> list) {
        suggestionList = new Food[list.size()];
        suggestionList = list.toArray(suggestionList);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        //set view here
        holder.foodPic.setImageBitmap(suggestionList[position].getThumbImage());
        holder.foodName.setText(suggestionList[position].getFood_name());
        holder.cals.setText(Float.toString(suggestionList[position].getCalories()));
    }

    @Override
    public int getItemCount() {
        return suggestionList.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CircularImageView foodPic;
        private final TextView foodName;
        private final TextView cals;

        public ViewHolder(View view) {
            super(view);
            foodPic = view.findViewById(R.id.circularImageView_foodPic);
            foodName = view.findViewById(R.id.textView_FoodName);
            cals = view.findViewById(R.id.textView_Cal);
        }

    }

}
