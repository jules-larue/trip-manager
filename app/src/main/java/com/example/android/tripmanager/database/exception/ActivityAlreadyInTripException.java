package com.example.android.tripmanager.database.exception;

public class ActivityAlreadyInTripException extends Exception {

    public ActivityAlreadyInTripException() {
        super("The activity has already been added to the trip");
    }

    public ActivityAlreadyInTripException(String activityName, String tripName) {
        super("The activity '" + activityName + "' has already been added to the trip '" + tripName + "'.");
    }
}