package com.example.android.tripmanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.tripmanager.R;
import com.example.android.tripmanager.UserConnectedManager;
import com.example.android.tripmanager.activity.TripActivitiesListActivity;
import com.example.android.tripmanager.database.bean.TripBean;
import com.example.android.tripmanager.database.bean.UserBean;
import com.example.android.tripmanager.database.dao.TripDao;
import com.example.android.tripmanager.database.exception.GuestAlreadyInTripException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import static android.graphics.Typeface.BOLD;

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.TripViewHolder> {

    private Context mContext;

    private ArrayList<TripBean> mTrips;

    public TripsAdapter(Context context, ArrayList<TripBean> trips) {
        mContext = context;
        mTrips = trips;
    }

    @Override
    public TripViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View tripItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trip_item, parent, false);

        return new TripViewHolder(tripItemView);
    }

    @Override
    public void onBindViewHolder(TripViewHolder holder, int position) {
        TripBean trip = mTrips.get(position);

        // Set data to display
        String creator = trip.getCreator().getNickname();
        holder.creator.setText(creator);
        holder.name.setText(trip.getName());

        DateFormat dateFormat = new SimpleDateFormat(TripBean.DATE_FORMAT);
        DateFormat dateFormatHumanReadable = DateFormat.getDateInstance(DateFormat.LONG, Locale.ENGLISH);
        try {
            String dateRange = String.format(mContext.getString(R.string.date_range),
                    dateFormatHumanReadable.format(dateFormat.parse(trip.getStartsOn())),
                    dateFormatHumanReadable.format(dateFormat.parse(trip.getEndsOn())));
            holder.dates.setText(dateRange);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int nbActivities = trip.getActivities().size();
        String nbActivitiesText = nbActivities +
                (nbActivities > 1 ? " activities" : " activity");

        holder.nbActivities.setText(nbActivitiesText);
        int nbGuests = trip.getGuests().size();
        String nbGuestsText = nbGuests +
                (nbGuests > 1 ? " guests" : " guest");
        holder.nbGuests.setText(nbGuestsText);

        // Handle the visibility of 'join' and 'leave' buttons
        UserBean userConnected = UserConnectedManager.getConnectedUser();
        TripDao tripDao = new TripDao(mContext);
        if (tripDao.guestExists(userConnected.getNickname(), trip.getId())) {
            holder.btnLeave.setVisibility(View.VISIBLE);
            holder.btnJoin.setVisibility(View.GONE);
        } else {
            // User not in the trip
            holder.btnLeave.setVisibility(View.GONE);
            holder.btnJoin.setVisibility(View.VISIBLE);
        }

        String tripCreator = trip.getCreator().getNickname();
        // Hide buttons if the user is the creator, and put creator in bold
        if (userConnected.getNickname().equals(tripCreator)) {
            holder.btnLeave.setVisibility(View.GONE);
            holder.btnJoin.setVisibility(View.GONE);
            holder.creator.setTypeface(null, BOLD);
        }
    }

    @Override
    public int getItemCount() {
        return mTrips.size();
    }

    public class TripViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView creator;
        public TextView dates;
        public TextView nbActivities;
        public TextView nbGuests;
        public Button btnView;
        public Button btnJoin;
        public Button btnLeave;

        public TripViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_trip_name);
            creator = itemView.findViewById(R.id.tv_trip_creator);
            dates = itemView.findViewById(R.id.tv_trip_dates);
            nbActivities = itemView.findViewById(R.id.tv_nb_activities);
            nbGuests = itemView.findViewById(R.id.tv_nb_guests);
            btnView = itemView.findViewById(R.id.btn_view_trip);
            btnJoin = itemView.findViewById(R.id.btn_join_trip);
            btnLeave = itemView.findViewById(R.id.btn_leave_trip);

            btnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int clickedPosition = getAdapterPosition();
                    final TripBean tripSelected = mTrips.get(clickedPosition);
                    Intent tripDetailsIntent = new Intent(mContext, TripActivitiesListActivity.class);
                    tripDetailsIntent.putExtra(TripActivitiesListActivity.EXTRA_TRIP_ID,
                            tripSelected.getId());

                    mContext.startActivity(tripDetailsIntent);
                }
            });


            final UserBean userConnected = UserConnectedManager.getConnectedUser();

            btnJoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int clickedPosition = getAdapterPosition();
                    final TripBean tripSelected = mTrips.get(clickedPosition);
                    TripDao tripDao = new TripDao(mContext);
                    try {
                        // Add the user to the trip
                        tripDao.addGuest(userConnected.getNickname(), tripSelected.getId());

                        Toast.makeText(mContext, "You are now part of the trip " + tripSelected.getName() + "!", Toast.LENGTH_SHORT).show();

                        // Hide the 'join' button and show the 'leave' button
                        btnJoin.setVisibility(View.GONE);
                        btnLeave.setVisibility(View.VISIBLE);

                        // Increment the number of guests displayed
                        tripSelected.addGuest(userConnected);
                        int nbGuestsInt = tripSelected.getGuests().size();
                        String nbGuestsText = nbGuestsInt +
                                (nbGuestsInt > 1 ? " guests" : " guest");
                        nbGuests.setText(nbGuestsText);
                    } catch (GuestAlreadyInTripException e) {
                        e.printStackTrace();
                    }
                }
            });


            btnLeave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int clickedPosition = getAdapterPosition();
                    final TripBean tripSelected = mTrips.get(clickedPosition);
                    // Delete the user from the trip
                    TripDao tripDao = new TripDao(mContext);
                    tripDao.deleteGuest(userConnected.getNickname(), tripSelected.getId());

                    // User added to the trip
                    Toast.makeText(mContext, "You have left the trip " + tripSelected.getName() + ".", Toast.LENGTH_SHORT).show();

                    // Hide the 'leave' button and show the 'join' button
                    btnLeave.setVisibility(View.GONE);
                    btnJoin.setVisibility(View.VISIBLE);

                    // Decrement the number of guests displayed
                    tripSelected.deleteGuest(userConnected);
                    int nbGuestsInt = tripSelected.getGuests().size();
                    String nbGuestsText = nbGuestsInt +
                            (nbGuestsInt > 1 ? " guests" : " guest");
                    nbGuests.setText(nbGuestsText);
                }
            });
        }
    }
}
