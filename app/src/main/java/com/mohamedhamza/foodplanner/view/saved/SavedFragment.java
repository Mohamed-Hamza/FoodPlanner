package com.mohamedhamza.foodplanner.view.saved;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mohamedhamza.foodplanner.R;
import com.mohamedhamza.foodplanner.contract.SavedContract;
import com.mohamedhamza.foodplanner.model.Meal;
import com.mohamedhamza.foodplanner.presenter.SavedMealsPresenter;

import java.util.List;

public class SavedFragment extends Fragment implements SavedContract.View, SavedMealsAdapter.OnMealClickListener {
    private SavedMealsPresenter presenter;
    private RecyclerView savedMealsRecyclerView;
    private SavedMealsAdapter savedMealsAdapter;
    private Group loggedInGroup, guestGroup;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new SavedMealsPresenter(this, getContext());
        savedMealsRecyclerView = view.findViewById(R.id.saved_meals_recycler_view);
        loggedInGroup = view.findViewById(R.id.savedMealsViewsGroup);
        guestGroup = view.findViewById(R.id.notLoggedInViewsGroup);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            loggedInGroup.setVisibility(View.VISIBLE);
            guestGroup.setVisibility(View.GONE);
        } else {
            loggedInGroup.setVisibility(View.GONE);
            guestGroup.setVisibility(View.VISIBLE);
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        savedMealsRecyclerView.setLayoutManager(gridLayoutManager);
        getTheSavedMeals();
    }

    void getTheSavedMeals() {
        presenter.getSavedMeals().observe(getViewLifecycleOwner(), this::setMeals);
    }

    @Override
    public void showEmptyState() {

    }

    @Override
    public void setMeals(List<Meal> meals) {
        savedMealsAdapter = new SavedMealsAdapter(meals, this);
        savedMealsRecyclerView.setAdapter(savedMealsAdapter);

    }

    @Override
    public void onFavoriteClick(Meal meal) {
        //Since we are in the favorite fragment, we just remove the meal from the database
        presenter.deleteSavedMeal(meal);
        getTheSavedMeals();

    }

    @Override
    public void onMealClick(Meal meal) {
//        //Go to the meal fragment with navigation component
//        SavedFragmentDirections.ActionSavedFragmentToMealFragment action = SavedFragmentDirections.actionSavedFragmentToMealFragment(meal.getIdMeal());
//        Navigation.findNavController(getView()).navigate(action);

        // Inside the click listener for meal items in the All Meals Fragment
        String sourceFragment = getArguments().getString("sourceFragment");
        if (sourceFragment != null && sourceFragment.equals("planner")) {
            Bundle result = new Bundle();
            result.putString("selectedMealId", meal.getIdMeal());
            result.putString("day", getArguments().getString("day"));
            getParentFragmentManager().setFragmentResult("mealSelected", result);
//            Navigation.findNavController(getView()).popBackStack();
            Navigation.findNavController(getView()).navigateUp();
        } else {
            // Navigate to the Meal Details Fragment to view the meal details
            SavedFragmentDirections.ActionSavedFragmentToMealFragment action = SavedFragmentDirections.actionSavedFragmentToMealFragment(meal.getIdMeal());
            Navigation.findNavController(getView()).navigate(action);

        }
    }
}