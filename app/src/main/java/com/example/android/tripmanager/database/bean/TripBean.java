package com.example.android.tripmanager.database.bean;

import java.util.HashSet;

/**
 * Created by jules on 26/03/18.
 */

public class TripBean {
    
    protected long mId = -1;

    protected String mName;

    protected UserBean mCreator;

    protected long mStartsAt;

    protected long mEndsAt;

    protected HashSet<UserBean> mGuests;

    public TripBean() {

    }

    public TripBean(String name, UserBean creator, long startsAt, long endsAt) {
        mName = name;
        mCreator = creator;
        mStartsAt = startsAt;
        mEndsAt = endsAt;
    }

    public TripBean(long id, String name, UserBean creator, long startsAt, long endsAt) {
        this(name, creator, startsAt, endsAt);
        mId = id;
    }

    public TripBean(String name, UserBean creator, long startsAt, long endsAt, HashSet<UserBean> guests) {
        this(name, creator, startsAt, endsAt);
        mEndsAt = endsAt;
        mGuests = guests;
    }

    public TripBean(long id, String name, UserBean creator, long startAt, long endsAt, HashSet<UserBean> guests) {
        this(name, creator, startAt, endsAt, guests);
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

    public long getStartsAt() {
        return mStartsAt;
    }

    public void setStartsAt(long startsAt) {
        mStartsAt = startsAt;
    }

    public long getEndsAt() {
        return mEndsAt;
    }

    public void setEndsAt(long endsAt) {
        mEndsAt = endsAt;
    }

    public HashSet<UserBean> getGuests() {
        return mGuests;
    }

    public void setGuests(HashSet<UserBean> guests) {
        mGuests = guests;
    }
}
