package com.hacker.news.activities;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.hacker.news.R;
import com.hacker.news.fragments.DetailedCommentFragment;

public class DetailedCommentActivity extends AppCompatActivity {
    private static final String TAG = DetailedCommentActivity.class.getName();
    private Toolbar toolbar;
    private DetailedCommentFragment mDetailedCommentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(String.format(getResources().getString(R.string.comment), getIntent().getIntExtra("commentCount", 0)));

        mDetailedCommentFragment = (DetailedCommentFragment)getSupportFragmentManager().findFragmentById(R.id.comment_fragment);
        mDetailedCommentFragment.setComment(getIntent().getIntArrayExtra("kids"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }
}
