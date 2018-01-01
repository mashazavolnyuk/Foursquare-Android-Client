package com.mashazavolnyuk.client;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.mashazavolnyuk.client.fragments.PlaceFragment;
import com.mashazavolnyuk.client.fragments.FilterFragment;
import com.mashazavolnyuk.client.fragments.MainListFragment;
import com.mashazavolnyuk.client.fragments.MapFragment;

public class MainActivity extends AppCompatActivity {
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getFragmentManager();
        if (fragmentManager.getBackStackEntryCount() == 0) {
            goToListPlaces();
        }
        fragmentManager.addOnBackStackChangedListener(this::checkBackButton);
    }

    public void goToListPlaces() {
        goToFragment(new MainListFragment());
    }

    public void goToFilter() {
        goToFragment(new FilterFragment());
    }

    public void goToMap() {
        goToFragment(new MapFragment());
    }

    public void goToAboutSelectedPlace() {
        goToFragment(new PlaceFragment());
    }

    private void goToFragment(Fragment fragment) {
        fragmentManager.beginTransaction().replace(R.id.content, fragment)
                .addToBackStack(fragment.getTag()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkBackButton(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar == null){
            return;
        }
        if (getFragmentManager().getBackStackEntryCount() > 1) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        } else {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(false);
        }
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() == 1) {
            finish();
        }
        super.onBackPressed();
    }
}
