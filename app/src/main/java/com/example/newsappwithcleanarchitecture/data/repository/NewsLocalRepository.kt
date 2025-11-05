package com.example.newsappwithcleanarchitecture.data.repository

import android.util.Log
import com.example.newsappwithcleanarchitecture.data.local.NewsDao
import com.example.newsappwithcleanarchitecture.data.mapper.NewsMapper
import com.example.newsappwithcleanarchitecture.domain.model.News
import com.example.newsappwithcleanarchitecture.util.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsLocalRepository @Inject constructor(
    private val dao: NewsDao
) {
    fun getAllNews(): Flow<List<News>> =
        dao.getAllNewsFlow().map { entities ->
            NewsMapper.mapEntityListToDomainList(entities)
        }

    suspend fun saveNews(newsList: List<News>): ResultState<Unit> {
        return try {
            dao.clearNews()
            dao.insertAllNews(NewsMapper.mapDomainListToEntityList(newsList))
            ResultState.Success(Unit)
        } catch (e: Exception) {
            Log.e("NewsLocalRepository", "Failed to save news", e)
            ResultState.Error(e.localizedMessage ?: "Unknown error")
        }
    }
}