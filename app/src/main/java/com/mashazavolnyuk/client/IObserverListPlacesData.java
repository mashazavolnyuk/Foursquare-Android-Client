package com.mashazavolnyuk.client;

import android.arch.lifecycle.LiveData;

import com.mashazavolnyuk.client.data.Data;

public interface IObserverListPlacesData {
    void newData(LiveData<Data> dataLiveData);
}
