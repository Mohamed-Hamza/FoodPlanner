package com.mohamedhamza.foodplanner.view.saved;

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
import com.mohamedhamza.foodplanner.view.search.CategoryMealsAdapter;

import java.util.List;

/**
 * Created by Mohamed Hamza on 5/28/2023.
 */

public class SavedMealsAdapter extends RecyclerView.Adapter<SavedMealsAdapter.ViewHolder> {
    private List<Meal> meals;
    private OnMealClickListener onMealClickListener;


    public interface OnMealClickListener {
        void onFavoriteClick(Meal meal);
        void onMealClick(Meal meal);
    }

    public SavedMealsAdapter(List<Meal> meals, OnMealClickListener onMealClickListener) {
        this.meals = meals;
        this.onMealClickListener = onMealClickListener;
    }

    @NonNull
    @Override
    public SavedMealsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_meal_item, parent, false);
        return new SavedMealsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedMealsAdapter.ViewHolder holder, int position) {
        Meal meal = meals.get(position);
        holder.favoriteBtn.setOnClickListener(v -> onMealClickListener.onFavoriteClick(meal));
        holder.itemView.setOnClickListener(v -> onMealClickListener.onMealClick(meal));
        holder.bind(meal);
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mealName;
        private ImageView mealImage;
        private ImageButton favoriteBtn;

        ViewHolder(@NonNull View itemView) {
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
