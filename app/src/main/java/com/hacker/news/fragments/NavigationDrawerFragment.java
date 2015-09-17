package com.hacker.news.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.support.v7.widget.Toolbar;

import com.hacker.news.R;
import com.hacker.news.activities.PopularNewsActivity;
import com.hacker.news.utils.Constants;

public class NavigationDrawerFragment extends Fragment {
    private static final String TAG = NavigationDrawerFragment.class.getName();
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private View mContainer;
    public NavigationDrawerFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(readFromPreference(getActivity(), Constants.KEY_USER_LEARNED, "false"));
        if(savedInstanceState != null){
            mFromSavedInstanceState = true;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
    }

    /*
    Initialize the Navigation Drawer
     */
    public void init(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolBar) {
        mContainer = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolBar, R.string.drawer_open, R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if(!mUserLearnedDrawer){
                    mUserLearnedDrawer = true;
                    saveToPreference(getActivity(), Constants.KEY_USER_LEARNED, mUserLearnedDrawer+"");
                }
                getActivity().supportInvalidateOptionsMenu();
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().supportInvalidateOptionsMenu();
                toolBar.clearAnimation();
            }
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (Build.VERSION.SDK_INT < 11) {
                    final AlphaAnimation animation = new AlphaAnimation(1 - slideOffset, 0.6f);
                    animation.setDuration(0);
                    animation.setFillAfter(true);
                    toolBar.startAnimation(animation);
                } else if (slideOffset < 0.6) {
                    toolBar.setAlpha(1 - slideOffset);
                }
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
                if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
                    mDrawerLayout.openDrawer(mContainer);
                }
            }
        });
    }


    /*
    To store the information about the user understood the Navigation drawer support for this application
     */
    public static void saveToPreference(Context context, String prefName, String prefValue){
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(prefName, prefValue);
        editor.apply();
    }

    /*
    Read the information from the shared preference
     */
    public static String readFromPreference(Context context, String prefName, String defaultValue){
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPref.getString(prefName, defaultValue);
    }
}
