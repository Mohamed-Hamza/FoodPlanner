package com.mohamedhamza.foodplanner.data;

import android.content.Context;

;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.mohamedhamza.foodplanner.model.Meal;
import com.mohamedhamza.foodplanner.model.PlannedDate;

/**
 * Created by Mohamed Hamza on 5/28/2023.
 */

@Database(entities = {Meal.class, PlannedDate.class}, version = 2)
@TypeConverters({DateConverter.class})
public abstract  class AppDatabase extends RoomDatabase {
    public abstract SavedMealDao savedMealDao();
    public abstract PlannedDateDao plannedDateDao();

    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = androidx.room.Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "meals_database")
                    .allowMainThreadQueries() //TODO: Remove this line
                    .build();
        }
        return instance;
    }


}
