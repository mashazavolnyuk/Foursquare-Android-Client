package com.mashazavolnyuk.client.location;

public interface ILocationSubsriber {

    void changeData(double latitude, double longitude);
    void providerDisabled(String s);
    void providerEnabled(String s);
}
