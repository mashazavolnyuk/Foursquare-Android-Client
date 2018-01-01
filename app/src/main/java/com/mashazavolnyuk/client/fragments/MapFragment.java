package com.mashazavolnyuk.client.fragments;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.mashazavolnyuk.client.Constants;
import com.mashazavolnyuk.client.R;
import com.mashazavolnyuk.client.data.locationUser.BaseLocation;
import com.mashazavolnyuk.client.filter.FilterParams;
import com.mashazavolnyuk.client.utils.ConverterForZoom;
import com.mashazavolnyuk.client.utils.GeocoderUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MapFragment extends BaseFragment implements OnMapReadyCallback,
        GoogleMap.OnCameraIdleListener {

    @BindView((R.id.radiusSeekBar))
    SeekBar zoomLevelMap;
    @BindView(R.id.map_view)
    MapView mapView;
    private GoogleMap map;
    private SharedPreferences preferences;
    private Unbinder unbinder;
    private BaseLocation baseLocation;
    private double currentLatitude;
    private double currentLongitude;
    private float radius;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        unbinder = ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        preferences = getActivity().getSharedPreferences(Constants.PREF_FILTERS, Context.MODE_PRIVATE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            zoomLevelMap.setMin(0);
        }
        setListener();
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapView.getMapAsync(this);
        return view;
    }

    private void setListener() {
        zoomLevelMap.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean b) {
                double valueForZoom = ConverterForZoom.getKmByPosition(value);
                radius = (float) valueForZoom;
                float zoom = (float) getZoomLevel(valueForZoom);
                zoomMap(currentLatitude, currentLongitude, zoom);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.map_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_location:
                saveLocation(getActivity().getApplication(), currentLatitude, currentLongitude);
                return true;
            default:
                break;
        }

        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        map = googleMap;
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnCameraIdleListener(this);
        baseLocation = getBaseLocation();
        assert baseLocation != null;
        LatLng coordinate = new LatLng(baseLocation.getLatitude(), baseLocation.getLongitude());
        radius = baseLocation.getRadius();
        zoomLevelMap.setProgress(ConverterForZoom.getPositionByRadius((double) radius));
        float zoom = (float) getZoomLevel(radius);
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                coordinate, zoom);
        map.animateCamera(location);
    }

    private void zoomMap(double latitude, double longitude, float zoom) {
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude),
                zoom);
        map.animateCamera(location);
    }

    private double getZoomLevel(double radius) {
        return Math.round(14 - Math.log(radius) / Math.log(2));
    }

    private BaseLocation getBaseLocation() {
        Gson gson = new Gson();
        String userLocationModel = preferences.getString(FilterParams.USER_LOCATION, "");
        String selectedLocationModel = preferences.getString(FilterParams.SELECTED_LOCATION, "");
        if (!selectedLocationModel.isEmpty()) {
            baseLocation = gson.fromJson(selectedLocationModel, BaseLocation.class);
            if (baseLocation != null) {
                return baseLocation;
            } else {
                baseLocation = gson.fromJson(userLocationModel, BaseLocation.class);
                return baseLocation;
            }
        } else {
            baseLocation = gson.fromJson(userLocationModel, BaseLocation.class);
            return baseLocation;
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCameraIdle() {
        LatLng target = map.getCameraPosition().target;
        currentLatitude = target.latitude;
        currentLongitude = target.longitude;
        showAddress(getActivity().getApplication(), target.latitude, target.longitude);
    }

    public void showAddress(Application context, double latitude, double longitude) {
        String address = GeocoderUtil.getAddressByLocation(context, latitude, longitude);
        if (address != null) {
            getActivity().setTitle(address);
        } else {
            getActivity().setTitle("not found address");
        }
    }

    private void saveLocation(Application context, double latitude, double longitude) {
        String address = GeocoderUtil.getAddressByLocation(context, latitude, longitude);
        BaseLocation selectedLocation = new BaseLocation();
        selectedLocation.setAddress(address);
        selectedLocation.setLatitude(latitude);
        selectedLocation.setLongitude(longitude);
        selectedLocation.setRadius(radius * Constants.DEFAULT_RADIUS);
        Gson gson = new Gson();
        String jsonModel = gson.toJson(selectedLocation);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(FilterParams.SELECTED_LOCATION, jsonModel);
        editor.apply();
    }
}
