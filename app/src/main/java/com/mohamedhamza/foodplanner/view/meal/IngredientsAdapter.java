package com.mohamedhamza.foodplanner.view.meal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mohamedhamza.foodplanner.R;

import java.util.List;

/**
 * Created by Mohamed Hamza on 5/29/2023.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {
    private List<String> ingredients;
    private List<String> amounts;

    public IngredientsAdapter(List<String> ingredients, List<String> amounts) {
        this.ingredients = ingredients;
        this.amounts = amounts;
    }

    @NonNull
    @Override
    public IngredientsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_layout, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsAdapter.ViewHolder holder, int position) {
        String ingredient = ingredients.get(position);
        String amount = amounts.get(position);

        holder.ingredientNameTextView.setText(ingredient);
        holder.amountTextView.setText(amount);
        Glide.with(holder.itemView.getContext()).load("https://www.themealdb.com/images/ingredients/" + ingredient + ".png").into(holder.ingredientImageView);


    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ingredientNameTextView;
        TextView amountTextView;
        ImageView ingredientImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientNameTextView = itemView.findViewById(R.id.ingredient_name);
            amountTextView = itemView.findViewById(R.id.ingredient_amount);
            ingredientImageView = itemView.findViewById(R.id.ingredient_image);
        }
    }
}
