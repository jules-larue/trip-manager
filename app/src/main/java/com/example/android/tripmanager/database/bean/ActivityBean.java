package com.example.android.tripmanager.database.bean;

/**
 * Created by jules on 26/03/18.
 */

public class ActivityBean {

    private long mId;

    private String mName;

    private String mLocation;

    public ActivityBean() {

    }

    public ActivityBean(String name, String location) {
        mName = name;
        mLocation = location;
    }

    public ActivityBean(long id, String name, String location) {
        this(name, location);
        mId = id;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }
}
