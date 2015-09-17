package com.hacker.news;

import android.app.Application;
import android.content.Context;


public class HackerNewsApplication extends Application {


    private static HackerNewsApplication sInstance;


    public static HackerNewsApplication getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }


}
