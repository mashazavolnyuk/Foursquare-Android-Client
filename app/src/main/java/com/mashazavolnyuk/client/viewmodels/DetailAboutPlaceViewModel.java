package com.mashazavolnyuk.client.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.mashazavolnyuk.client.data.Data;
import com.mashazavolnyuk.client.data.Item;
import com.mashazavolnyuk.client.data.photos.DetailedPhoto;
import com.mashazavolnyuk.client.data.photos.PhotoResponseData;
import com.mashazavolnyuk.client.repositories.DetailAboutPlaceRepository;
import com.mashazavolnyuk.client.repositories.IObserverDetailedPhoto;
import com.mashazavolnyuk.client.repositories.IObserverListPlacesData;

public class DetailAboutPlaceViewModel extends ViewModel {

    private final MutableLiveData<Item> selected = new MutableLiveData<Item>();
    private MutableLiveData<DetailedPhoto> photoResponseDataMutableLiveData =
            new MutableLiveData<>();

    public void select(Item item) {
        selected.setValue(item);
    }

    public LiveData<Item> getSelected() {
        return selected;
    }

    public void getPhotoByIdVenue(String id, IObserverDetailedPhoto iObserverDetailedPhoto) {
        DetailAboutPlaceRepository detailAboutPlaceRepository = new DetailAboutPlaceRepository();
        detailAboutPlaceRepository.getDetailedPhotoById(id, new IObserverDetailedPhoto() {
            @Override
            public void newData(LiveData<DetailedPhoto> dataLiveData) {
                photoResponseDataMutableLiveData = (MutableLiveData<DetailedPhoto>) dataLiveData;
                iObserverDetailedPhoto.newData(photoResponseDataMutableLiveData);
            }
        });
    }
}
