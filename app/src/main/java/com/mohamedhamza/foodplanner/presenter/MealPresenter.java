package com.mohamedhamza.foodplanner.presenter;

import android.content.Context;

import com.mohamedhamza.foodplanner.data.AppDatabase;
import com.mohamedhamza.foodplanner.data.SavedMealDao;
import com.mohamedhamza.foodplanner.model.Meal;
import com.mohamedhamza.foodplanner.model.MealApiService;
import com.mohamedhamza.foodplanner.model.MealResponse;
import com.mohamedhamza.foodplanner.view.meal.MealView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mohamed Hamza on 5/29/2023.
 */

public class MealPresenter {
    private MealApiService mealApiService;
    private MealView view;
    SavedMealDao savedMealDao;


    public MealPresenter(MealApiService mealApiService, MealView view, Context context) {
        this.view = view;
        this.mealApiService = mealApiService;
        savedMealDao = AppDatabase.getInstance(context).savedMealDao();
    }

    public void getMeal(String id) {
        view.showLoading();
        // Check if the meal is already saved in the database
        if (isSaved(id)) {
            Meal savedMeal = savedMealDao.getMealById(id);
            view.hideLoading();
            view.showMeal(savedMeal);
        } else {

            mealApiService.getMealById(id).enqueue(new Callback<MealResponse>() {
                @Override
                public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                    view.hideLoading();
                    if (response.isSuccessful() && response.body() != null) {
                        view.showMeal(response.body().getMeals().get(0));
                    } else {
                        view.onErrorLoading(response.message());
                    }
                }

                @Override
                public void onFailure(Call<MealResponse> call, Throwable t) {
                    view.hideLoading();
                    view.onErrorLoading(t.getLocalizedMessage());
                }
            });
        }
    }

    public void saveMeal(Meal meal) {
        savedMealDao.insertSavedMeal(meal);
    }

    public void deleteMeal(Meal meal) {
        savedMealDao.deleteSavedMeal(meal);
    }

    //check if the meal is saved or not
    public boolean isSaved(String mealId) {
        return savedMealDao.isSaved(mealId) > 0;
    }


}
