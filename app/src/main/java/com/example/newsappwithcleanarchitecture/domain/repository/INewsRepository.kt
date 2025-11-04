package com.example.newsappwithcleanarchitecture.domain.repository

import com.example.newsappwithcleanarchitecture.domain.model.News
import kotlinx.coroutines.flow.Flow

interface INewsRepository {
    fun getLatestNews(): Flow<List<News>>
    suspend fun fetchAndCacheNews()
}