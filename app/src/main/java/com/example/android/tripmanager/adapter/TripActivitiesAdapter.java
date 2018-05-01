package com.example.android.tripmanager.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.tripmanager.R;
import com.example.android.tripmanager.activity.EditTripActivityActivity;
import com.example.android.tripmanager.activity.TripActivitiesListActivity;
import com.example.android.tripmanager.database.bean.TripActivityBean;
import com.example.android.tripmanager.database.dao.TripDao;

import java.util.ArrayList;

public class TripActivitiesAdapter extends RecyclerView.Adapter<TripActivitiesAdapter.ActivityViewHolder> {

    private TripActivitiesListActivity mActivity;
    private ArrayList<TripActivityBean> mActivities;

    public TripActivitiesAdapter(TripActivitiesListActivity activity, ArrayList<TripActivityBean> activities) {
        mActivity = activity;
        mActivities = activities;
    }

    @Override
    public ActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View activityCardView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trip_activity_cardview_layout, parent, false);

        return new ActivityViewHolder(activityCardView);
    }

    @Override
    public void onBindViewHolder(ActivityViewHolder holder, int position) {
        // Get the activity to display
        final TripActivityBean tripActivity = mActivities.get(position);

        // Se the view data
        holder.name.setText(tripActivity.getName());
        holder.location.setText(tripActivity.getLocation());
        holder.fromTime.setText(tripActivity.getStartTimeFormatted());
        holder.fromDate.setText(tripActivity.getStartDateFormatted());
        holder.toTime.setText(tripActivity.getEndTimeFormatted());
        holder.toDate.setText(tripActivity.getEndDateFormatted());
        holder.price.setText(String.valueOf(tripActivity.getPrice()));
        String rate = String.format(mActivity.getString(R.string.rate_value_int),
                tripActivity.getRate());
        holder.rate.setText(rate);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Run the EditActivity for this trip activity
                Intent editIntent = new Intent(mActivity, EditTripActivityActivity.class);
                editIntent.putExtra(EditTripActivityActivity.EXTRA_TRIP_ACTIVITY_ID,
                        tripActivity.getId());
                mActivity.startActivity(editIntent);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener confirmDialogListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                TripDao tripDao = new TripDao(mActivity);
                                tripDao.deleteTripActivity(tripActivity);

                                // Show a Toast message and reload the activities
                                Toast.makeText(mActivity, "The activity has been removed from the trip.", Toast.LENGTH_SHORT).show();
                                mActivity.onActivityDeleted();
                                break;
                        }
                        dialog.dismiss();
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setMessage(mActivity.getString(R.string.title_confirm_delete_trip_activity))
                        .setPositiveButton(mActivity.getString(R.string.yes), confirmDialogListener)
                        .setNegativeButton(mActivity.getString(R.string.no), confirmDialogListener)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mActivities.size();
    }

    public static class ActivityViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView location;
        public TextView fromTime;
        public TextView fromDate;
        public TextView toTime;
        public TextView toDate;
        public TextView price;
        public TextView rate;
        public ImageButton edit;
        public ImageButton delete;

        public ActivityViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_trip_activity_name);
            location = itemView.findViewById(R.id.tv_trip_activity_location);
            fromTime = itemView.findViewById(R.id.tv_from_time);
            fromDate = itemView.findViewById(R.id.tv_from_date);
            toTime = itemView.findViewById(R.id.tv_to_time);
            toDate = itemView.findViewById(R.id.tv_to_date);
            price = itemView.findViewById(R.id.tv_price);
            rate = itemView.findViewById(R.id.tv_rate);
            edit = itemView.findViewById(R.id.btn_edit);
            delete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
