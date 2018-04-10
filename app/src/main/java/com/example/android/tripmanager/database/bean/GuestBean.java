package com.example.android.tripmanager.database.bean;

/**
 * Created by jules on 26/03/18.
 */

public class GuestBean extends UserBean {

    private TripBean mTrip;

    public GuestBean(String nickname, String password, String firstName,
                     String lastName, long birthDate, TripBean trip) {
        super(nickname, password, firstName, lastName, birthDate);
        mTrip = trip;
    }

    public TripBean getTrip() {
        return mTrip;
    }

    public void setTrip(TripBean trip) {
        mTrip = trip;
    }
}
