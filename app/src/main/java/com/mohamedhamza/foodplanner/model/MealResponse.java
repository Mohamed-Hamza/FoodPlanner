package com.mohamedhamza.foodplanner.model;

import java.util.List;

/**
 * Created by Mohamed Hamza on 5/26/2023.
 */

public class MealResponse {
    private List<Meal> meals;

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }
}
