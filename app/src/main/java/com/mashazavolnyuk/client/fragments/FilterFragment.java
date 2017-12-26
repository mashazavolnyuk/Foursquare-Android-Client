package com.mashazavolnyuk.client.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mashazavolnyuk.client.MainActivity;
import com.mashazavolnyuk.client.R;
import com.mashazavolnyuk.client.filter.FilterParams;

public class FilterFragment extends BaseFragment {

    TextView selectPlaceAndRadius;
    SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        setHasOptionsMenu(true);
        selectPlaceAndRadius = view.findViewById(R.id.selectPlaceAndRadius);
        selectPlaceAndRadius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).goToMap();
            }
        });
        preferences = getActivity().getSharedPreferences("Filters", Context.MODE_PRIVATE);
        return view;
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

    private void resetFilterSettings() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(FilterParams.SORT_BY_RELEVANCE, true);
        editor.putBoolean(FilterParams.FILTER_BY_EXPENSIVE_LEVEL1, false);
        editor.putBoolean(FilterParams.FILTER_BY_EXPENSIVE_LEVEL2, false);
        editor.putBoolean(FilterParams.FILTER_BY_EXPENSIVE_LEVEL3, false);
        editor.putBoolean(FilterParams.FILTER_BY_EXPENSIVE_LEVEL4, false);
        editor.apply();
    }

    private void updateUIAccordingToFilterSettings() {

    }

}
