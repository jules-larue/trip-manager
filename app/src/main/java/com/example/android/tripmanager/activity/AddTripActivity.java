package com.example.android.tripmanager.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.tripmanager.R;
import com.example.android.tripmanager.UserConnectedManager;
import com.example.android.tripmanager.activity.listener.DateFieldClickListener;
import com.example.android.tripmanager.database.bean.TripBean;
import com.example.android.tripmanager.database.bean.UserBean;
import com.example.android.tripmanager.database.dao.TripDao;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddTripActivity extends AppCompatActivity {

    private EditText mEditName;
    private EditText mEditStartDate;
    private EditText mEditEndDate;
    private Button mBtnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        mEditName = findViewById(R.id.edit_trip_name);
        mEditStartDate = findViewById(R.id.edit_trip_start_date);
        mEditEndDate = findViewById(R.id.edit_trip_end_date);
        mBtnSave = findViewById(R.id.btn_add_trip);

        setTitle(R.string.add_a_trip);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Assign each date field a click listener
        mEditStartDate.setOnClickListener(new DateFieldClickListener(this));
        mEditEndDate.setOnClickListener(new DateFieldClickListener(this));

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTripFormValidator validator = new AddTripFormValidator();
                if (validator.validate()) {
                    String name = mEditName.getText().toString();
                    String startDate = mEditStartDate.getText().toString();
                    String endDate = mEditEndDate.getText().toString();
                    UserBean creator = UserConnectedManager.getConnectedUser();

                    TripBean tripToAdd = new TripBean(name, creator, startDate, endDate);
                    
                    // Add the trip in the database
                    TripDao tripDao = new TripDao(getApplicationContext());
                    tripDao.insertTrip(tripToAdd);

                    Toast.makeText(AddTripActivity.this, "The trip was successfully added!", Toast.LENGTH_SHORT).show();

                    // Kill this activity
                    finish();
                } else {
                    Toast.makeText(AddTripActivity.this, "Please check that fields are correct.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private class AddTripFormValidator {

        public boolean validate() {
            String name = mEditName.getText().toString();
            String startDateString = mEditStartDate.getText().toString();
            String endDateString = mEditEndDate.getText().toString();

            if (name.isEmpty()) {
                return false;
            }

            // Check date formats
            try {
                DateFormat dateFormat = new SimpleDateFormat(TripBean.DATE_FORMAT);
                Date startDate = dateFormat.parse(startDateString);
                Date endDate = dateFormat.parse(endDateString);

                if (startDate.after(endDate)) {
                    return false;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }

            return true;
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
