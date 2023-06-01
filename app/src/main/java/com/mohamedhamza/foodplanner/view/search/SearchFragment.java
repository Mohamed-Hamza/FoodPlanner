package com.mohamedhamza.foodplanner.view.search;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mohamedhamza.foodplanner.R;
import com.mohamedhamza.foodplanner.model.ApiClient;
import com.mohamedhamza.foodplanner.model.Meal;
import com.mohamedhamza.foodplanner.model.MealApiService;
import com.mohamedhamza.foodplanner.model.MealCategory;
import com.mohamedhamza.foodplanner.model.SimplifiedMeal;
import com.mohamedhamza.foodplanner.presenter.SearchPresenter;
import com.mohamedhamza.foodplanner.view.home.HomeFragmentDirections;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class SearchFragment extends Fragment implements SearchView, CategoryAdapter.OnCategoryClickListener, CategoryMealsAdapter.OnMealFavoriteClickListener, MealSearchAdapter.OnItemClickListener {
    private SearchPresenter presenter;
    private RecyclerView categoryRecyclerView, categoryMealsRecyclerView;
    private LottieAnimationView emptyStateAnimation;
    private Group groupEmptyState, groupNormalState;
    TextView username;
    CircleImageView userimage;
    TextInputLayout searchInput;
    RecyclerView searchResultsRecyclerView;
    MealSearchAdapter searchResultsAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        categoryRecyclerView = view.findViewById(R.id.category_chips_recycler_view);
        categoryMealsRecyclerView = view.findViewById(R.id.category_meals_recycler_view);
        emptyStateAnimation = view.findViewById(R.id.emptyStateAnimation);
        groupEmptyState = view.findViewById(R.id.groupNormalState);
        groupNormalState = view.findViewById(R.id.groupNormalState);
        username = view.findViewById(R.id.usernameTV);
        userimage = view.findViewById(R.id.profile_image);
        searchInput = view.findViewById(R.id.searchTextField);
        searchResultsRecyclerView = view.findViewById(R.id.searchResultsRecyclerView);
        searchResultsAdapter = new MealSearchAdapter(new ArrayList<SimplifiedMeal>(), this);
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        searchResultsRecyclerView.setAdapter(searchResultsAdapter);


        handleSearch();

        welcomeUser();

        presenter = new SearchPresenter(ApiClient.getRetrofit().create(MealApiService.class), this, getContext());
        presenter.getCategoryList();
        presenter.getMealsByCategory("Seafood");

    }

    private void handleSearch() {
        searchInput.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchInput.getEditText().setText("");
                searchInput.getEditText().clearFocus();
                searchInput.clearFocus();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
                searchResultsAdapter.clearMeals();
                searchResultsRecyclerView.setVisibility(View.GONE);
                //groupNormalState.setVisibility(View.VISIBLE);
            }
        });
        searchInput.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 3) {
                    presenter.searchMeals(charSequence.toString());
                }
                if (charSequence.length() == 0) {
//                    searchResultsRecyclerView.setVisibility(View.GONE);
//                    groupNormalState.setVisibility(View.VISIBLE);
                    searchResultsAdapter.clearMeals();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void welcomeUser() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            username.setText(currentUser.getDisplayName());
            if (currentUser.getPhotoUrl() != null) {
                String photoUrl = currentUser.getPhotoUrl().toString();
                Glide.with(this)
                        .load(photoUrl)
                        .into(userimage);
            } else {
                // Set a default image if the user doesn't have a photo or the URL is invalid
                userimage.setImageResource(R.drawable.man);
            }
        } else {
            username.setText("Guest");
            userimage.setImageResource(R.drawable.man);
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void displayCategories(List<MealCategory> categories) {
        CategoryAdapter categoryAdapter = new CategoryAdapter(categories, this);
        categoryRecyclerView.setHasFixedSize(true);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryRecyclerView.setAdapter(categoryAdapter);



    }

    @Override
    public void displayMeals(List<Meal> meals) {
        CategoryMealsAdapter categoryMealsAdapter = new CategoryMealsAdapter(meals, this);
        categoryMealsRecyclerView.setHasFixedSize(true);
        categoryMealsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryMealsRecyclerView.setAdapter(categoryMealsAdapter);

    }

    @Override
    public void onErrorLoading(String message) {
        groupNormalState.setVisibility(View.INVISIBLE);
        groupEmptyState.setVisibility(View.VISIBLE);
        emptyStateAnimation.playAnimation();

    }

    @Override
    public void saveMeal(Meal meal) {

    }

    @Override
    public void showSearchResults(List<SimplifiedMeal> meals) {
        searchResultsRecyclerView.setVisibility(View.VISIBLE);
        searchResultsAdapter.setMeals(meals);

    }

    @Override
    public void clearSearchResults() {
        searchResultsRecyclerView.setVisibility(View.GONE);
        searchResultsAdapter.clearMeals();

    }

    @Override
    public void showSearchError(String message) {

    }

    @Override
    public void onCategoryClick(MealCategory category) {
        presenter.getMealsByCategory(category.getStrCategory());

    }

    @Override
    public void onAddMealToFavorites(Meal meal) {
        presenter.saveMeal(meal);

    }

    @Override
    public void onRemoveMealFromFavorites(Meal meal) {
        presenter.deleteSavedMeal(meal);
    }

    @Override
    public void onMealClick(Meal meal) {
SearchFragmentDirections.ActionSearchFragmentToMealFragment action = SearchFragmentDirections.actionSearchFragmentToMealFragment(meal.getIdMeal());
        Navigation.findNavController(getView()).navigate(action);

    }

    @Override
    public void onItemClick(SimplifiedMeal meal) {
        SearchFragmentDirections.ActionSearchFragmentToMealFragment action = SearchFragmentDirections.actionSearchFragmentToMealFragment(meal.getIdMeal());
        Navigation.findNavController(getView()).navigate(action);


    }
}