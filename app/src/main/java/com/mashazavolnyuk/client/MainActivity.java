package com.mashazavolnyuk.client;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mashazavolnyuk.client.fragments.MainListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        goToFragment(new MainListFragment());
    }

    public void goToFragment(Fragment fragment){
        getFragmentManager().beginTransaction().add(R.id.content, fragment)
                .addToBackStack(fragment.getTag()).commit();
    }
}
