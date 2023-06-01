package com.mohamedhamza.foodplanner.presenter;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.mohamedhamza.foodplanner.contract.HomeContract;
import com.mohamedhamza.foodplanner.data.AppDatabase;
import com.mohamedhamza.foodplanner.data.SavedMealDao;
import com.mohamedhamza.foodplanner.model.Meal;
import com.mohamedhamza.foodplanner.view.login.LoginView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohamed Hamza on 6/1/2023.
 */

public class LoginPresenter {
    private LoginView view;
    SavedMealDao savedMealDao;


    public LoginPresenter(LoginView view, Context context) {
        this.view = view;
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        savedMealDao = appDatabase.savedMealDao();

    }

    public void retriveUserSavedMeals(FirebaseUser user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = user.getUid();
        CollectionReference savedMealsRef = db.collection("users").document(userId).collection("saved_meals");
        // Query the saved meals collection
        savedMealsRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Meal> savedMeals = new ArrayList<>();

                        // Iterate over the retrieved documents
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            // Convert each document to a Meal object and add it to the list
                            Meal meal = document.toObject(Meal.class);
                            savedMeals.add(meal);
                        }
                        if (savedMeals.size() > 0){
                            savedMealDao.insertAll(savedMeals);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to retrieve saved meals
                    }
                });

    }

}
