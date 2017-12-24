package com.mashazavolnyuk.client;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mashazavolnyuk.client.fragments.FilterFragment;
import com.mashazavolnyuk.client.fragments.MainListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        goToListPlaces();
    }

    public void goToListPlaces() {
        goToFragment(new MainListFragment());
    }

    public void goToFilter() {
        goToFragment(new FilterFragment());
    }

    private void goToFragment(Fragment fragment) {
        getFragmentManager().beginTransaction().replace(R.id.content, fragment)
                .addToBackStack(fragment.getTag()).commit();
    }
}
