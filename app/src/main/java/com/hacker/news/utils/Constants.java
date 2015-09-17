package com.hacker.news.utils;


public class Constants {
    private static final String baseUrl = "https://hacker-news.firebaseio.com/v0/";
    private static final String topStoriesUrl = "topstories";
    private static final String singleItemUrl = "item/";
    private static final String extension = ".json";

    public static final String KEY_TITLE = "title";
    public static final String KEY_AUTHOR = "by";
    public static final String KEY_SCORE = "score";
    public static final String KEY_TIME = "time";
    public static final String KEY_URL = "url";
    public static final String KEY_KIDS = "kids";
    public static final String KEY_COMMENT_COUNT = "descendants";
    public static final String KEY_CONTENT = "text";

    public static final String PREF_FILE_NAME = "hacker_pref";
    public static final String KEY_USER_LEARNED = "user_learned_drawer";

    public static String getTopStoriesUrl() {
        return baseUrl + topStoriesUrl + extension;
    }

    public static String getSingleItemUrl(int item) {
        return baseUrl + singleItemUrl + item + extension;
    }
}
