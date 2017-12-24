package com.mashazavolnyuk.client.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mashazavolnyuk.client.R;
import com.mashazavolnyuk.client.adapters.ListPlacesAdapter;
import com.mashazavolnyuk.client.api.RetrofitClient;
import com.mashazavolnyuk.client.api.requests.IRequestListPlaces;
import com.mashazavolnyuk.client.data.Data;
import com.mashazavolnyuk.client.data.Venue;
import com.mashazavolnyuk.client.location.ILocationSubsriber;
import com.mashazavolnyuk.client.location.PersonalLocationListener;

import java.util.List;

import retrofit2.Callback;


public class MainListFragment extends BaseFragment implements ILocationSubsriber, SearchView.OnQueryTextListener {

    RecyclerView listPlaces;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_list, container, false);
        listPlaces = view.findViewById(R.id.listPlaces);
        setHasOptionsMenu(true);
        requestNecessaryPermissions();
        PersonalLocationListener.SetUpLocationListener(getActivity(), this);
        return view;
    }


    private void requestNecessaryPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_FINE_LOCATION,
            }, 1);
        }
    }

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
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // use this method when query submitted
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // use this method for auto complete search process
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                showToast("filter press");
                return true;
            default:
                break;
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            int grantResult = grantResults[i];

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(permission) && grantResult !=
                        PackageManager.PERMISSION_GRANTED) {
                    return;
                } else if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
        }
    }

    @Override
    public void changeData(double latitude, double longitude) {
        showToast("" + latitude + "" + longitude);
        testRequest(latitude, longitude);
    }

    @Override
    public void providerDisabled(String s) {
        showToast("" + s);
    }

    @Override
    public void providerEnabled(String s) {
        showToast("" + s);
    }

    private void testRequest(double latitude, double longitude) {
        IRequestListPlaces iRequestListPlaces = RetrofitClient.getRetrofit().create(IRequestListPlaces.class);
        String id = getString(R.string.foursquare_client_id);
        String secret = getString(R.string.foursquare_client_secret);
        iRequestListPlaces.getListPlaces(id, secret, "" + latitude + "," + longitude).enqueue(new Callback<Data>() {
            @Override
            public void onResponse(retrofit2.Call<Data> call, retrofit2.Response<Data> response) {
                Data data = response.body();
                fillData(data.getResponse().getVenues());
            }

            @Override
            public void onFailure(retrofit2.Call<Data> call, Throwable t) {

            }
        });
    }

    private void fillData(List<Venue> venues) {
        ListPlacesAdapter listPlacesAdapter = new ListPlacesAdapter(getActivity(), venues);
        listPlaces.setLayoutManager(new LinearLayoutManager(getActivity()));
        listPlaces.setAdapter(listPlacesAdapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
