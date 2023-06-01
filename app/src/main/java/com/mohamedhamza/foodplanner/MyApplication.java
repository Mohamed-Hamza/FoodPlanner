package com.mohamedhamza.foodplanner;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airbnb.lottie.L;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mohamedhamza.foodplanner.data.AppDatabase;
import com.mohamedhamza.foodplanner.data.SavedMealDao;
import com.mohamedhamza.foodplanner.model.Meal;

import java.util.List;

/**
 * Created by Mohamed Hamza on 6/1/2023.
 */

public class MyApplication extends Application {
    SavedMealDao savedMealDao;

    @Override
    public void onCreate() {
        super.onCreate();

        savedMealDao = AppDatabase.getInstance(this).savedMealDao();

        registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks(){

            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                backupSavedMealsToFirestore();

            }
        });

    }

    private void backupSavedMealsToFirestore() {
        Log.d("MyApplication", "backupSavedMealsToFirestore: ");
        List<Meal> savedMeals = savedMealDao.getAllSavedMealsSync();
        Log.d("MyApplication", "backupSavedMealsToFirestore: " + savedMeals.size());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            CollectionReference savedMealsRef = db.collection("users").document(currentUser.getUid()).collection("saved_meals");

            for (Meal meal : savedMeals) {
                savedMealsRef.document(meal.getIdMeal()).set(meal);
            }
        }


    }
}
