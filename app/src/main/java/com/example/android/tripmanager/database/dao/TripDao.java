package com.example.android.tripmanager.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.android.tripmanager.database.exception.ActivityAlreadyInTripException;
import com.example.android.tripmanager.database.DbHelper;
import com.example.android.tripmanager.database.exception.GuestAlreadyInTripException;
import com.example.android.tripmanager.database.bean.GuestBean;
import com.example.android.tripmanager.database.bean.TripActivityBean;
import com.example.android.tripmanager.database.bean.TripBean;
import com.example.android.tripmanager.database.bean.UserBean;

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

    public void addGuest(GuestBean guest) throws GuestAlreadyInTripException {
        // Check if guest with same nickname exists
        if (guestExists(guest)) {
            throw new GuestAlreadyInTripException(guest.getNickname(), guest.getTrip().getName());
        }

        ContentValues values = new ContentValues();
        values.put(DbHelper.GUEST_NICKNAME, guest.getNickname());
        values.put(DbHelper.GUEST_TRIP, guest.getTrip().getName());

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.insert(DbHelper.TABLE_GUEST, null, values);
    }

    public int deleteGuest(GuestBean guest) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String tripId = String.valueOf(guest.getTrip().getId());
        return db.delete(DbHelper.TABLE_GUEST,
                DbHelper.GUEST_NICKNAME + " = ? AND " + DbHelper.GUEST_TRIP + " = ?",
                new String[]{ guest.getNickname(), tripId });
    }

    private boolean guestExists(GuestBean guest) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Search for the guest in the trip
        Cursor cursor = db.query(DbHelper.TABLE_GUEST,
                null,
                DbHelper.GUEST_NICKNAME + " = ? AND " + DbHelper.GUEST_TRIP + " = ?",
                new String[]{ guest.getNickname(), String.valueOf(guest.getTrip().getId()) },
                null,
                null,
                null,
                null);

        // We get one result if one guest with nickname is found
        return cursor.getCount() > 0;
    }

    public void addActivity(TripActivityBean activity) throws ActivityAlreadyInTripException {
        // Check if guest with same nickname exists
        if (isActivityInTrip(activity)) {
            throw new ActivityAlreadyInTripException(activity.getName(), activity.getTrip().getName());
        }

        ContentValues values = new ContentValues();
        values.put(DbHelper.TRIP_ACTIVITY_ACTIVITY_ID, activity.getId());
        values.put(DbHelper.TRIP_ACTIVITY_TRIP_ID, activity.getTrip().getId());
        values.put(DbHelper.TRIP_ACTIVITY_STARTS_AT, activity.getStartsAt());
        values.put(DbHelper.TRIP_ACTIVITY_ENDS_AT, activity.getEndsAt());
        values.put(DbHelper.TRIP_ACTIVITY_PRICE, activity.getPrice());
        values.putNull(DbHelper.TRIP_ACTIVITY_RATE);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.insert(DbHelper.TABLE_TRIP_ACTIVITY, null, values);
    }

    public int deleteActivity(TripActivityBean activity) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String tripId = String.valueOf(activity.getTrip().getId());
        String activityId = String.valueOf(activity.getId());
        return db.delete(DbHelper.TABLE_TRIP_ACTIVITY,
                DbHelper.TRIP_ACTIVITY_ACTIVITY_ID + " = ? AND " + DbHelper.TRIP_ACTIVITY_TRIP_ID + " = ?",
                new String[]{ activityId, tripId });
    }

    private boolean isActivityInTrip(TripActivityBean activity) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Search for the activity in the trip
        Cursor cursor = db.query(DbHelper.TABLE_TRIP_ACTIVITY,
                null,
                DbHelper.TRIP_ACTIVITY_ACTIVITY_ID + " = ? AND " + DbHelper.TRIP_ACTIVITY_TRIP_ID + " = ?",
                new String[]{ String.valueOf(activity.getId()), String.valueOf(activity.getTrip().getId()) },
                null,
                null,
                null,
                null);

        // We get one result if one activity with nickname is found
        return cursor.getCount() > 0;
    }


    private TripBean toBean(Cursor cursor) {
        long tripId = cursor.getLong(cursor.getColumnIndex(DbHelper.TRIP_ID));
        String creatorNickname = cursor.getString(cursor.getColumnIndex(DbHelper.TRIP_CREATOR));
        String tripName = cursor.getString(cursor.getColumnIndex(DbHelper.TRIP_NAME));
        long tripStartsAt = cursor.getLong(cursor.getColumnIndex(DbHelper.TRIP_STARTS_ON));
        long tripEndsAt = cursor.getLong(cursor.getColumnIndex(DbHelper.TRIP_ENDS_ON));

        // Get the creator from database
        UserDao userDao = new UserDao(mContext);
        UserBean creator = userDao.getUserByNickname(creatorNickname);

        return new TripBean(tripId, tripName, creator, tripStartsAt, tripEndsAt);
    }

    private ContentValues toContentValues(TripBean trip) {
        // Get creator nickname
        String creatorNickname = trip.getCreator().getNickname();

        ContentValues values = new ContentValues();
        values.put(DbHelper.TRIP_ID, trip.getId());
        values.put(DbHelper.TRIP_CREATOR, creatorNickname);
        values.put(DbHelper.TRIP_NAME, trip.getName());
        values.put(DbHelper.TRIP_STARTS_ON, trip.getStartsAt());
        values.put(DbHelper.TRIP_ENDS_ON, trip.getEndsAt());
        return values;
    }
}
