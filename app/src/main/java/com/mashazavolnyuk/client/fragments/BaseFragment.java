package com.mashazavolnyuk.client.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class BaseFragment extends Fragment {

    protected ProgressDialog dialog;
    protected static String TAG;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void showDialog(String mess, int ProgressStyle, boolean cancel) {
        if (dialog == null) {
            dialog = new ProgressDialog(getActivity());
            dialog.setProgressStyle(ProgressStyle);
            dialog.setCancelable(cancel);
        }
        if (mess == null || mess.isEmpty()) {
            dialog.setMessage("Please wait");
        } else {
            dialog.setMessage(mess);
        }
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    public void hideDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public void showToast(String mess, int TYPE) {
        Toast.makeText(getActivity(), mess, TYPE).show();
    }

    public void showToast(String mess) {
        Toast.makeText(getActivity(), mess, Toast.LENGTH_SHORT).show();
    }

    public void setTagName(String tagName) {
        TAG = tagName;
    }

    protected ProgressDialog getDialog() {
        return dialog;
    }
}
