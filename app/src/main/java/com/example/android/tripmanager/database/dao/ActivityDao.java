package com.example.android.tripmanager.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.android.tripmanager.database.DbHelper;
import com.example.android.tripmanager.database.bean.ActivityBean;

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
    public long updateTrip(ActivityBean activity) {
        ContentValues values = toContentValues(activity);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        return db.update(DbHelper.TABLE_ACTIVITY, null, values);
    }
    public long deleteTrip(ActivityBean activity) {
        ContentValues values = toContentValues(activity);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        return db.delete(DbHelper.TABLE_ACTIVITY, null, values);
    }


    public ArrayList<ActivityBean> getActivitiesByNameLike(String query, boolean orderByName) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Build "ORDER BY" clause according to the argument `orderByName`
        String orderByClause = orderByName ? DbHelper.ACTIVITY_NAME + " ACS" : null;
        Cursor cursor = db.query(DbHelper.TABLE_ACTIVITY,
                null,
                DbHelper.ACTIVITY_NAME + " LIKE %?%",
                new String[]{ query },
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


    public ContentValues toContentValues(ActivityBean activity) {
        ContentValues values = new ContentValues();

        values.put(DbHelper.ACTIVITY_NAME, activity.getName());
        values.put(DbHelper.ACTIVITY_LOCATION, activity.getLocation());

        return values;
    }


    public ActivityBean toBean(Cursor cursor) {
        // Get attributes from cursor
        long id = cursor.getLong(cursor.getColumnIndex(DbHelper.ACTIVITY_ID));
        String name = cursor.getString(cursor.getColumnIndex(DbHelper.ACTIVITY_NAME));
        String location = cursor.getString(cursor.getColumnIndex(DbHelper.ACTIVITY_LOCATION));

        // Returns the activity
        return new ActivityBean(id, name, location);
    }
}
