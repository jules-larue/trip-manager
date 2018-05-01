package com.example.android.tripmanager.database.bean;

import com.example.android.tripmanager.util.DateUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jules on 26/03/18.
 */

public class UserBean {

    protected String mNickname;

    protected String mPassword;

    protected String mFirstName;

    protected String mLastName;

    protected Date mBirthDate;

    public static final String BIRTH_DATE_FORMAT = "dd.mm.yyyy";

    public UserBean() {

    }

    public UserBean(String nickname, String password, String firstName,
                    String lastName, Date birthDate) {
        mNickname = nickname;
        mPassword = password;
        mFirstName = firstName;
        mLastName = lastName;
        mBirthDate = birthDate;
    }

    public String getNickname() {
        return mNickname;
    }

    public void setNickname(String nickname) {
        mNickname = nickname;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public Date getBirthDate() {
        return mBirthDate;
    }

    public String getBirthDateAsString() {
        DateFormat dateFormat = new SimpleDateFormat(BIRTH_DATE_FORMAT);
        return dateFormat.format(mBirthDate);
    }

    public void setBirthDate(Date birthDate) {
        mBirthDate = birthDate;
    }

    public int getAge() {
        return DateUtils.computeAge(mBirthDate);
    }


    @Override
    public boolean equals(Object obj) {
        UserBean other = (UserBean) obj;
        return mNickname.equals(other.getNickname());
    }
}
