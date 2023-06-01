package com.mohamedhamza.foodplanner.view.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mohamedhamza.foodplanner.R;
import com.mohamedhamza.foodplanner.model.Meal;

import java.util.List;

/**
 * Created by Mohamed Hamza on 5/27/2023.
 */

public class CategoryMealsAdapter extends RecyclerView.Adapter<CategoryMealsAdapter.MealViewHolder>  {
    private List<Meal> meals;
    private OnMealFavoriteClickListener onMealFavoriteClickListener;

    public interface OnMealFavoriteClickListener {
        void onAddMealToFavorites(Meal meal);
        void onRemoveMealFromFavorites(Meal meal);
        void onMealClick(Meal meal);


    }


    public CategoryMealsAdapter(List<Meal> meals, OnMealFavoriteClickListener onMealFavoriteClickListener) {
        this.meals = meals;
        this.onMealFavoriteClickListener = onMealFavoriteClickListener;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_meal_item, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Meal meal = meals.get(position);
        holder.bind(meal);
        holder.itemView.setOnClickListener(v -> onMealFavoriteClickListener.onMealClick(meal));
        holder.favoriteBtn.setOnClickListener(v -> {
            if (holder.favoriteBtn.isSelected()){
                holder.favoriteBtn.setSelected(false);
                onMealFavoriteClickListener.onRemoveMealFromFavorites(meal);
            } else {
                holder.favoriteBtn.setSelected(true);
                onMealFavoriteClickListener.onAddMealToFavorites(meal);
            }
        });
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    static class MealViewHolder extends RecyclerView.ViewHolder {
        private TextView mealName;
        private ImageView mealImage;
        private ImageButton favoriteBtn;

        MealViewHolder(@NonNull View itemView) {
            super(itemView);
            mealName = itemView.findViewById(R.id.mealName);
            mealImage = itemView.findViewById(R.id.mealImage);
            favoriteBtn = itemView.findViewById(R.id.bookmarkButton);
        }

        void bind(Meal meal) {
            mealName.setText(meal.getStrMeal());
            Glide.with(itemView.getContext()).load(meal.getStrMealThumb()).into(mealImage);
        }
    }
}