package com.example.android.tripmanager.activity;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.android.tripmanager.R;
import com.example.android.tripmanager.fragment.TripsListFragment;
import com.example.android.tripmanager.fragment.YourAccountFragment;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView mBottomNavigationView;

    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final FragmentManager fragmentManager = getSupportFragmentManager();

        // Init the bottom navigation menu
        mBottomNavigationView = findViewById(R.id.bottom_navigation_view);
        mBottomNavigationView.inflateMenu(R.menu.home_bottom_menu);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                Bundle bundle = new Bundle();
                switch (itemId) {

                    case R.id.item_your_trips:
                        mCurrentFragment = new TripsListFragment();
                        bundle.putSerializable(TripsListFragment.EXTRA_TRIP_SELECTION,
                                TripsListFragment.TripSelection.USER_TRIPS);
                        break;

                    case R.id.item_all_trips:
                        mCurrentFragment = new TripsListFragment();
                        bundle.putSerializable(TripsListFragment.EXTRA_TRIP_SELECTION,
                                TripsListFragment.TripSelection.ALL_TRIPS);
                        break;

                    case R.id.item_your_account:
                        mCurrentFragment = new YourAccountFragment();
                        break;
                }

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                mCurrentFragment.setArguments(bundle);
                transaction.replace(R.id.main_container, mCurrentFragment).commit();
                return true;
            }
        });

        mBottomNavigationView.setSelectedItemId(R.id.item_all_trips);

    }
}
