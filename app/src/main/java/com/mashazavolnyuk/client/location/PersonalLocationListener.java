package com.mashazavolnyuk.client.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

public class PersonalLocationListener implements LocationListener {

    private static Location location; // здесь будет всегда доступна самая последняя информация о местоположении пользователя.
    private static ILocationSubsriber sILocationSubsriber;
    private static LocationManager locationManager;

    public void SetUpLocationListener(Context context, ILocationSubsriber iLocationSubsriber) // это нужно запустить в самом начале работы программы
    {
        sILocationSubsriber = iLocationSubsriber;
        locationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new PersonalLocationListener();

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (locationManager != null) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    100 * 10, 10, locationListener);
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 100 * 10, 10,
                    locationListener);
        }
        if (locationManager != null) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
    }

    public void stop() {
        locationManager.removeUpdates(this);
        location = null;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (sILocationSubsriber != null) {
            sILocationSubsriber.changeData(location.getLatitude(), location.getLongitude());
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        if (sILocationSubsriber != null) {
            sILocationSubsriber.providerEnabled(s);
        }
    }

    @Override
    public void onProviderDisabled(String s) {
        if (sILocationSubsriber != null) {
            sILocationSubsriber.providerDisabled(s);
        }
    }
}
