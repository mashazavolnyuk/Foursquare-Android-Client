package com.mashazavolnyuk.client.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.mashazavolnyuk.client.data.Data;
import com.mashazavolnyuk.client.data.Item;
import com.mashazavolnyuk.client.repositories.CallbackResponse;
import com.mashazavolnyuk.client.repositories.ListPlaceRepository;

import java.util.List;

public class ListPlaceViewModel extends ViewModel {

    private static final String TAG = "MyViewModel";
    private MutableLiveData<List<Item>> group = new MutableLiveData<>();

    public List<Item> getCache() {
        if (group != null) {
            return group.getValue();
        } else {
            return null;
        }
    }

    public void loadGroups(double latitude, double longitude, final CallbackResponse<List<Item>>
            callbackResponse) {
        Log.d(TAG, "loadGroups()");
        ListPlaceRepository listPlaceRepository = new ListPlaceRepository();
        listPlaceRepository.getListPlaces(latitude, longitude, new CallbackResponse<List<Item>>() {
            @Override
            public void response(List<Item> response) {
                group.setValue((response));
                callbackResponse.response(response);
            }
        });
    }

}
