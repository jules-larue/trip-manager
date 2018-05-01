package com.example.android.tripmanager.database.bean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TripActivityBean extends ActivityBean {

    private TripBean mTrip;

    private int mRate;

    private int mPrice;

    private long mStartsAt;

    private long mEndsAt;

    private long mId = -1;

    public static final String DATE_FORMAT = "dd.MM.yyyy";
    public static final String TIME_FORMAT = "HH:mm";
    public static final String DATETIME_FORMAT = DATE_FORMAT + " " + TIME_FORMAT;

    public TripActivityBean() {
        super();
    }

    public TripActivityBean(String name, String location, long startsAt, long endsAt, int rate, int price) {
        super(name, location);
        mStartsAt = startsAt;
        mEndsAt = endsAt;
        mRate = rate;
        mPrice = price;
    }

    public TripActivityBean(long id, String name, String location, long startsAt, long endsAt, int rate, int price) {
        super(id, name, location);
        mStartsAt = startsAt;
        mEndsAt = endsAt;
        mRate = rate;
        mPrice = price;
    }

    public TripBean getTrip() {
        return mTrip;
    }

    public void setTrip(TripBean trip) {
        this.mTrip = trip;
    }

    public int getRate() {
        return mRate;
    }

    public void setRate(int rate) {
        this.mRate = rate;
    }

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int price) {
        mPrice = price;
    }

    public long getStartsAt() {
        return mStartsAt;
    }

    public String getStartsAtFormatted() {
        DateFormat dateFormat = new SimpleDateFormat(DATETIME_FORMAT);
        return dateFormat.format(new Date(mStartsAt));
    }

    public void setStartsAt(long startsAt) {
        mStartsAt = startsAt;
    }

    public long getEndsAt() {
        return mEndsAt;
    }

    public String getEndsAtFormatted() {
        DateFormat dateFormat = new SimpleDateFormat(DATETIME_FORMAT);
        return dateFormat.format(new Date(mEndsAt));
    }

    public void setEndsAt(long endsAt) {
        mEndsAt = endsAt;
    }

    public String getStartTimeFormatted() {
        DateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
        return timeFormat.format(mStartsAt);
    }

    public String getStartDateFormatted() {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        return dateFormat.format(mStartsAt);
    }

    public String getEndTimeFormatted() {
        DateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
        return timeFormat.format(mEndsAt);
    }

    public String getEndDateFormatted() {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        return dateFormat.format(mEndsAt);
    }
}
