package com.mashazavolnyuk.client.fragments;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.mashazavolnyuk.client.R;
import com.mashazavolnyuk.client.utils.GeocoderUtil;



public class MapFragment extends BaseFragment implements OnMapReadyCallback,
        GoogleMap.OnCameraIdleListener {

    GoogleMap map;
    private static final String TAG = "MapFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        MapView mMapView = view.findViewById(R.id.map_dashBoard);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);
        return view;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        map = googleMap;
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnCameraIdleListener(this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCameraIdle() {
        LatLng target = map.getCameraPosition().target;
        showAddress(getActivity(), target.latitude, target.longitude);
    }

    public void showAddress(Context context, double latitude, double longitude) {
        String address = GeocoderUtil.getAddressByLocation(context, latitude, longitude);
        if (address != null) {
            getActivity().setTitle(address);
        } else {
            getActivity().setTitle("not found address");
        }
    }
}
