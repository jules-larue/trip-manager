package com.example.android.tripmanager.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.android.tripmanager.database.exception.ActivityAlreadyInTripException;
import com.example.android.tripmanager.database.DbHelper;
import com.example.android.tripmanager.database.exception.GuestAlreadyInTripException;
import com.example.android.tripmanager.database.bean.GuestBean;
import com.example.android.tripmanager.database.bean.TripActivityBean;
import com.example.android.tripmanager.database.bean.TripBean;
import com.example.android.tripmanager.database.bean.UserBean;
import com.example.android.tripmanager.database.exception.UserNotFoundException;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TripDao {

    private DbHelper mDbHelper;

    private Context mContext;

    public TripDao(Context context) {
        mDbHelper = DbHelper.getHelper(context);
        mContext = context;
    }

    public long insertTrip(TripBean trip) {
        ContentValues values = toContentValues(trip);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        return db.insert(DbHelper.TABLE_TRIP, null, values);
    }

    public TripBean selectTrip(int mId) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.query(DbHelper.TABLE_TRIP
                ,null
                ,DbHelper.TRIP_ID + " = ?"
                ,new String[]{String.valueOf(mId)}
                ,null
                ,null
                ,null
        );

        TripBean result = null;
        if (cursor.moveToFirst()) {
            result = toBean(cursor);
        }
        cursor.close();
        return result;
    }


    public int updateTrip(long tripId, TripBean updatedTrip) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues updatedValues = toContentValues(updatedTrip);
        return db.update(DbHelper.CREATE_TABLE_TRIP,
                updatedValues,
                DbHelper.TRIP_ID + " = ?",
                new String[]{ String.valueOf(tripId) });
    }

    public void addGuest(String userNickname, long tripId) throws GuestAlreadyInTripException {
        // Check if guest with same nickname exists
        if (guestExists(userNickname, tripId)) {
            throw new GuestAlreadyInTripException();
        }

        ContentValues values = new ContentValues();
        values.put(DbHelper.GUEST_NICKNAME, userNickname);
        values.put(DbHelper.GUEST_TRIP, tripId);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.insert(DbHelper.TABLE_GUEST, null, values);
    }

    public int deleteGuest(String userNickname, long tripId) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        return db.delete(DbHelper.TABLE_GUEST,
                DbHelper.GUEST_NICKNAME + " = ? AND " + DbHelper.GUEST_TRIP + " = ?",
                new String[]{ userNickname, String.valueOf(tripId) });
    }

    public boolean guestExists(String userNickname, long tripId) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Search for the guest in the trip
        Cursor cursor = db.query(DbHelper.TABLE_GUEST,
                null,
                DbHelper.GUEST_NICKNAME + " = ? AND " + DbHelper.GUEST_TRIP + " = ?",
                new String[]{ userNickname, String.valueOf(tripId) },
                null,
                null,
                null,
                null);

        // We get one result if one guest with nickname is found
        return cursor.getCount() > 0;
    }


    public TripActivityBean getTripActivityById(long tripActivityId) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor = db.query(DbHelper.TABLE_TRIP_ACTIVITY + " natural join " + DbHelper.TABLE_ACTIVITY,
                null,
                DbHelper.TRIP_ACTIVITY_ACTIVITY_ID + " = ?",
                new String[]{ String.valueOf(tripActivityId) },
                null,
                null,
                null,
                null);

        TripActivityBean tripActivity = null;
        if (cursor.moveToNext()) {
            tripActivity = toTripActivityBean(cursor);
        }

        return tripActivity;
    }

    public void addTripActivity(TripActivityBean activity, long tripId) throws ActivityAlreadyInTripException {
        // Check if guest with same nickname exists
        if (isActivityInTrip(activity.getId(), tripId)) {
            throw new ActivityAlreadyInTripException();
        }

        ContentValues values = new ContentValues();
        values.put(DbHelper.TRIP_ACTIVITY_ACTIVITY_ID, activity.getId());
        values.put(DbHelper.TRIP_ACTIVITY_TRIP_ID, tripId);
        values.put(DbHelper.TRIP_ACTIVITY_STARTS_AT, activity.getStartsAt());
        values.put(DbHelper.TRIP_ACTIVITY_ENDS_AT, activity.getEndsAt());
        values.put(DbHelper.TRIP_ACTIVITY_PRICE, activity.getPrice());
        values.putNull(DbHelper.TRIP_ACTIVITY_RATE);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.insert(DbHelper.TABLE_TRIP_ACTIVITY, null, values);
    }


    public int deleteTripActivity(TripActivityBean activity) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String tripId = String.valueOf(activity.getTrip().getId());
        String activityId = String.valueOf(activity.getId());
        return db.delete(DbHelper.TABLE_TRIP_ACTIVITY,
                DbHelper.TRIP_ACTIVITY_ACTIVITY_ID + " = ? AND " + DbHelper.TRIP_ACTIVITY_TRIP_ID + " = ?",
                new String[]{ activityId, tripId });
    }

    private boolean isActivityInTrip(long tripActivityId, long tripId) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Search for the activity in the trip
        Cursor cursor = db.query(DbHelper.TABLE_TRIP_ACTIVITY,
                null,
                DbHelper.TRIP_ACTIVITY_ACTIVITY_ID + " = ? AND " + DbHelper.TRIP_ACTIVITY_TRIP_ID + " = ?",
                new String[]{ String.valueOf(tripActivityId), String.valueOf(tripId) },
                null,
                null,
                null,
                null);

        // We get one result if one activity with nickname is found
        return cursor.getCount() > 0;
    }

    public ArrayList<TripActivityBean> getActivitiesByTripId(long tripId){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        /*
            In this query, we join the ACTIVITY and TRIP_ACTIVITY tables to get
            the full information of each trip activity.
            We make sure to only select activities belonging that are part
            of the trip with the id `tripId`.
         */
        String query = "SELECT *" +
                " FROM " + DbHelper.TABLE_ACTIVITY + " natural join " + DbHelper.TABLE_TRIP_ACTIVITY +
                " WHERE " + DbHelper.TRIP_ACTIVITY_TRIP_ID + " = ?" +
                " AND " + DbHelper.TRIP_ACTIVITY_ACTIVITY_ID + " = " + DbHelper.ACTIVITY_ID +
                " ORDER BY " + DbHelper.TRIP_ACTIVITY_STARTS_AT;
        Cursor cursor = db.rawQuery(query, new String[]{ String.valueOf(tripId) });

        ArrayList<TripActivityBean> results = new ArrayList<>();
        while (cursor.moveToNext()) {
            results.add(toTripActivityBean(cursor));
        }

        cursor.close();
        return results;
    }

    public ArrayList<UserBean> getGuestsByTripId(long tripId) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor = db.query(DbHelper.TABLE_GUEST + " natural join " + DbHelper.TABLE_USER,
                null,
                DbHelper.GUEST_TRIP + " = ? AND " + DbHelper.GUEST_NICKNAME + " = " + DbHelper.USER_NICKNAME,
                new String[]{String.valueOf(tripId)},
                null,
                null,
                DbHelper.GUEST_NICKNAME,
                null);

        ArrayList<UserBean> results = new ArrayList<>();
        while (cursor.moveToNext()) {
            UserBean guest = UserDao.toBean(cursor);
            results.add(guest);
        }

        cursor.close();
        return results;
    }

    /**
     * Get all the trips stored in the database.
     * Optional parameters allow to specify whether
     * we should include additional information.
     * @param withActivities true to include the trip activities in the trip results
     * @param withGuests true to include the trip guests in the trip results
     * @return
     */
    public ArrayList<TripBean> getAllTrips(boolean withGuests, boolean withActivities) {
        ArrayList<TripBean> allTrips = new ArrayList<>();

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // First select all the trip info
        Cursor cursor = db.query(DbHelper.TABLE_TRIP,
                null,
                null,
                null,
                null,
                null,
                null,
                null);

        while (cursor.moveToNext()) {
            // Build the trip
            TripBean trip = toBean(cursor);

            if (withActivities) {
                // Get all activities fot the current trip
                ArrayList<TripActivityBean> tripActivities = getActivitiesByTripId(trip.getId());
                trip.setActivities(tripActivities);
            }

            if (withGuests) {
                // Get all the guests of the trip
                ArrayList<UserBean> guests = getGuestsByTripId(trip.getId());
                trip.setGuests(guests);
            }
            allTrips.add(trip);
        }
        cursor.close();

        return allTrips;
    }

    /**
     * Get all the trips that a user has created or
     * is a guest of.
     * Optional parameters allow to specify whether
     * we should include additional information.
     * @param withActivities true to include the trip activities in the trip results
     * @param withGuests true to include the trip guests in the trip results
     * @return
     */
    public ArrayList<TripBean> getUserTrips(boolean withGuests, boolean withActivities,
                                            String userNickname) {
        ArrayList<TripBean> allTrips = new ArrayList<>();

        SQLiteDatabase db = mDbHelper.getReadableDatabase();


        Cursor cursorTripsOwned = db.query(DbHelper.TABLE_TRIP,
                null,
                DbHelper.TRIP_CREATOR + " = ?",
                new String[]{ userNickname },
                null,
                null,
                null);

        Cursor cursorTripsJoined = db.query(DbHelper.TABLE_TRIP + " natural join " + DbHelper.TABLE_GUEST,
                null,
                DbHelper.GUEST_NICKNAME + " = ? AND " + DbHelper.TRIP_ID + " = " + DbHelper.GUEST_TRIP,
                new String[]{ userNickname },
                null,
                null,
                null);

        allTrips.addAll(buildTrips(cursorTripsOwned, withActivities, withGuests));
        allTrips.addAll(buildTrips(cursorTripsJoined, withActivities, withGuests));
        cursorTripsOwned.close();
        cursorTripsJoined.close();

        return allTrips;
    }

    private ArrayList<TripBean> buildTrips(Cursor cursor, boolean withActivities,
                                           boolean withGuests) {
        ArrayList<TripBean> trips = new ArrayList<>();
        while (cursor.moveToNext()) {
            // Build the trip
            TripBean trip = toBean(cursor);

            if (withActivities) {
                // Get all activities fot the current trip
                ArrayList<TripActivityBean> tripActivities = getActivitiesByTripId(trip.getId());
                trip.setActivities(tripActivities);
            }

            if (withGuests) {
                // Get all the guests of the trip
                ArrayList<UserBean> guests = getGuestsByTripId(trip.getId());
                trip.setGuests(guests);
            }
            trips.add(trip);
        }

        return trips;
    }


    private TripBean toBean(Cursor cursor) {
        long tripId = cursor.getLong(cursor.getColumnIndex(DbHelper.TRIP_ID));
        String creatorNickname = cursor.getString(cursor.getColumnIndex(DbHelper.TRIP_CREATOR));
        String tripName = cursor.getString(cursor.getColumnIndex(DbHelper.TRIP_NAME));
        String tripStartsAt = cursor.getString(cursor.getColumnIndex(DbHelper.TRIP_STARTS_ON));
        String tripEndsAt = cursor.getString(cursor.getColumnIndex(DbHelper.TRIP_ENDS_ON));

        // Get the creator from database
        UserDao userDao = new UserDao(mContext);
        UserBean creator = null;
        try {
            creator = userDao.getUserByNickname(creatorNickname);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }

        return new TripBean(tripId, tripName, creator, tripStartsAt, tripEndsAt);
    }

    private TripActivityBean toTripActivityBean(Cursor cursor){

        long id = cursor.getLong(cursor.getColumnIndex(DbHelper.TRIP_ACTIVITY_ACTIVITY_ID));
        int rate = cursor.getInt(cursor.getColumnIndex(DbHelper.TRIP_ACTIVITY_RATE));
        int price = cursor.getInt(cursor.getColumnIndex(DbHelper.TRIP_ACTIVITY_PRICE));
        long startsAt = cursor.getLong(cursor.getColumnIndex(DbHelper.TRIP_ACTIVITY_STARTS_AT));
        long endsAt = cursor.getLong(cursor.getColumnIndex(DbHelper.TRIP_ACTIVITY_ENDS_AT));
        long tripId = cursor.getLong(cursor.getColumnIndex(DbHelper.TRIP_ACTIVITY_TRIP_ID));
        String name = cursor.getString(cursor.getColumnIndex(DbHelper.ACTIVITY_NAME));
        String location = cursor.getString(cursor.getColumnIndex(DbHelper.ACTIVITY_LOCATION));

        TripActivityBean tripActivity = new TripActivityBean(id,  name,  location,  startsAt,  endsAt,  rate,  price);
        TripBean trip = new TripBean();
        TripDao tripDao = new TripDao(mContext);
        trip = tripDao.selectTrip((int)tripId);
        tripActivity.setTrip(trip);
        return tripActivity;
    }


    private ContentValues toContentValues(TripBean trip) {
        // Get creator nickname
        String creatorNickname = trip.getCreator().getNickname();

        ContentValues values = new ContentValues();
        values.put(DbHelper.TRIP_CREATOR, creatorNickname);
        values.put(DbHelper.TRIP_NAME, trip.getName());
        values.put(DbHelper.TRIP_STARTS_ON, trip.getStartsOn());
        values.put(DbHelper.TRIP_ENDS_ON, trip.getEndsOn());
        return values;
    }
}
