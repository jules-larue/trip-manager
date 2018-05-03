package com.example.android.tripmanager.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.android.tripmanager.R;
import com.example.android.tripmanager.adapter.GuestsAdapter;
import com.example.android.tripmanager.database.bean.UserBean;
import com.example.android.tripmanager.database.dao.TripDao;
import com.example.android.tripmanager.database.dao.UserDao;

import java.util.ArrayList;

public class GuestsActivity extends AppCompatActivity {

    private TextView mTvNbGuests;

    private RecyclerView mGuestsList;

    private TextView mTvNoGuest;

    private long mTripId;

    public static final String EXTRA_TRIP_ID = "tripId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guests);

        mTvNbGuests = findViewById(R.id.tv_nb_guests);
        mGuestsList = findViewById(R.id.rv_guests);
        mTvNoGuest = findViewById(R.id.tv_no_guest);
        mGuestsList.setLayoutManager(new LinearLayoutManager(this));

        mTripId = getIntent()
                .getExtras()
                .getLong(EXTRA_TRIP_ID);

        TripDao tripDao = new TripDao(this);
        ArrayList<UserBean> guests = tripDao.getGuestsByTripId(mTripId);
        GuestsAdapter guestsAdapter = new GuestsAdapter(this, guests);
        mGuestsList.setAdapter(guestsAdapter);

        if (guests.isEmpty()) {
            // No guest in the trip
            mTvNbGuests.setVisibility(View.GONE);
            mGuestsList.setVisibility(View.GONE);
            mTvNoGuest.setVisibility(View.VISIBLE);
        } else {
            // At least one guest
            mTvNbGuests.setVisibility(View.VISIBLE);
            mGuestsList.setVisibility(View.VISIBLE);
            mTvNoGuest.setVisibility(View.GONE);

            // Init the number of guests text view
            if (guests.size() == 1) {
                mTvNbGuests.setText(getString(R.string.one_guest));
            } else {
                // More than 1 guest
                String nbGuestsText = String.format(getString(R.string.n_guests),
                        guests.size());
                mTvNbGuests.setText(nbGuestsText);
            }
        }
    }
}
