package com.example.android.tripmanager.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.android.tripmanager.R;
import com.example.android.tripmanager.activity.listener.DateTimeFieldClickListener;
import com.example.android.tripmanager.database.bean.ActivityBean;
import com.example.android.tripmanager.database.bean.TripActivityBean;
import com.example.android.tripmanager.database.bean.TripBean;
import com.example.android.tripmanager.database.dao.ActivityDao;
import com.example.android.tripmanager.database.dao.TripDao;
import com.example.android.tripmanager.database.exception.ActivityAlreadyInTripException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddTripActivityActivity extends AppCompatActivity {

    /**
     * Id of the activity corresponding to the trip activity.
     * Value is -1 if there is not (we create a brand new activity).
     */
    private long mIdRelatedActivity;
    private long mIdTrip;

    private EditText mEditName;
    private EditText mEditLocation;
    private EditText mEditPrice;
    private EditText mEditStartsAt;
    private EditText mEditEndsAt;

    private Button mBtnAddActivity;

    public static final String EXTRA_TRIP_ID = "tripId";
    public static final String EXTRA_RELATED_ACTIVITY_ID = "relatedActivityId";

    public static final long NO_RELATED_ACTIVITY_ID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip_activity);

        // Check if we create a trip activity from an existing activity
        // value is -1 if we create a brand new activity
        mIdRelatedActivity = getIntent()
                .getLongExtra(EXTRA_RELATED_ACTIVITY_ID, NO_RELATED_ACTIVITY_ID);
        mIdTrip = getIntent()
                .getLongExtra(EXTRA_TRIP_ID, -1);

        // Init widgets
        mEditName = findViewById(R.id.edit_name);
        mEditLocation = findViewById(R.id.edit_location);
        mEditPrice = findViewById(R.id.edit_price);
        mEditStartsAt = findViewById(R.id.edit_starts_at);
        mEditEndsAt = findViewById(R.id.edit_ends_at);
        mBtnAddActivity = findViewById(R.id.btn_add_trip_activity);

        setTitle(R.string.add_activity_to_trip);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Assign click listeners to date fields
        mEditStartsAt.setOnClickListener(new DateTimeFieldClickListener(this));
        mEditEndsAt.setOnClickListener(new DateTimeFieldClickListener(this));

        final ActivityDao activityDao = new ActivityDao(this);

        if (mIdRelatedActivity != NO_RELATED_ACTIVITY_ID) {
            // We create a trip activity from an existing activity
            // The 'name' and 'location' should NOT be editable,
            // and we set the text of the existing activity in database
            mEditName.setKeyListener(null);
            mEditLocation.setKeyListener(null);

            ActivityBean relatedActivity = activityDao.getActivityById(mIdRelatedActivity);
            mEditName.setText(relatedActivity.getName());
            mEditLocation.setText(relatedActivity.getLocation());
        }

        mBtnAddActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditFormValidator validator = new EditFormValidator();
                if (validator.validate()) {
                    /*  All fields correct
                        First, check if we have to insert a new activity
                    */

                    if (mIdRelatedActivity == NO_RELATED_ACTIVITY_ID) {
                        // Create a new activity
                        // with the values of fields
                        String name = mEditName.getText().toString();
                        String location = mEditLocation.getText().toString();
                        ActivityBean newActivity = new ActivityBean(name, location);

                        // Insert the new activity in the database
                        mIdRelatedActivity = activityDao.insertActivity(newActivity);
                    }

                    TripDao tripDao = new TripDao(getApplicationContext());

                    // Create a trip activity object and insert it in the database
                    String price = mEditPrice.getText().toString();
                    String startsAt = mEditStartsAt.getText().toString();
                    String endsAt = mEditEndsAt.getText().toString();
                    TripActivityBean tripActivity = new TripActivityBean();
                    tripActivity.setPrice(Integer.parseInt(price));
                    tripActivity.setId(mIdRelatedActivity);

                    DateFormat dateFormat = new SimpleDateFormat(TripActivityBean.DATETIME_FORMAT);
                    try {
                        tripActivity.setStartsAt(dateFormat.parse(startsAt).getTime());
                        tripActivity.setEndsAt(dateFormat.parse(endsAt).getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    try {
                        // Trip activity is created
                        tripDao.addTripActivity(tripActivity, mIdTrip);
                        Toast.makeText(AddTripActivityActivity.this, "The activity has been added to the trip.", Toast.LENGTH_LONG).show();

                        // Get back to previous activity
                        finish();
                    } catch (ActivityAlreadyInTripException e) {
                        // Activity already added
                        Toast.makeText(AddTripActivityActivity.this, "This activity is already in the trip!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else {
                    // Validation fails, at least one field wrong
                    Toast.makeText(AddTripActivityActivity.this, "Please check the fields.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class EditFormValidator {

        public boolean validate() {
            // Get fields values
            String name = mEditName.getText().toString();
            String location = mEditLocation.getText().toString();
            String price = mEditPrice.getText().toString();
            String startsAt = mEditStartsAt.getText().toString();
            String endsAt = mEditEndsAt.getText().toString();

            if (name.isEmpty()
                    || location.isEmpty()
                    || price.isEmpty()
                    || startsAt.isEmpty()
                    || endsAt.isEmpty()) {
                // At least one field is empty
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
