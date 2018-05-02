package com.example.android.tripmanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jules on 25/03/18.
 */

public class DbHelper extends SQLiteOpenHelper {

    /**
     * Name of the database
     */
    private static final String NAME = "TripManager";

    /**
     * Current version of the database
     */
    private static final int VERSION = 1;

    private static DbHelper mInstance;

    // Table names
    public static final String TABLE_USER = "user";
    public static final String TABLE_GUEST = "guest";
    public static final String TABLE_TRIP = "trip";
    public static final String TABLE_ACTIVITY = "activity";
    public static final String TABLE_TRIP_ACTIVITY = "trip_activity";
    public static final String TABLE_POST = "post";
    public static final String TABLE_POST_VOTE = "post_vote";

    // User columns
    public static final String USER_NICKNAME = "nickname";
    public static final String USER_PASSWORD = "password";
    public static final String USER_FIRST_NAME = "first_name";
    public static final String USER_LAST_NAME = "last_name";
    public static final String USER_BIRTH_DATE = "birth_date";

    // Guest columns
    public static final String GUEST_NICKNAME = "guest_nickname";
    public static final String GUEST_TRIP = "trip_id";

    // Trip columns
    public static final String TRIP_ID = "id";
    public static final String TRIP_CREATOR = "creator";
    public static final String TRIP_NAME = "name";
    public static final String TRIP_STARTS_ON = "starts_on";
    public static final String TRIP_ENDS_ON = "ends_on";

    // Activity columns
    public static final String ACTIVITY_ID = "id";
    public static final String ACTIVITY_NAME = "name";
    public static final String ACTIVITY_LOCATION = "location";

    // Trip activity columns
    public static final String TRIP_ACTIVITY_ACTIVITY_ID = "activity_id";
    public static final String TRIP_ACTIVITY_TRIP_ID = "trip_id";
    public static final String TRIP_ACTIVITY_RATE = "rate";
    public static final String TRIP_ACTIVITY_PRICE = "price";
    public static final String TRIP_ACTIVITY_STARTS_AT = "starts_at";
    public static final String TRIP_ACTIVITY_ENDS_AT = "ends_at";

    // Post columns
    public static final String POST_ID = "id";
    public static final String POST_CREATOR = "creator";
    public static final String POST_DATE = "publication_date";
    public static final String POST_CONTENT = "content";

    // Post votes columns
    public static final String POST_VOTE_VOTER = "voter";
    public static final String POST_VOTE_POST_ID = "post_id";
    public static final String POST_VOTE_VALUE = "vote_value";

    /**
     * SQL query to create "user" table
     */
    public static final String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS " + TABLE_USER + "("
            + USER_NICKNAME + " TEXT PRIMARY KEY,"
            + USER_PASSWORD + " TEXT NOT NULL,"
            + USER_FIRST_NAME + " TEXT NOT NULL,"
            + USER_LAST_NAME + " TEXT NOT NULL,"
            + USER_BIRTH_DATE + " TEXT NOT NULL)";

    /**
     * SQL query to create "guest" table
     */
    public static final String CREATE_TABLE_GUEST = "CREATE TABLE IF NOT EXISTS " + TABLE_GUEST + "("
            + GUEST_NICKNAME + " TEXT NOT NULL,"
            + GUEST_TRIP + " INTEGER NOT NULL,"
            + "PRIMARY KEY (" + GUEST_NICKNAME + ", " + GUEST_TRIP + "),"
            + "FOREIGN KEY (" + GUEST_NICKNAME + ") REFERENCES " + TABLE_USER + "(" + USER_NICKNAME + "),"
            + "FOREIGN KEY (" + GUEST_TRIP + ") REFERENCES " + TABLE_TRIP + "(" + TRIP_ID + "))";

    /**
     * SQL query to create "trip" table
     */
    public static final String CREATE_TABLE_TRIP = "CREATE TABLE IF NOT EXISTS " + TABLE_TRIP + "("
            + TRIP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + TRIP_CREATOR + " TEXT NOT NULL,"
            + TRIP_NAME + " TEXT NOT NULL,"
            + TRIP_STARTS_ON + " TEXT NOT NULL,"
            + TRIP_ENDS_ON + " TEXT NOT NULL,"
            + "FOREIGN KEY (" + TRIP_CREATOR + ") REFERENCES " + TABLE_USER + "(" + USER_NICKNAME + "))";

    /**
     * SQL query to create "activity" table
     */
    public static final String CREATE_TABLE_ACTIVITY = "CREATE TABLE IF NOT EXISTS " + TABLE_ACTIVITY + "("
            + ACTIVITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + ACTIVITY_NAME + " TEXT NOT NULL,"
            + ACTIVITY_LOCATION + " TEXT NOT NULL)";
    /**
     * SQL query to create "trip_activity" table
     */
    public static final String CREATE_TABLE_TRIP_ACTIVITY = "CREATE TABLE IF NOT EXISTS " + TABLE_TRIP_ACTIVITY + "("
            + TRIP_ACTIVITY_ACTIVITY_ID + " INTEGER NOT NULL,"
            + TRIP_ACTIVITY_TRIP_ID + " INTEGER NOT NULL,"
            + TRIP_ACTIVITY_RATE + " INTEGER,"
            + TRIP_ACTIVITY_PRICE + " INTEGER NOT NULL,"
            + TRIP_ACTIVITY_STARTS_AT + " INTEGER NOT NULL,"
            + TRIP_ACTIVITY_ENDS_AT + " INTEGER NOT NULL,"
            + "PRIMARY KEY (" + TRIP_ACTIVITY_ACTIVITY_ID + ", " + TRIP_ACTIVITY_TRIP_ID + "),"
            + "FOREIGN KEY (" + TRIP_ACTIVITY_ACTIVITY_ID + ") REFERENCES " + TABLE_ACTIVITY + "(" + ACTIVITY_ID + "),"
            + "FOREIGN KEY (" + TRIP_ACTIVITY_TRIP_ID + ") REFERENCES " + TABLE_TRIP + "(" + TRIP_ID + "))";

    /**
     * SQL query to create "post" table
     */
    public static final String CREATE_TABLE_POST = "CREATE TABLE IF NOT EXISTS " + TABLE_POST + "("
            + POST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + POST_CREATOR + " TEXT NOT NULL,"
            + POST_DATE + " INTEGER NOT NULL,"
            + POST_CONTENT + " TEXT NOT NULL,"
            + " FOREIGN KEY (" + POST_CREATOR + ") REFERENCES " + TABLE_USER + "(" + USER_NICKNAME + "))";

    /**
     * SQL query to create "post_likes" table
     */
    public static final String CREATE_TABLE_POST_VOTES = "CREATE TABLE IF NOT EXISTS " + TABLE_POST_VOTE + "("
            + POST_VOTE_VOTER + " TEXT NOT NULL,"
            + POST_VOTE_POST_ID + " INTEGER NOT NULL,"
            + POST_VOTE_VALUE + " INTEGER NOT NULL,"
            + "PRIMARY KEY (" + POST_VOTE_VOTER + ", " + POST_VOTE_POST_ID + "),"
            + "FOREIGN KEY (" + POST_VOTE_VOTER + ") REFERENCES " + TABLE_USER + "(" + USER_NICKNAME + "),"
            + "FOREIGN KEY (" + POST_VOTE_POST_ID + ") REFERENCES " + TABLE_POST + "(" + POST_ID + "))";


    // Drop queries
    public static final String DROP_TABLE_USER = "DROP TABLE " + TABLE_USER;
    public static final String DROP_TABLE_GUEST = "DROP TABLE " + TABLE_GUEST;
    public static final String DROP_TABLE_TRIP = "DROP TABLE " + TABLE_TRIP;
    public static final String DROP_TABLE_ACTIVITY = "DROP TABLE " + TABLE_ACTIVITY;
    public static final String DROP_TABLE_TRIP_ACTIVITY = "DROP TABLE " + TABLE_TRIP_ACTIVITY;
    public static final String DROP_TABLE_POST = "DROP TABLE " + TABLE_POST;
    public static final String DROP_TABLE_POST_VOTE = "DROP TABLE " + TABLE_POST_VOTE;

    private DbHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    public static synchronized DbHelper getHelper(Context context) {
        if (mInstance == null) {
            mInstance = new DbHelper(context);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_USER);
        sqLiteDatabase.execSQL(CREATE_TABLE_TRIP);
        sqLiteDatabase.execSQL(CREATE_TABLE_GUEST);
        sqLiteDatabase.execSQL(CREATE_TABLE_ACTIVITY);
        sqLiteDatabase.execSQL(CREATE_TABLE_TRIP_ACTIVITY);
        sqLiteDatabase.execSQL(CREATE_TABLE_POST);
        sqLiteDatabase.execSQL(CREATE_TABLE_POST_VOTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop all tables
        sqLiteDatabase.execSQL(DROP_TABLE_USER);
        sqLiteDatabase.execSQL(DROP_TABLE_TRIP);
        sqLiteDatabase.execSQL(DROP_TABLE_GUEST);
        sqLiteDatabase.execSQL(DROP_TABLE_ACTIVITY);
        sqLiteDatabase.execSQL(DROP_TABLE_TRIP_ACTIVITY);
        sqLiteDatabase.execSQL(DROP_TABLE_POST);
        sqLiteDatabase.execSQL(DROP_TABLE_POST_VOTE);

        // Recreate all tables
        sqLiteDatabase.execSQL(CREATE_TABLE_USER);
        sqLiteDatabase.execSQL(CREATE_TABLE_TRIP);
        sqLiteDatabase.execSQL(CREATE_TABLE_GUEST);
        sqLiteDatabase.execSQL(CREATE_TABLE_ACTIVITY);
        sqLiteDatabase.execSQL(CREATE_TABLE_TRIP_ACTIVITY);
        sqLiteDatabase.execSQL(CREATE_TABLE_POST);
        sqLiteDatabase.execSQL(CREATE_TABLE_POST_VOTES);
    }
}
