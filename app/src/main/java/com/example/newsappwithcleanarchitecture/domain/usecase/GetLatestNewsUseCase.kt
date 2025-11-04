package com.example.newsappwithcleanarchitecture.domain.usecase

import com.example.newsappwithcleanarchitecture.domain.model.News
import com.example.newsappwithcleanarchitecture.domain.repository.INewsRepository
import kotlinx.coroutines.flow.Flow

class GetLatestNewsUseCase(private val repository: INewsRepository) {
    operator fun invoke(): Flow<List<News>> = repository.getLatestNews()
}
