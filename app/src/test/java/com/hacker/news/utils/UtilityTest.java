package com.hacker.news.utils;

import android.os.Build;
import com.hacker.news.BuildConfig;
import com.hacker.news.HackerNewsApplication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class UtilityTest {
    @Test
    public void testIsNetworkAvailable() throws Exception {
        assertTrue(Utility.isNetworkAvailable(HackerNewsApplication.getAppContext()));
    }
}