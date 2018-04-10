package com.example.android.tripmanager.database.exception;

/**
 * Created by jules on 26/03/18.
 */

public class NicknameAlreadyExistsException extends Exception {

    public NicknameAlreadyExistsException() {
        super("A user with the same nickname already exists.");
    }

    public NicknameAlreadyExistsException(String existingNickname) {
        super("A user with the nickname '" + existingNickname + "' already exists.");
    }
}
