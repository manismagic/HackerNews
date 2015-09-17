package com.hacker.news.network;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.hacker.news.HackerNewsApplication;

/*
This singleton class is used to post the request to the server
 */

public class VolleySingleton {

    private static final String TAG = VolleySingleton.class.getName();
    private static VolleySingleton sInstance = null;
    private RequestQueue mRequestQueue;
    private Context mContext;

    VolleySingleton(Context context) {
        mContext = context;
        getRequestQueue();
    }

    public static VolleySingleton getInstance() {
        if (sInstance == null) {
            sInstance = new VolleySingleton(HackerNewsApplication.getAppContext());
        }
        return sInstance;
    }

    /*
    Get the Request Queue object
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(HackerNewsApplication.getAppContext());
        }
        return mRequestQueue;
    }

    /*
    Add a request queue with the Tag
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    /*
    Add a request queue
     */
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    /*
        Cancel a request queue based the Tag
    */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
