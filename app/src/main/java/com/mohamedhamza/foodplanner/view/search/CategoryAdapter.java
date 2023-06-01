package com.mohamedhamza.foodplanner.view.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.mohamedhamza.foodplanner.R;
import com.mohamedhamza.foodplanner.model.MealCategory;

import java.util.List;

/**
 * Created by Mohamed Hamza on 5/27/2023.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{
    private List<MealCategory> categories;
    private OnCategoryClickListener onCategoryClickListener;

    interface OnCategoryClickListener{
        void onCategoryClick(MealCategory category);
    }

    public CategoryAdapter(List<MealCategory> categories, OnCategoryClickListener onCategoryClickListener) {
        this.categories = categories;
        this.onCategoryClickListener = onCategoryClickListener;
    }


    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_chip_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewHolder holder, int position) {
        holder.bind(categories.get(position));

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        private Chip chipCategory;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            //chipCategory is the root element of category_chip_item.xml
            chipCategory = itemView.findViewById(R.id.categoryChip);
        }
        void bind(final MealCategory category){
            chipCategory.setText(category.getStrCategory());
            chipCategory.setOnClickListener(v -> onCategoryClickListener.onCategoryClick(category));
        }

    }
}
