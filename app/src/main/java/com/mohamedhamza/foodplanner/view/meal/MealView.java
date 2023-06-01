package com.mohamedhamza.foodplanner.view.meal;

import com.mohamedhamza.foodplanner.model.Meal;

/**
 * Created by Mohamed Hamza on 5/29/2023.
 */

public interface MealView {
    void showMeal(Meal meal);
    void showLoading();
    void hideLoading();
    void onErrorLoading(String message);

}
