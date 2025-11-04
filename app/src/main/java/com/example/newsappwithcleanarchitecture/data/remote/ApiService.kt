package com.example.newsappwithcleanarchitecture.data.remote

import com.example.newsappwithcleanarchitecture.data.remote.response.NewsSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("latest-news")
    suspend fun getAllNews(
        @Query("language") language: String = "en",
        @Query("keywords") keywords: String = "",
    ): NewsSearchResponse
}