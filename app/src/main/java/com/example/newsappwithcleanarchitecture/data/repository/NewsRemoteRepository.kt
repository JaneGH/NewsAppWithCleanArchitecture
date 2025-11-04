package com.example.newsappwithcleanarchitecture.data.repository

import android.util.Log
import com.example.newsappwithcleanarchitecture.data.mapper.NewsMapper
import com.example.newsappwithcleanarchitecture.data.remote.ApiService
import com.example.newsappwithcleanarchitecture.domain.model.News
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NewsRemoteRepository @Inject constructor(
    private val api: ApiService
) {
    fun fetchNews(): Flow<List<News>> = flow {
        try {
            val response = api.getAllNews()
            val newsList = NewsMapper.mapDtoListToDomainList(response.news)
            emit(newsList)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("NewsRemoteRepository", "Error fetching news", e)
            emit(emptyList())
        }
    }
}