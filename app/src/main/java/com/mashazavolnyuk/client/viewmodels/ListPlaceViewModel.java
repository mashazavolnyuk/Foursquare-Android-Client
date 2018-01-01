package com.mashazavolnyuk.client.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.mashazavolnyuk.client.data.PlaceItem;
import com.mashazavolnyuk.client.repositories.CallbackResponse;
import com.mashazavolnyuk.client.repositories.ListPlaceRepository;

import java.util.List;

public class ListPlaceViewModel extends ViewModel {

    private static final String TAG = "MyViewModel";
    private MutableLiveData<List<PlaceItem>> group = new MutableLiveData<>();

    public List<PlaceItem> getCache() {
        if (group != null) {
            return group.getValue();
        } else {
            return null;
        }
    }

    public void loadGroups(double latitude, double longitude, boolean isSortByDistance,
                           String prices, float radius, final CallbackResponse<List<PlaceItem>>
                                   callbackResponse) {
        Log.d(TAG, "loadGroups()");
        ListPlaceRepository listPlaceRepository = new ListPlaceRepository();
        listPlaceRepository.getListPlaces(latitude, longitude, isSortByDistance, prices, radius, response -> {
            group.setValue((response));
            callbackResponse.response(response);
        });
    }

}
