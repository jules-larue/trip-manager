package com.example.android.tripmanager.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.android.tripmanager.database.DbHelper;
import com.example.android.tripmanager.database.bean.ActivityBean;
import com.example.android.tripmanager.database.bean.TripActivityBean;
import com.example.android.tripmanager.database.bean.TripBean;

import java.util.ArrayList;

public class ActivityDao {
    private DbHelper mDbHelper;

    private Context mContext;

    public ActivityDao(Context context) {
        mDbHelper = DbHelper.getHelper(context);
        mContext = context;
    }

    public long insertTrip(ActivityBean activity) {
        ContentValues values = toContentValues(activity);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        return db.insert(DbHelper.TABLE_ACTIVITY, null, values);
    }

    public TripActivityBean getTripActivity(int aId) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor = db.query(DbHelper.TABLE_TRIP_ACTIVITY,
                null,
                DbHelper.TRIP_ACTIVITY_ACTIVITY_ID + " = ?",
                new String[]{String.valueOf(aId)},
                null,
                null,
                null,
                null);


        return toTripActivityBean(cursor);
    }


    public ActivityBean getActivityById(long activityId) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.query(DbHelper.TABLE_ACTIVITY,
                null,
                DbHelper.ACTIVITY_ID + " = ?",
                new String[]{ String.valueOf(activityId) },
                null,
                null,
                null);

        ActivityBean activity = null;
        if (cursor.moveToNext()) {
            activity = toBean(cursor);
        }

        return activity;
    }


    public long insertActivity(ActivityBean activityBean) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = toContentValues(activityBean);
        return db.insert(DbHelper.TABLE_ACTIVITY, null, values);
    }


    public ArrayList<ActivityBean> getActivitiesByNameLike(String query, boolean orderByName) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Build "ORDER BY" clause according to the argument `orderByName`
        String orderByClause = orderByName ? DbHelper.ACTIVITY_NAME : "";
        Cursor cursor = db.query(DbHelper.TABLE_ACTIVITY,
                null,
                DbHelper.ACTIVITY_NAME + " LIKE ?",
                new String[]{ "%" + query + "%" },
                null,
                null,
                orderByClause,
                null);

        // Get all activities found
        ArrayList<ActivityBean> results = new ArrayList<>();
        while (cursor.moveToNext()) {
            results.add(toBean(cursor));
        }

        // Free resources
        cursor.close();

        return results;
    }


    public ArrayList<ActivityBean> getAllActivities(boolean orderByName) {
        // Empty query is equivalent to any name
        return getActivitiesByNameLike("", orderByName);
    }

    public ArrayList<ActivityBean> getAllActivitiesWithAverage(boolean orderByName) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        ArrayList<ActivityBean> activities = getAllActivities(true);
        for (ActivityBean activity : activities) {

            // Compute the average price and rate for each activity
            long activityId = activity.getId();
            Cursor cursorAverage = db.rawQuery("SELECT AVG(" + DbHelper.TRIP_ACTIVITY_PRICE + "), AVG(" + DbHelper.TRIP_ACTIVITY_RATE
                    + ") FROM " + DbHelper.TABLE_TRIP_ACTIVITY
                    + " WHERE " + DbHelper.TRIP_ACTIVITY_ACTIVITY_ID + " = ?",
                    new String[]{ String.valueOf(activityId) });

            if (cursorAverage.moveToFirst()) {
                float avgPrice = cursorAverage.getFloat(0);
                float avgRate = cursorAverage.getFloat(1);
                activity.setAvgPrice(avgPrice);
                activity.setAvgRate(avgRate);
            }
        }

        return activities;
    }

    public int updateTripActivity(long aId, TripActivityBean newTripActivityInfo){
        ContentValues updatedValues = toContentValues(newTripActivityInfo);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        return db.update(DbHelper.TABLE_TRIP_ACTIVITY,
                updatedValues,
                DbHelper.TRIP_ACTIVITY_ACTIVITY_ID + "= ?",
                new String[]{String.valueOf(aId)});
    }

    public ContentValues toContentValues(ActivityBean activity) {
        ContentValues values = new ContentValues();

        values.put(DbHelper.ACTIVITY_NAME, activity.getName());
        values.put(DbHelper.ACTIVITY_LOCATION, activity.getLocation());

        return values;
    }


    public TripActivityBean toTripActivityBean(Cursor cursor) {
        long activityId = cursor.getLong(cursor.getColumnIndex(DbHelper.TRIP_ACTIVITY_ACTIVITY_ID));

        // Get related activity
        ActivityBean activityBean = getActivityById(activityId);

        int rate = cursor.getInt(cursor.getColumnIndex(DbHelper.TRIP_ACTIVITY_RATE));
        int price = cursor.getInt(cursor.getColumnIndex(DbHelper.TRIP_ACTIVITY_PRICE));
        long startsAt = cursor.getLong(cursor.getColumnIndex(DbHelper.TRIP_ACTIVITY_STARTS_AT));
        long endsAt = cursor.getLong(cursor.getColumnIndex(DbHelper.TRIP_ACTIVITY_ENDS_AT));
        long tripId = cursor.getLong(cursor.getColumnIndex(DbHelper.TRIP_ACTIVITY_TRIP_ID));

        TripActivityBean tripActivity = new TripActivityBean(activityBean.getName(),
                activityBean.getLocation(),
                startsAt,
                endsAt,
                rate,
                price);

        TripBean trip = new TripBean();
        TripDao tripDao = new TripDao(mContext);
        trip = tripDao.selectTrip((int)tripId);
        tripActivity.setTrip(trip);
        return tripActivity;
    }

    /**
     * Computes the average price of an activity
     * from the database.
     * @param activityId the id of the activity we want to have
     *                   the average price of
     * @return the average price of the activity or -1 if no price is found for the activity
     */
    public float getAverageActivityPrice(long activityId) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + DbHelper.TRIP_ACTIVITY_PRICE
                + ") FROM " + DbHelper.TABLE_TRIP_ACTIVITY
                + " WHERE " + DbHelper.TRIP_ACTIVITY_ACTIVITY_ID + " = ?",
                new String[]{ String.valueOf(activityId) },
                null);

        if (cursor.moveToNext()) {
            return cursor.getFloat(0);
        } else {
            return -1;
        }
    }


    /**
     * Computes the average rate of an activity
     * from the database.
     * @param activityId the id of the activity we want to have
     *                   the average rate of
     * @return the average price of the activity or -1 if no price is found for the activity
     */
    public float getAverageActivityRate(long activityId) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT AVG(" + DbHelper.TRIP_ACTIVITY_RATE
                        + ") FROM " + DbHelper.TABLE_TRIP_ACTIVITY
                        + " WHERE " + DbHelper.TRIP_ACTIVITY_ACTIVITY_ID + " = ?",
                new String[]{ String.valueOf(activityId) },
                null);

        if (cursor.moveToNext()) {
            return cursor.getFloat(0);
        } else {
            return -1;
        }
    }


    public ActivityBean toBean(Cursor cursor) {
        // Get attributes from cursor
        long id = cursor.getLong(cursor.getColumnIndex(DbHelper.ACTIVITY_ID));
        String name = cursor.getString(cursor.getColumnIndex(DbHelper.ACTIVITY_NAME));
        String location = cursor.getString(cursor.getColumnIndex(DbHelper.ACTIVITY_LOCATION));

        // Returns the activity
        return new ActivityBean(id, name, location);
    }

    private ContentValues toContentValues(TripActivityBean tripActivity){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.TRIP_ACTIVITY_TRIP_ID,tripActivity.getTrip().getId());
        contentValues.put(DbHelper.TRIP_ACTIVITY_ACTIVITY_ID,tripActivity.getId());
        contentValues.put(DbHelper.TRIP_ACTIVITY_PRICE,tripActivity.getPrice());
        contentValues.put(DbHelper.TRIP_ACTIVITY_RATE,tripActivity.getRate());
        return contentValues;
    }
}
