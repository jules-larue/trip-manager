package com.example.android.tripmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.tripmanager.database.bean.ActivityBean;
import com.example.android.tripmanager.database.bean.TripActivityBean;
import com.example.android.tripmanager.database.bean.UserBean;
import  com.example.android.tripmanager.database.dao.TripDao;
import  com.example.android.tripmanager.database.bean.TripBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wyn on 2018/4/18.
 */

public class TravelDetailActivity extends AppCompatActivity {

    private int travelId;

    private TextView title;

    private TextView creator;

    private TextView place;

    private TextView price;

    private TextView startDay;

    private TextView endDay;

    private TripDao tripDao;

    private ListView scheduleList;

    private ListView guestList;




    private int mId;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_detail);

        Bundle bundle = getIntent().getExtras();
        mId = bundle.getInt("mId",1);
        tripDao = new TripDao(this);

        initView();
        initData();
    }

    /**
     * author : wyn
     * Init the activity's view
     */
    private void initView() {
        title = findViewById(R.id.showTitle);
        creator = findViewById(R.id.creator);
        startDay = findViewById(R.id.startDay);
        endDay = findViewById(R.id.endDay);
        scheduleList = findViewById(R.id.scheduleList);
        guestList = findViewById(R.id.guestList);
    }

    /**
     * author : wyn
     * Init travel's data by travel'id
     * running after initView
     */
    private void initData() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        TripBean trip = tripDao.selectTrip(this.mId);
        title.setText(trip.getName());
        creator.setText(trip.getCreator().getNickname());
        startDay.setText(dateFormat.format(trip.getStartsAt()));
        endDay.setText(dateFormat.format(trip.getEndsAt()));

       // private Map<String, List<String>> dataset = new HashMap<>();
       // private String[] parentList = new String[]{"first", "second", "third"};

        scheduleList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, getScheduleData()));
        guestList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, getGuestData()));

    }

    private List<String> getScheduleData(){

        TripBean trip = tripDao.selectTrip(this.mId);
        TripDao tripDao = new TripDao(this);
        List<String> data = new ArrayList<String>();
        long startsOn, endsOn;
        String activiyName, activityDate;
        ArrayList<TripActivityBean> activities = tripDao.getActivityByTripId(mId);
        int activityNum = activities.size();
        for(int i = 0; i < activityNum; i++){
            startsOn =  activities.get(i).getStartsAt();
            endsOn = activities.get(i).getEndsAt();
            activityDate = String.valueOf(startsOn) + " - " + String.valueOf(endsOn);
            activiyName = activities.get(i).getName();
            data.add(activityDate);
            data.add(activiyName);
        }


        return data;
    }

    private List<String> getGuestData(){

        TripBean trip = tripDao.selectTrip(this.mId);
        List<String> data = new ArrayList<String>();
        //data.add(trip.getGuests());

        while(trip.getGuests().iterator().hasNext())
        {
            UserBean user = trip.getGuests().iterator().next();
            data.add(user.getNickname());
        }

        return data;
    }
}
