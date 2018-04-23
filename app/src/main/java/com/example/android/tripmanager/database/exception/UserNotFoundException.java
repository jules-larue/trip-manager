package com.example.android.tripmanager.database.exception;

public class UserNotFoundException extends Exception {

    public UserNotFoundException(String username) {
        System.out.println("Sorry, the user with username " + username
                + " was not found in the database.");
    }
}
