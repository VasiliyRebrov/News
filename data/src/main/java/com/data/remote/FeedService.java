package com.data.remote;


import com.data.remote.entities.RssRoot;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FeedService {
    @GET("https://www.vesti.ru/vesti.rss")
    Call<RssRoot> loadRSSFeed();
}