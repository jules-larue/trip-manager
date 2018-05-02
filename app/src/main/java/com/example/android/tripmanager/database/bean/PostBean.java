package com.example.android.tripmanager.database.bean;

import com.example.android.tripmanager.VoteValue;

import java.util.HashMap;

public class PostBean {

    private long mId;

    private UserBean mCreator;

    private long mDate;

    private String mContent;

    /**
     * Indicates whether the connected user has voted
     * for this post or not.
     */
    private VoteValue mConnectedUserVote;

    /**
     * Number of each vote by vote value for this post
     */
    private HashMap<VoteValue, Integer> mVotes;

    public static final String DATE_FORMAT = "MMM, d yyyy HH:mm";

    public PostBean(UserBean creator, long date, String content) {
        mCreator = creator;
        mDate = date;
        mContent = content;
        mVotes = new HashMap<>();
    }

    public PostBean(long id, UserBean creator, long date, String content) {
        this(creator, date, content);
        mId = id;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public UserBean getCreator() {
        return mCreator;
    }

    public void setCreator(UserBean creator) {
        mCreator = creator;
    }

    public long getDate() {
        return mDate;
    }

    public void setDate(long date) {
        mDate = date;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public VoteValue getConnectedUserVote() {
        return mConnectedUserVote;
    }

    public void setConnectedUserVote(VoteValue vote) {
        mConnectedUserVote = vote;
    }

    public HashMap<VoteValue, Integer> getVotes() {
        return mVotes;
    }

    public void setVotes(HashMap<VoteValue, Integer> votes) {
        mVotes = votes;
    }

    public int getNbUpVotes() {
        return mVotes.get(VoteValue.UP_VOTE);
    }

    public int getNbDownVotes() {
        return mVotes.get(VoteValue.DOWN_VOTE);
    }

    public void addUpVote() {
        mVotes.put(VoteValue.UP_VOTE,
                mVotes.get(VoteValue.UP_VOTE) + 1);
    }

    public void deleteUpVote() {
        mVotes.put(VoteValue.UP_VOTE,
                mVotes.get(VoteValue.UP_VOTE) - 1);
    }

    public void addDownVote() {
        mVotes.put(VoteValue.DOWN_VOTE,
                mVotes.get(VoteValue.DOWN_VOTE) + 1);
    }

    public void deleteDownVote() {
        mVotes.put(VoteValue.DOWN_VOTE,
                mVotes.get(VoteValue.DOWN_VOTE) - 1);
    }
}
