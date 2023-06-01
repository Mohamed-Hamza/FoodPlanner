package com.mohamedhamza.foodplanner.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;


/**
 * Created by Mohamed Hamza on 5/31/2023.
 */

@Entity(tableName = "planned_dates",
        foreignKeys = @ForeignKey(entity = Meal.class, parentColumns = "idMeal", childColumns = "mealId", onDelete = ForeignKey.CASCADE))
public class PlannedDate {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private long mealId; // Foreign key to Meal table
    private Date date; // Date of the planned meal




    // Add all missing getters and setters for fields above
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMealId() {
        return mealId;
    }

    public void setMealId(long mealId) {
        this.mealId = mealId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }



}
