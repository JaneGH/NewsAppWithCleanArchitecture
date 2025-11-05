package com.example.newsappwithcleanarchitecture.domain.repository

import com.example.newsappwithcleanarchitecture.domain.model.News
import com.example.newsappwithcleanarchitecture.util.ResultState
import kotlinx.coroutines.flow.Flow

interface INewsRepository {
    fun getLatestNews(): Flow<List<News>>
    suspend fun fetchAndCacheNews(): Flow<ResultState<Unit>>
}