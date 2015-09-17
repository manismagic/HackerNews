package com.hacker.news.pojo;


import android.os.Parcel;
import android.os.Parcelable;

public class Story implements Parcelable{
    private String title;
    private String author;
    private int score;
    private long time;
    private String url;
    private int commentCount;
    private int[] kids;
    private String content;


    public Story(){

    }

    protected Story(Parcel in) {
        title = in.readString();
        author = in.readString();
        score = in.readInt();
        time = in.readLong();
        url = in.readString();
        commentCount = in.readInt();
        kids = in.createIntArray();
        content = in.readString();
    }

    public static final Creator<Story> CREATOR = new Creator<Story>() {
        @Override
        public Story createFromParcel(Parcel in) {
            return new Story(in);
        }

        @Override
        public Story[] newArray(int size) {
            return new Story[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public long getTime() {
        return time;
    }

    public int getScore() {
        return score;
    }

    public String getUrl() {
        return url;
    }

    public String getAuthor() {
        return author;
    }


    public void setTitle(String title) {
        if(title == null || title.isEmpty()){
            this.title = null;
        }else{
            this.title = title;
        }

    }

    public void setAuthor(String author) {
        if(author == null || author.isEmpty()){
            this.author = "unknown";
        }else{
            this.author = author;
        }

    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setUrl(String url) {
        if(url == null || url.isEmpty()){
            this.url = null;
        }else{
            this.url = url;
        }
    }

    public int[] getKids() {
        return kids;
    }

    public void setKids(int[] kids) {
        this.kids = kids;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(author);
        dest.writeInt(score);
        dest.writeLong(time);
        dest.writeString(url);
        dest.writeInt(commentCount);
        dest.writeIntArray(kids);
        dest.writeString(content);
    }
}
