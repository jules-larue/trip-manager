package com.example.android.tripmanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.tripmanager.R;
import com.example.android.tripmanager.database.bean.UserBean;

import java.util.ArrayList;

public class GuestsAdapter extends RecyclerView.Adapter<GuestsAdapter.GuestViewHolder> {

    private Context mContext;

    private ArrayList<UserBean> mGuests;

    public GuestsAdapter(Context context, ArrayList<UserBean> guests) {
        mContext = context;
        mGuests = guests;
    }

    @Override
    public GuestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View guestItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.guest_item, parent, false);

        return new GuestViewHolder(guestItemView);
    }

    @Override
    public void onBindViewHolder(GuestViewHolder holder, int position) {
        UserBean user = mGuests.get(position);

        holder.nickname.setText(user.getNickname());

        String nameAndAge = String.format(mContext.getString(R.string.name_and_age),
                user.getFirstName(),
                user.getLastName(),
                user.getAge());
        holder.nameAndAge.setText(nameAndAge);
    }

    @Override
    public int getItemCount() {
        return mGuests.size();
    }

    public class GuestViewHolder extends RecyclerView.ViewHolder {

        public TextView nickname;
        public TextView nameAndAge;

        public GuestViewHolder(View itemView) {
            super(itemView);
            nickname = itemView.findViewById(R.id.tv_guest_nickname);
            nameAndAge = itemView.findViewById(R.id.tv_guest_name_and_age);
        }

    }
}
