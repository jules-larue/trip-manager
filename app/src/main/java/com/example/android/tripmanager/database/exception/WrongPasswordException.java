package com.example.android.tripmanager.database.exception;

public class WrongPasswordException extends Exception {

    public WrongPasswordException(String username) {
        System.out.println("The password for the user " + username + " is incorrect.");
    }
}
