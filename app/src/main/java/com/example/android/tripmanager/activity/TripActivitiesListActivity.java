package com.example.android.tripmanager.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.android.tripmanager.R;
import com.example.android.tripmanager.adapter.TripActivitiesAdapter;
import com.example.android.tripmanager.database.bean.TripActivityBean;
import com.example.android.tripmanager.database.dao.TripDao;

import java.util.ArrayList;

public class TripActivitiesListActivity extends AppCompatActivity {

    private RecyclerView mActivitiesList;

    private FloatingActionButton mFabAddActivity;

    private TextView mTvNoActivity;

    /**
     * The trip for which we want to display the
     * activities of
     */
    private long mTripId;

    public static final String EXTRA_TRIP_ID = "tripId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_activities_list);

        mActivitiesList = findViewById(R.id.rv_trip_activities);
        mFabAddActivity = findViewById(R.id.fab_add_trip_activity);
        mTvNoActivity = findViewById(R.id.tv_no_trip_activity);
        mActivitiesList.setLayoutManager(new LinearLayoutManager(this));

        setTitle(R.string.trip_details);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTripId = getIntent()
                .getLongExtra(EXTRA_TRIP_ID, -1);

        // Set FABs action
        mFabAddActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start an Android activity to show the list of all the activities
                // So the user can create a new trip activity from an existing activity
                // or create a new trip activity from scratch
                Intent addActivityIntent = new Intent(TripActivitiesListActivity.this, AllActivitiesActivity.class);
                addActivityIntent.putExtra(AllActivitiesActivity.EXTRA_TRIP_ID, mTripId);
                startActivity(addActivityIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Update the list of trip activities
        loadActivities();
    }

    public void loadActivities() {
        TripDao tripDao = new TripDao(getApplicationContext());
        ArrayList<TripActivityBean> tripActivities = tripDao.getActivitiesByTripId(mTripId);
        TripActivitiesAdapter tripActivitiesAdapter = new TripActivitiesAdapter(this, tripActivities);
        // Display the activities
        mActivitiesList.setAdapter(tripActivitiesAdapter);

        if (tripActivities.isEmpty()) {
            // No activity to display
            mActivitiesList.setVisibility(View.GONE);
            mTvNoActivity.setVisibility(View.VISIBLE);
        } else {
            // At least one activity to display
            mActivitiesList.setVisibility(View.VISIBLE);
            mTvNoActivity.setVisibility(View.GONE);
        }
    }


    public void onActivityDeleted() {
        // Reload the activities from the database
        loadActivities();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
