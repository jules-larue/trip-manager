package com.example.android.tripmanager.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.android.tripmanager.UserConnectedManager;
import com.example.android.tripmanager.VoteValue;
import com.example.android.tripmanager.database.DbHelper;
import com.example.android.tripmanager.database.bean.PostBean;
import com.example.android.tripmanager.database.bean.UserBean;

import java.util.ArrayList;
import java.util.HashMap;

public class PostDao {

    private DbHelper mDbHelper;

    private Context mContext;

    public PostDao(Context context) {
        mDbHelper = DbHelper.getHelper(context);
        mContext = context;
    }

    public long addPost(PostBean newPost) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = toContentValues(newPost);

        return db.insert(DbHelper.TABLE_POST, null, values);
    }

    public ArrayList<PostBean> getAllPostsOrderedByDate() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        ArrayList<PostBean> posts = new ArrayList<>();
        Cursor cursor = db.query(DbHelper.TABLE_POST + " natural join " + DbHelper.TABLE_USER,
                null,
                DbHelper.POST_CREATOR + " = " + DbHelper.USER_NICKNAME,
                null,
                null,
                null,
                DbHelper.POST_DATE + " DESC");

        while (cursor.moveToNext()) {
            PostBean post = toBean(cursor);

            // Check if the current user has voted for the post
            UserBean connectedUser = UserConnectedManager.getConnectedUser();
            Cursor cursorVote = db.query(DbHelper.TABLE_POST_VOTE,
                    null,
                    DbHelper.POST_VOTE_VOTER + " = ? AND " + DbHelper.POST_VOTE_POST_ID + " = ? ",
                    new String[]{ connectedUser.getNickname(), String.valueOf(post.getId()) },
                    null,
                    null,
                    null);

            if (!cursorVote.moveToFirst()) {
                // User has not voted
                post.setConnectedUserVote(VoteValue.NO_VOTE);
            } else {
                // User has voted
                int voteDb = cursorVote.getInt(cursorVote.getColumnIndex(DbHelper.POST_VOTE_VALUE));
                VoteValue userVote = VoteValue.getEnum(voteDb);
                post.setConnectedUserVote(userVote);
            }
            cursorVote.close();

            // Add the number of votes for this post
            post.setVotes(getNbVotes(post.getId()));

            // Add the post to the list
            posts.add(post);
        }
        cursor.close();

        return posts;
    }

    public void vote(String voterNickname, long postId, VoteValue vote) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbHelper.POST_VOTE_POST_ID, postId);
        values.put(DbHelper.POST_VOTE_VOTER, voterNickname);
        values.put(DbHelper.POST_VOTE_VALUE, vote.getVal());

        // Check if the user has already voted for this post
        if (getUserVote(voterNickname, postId) == VoteValue.NO_VOTE) {
            // Create the user's vote
            db.insert(DbHelper.TABLE_POST_VOTE,
                    null,
                    values);
        } else {
            // Update the user's vote
            db.update(DbHelper.TABLE_POST_VOTE,
                    values,
                    DbHelper.POST_VOTE_VOTER + " = ? AND " + DbHelper.POST_VOTE_POST_ID + " = ?",
                    new String[]{ voterNickname, String.valueOf(postId) });
        }
    }

    public void upVote(String voterNickname, long postId) {
        vote(voterNickname, postId, VoteValue.UP_VOTE);
    }

    public void downVote(String voterNickname, long postId) {
        vote(voterNickname, postId, VoteValue.DOWN_VOTE);
    }

    public void deleteVote(String voterNickname, long postId) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.delete(DbHelper.TABLE_POST_VOTE,
                DbHelper.POST_VOTE_VOTER + " = ? AND " + DbHelper.POST_VOTE_POST_ID + " = ?",
                new String[]{ voterNickname, String.valueOf(postId) });
    }

    /**
     * Calculates the total number of votes for one specific
     * post and for each vote value.
     * The result is a HashMap where keys are possible
     * vote values (VoteValue.UP_VOTE and VoteValue.DOWN_VOTE) and
     * the values are the number of votes for the post
     * and for this VoteValue.
     * For instance:
     * <code>
     * {
     *     VoteValue.UP_VOTE: 45,
     *     VoteValue.DOWN_VOTE: 23
     * }
     * </code>
     * @param postId the id of the post to get the votes from
     * @return a HashMap with the number of post votes for each
     * vote possible value and for the specific post.
     */
    public HashMap<VoteValue, Integer> getNbVotes(long postId) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor = db.query(DbHelper.TABLE_POST_VOTE,
                new String[]{ DbHelper.POST_VOTE_VALUE, "COUNT(" + DbHelper.POST_VOTE_VALUE + ")" },
                DbHelper.POST_VOTE_POST_ID + " = ?",
                new String[]{ String.valueOf(postId) },
                DbHelper.POST_VOTE_VALUE,
                null,
                null);

        HashMap<VoteValue, Integer> votesNumbers = new HashMap<>();

        while (cursor.moveToNext()) {
            /*
            Results in cursor are the form:
                * First column is the vote value (1, -1)
                * Second column is the number of votes for this value
             */
            VoteValue voteValue = VoteValue.getEnum(cursor.getInt(0));
            int nbVotes = cursor.getInt(1);
            votesNumbers.put(voteValue, nbVotes);
        }

        if (!votesNumbers.containsKey(VoteValue.UP_VOTE)) {
            votesNumbers.put(VoteValue.UP_VOTE, 0);
        }
        if (!votesNumbers.containsKey(VoteValue.DOWN_VOTE)) {
            votesNumbers.put(VoteValue.DOWN_VOTE, 0);
        }
        cursor.close();

        return votesNumbers;
    }

    /**
     * Checks whether a user has voted for a post
     * @param userNickname the nickname of the user
     * @param postId the id of the post
     * @return true if the user has voted for the post; false otherwise
     */
    public VoteValue getUserVote(String userNickname, long postId) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor = db.query(DbHelper.TABLE_POST_VOTE,
                null,
                DbHelper.POST_VOTE_VOTER + " = ? AND " + DbHelper.POST_VOTE_POST_ID + " = ?",
                new String[]{ userNickname, String.valueOf(postId) },
                null,
                null,
                null);

        VoteValue voteValue;
        if (cursor.moveToFirst()) {
            // User has voted
            int vote = cursor.getInt(0);
            if (vote == 1) {
                voteValue = VoteValue.UP_VOTE;
            } else {
                // Down vote
                voteValue = VoteValue.DOWN_VOTE;
            }
        } else {
            // User hasn't voted
            voteValue = VoteValue.NO_VOTE;
        }
        cursor.close();
        return voteValue;
    }


    public ContentValues toContentValues(PostBean post) {
        ContentValues values = new ContentValues();
        values.put(DbHelper.POST_CREATOR, post
                .getCreator()
                .getNickname());
        values.put(DbHelper.POST_DATE, post.getDate());
        values.put(DbHelper.POST_CONTENT, post.getContent());

        return values;
    }

    public static PostBean toBean(Cursor cursor) {
        UserBean creator = UserDao.toBean(cursor);
        long postId = cursor.getLong(cursor.getColumnIndex(DbHelper.POST_ID));
        long postDate = cursor.getLong(cursor.getColumnIndex(DbHelper.POST_DATE));
        String postContent = cursor.getString(cursor.getColumnIndex(DbHelper.POST_CONTENT));

        return new PostBean(postId, creator, postDate, postContent);
    }
}
