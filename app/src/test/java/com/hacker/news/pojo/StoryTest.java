package com.hacker.news.pojo;

import android.os.Build;
import android.os.Parcel;


import com.hacker.news.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class StoryTest {

    public Story testCreateStory() {

        String title = "My YC app: Dropbox - Throw away your USB drive";
        String author = "dhouston";
        int score = 111;
        long time = 1175714200;
        String url = "http://www.getdropbox.com/u/2/screencast.html";
        int commentCount = 71;
        int[] kids = {8952, 9224, 8917 };

        Story story = new Story();
        story.setTitle(title);
        story.setAuthor(author);
        story.setScore(score);
        story.setTime(time);
        story.setCommentCount(commentCount);
        story.setUrl(url);
        story.setKids(kids);

        return story;
    }

    @Test
    public void testWriteToParcel() throws Exception {
        Story story = testCreateStory();

        Parcel parcel = Parcel.obtain();
        story.writeToParcel(parcel, 0);

        parcel.setDataPosition(0);

        Story storyFromParcel = Story.CREATOR.createFromParcel(parcel);
        assertEquals(storyFromParcel.getTitle(), story.getTitle());
        assertEquals(storyFromParcel.getAuthor(), story.getAuthor());
        assertEquals(storyFromParcel.getScore(), story.getScore());
        assertEquals(storyFromParcel.getTime(), story.getTime());
        assertEquals(storyFromParcel.getCommentCount(), story.getCommentCount());
        assertEquals(storyFromParcel.getUrl(), story.getUrl());
        assertEquals(storyFromParcel.getKids().length, story.getKids().length);
    }
}