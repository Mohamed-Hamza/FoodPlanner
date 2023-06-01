package com.mohamedhamza.foodplanner.view.plan;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mohamedhamza.foodplanner.R;
import com.mohamedhamza.foodplanner.model.Meal;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;

/**
 * Created by Mohamed Hamza on 5/31/2023.
 */

public class PlannerDaySection extends Section {
    private final String title;
    private final List<Meal> list;
    private final ClickListener clickListener;


    PlannerDaySection(@NonNull final String title, @NonNull final List<Meal> list,
                 @NonNull final ClickListener clickListener) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.planner_meal_view)
                .headerResourceId(R.layout.planner_day_section_header)
                .build());

        this.title = title;
        this.list = list;
        this.clickListener = clickListener;


    }

    @Override
    public int getContentItemsTotal() {
        return list.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new PlannerMealViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        final PlannerMealViewHolder itemHolder = (PlannerMealViewHolder) holder;

        final Meal meal = list.get(position);

        itemHolder.mealName.setText(meal.getStrMeal());
        itemHolder.removeButton.setOnClickListener(v ->
                clickListener.onItemRemoveButtonClicked(this, itemHolder.getAdapterPosition())
        );

        itemHolder.rootView.setOnClickListener(v ->
                clickListener.onItemRootViewClicked(this, itemHolder.getAdapterPosition())
        );

        Glide.with(itemHolder.rootView.getContext())
                .load(meal.getStrMealThumb())
                .into(itemHolder.ivMeal);

    }

    interface ClickListener {
        void onHeaderAddMealButtonClicked(View view, @NonNull final PlannerDaySection section, int itemAdapterPosition);
        void onItemRootViewClicked(@NonNull final PlannerDaySection section, final int itemAdapterPosition);
        void onItemRemoveButtonClicked(@NonNull final PlannerDaySection section, final int itemAdapterPosition);
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(final RecyclerView.ViewHolder holder) {
        final HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
        headerHolder.tvTitle.setText(title);

        headerHolder.btnAddMeal.setOnClickListener(v ->
                clickListener.onHeaderAddMealButtonClicked(v,this, headerHolder.getAdapterPosition())
        );
    }

    void add(Meal meal) {
        list.add(meal);
    }

    public String getTitle() {
        return title;
    }




}
