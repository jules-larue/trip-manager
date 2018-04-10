package com.example.android.tripmanager.database.bean;

import java.util.Date;

/**
 * Created by jules on 26/03/18.
 */

public class UserBean {

    protected String mNickname;

    protected String mPassword;

    protected String mFirstName;

    protected String mLastName;

    protected long mBirthDate;

    public UserBean() {

    }

    public UserBean(String nickname, String password, String firstName,
                    String lastName, long birthDate) {
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

    public String getmFirstName() {
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

    public long getBirthDateMillis() {
        return mBirthDate;
    }

    public Date getBirthDate() {
        return new Date(mBirthDate);
    }

    public void setBirthDate(long birthDate) {
        mBirthDate = birthDate;
    }

}
