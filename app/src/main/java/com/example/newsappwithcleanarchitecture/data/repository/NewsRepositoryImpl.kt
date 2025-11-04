package com.example.newsappwithcleanarchitecture.data.repository

import com.example.newsappwithcleanarchitecture.domain.model.News
import com.example.newsappwithcleanarchitecture.domain.repository.INewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val local: NewsLocalRepository,
    private val remote: NewsRemoteRepository
) : INewsRepository {

    override fun getLatestNews(): Flow<List<News>> = local.getAllNews()

    override suspend fun fetchAndCacheNews() {
        try {
            remote.fetchNews()
                .collect { newsList ->
                    local.saveNews(newsList)
                }
        } catch (e: Exception) {
            e
        }
    }
}
