package com.mohamedhamza.foodplanner;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;


public class SignupFragment extends Fragment {
    TextView textView;
    FirebaseAuth mAuth;
    Button signUpButton;
    TextInputLayout  emailTextInputLayout, passwordTextInputLayout, confirmPasswordTextInputLayout, usernameTextInputLayout;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView  = view.findViewById(R.id.loginTextView);
        textView.setOnClickListener(view1 -> {
            NavController navcontroller= Navigation.findNavController(view);
            navcontroller.navigate(R.id.action_signupFragment_to_loginFragment);
        });

        signUpButton = view.findViewById(R.id.signupButton);
        emailTextInputLayout = view.findViewById(R.id.emailTextField);
        passwordTextInputLayout = view.findViewById(R.id.userPasswordTextField);
        confirmPasswordTextInputLayout = view.findViewById(R.id.confirmPasswordTextField);
        usernameTextInputLayout = view.findViewById(R.id.userNameTextField);

        signUpButton.setOnClickListener(view1 -> {
            String username = usernameTextInputLayout.getEditText().getText().toString();
            String email = emailTextInputLayout.getEditText().getText().toString();
            String password = passwordTextInputLayout.getEditText().getText().toString();
            String confirmPassword = confirmPasswordTextInputLayout.getEditText().getText().toString();
            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || username.isEmpty()) {
                emailTextInputLayout.setError("Please fill all the fields");
                passwordTextInputLayout.setError("Please fill all the fields");
                confirmPasswordTextInputLayout.setError("Please fill all the fields");
            } else if (!password.equals(confirmPassword)) {
                passwordTextInputLayout.setError("Passwords do not match");
                confirmPasswordTextInputLayout.setError("Passwords do not match");
            } else {
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        //Registeration is successful, update user profile
                        mAuth.getCurrentUser().updateProfile(new UserProfileChangeRequest.Builder()
                                .setDisplayName(username)
                                //.setPhotoUri(null)
                                .build()).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                //Profile update is successful, navigate to home fragment
                                NavController navController = Navigation.findNavController(view);
                                navController.navigate(R.id.action_signupFragment_to_homeFragment);
                            } else {
                                emailTextInputLayout.setError(task1.getException().getMessage());
                            }
                        });
                    } else {
                        emailTextInputLayout.setError(task.getException().getMessage());
                    }
                });
            }
        });
    }
}

