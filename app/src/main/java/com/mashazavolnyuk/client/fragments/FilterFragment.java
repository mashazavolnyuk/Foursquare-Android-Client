package com.mashazavolnyuk.client.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mashazavolnyuk.client.Constants;
import com.mashazavolnyuk.client.MainActivity;
import com.mashazavolnyuk.client.R;
import com.mashazavolnyuk.client.data.locationUser.BaseLocation;
import com.mashazavolnyuk.client.filter.FilterParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FilterFragment extends BaseFragment {

    @BindView(R.id.selectPlaceAndRadius)
    TextView selectPlaceAndRadius;
    @BindView((R.id.sortByRelevance))
    Button sortByRelevance;
    @BindView((R.id.sortByDistance))
    Button sortByDistance;
    @BindView((R.id.filterByExpensiveLevel1))
    Button filterByExpensiveLevel1;
    @BindView((R.id.filterByExpensiveLevel2))
    Button filterByExpensiveLevel2;
    @BindView((R.id.filterByExpensiveLevel3))
    Button filterByExpensiveLevel3;
    @BindView((R.id.filterByExpensiveLevel4))
    Button filterByExpensiveLevel4;
    @BindView(R.id.returnToStartLocation)
    ImageView returnToStartLocation;


    boolean isAvailableLevel1;
    boolean isAvailableLevel2;
    boolean isAvailableLevel3;
    boolean isAvailableLevel4;
    boolean isSortByRelevance;
    BaseLocation userLocation;
    BaseLocation selectedLocation;
    TextView hereNow;

    SharedPreferences preferences;
    private Unbinder unbinder;


    @SuppressLint("CommitPrefEdits")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        unbinder = ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        hereNow = view.findViewById(R.id.hereNow);
        preferences = getActivity().getSharedPreferences(Constants.PREF_FILTERS, Context.MODE_PRIVATE);
        initValueFromPreference();
        updateUIAccordingToFilterSettings();
        setListeners();
        selectPlaceAndRadius.setOnClickListener(view1 -> ((MainActivity) getActivity()).goToMap());
        returnToStartLocation.setOnClickListener(view12 -> resetLocation());
        return view;
    }

    private void initValueFromPreference() {
        isSortByRelevance = preferences.getBoolean(FilterParams.SORT_BY_RELEVANCE, true);
        isAvailableLevel1 = preferences.getBoolean(FilterParams.FILTER_BY_EXPENSIVE_LEVEL1, false);
        isAvailableLevel2 = preferences.getBoolean(FilterParams.FILTER_BY_EXPENSIVE_LEVEL2, false);
        isAvailableLevel3 = preferences.getBoolean(FilterParams.FILTER_BY_EXPENSIVE_LEVEL3, false);
        isAvailableLevel4 = preferences.getBoolean(FilterParams.FILTER_BY_EXPENSIVE_LEVEL4, false);
        Gson gson = new Gson();
        String userLocationModel = preferences.getString(FilterParams.USER_LOCATION, "");
        String selectedLocationModel = preferences.getString(FilterParams.SELECTED_LOCATION, "");
        userLocation = gson.fromJson(userLocationModel, BaseLocation.class);
        selectedLocation = gson.fromJson(selectedLocationModel, BaseLocation.class);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setListeners() {
        sortByDistance.setOnClickListener(view -> {
            isSortByRelevance = false;
            highlightActiveElement(sortByDistance, true);
            highlightActiveElement(sortByRelevance, false);
            applyChangeForLevel(5, sortByRelevance); //one boolean for two element
        });
        sortByRelevance.setOnClickListener(view -> {
            highlightActiveElement(sortByRelevance, true);
            highlightActiveElement(sortByDistance, false);
            isSortByRelevance = true;
            applyChangeForLevel(5, sortByRelevance);
        });
        filterByExpensiveLevel1.setOnClickListener(view -> {
            isAvailableLevel1 = !isAvailableLevel1;
            applyChangeForLevel(1, filterByExpensiveLevel1);
        });
        filterByExpensiveLevel2.setOnClickListener(view -> {
            isAvailableLevel2 = !isAvailableLevel2;
            applyChangeForLevel(2, filterByExpensiveLevel2);
        });
        filterByExpensiveLevel3.setOnClickListener(view -> {
            isAvailableLevel3 = !isAvailableLevel3;
            applyChangeForLevel(3, filterByExpensiveLevel3);
        });
        filterByExpensiveLevel4.setOnClickListener(view -> {
            isAvailableLevel4 = !isAvailableLevel4;
            applyChangeForLevel(4, filterByExpensiveLevel4);

        });
    }

    private void applyChangeForLevel(int level, Button button) {
        boolean chooseLevel = false;
        String filterParams = null;
        switch (level) {
            case 1:
                chooseLevel = isAvailableLevel1;
                filterParams = FilterParams.FILTER_BY_EXPENSIVE_LEVEL1;
                break;
            case 2:
                chooseLevel = isAvailableLevel2;
                filterParams = FilterParams.FILTER_BY_EXPENSIVE_LEVEL2;
                break;
            case 3:
                chooseLevel = isAvailableLevel3;
                filterParams = FilterParams.FILTER_BY_EXPENSIVE_LEVEL3;
                break;
            case 4:
                chooseLevel = isAvailableLevel4;
                filterParams = FilterParams.FILTER_BY_EXPENSIVE_LEVEL4;
                break;
            case 5:
                chooseLevel = isSortByRelevance;
                filterParams = FilterParams.SORT_BY_RELEVANCE;
                break;
        }
        highlightActiveElement(button, chooseLevel);
        saveParamInSettings(filterParams, chooseLevel);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.filter_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reset:
                resetFilterSettings();
                return true;
            default:
                break;
        }
        return false;
    }

    private void saveParamInSettings(String tag, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(tag, value);
        editor.apply();
    }

    private void resetFilterSettings() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(FilterParams.SORT_BY_RELEVANCE, true);
        editor.putBoolean(FilterParams.FILTER_BY_EXPENSIVE_LEVEL1, false);
        editor.putBoolean(FilterParams.FILTER_BY_EXPENSIVE_LEVEL2, false);
        editor.putBoolean(FilterParams.FILTER_BY_EXPENSIVE_LEVEL3, false);
        editor.putBoolean(FilterParams.FILTER_BY_EXPENSIVE_LEVEL4, false);
        editor.apply();
        isAvailableLevel1 = false;
        isAvailableLevel2 = false;
        isAvailableLevel3 = false;
        isAvailableLevel4 = false;
        isSortByRelevance = true;
        updateUIAccordingToFilterSettings();
    }

    private void resetLocation() {
        selectedLocation = null;
        Gson gson = new Gson();
        String jsonModel = gson.toJson(selectedLocation);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(FilterParams.SELECTED_LOCATION, jsonModel);
        editor.apply();
        returnToStartLocation.setVisibility(View.INVISIBLE);
        hereNow.setText(userLocation.getAddress());
    }

    private void updateUIAccordingToFilterSettings() {
        if (isSortByRelevance) {
            highlightActiveElement(sortByRelevance, true);
            highlightActiveElement(sortByDistance, false);
        } else {
            highlightActiveElement(sortByDistance, true);
            highlightActiveElement(sortByRelevance, false);
        }
        applyChangeForLevel(1, filterByExpensiveLevel1);
        applyChangeForLevel(2, filterByExpensiveLevel2);
        applyChangeForLevel(3, filterByExpensiveLevel3);
        applyChangeForLevel(4, filterByExpensiveLevel4);
        if (selectedLocation != null) {
            returnToStartLocation.setVisibility(View.VISIBLE);
            hereNow.setText(selectedLocation.getAddress());
        } else {
            returnToStartLocation.setVisibility(View.INVISIBLE);
            hereNow.setText(userLocation.getAddress());
        }
    }

    private void highlightActiveElement(Button button, boolean isHighlighted) {
        if (isHighlighted) {
            button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            button.setTextColor(getResources().getColor(R.color.white));
        } else {
            button.setBackgroundColor(getResources().getColor(R.color.white));
            button.setTextColor(getResources().getColor(R.color.colorText));
        }
    }
}
