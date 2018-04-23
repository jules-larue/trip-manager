package com.example.android.tripmanager.activities;

import android.content.Context;

import com.example.android.tripmanager.database.bean.ActivityBean;
import com.example.android.tripmanager.database.bean.UserBean;
import com.example.android.tripmanager.database.dao.ActivityDao;
import com.example.android.tripmanager.database.dao.UserDao;
import com.example.android.tripmanager.database.exception.UserNotFoundException;
import com.example.android.tripmanager.database.exception.WrongPasswordException;

import java.util.ArrayList;

/**
 * Created by JEREMY on 2018-04-19.
 */

public class ActivityConnectedManager {
    private static ArrayList<ActivityBean> mActivityConnected;

    public static void connectActivity(Context context, String name, String location) {
        ActivityDao activityDao = new ActivityDao(context);
        // Find the user we want to connect
        ArrayList<ActivityBean> activityToConnect = activityDao.getAllActivities(true);
        mActivityConnected = activityToConnect;


    }


    public void disconnectCurrentUser() {
        mActivityConnected = null;
    }

}
