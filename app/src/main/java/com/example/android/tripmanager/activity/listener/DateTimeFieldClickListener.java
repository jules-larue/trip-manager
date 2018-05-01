package com.example.android.tripmanager.activity.listener;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

import java.util.Calendar;

public class DateTimeFieldClickListener implements View.OnClickListener {

    private Context mContext;

    public DateTimeFieldClickListener(Context context) {
        mContext = context;
    }

    @Override
    public void onClick(View view) {
        Calendar now = Calendar.getInstance();
        new DatePickerDialog(mContext,
                new OnDateTimeSetListener(mContext, (EditText) view),
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH))
                .show();
    }
}
