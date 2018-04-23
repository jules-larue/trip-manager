package com.example.android.tripmanager.activities;

import android.content.ActivityNotFoundException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.android.tripmanager.R;
import com.example.android.tripmanager.database.bean.ActivityBean;
import com.example.android.tripmanager.database.dao.ActivityDao;

/**
 * Created by JEREMY on 2018-04-16.
 */

public class DeleteActivity extends AppCompatActivity {


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
                DeleteActivity deleteActivity = new DeleteActivity(DeleteActivity.this);

                        ActivityBean newActivity = new ActivityBean(mName.getText().toString(),
                                mEditLocation.getText().toString());
                        ActivityDao activityDao = new ActivityDao(DeleteActivity.this);

                        // Insert the new user
                        activityDao.deleteTrip()(newActivity);

                        // Here insertion is successful,
                        // so we go back to log in screen
                        Toast.makeText(DeleteActivity.this, "You have been successfully delete previous Activity!", Toast.LENGTH_SHORT).show();
                        finish();



            }
        });
    }
}
