package com.example.android.tripmanager.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.tripmanager.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by JEREMY on 2018-04-18.
 */


public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {
    Context context;
    ArrayList<HashMap<String, String>> noticeList;

    public ActivityAdapter(Context context, ArrayList<HashMap<String, String>> noticeList) {
        Log.e("[IMPORTANT] ", "init size" + noticeList.size());
        this.context = context;
        this.noticeList = noticeList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_activities,null);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        HashMap<String, String> noticeData = noticeList.get(position);
        final int pos = position;

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, FragmentActivity.class);
//                i.putExtra("name", noticeList.get(pos).name);
//                i.putExtra("location", noticeList.get(pos).location);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.noticeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_date;
        TextView tv_content;
        TextView tv_writer;
        TextView tv_count;
        CardView cv;
        ImageView iv_image;
        RelativeLayout rl_image;

        public ViewHolder(View v) {
            super(v);
            tv_title = (TextView) v.findViewById(R.id.tv_title);
            tv_date = (TextView) v.findViewById(R.id.tv_date);
            tv_content = (TextView) v.findViewById(R.id.tv_content);
            tv_writer = (TextView) v.findViewById(R.id.tv_writer);
            tv_count = (TextView) v.findViewById(R.id.tv_count);
            cv = (CardView) v.findViewById(R.id.cv);
            iv_image = (ImageView) v.findViewById(R.id.iv_image);
            rl_image = (RelativeLayout) v.findViewById(R.id.rl_image);
        }
    }


}
