package com.mohamedhamza.foodplanner.view.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.mohamedhamza.foodplanner.R;
import com.mohamedhamza.foodplanner.presenter.LoginPresenter;

import java.util.concurrent.Executor;

public class LoginFragment extends Fragment implements LoginView    {
    TextView textView;
    Button login, loginWithGoogleButton, guestOpen;
    FirebaseAuth mAuth;
    TextInputLayout emailTextInputLayout, passwordTextInputLayout;
    GoogleSignInClient mGoogleSignInClient;
    LoginPresenter presenter;

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "LoginFragment";


    //override oncreate method to get the instance of the firebase auth.
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        presenter = new LoginPresenter(this,getContext());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user!=null){
                                presenter.retriveUserSavedMeals(user);
                            }

                            NavController navcontroller= Navigation.findNavController(requireActivity(),R.id.nav_host_fragment);
                            navcontroller.navigate(R.id.action_loginFragment_to_homeFragment);
                            Toast.makeText(requireActivity(), "Authentication Success.",
                                    Toast.LENGTH_SHORT).show();




                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView  = view.findViewById(R.id.signUpTextView);
        login = view.findViewById(R.id.loginButton);
        emailTextInputLayout = view.findViewById(R.id.userEmailTextField);
        passwordTextInputLayout = view.findViewById(R.id.userPasswordTextField);
        loginWithGoogleButton = view.findViewById(R.id.loginWithGoogleButton);
        guestOpen = view.findViewById(R.id.guestOpenButton);

        guestOpen.setOnClickListener(view1 -> {
            NavController navcontroller= Navigation.findNavController(view);
            navcontroller.navigate(R.id.action_loginFragment_to_homeFragment);
        });



        loginWithGoogleButton.setOnClickListener(view1 -> {
            signIn();
        });

        login.setOnClickListener(view1 -> {
            String email = emailTextInputLayout.getEditText().getText().toString();
            String password = passwordTextInputLayout.getEditText().getText().toString();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    NavController navcontroller= Navigation.findNavController(view);
                    navcontroller.navigate(R.id.action_loginFragment_to_homeFragment);
                } else {
                    Toast.makeText(getContext(), "User is not registered", Toast.LENGTH_SHORT).show();
                }
            });
        });





        textView.setOnClickListener(view1 -> {
            NavController navcontroller=Navigation.findNavController(view);
            navcontroller.navigate(R.id.action_loginFragment_to_signupFragment);

        });

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


}