package com.mohamedhamza.foodplanner.presenter;

import android.content.Context;

import com.mohamedhamza.foodplanner.contract.HomeContract;
import com.mohamedhamza.foodplanner.data.AppDatabase;
import com.mohamedhamza.foodplanner.data.SavedMealDao;
import com.mohamedhamza.foodplanner.model.ApiClient;
import com.mohamedhamza.foodplanner.model.Meal;
import com.mohamedhamza.foodplanner.model.MealApiService;
import com.mohamedhamza.foodplanner.model.MealResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mohamed Hamza on 5/26/2023.
 */

public class HomePresenter implements HomeContract.Presenter{
    private HomeContract.View view;
    private Meal loadedMeal;
    private SavedMealDao savedMealDao;

    public HomePresenter(HomeContract.View view, Context context) {
        this.view = view;
        savedMealDao = AppDatabase.getInstance(context).savedMealDao();
    }

    @Override
    public void getMeal() {
        if (loadedMeal != null) {
            view.setMeal(loadedMeal);
            return;
        }
        view.showLoading();
        Call<MealResponse> mealResponseCall = ApiClient.getRetrofit().create(MealApiService.class).getRandomMeal();
        mealResponseCall.enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                view.hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    view.setMeal(response.body().getMeals().get(0));
                    loadedMeal = response.body().getMeals().get(0);
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

    public void deleteAllSavedMeals() {
        savedMealDao.deleteAllSavedMeals();
    }
}
