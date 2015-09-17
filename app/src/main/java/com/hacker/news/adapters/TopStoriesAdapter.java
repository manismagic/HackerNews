package com.hacker.news.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hacker.news.R;
import com.hacker.news.pojo.Story;
import com.hacker.news.utils.Utility;

import java.util.Collections;
import java.util.List;

public class TopStoriesAdapter extends RecyclerView.Adapter<TopStoriesAdapter.TopStoriesViewHolder> {

    private static final String TAG = TopStoriesAdapter.class.getName();
    private Context mContext;
    private LayoutInflater inflater;
    private List<Story> listTopStories = Collections.emptyList();
    private ItemClickListener mItemClickListener;

    public TopStoriesAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public TopStoriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_top_stories, parent, false);
        TopStoriesViewHolder viewHolder = new TopStoriesViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TopStoriesViewHolder holder, int position) {
        Story currentStory = listTopStories.get(position);
        holder.title.setText(currentStory.getTitle());
        holder.author.setText(Utility.getComparativeTime(mContext, currentStory.getTime(), currentStory.getAuthor()));
        holder.score.setText(String.format(mContext.getResources().getString(R.string.points), currentStory.getScore()));
        holder.comment.setText(String.format(mContext.getResources().getString(R.string.comment), currentStory.getCommentCount()));
    }

    @Override
    public int getItemCount() {
        return listTopStories.size();
    }

    public void setListTopStories(List<Story> listTopStories) {
        this.listTopStories = listTopStories;
        notifyDataSetChanged();
    }

    class TopStoriesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView author;
        TextView comment;
        TextView score;
        LinearLayout commentLayout;

        public TopStoriesViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.title);
            author = (TextView) itemView.findViewById(R.id.author_time);
            score = (TextView) itemView.findViewById(R.id.score);
            comment = (TextView) itemView.findViewById(R.id.comment);
            commentLayout = (LinearLayout) itemView.findViewById(R.id.layout_comment);
            commentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mItemClickListener) {
                        mItemClickListener.commentClicked(getAdapterPosition());
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            if (null != mItemClickListener) {
                mItemClickListener.itemClicked(v, getAdapterPosition());
            }
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void itemClicked(View view, int position);

        void commentClicked(int position);
    }
}
