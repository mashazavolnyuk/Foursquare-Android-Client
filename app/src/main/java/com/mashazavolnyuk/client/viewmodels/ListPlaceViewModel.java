package com.mashazavolnyuk.client.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.mashazavolnyuk.client.repositories.IObserverFinishedLoadingPlace;
import com.mashazavolnyuk.client.repositories.IObserverListPlacesData;
import com.mashazavolnyuk.client.repositories.ListPlaceRepository;
import com.mashazavolnyuk.client.data.Data;
import com.mashazavolnyuk.client.data.Group;

import java.util.List;

public class ListPlaceViewModel extends ViewModel {

    private static final String TAG = "MyViewModel";
    private MutableLiveData<List<Group>> group = new MutableLiveData<>();

    public List<Group> getCache() {
        if (group != null) {
            return group.getValue();
        } else {
            return null;
        }
    }

    public void loadGroups(double latitude, double longitude, final IObserverFinishedLoadingPlace
            iObserverFinishedLoadingPlace) {
        Log.d(TAG, "loadGroups()");
        ListPlaceRepository listPlaceRepository = new ListPlaceRepository();
        listPlaceRepository.getProjectList(latitude, longitude, new IObserverListPlacesData() {
            @Override
            public void newData(LiveData<Data> newDataLiveData) {
                group.setValue(newDataLiveData.getValue().getResponse().getGroups());
                iObserverFinishedLoadingPlace.loadedData(newDataLiveData.getValue());
            }
        });
    }

}
