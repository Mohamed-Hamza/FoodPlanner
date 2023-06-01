package com.mohamedhamza.foodplanner.view.plan;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mohamedhamza.foodplanner.R;

/**
 * Created by Mohamed Hamza on 5/31/2023.
 */

final class PlannerMealViewHolder extends RecyclerView.ViewHolder {

    final View rootView;
    final ImageView ivMeal;
    final TextView mealName;
    final ImageButton removeButton;

    PlannerMealViewHolder(@NonNull final View view) {
        super(view);

        rootView = view;
        ivMeal = view.findViewById(R.id.mealImage);
        mealName = view.findViewById(R.id.mealName);
        removeButton = view.findViewById(R.id.removeMealButton);
    }
}
