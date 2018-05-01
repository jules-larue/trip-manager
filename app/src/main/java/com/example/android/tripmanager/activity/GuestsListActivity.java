package com.example.android.tripmanager.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.tripmanager.R;

public class GuestsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guests_list);

        setTitle(R.string.trip_guests);
    }
}
