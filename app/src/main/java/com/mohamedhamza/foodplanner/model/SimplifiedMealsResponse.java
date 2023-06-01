package com.mohamedhamza.foodplanner.model;

import java.util.List;

/**
 * Created by Mohamed Hamza on 5/27/2023.
 */

public class SimplifiedMealsResponse {
    private List<SimplifiedMeal> meals;

    public List<SimplifiedMeal> getMeals() {
        return meals;
    }

    public void setMeals(List<SimplifiedMeal> meals) {
        this.meals = meals;
    }
}
