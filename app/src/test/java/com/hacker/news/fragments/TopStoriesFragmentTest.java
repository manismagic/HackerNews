package com.hacker.news.fragments;

import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;


import com.hacker.news.BuildConfig;
import com.hacker.news.R;
import com.hacker.news.activities.PopularNewsActivity;
import com.hacker.news.adapters.TopStoriesAdapter;
import com.hacker.news.pojo.Story;
import com.hacker.news.pojo.StoryTest;

import org.assertj.android.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.Implements;
import org.robolectric.util.ActivityController;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNotNull;

@Implements(value = SwipeRefreshLayout.class, inheritImplementationMethods = true)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
public class TopStoriesFragmentTest{

    private ActivityController<PopularNewsActivity> mActivityActivityController;
    private PopularNewsActivity mPopularNewsActivity;
    private TopStoriesAdapter mTopStoriesAdapter;
    private TopStoriesAdapter.TopStoriesViewHolder holder;
    private List<Story> mStoryTestList;
    private RecyclerView mRecyclerView;

    @Before
    public void setUp() throws Exception {
        mActivityActivityController = Robolectric.buildActivity(PopularNewsActivity.class)
                .create().start().resume().visible();
        mPopularNewsActivity = mActivityActivityController.get();
    }

    @Test
    public void testOnCreateView() throws Exception {
        mPopularNewsActivity.getSupportFragmentManager()
                .beginTransaction()
                .add(new TopStoriesFragment(), null)
                .commit();
    }

    @Test
    public void testTopStories() {
        mRecyclerView = (RecyclerView) mPopularNewsActivity.findViewById(R.id.listTopStories);
        mTopStoriesAdapter = (TopStoriesAdapter)mRecyclerView.getAdapter();
        holder = mTopStoriesAdapter.createViewHolder(mRecyclerView, 0);
        assertNotNull(holder);
        StoryTest storyTest = new StoryTest();
        Story story = storyTest.testCreateStory();
        mStoryTestList = new ArrayList<>();
        mStoryTestList.add(story);
        mTopStoriesAdapter.setListTopStories(mStoryTestList);
        mTopStoriesAdapter.bindViewHolder(holder, 0);
        Assertions.assertThat((TextView) holder.itemView.findViewById(R.id.title)).hasText("My YC app: Dropbox - Throw away your USB drive");
        Assertions.assertThat((TextView) holder.itemView.findViewById(R.id.score)).hasText("Points (111)");
        Assertions.assertThat((TextView) holder.itemView.findViewById(R.id.comment)).hasText("Comments (71)");
    }


    @Test
    public void testCommentClicked() throws Exception {
        mRecyclerView = (RecyclerView) mPopularNewsActivity.findViewById(R.id.listTopStories);
        mTopStoriesAdapter = (TopStoriesAdapter)mRecyclerView.getAdapter();
/*        holder = mTopStoriesAdapter.createViewHolder(mRecyclerView, 0);
        assertNotNull(holder);*/
        StoryTest storyTest = new StoryTest();
        Story story = storyTest.testCreateStory();
        mStoryTestList = new ArrayList<>();
        mStoryTestList.add(story);
        /*mTopStoriesAdapter.setListTopStories(mStoryTestList);
        mTopStoriesAdapter.bindViewHolder(holder, 0);
        View commentButton = holder.itemView.findViewById(R.id.layout_comment);
        commentButton.performClick();*/
        mTopStoriesAdapter.mItemClickListener.commentClicked(mStoryTestList, 0);
    }

    @Test
    public void testItemClicked() throws Exception {
        mRecyclerView = (RecyclerView) mPopularNewsActivity.findViewById(R.id.listTopStories);
        mTopStoriesAdapter = (TopStoriesAdapter)mRecyclerView.getAdapter();
        /*holder = mTopStoriesAdapter.createViewHolder(mRecyclerView, 0);
        assertNotNull(holder);*/
        StoryTest storyTest = new StoryTest();
        Story story = storyTest.testCreateStory();
        mStoryTestList = new ArrayList<>();
        mStoryTestList.add(story);
        /*mTopStoriesAdapter.setListTopStories(mStoryTestList);
        mTopStoriesAdapter.bindViewHolder(holder, 0);*/
        //holder.itemView.performClick();
        mTopStoriesAdapter.mItemClickListener.itemClicked(mStoryTestList, 0);
    }

    @After
    public void tearDown() throws Exception {
        mActivityActivityController.pause().stop().destroy();
    }
}