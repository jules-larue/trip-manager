package com.example.android.tripmanager.activity.listener;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.android.tripmanager.database.bean.TripActivityBean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class OnDateTimeSetListener implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private Context mContext;

    private EditText mDateField;

    /**
     * The date that we are setting with the DatePicker
     * and the TimePicker
     */
    private Calendar mDateSet = Calendar.getInstance();

    public OnDateTimeSetListener(Context context, EditText dateField) {
        mContext = context;
        mDateField = dateField;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        // Get the date selected
        mDateSet.set(Calendar.YEAR, year);
        mDateSet.set(Calendar.MONTH, monthOfYear);
        mDateSet.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        Calendar now = Calendar.getInstance();
        new TimePickerDialog(mContext,
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true)
                .show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        mDateSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mDateSet.set(Calendar.MINUTE, minute);

        // Format the date time and display it
        DateFormat dateTimeFormat = new SimpleDateFormat(TripActivityBean.DATETIME_FORMAT);
        String formattedDateTime = dateTimeFormat.format(mDateSet.getTime());
        mDateField.setText(formattedDateTime);
    }
}
