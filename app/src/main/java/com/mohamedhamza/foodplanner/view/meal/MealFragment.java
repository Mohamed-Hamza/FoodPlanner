package com.mohamedhamza.foodplanner.view.meal;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.mohamedhamza.foodplanner.R;
import com.mohamedhamza.foodplanner.model.ApiClient;
import com.mohamedhamza.foodplanner.model.Meal;
import com.mohamedhamza.foodplanner.model.MealApiService;
import com.mohamedhamza.foodplanner.presenter.MealPresenter;
import com.mohamedhamza.foodplanner.view.utils.IconManager;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import moe.feng.common.stepperview.IStepperAdapter;
import moe.feng.common.stepperview.VerticalStepperItemView;
import moe.feng.common.stepperview.VerticalStepperView;


public class MealFragment extends Fragment implements MealView, IStepperAdapter {
    private static final String TAG = "MealFragment";
    private MealPresenter presenter;
    TextView mealName;
    Chip mealCategory, mealArea;
    ImageView mealImage;
    RecyclerView ingredientsRecyclerView;
    private VerticalStepperView mVerticalStepperView;
    private List<String> mealInstructions = new ArrayList<>();
    YouTubePlayerView youTubePlayerView;
    private IconManager iconManager;
    private ImageButton favoriteButton;







    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new MealPresenter(ApiClient.getRetrofit().create(MealApiService.class), this, getContext());
        iconManager = new IconManager(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_meal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mealName = view.findViewById(R.id.meal_name);
        mealCategory = view.findViewById(R.id.meal_category);
        mealArea = view.findViewById(R.id.meal_country);
        mealImage = view.findViewById(R.id.mealImage);
        ingredientsRecyclerView = view.findViewById(R.id.ingredientsRecyclerView);
        mVerticalStepperView = view.findViewById(R.id.stepperView);
        favoriteButton = view.findViewById(R.id.bookmarkButton);
        youTubePlayerView = view.findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);



        MealFragmentArgs args = MealFragmentArgs.fromBundle(getArguments());
        //presenter.setMealId(args.getMealId());
        presenter.getMeal(args.getMealId());
    }

    @Override
    public void showMeal(Meal meal) {
        mealName.setText(meal.getStrMeal());
        favoriteButton.setSelected(presenter.isSaved(meal.getIdMeal()));
        favoriteButton.setOnClickListener(v -> {
            if (favoriteButton.isSelected()) {
                favoriteButton.setSelected(false);
                presenter.deleteMeal(meal);
            } else {
                favoriteButton.setSelected(true);
                presenter.saveMeal(meal);
            }
        });


        mealCategory.setText(meal.getStrCategory());
        mealArea.setText(meal.getStrArea());
        Glide.with(getContext()).load(meal.getStrMealThumb()).into(mealImage);
        Drawable categoryIcon = iconManager.getCategoryIcon(meal.getStrCategory());
        mealCategory.setChipIcon(categoryIcon);
        Drawable areaIcon = iconManager.getAreaIcon(meal.getStrArea());
        mealArea.setChipIcon(areaIcon);



        List<String> ingredients = new ArrayList<>();
        List<String> amounts = new ArrayList<>();

        // Extract the ingredients and amounts from the Meal object
        for (int i = 1; i < 20; i++) {
            String ingredient = getIngredientValue(meal, "getStrIngredient" + i);
            String amount = getIngredientValue(meal, "getStrMeasure" + i);

            // If ingredient or amount is empty, stop adding further ingredients
//            if (ingredient == null || ingredient.isEmpty() || amount.isEmpty()) {
//                break;
//            }
            if (ingredient == null) {
                break;
            }

            ingredients.add(ingredient);
            amounts.add(amount);
        }


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        ingredientsRecyclerView.setLayoutManager(layoutManager);
        IngredientsAdapter adapter = new IngredientsAdapter(ingredients, amounts);
        ingredientsRecyclerView.setAdapter(adapter);

        // Extract the instructions from the Meal object
        this.mealInstructions = meal.getInstructionSteps();
        mVerticalStepperView.setStepperAdapter(this);

        // Extract the video id from the Meal object
        String videoId = meal.getStrYoutube().split("=")[1];
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                //youTubePlayer.loadVideo(videoId, 0);
                youTubePlayer.cueVideo(videoId, 0);
            }
        });




    }

    @Override
    public void showLoading() {


    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onErrorLoading(String message) {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    // Helper method to get ingredient value dynamically
    private String getIngredientValue(Meal meal, String methodName) {
        try {
            Method method = meal.getClass().getMethod(methodName);
            return (String) method.invoke(meal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }


    // Vertical Stepper View Methods
    @NonNull
    @Override
    public CharSequence getTitle(int index) {
        int realIndex = index + 1;
        return "Step " + realIndex;
    }

    @Override
    public CharSequence getSummary(int i) {
        return null;
    }

    @Override
    public int size() {
        return mealInstructions.size();
    }

    @Override
    public View onCreateCustomView(int index, Context context, VerticalStepperItemView parent) {
        View inflateView = LayoutInflater.from(context).inflate(R.layout.vertical_stepper_sample_item, parent, false);
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mVerticalStepperView.setCurrentStep(index);
            }
        });
        String step = mealInstructions.get(index);
        TextView contentView = inflateView.findViewById(R.id.step_content);
        contentView.setText(step);
        return inflateView;
    }

    @Override
    public void onShow(int i) {

    }

    @Override
    public void onHide(int i) {

    }


}