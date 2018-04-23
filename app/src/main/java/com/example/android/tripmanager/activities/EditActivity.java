package com.example.android.tripmanager.activities;

import android.content.ActivityNotFoundException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.tripmanager.R;
import com.example.android.tripmanager.database.bean.ActivityBean;
import com.example.android.tripmanager.database.dao.ActivityDao;

/**
 * Created by JEREMY on 2018-04-12.
 */

public class EditActivity  extends AppCompatActivity {
    private long mId = 0;
    private EditText mName;
    private EditText mEditLocation;
    private Button mBtnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_activity);
        mEditLocation = findViewById(R.id.edit_insert_location);
        mName = findViewById(R.id.edit_insert_name);


        mBtnSubmit = findViewById(R.id.btn_submit);
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditActivity.EditFormValidator editFormValidator = new EditActivity.EditFormValidator(EditActivity.this);
                try {
                    if (!editFormValidator.validate()) {
                        // At least one incorrect field
                        Toast.makeText(EditActivity.this, "Please that all the fields are correct.", Toast.LENGTH_SHORT).show();
                    } else {
                        // All fields are OK,
                        // we can add the user to the database

                        ActivityBean newActivity = new ActivityBean(mName.getText().toString(),
                                mEditLocation.getText().toString());
                        ActivityDao activityDao = new ActivityDao(EditActivity.this);

                        // Insert the new user
                        activityDao.updateTrip()(newActivity);
                        mId++;
                        // Here insertion is successful,
                        // so we go back to log in screen
                        Toast.makeText(EditActivity.this, "You have been successfully add new Activity!", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(EditActivity.this, "This name has already been taken.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private class EditFormValidator {

        private EditActivity mEditActivity;


        public EditFormValidator(EditActivity editActivity) {
            mEditActivity = editActivity;
        }

        /**
         * Checks that all the fields of the sign up form are correct.
         *
         * @return true if all the fields are correct; false otherwise
         */
        public boolean validate() {

            // Get all inputs values
            String name = mEditActivity.mName.getText().toString();
            Long ID = mEditActivity.mId;
            String location = mEditActivity.mEditLocation.getText().toString();


            // Check if fields are empty
            if (name.isEmpty()) {
                return false;
            }


            if (location.isEmpty()) {
                return false;
            }
            return true;
        }
    }
}
