package com.mashazavolnyuk.client.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mashazavolnyuk.client.R;
import com.mashazavolnyuk.client.location.ILocationSubsriber;
import com.mashazavolnyuk.client.location.PersonalLocationListener;

public class MainListFragment extends BaseFragment implements ILocationSubsriber {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_list, container, false);
        requestNecessaryPermissions();
        PersonalLocationListener.SetUpLocationListener(getActivity(), this);
        return view;
    }


    private void requestNecessaryPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_FINE_LOCATION,
            }, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            int grantResult = grantResults[i];

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(permission) && grantResult !=
                        PackageManager.PERMISSION_GRANTED) {
                    return;
                } else if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
        }
    }

    @Override
    public void changeData(double latitude, double longitude) {
        showToast("" + latitude + "" + longitude);
    }

    @Override
    public void providerDisabled(String s) {
        showToast("" + s);
    }

    @Override
    public void providerEnabled(String s) {
        showToast("" + s);
    }
}
