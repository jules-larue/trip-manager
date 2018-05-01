package com.example.android.tripmanager.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.tripmanager.R;
import com.example.android.tripmanager.activity.listener.DateTimeFieldClickListener;
import com.example.android.tripmanager.database.bean.TripActivityBean;
import com.example.android.tripmanager.database.dao.ActivityDao;
import com.example.android.tripmanager.database.dao.TripDao;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class EditTripActivityActivity extends AppCompatActivity {

    private long mTripActivityId;

    private Spinner mSpinnerRate;
    private EditText mEditPrice;
    private EditText mEditStartsAt;
    private EditText mEditEndsAt;
    private Button mBtnSaveChanges;

    public static final String EXTRA_TRIP_ACTIVITY_ID = "tripActivityId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trip_activity);

        mTripActivityId = getIntent()
                .getExtras()
                .getLong(EXTRA_TRIP_ACTIVITY_ID, -1);

        // Init widgets
        mSpinnerRate = findViewById(R.id.edit_rate);
        mEditPrice = findViewById(R.id.edit_price);
        mEditStartsAt = findViewById(R.id.edit_starts_at);
        mEditEndsAt = findViewById(R.id.edit_ends_at);

        setTitle(R.string.edit_activity);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Assign listeners to date fields
        mEditStartsAt.setOnClickListener(new DateTimeFieldClickListener(this));
        mEditEndsAt.setOnClickListener(new DateTimeFieldClickListener(this));

        // Get the trip activity and display its info
        final TripDao tripDao = new TripDao(getApplicationContext());
        final TripActivityBean tripActivity = tripDao.getTripActivityById(mTripActivityId);
        mSpinnerRate.setSelection(tripActivity.getRate());
        mEditPrice.setText(String.valueOf(tripActivity.getPrice()));
        mEditStartsAt.setText(tripActivity.getStartsAtFormatted());
        mEditEndsAt.setText(tripActivity.getEndsAtFormatted());

        mBtnSaveChanges = findViewById(R.id.btn_edit_trip_activity);
        mBtnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditFormValidator validator = new EditFormValidator();
                if (validator.validate()) {
                    // Update the activity
                    int rate = Integer.parseInt((String) mSpinnerRate.getSelectedItem());
                    int price = Integer.parseInt(mEditPrice.getText().toString());
                    String startsAt = mEditStartsAt.getText().toString();
                    String endsAt = mEditEndsAt.getText().toString();

                    // Convert dates to millis
                    DateFormat dateFormat = new SimpleDateFormat(TripActivityBean.DATETIME_FORMAT);
                    try {
                        long startsAtMillis = dateFormat.parse(startsAt).getTime();
                        long endsAtMillis = dateFormat.parse(endsAt).getTime();
                        tripActivity.setRate(rate);
                        tripActivity.setPrice(price);
                        tripActivity.setStartsAt(startsAtMillis);
                        tripActivity.setEndsAt(endsAtMillis);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    // Update the activity in database
                    ActivityDao activityDao = new ActivityDao(getApplicationContext());
                    activityDao.updateTripActivity(mTripActivityId, tripActivity);
                    Toast.makeText(EditTripActivityActivity.this, "The activity has been updated!", Toast.LENGTH_LONG).show();

                    // Close the activity
                    finish();
                } else {
                    // Errors
                    Toast.makeText(EditTripActivityActivity.this, "Please check that all the fields are correct.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private class EditFormValidator {

        public boolean validate() {
            // Get fields values
            String rate = mSpinnerRate.getSelectedItem().toString();
            String price = mEditPrice.getText().toString();
            String startsAt = mEditStartsAt.getText().toString();
            String endsAt = mEditEndsAt.getText().toString();

            if (rate.isEmpty() || price.isEmpty()
                    || startsAt.isEmpty() || endsAt.isEmpty()) {
                return false;
            }

            try {
                if (Integer.parseInt(price) < 0) {
                    return false;
                }
            } catch (NumberFormatException e) {
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
