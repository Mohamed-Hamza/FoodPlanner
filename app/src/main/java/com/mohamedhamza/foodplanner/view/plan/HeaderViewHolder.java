package com.mohamedhamza.foodplanner.view.plan;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mohamedhamza.foodplanner.R;

/**
 * Created by Mohamed Hamza on 5/31/2023.
 */

final class HeaderViewHolder extends RecyclerView.ViewHolder {

    final TextView tvTitle;
    final Button btnAddMeal;

    HeaderViewHolder(@NonNull final View view) {
        super(view);

        tvTitle = view.findViewById(R.id.tvTitle);
        btnAddMeal = view.findViewById(R.id.btnAddMeal);
    }
}
