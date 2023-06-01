package com.mohamedhamza.foodplanner.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Mohamed Hamza on 5/27/2023.
 */

public class CategoryListResponse {
    @SerializedName("meals")
    private List<MealCategory> categories;

    public List<MealCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<MealCategory> categories) {
        this.categories = categories;
    }

}
