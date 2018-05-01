package com.example.android.tripmanager.activity.listener;

import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.android.tripmanager.database.bean.TripBean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class OnDateSetListener implements DatePickerDialog.OnDateSetListener {

    private EditText mDateField;

    public OnDateSetListener(EditText dateField) {
        mDateField = dateField;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        DateFormat dateFormat = new SimpleDateFormat(TripBean.DATE_FORMAT);

        // Get the date selected
        Calendar dateSet = Calendar.getInstance();
        dateSet.set(Calendar.YEAR, year);
        dateSet.set(Calendar.MONTH, monthOfYear);
        dateSet.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        // Format the date and display it in the field
        String dateToDisplay = dateFormat.format(dateSet.getTime());
        mDateField.setText(dateToDisplay);
    }
}
