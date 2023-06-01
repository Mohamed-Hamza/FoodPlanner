package com.mohamedhamza.foodplanner.view.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mohamedhamza.foodplanner.R;
import com.mohamedhamza.foodplanner.contract.HomeContract;
import com.mohamedhamza.foodplanner.model.Meal;
import com.mohamedhamza.foodplanner.presenter.HomePresenter;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment extends Fragment implements HomeContract.View {
    TextView username;
    CircleImageView userimage;
    private HomePresenter presenter;
    ImageView mealThumb;
    TextView mealName;
    private LottieAnimationView loadingAnimation;
    private CardView mealCardView;
    private FrameLayout emptyStateContainer;
    private LottieAnimationView emptyStateAnimation;
    private Group groupEmptyState, groupNormalState;
    private ImageButton logoutButton;





    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        presenter  = new HomePresenter(this,getContext());

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //get current user info and print it in the
        username = view.findViewById(R.id.usernameTV);
        userimage = view.findViewById(R.id.profile_image);
        loadingAnimation = view.findViewById(R.id.mealLoadingAnimation);
        //get meal info and print it in the home fragment
        mealThumb = view.findViewById(R.id.mealImage);
        mealName = view.findViewById(R.id.mealName);
        mealCardView = view.findViewById(R.id.mealView);
        emptyStateAnimation = view.findViewById(R.id.emptyStateAnimation);
        groupEmptyState = view.findViewById(R.id.groupEmptyState);
        groupNormalState = view.findViewById(R.id.groupNormalState);
        logoutButton = view.findViewById(R.id.logoutButton);

        handleLogoutButton();





        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            username.setText(currentUser.getDisplayName());
            if (currentUser.getPhotoUrl() != null) {
                String photoUrl = currentUser.getPhotoUrl().toString();
                Glide.with(this)
                        .load(photoUrl)
                        .into(userimage);


            } else {
                userimage.setImageResource(R.drawable.man);
            }
        } else {
            username.setText("Guest");
            userimage.setImageResource(R.drawable.man);
        }

    }

    private void handleLogoutButton() {
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_loginFragment);
                presenter.deleteAllSavedMeals();
            }
        });
    }

    @Override
    public void showLoading() {
        mealThumb.setVisibility(View.INVISIBLE);
        loadingAnimation.setVisibility(View.VISIBLE);
        loadingAnimation.playAnimation();

    }

    @Override
    public void hideLoading() {
        mealThumb.setVisibility(View.VISIBLE);
        loadingAnimation.setVisibility(View.INVISIBLE);
        loadingAnimation.pauseAnimation();
    }

    @Override
    public void setMeal(Meal meal) {
        Glide.with(this)
                .load(meal.getStrMealThumb())
                .into(mealThumb);
        mealName.setText(meal.getStrMeal());
        mealCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragmentDirections.ActionHomeFragmentToMealFragment action = HomeFragmentDirections.actionHomeFragmentToMealFragment(meal.getIdMeal());
                Navigation.findNavController(v).navigate(action);
            }
        });


    }

    @Override
    public void onErrorLoading(String message) {
        groupNormalState.setVisibility(View.INVISIBLE);
        groupEmptyState.setVisibility(View.VISIBLE);
        emptyStateAnimation.playAnimation();

    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getMeal();
    }
}