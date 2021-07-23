package com.project.healthcompanion;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.project.healthcompanion.Model.SuggestionItem;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private final List<SuggestionItem> suggestionList;
    private final Context context;
    private OnItemClickListener mOnItemClickListener;

    ListAdapter(List<SuggestionItem> list, Context context) {
        suggestionList = list;
        this.context = context;
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ViewHolder vh;

        View view = LayoutInflater.from(context)
                .inflate(R.layout.food_list_item, parent, false);
        vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        SuggestionItem item = suggestionList.get(position);

        Bitmap image = item.getImage();
        if (image != null)
            holder.foodPic.setImageBitmap(image);

        holder.foodName.setText(item.getFoodName());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, suggestionList.get(position), position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return suggestionList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, SuggestionItem obj, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final CircularImageView foodPic;
        private final TextView foodName;
        private final View parentLayout;

        public ViewHolder(View view) {
            super(view);
            foodPic = view.findViewById(R.id.circularImageView_foodPic);
            foodName = view.findViewById(R.id.textView_FoodName);
            parentLayout = view.findViewById(R.id.parentLayout);
        }
    }
}
