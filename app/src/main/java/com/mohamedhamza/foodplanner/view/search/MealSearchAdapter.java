package com.mohamedhamza.foodplanner.view.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mohamedhamza.foodplanner.R;
import com.mohamedhamza.foodplanner.model.SimplifiedMeal;

import java.util.List;

/**
 * Created by Mohamed Hamza on 5/31/2023.
 */

public class MealSearchAdapter extends RecyclerView.Adapter<MealSearchAdapter.MealViewHolder> {
    private List<SimplifiedMeal> meals;
    private OnItemClickListener listener;

    public MealSearchAdapter(List<SimplifiedMeal> meals, OnItemClickListener listener) {
        this.meals = meals;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(SimplifiedMeal meal);
    }

    public class MealViewHolder extends RecyclerView.ViewHolder {
        public ImageView mealImageView;
        public TextView mealNameTextView;

        public MealViewHolder(View itemView) {
            super(itemView);
            mealImageView = itemView.findViewById(R.id.mealImageView);
            mealNameTextView = itemView.findViewById(R.id.mealNameTextView);
        }

        public void bind(SimplifiedMeal meal) {
            // Set the meal data to the views
            mealNameTextView.setText(meal.getStrMeal());
            Glide.with(itemView.getContext()).load(meal.getStrMealThumb()).into(mealImageView);
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(meal);
                }
            });
        }
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.meal_search_result, parent, false);
        return new MealViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        SimplifiedMeal meal = meals.get(position);
        holder.bind(meal);
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public void setMeals(List<SimplifiedMeal> meals) {
        this.meals = meals;
        notifyDataSetChanged();
    }

    public void clearMeals() {
        this.meals.clear();
        notifyDataSetChanged();
    }
}