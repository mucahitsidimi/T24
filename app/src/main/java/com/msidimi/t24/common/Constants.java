package com.msidimi.t24.common;

/**
 * Created by mucahit on 18/09/16.
 */
public class Constants {

    /* APP Global Settings*/
    public static final long NEWS_REFRESH_TIME = 1000 * 60 * 2;  // split-second * second * minute
    public static final int SLIDER_DURATION = 3000;
    public static final int SOCKET_TIMEOUT = 10000;
    public static final int SPLASH_TIME = 1000;

    /* API Request Tag */
    public static final String API_TAG_GET_CATEGORIES = "T1";
    public static final String API_TAG_GET_NEWS = "T2";
    public static final String API_TAG_GET_NEWS_DETAIL = "T3";
    public static final String API_TAG_GET_SLIDER_NEWS = "T4";

    /* Intent Key */
    public static final String IK_NEWS_ID = "news_id";
    public static final String IK_NEWS_ARRAY_LIST = "news_array_list";
    public static final String IK_NEWS_POSITION = "news_position";
}
