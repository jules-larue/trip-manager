package com.example.android.tripmanager.database.exception;

public class GuestAlreadyInTripException extends Exception {

    public GuestAlreadyInTripException() {
        super("The guest has already been added to the trip");
    }

    public GuestAlreadyInTripException(String guestNickname, String tripName) {
        super("The guest '" + guestNickname + "' has already been added to the trip '" + tripName + "'.");
    }
}
