package com.mohamedhamza.foodplanner.contract;

import com.mohamedhamza.foodplanner.model.Meal;

import java.util.List;

/**
 * Created by Mohamed Hamza on 5/28/2023.
 */

public interface SavedContract {
    interface View {
        void showEmptyState();

        void setMeals(List<Meal> meals);
    }

    interface Presenter {
        void getSavedMeals();
    }
}
