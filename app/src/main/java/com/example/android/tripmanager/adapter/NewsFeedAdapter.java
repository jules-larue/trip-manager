package com.example.android.tripmanager.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.tripmanager.R;
import com.example.android.tripmanager.UserConnectedManager;
import com.example.android.tripmanager.VoteValue;
import com.example.android.tripmanager.database.bean.PostBean;
import com.example.android.tripmanager.database.bean.UserBean;
import com.example.android.tripmanager.database.dao.PostDao;
import com.example.android.tripmanager.fragment.NewsFeedFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NewsFeedAdapter extends RecyclerView.Adapter {

    private NewsFeedFragment mFragment;
    private ArrayList<PostBean> mPosts;

    public NewsFeedAdapter(NewsFeedFragment fragment, ArrayList<PostBean> posts) {
        mFragment = fragment;
        mPosts = posts;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            // TextArea to post something
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.post_textarea_item, parent, false);
            return new PostTextAreaView(view);
        } else {
            // Post view
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.post_item, parent, false);
            return new PostItemView(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position != 0) {
            // Post item
            bindPostItem(holder, position);
        }
    }

    private void bindPostEditor(RecyclerView.ViewHolder holder, int position) {

    }

    private void bindPostItem(RecyclerView.ViewHolder holder, int position) {
        PostBean post = mPosts.get(position - 1);

        PostItemView postItemView = (PostItemView) holder;
        postItemView.postAuthor.setText(post.getCreator().getNickname());

        DateFormat dateFormat = new SimpleDateFormat(PostBean.DATE_FORMAT, Locale.UK);
        String formattedDate = dateFormat.format(post.getDate());
        postItemView.postDate.setText(formattedDate);
        postItemView.content.setText(post.getContent());
        postItemView.nbUpVotes.setText(String.valueOf(post.getNbUpVotes()));
        postItemView.nbDownVotes.setText(String.valueOf(post.getNbDownVotes()));
        initVoteButtons(post, postItemView);
    }

    private void initVoteButtons(PostBean post, PostItemView itemView) {
        /*
        Get the buttons to deselect.
        A button for which user has voted
        is selected.
         */
        ArrayList<ImageButton> buttonsToDeselect = new ArrayList<>();
        switch (post.getConnectedUserVote()) {
            case UP_VOTE:
                // User has up voted, so down thumb is deselected
                buttonsToDeselect.add(itemView.btnDownVote);
                break;

            case DOWN_VOTE:
                // User has down voted, so up thumb is deselected
                buttonsToDeselect.add(itemView.btnUpVote);
                break;

            case NO_VOTE:
                // User has not voted, so deselect all buttons
                buttonsToDeselect.add(itemView.btnUpVote);
                buttonsToDeselect.add(itemView.btnDownVote);
                break;
        }

        // Deselect buttons
        int deselectionColor = mFragment.getResources().getColor(R.color.deselected_thumb);
        for (ImageButton button : buttonsToDeselect) {
            button.setColorFilter(deselectionColor);
        }
    }

    @Override
    public int getItemCount() {
        return mPosts.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        // First item is different (TextArea)
        if (position == 0) {
            return 1;
        } else {
            return 2;
        }
    }


    private class PostTextAreaView extends RecyclerView.ViewHolder {

        private EditText editPost;
        private Button btnSendPost;

        public PostTextAreaView(View itemView) {
            super(itemView);
            editPost = itemView.findViewById(R.id.edit_post);
            btnSendPost = itemView.findViewById(R.id.btn_send_post);

            btnSendPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String postContent = editPost.getText().toString();

                    if (postContent.isEmpty()) {
                        // Post content empty
                        Toast.makeText(mFragment.getContext(), "Please enter a text first.", Toast.LENGTH_SHORT).show();
                    } else {
                        PostDao postDao = new PostDao(mFragment.getContext());

                        UserBean connectedUser = UserConnectedManager.getConnectedUser();
                        long nowMillis = new Date().getTime();

                        // Handle line breaks
                        //postContent = postContent.replaceAll("\\n", "<br/>");
                        PostBean newPost = new PostBean(connectedUser,
                                nowMillis,
                                postContent);
                        postDao.addPost(newPost);


                        // Close keyboard
                        InputMethodManager inputManager =
                                (InputMethodManager) mFragment.getContext()
                                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(
                                mFragment.getActivity().getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);

                        // Empty EditText and remove focus
                        editPost.setText("");
                        editPost.clearFocus();

                        // Reload posts in fragment
                        mFragment.loadPosts();
                    }
                }
            });
        }
    }


    private class PostItemView extends RecyclerView.ViewHolder {

        public TextView postAuthor;
        public TextView postDate;
        public TextView content;
        public ImageButton btnUpVote;
        public TextView nbUpVotes;
        public ImageButton btnDownVote;
        public TextView nbDownVotes;

        public PostItemView(View itemView) {
            super(itemView);
            postAuthor = itemView.findViewById(R.id.post_author);
            postDate = itemView.findViewById(R.id.post_date);
            content = itemView.findViewById(R.id.post_content);
            btnUpVote = itemView.findViewById(R.id.btn_up_vote);
            nbUpVotes = itemView.findViewById(R.id.nb_up_votes);
            btnDownVote = itemView.findViewById(R.id.btn_down_vote);
            nbDownVotes = itemView.findViewById(R.id.nb_down_votes);

            // Create click listener for vote buttons
            btnUpVote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PostBean post = mPosts.get(getAdapterPosition() - 1);
                    PostDao postDao = new PostDao(mFragment.getContext());
                    UserBean connectedUser = UserConnectedManager.getConnectedUser();
                    if (post.getConnectedUserVote() == VoteValue.UP_VOTE) {
                        // User has already up voted, so delete vote
                        postDao.deleteVote(connectedUser.getNickname(),
                                post.getId());

                        // Update model and view
                        post.setConnectedUserVote(VoteValue.NO_VOTE);
                        post.deleteUpVote();
                        deselectVoteButton(btnUpVote);
                    } else {
                        // User has not up voted yet
                        postDao.upVote(connectedUser.getNickname(), post.getId());
                        if (post.getConnectedUserVote() == VoteValue.DOWN_VOTE) {
                            post.deleteDownVote();
                        }

                        // Update model
                        post.setConnectedUserVote(VoteValue.UP_VOTE);
                        post.addUpVote();
                        selectVoteButton(btnUpVote);
                    }

                    // Update view
                    nbUpVotes.setText(String.valueOf(post.getNbUpVotes()));
                    nbDownVotes.setText(String.valueOf(post.getNbDownVotes()));
                }
            });


            btnDownVote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PostBean post = mPosts.get(getAdapterPosition() - 1);
                    PostDao postDao = new PostDao(mFragment.getContext());
                    UserBean connectedUser = UserConnectedManager.getConnectedUser();
                    if (post.getConnectedUserVote() == VoteValue.DOWN_VOTE) {
                        // User has already down voted, so delete vote
                        postDao.deleteVote(connectedUser.getNickname(),
                                post.getId());

                        // Update model and view
                        post.setConnectedUserVote(VoteValue.NO_VOTE);
                        post.deleteDownVote();
                        deselectVoteButton(btnDownVote);
                    } else {
                        // User has not down voted yet
                        postDao.downVote(connectedUser.getNickname(), post.getId());

                        if (post.getConnectedUserVote() == VoteValue.UP_VOTE) {
                            post.deleteUpVote();
                        }

                        // Update model
                        post.setConnectedUserVote(VoteValue.DOWN_VOTE);
                        post.addDownVote();
                        selectVoteButton(btnDownVote);
                    }

                    // Update view
                    nbUpVotes.setText(String.valueOf(post.getNbUpVotes()));
                    nbDownVotes.setText(String.valueOf(post.getNbDownVotes()));

                }
            });
        }

        private void selectVoteButton(ImageButton buttonToSelect) {
            buttonToSelect.setColorFilter(Color.BLACK);

            // Deselect other button
            ImageButton buttonToDeselect = (buttonToSelect == btnUpVote) ? btnDownVote : btnUpVote;
            deselectVoteButton(buttonToDeselect);
        }

        private void deselectVoteButton(ImageButton buttonToDeselect) {
            int deselectionColor = mFragment.getContext().getResources().getColor(R.color.deselected_thumb);
            buttonToDeselect.setColorFilter(deselectionColor);
        }
    }
}
