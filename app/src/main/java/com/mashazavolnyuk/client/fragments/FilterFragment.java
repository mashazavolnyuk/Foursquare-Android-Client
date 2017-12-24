package com.mashazavolnyuk.client.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mashazavolnyuk.client.MainActivity;
import com.mashazavolnyuk.client.R;

public class FilterFragment extends BaseFragment {

    TextView selectPlaceAndRadius;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        selectPlaceAndRadius = view.findViewById(R.id.selectPlaceAndRadius);
        selectPlaceAndRadius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).goToMap();
            }
        });
        return view;
    }
}
