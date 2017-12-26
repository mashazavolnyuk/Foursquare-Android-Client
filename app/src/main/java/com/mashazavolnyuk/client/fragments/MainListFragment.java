package com.mashazavolnyuk.client.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mashazavolnyuk.client.repositories.IObserverFinishedLoadingPlace;
import com.mashazavolnyuk.client.MainActivity;
import com.mashazavolnyuk.client.R;
import com.mashazavolnyuk.client.adapters.IListPlacesOnClickListener;
import com.mashazavolnyuk.client.adapters.ListPlacesAdapter;
import com.mashazavolnyuk.client.data.Data;
import com.mashazavolnyuk.client.data.Group;
import com.mashazavolnyuk.client.data.Item;
import com.mashazavolnyuk.client.viewmodels.DetailAboutPlaceViewModel;
import com.mashazavolnyuk.client.viewmodels.ListPlaceViewModel;

import java.util.List;


public class MainListFragment extends BaseFragment implements SearchView.OnQueryTextListener,
        IListPlacesOnClickListener, LocationListener {

    private static final int LOCATION_REQUEST_CODE = 1;
    private DetailAboutPlaceViewModel detailAboutPlaceViewMode;
    private ListPlaceViewModel listPlaceViewMode;
    private RecyclerView listPlaces;
    private LocationManager locationManager;
    private ListPlacesAdapter listPlacesAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_list, container, false);
        listPlaces = view.findViewById(R.id.listPlaces);
        setHasOptionsMenu(true);
        listPlaceViewMode = ViewModelProviders.of((FragmentActivity) getActivity()).get(
                ListPlaceViewModel.class);
        detailAboutPlaceViewMode = ViewModelProviders.of((FragmentActivity) getActivity()).get(
                DetailAboutPlaceViewModel.class);
        List<Group> groups = listPlaceViewMode.getCache();
        if (groups != null) {
            fillData(groups.get(0));
        } else {
            tryStartFindLocation();
        }
        return view;
    }

    private void tryStartFindLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean isFineLocationDisabled = false;
            boolean isCoarseLocationDisables = false;
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                }, LOCATION_REQUEST_CODE);
                isFineLocationDisabled = true;
            }
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                }, LOCATION_REQUEST_CODE);
                isCoarseLocationDisables = true;
            }
            if (!isFineLocationDisabled || !isCoarseLocationDisables) {
                startFindLocation();
            }
        } else {
            startFindLocation();
        }
    }

    @SuppressLint("MissingPermission")
    private void startFindLocation() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    100 * 10, 10, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    100 * 10, 10, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        listPlaceViewMode.loadGroups(location.getLatitude(), location.getLongitude(), new IObserverFinishedLoadingPlace() {
            @Override
            public void loadedData(Data data) {
                fillData(data.getResponse().getGroups().get(0));
            }
        });
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


//        FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
//        mFusedLocationProviderClient.getLastLocation()
//                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        // Got last known location. In some rare situations this can be null.
//                        if (location != null) {
//                            testRequest(location.getLatitude(), location.getLongitude());
//                        }
//                    }
//                });
//
//        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//}

//    private void onLocationUpdate(Location location){
//        showToast("" + location.getLatitude() + "" + location.getLongitude());
//        if(locationListener != null) {
//            locationManager.removeUpdates(locationListener);
//            locationListener = null;
//        }
//        testRequest(location.getLatitude(), location.getLongitude());
//    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                //Search View is collapsed
                return false;
            }
        });
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                ((MainActivity) (getActivity())).goToFilter();
                return true;
            default:
                break;
        }

        return false;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            int grantResult = grantResults[i];

            if (!shouldShowRequestPermissionRationale(permission) && grantResult !=
                    PackageManager.PERMISSION_GRANTED) {
                tryStartFindLocation();
            } else if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
    }

    private void fillData(Group groupPlaces) {
        listPlacesAdapter = new ListPlacesAdapter(getActivity(), groupPlaces.getItems(), this);
        listPlaces.setLayoutManager(new LinearLayoutManager(getActivity()));
        listPlaces.setAdapter(listPlacesAdapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        listPlacesAdapter.getFilter().filter(newText);
        return false;
    }

    @Override
    public void setItem(Item item) {
        detailAboutPlaceViewMode.select(item);
        ((MainActivity) getActivity()).goToAboutSelectedPlace();
    }
}
