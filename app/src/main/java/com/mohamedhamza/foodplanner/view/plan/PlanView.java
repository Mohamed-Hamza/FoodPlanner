package com.mohamedhamza.foodplanner.view.plan;

import com.mohamedhamza.foodplanner.model.Meal;

import java.util.List;

/**
 * Created by Mohamed Hamza on 5/31/2023.
 */

public interface PlanView {
    //show the planned meals for the day
    void showPlannedMeals(List<Meal> meals, String dayName);
    void showEmptyDays(List<String> dayNames);
}
