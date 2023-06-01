package com.mohamedhamza.foodplanner.view.search;

import com.mohamedhamza.foodplanner.model.Meal;
import com.mohamedhamza.foodplanner.model.MealCategory;
import com.mohamedhamza.foodplanner.model.SimplifiedMeal;

import java.util.List;

/**
 * Created by Mohamed Hamza on 5/27/2023.
 */

public interface SearchView {
    void showLoading();
    void hideLoading();
    void displayCategories(List<MealCategory> categories);
    void displayMeals(List<Meal> meals);
    void onErrorLoading(String message);
    void saveMeal(Meal meal);
    void showSearchResults(List<SimplifiedMeal> meals);
    void clearSearchResults();
    void showSearchError(String message);


}
