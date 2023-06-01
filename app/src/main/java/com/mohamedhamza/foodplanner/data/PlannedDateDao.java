package com.mohamedhamza.foodplanner.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.mohamedhamza.foodplanner.model.Meal;
import com.mohamedhamza.foodplanner.model.PlannedDate;

import java.util.Date;
import java.util.List;

/**
 * Created by Mohamed Hamza on 5/31/2023.
 */

@Dao
public interface PlannedDateDao {
    @Insert
    void insert(PlannedDate plannedDate);

    @Delete
    void delete(PlannedDate plannedDate);

    @Query("SELECT saved_meals.* FROM saved_meals " +
            "INNER JOIN planned_dates ON saved_meals.idMeal = planned_dates.mealId " +
            "WHERE planned_dates.date = :date")
    LiveData<List<Meal>> getPlannedMealsByDate(Date date);
}
