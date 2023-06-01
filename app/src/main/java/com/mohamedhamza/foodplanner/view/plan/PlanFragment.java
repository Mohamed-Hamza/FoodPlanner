package com.mohamedhamza.foodplanner.view.plan;

import static com.mohamedhamza.foodplanner.view.utils.DateUtils.getDateForDayName;

import android.os.Bundle;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.mohamedhamza.foodplanner.R;
import com.mohamedhamza.foodplanner.model.ApiClient;
import com.mohamedhamza.foodplanner.model.Meal;
import com.mohamedhamza.foodplanner.model.MealApiService;
import com.mohamedhamza.foodplanner.model.SimplifiedMeal;
import com.mohamedhamza.foodplanner.presenter.PlanPresenter;
import com.mohamedhamza.foodplanner.view.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class PlanFragment extends Fragment implements PlannerDaySection.ClickListener, PlanView {
    private SectionedRecyclerViewAdapter sectionedAdapter;
    private PlanPresenter presenter;
    RecyclerView recyclerView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan, container, false);

//        sectionedAdapter.addSection(new PlannerDaySection("Saturday",
//                new ArrayList<>(), this));
//        sectionedAdapter.addSection(new PlannerDaySection("Sunday",
//                new ArrayList<>(), this));
//        sectionedAdapter.addSection(new PlannerDaySection("Monday",
//                new ArrayList<>(), this));
//        sectionedAdapter.addSection(new PlannerDaySection("Tuesday",
//                new ArrayList<>(), this));
//        sectionedAdapter.addSection(new PlannerDaySection("Wednesday",
//                new ArrayList<>(), this));
//        sectionedAdapter.addSection(new PlannerDaySection("Thursday",
//                new ArrayList<>(), this));
//        sectionedAdapter.addSection(new PlannerDaySection("Friday",
//                new ArrayList<>(), this));


        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new PlanPresenter(ApiClient.getRetrofit().create(MealApiService.class), this, getContext());
        sectionedAdapter = new SectionedRecyclerViewAdapter();
        recyclerView = view.findViewById(R.id.plan_recycler_view);

        GridLayoutManager glm = new GridLayoutManager(getContext(), 2);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(final int position) {
                if (sectionedAdapter.getSectionItemViewType(position) == SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER) {
                    return 2;
                }
                return 1;
            }
        });
        recyclerView.setLayoutManager(glm);
        recyclerView.setAdapter(sectionedAdapter);

        // Get the list of dates for the current week
        List<Date> datesForCurrentWeek = DateUtils.getDatesForCurrentWeek();

        // Retrieve planned meals for each day in the week
        presenter.getPlannedMealsForWeek(datesForCurrentWeek);


        getParentFragmentManager().setFragmentResultListener("mealSelected", this, (requestKey, result) -> {
            if (requestKey.equals("mealSelected")) {
                Bundle args = result.getBundle("mealSelected");
                if (args != null) {
                    String selectedMeal = args.getString("selectedMealId");
                    String dayName = args.getString("day");
                    Log.d("PlanFragment", "onViewCreated: " + selectedMeal + " " + dayName);
                    presenter.addPlannedMeal(selectedMeal, getDateForDayName(dayName));
//                    PlannerDaySection section = (PlannerDaySection) sectionedAdapter.getSectionForPosition(dayPosition);
//                    section.add(meal);
//                    sectionedAdapter.getAdapterForSection(section).notifyItemChanged(dayPosition);

                    // Handle the selected meal data and update the corresponding day in the planner
                }


            }

//            Meal meal = (Meal) result.getSerializable("meal");
//            String day = result.getString("day");
//            int position = result.getInt("position");
//            PlannerDaySection section = (PlannerDaySection) sectionedAdapter.getSection(day);
//            section.addMeal(meal, position);
//            sectionedAdapter.notifyDataSetChanged();
        });
    }

    private void showMenu(View view, @MenuRes int menuRes, String dayName) {
        PopupMenu popup = new PopupMenu(requireContext(), view);
        popup.getMenuInflater().inflate(menuRes, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            //determine whether Favourites/Search Fragments were opened from the Planner Fragment or not
            Bundle args = new Bundle();
            args.putString("sourceFragment", "planner");
            args.putString("day", dayName);
            switch (item.getItemId()) {
                case R.id.from_favourites:
                    Navigation.findNavController(view).navigate(R.id.action_planFragment_to_savedFragment, args);
                    return true;
                case R.id.from_search:
                    Navigation.findNavController(view).navigate(R.id.action_planFragment_to_searchFragment, args);
                    return true;
                default:
                    return false;
            }
        });
        popup.setOnDismissListener(menu -> {
            //Nothing for now
        });
        popup.show();
    }


    @Override
    public void onHeaderAddMealButtonClicked(View view, @NonNull PlannerDaySection section, int itemAdapterPosition) {
        showMenu(view,R.menu.add_to_plan_popup_menu, section.getTitle());
//        section.add(new Meal("RICE", "https://www.themealdb.com/images/media/meals/1529444830.jpg", "1"));
//        sectionedAdapter.getAdapterForSection(section).notifyItemChanged(itemAdapterPosition);

    }

    @Override
    public void onItemRootViewClicked(@NonNull PlannerDaySection section, int itemAdapterPosition) {

    }

    @Override
    public void onItemRemoveButtonClicked(@NonNull PlannerDaySection section, int itemAdapterPosition) {

    }


    @Override
    public void showPlannedMeals(List<Meal> meals, String dayName) {
        sectionedAdapter.addSection(new PlannerDaySection(dayName,
                meals, this));
    }

    @Override
    public void showEmptyDays(List<String> dayNames) {
        for (String dayName : dayNames) {
            sectionedAdapter.addSection(new PlannerDaySection(dayName,
                    new ArrayList<>(), this));
        }
        sectionedAdapter.notifyDataSetChanged();
    }
}