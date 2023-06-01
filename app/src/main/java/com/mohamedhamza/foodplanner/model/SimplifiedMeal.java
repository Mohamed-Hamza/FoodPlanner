package com.mohamedhamza.foodplanner.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by Mohamed Hamza on 5/27/2023.
 */

public class SimplifiedMeal {
    private String strMeal;
    private String strMealThumb;

    private String idMeal;

    public SimplifiedMeal(String strMeal, String strMealThumb, @NonNull String idMeal) {
        this.strMeal = strMeal;
        this.strMealThumb = strMealThumb;
        this.idMeal = idMeal;
    }

    public String getStrMeal() {
        return strMeal;
    }

    public String getStrMealThumb() {
        return strMealThumb;
    }

    public String getIdMeal() {
        return idMeal;
    }
}
