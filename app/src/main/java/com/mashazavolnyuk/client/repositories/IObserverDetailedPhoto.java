package com.mashazavolnyuk.client.repositories;

import android.arch.lifecycle.LiveData;
import com.mashazavolnyuk.client.data.photos.DetailedPhoto;

public interface IObserverDetailedPhoto {
    void newData(LiveData<DetailedPhoto> dataLiveData);
}
