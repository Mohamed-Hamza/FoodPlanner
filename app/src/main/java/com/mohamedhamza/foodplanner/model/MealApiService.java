package com.mohamedhamza.foodplanner.model;


import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Mohamed Hamza on 5/26/2023.
 */

public interface MealApiService {
    @GET("random.php")
    Call<MealResponse> getRandomMeal();

    @GET("filter.php")
    Call<MealResponse> getMealsByCategory(@Query("c") String category);

    //list all categories
    @GET("list.php?c=list")
    Call<CategoryListResponse> getCategoryList();

    @GET("lookup.php")
    Call<MealResponse> getMealById(@Query("i") String id);

    @GET("filter.php")
    Observable<SimplifiedMealsResponse> filterByCategory(@Query("c") String category);

    @GET("filter.php")
    Observable<SimplifiedMealsResponse> filterByIngredient(@Query("i") String ingredient);

    @GET("filter.php")
    Observable<SimplifiedMealsResponse> filterByArea(@Query("a") String area);


}
