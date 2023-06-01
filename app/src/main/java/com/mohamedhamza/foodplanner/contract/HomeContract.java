package com.mohamedhamza.foodplanner.contract;

import com.mohamedhamza.foodplanner.model.Meal;

/**
 * Created by Mohamed Hamza on 5/26/2023.
 */

public interface HomeContract {
    interface View {
        void showLoading();

        void hideLoading();

        void setMeal(Meal meal);

        void onErrorLoading(String message);
    }

    interface Presenter {
        void getMeal();
    }
}
