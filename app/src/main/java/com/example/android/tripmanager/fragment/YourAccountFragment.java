package com.example.android.tripmanager.fragment;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.tripmanager.R;
import com.example.android.tripmanager.UserConnectedManager;
import com.example.android.tripmanager.database.bean.UserBean;
import com.example.android.tripmanager.database.dao.UserDao;

import java.text.DateFormat;
import java.util.Locale;

public class YourAccountFragment extends Fragment {

    private TextView mTvNickname;
    private TextView mTvNames;
    private TextView mTvBirthDate;
    private TextView mTvNbTripsOwned;
    private TextView mTvNbTripsJoined;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_your_account, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mTvNickname = getView().findViewById(R.id.tv_nickname);
        mTvNames = getView().findViewById(R.id.tv_names);
        mTvBirthDate = getView().findViewById(R.id.tv_birth_date);
        mTvNbTripsOwned = getView().findViewById(R.id.tv_nb_trips_owned);
        mTvNbTripsJoined = getView().findViewById(R.id.tv_nb_trips_joined);

        // Set action bar title
        getActivity().setTitle(R.string.your_account);

        // Get the current user and set data to display
        UserBean currentUser = UserConnectedManager.getConnectedUser();

        mTvNickname.setText(currentUser.getNickname());
        String firstNameAndLastName = String.format(getString(R.string.firstname_lastname),
                currentUser.getFirstName(),
                currentUser.getLastName());
        mTvNames.setText(firstNameAndLastName);

        DateFormat birthDateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.ENGLISH);
        mTvBirthDate.setText(birthDateFormat.format(currentUser.getBirthDate()));

        UserDao userDao = new UserDao(getContext());
        int nbTripsOwned = userDao.getNbTripsOwned(currentUser.getNickname());
        int nbTripsJoined = userDao.getNbTripsJoined(currentUser.getNickname());

        String textNbTripsOwned;
        String textNbTripsJoined;

        // Handle plural words
        if (nbTripsOwned > 1) {
            textNbTripsOwned = getString(R.string.n_trips_owned);
        } else {
            textNbTripsOwned = getString(R.string.n_trip_owned);
        }

        if (nbTripsJoined > 1) {
            textNbTripsJoined = getString(R.string.n_trips_joined);
        } else {
            textNbTripsJoined = getString(R.string.n_trip_joined);
        }

        mTvNbTripsOwned.setText(String.format(textNbTripsOwned, nbTripsOwned));
        mTvNbTripsJoined.setText(String.format(textNbTripsJoined, nbTripsJoined));
    }
}
