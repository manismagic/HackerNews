package com.hacker.news.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.hacker.news.R;
import com.hacker.news.activities.DetailedCommentActivity;
import com.hacker.news.adapters.DetailedCommentAdapter;
import com.hacker.news.network.PriorityRequest;
import com.hacker.news.network.VolleySingleton;
import com.hacker.news.pojo.Story;
import com.hacker.news.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/*
This fragment class is used to show the comments for the item selected
 */

public class DetailedCommentFragment extends Fragment implements DetailedCommentAdapter.CommentClickListener{
    private static final String TAG = DetailedCommentFragment.class.getName();
    private int[] comments;
    private RecyclerView mRecyclerView;
    private DetailedCommentAdapter mDetailedCommentAdapter;
    List<Story> mCommentStories = new ArrayList<>();
    private TextView mNetworkError;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.listComments);
        mDetailedCommentAdapter = new DetailedCommentAdapter(getActivity());
        mDetailedCommentAdapter.setClickListener(this);
        mRecyclerView.setAdapter(mDetailedCommentAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mNetworkError = (TextView) view.findViewById(R.id.commentNetworkError);
        return view;
    }

    public void setComment(int[] comments) {
        this.comments = comments;
        sendCommentRequest(comments);
    }

    private void sendCommentRequest(int[] items) {
        for (int i = 0; i < items.length; i++) {
            sendCommentItemRequest(items[i]);
        }
    }

    /*
    Send a request to get the comment information from the server
     */
    public void sendCommentItemRequest(int item) {
        Log.d(TAG, "Comment Item Request:: " + item);
        PriorityRequest storyItemRequest = new PriorityRequest(Request.Method.GET,
                Constants.getSingleItemUrl(item), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mNetworkError.setVisibility(View.VISIBLE);
                if (response != null) {
                    parseCommentItem(response);
                }
                mDetailedCommentAdapter.setCommentList(mCommentStories);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleNetworkError(error);
            }
        });
        storyItemRequest.setTag("comment");
        VolleySingleton.getInstance().getRequestQueue().add(storyItemRequest);
    }

    /*
    Parse the comment item from the response object
     */
    private void parseCommentItem(JSONObject response) {
        try {
            Story story = new Story();
            if (response.has(Constants.KEY_CONTENT)) {
                String content = response.getString(Constants.KEY_CONTENT);
                if(content == null || content.isEmpty()){
                    return;
                }
                story.setContent(content);
            }
            if (response.has(Constants.KEY_AUTHOR)) {
                story.setAuthor(response.getString(Constants.KEY_AUTHOR));
            }
            if (response.has(Constants.KEY_TIME)) {
                story.setTime(response.getLong(Constants.KEY_TIME));
            }

            if(response.has(Constants.KEY_COMMENT_COUNT)){
                story.setCommentCount(response.getInt(Constants.KEY_COMMENT_COUNT));
            }

            if (response.has(Constants.KEY_KIDS)) {
                int[] kids;
                JSONArray kidsData = response.getJSONArray(Constants.KEY_KIDS);
                if (kidsData.length() > 0) {
                    kids = new int[kidsData.length()];
                    for (int i = 0; i < kids.length; i++) {
                        kids[i] = kidsData.getInt(i);
                    }
                    story.setKids(kids);
                }
            }
            mCommentStories.add(story);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
    Handle the network error
     */
    private void handleNetworkError(VolleyError error) {
        mNetworkError.setVisibility(View.VISIBLE);
        if (error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof NetworkError) {
            mNetworkError.setText(R.string.error_comment_network);
        } else if (error instanceof ParseError) {
            mNetworkError.setText(R.string.error_parser);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        VolleySingleton.getInstance().cancelPendingRequests("comment");
    }

    /*
    To handle the comment information for the particular comment
     */

    @Override
    public void commentClicked(int position) {
        VolleySingleton.getInstance().cancelPendingRequests("comment");
        int[] kids = mCommentStories.get(position).getKids();
        if(kids!= null && kids.length > 0){
            Intent intent = new Intent(getActivity(), DetailedCommentActivity.class);
            intent.putExtra("commentCount", kids.length);
            intent.putExtra("kids", kids);
            startActivity(intent);
        }
    }
}
