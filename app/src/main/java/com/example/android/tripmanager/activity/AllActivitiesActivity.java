package com.example.android.tripmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.android.tripmanager.R;
import com.example.android.tripmanager.adapter.AllActivitiesAdapter;
import com.example.android.tripmanager.database.bean.ActivityBean;
import com.example.android.tripmanager.database.dao.ActivityDao;

import java.util.ArrayList;

public class AllActivitiesActivity extends AppCompatActivity {

    private RecyclerView mRecyclerViewAllActivities;
    private Button mBtnSkip;

    /**
     * The identifier of the trip where
     * we want to add an activity
     */
    private long mTripId;

    public static final String EXTRA_TRIP_ID = "tripId";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_all_activities);

        // Init views
        mRecyclerViewAllActivities = findViewById(R.id.rv_all_activities);
        mBtnSkip = findViewById(R.id.btn_skip);
        mRecyclerViewAllActivities.setLayoutManager(new LinearLayoutManager(this));

        setTitle(R.string.choose_existing_activity);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTripId = getIntent()
                .getLongExtra(EXTRA_TRIP_ID, -1);

        // Get all the activities from database
        ActivityDao activityDao = new ActivityDao(getApplicationContext());
        ArrayList<ActivityBean> allActivities = activityDao.getAllActivitiesWithAverage(true);

        // Go directly to next activity if there is nothing to show
        if (allActivities.isEmpty()) {
            startNextActivity();
        }

        // Init adapter and display data
        AllActivitiesAdapter adapter = new AllActivitiesAdapter(this,
                allActivities,
                mTripId);
        mRecyclerViewAllActivities.setAdapter(adapter);

        mBtnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNextActivity();
            }
        });
    }


    private void startNextActivity() {
        Intent createNewActivityIntent = new Intent(AllActivitiesActivity.this, AddTripActivityActivity.class);
        createNewActivityIntent.putExtra(AddTripActivityActivity.EXTRA_TRIP_ID, mTripId);
        startActivity(createNewActivityIntent);

        // Current activity should be killed
        finish();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
