package com.example.android.tripmanager.database.bean;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by jules on 26/03/18.
 */

public class TripBean {
    
    protected long mId = -1;

    protected String mName;

    protected UserBean mCreator;

    protected String mStartsOn;

    protected String mEndsOn;

    protected ArrayList<UserBean> mGuests;

    protected ArrayList<TripActivityBean> mActivities;

    public static final String DATE_FORMAT = "dd.MM.yyyy";

    public TripBean() {

    }

    public TripBean(String name, UserBean creator, String startsOn, String endsOn) {
        mName = name;
        mCreator = creator;
        mStartsOn = startsOn;
        mEndsOn = endsOn;
    }

    public TripBean(long id, String name, UserBean creator, String startsOn,
                    String endsOn) {
        this(name, creator, startsOn, endsOn);
        mId = id;
    }

    public TripBean(String name, UserBean creator, String startsOn, String endsOn,
                    ArrayList<UserBean> guests) {
        this(name, creator, startsOn, endsOn);
        mEndsOn = endsOn;
        mGuests = guests;
    }

    public TripBean(long id, String name, UserBean creator, String startOn,
                    String endsOn, ArrayList<UserBean> guests) {
        this(name, creator, startOn, endsOn, guests);
        mId = id;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public UserBean getCreator() {
        return mCreator;
    }

    public void setCreator(UserBean creator) {
        mCreator = creator;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getStartsOn() {
        return mStartsOn;
    }

    public void setStartsOn(String startsOn) {
        mStartsOn = startsOn;
    }

    public String getEndsOn() {
        return mEndsOn;
    }

    public void setEndsOn(String endsOn) {
        mEndsOn = endsOn;
    }

    public ArrayList<UserBean> getGuests() {
        return mGuests;
    }

    public void setGuests(ArrayList<UserBean> guests) {
        mGuests = guests;
    }

    public ArrayList<TripActivityBean> getActivities() {
        return mActivities;
    }

    public void setActivities(ArrayList<TripActivityBean> activities) {
        mActivities = activities;
    }

    public void addGuest(UserBean newGuest) {
        mGuests.add(newGuest);
    }

    public void deleteGuest(UserBean guestToDelete) {
        mGuests.remove(guestToDelete);
    }
}
