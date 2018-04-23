package com.example.android.tripmanager.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.tripmanager.R;
import com.example.android.tripmanager.database.DbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by JEREMY on 2018-04-10.
 */

public class FragmentActivity extends Fragment {

    ArrayList<HashMap<String,String>> noticeList;
    DbHelper mDbHelper;


    public static final String ACTIVITY_NAME = "name";
    public long ACTIVITY_ID = 1;
    public static final String ACTIVITY_LOCATION = "location";

    JSONArray container = null;
     /* UI */
    private RecyclerView rv;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_activities, container, false);
        noticeList = new ArrayList<HashMap<String, String>>();
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv = (RecyclerView) view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(mLinearLayoutManager);






        return view;
    }
    public void makeList(String myJSON) {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);

            for(int i=0; i<container.length(); i++) {

                JSONObject c = container.getJSONObject(i);
                String name = c.getString(ACTIVITY_NAME);
                String location = c.getString(ACTIVITY_LOCATION);




                HashMap<String,String> container = new HashMap<String,String>();
                container.put(ACTIVITY_NAME,name);
                container.put(ACTIVITY_LOCATION,location);


                noticeList.add(container);
            }

            ActivityAdapter adapter = new ActivityAdapter(getActivity(),noticeList);
            Log.e("onCreate[noticeList]", "" + noticeList.size());
            rv.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }catch(JSONException e) {
            e.printStackTrace();
        }
    }

}


