package com.example.android.tripmanager.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.example.android.tripmanager.R;
import com.example.android.tripmanager.adapter.NewsFeedAdapter;
import com.example.android.tripmanager.database.bean.PostBean;
import com.example.android.tripmanager.database.dao.PostDao;

import java.util.ArrayList;

public class NewsFeedFragment extends Fragment {

    private RecyclerView mNewsFeed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_feed, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mNewsFeed = getView().findViewById(R.id.rv_news_feed);
        mNewsFeed.setLayoutManager(new LinearLayoutManager(getContext()));

        getActivity().setTitle(R.string.news_feed);
        // Load posts
        loadPosts();
    }

    public void loadPosts() {
        PostDao postDao = new PostDao(getContext());
        ArrayList<PostBean> posts = postDao.getAllPostsOrderedByDate();

        NewsFeedAdapter newsFeedAdapter = new NewsFeedAdapter(this, posts);

        mNewsFeed.setAdapter(newsFeedAdapter);
    }
}
