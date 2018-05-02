package com.example.android.tripmanager.fragment;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.tripmanager.R;
import com.example.android.tripmanager.UserConnectedManager;
import com.example.android.tripmanager.activity.AddTripActivity;
import com.example.android.tripmanager.adapter.TripsAdapter;
import com.example.android.tripmanager.database.bean.TripBean;
import com.example.android.tripmanager.database.bean.UserBean;
import com.example.android.tripmanager.database.dao.TripDao;

import java.util.ArrayList;

public class TripsListFragment extends Fragment {

    private RecyclerView mTripList;
    private FloatingActionButton mBtnAddTrip;

    public enum TripSelection { ALL_TRIPS, USER_TRIPS }
    private TripSelection mTripSelection;
    public static final String EXTRA_TRIP_SELECTION = "tripSelection";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mTripSelection = (TripSelection) bundle.get(EXTRA_TRIP_SELECTION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trips_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Init widgets
        mTripList = getView().findViewById(R.id.rv_all_trips);
        mBtnAddTrip = getView().findViewById(R.id.fab_add_trip);

        mTripList.setLayoutManager(new LinearLayoutManager(getContext()));

        mBtnAddTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addTripIntent = new Intent(getActivity(), AddTripActivity.class);
                startActivity(addTripIntent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        // Update the list of trips
        loadTrips();
    }

    private void loadTrips() {
        TripDao tripDao = new TripDao(getContext());

        ArrayList<TripBean> trips = new ArrayList<>();
        // Get the correct trips and
        // set the action title correctly
        switch (mTripSelection) {
            case ALL_TRIPS:
                // Get all the trips of the app
                trips = tripDao.getAllTrips(true, true);
                getActivity().setTitle(R.string.all_trips);
                break;
            case USER_TRIPS:
                // Get only the trips that connected user
                // has created or is part of
                UserBean userConnected = UserConnectedManager.getConnectedUser();
                trips = tripDao.getUserTrips(true,
                        true,
                        userConnected.getNickname());
                getActivity().setTitle(R.string.your_trips);
                break;
        }

        TripsAdapter tripsAdapter = new TripsAdapter(getContext(), trips);
        mTripList.setAdapter(tripsAdapter);
    }
}
