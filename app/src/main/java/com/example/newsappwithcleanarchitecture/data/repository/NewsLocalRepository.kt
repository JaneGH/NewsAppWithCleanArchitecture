package com.example.newsappwithcleanarchitecture.data.repository

import com.example.newsappwithcleanarchitecture.data.local.NewsDao
import com.example.newsappwithcleanarchitecture.data.mapper.NewsMapper
import com.example.newsappwithcleanarchitecture.domain.model.News
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

    suspend fun saveNews(newsList: List<News>) {
        dao.clearNews()
        dao.insertAllNews(NewsMapper.mapDomainListToEntityList(newsList))
    }
}