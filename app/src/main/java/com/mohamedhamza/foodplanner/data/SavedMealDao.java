package com.mohamedhamza.foodplanner.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mohamedhamza.foodplanner.model.Meal;

import java.util.List;

/**
 * Created by Mohamed Hamza on 5/28/2023.
 */

@Dao
public interface SavedMealDao {
    @Insert
    void insertSavedMeal(Meal meal);

    @Delete
    void deleteSavedMeal(Meal meal);

    @Query("SELECT * FROM saved_meals")
    LiveData<List<Meal>> getAllSavedMeals();


    @Query("SELECT * FROM saved_meals")
    List<Meal> getAllSavedMealsSync();


    //check if the meal is saved or not
    @Query("SELECT EXISTS (SELECT 1 FROM saved_meals WHERE idMeal = :id)")
    int isSaved(String id);

    //get the meal by id
    @Query("SELECT * FROM saved_meals WHERE idMeal = :id")
    Meal getMealById(String id);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Meal> savedMeals);

    //delete all saved meals
    @Query("DELETE FROM saved_meals")
    void deleteAllSavedMeals();

}
