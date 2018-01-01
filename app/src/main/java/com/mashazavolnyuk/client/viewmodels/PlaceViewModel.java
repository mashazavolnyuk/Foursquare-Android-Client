package com.mashazavolnyuk.client.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;

import com.mashazavolnyuk.client.data.PlaceItem;
import com.mashazavolnyuk.client.data.Venue;
import com.mashazavolnyuk.client.data.photos.PhotoItem;
import com.mashazavolnyuk.client.data.tips.DetailTip;
import com.mashazavolnyuk.client.repositories.CallbackResponse;
import com.mashazavolnyuk.client.repositories.PlaceRepository;

import java.util.List;

public class PlaceViewModel extends ViewModel {

    private final MutableLiveData<PlaceItem> selectedPlace = new MutableLiveData<>();
    private MutableLiveData<List<PhotoItem>> photoResponseDataMutableLiveData =
            new MutableLiveData<>();

    public void setSelectedPlace(PlaceItem placeItem) {
        selectedPlace.setValue(placeItem);
    }

    public LiveData<PlaceItem> getSelectedPlace() {
        return selectedPlace;
    }

    public void getPhotoByIdVenue(String id, CallbackResponse<List<PhotoItem>> callbackResponse) {
        PlaceRepository placeRepository = new PlaceRepository();
        placeRepository.getDetailedPhotoById(id, response -> {
            photoResponseDataMutableLiveData.setValue(response);
            callbackResponse.response(response);
        });

    }

    public void getTipsByIdVenue(String id, CallbackResponse<List<DetailTip>> callbackResponse) {
        PlaceRepository placeRepository = new PlaceRepository();
        placeRepository.getDetailedTipsById(id, new CallbackResponse<List<DetailTip>>() {
            @Override
            public void response(List<DetailTip> response) {
                callbackResponse.response(response);
            }
        });
    }

    public void loadStaticGoogleMap(Venue venue, CallbackResponse<Bitmap> bitmapCallbackResponse) {
        PlaceRepository placeRepository = new PlaceRepository();
        placeRepository.loadStaticGoogleMap(venue, bitmapCallbackResponse);
    }
}
