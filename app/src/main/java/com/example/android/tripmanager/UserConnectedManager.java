package com.example.android.tripmanager;

import android.content.Context;

import com.example.android.tripmanager.database.bean.UserBean;
import com.example.android.tripmanager.database.dao.UserDao;
import com.example.android.tripmanager.database.exception.UserNotFoundException;
import com.example.android.tripmanager.database.exception.WrongPasswordException;

public class UserConnectedManager {

    private static UserBean sUserConnected;

    public static void connectUser(Context context, String username, String password) throws UserNotFoundException, WrongPasswordException {
        UserDao userDao = new UserDao(context);
        // Find the user we want to connect
        UserBean userToConnect = userDao.getUserByNickname(username);
        String realPassword = userToConnect.getPassword();

        // Check the password
        if (password.equals(realPassword)) {

            // Password is OK, so we connect the user.
            sUserConnected = userToConnect;
        } else {
            throw new WrongPasswordException(username);
        }
    }

    public void disconnectCurrentUser() {
        sUserConnected = null;
    }

    public static UserBean getConnectedUser() {
        return sUserConnected;
    }

}
