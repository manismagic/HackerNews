package com.hacker.news.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.hacker.news.HackerNewsApplication;
import com.hacker.news.R;
import com.hacker.news.activities.DetailedCommentActivity;
import com.hacker.news.adapters.TopStoriesAdapter;
import com.hacker.news.network.CacheRequest;
import com.hacker.news.network.VolleySingleton;
import com.hacker.news.pojo.Story;
import com.hacker.news.utils.Constants;
import com.hacker.news.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/*
   This fragment class used to show the Top Stories in the List
 */
public class TopStoriesFragment extends Fragment implements TopStoriesAdapter.ItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = TopStoriesFragment.class.getName();
    private static final String STATE_TOP_MOVIES = "state_top_movies";
    private static final String STATE_TOP_MOVIES_SIZE = "state_top_movies_size";
    private RecyclerView mRecyclerView;
    private TopStoriesAdapter mTopStoriesAdapter;
    private List<Story> mListTopStories = new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayout mNetWorkError;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_stories, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeTopStories);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorWhite);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.listTopStories);
        mTopStoriesAdapter = new TopStoriesAdapter(getActivity());
        mTopStoriesAdapter.setClickListener(this);
        mRecyclerView.setAdapter(mTopStoriesAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mNetWorkError = (LinearLayout) view.findViewById(R.id.cloud_network);
        mNetWorkError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });
        if (savedInstanceState == null) {
            mSwipeRefreshLayout.post(new Runnable() {
                                         @Override
                                         public void run() {
                                             mSwipeRefreshLayout.setRefreshing(true);
                                             sendTopStoriesRequest();
                                         }
                                     }
            );
        } else {
            mListTopStories = savedInstanceState.getParcelableArrayList(STATE_TOP_MOVIES);
            mTopStoriesAdapter.setListTopStories(mListTopStories);
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (Utility.isNetworkAvailable(HackerNewsApplication.getAppContext())) {
            mNetWorkError.setVisibility(View.GONE);
        } else {
            mNetWorkError.setVisibility(View.VISIBLE);
        }
    }

    /*
    Store the data when user changed the orientation
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_TOP_MOVIES_SIZE, mListTopStories.size());
        outState.putParcelableArrayList(STATE_TOP_MOVIES, (ArrayList) mListTopStories);
    }

    /*
    Make a request to get the Top Story list from the server
     */
    private void sendTopStoriesRequest() {
        CacheRequest topStoryJsonRequest = new CacheRequest(Request.Method.GET, Constants.getTopStoriesUrl(), new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    JSONArray jsonObject = new JSONArray(new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers)));
                    parseData(jsonObject);
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleNetworkError(error);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        VolleySingleton.getInstance().addToRequestQueue(topStoryJsonRequest, "top");
    }


    /*
    Parse the Top Story List from the response
     */
    private void parseData(JSONArray response) {
        Log.e(TAG, "parseData: " + response.length());
        for (int i = 0; i < response.length(); i++) {
            try {
                sendStoryItemRequest(response.getInt(i));
            } catch (JSONException e) {
                Log.e(TAG, "JSON Parsing error: " + e.getMessage());
            }
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }

     /*
    Make a request to get the each Story item from the server
     */

    public void sendStoryItemRequest(int item) {
        CacheRequest storyItemRequest = new CacheRequest(Request.Method.GET, Constants.getSingleItemUrl(item), new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                if (response != null) {
                    try {
                        parseStoryItem(new JSONObject(new String(response.data, HttpHeaderParser.parseCharset(response.headers))));
                    } catch (UnsupportedEncodingException | JSONException e) {
                        e.printStackTrace();
                    }
                }
                mTopStoriesAdapter.setListTopStories(mListTopStories);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleNetworkError(error);
            }
        });
        VolleySingleton.getInstance().addToRequestQueue(storyItemRequest, "item");
    }


    /*
    Parse the response of each item of the Story and store in the Array List
     */
    private void parseStoryItem(JSONObject response) {
        try {
            Story story = new Story();
            if (response.has(Constants.KEY_TITLE)) {
                story.setTitle(response.getString(Constants.KEY_TITLE));
            }
            if (response.has(Constants.KEY_AUTHOR)) {
                story.setAuthor(response.getString(Constants.KEY_AUTHOR));
            }
            if (response.has(Constants.KEY_SCORE)) {
                story.setScore(response.getInt(Constants.KEY_SCORE));
            }
            if (response.has(Constants.KEY_TIME)) {
                story.setTime(response.getLong(Constants.KEY_TIME));
            }
            if (response.has(Constants.KEY_URL)) {
                story.setUrl(response.getString(Constants.KEY_URL));
            }
            if (response.has(Constants.KEY_COMMENT_COUNT)) {
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
            mListTopStories.add(story);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void handleNetworkError(VolleyError error) {
        mNetWorkError.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        VolleySingleton.getInstance().cancelPendingRequests("top");
        VolleySingleton.getInstance().cancelPendingRequests("item");
    }

    /*
    Open a browser when user selected tap on the item in the list
     */
    @Override
    public void itemClicked(List<Story> list, int position) {
        String url = list.get(position).getUrl();
        if (null != url) {
            startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
        }
    }

    /*
    Launch Comment Activity to show the Comment for the selected item
     */

    @Override
    public void commentClicked(List<Story> list, int position) {
        int commentCount = list.get(position).getCommentCount();
        if (commentCount != 0) {
            Intent intent = new Intent(getActivity(), DetailedCommentActivity.class);
            intent.putExtra("commentCount", list.get(position).getCommentCount());
            int[] kids = list.get(position).getKids();
            intent.putExtra("kids", kids);
            startActivity(intent);
        }
    }

    /*
    Refresh the content
     */
    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        if (Utility.isNetworkAvailable(HackerNewsApplication.getAppContext())) {
            mNetWorkError.setVisibility(View.GONE);
            // TODO : Clear the cache and make a new request
            //VolleySingleton.getInstance().getRequestQueue().getCache().clear();
            //Utility.trimCache(getActivity());
            //VolleySingleton.getInstance().getRequestQueue().getCache().remove(Constants.getTopStoriesUrl());
            /*VolleySingleton.getInstance().getRequestQueue().getCache().invalidate("top", true);*/
        } else {
            mNetWorkError.setVisibility(View.VISIBLE);
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sendTopStoriesRequest();
            }
        });
    }
}