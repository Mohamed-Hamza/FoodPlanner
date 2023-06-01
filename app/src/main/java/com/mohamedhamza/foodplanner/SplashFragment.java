package com.mohamedhamza.foodplanner;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SplashFragment extends Fragment {
    LottieAnimationView lottieAnimationView;
    TextView appName;
    FirebaseAuth mAuth;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View splashView = inflater.inflate(R.layout.fragment_splash, container, false);
        lottieAnimationView = splashView.findViewById(R.id.animation_view);
        appName = splashView.findViewById(R.id.splashAppNameTextView);
        return splashView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startAnimation();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Navigation.findNavController(requireView()).navigate(R.id.action_splashFragment_to_homeFragment);
        } else {
            Navigation.findNavController(requireView()).navigate(R.id.action_splashFragment_to_loginFragment);
        }


    }

    private void startAnimation() {
        lottieAnimationView.playAnimation();
        //ensure that the navigation to the login screen occurs after the Lottie animation finishes
        lottieAnimationView.addAnimatorUpdateListener(animation -> {
            if (animation.getAnimatedFraction() == 1) { //Animation is finished
                Animation slideAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_from_left);
                slideAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
//                        appName.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
//                        appName.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(() -> {
                            Navigation.findNavController(requireView()).navigate(R.id.action_splashFragment_to_loginFragment);
                        }, 1500); // Delay in milliseconds (e.g., 500 for half a second)

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                appName.setAnimation(slideAnimation);
                appName.setVisibility(View.VISIBLE);

            }
        });

    }
}