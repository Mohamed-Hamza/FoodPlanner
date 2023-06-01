package com.mohamedhamza.foodplanner.presenter;

import android.content.Context;
import android.util.Log;

import com.mohamedhamza.foodplanner.data.AppDatabase;
import com.mohamedhamza.foodplanner.data.SavedMealDao;
import com.mohamedhamza.foodplanner.model.CategoryListResponse;
import com.mohamedhamza.foodplanner.model.Meal;
import com.mohamedhamza.foodplanner.model.MealApiService;
import com.mohamedhamza.foodplanner.model.MealResponse;
import com.mohamedhamza.foodplanner.model.SimplifiedMealsResponse;
import com.mohamedhamza.foodplanner.view.search.SearchView;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;

import java.util.Collections;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mohamed Hamza on 5/27/2023.
 */

public class SearchPresenter {
    private MealApiService mealApiService;
    private SearchView searchView;
    private static final String TAG = "SearchPresenter";
    SavedMealDao savedMealDao;
    CompositeDisposable compositeDisposable;



    public SearchPresenter(MealApiService mealApiService, SearchView searchView, Context context) {
        this.mealApiService = mealApiService;
        this.searchView = searchView;
        savedMealDao = AppDatabase.getInstance(context).savedMealDao();
        compositeDisposable = new CompositeDisposable();
    }



    public void getMealsByCategory(String category) {
        searchView.showLoading();
        mealApiService.getMealsByCategory(category).enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                searchView.hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    searchView.displayMeals(response.body().getMeals());
                } else {
                    searchView.onErrorLoading(response.message());
                }
            }

            @Override
            public void onFailure(Call<MealResponse> call, Throwable t) {
                searchView.hideLoading();
                searchView.onErrorLoading(t.getMessage());
            }
        });


    }


    public void getCategoryList() {
        searchView.showLoading();
        mealApiService.getCategoryList().enqueue(new Callback<CategoryListResponse>() {
            @Override
            public void onResponse(Call<CategoryListResponse> call, Response<CategoryListResponse> response) {
                searchView.hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    searchView.displayCategories(response.body().getCategories());
                } else {
                    searchView.onErrorLoading(response.message());
                }
            }

            @Override
            public void onFailure(Call<CategoryListResponse> call, Throwable t) {
                searchView.hideLoading();
                searchView.onErrorLoading(t.getMessage());
            }
        });
    }

    public void saveMeal(Meal meal) {
        savedMealDao.insertSavedMeal(meal);
    }

    public void deleteSavedMeal(Meal meal) {
        savedMealDao.deleteSavedMeal(meal);
    }


    public void searchMeals(String query) {
        if (query.length() < 3) {
//            searchView.showSearchResults(Collections.emptyList());
            searchView.clearSearchResults();
            return;
        }
        searchView.showLoading();
        compositeDisposable.clear();

        Observable<SimplifiedMealsResponse> categoryObservable = mealApiService.filterByCategory(query);
        Observable<SimplifiedMealsResponse> ingredientObservable = mealApiService.filterByIngredient(query);
        Observable<SimplifiedMealsResponse> areaObservable = mealApiService.filterByArea(query);

        compositeDisposable.add(
                Observable.merge(categoryObservable, ingredientObservable, areaObservable)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleSearchSuccess, this::handleSearchError)
        );
    }

    private void handleSearchSuccess(SimplifiedMealsResponse response) {
        searchView.hideLoading();
        if (response.getMeals() != null && !response.getMeals().isEmpty()) {
            searchView.showSearchResults(response.getMeals());
        } else {
            //earchView.showSearchResults(Collections.emptyList());
            searchView.clearSearchResults();
        }
    }

    private void handleSearchError(Throwable throwable) {
        searchView.hideLoading();
        searchView.showSearchError(throwable.getLocalizedMessage());
    }

    public void dispose() {
        compositeDisposable.dispose();
    }




}


