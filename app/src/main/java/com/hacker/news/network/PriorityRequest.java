package com.hacker.news.network;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

/*
This class's used to make a request based on priority
 */

public class PriorityRequest extends JsonObjectRequest {

    private Priority priority = Priority.HIGH;
    public PriorityRequest(int method, String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    @Override
    public Priority getPriority(){
        return priority;
    }

    public void setPriority(Priority priority){
        this.priority = priority;
    }

}



