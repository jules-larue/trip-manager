package com.example.android.tripmanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.tripmanager.R;
import com.example.android.tripmanager.activity.AddTripActivityActivity;
import com.example.android.tripmanager.activity.AllActivitiesActivity;
import com.example.android.tripmanager.database.bean.ActivityBean;

import java.util.ArrayList;

public class AllActivitiesAdapter extends RecyclerView.Adapter<AllActivitiesAdapter.ActivityViewHolder> {

    private AllActivitiesActivity mRelatedActivity;

    private ArrayList<ActivityBean> mActivities;

    /**
     * The id of the trip we want to add an activity to.
     * This id is passed to the intent that launches the
     * AddActivity activity.
     */
    private long mTripId;

    public AllActivitiesAdapter(AllActivitiesActivity activity, ArrayList<ActivityBean> activities, long tripId) {
        mRelatedActivity = activity;
        mActivities = activities;
        mTripId = tripId;
    }

    @Override
    public ActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View activityItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item, parent, false);

        return new ActivityViewHolder(activityItemView);
    }

    @Override
    public void onBindViewHolder(ActivityViewHolder holder, int position) {
        ActivityBean activity = mActivities.get(position);

        holder.name.setText(activity.getName());
        holder.location.setText(activity.getLocation());
        String avgPrice = String.format(mRelatedActivity.getString(R.string.price_value_float),
                activity.getAvgPrice());
        String avgRate = String.format(mRelatedActivity.getString(R.string.rate_value_float), activity.getAvgRate());
        holder.avgPrice.setText(avgPrice);
        holder.avgRate.setText(avgRate);

    }

    @Override
    public int getItemCount() {
        return mActivities.size();
    }

    public class ActivityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name;
        public TextView location;
        public TextView avgPrice;
        public TextView avgRate;

        public ActivityViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_activity_name);
            location = itemView.findViewById(R.id.tv_activity_location);
            avgPrice = itemView.findViewById(R.id.tv_activity_avg_price);
            avgRate = itemView.findViewById(R.id.tv_activity_avg_rate);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            final ActivityBean activityClicked = mActivities.get(clickedPosition);

            Intent createActivityIntent = new Intent(mRelatedActivity, AddTripActivityActivity.class);
            createActivityIntent.putExtra(AddTripActivityActivity.EXTRA_RELATED_ACTIVITY_ID,
                    activityClicked.getId());
            createActivityIntent.putExtra(AddTripActivityActivity.EXTRA_TRIP_ID,
                    mTripId);
            mRelatedActivity.startActivity(createActivityIntent);

            // The activity showing all the activities should be killed
            mRelatedActivity.finish();
        }
    }
}
