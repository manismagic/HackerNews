package com.hacker.news.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.SpannableString;
import android.text.format.DateUtils;
import android.util.Log;

import java.io.File;

public class Utility {

    private static final String TAG = Utility.class.getName();

    public static SpannableString getComparativeTime(Context context, long time, String author){
        CharSequence relativeTime = "";
        try {
            relativeTime = DateUtils.getRelativeDateTimeString(context, time * 1000,
                    DateUtils.MINUTE_IN_MILLIS,
                    DateUtils.YEAR_IN_MILLIS,
                    DateUtils.FORMAT_ABBREV_MONTH);
        } catch (NullPointerException e) {
        }

        return new SpannableString(String.format("by %s, %s", author, relativeTime ));
    }

    /*
    To check the network availability
     */

    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info == null){
                return false;
            }else{
                return true;
            }
        }
        return false;
    }

    public static void trimCache(Context context) {
        Log.d(TAG, "trimCache");
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static boolean deleteDir(File dir) {
        Log.d(TAG, "deleteDir");
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                Log.d(TAG, "deleteDir:: " + children[i]);
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

}
