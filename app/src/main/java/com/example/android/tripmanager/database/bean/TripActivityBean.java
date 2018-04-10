package com.example.android.tripmanager.database.bean;

public class TripActivityBean extends ActivityBean {

    private TripBean mTrip;

    private int mRate;

    private int mPrice;

    private long mStartsAt;

    private long mEndsAt;

    private long mId = -1;

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

    public void setStartsAt(long startsAt) {
        mStartsAt = startsAt;
    }

    public long getEndsAt() {
        return mEndsAt;
    }

    public void setEndsAt(long endsAt) {
        mEndsAt = endsAt;
    }
}
