package com.example.android.tripmanager.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.android.tripmanager.database.DbHelper;
import com.example.android.tripmanager.database.exception.NicknameAlreadyExistsException;
import com.example.android.tripmanager.database.bean.UserBean;
import com.example.android.tripmanager.database.exception.UserNotFoundException;

/**
 * Created by jules on 26/03/18.
 */

public class UserDao {

    private DbHelper mDbHelper;

    public UserDao(Context context) {
        mDbHelper = DbHelper.getHelper(context);
    }

    public long insertUser(UserBean user) throws NicknameAlreadyExistsException {
        // Check if user with same nickname exists
        if (userExists(user.getNickname())) {
            throw new NicknameAlreadyExistsException(user.getNickname());
        }

        ContentValues values = toContentValues(user);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        return db.insert(DbHelper.TABLE_USER, null, values);
    }

    private boolean userExists(String nickname) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Search for a user with the required nickname
        Cursor cursor = db.query(DbHelper.TABLE_USER,
                null,
                DbHelper.USER_NICKNAME + " = ?",
                new String[]{ nickname },
                null,
                null,
                null,
                null);

        // We get one result if one user with nickname is found
        return cursor.getCount() > 0;
    }

    public int updateUser(String nickname, UserBean newUserInfo) {
        ContentValues updateValues = toContentValues(newUserInfo);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        return db.update(DbHelper.TABLE_USER,
                updateValues,
                DbHelper.USER_NICKNAME + " = ?",
                new String[]{ nickname });
    }

    public int deleteUser(String nickname) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        return db.delete(DbHelper.TABLE_USER,
                DbHelper.USER_NICKNAME + " = ?",
                new String[]{ nickname });
    }

    public UserBean getUserByNickname(String nickname) throws UserNotFoundException {
        if (!userExists(nickname)) {
            throw new UserNotFoundException(nickname);
        }

        UserBean result = null;

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.query(DbHelper.TABLE_USER,
                null,
                DbHelper.USER_NICKNAME + " = ?",
                new String[]{ nickname },
                null,
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            result = toBean(cursor);
        }

        cursor.close();
        return result;
    }

    private UserBean toBean(Cursor cursor) {
        String nickname = cursor.getString(cursor.getColumnIndex(DbHelper.USER_NICKNAME));
        String password = cursor.getString(cursor.getColumnIndex(DbHelper.USER_PASSWORD));
        String firstName = cursor.getString(cursor.getColumnIndex(DbHelper.USER_FIRST_NAME));
        String lastName = cursor.getString(cursor.getColumnIndex(DbHelper.USER_LAST_NAME));
        long birthDate = cursor.getLong(cursor.getColumnIndex(DbHelper.USER_BIRTH_DATE));

        return new UserBean(nickname,
                password,
                firstName,
                lastName,
                birthDate);
    }

    private ContentValues toContentValues(UserBean user) {
        ContentValues values = new ContentValues();
        values.put(DbHelper.USER_NICKNAME, user.getNickname());
        values.put(DbHelper.USER_PASSWORD, user.getPassword());
        values.put(DbHelper.USER_FIRST_NAME, user.getmFirstName());
        values.put(DbHelper.USER_LAST_NAME, user.getLastName());
        values.put(DbHelper.USER_BIRTH_DATE, user.getBirthDateMillis());
        return values;
    }
}
