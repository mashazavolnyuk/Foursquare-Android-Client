package com.mashazavolnyuk.client.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;
import android.util.Log;

import com.mashazavolnyuk.client.data.Item;
import com.mashazavolnyuk.client.data.Venue;
import com.mashazavolnyuk.client.data.photos.PhotoItem;
import com.mashazavolnyuk.client.data.tips.DetailTip;
import com.mashazavolnyuk.client.data.tips.TipElement;
import com.mashazavolnyuk.client.repositories.CallbackResponse;
import com.mashazavolnyuk.client.repositories.DetailAboutPlaceRepository;

import java.util.List;

public class DetailAboutPlaceViewModel extends ViewModel {

    private final MutableLiveData<Item> selected = new MutableLiveData<>();
    private MutableLiveData<List<PhotoItem>> photoResponseDataMutableLiveData =
            new MutableLiveData<>();

    public void select(Item item) {
        selected.setValue(item);
    }

    public LiveData<Item> getSelected() {
        return selected;
    }

    public void getPhotoByIdVenue(String id, CallbackResponse<List<PhotoItem>> callbackResponse) {
        DetailAboutPlaceRepository detailAboutPlaceRepository = new DetailAboutPlaceRepository();
        detailAboutPlaceRepository.getDetailedPhotoById(id, response -> {
            photoResponseDataMutableLiveData.setValue(response);
            callbackResponse.response(response);
        });

    }

    public void getTipsByIdVenue(String id, CallbackResponse<List<DetailTip>> callbackResponse) {
        DetailAboutPlaceRepository detailAboutPlaceRepository = new DetailAboutPlaceRepository();
        detailAboutPlaceRepository.getDetailedTipsById(id, new CallbackResponse<List<DetailTip>>() {
            @Override
            public void response(List<DetailTip> response) {
                callbackResponse.response(response);
            }
        });
    }

    public void loadStaticGoogleMap(Venue venue, CallbackResponse<Bitmap> bitmapCallbackResponse) {
        DetailAboutPlaceRepository detailAboutPlaceRepository = new DetailAboutPlaceRepository();
        detailAboutPlaceRepository.loadStaticGoogleMap(venue, bitmapCallbackResponse);
    }
}
