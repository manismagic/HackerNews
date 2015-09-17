package com.hacker.news.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hacker.news.R;
import com.hacker.news.pojo.Story;
import com.hacker.news.utils.Utility;

import java.util.Collections;
import java.util.List;

public class DetailedCommentAdapter extends RecyclerView.Adapter<DetailedCommentAdapter.CommentViewHolder> {
    private static final String TAG = DetailedCommentAdapter.class.getName();
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<Story> listComment = Collections.emptyList();
    private CommentClickListener mCommentClickListener;

    public DetailedCommentAdapter(Context context){
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_comment, parent, false);
        CommentViewHolder viewHolder = new CommentViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        Story currentStory = listComment.get(position);
        holder.title.setText(TextUtils.isEmpty(currentStory.getContent()) ? "" : Html.fromHtml(currentStory.getContent()));
        holder.author.setText(Utility.getComparativeTime(mContext, currentStory.getTime(), currentStory.getAuthor()));
        holder.comment.setText(String.valueOf(currentStory.getKids()==null? 0 :currentStory.getKids().length));
    }

    @Override
    public int getItemCount() {
        return listComment.size();
    }

    public void setCommentList(List<Story> listComment) {
        this.listComment = listComment;
        notifyDataSetChanged();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView author;
        TextView comment;
        public CommentViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            author = (TextView) itemView.findViewById(R.id.author_time);
            comment = (TextView) itemView.findViewById(R.id.comment);
            comment.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    mCommentClickListener.commentClicked(getAdapterPosition());
                }
            });

        }
    }

    public void setClickListener(CommentClickListener commentClickListener){
        mCommentClickListener = commentClickListener;
    }

    public interface CommentClickListener{
        void commentClicked(int position);
    }

}
