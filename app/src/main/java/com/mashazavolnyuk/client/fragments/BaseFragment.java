package com.mashazavolnyuk.client.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.mashazavolnyuk.client.R;

public class BaseFragment extends Fragment {

    protected ProgressDialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void showWaitDialog() {
        if (dialog == null) {
            dialog = new ProgressDialog(getActivity());
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
        }
        dialog.setMessage(getString(R.string.text_wait));
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

    public void showToast(String mess) {
        Toast.makeText(getActivity(), mess, Toast.LENGTH_SHORT).show();
    }

    protected ProgressDialog getDialog() {
        return dialog;
    }
}
