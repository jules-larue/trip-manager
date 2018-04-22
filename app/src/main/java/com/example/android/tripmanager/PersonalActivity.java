package com.example.android.tripmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.tripmanager.database.bean.GuestBean;
import com.example.android.tripmanager.database.bean.TripBean;
import com.example.android.tripmanager.database.bean.UserBean;
import com.example.android.tripmanager.database.dao.TripDao;
import com.example.android.tripmanager.database.dao.UserDao;
import com.example.android.tripmanager.database.exception.UserNotFoundException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wyn on 2018/4/22.
 */

public class PersonalActivity extends AppCompatActivity {

    private TextView username;

    private TextView firstName;

    private TextView lastName;

    private TextView birthDate;

   // private TextView password;

    private ListView tripListView;

    private UserDao userDao;

    private GuestBean guest;

    private String mNickname;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_detail);

        Bundle bundle = getIntent().getExtras();
        mNickname = bundle.getString("mNickname","");
        userDao = new UserDao(this);

        initView();
        initData();

    }

    private void initView(){
        username = findViewById(R.id.mUsername);
        firstName = findViewById(R.id.mFirstName);
        lastName = findViewById(R.id.mLastName);
        birthDate = findViewById(R.id.mBirthDate);
        tripListView = findViewById(R.id.mTripListView);
    }

    private void initData(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            UserBean user = userDao.getUserByNickname(mNickname);
            firstName.setText(user.getmFirstName());
            lastName.setText(user.getLastName());
            birthDate.setText(user.getBirthDateAsString());


            guest = (GuestBean) user;
            tripListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, getData()));

        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }


    }

    private List<String> getData(){

        ArrayList<TripBean> tripList = new ArrayList<>();
        String guestName = guest.getNickname();
        tripList = userDao.getTripByGuestName(guestName);
        int tripNum = tripList.size();
        List<String> data = new ArrayList<String>();
        for(int i = 0; i < tripNum; i++) {
            data.add(tripList.get(i).getName());
        }

        return data;
    }
}
