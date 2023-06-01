package com.mohamedhamza.foodplanner.presenter;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.mohamedhamza.foodplanner.contract.SavedContract;
import com.mohamedhamza.foodplanner.data.AppDatabase;
import com.mohamedhamza.foodplanner.data.SavedMealDao;
import com.mohamedhamza.foodplanner.model.Meal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohamed Hamza on 5/28/2023.
 */

public class SavedMealsPresenter  {
    SavedContract.View view;
    List<Meal> meals;
    SavedMealDao savedMealDao;

    public SavedMealsPresenter(SavedContract.View view, Context context) {
        this.view = view;
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        savedMealDao = appDatabase.savedMealDao();
        meals = new ArrayList<>();

    }


    public LiveData<List<Meal>> getSavedMeals() {
        return savedMealDao.getAllSavedMeals();
    }

    public void deleteSavedMeal(Meal savedMeal) {
        savedMealDao.deleteSavedMeal(savedMeal);
    }


}
