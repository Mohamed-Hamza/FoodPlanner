package com.mohamedhamza.foodplanner.presenter;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.mohamedhamza.foodplanner.data.AppDatabase;
import com.mohamedhamza.foodplanner.data.SavedMealDao;
import com.mohamedhamza.foodplanner.model.Meal;
import com.mohamedhamza.foodplanner.model.MealApiService;
import com.mohamedhamza.foodplanner.model.MealResponse;
import com.mohamedhamza.foodplanner.model.PlannedDate;
import com.mohamedhamza.foodplanner.view.plan.PlanView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mohamed Hamza on 5/31/2023.
 */

public class PlanPresenter {
    private static final String TAG = "PlanPresenter";
    private AppDatabase appDatabase;
    private MealApiService mealApiService;

    private PlanView planView;
    private SavedMealDao savedMealDao;

    public PlanPresenter(MealApiService mealApiService, PlanView planView, Context context) {
        this.planView = planView;
        appDatabase = AppDatabase.getInstance(context);
        savedMealDao = appDatabase.savedMealDao();
        this.mealApiService = mealApiService;

    }


    public void addPlannedMeal(String mealId, Date date) {
        PlannedDate plannedDate = new PlannedDate();
        plannedDate.setMealId(Long.parseLong(mealId));
        plannedDate.setDate(date);
        //Download the meal from the API and save it to the database as a saved meal
        saveMeal(mealId);
        appDatabase.plannedDateDao().insert(plannedDate);

    }

    public void saveMeal(String id) {
        // Check if the meal is already saved in the database
        if (!isSaved(id)) {

            mealApiService.getMealById(id).enqueue(new Callback<MealResponse>() {
                @Override
                public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Meal meal =  response.body().getMeals().get(0);
                        savedMealDao.insertSavedMeal(meal);

                    } else {
                        //view.onErrorLoading(response.message());
                    }
                }
                @Override
                public void onFailure(Call<MealResponse> call, Throwable t) {
//                    view.hideLoading();
//                    view.onErrorLoading(t.getLocalizedMessage());
                }
            });

    }}



    //check if the meal is saved or not
    public boolean isSaved(String mealId) {
        return savedMealDao.isSaved(mealId) > 0;
    }



    public void getPlannedMealsForWeek(List<Date> dates) {
        List<String> dayNames = new ArrayList<>();

        for (Date date : dates) {
            LiveData<List<Meal>> plannedMealsLiveData = appDatabase.plannedDateDao().getPlannedMealsByDate(date);
            plannedMealsLiveData.observeForever(new Observer<List<Meal>>() {
                @Override
                public void onChanged(List<Meal> meals) {
                    String dayName = getDayNameFromDate(date);

                    if (meals.isEmpty()) {
                        dayNames.add(dayName);
                    } else {
                        planView.showPlannedMeals(meals, dayName);
                    }

                    if (dayNames.size() == dates.size()) {
                        planView.showEmptyDays(dayNames);
                    }

                    plannedMealsLiveData.removeObserver(this);
                }
            });

        }
    }





    private String getDayNameFromDate(Date date) {
        // Return the day's name as a string
        return new SimpleDateFormat("EEEE", Locale.getDefault()).format(date);
    }



    }
