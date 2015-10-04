package com.hacker.news.activities;

import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;


import com.hacker.news.BuildConfig;
import com.hacker.news.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
public class PopularNewsActivityTest {

    PopularNewsActivity mPopularNewsActivity;
    FragmentManager mFragmentManager;
    DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    @Before
    public void setup() {
        mPopularNewsActivity = Robolectric.setupActivity(PopularNewsActivity.class);
        toolbar = (Toolbar) mPopularNewsActivity.findViewById(R.id.tool_bar);
        mDrawerLayout = (DrawerLayout) mPopularNewsActivity.findViewById(R.id.drawer_layout);
        mFragmentManager = mPopularNewsActivity.getSupportFragmentManager();
    }

    @Test
    public void testNotNull() {
        assertNotNull(mPopularNewsActivity);
        assertNotNull(toolbar);
        assertNotNull(mDrawerLayout);
    }

    @Test
    public void testToolBarTitle() throws Exception{
        assertEquals(mPopularNewsActivity.getString(R.string.top_stories), mPopularNewsActivity.getSupportActionBar().getTitle());
    }

}