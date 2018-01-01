package com.mashazavolnyuk.client.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.mashazavolnyuk.client.ProjectConstants;
import com.mashazavolnyuk.client.data.locationUser.BaseLocation;
import com.mashazavolnyuk.client.data.locationUser.SelectedLocation;
import com.mashazavolnyuk.client.data.locationUser.UserLocation;
import com.mashazavolnyuk.client.filter.FilterParams;
import com.mashazavolnyuk.client.MainActivity;
import com.mashazavolnyuk.client.R;
import com.mashazavolnyuk.client.adapters.IListPlacesOnClickListener;
import com.mashazavolnyuk.client.adapters.ListPlacesAdapter;
import com.mashazavolnyuk.client.data.Item;
import com.mashazavolnyuk.client.utils.GeocoderUtil;
import com.mashazavolnyuk.client.viewmodels.DetailAboutPlaceViewModel;
import com.mashazavolnyuk.client.viewmodels.ListPlaceViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.content.Context.LOCATION_SERVICE;


public class MainListFragment extends BaseFragment implements SearchView.OnQueryTextListener,
        IListPlacesOnClickListener, LocationListener {

    private static final int LOCATION_REQUEST_CODE = 1;
    @BindView(R.id.listPlaces)
    RecyclerView recyclerViewPlaces;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private DetailAboutPlaceViewModel detailAboutPlaceViewMode;
    private ListPlaceViewModel listPlaceViewMode;
    private LocationManager locationManager;
    private ListPlacesAdapter listPlacesAdapter;
    private SharedPreferences preferences;
    private List<Item> listPlaces;
    private BaseLocation baseLocation;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        preferences = getActivity().getSharedPreferences("Filters", Context.MODE_PRIVATE);
        listPlaceViewMode = ViewModelProviders.of((FragmentActivity) getActivity()).get(
                ListPlaceViewModel.class);
        detailAboutPlaceViewMode = ViewModelProviders.of((FragmentActivity) getActivity()).get(
                DetailAboutPlaceViewModel.class);
        listPlaces = listPlaceViewMode.getCache();
        baseLocation = getBaseLocation();
        setListeners();
        showDialog("Please wait", ProgressDialog.STYLE_SPINNER, false);
        if (listPlaces != null && listPlaces.size() > 0) {
            fillData(listPlaces);
        } else {
            tryStartFindLocation();
        }
        return view;
    }

    private void setListeners() {
        swipeRefreshLayout.setOnRefreshListener(this::tryStartFindLocation);
    }

    private BaseLocation getBaseLocation() {
        Gson gson = new Gson();
        String userLocationModel = preferences.getString(FilterParams.USER_LOCATION, "");
        String selectedLocationModel = preferences.getString(FilterParams.SELECTED_LOCATION, "");
        if (!selectedLocationModel.isEmpty()) {
            baseLocation = gson.fromJson(selectedLocationModel, SelectedLocation.class);
            return baseLocation;
        } else {
            if (!userLocationModel.isEmpty()) {
                baseLocation = gson.fromJson(userLocationModel, UserLocation.class);
                return baseLocation;
            }
        }
        return null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (getDialog() != null)
            outState.putBoolean("isShowDialog", getDialog().isShowing());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
        if (baseLocation != null) {
            if (baseLocation.getLatitude() > 0.0 && baseLocation.getLongitude() > 0.0) {
                loadPlacesByLocation(baseLocation.getLatitude(), baseLocation.getLongitude(), baseLocation.getRadius());
            }
        } else {
            locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(LOCATION_SERVICE);
            Location location = getLastKnownLocation();
            if (location != null) {
                this.onLocationChanged(location);
            } else {
                hideDialog();
                showToast("Please enable GPS and try again");
            }
        }
    }

    @SuppressLint("MissingPermission")
    private Location getLastKnownLocation() {
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    @Override
    public void onLocationChanged(Location location) {
        saveLocation(location);
        loadPlacesByLocation(location.getLatitude(), location.getLongitude(), ProjectConstants.DEFAULT_RADIUS);
        locationManager.removeUpdates(this);
    }

    private void loadPlacesByLocation(double latitude, double longitude, float radius) {
        String listPricesFilter = getFillPricesFilter();
        boolean isSortByRelevance = preferences.getBoolean(FilterParams.SORT_BY_RELEVANCE, true);
        listPlaceViewMode.loadGroups(latitude, longitude, !isSortByRelevance, listPricesFilter, radius, data -> {
            listPlaces = data;
            fillData(listPlaces);
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private String getFillPricesFilter() {
        StringBuilder stringBuilder = new StringBuilder();
        boolean level1 = preferences.getBoolean(FilterParams.FILTER_BY_EXPENSIVE_LEVEL1, false);
        boolean level2 = preferences.getBoolean(FilterParams.FILTER_BY_EXPENSIVE_LEVEL2, false);
        boolean level3 = preferences.getBoolean(FilterParams.FILTER_BY_EXPENSIVE_LEVEL3, false);
        boolean level4 = preferences.getBoolean(FilterParams.FILTER_BY_EXPENSIVE_LEVEL4, false);
        if (level1) {
            stringBuilder.append("1");
            stringBuilder.append(",");
        }
        if (level2) {
            stringBuilder.append("2");
            stringBuilder.append(",");
        }
        if (level3) {
            stringBuilder.append("3");
            stringBuilder.append(",");
        }
        if (level4) {
            stringBuilder.append("4");
        }
        return stringBuilder.toString();
    }

    private void saveLocation(Location location) {
        String address = GeocoderUtil.getAddressByLocation(getActivity().getApplication(), location.getLatitude(),
                location.getLongitude());
        UserLocation userLocation = new UserLocation();
        userLocation.setLatitude(location.getLatitude());
        userLocation.setLongitude(location.getLongitude());
        userLocation.setAddress(address);
        Gson gson = new Gson();
        String jsonModel = gson.toJson(userLocation);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(FilterParams.USER_LOCATION, jsonModel);
        editor.apply();
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) item.getActionView();
        MenuItem menuItem = menu.findItem(R.id.action_search);
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentApiVersion >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    listPlacesAdapter.setNewData(listPlaces);
                    return true;
                }

                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {

                    return true;
                }
            });
        } else {
            searchView.setOnSearchClickListener(view -> {
            });
            searchView.setOnCloseListener(() -> {
                listPlacesAdapter.setNewData(listPlaces);
                return false;
            });
        }
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

    private void fillData(List<Item> listPlaces) {
        listPlacesAdapter = new ListPlacesAdapter(getActivity(), listPlaces, this);
        recyclerViewPlaces.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewPlaces.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerViewPlaces.setAdapter(listPlacesAdapter);
        hideDialog();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        listPlacesAdapter.setNewData(listPlaces);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        filterPlace(newText);
        return false;
    }

    @Override
    public void setItem(Item item) {
        detailAboutPlaceViewMode.select(item);
        ((MainActivity) getActivity()).goToAboutSelectedPlace();
    }

    private void filterPlace(CharSequence constraint) {
        if (constraint != null && constraint.length() > 0) {
            List<Item> filterList = new ArrayList<>();
            List<Item> items = listPlaces;
            for (int i = 0; i < items.size(); i++) {
                if ((items.get(i).getVenue().getName().toUpperCase()).contains(constraint.toString().toUpperCase()) ||
                        (items.get(i).getVenue().getCategories().get(0).getShortName().toUpperCase()).contains(constraint.toString().toUpperCase())
                        ) {
                    filterList.add(listPlaces.get(i));
                }
            }
            listPlacesAdapter.setNewData(filterList);
        }

    }
}
