package com.example.newsappwithcleanarchitecture.data.remote.response

import com.example.newsappwithcleanarchitecture.data.remote.NewsDTO

data class NewsSearchResponse (
    val status: String,
    val news: List<NewsDTO>
)