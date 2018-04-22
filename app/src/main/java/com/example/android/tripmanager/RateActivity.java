package com.example.android.tripmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.android.tripmanager.database.bean.ActivityBean;
import com.example.android.tripmanager.database.bean.TripActivityBean;
import com.example.android.tripmanager.database.bean.TripBean;
import com.example.android.tripmanager.database.dao.ActivityDao;
import com.example.android.tripmanager.database.dao.TripDao;

/**
 * Created by wyn on 2018/4/21.
 */

public class RateActivity extends AppCompatActivity {

    private TextView rateTripName;

    private RadioButton rate1,rate2,rate3,rate4,rate5;

    private EditText comment;

    private String mComment;

    private int mId;

    private int mRate;

    private TripDao tripDao;

    private TripBean trip;

    private ActivityDao activityDao;

    private TripActivityBean tripActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        Bundle bundle = getIntent().getExtras();
        mId = bundle.getInt("mId",1);   //get activityId from former page
        activityDao = new ActivityDao(this);
        tripDao = new TripDao(this);

        initView();
        tripActivity = activityDao.getTripActivity(mId);
        trip = tripDao.selectTrip((int)tripActivity.getTrip().getId());
        rateTripName.setText(trip.getName());
        rateTripName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tripIntent = new Intent(RateActivity.this, TravelDetailActivity.class);
                startActivity(tripIntent);
            }
        });
    }

    public void initView() {

        rate1 = findViewById(R.id.rate1);
        rate2 = findViewById(R.id.rate2);
        rate3 = findViewById(R.id.rate3);
        rate4 = findViewById(R.id.rate4);
        rate5 = findViewById(R.id.rate5);
        comment = findViewById(R.id.comment);
        rateTripName = findViewById(R.id.rateTripName);
    }


    public void submitRate(){

        mComment = comment.getText().toString();
        if(rate1.isChecked()) mRate = 1;
        else if(rate2.isChecked()) mRate = 2;
        else if(rate3.isChecked()) mRate = 3;
        else if(rate4.isChecked()) mRate = 4;
        else if(rate5.isChecked()) mRate = 5;

        int guestNum = trip.getGuests().size();
        int oldRate = tripActivity.getRate();
        int newRate;

        if(oldRate == 0) newRate = mRate;
        else newRate = (guestNum * oldRate + mRate) / (guestNum + 1);
        tripActivity.setRate(newRate);
        activityDao.updateTripActivity(String.valueOf(mId),tripActivity);
    }
}
